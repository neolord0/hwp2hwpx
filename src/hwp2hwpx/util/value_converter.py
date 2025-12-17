"""Value converter utilities for HWP to HWPX conversion."""

from hwp2hwpx.hwp.enums.border_fill import BorderThickness, BorderType, BorderType2
from hwp2hwpx.hwp.enums.control import (
    ArcType as HWPArcType,
    HeaderFooterApplyPage,
    LineArrowShape,
    LineArrowSize,
    LineEndShape,
    LineType,
    NumberSort,
    TextVerticalAlignment,
)
from hwp2hwpx.hwp.enums.para_shape import (
    Alignment,
    NumberShape,
    ParagraphNumberFormat,
    ValueType,
    VerticalAlignment,
)
from hwp2hwpx.hwpx.enums.align import HorizontalAlign2, VerticalAlign1, VerticalAlign2
from hwp2hwpx.hwpx.enums.line_type import (
    ArrowSize,
    ArrowType,
    LineCap,
    LineType2,
    LineType3,
    LineWidth,
)
from hwp2hwpx.hwpx.enums.number_type import (
    ApplyPageType,
    ArcType,
    NumberType1,
    NumberType2,
    NumType,
    ValueUnit1,
)
from hwp2hwpx.util.color import Color4Byte, NONE_COLOR_VALUE


def bool_from_short(value: int) -> bool:
    """Convert short value to boolean."""
    return value != 0


def bool_from_string(value: str) -> bool:
    """Convert string value to boolean."""
    return value != "0"


def line_type2_from_border_type(hwp_type: BorderType) -> LineType2:
    """Convert HWP BorderType to HWPX LineType2."""
    mapping = {
        BorderType.NONE: LineType2.NONE,
        BorderType.SOLID: LineType2.SOLID,
        BorderType.DASH: LineType2.DASH,
        BorderType.DOT: LineType2.DOT,
        BorderType.DASH_DOT: LineType2.DASH_DOT,
        BorderType.DASH_DOT_DOT: LineType2.DASH_DOT_DOT,
        BorderType.LONG_DASH: LineType2.LONG_DASH,
        BorderType.CIRCLE_DOT: LineType2.CIRCLE,
        BorderType.DOUBLE: LineType2.DOUBLE_SLIM,
        BorderType.THIN_THICK: LineType2.SLIM_THICK,
        BorderType.THICK_THIN: LineType2.THICK_SLIM,
        BorderType.THIN_THICK_THIN: LineType2.SLIM_THICK_SLIM,
    }
    return mapping.get(hwp_type, LineType2.NONE)


def line_width_from_border_thickness(hwp_thickness: BorderThickness) -> LineWidth:
    """Convert HWP BorderThickness to HWPX LineWidth."""
    mapping = {
        BorderThickness.MM0_1: LineWidth.MM_0_1,
        BorderThickness.MM0_12: LineWidth.MM_0_12,
        BorderThickness.MM0_15: LineWidth.MM_0_15,
        BorderThickness.MM0_2: LineWidth.MM_0_2,
        BorderThickness.MM0_25: LineWidth.MM_0_25,
        BorderThickness.MM0_3: LineWidth.MM_0_3,
        BorderThickness.MM0_4: LineWidth.MM_0_4,
        BorderThickness.MM0_5: LineWidth.MM_0_5,
        BorderThickness.MM0_6: LineWidth.MM_0_6,
        BorderThickness.MM0_7: LineWidth.MM_0_7,
        BorderThickness.MM1_0: LineWidth.MM_1_0,
        BorderThickness.MM1_5: LineWidth.MM_1_5,
        BorderThickness.MM2_0: LineWidth.MM_2_0,
        BorderThickness.MM3_0: LineWidth.MM_3_0,
        BorderThickness.MM4_0: LineWidth.MM_4_0,
        BorderThickness.MM5_0: LineWidth.MM_5_0,
    }
    return mapping.get(hwp_thickness, LineWidth.MM_0_1)


def color_to_string(hwp_color: Color4Byte) -> str:
    """Convert HWP Color4Byte to hex string or 'none'."""
    if hwp_color.value == NONE_COLOR_VALUE:
        return "none"
    return hwp_color.to_hex()


def color_value_to_string(value: int) -> str:
    """Convert 32-bit color value to hex string."""
    if value == NONE_COLOR_VALUE:
        return "none"
    r = value & 0xFF
    g = (value >> 8) & 0xFF
    b = (value >> 16) & 0xFF
    return f"#{r:02X}{g:02X}{b:02X}"


def color_from_numeric_string(color_str: str) -> str:
    """Convert numeric color string to hex format."""
    long_color = int(color_str)
    red = (long_color >> 16) & 0xFF
    green = (long_color >> 8) & 0xFF
    blue = long_color & 0xFF
    # HWP stores as RGB, swap to get proper order
    return f"#{blue:02X}{green:02X}{red:02X}"


def color_with_none_check(hwp_color: Color4Byte, none_value: int) -> str:
    """Convert color with custom none value check."""
    if hwp_color.value == none_value:
        return "none"
    return hwp_color.to_hex()


def line_type3_from_border_type2(hwp_type: BorderType2) -> LineType3:
    """Convert HWP BorderType2 to HWPX LineType3."""
    mapping = {
        BorderType2.SOLID: LineType3.SOLID,
        BorderType2.DASH: LineType3.DASH,
        BorderType2.DOT: LineType3.DOT,
        BorderType2.DASH_DOT: LineType3.DASH_DOT,
        BorderType2.DASH_DOT_DOT: LineType3.DASH_DOT_DOT,
        BorderType2.LONG_DASH: LineType3.LONG_DASH,
        BorderType2.CIRCLE_DOT: LineType3.CIRCLE,
        BorderType2.DOUBLE: LineType3.DOUBLE_SLIM,
        BorderType2.THIN_THICK: LineType3.SLIM_THICK,
        BorderType2.THICK_THIN: LineType3.THICK_SLIM,
        BorderType2.THIN_THICK_THIN: LineType3.SLIM_THICK_SLIM,
        BorderType2.WAVE: LineType3.WAVE,
        BorderType2.DOUBLE_WAVE: LineType3.DOUBLEWAVE,
    }
    return mapping.get(hwp_type, LineType3.SOLID)


def line_type2_from_border_type2(hwp_type: BorderType2) -> LineType2:
    """Convert HWP BorderType2 to HWPX LineType2."""
    mapping = {
        BorderType2.SOLID: LineType2.SOLID,
        BorderType2.DASH: LineType2.DASH,
        BorderType2.DOT: LineType2.DOT,
        BorderType2.DASH_DOT: LineType2.DASH_DOT,
        BorderType2.DASH_DOT_DOT: LineType2.DASH_DOT_DOT,
        BorderType2.LONG_DASH: LineType2.LONG_DASH,
        BorderType2.CIRCLE_DOT: LineType2.CIRCLE,
        BorderType2.DOUBLE: LineType2.DOUBLE_SLIM,
        BorderType2.THIN_THICK: LineType2.SLIM_THICK,
        BorderType2.THICK_THIN: LineType2.THICK_SLIM,
        BorderType2.THIN_THICK_THIN: LineType2.SLIM_THICK_SLIM,
    }
    return mapping.get(hwp_type, LineType2.NONE)


def value_unit1_from_value_type(hwp_type: ValueType) -> ValueUnit1:
    """Convert HWP ValueType to HWPX ValueUnit1."""
    if hwp_type == ValueType.RATIO_FOR_LETTER:
        return ValueUnit1.PERCENT
    return ValueUnit1.HWPUNIT


def horizontal_align2_from_alignment(hwp_align: Alignment) -> HorizontalAlign2:
    """Convert HWP Alignment to HWPX HorizontalAlign2."""
    mapping = {
        Alignment.JUSTIFY: HorizontalAlign2.JUSTIFY,
        Alignment.LEFT: HorizontalAlign2.LEFT,
        Alignment.RIGHT: HorizontalAlign2.RIGHT,
        Alignment.CENTER: HorizontalAlign2.CENTER,
        Alignment.DISTRIBUTE: HorizontalAlign2.DISTRIBUTE,
        Alignment.DIVIDE: HorizontalAlign2.DISTRIBUTE_SPACE,
    }
    return mapping.get(hwp_align, HorizontalAlign2.JUSTIFY)


def vertical_align1_from_vertical_alignment(hwp_vert_align: VerticalAlignment) -> VerticalAlign1:
    """Convert HWP VerticalAlignment to HWPX VerticalAlign1."""
    mapping = {
        VerticalAlignment.BY_FONT: VerticalAlign1.BASELINE,
        VerticalAlignment.TOP: VerticalAlign1.TOP,
        VerticalAlignment.CENTER: VerticalAlign1.CENTER,
        VerticalAlignment.BOTTOM: VerticalAlign1.BOTTOM,
    }
    return mapping.get(hwp_vert_align, VerticalAlign1.BASELINE)


def ref_id_string(hwp_ref_id: int) -> str:
    """Convert HWP reference ID to string."""
    if hwp_ref_id == -1:
        return "4294967295"
    return str(hwp_ref_id)


def number_type1_from_para_number_format(hwp_format: ParagraphNumberFormat) -> NumberType1:
    """Convert HWP ParagraphNumberFormat to HWPX NumberType1."""
    mapping = {
        ParagraphNumberFormat.NUMBER: NumberType1.DIGIT,
        ParagraphNumberFormat.CIRCLED_NUMBER: NumberType1.CIRCLED_DIGIT,
        ParagraphNumberFormat.UPPERCASE_ROMAN_NUMBER: NumberType1.ROMAN_CAPITAL,
        ParagraphNumberFormat.LOWERCASE_ROMAN_NUMBER: NumberType1.ROMAN_SMALL,
        ParagraphNumberFormat.UPPERCASE_ALPHABET: NumberType1.LATIN_CAPITAL,
        ParagraphNumberFormat.LOWERCASE_ALPHABET: NumberType1.LATIN_SMALL,
        ParagraphNumberFormat.CIRCLED_UPPERCASE_ALPHABET: NumberType1.CIRCLED_LATIN_CAPTION,
        ParagraphNumberFormat.CIRCLED_LOWERCASE_ALPHABET: NumberType1.CIRCLED_LATIN_SMALL,
        ParagraphNumberFormat.HANGUL: NumberType1.HANGUL_SYLLABLE,
        ParagraphNumberFormat.CIRCLED_HANGUL: NumberType1.CIRCLED_HANGUL_SYLLABLE,
        ParagraphNumberFormat.HANGUL_JAMO: NumberType1.HANGUL_JAMO,
        ParagraphNumberFormat.CIRCLED_HANGUL_JAMO: NumberType1.CIRCLED_HANGUL_JAMO,
        ParagraphNumberFormat.HANGUL_NUMBER: NumberType1.HANGUL_PHONETIC,
        ParagraphNumberFormat.HANJA_NUMBER: NumberType1.IDEOGRAPH,
        ParagraphNumberFormat.CIRCLED_HANJA_NUMBER: NumberType1.CIRCLED_IDEOGRAPH,
    }
    return mapping.get(hwp_format, NumberType1.DIGIT)


def number_type2_from_number_shape(hwp_shape: NumberShape) -> NumberType2:
    """Convert HWP NumberShape to HWPX NumberType2."""
    mapping = {
        NumberShape.NUMBER: NumberType2.DIGIT,
        NumberShape.CIRCLED_NUMBER: NumberType2.CIRCLED_DIGIT,
        NumberShape.UPPERCASE_ROMAN_NUMBER: NumberType2.ROMAN_CAPITAL,
        NumberShape.LOWERCASE_ROMAN_NUMBER: NumberType2.ROMAN_SMALL,
        NumberShape.UPPERCASE_ALPHABET: NumberType2.LATIN_CAPITAL,
        NumberShape.LOWERCASE_ALPHABET: NumberType2.LATIN_SMALL,
        NumberShape.CIRCLED_UPPERCASE_ALPHABET: NumberType2.CIRCLED_LATIN_CAPTION,
        NumberShape.CIRCLED_LOWERCASE_ALPHABET: NumberType2.CIRCLED_LATIN_SMALL,
        NumberShape.HANGUL: NumberType2.HANGUL_SYLLABLE,
        NumberShape.CIRCLED_HANGUL: NumberType2.CIRCLED_HANGUL_SYLLABLE,
        NumberShape.HANGUL_JAMO: NumberType2.HANGUL_JAMO,
        NumberShape.CIRCLED_HANGUL_JAMO: NumberType2.CIRCLED_HANGUL_JAMO,
        NumberShape.HANGUL_NUMBER: NumberType2.HANGUL_PHONETIC,
        NumberShape.HANJA_NUMBER: NumberType2.IDEOGRAPH,
        NumberShape.CIRCLED_HANJA_NUMBER: NumberType2.CIRCLED_IDEOGRAPH,
        NumberShape.HANGUL_SIBGAN: NumberType2.DECAGON_CIRCLE,
        NumberShape.HANJA_SIBGAN: NumberType2.DECAGON_CIRCLE_HANJA,
        NumberShape.SYMBOL: NumberType2.SYMBOL,
        NumberShape.USER_CHAR: NumberType2.USER_CHAR,
    }
    return mapping.get(hwp_shape, NumberType2.DIGIT)


def number_type1_from_number_shape(hwp_shape: NumberShape) -> NumberType1:
    """Convert HWP NumberShape to HWPX NumberType1."""
    mapping = {
        NumberShape.NUMBER: NumberType1.DIGIT,
        NumberShape.CIRCLED_NUMBER: NumberType1.CIRCLED_DIGIT,
        NumberShape.UPPERCASE_ROMAN_NUMBER: NumberType1.ROMAN_CAPITAL,
        NumberShape.LOWERCASE_ROMAN_NUMBER: NumberType1.ROMAN_SMALL,
        NumberShape.UPPERCASE_ALPHABET: NumberType1.LATIN_CAPITAL,
        NumberShape.LOWERCASE_ALPHABET: NumberType1.LATIN_SMALL,
        NumberShape.CIRCLED_UPPERCASE_ALPHABET: NumberType1.CIRCLED_LATIN_CAPTION,
        NumberShape.CIRCLED_LOWERCASE_ALPHABET: NumberType1.CIRCLED_LATIN_SMALL,
        NumberShape.HANGUL: NumberType1.HANGUL_SYLLABLE,
        NumberShape.CIRCLED_HANGUL: NumberType1.CIRCLED_HANGUL_SYLLABLE,
        NumberShape.HANGUL_JAMO: NumberType1.HANGUL_JAMO,
        NumberShape.CIRCLED_HANGUL_JAMO: NumberType1.CIRCLED_HANGUL_JAMO,
        NumberShape.HANGUL_NUMBER: NumberType1.HANGUL_PHONETIC,
        NumberShape.HANJA_NUMBER: NumberType1.IDEOGRAPH,
        NumberShape.CIRCLED_HANJA_NUMBER: NumberType1.CIRCLED_IDEOGRAPH,
    }
    return mapping.get(hwp_shape, NumberType1.DIGIT)


def string_null_check(s: str | None) -> str:
    """Check for null character in string and return empty string if found."""
    if s is not None and len(s) == 1 and ord(s[0]) == 0:
        return ""
    return s or ""


def vertical_align2_from_text_vertical_alignment(
    hwp_align: TextVerticalAlignment,
) -> VerticalAlign2:
    """Convert HWP TextVerticalAlignment to HWPX VerticalAlign2."""
    mapping = {
        TextVerticalAlignment.TOP: VerticalAlign2.TOP,
        TextVerticalAlignment.CENTER: VerticalAlign2.CENTER,
        TextVerticalAlignment.BOTTOM: VerticalAlign2.BOTTOM,
    }
    return mapping.get(hwp_align, VerticalAlign2.CENTER)


def apply_page_type_from_header_footer_apply_page(
    hwp_apply_page: HeaderFooterApplyPage,
) -> ApplyPageType:
    """Convert HWP HeaderFooterApplyPage to HWPX ApplyPageType."""
    mapping = {
        HeaderFooterApplyPage.BOTH_PAGE: ApplyPageType.BOTH,
        HeaderFooterApplyPage.EVEN_PAGE: ApplyPageType.EVEN,
        HeaderFooterApplyPage.ODD_PAGE: ApplyPageType.ODD,
    }
    return mapping.get(hwp_apply_page, ApplyPageType.BOTH)


def to_unsigned_32(value: int) -> int:
    """Convert signed 32-bit value to unsigned."""
    return value & 0xFFFFFFFF


def to_unsigned_8(value: int) -> int:
    """Convert signed 8-bit value to unsigned."""
    return value & 0xFF


def num_type_from_number_sort(hwp_sort: NumberSort) -> NumType:
    """Convert HWP NumberSort to HWPX NumType."""
    mapping = {
        NumberSort.PAGE: NumType.PAGE,
        NumberSort.FOOTNOTE: NumType.FOOTNOTE,
        NumberSort.ENDNOTE: NumType.ENDNOTE,
        NumberSort.PICTURE: NumType.PICTURE,
        NumberSort.TABLE: NumType.TABLE,
        NumberSort.EQUATION: NumType.EQUATION,
    }
    return mapping.get(hwp_sort, NumType.PAGE)


def line_type2_from_line_type(hwp_type: LineType) -> LineType2:
    """Convert HWP LineType to HWPX LineType2."""
    mapping = {
        LineType.NONE: LineType2.NONE,
        LineType.SOLID: LineType2.SOLID,
        LineType.DASH: LineType2.DOT,  # Note: dash => dot in HWPX
        LineType.DOT: LineType2.DASH,  # Note: dot => dash in HWPX
        LineType.DASH_DOT: LineType2.DASH_DOT,
        LineType.DASH_DOT_DOT: LineType2.DASH_DOT_DOT,
        LineType.LONG_DASH: LineType2.LONG_DASH,
        LineType.CIRCLE_DOT: LineType2.CIRCLE,
        LineType.DOUBLE: LineType2.DOUBLE_SLIM,
        LineType.THIN_BOLD: LineType2.SLIM_THICK,
        LineType.BOLD_THIN: LineType2.THICK_SLIM,
        LineType.THIN_BOLD_THIN: LineType2.SLIM_THICK_SLIM,
    }
    return mapping.get(hwp_type, LineType2.NONE)


def line_cap_from_line_end_shape(hwp_shape: LineEndShape) -> LineCap:
    """Convert HWP LineEndShape to HWPX LineCap."""
    if hwp_shape == LineEndShape.ROUND:
        return LineCap.ROUND
    return LineCap.FLAT


def arrow_type_from_line_arrow_shape(hwp_shape: LineArrowShape) -> ArrowType:
    """Convert HWP LineArrowShape to HWPX ArrowType."""
    mapping = {
        LineArrowShape.NONE: ArrowType.NORMAL,
        LineArrowShape.ARROW: ArrowType.ARROW,
        LineArrowShape.LINED_ARROW: ArrowType.SPEAR,
        LineArrowShape.CONCAVE_ARROW: ArrowType.CONCAVE_ARROW,
        LineArrowShape.DIAMOND: ArrowType.EMPTY_DIAMOND,
        LineArrowShape.CIRCLE: ArrowType.EMPTY_CIRCLE,
        LineArrowShape.RECTANGLE: ArrowType.EMPTY_BOX,
    }
    return mapping.get(hwp_shape, ArrowType.NORMAL)


def arrow_fill_from_line_arrow_shape(hwp_shape: LineArrowShape) -> bool:
    """Check if arrow should be filled based on HWP LineArrowShape."""
    return hwp_shape in (
        LineArrowShape.NONE,
        LineArrowShape.DIAMOND,
        LineArrowShape.CIRCLE,
        LineArrowShape.RECTANGLE,
    )


def arrow_size_from_line_arrow_size(hwp_size: LineArrowSize) -> ArrowSize:
    """Convert HWP LineArrowSize to HWPX ArrowSize."""
    mapping = {
        LineArrowSize.SMALL_SMALL: ArrowSize.SMALL_SMALL,
        LineArrowSize.SMALL_MIDDLE: ArrowSize.SMALL_MEDIUM,
        LineArrowSize.SMALL_BIG: ArrowSize.SMALL_LARGE,
        LineArrowSize.MIDDLE_SMALL: ArrowSize.MEDIUM_SMALL,
        LineArrowSize.MIDDLE_MIDDLE: ArrowSize.MEDIUM_MEDIUM,
        LineArrowSize.MIDDLE_BIG: ArrowSize.MEDIUM_LARGE,
        LineArrowSize.BIG_SMALL: ArrowSize.LARGE_SMALL,
        LineArrowSize.BIG_MIDDLE: ArrowSize.LARGE_MEDIUM,
        LineArrowSize.BIG_BIG: ArrowSize.LARGE_LARGE,
    }
    return mapping.get(hwp_size, ArrowSize.SMALL_SMALL)


def arc_type_from_hwp_arc_type(hwp_type: HWPArcType) -> ArcType:
    """Convert HWP ArcType to HWPX ArcType."""
    mapping = {
        HWPArcType.ARC: ArcType.NORMAL,
        HWPArcType.CIRCULAR_SECTOR: ArcType.PIE,
        HWPArcType.BOW: ArcType.CHORD,
    }
    return mapping.get(hwp_type, ArcType.NORMAL)
