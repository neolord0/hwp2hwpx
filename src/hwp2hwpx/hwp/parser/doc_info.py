"""DocInfo section parser for HWP format."""

from typing import List, Tuple

from hwp2hwpx.hwp.enums.border_fill import (
    BorderThickness,
    BorderType,
    DiagonalType,
    GradientType,
    ImageFillType,
    PatternType,
)
from hwp2hwpx.hwp.enums.para_shape import (
    LineSpacingType,
    ParagraphNumberFormat,
    TabLeader,
    TabType,
    ValueType,
)
from hwp2hwpx.hwp.enums.tags import HWPTag
from hwp2hwpx.hwp.models.bin_data import BinDataCompress, BinDataItem, BinDataState, BinDataType
from hwp2hwpx.hwp.models.border_fill import (
    Border,
    BorderFill,
    BorderLine,
    FillInfo,
    GradientColor,
    GradientFill,
    ImageFill,
    PatternFill,
)
from hwp2hwpx.hwp.models.bullet import Bullet
from hwp2hwpx.hwp.models.char_shape import (
    CharShape,
    CharShapeFontRef,
    CharShapeOffset,
    CharShapeRatio,
    CharShapeRelSize,
    CharShapeSpacing,
)
from hwp2hwpx.hwp.models.doc_info import (
    CompatibleDocument,
    DocInfo,
    DocumentProperties,
    IDMappings,
    LayoutCompatibility,
    MemoShape,
    TrackChange,
    TrackChangeAuthor,
)
from hwp2hwpx.hwp.models.face_name import FaceName, FontMetrics, FontSubstituteInfo
from hwp2hwpx.hwp.models.numbering import Numbering, NumberingLevel
from hwp2hwpx.hwp.models.para_shape import ParaMargin, ParaShape
from hwp2hwpx.hwp.models.style import Style
from hwp2hwpx.hwp.models.tab_def import TabDef, TabItem
from hwp2hwpx.hwp.parser.record import Record, parse_records_as_objects
from hwp2hwpx.util.binary import (
    get_bits,
    read_bytes,
    read_int16,
    read_int32,
    read_uint8,
    read_uint16,
    read_uint32,
    read_wchar_string,
    read_wchar_string_with_length,
)
from hwp2hwpx.util.color import Color4Byte


def parse_doc_info(data: bytes) -> DocInfo:
    """Parse DocInfo section from decompressed data.

    Args:
        data: Decompressed DocInfo stream data

    Returns:
        Parsed DocInfo object
    """
    doc_info = DocInfo()
    records = parse_records_as_objects(data)

    current_font_type = 0  # Track which font type we're parsing

    for record in records:
        tag = record.tag_id

        if tag == HWPTag.DOCUMENT_PROPERTIES:
            doc_info.document_properties = _parse_document_properties(record.data)

        elif tag == HWPTag.ID_MAPPINGS:
            doc_info.id_mappings = _parse_id_mappings(record.data)

        elif tag == HWPTag.BIN_DATA:
            item = _parse_bin_data_item(record.data)
            doc_info.bin_data_items.append(item) if hasattr(doc_info, 'bin_data_items') else None

        elif tag == HWPTag.FACE_NAME:
            face_name = _parse_face_name(record.data)
            doc_info.face_names.add(face_name, current_font_type)
            # Increment font type after adding to appropriate list
            font_counts = [
                doc_info.id_mappings.korean_font_count,
                doc_info.id_mappings.english_font_count,
                doc_info.id_mappings.chinese_font_count,
                doc_info.id_mappings.japanese_font_count,
                doc_info.id_mappings.other_font_count,
                doc_info.id_mappings.symbol_font_count,
                doc_info.id_mappings.user_font_count,
            ]
            current_list = doc_info.face_names.get_by_script_type(current_font_type)
            if len(current_list) >= font_counts[current_font_type]:
                current_font_type += 1
                if current_font_type > 6:
                    current_font_type = 6

        elif tag == HWPTag.BORDER_FILL:
            border_fill = _parse_border_fill(record.data)
            doc_info.border_fills.add(border_fill)

        elif tag == HWPTag.CHAR_SHAPE:
            char_shape = _parse_char_shape(record.data)
            doc_info.char_shapes.add(char_shape)

        elif tag == HWPTag.TAB_DEF:
            tab_def = _parse_tab_def(record.data)
            doc_info.tab_defs.add(tab_def)

        elif tag == HWPTag.NUMBERING:
            numbering = _parse_numbering(record.data)
            doc_info.numberings.add(numbering)

        elif tag == HWPTag.BULLET:
            bullet = _parse_bullet(record.data)
            doc_info.bullets.add(bullet)

        elif tag == HWPTag.PARA_SHAPE:
            para_shape = _parse_para_shape(record.data)
            doc_info.para_shapes.add(para_shape)

        elif tag == HWPTag.STYLE:
            style = _parse_style(record.data)
            doc_info.styles.add(style)

        elif tag == HWPTag.MEMO_SHAPE:
            memo_shape = _parse_memo_shape(record.data)
            doc_info.memo_shapes.append(memo_shape)

        elif tag == HWPTag.TRACK_CHANGE:
            track_change = _parse_track_change(record.data)
            doc_info.track_changes.append(track_change)

        elif tag == HWPTag.TRACK_CHANGE_AUTHOR:
            author = _parse_track_change_author(record.data)
            doc_info.track_change_authors.append(author)

        elif tag == HWPTag.COMPATIBLE_DOCUMENT:
            doc_info.compatible_document = _parse_compatible_document(record.data)

        elif tag == HWPTag.LAYOUT_COMPATIBILITY:
            doc_info.layout_compatibility = _parse_layout_compatibility(record.data)

    return doc_info


def _parse_document_properties(data: bytes) -> DocumentProperties:
    """Parse document properties record."""
    props = DocumentProperties()
    offset = 0

    props.section_count, offset = read_uint16(data, offset)
    props.starting_page_number, offset = read_uint16(data, offset)
    props.starting_footnote_number, offset = read_uint16(data, offset)
    props.starting_endnote_number, offset = read_uint16(data, offset)
    props.starting_picture_number, offset = read_uint16(data, offset)
    props.starting_table_number, offset = read_uint16(data, offset)
    props.starting_equation_number, offset = read_uint16(data, offset)

    if len(data) > offset:
        props.list_id, offset = read_uint32(data, offset)
        props.paragraph_id, offset = read_uint32(data, offset)
        props.paragraph_char_unit_location, offset = read_uint32(data, offset)

    return props


def _parse_id_mappings(data: bytes) -> IDMappings:
    """Parse ID mappings record."""
    mappings = IDMappings()
    offset = 0

    mappings.bin_data_count, offset = read_int32(data, offset)
    mappings.korean_font_count, offset = read_int32(data, offset)
    mappings.english_font_count, offset = read_int32(data, offset)
    mappings.chinese_font_count, offset = read_int32(data, offset)
    mappings.japanese_font_count, offset = read_int32(data, offset)
    mappings.other_font_count, offset = read_int32(data, offset)
    mappings.symbol_font_count, offset = read_int32(data, offset)
    mappings.user_font_count, offset = read_int32(data, offset)
    mappings.border_fill_count, offset = read_int32(data, offset)
    mappings.char_shape_count, offset = read_int32(data, offset)
    mappings.tab_def_count, offset = read_int32(data, offset)
    mappings.numbering_count, offset = read_int32(data, offset)
    mappings.bullet_count, offset = read_int32(data, offset)
    mappings.para_shape_count, offset = read_int32(data, offset)
    mappings.style_count, offset = read_int32(data, offset)

    if len(data) > offset:
        mappings.memo_count, offset = read_int32(data, offset)

    if len(data) > offset:
        mappings.track_change_count, offset = read_int32(data, offset)
        mappings.track_change_author_count, offset = read_int32(data, offset)

    return mappings


def _parse_bin_data_item(data: bytes) -> BinDataItem:
    """Parse binary data item record."""
    offset = 0
    properties, offset = read_uint16(data, offset)

    data_type = BinDataType(properties & 0x0F)
    compress = BinDataCompress((properties >> 4) & 0x03)
    state = BinDataState((properties >> 6) & 0x03)

    item = BinDataItem(
        bin_data_id=0,
        data_type=data_type,
        compress_mode=compress,
        state=state,
    )

    if data_type == BinDataType.LINK:
        item.absolute_path, offset = read_wchar_string_with_length(data, offset)
        item.relative_path, offset = read_wchar_string_with_length(data, offset)
    elif data_type in (BinDataType.EMBEDDING, BinDataType.STORAGE):
        bin_data_id, offset = read_uint16(data, offset)
        item.bin_data_id = bin_data_id
        if len(data) > offset:
            item.extension, offset = read_wchar_string_with_length(data, offset)

    return item


def _parse_face_name(data: bytes) -> FaceName:
    """Parse face name record."""
    offset = 0
    font_type, offset = read_uint8(data, offset)
    name, offset = read_wchar_string_with_length(data, offset)

    face_name = FaceName(name=name, font_type=font_type)

    # Substitute info
    if font_type & 0x20:
        sub_info = FontSubstituteInfo()
        sub_info.font_type, offset = read_uint8(data, offset)
        sub_info.serif, offset = read_uint8(data, offset)
        sub_info.weight, offset = read_uint8(data, offset)
        face_name.substitute_info = sub_info

    # Font metrics
    if font_type & 0x40:
        metrics = FontMetrics()
        panose, offset = read_bytes(data, offset, 10)
        metrics.panose = panose
        metrics.default_width, offset = read_uint8(data, offset)
        metrics.default_height, offset = read_uint8(data, offset)
        face_name.metrics = metrics

    # Default font name
    if font_type & 0x80:
        face_name.default_font_name, offset = read_wchar_string_with_length(data, offset)

    return face_name


def _parse_border_fill(data: bytes) -> BorderFill:
    """Parse border fill record."""
    border_fill = BorderFill()
    offset = 0

    border_fill.properties, offset = read_uint16(data, offset)

    # Parse border lines
    border = Border()
    for line_name in ['left', 'right', 'top', 'bottom']:
        line = BorderLine()
        border_type_value, offset = read_uint8(data, offset)
        thickness_value, offset = read_uint8(data, offset)
        color_value, offset = read_uint32(data, offset)

        try:
            line.border_type = BorderType(border_type_value)
        except ValueError:
            line.border_type = BorderType.NONE

        try:
            line.thickness = BorderThickness(thickness_value)
        except ValueError:
            line.thickness = BorderThickness.MM0_1

        line.color = Color4Byte(color_value)
        setattr(border, line_name, line)

    # Diagonal
    diag_type, offset = read_uint8(data, offset)
    try:
        border.diagonal_type = DiagonalType(diag_type)
    except ValueError:
        border.diagonal_type = DiagonalType.NONE

    if border.diagonal_type != DiagonalType.NONE:
        diag_line = BorderLine()
        diag_type_value, offset = read_uint8(data, offset)
        diag_thickness, offset = read_uint8(data, offset)
        diag_color, offset = read_uint32(data, offset)
        try:
            diag_line.border_type = BorderType(diag_type_value)
        except ValueError:
            diag_line.border_type = BorderType.SOLID
        try:
            diag_line.thickness = BorderThickness(diag_thickness)
        except ValueError:
            diag_line.thickness = BorderThickness.MM0_1
        diag_line.color = Color4Byte(diag_color)
        border.diagonal = diag_line

    border_fill.border = border

    # Fill info
    if len(data) > offset:
        fill_info = FillInfo()
        fill_info.fill_type, offset = read_uint32(data, offset)

        if fill_info.has_pattern_fill():
            pattern = PatternFill()
            back_color, offset = read_uint32(data, offset)
            pattern_color, offset = read_uint32(data, offset)
            pattern_type, offset = read_int32(data, offset)
            pattern.back_color = Color4Byte(back_color)
            pattern.pattern_color = Color4Byte(pattern_color)
            try:
                pattern.pattern_type = PatternType(pattern_type)
            except ValueError:
                pattern.pattern_type = PatternType.NONE
            fill_info.pattern_fill = pattern

        if fill_info.has_gradient_fill():
            gradient = GradientFill()
            gradient_type, offset = read_uint8(data, offset)
            try:
                gradient.gradient_type = GradientType(gradient_type)
            except ValueError:
                gradient.gradient_type = GradientType.LINEAR
            gradient.angle, offset = read_int32(data, offset)
            gradient.center_x, offset = read_int32(data, offset)
            gradient.center_y, offset = read_int32(data, offset)
            gradient.step, offset = read_uint32(data, offset)
            gradient.step_center, offset = read_uint32(data, offset)
            color_count, offset = read_uint32(data, offset)

            for _ in range(color_count):
                color_value, offset = read_uint32(data, offset)
                position, offset = read_int32(data, offset)
                gradient.colors.append(GradientColor(
                    color=Color4Byte(color_value),
                    position=position,
                ))

            if len(data) > offset + 3:
                gradient.blur, offset = read_uint8(data, offset)
                gradient.blur_center, offset = read_uint32(data, offset)

            fill_info.gradient_fill = gradient

        if fill_info.has_image_fill():
            image = ImageFill()
            fill_type_value, offset = read_uint8(data, offset)
            try:
                image.fill_type = ImageFillType(fill_type_value)
            except ValueError:
                image.fill_type = ImageFillType.TILE_ALL

            if len(data) > offset + 7:
                image.bright, offset = read_int16(data, offset)
                image.contrast, offset = read_int16(data, offset)
                image.effect, offset = read_uint8(data, offset)
                image.bin_item_id, offset = read_uint16(data, offset)

            fill_info.image_fill = image

        border_fill.fill_info = fill_info

    return border_fill


def _parse_char_shape(data: bytes) -> CharShape:
    """Parse character shape record."""
    char_shape = CharShape()
    offset = 0

    # Font references (7 script types)
    font_refs = CharShapeFontRef()
    font_refs.korean, offset = read_uint16(data, offset)
    font_refs.english, offset = read_uint16(data, offset)
    font_refs.chinese, offset = read_uint16(data, offset)
    font_refs.japanese, offset = read_uint16(data, offset)
    font_refs.other, offset = read_uint16(data, offset)
    font_refs.symbol, offset = read_uint16(data, offset)
    font_refs.user, offset = read_uint16(data, offset)
    char_shape.font_refs = font_refs

    # Ratios
    ratios = CharShapeRatio()
    ratios.korean, offset = read_uint8(data, offset)
    ratios.english, offset = read_uint8(data, offset)
    ratios.chinese, offset = read_uint8(data, offset)
    ratios.japanese, offset = read_uint8(data, offset)
    ratios.other, offset = read_uint8(data, offset)
    ratios.symbol, offset = read_uint8(data, offset)
    ratios.user, offset = read_uint8(data, offset)
    char_shape.ratios = ratios

    # Spacings
    spacings = CharShapeSpacing()
    spacings.korean, offset = read_int16(data, offset)
    spacings.english, offset = read_int16(data, offset)
    spacings.chinese, offset = read_int16(data, offset)
    spacings.japanese, offset = read_int16(data, offset)
    spacings.other, offset = read_int16(data, offset)
    spacings.symbol, offset = read_int16(data, offset)
    spacings.user, offset = read_int16(data, offset)
    char_shape.spacings = spacings

    # Relative sizes
    rel_sizes = CharShapeRelSize()
    rel_sizes.korean, offset = read_uint8(data, offset)
    rel_sizes.english, offset = read_uint8(data, offset)
    rel_sizes.chinese, offset = read_uint8(data, offset)
    rel_sizes.japanese, offset = read_uint8(data, offset)
    rel_sizes.other, offset = read_uint8(data, offset)
    rel_sizes.symbol, offset = read_uint8(data, offset)
    rel_sizes.user, offset = read_uint8(data, offset)
    char_shape.rel_sizes = rel_sizes

    # Offsets
    offsets = CharShapeOffset()
    offsets.korean, offset = read_int16(data, offset)
    offsets.english, offset = read_int16(data, offset)
    offsets.chinese, offset = read_int16(data, offset)
    offsets.japanese, offset = read_int16(data, offset)
    offsets.other, offset = read_int16(data, offset)
    offsets.symbol, offset = read_int16(data, offset)
    offsets.user, offset = read_int16(data, offset)
    char_shape.offsets = offsets

    # Base size and properties
    char_shape.base_size, offset = read_int32(data, offset)
    char_shape.properties, offset = read_uint32(data, offset)

    # Shadow offsets
    char_shape.shadow_offset_x, offset = read_int16(data, offset)
    char_shape.shadow_offset_y, offset = read_int16(data, offset)

    # Colors
    text_color, offset = read_uint32(data, offset)
    underline_color, offset = read_uint32(data, offset)
    shade_color, offset = read_uint32(data, offset)
    shadow_color, offset = read_uint32(data, offset)

    char_shape.text_color = Color4Byte(text_color)
    char_shape.underline_color = Color4Byte(underline_color)
    char_shape.shade_color = Color4Byte(shade_color)
    char_shape.shadow_color = Color4Byte(shadow_color)

    # Border fill ID (optional)
    if len(data) > offset + 1:
        char_shape.border_fill_id, offset = read_uint16(data, offset)

    # Strikeout color (optional, version 5.0.2.1+)
    if len(data) > offset + 3:
        strikeout_color, offset = read_uint32(data, offset)
        char_shape.strikeout_color = Color4Byte(strikeout_color)

    return char_shape


def _parse_para_shape(data: bytes) -> ParaShape:
    """Parse paragraph shape record."""
    para_shape = ParaShape()
    offset = 0

    para_shape.properties1, offset = read_uint32(data, offset)
    para_shape.margin.left, offset = read_int32(data, offset)
    para_shape.margin.right, offset = read_int32(data, offset)
    para_shape.margin.indent, offset = read_int32(data, offset)
    para_shape.margin.prev, offset = read_int32(data, offset)
    para_shape.margin.next, offset = read_int32(data, offset)

    line_spacing, offset = read_int32(data, offset)
    para_shape.line_spacing = line_spacing

    para_shape.tab_def_id, offset = read_uint16(data, offset)
    para_shape.numbering_id, offset = read_uint16(data, offset)
    para_shape.border_fill_id, offset = read_uint16(data, offset)

    para_shape.border_offset_left, offset = read_int16(data, offset)
    para_shape.border_offset_right, offset = read_int16(data, offset)
    para_shape.border_offset_top, offset = read_int16(data, offset)
    para_shape.border_offset_bottom, offset = read_int16(data, offset)

    if len(data) > offset + 3:
        para_shape.properties2, offset = read_uint32(data, offset)

    if len(data) > offset + 3:
        para_shape.properties3, offset = read_uint32(data, offset)

    if len(data) > offset + 3:
        para_shape.line_wrap, offset = read_uint32(data, offset)

    if len(data) > offset + 3:
        para_shape.auto_spacing_para_english, offset = read_uint32(data, offset)
        para_shape.auto_spacing_para_number, offset = read_uint32(data, offset)

    # Determine line spacing type from properties1
    spacing_type = (para_shape.properties1 >> 28) & 0x03
    try:
        para_shape.line_spacing_type = LineSpacingType(spacing_type)
    except ValueError:
        para_shape.line_spacing_type = LineSpacingType.PERCENT

    return para_shape


def _parse_tab_def(data: bytes) -> TabDef:
    """Parse tab definition record."""
    tab_def = TabDef()
    offset = 0

    tab_def.properties, offset = read_uint32(data, offset)
    count, offset = read_uint32(data, offset)

    for _ in range(count):
        item = TabItem()
        item.position, offset = read_uint32(data, offset)
        tab_type, offset = read_uint8(data, offset)
        leader, offset = read_uint8(data, offset)
        offset += 2  # Reserved

        try:
            item.tab_type = TabType(tab_type)
        except ValueError:
            item.tab_type = TabType.LEFT

        try:
            item.leader = TabLeader(leader)
        except ValueError:
            item.leader = TabLeader.NONE

        tab_def.items.append(item)

    return tab_def


def _parse_numbering(data: bytes) -> Numbering:
    """Parse numbering record."""
    numbering = Numbering()
    offset = 0

    # Parse up to 7 levels
    for i in range(7):
        if len(data) <= offset:
            break

        level = NumberingLevel()
        para_shape_id, offset = read_uint32(data, offset)
        level.para_shape_id = para_shape_id

        if len(data) > offset:
            format_string, offset = read_wchar_string_with_length(data, offset)
            level.format_string = format_string

        numbering.levels.append(level)

    # Start numbers (7 values)
    for i in range(7):
        if len(data) > offset + 3:
            start_num, offset = read_uint32(data, offset)
            numbering.start_numbers[i] = start_num

    return numbering


def _parse_bullet(data: bytes) -> Bullet:
    """Parse bullet record."""
    bullet = Bullet()
    offset = 0

    if len(data) > offset + 1:
        bullet_char, offset = read_wchar_string(data, offset, 1)
        bullet.bullet_char = bullet_char

    if len(data) > offset + 3:
        bullet.char_shape_id, offset = read_uint32(data, offset)

    return bullet


def _parse_style(data: bytes) -> Style:
    """Parse style record."""
    style = Style()
    offset = 0

    style.name, offset = read_wchar_string_with_length(data, offset)
    style.english_name, offset = read_wchar_string_with_length(data, offset)
    style.properties, offset = read_uint8(data, offset)
    style.next_style_id, offset = read_uint8(data, offset)
    style.lang_id, offset = read_int16(data, offset)
    style.para_shape_id, offset = read_uint16(data, offset)
    style.char_shape_id, offset = read_uint16(data, offset)

    return style


def _parse_memo_shape(data: bytes) -> MemoShape:
    """Parse memo shape record."""
    memo = MemoShape()
    offset = 0

    memo.memo_id, offset = read_uint32(data, offset)
    if len(data) > offset + 3:
        memo.width, offset = read_uint32(data, offset)
    if len(data) > offset + 3:
        memo.properties, offset = read_uint32(data, offset)

    return memo


def _parse_track_change(data: bytes) -> TrackChange:
    """Parse track change record."""
    change = TrackChange()
    # Minimal implementation - just store raw data for now
    return change


def _parse_track_change_author(data: bytes) -> TrackChangeAuthor:
    """Parse track change author record."""
    author = TrackChangeAuthor()
    offset = 0

    if len(data) > 0:
        author.author_name, offset = read_wchar_string_with_length(data, offset)

    return author


def _parse_compatible_document(data: bytes) -> CompatibleDocument:
    """Parse compatible document record."""
    compat = CompatibleDocument()
    offset = 0

    if len(data) > 3:
        compat.target_program, offset = read_uint32(data, offset)

    return compat


def _parse_layout_compatibility(data: bytes) -> LayoutCompatibility:
    """Parse layout compatibility record."""
    layout = LayoutCompatibility()
    offset = 0

    if len(data) > 3:
        layout.char_width, offset = read_uint32(data, offset)
    if len(data) > offset + 3:
        layout.char_spacing, offset = read_uint32(data, offset)
    if len(data) > offset + 3:
        layout.word_spacing, offset = read_uint32(data, offset)
    if len(data) > offset + 3:
        layout.line_height, offset = read_uint32(data, offset)

    return layout
