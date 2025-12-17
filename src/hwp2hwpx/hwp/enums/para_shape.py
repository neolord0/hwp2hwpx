"""Paragraph shape enumerations for HWP format."""

from enum import Enum


class Alignment(Enum):
    """Paragraph alignment enumeration."""

    JUSTIFY = 0
    LEFT = 1
    RIGHT = 2
    CENTER = 3
    DISTRIBUTE = 4
    DIVIDE = 5


class VerticalAlignment(Enum):
    """Vertical alignment enumeration."""

    BY_FONT = 0
    TOP = 1
    CENTER = 2
    BOTTOM = 3


class LineSpacingType(Enum):
    """Line spacing type enumeration."""

    PERCENT = 0
    FIXED = 1
    BETWEEN_LINES = 2
    AT_LEAST = 3


class TabType(Enum):
    """Tab type enumeration."""

    LEFT = 0
    RIGHT = 1
    CENTER = 2
    DECIMAL = 3


class TabLeader(Enum):
    """Tab leader type enumeration."""

    NONE = 0
    SOLID = 1
    DASH = 2
    DOT = 3
    DASH_DOT = 4
    DASH_DOT_DOT = 5
    LONG_DASH = 6
    HYPHEN = 7


class BreakType(Enum):
    """Break type enumeration."""

    NONE = 0
    PAGE = 1
    COLUMN = 2


class HeadingType(Enum):
    """Heading type enumeration."""

    NONE = 0
    OUTLINE = 1
    NUMBER = 2
    BULLET = 3


class ParagraphNumberFormat(Enum):
    """Paragraph number format enumeration."""

    NUMBER = 0
    CIRCLED_NUMBER = 1
    UPPERCASE_ROMAN_NUMBER = 2
    LOWERCASE_ROMAN_NUMBER = 3
    UPPERCASE_ALPHABET = 4
    LOWERCASE_ALPHABET = 5
    CIRCLED_UPPERCASE_ALPHABET = 6
    CIRCLED_LOWERCASE_ALPHABET = 7
    HANGUL = 8
    CIRCLED_HANGUL = 9
    HANGUL_JAMO = 10
    CIRCLED_HANGUL_JAMO = 11
    HANGUL_NUMBER = 12
    HANJA_NUMBER = 13
    CIRCLED_HANJA_NUMBER = 14


class ValueType(Enum):
    """Value type for paragraph numbering."""

    RATIO_FOR_LETTER = 0
    VALUE = 1


class NumberShape(Enum):
    """Number shape enumeration."""

    NUMBER = 0
    CIRCLED_NUMBER = 1
    UPPERCASE_ROMAN_NUMBER = 2
    LOWERCASE_ROMAN_NUMBER = 3
    UPPERCASE_ALPHABET = 4
    LOWERCASE_ALPHABET = 5
    CIRCLED_UPPERCASE_ALPHABET = 6
    CIRCLED_LOWERCASE_ALPHABET = 7
    HANGUL = 8
    CIRCLED_HANGUL = 9
    HANGUL_JAMO = 10
    CIRCLED_HANGUL_JAMO = 11
    HANGUL_NUMBER = 12
    HANJA_NUMBER = 13
    CIRCLED_HANJA_NUMBER = 14
    HANGUL_SIBGAN = 15
    HANJA_SIBGAN = 16
    SYMBOL = 0x80
    USER_CHAR = 0x81
