"""BodyText section parser for HWP format."""

from typing import List, Optional

from hwp2hwpx.hwp.enums.tags import HWPTag
from hwp2hwpx.hwp.models.body_text import BodyText, Section
from hwp2hwpx.hwp.models.control import (
    Control,
    CtrlHeaderCommon,
    SectionDefine,
    ColumnDefine,
    PageDef,
    GsoControl,
    HeaderControl,
    FooterControl,
    FootnoteControl,
    EndnoteControl,
    AutoNumberControl,
    NewNumberControl,
    BookmarkControl,
    EquationControl,
)
from hwp2hwpx.hwp.models.table import TableControl
from hwp2hwpx.hwp.models.paragraph import (
    Paragraph,
    ParaHeader,
    ParaText,
    ParaCharShape,
    ParaLineSeg,
    ParaRangeTag,
)
from hwp2hwpx.hwp.models.table import Cell, Row
from hwp2hwpx.hwp.parser.record import Record, parse_records_as_objects
from hwp2hwpx.util.binary import (
    read_int16,
    read_int32,
    read_uint8,
    read_uint16,
    read_uint32,
    read_wchar_string,
)
from hwp2hwpx.util.color import Color4Byte


def parse_body_text(section_data_list: List[bytes]) -> BodyText:
    """Parse BodyText from list of section data.

    Args:
        section_data_list: List of decompressed section stream data

    Returns:
        Parsed BodyText object
    """
    body_text = BodyText()

    for section_data in section_data_list:
        section = parse_section(section_data)
        body_text.sections.append(section)

    return body_text


def parse_section(data: bytes) -> Section:
    """Parse a single section from decompressed data.

    Args:
        data: Decompressed section stream data

    Returns:
        Parsed Section object
    """
    section = Section()
    records = parse_records_as_objects(data)

    i = 0
    while i < len(records):
        record = records[i]

        if record.tag_id == HWPTag.PARA_HEADER:
            # Parse paragraph and its children
            para, consumed = _parse_paragraph(records, i)
            section.paragraphs.append(para)

            # Check first paragraph for section define
            if len(section.paragraphs) == 1 and para.controls:
                for ctrl in para.controls:
                    if isinstance(ctrl, SectionDefine):
                        section.section_define = ctrl
                        break

            i += consumed
        else:
            i += 1

    return section


def _parse_paragraph(records: List[Record], start_index: int) -> tuple[Paragraph, int]:
    """Parse a paragraph starting from given record index.

    Args:
        records: List of all records
        start_index: Starting index in records

    Returns:
        Tuple of (Paragraph, number of records consumed)
    """
    para = Paragraph()
    consumed = 1  # At least the PARA_HEADER record

    # Parse header
    header_record = records[start_index]
    para.header = _parse_para_header(header_record.data)

    # Parse child records
    i = start_index + 1
    control_index = 0

    while i < len(records):
        record = records[i]

        # Check if this record belongs to this paragraph (same or higher level)
        if record.level <= header_record.level and record.tag_id == HWPTag.PARA_HEADER:
            break

        consumed += 1

        if record.tag_id == HWPTag.PARA_TEXT:
            para.text = _parse_para_text(record.data, para.header.text_length)

        elif record.tag_id == HWPTag.PARA_CHAR_SHAPE:
            para.char_shapes = _parse_para_char_shapes(record.data)

        elif record.tag_id == HWPTag.PARA_LINE_SEG:
            para.line_segs = _parse_para_line_segs(record.data)

        elif record.tag_id == HWPTag.PARA_RANGE_TAG:
            para.range_tags = _parse_para_range_tags(record.data)

        elif record.tag_id == HWPTag.CTRL_HEADER:
            # Parse control and its children
            ctrl, ctrl_consumed = _parse_control(records, i)
            if ctrl:
                para.controls.append(ctrl)
            consumed += ctrl_consumed - 1  # -1 because we already counted this record
            i += ctrl_consumed - 1

        i += 1

    return para, consumed


def _parse_para_header(data: bytes) -> ParaHeader:
    """Parse paragraph header."""
    header = ParaHeader()
    offset = 0

    # Text length (in characters, not bytes)
    if len(data) >= 4:
        header.text_length, offset = read_uint32(data, offset)

    if len(data) >= offset + 4:
        header.control_mask, offset = read_uint32(data, offset)

    if len(data) >= offset + 2:
        header.para_shape_id, offset = read_uint16(data, offset)

    if len(data) >= offset + 1:
        header.style_id, offset = read_uint8(data, offset)

    if len(data) >= offset + 1:
        header.column_type, offset = read_uint8(data, offset)

    if len(data) >= offset + 2:
        header.char_shape_count, offset = read_uint16(data, offset)

    if len(data) >= offset + 2:
        header.range_tag_count, offset = read_uint16(data, offset)

    if len(data) >= offset + 2:
        header.line_align_count, offset = read_uint16(data, offset)

    if len(data) >= offset + 4:
        header.instance_id, offset = read_uint32(data, offset)

    return header


def _parse_para_text(data: bytes, char_count: int) -> ParaText:
    """Parse paragraph text."""
    para_text = ParaText()

    # Text is stored as UTF-16LE
    byte_length = min(len(data), char_count * 2)
    if byte_length > 0:
        try:
            para_text.text = data[:byte_length].decode('utf-16-le')
        except UnicodeDecodeError:
            para_text.text = ""

    return para_text


def _parse_para_char_shapes(data: bytes) -> List[ParaCharShape]:
    """Parse paragraph character shapes."""
    char_shapes = []
    offset = 0

    while offset < len(data) - 7:
        cs = ParaCharShape()
        cs.position, offset = read_uint32(data, offset)
        cs.char_shape_id, offset = read_uint32(data, offset)
        char_shapes.append(cs)

    return char_shapes


def _parse_para_line_segs(data: bytes) -> List[ParaLineSeg]:
    """Parse paragraph line segments."""
    line_segs = []
    offset = 0

    while offset < len(data) - 35:
        seg = ParaLineSeg()
        seg.text_start, offset = read_uint32(data, offset)
        seg.line_vertical_position, offset = read_int32(data, offset)
        seg.line_height, offset = read_int32(data, offset)
        seg.text_part_height, offset = read_int32(data, offset)
        seg.distance_from_baseline, offset = read_int32(data, offset)
        seg.line_spacing, offset = read_int32(data, offset)
        seg.column_index, offset = read_int16(data, offset)
        seg.segment_width, offset = read_int16(data, offset)
        seg.tag, offset = read_uint32(data, offset)
        line_segs.append(seg)

    return line_segs


def _parse_para_range_tags(data: bytes) -> List[ParaRangeTag]:
    """Parse paragraph range tags."""
    range_tags = []
    offset = 0

    while offset < len(data) - 11:
        tag = ParaRangeTag()
        tag.start, offset = read_uint32(data, offset)
        tag.end, offset = read_uint32(data, offset)
        tag.tag, offset = read_uint32(data, offset)
        range_tags.append(tag)

    return range_tags


def _parse_control(records: List[Record], start_index: int) -> tuple[Optional[Control], int]:
    """Parse a control and its child records.

    Args:
        records: List of all records
        start_index: Starting index in records

    Returns:
        Tuple of (Control or None, number of records consumed)
    """
    ctrl_record = records[start_index]
    consumed = 1

    # Parse control header to get control ID
    if len(ctrl_record.data) < 4:
        return None, consumed

    ctrl_id = ctrl_record.data[0:4].decode('ascii', errors='replace')

    # Create appropriate control type
    ctrl: Control

    if ctrl_id == 'secd':
        ctrl = _parse_section_define(ctrl_record, records, start_index)
    elif ctrl_id == 'cold':
        ctrl = _parse_column_define(ctrl_record.data)
    elif ctrl_id == 'tbl ':
        ctrl, extra = _parse_table_control(records, start_index)
        consumed += extra
    elif ctrl_id == 'gso ':
        ctrl = _parse_gso_control(ctrl_record.data)
    elif ctrl_id == 'head':
        ctrl, extra = _parse_header_control(records, start_index)
        consumed += extra
    elif ctrl_id == 'foot':
        ctrl, extra = _parse_footer_control(records, start_index)
        consumed += extra
    elif ctrl_id in ('fn  ', 'en  '):
        ctrl, extra = _parse_footnote_control(records, start_index, ctrl_id)
        consumed += extra
    elif ctrl_id == 'atno':
        ctrl = _parse_auto_number(ctrl_record.data)
    elif ctrl_id == 'nwno':
        ctrl = _parse_new_number(ctrl_record.data)
    elif ctrl_id == 'bokm':
        ctrl = _parse_bookmark(ctrl_record.data)
    elif ctrl_id == 'eqed':
        ctrl = _parse_equation(ctrl_record.data)
    else:
        # Generic control
        ctrl = Control(ctrl_id=ctrl_id)

    return ctrl, consumed


def _parse_section_define(record: Record, records: List[Record], start_index: int) -> SectionDefine:
    """Parse section define control."""
    section_def = SectionDefine()
    section_def.ctrl_id = 'secd'

    data = record.data
    offset = 4  # Skip control ID

    if len(data) >= offset + 4:
        section_def.properties, offset = read_uint32(data, offset)

    if len(data) >= offset + 2:
        section_def.column_gap, offset = read_uint16(data, offset)

    if len(data) >= offset + 2:
        section_def.vert_rel, offset = read_uint16(data, offset)

    if len(data) >= offset + 2:
        section_def.horz_rel, offset = read_uint16(data, offset)

    if len(data) >= offset + 4:
        section_def.default_tab, offset = read_uint32(data, offset)

    if len(data) >= offset + 2:
        section_def.numbering_shape, offset = read_uint16(data, offset)

    if len(data) >= offset + 2:
        section_def.page_starting_number, offset = read_uint16(data, offset)

    if len(data) >= offset + 2:
        section_def.picture_starting_number, offset = read_uint16(data, offset)

    if len(data) >= offset + 2:
        section_def.table_starting_number, offset = read_uint16(data, offset)

    if len(data) >= offset + 2:
        section_def.equation_starting_number, offset = read_uint16(data, offset)

    # Parse child records for page def, footnote shape, etc.
    for child in record.children:
        if child.tag_id == HWPTag.PAGE_DEF:
            section_def.page_def = _parse_page_def(child.data)
        elif child.tag_id == HWPTag.FOOTNOTE_SHAPE:
            pass  # Parse footnote shape
        elif child.tag_id == HWPTag.PAGE_BORDER_FILL:
            pass  # Parse page border fill

    return section_def


def _parse_page_def(data: bytes) -> PageDef:
    """Parse page definition."""
    page_def = PageDef()
    offset = 0

    if len(data) >= 4:
        page_def.paper_width, offset = read_uint32(data, offset)
    if len(data) >= offset + 4:
        page_def.paper_height, offset = read_uint32(data, offset)
    if len(data) >= offset + 4:
        page_def.margin_left, offset = read_uint32(data, offset)
    if len(data) >= offset + 4:
        page_def.margin_right, offset = read_uint32(data, offset)
    if len(data) >= offset + 4:
        page_def.margin_top, offset = read_uint32(data, offset)
    if len(data) >= offset + 4:
        page_def.margin_bottom, offset = read_uint32(data, offset)
    if len(data) >= offset + 4:
        page_def.header_margin, offset = read_uint32(data, offset)
    if len(data) >= offset + 4:
        page_def.footer_margin, offset = read_uint32(data, offset)
    if len(data) >= offset + 4:
        page_def.gutter_margin, offset = read_uint32(data, offset)
    if len(data) >= offset + 4:
        page_def.properties, offset = read_uint32(data, offset)

    return page_def


def _parse_column_define(data: bytes) -> ColumnDefine:
    """Parse column define control."""
    col_def = ColumnDefine()
    col_def.ctrl_id = 'cold'

    offset = 4  # Skip control ID
    if len(data) >= offset + 2:
        col_def.properties, offset = read_uint16(data, offset)

    if len(data) >= offset + 2:
        col_def.gap, offset = read_uint16(data, offset)

    return col_def


def _parse_table_control(records: List[Record], start_index: int) -> tuple[TableControl, int]:
    """Parse table control and its cells."""
    table = TableControl()
    table.ctrl_id = 'tbl '

    record = records[start_index]
    data = record.data
    offset = 4  # Skip control ID

    # Parse common header
    table.header = _parse_ctrl_header_common(data, offset)

    # For simplicity, return basic table structure
    # Full implementation would parse LIST_HEADER and cell data

    return table, 0


def _parse_ctrl_header_common(data: bytes, offset: int) -> CtrlHeaderCommon:
    """Parse common control header."""
    header = CtrlHeaderCommon()

    if len(data) >= offset + 4:
        header.properties, offset = read_uint32(data, offset)

    if len(data) >= offset + 4:
        header.vertical_offset, offset = read_uint32(data, offset)

    if len(data) >= offset + 4:
        header.horizontal_offset, offset = read_uint32(data, offset)

    if len(data) >= offset + 4:
        header.width, offset = read_uint32(data, offset)

    if len(data) >= offset + 4:
        header.height, offset = read_uint32(data, offset)

    if len(data) >= offset + 4:
        header.z_order, offset = read_int32(data, offset)

    if len(data) >= offset + 2:
        header.margin_left, offset = read_int16(data, offset)

    if len(data) >= offset + 2:
        header.margin_right, offset = read_int16(data, offset)

    if len(data) >= offset + 2:
        header.margin_top, offset = read_int16(data, offset)

    if len(data) >= offset + 2:
        header.margin_bottom, offset = read_int16(data, offset)

    if len(data) >= offset + 4:
        header.instance_id, offset = read_uint32(data, offset)

    return header


def _parse_gso_control(data: bytes) -> GsoControl:
    """Parse graphic shape object control."""
    gso = GsoControl()
    gso.ctrl_id = 'gso '
    # Minimal implementation - full GSO parsing is complex
    return gso


def _parse_header_control(records: List[Record], start_index: int) -> tuple[HeaderControl, int]:
    """Parse header control."""
    header = HeaderControl()
    header.ctrl_id = 'head'
    return header, 0


def _parse_footer_control(records: List[Record], start_index: int) -> tuple[FooterControl, int]:
    """Parse footer control."""
    footer = FooterControl()
    footer.ctrl_id = 'foot'
    return footer, 0


def _parse_footnote_control(records: List[Record], start_index: int, ctrl_id: str) -> tuple[Control, int]:
    """Parse footnote/endnote control."""
    if ctrl_id == 'fn  ':
        ctrl = FootnoteControl()
        ctrl.ctrl_id = 'fn  '
    else:
        ctrl = EndnoteControl()
        ctrl.ctrl_id = 'en  '
    return ctrl, 0


def _parse_auto_number(data: bytes) -> AutoNumberControl:
    """Parse auto number control."""
    auto_num = AutoNumberControl()
    auto_num.ctrl_id = 'atno'
    return auto_num


def _parse_new_number(data: bytes) -> NewNumberControl:
    """Parse new number control."""
    new_num = NewNumberControl()
    new_num.ctrl_id = 'nwno'
    return new_num


def _parse_bookmark(data: bytes) -> BookmarkControl:
    """Parse bookmark control."""
    bookmark = BookmarkControl()
    bookmark.ctrl_id = 'bokm'
    return bookmark


def _parse_equation(data: bytes) -> EquationControl:
    """Parse equation control."""
    equation = EquationControl()
    equation.ctrl_id = 'eqed'
    return equation
