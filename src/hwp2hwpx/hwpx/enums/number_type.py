"""Number type enumerations for HWPX format."""

from enum import Enum


class NumberType1(Enum):
    """Number type for numbering and paragraph formats."""

    DIGIT = "digit"
    CIRCLED_DIGIT = "circled_digit"
    ROMAN_CAPITAL = "roman_capital"
    ROMAN_SMALL = "roman_small"
    LATIN_CAPITAL = "latin_capital"
    LATIN_SMALL = "latin_small"
    CIRCLED_LATIN_CAPTION = "circled_latin_capital"
    CIRCLED_LATIN_SMALL = "circled_latin_small"
    HANGUL_SYLLABLE = "hangul_syllable"
    CIRCLED_HANGUL_SYLLABLE = "circled_hangul_syllable"
    HANGUL_JAMO = "hangul_jamo"
    CIRCLED_HANGUL_JAMO = "circled_hangul_jamo"
    HANGUL_PHONETIC = "hangul_phonetic"
    IDEOGRAPH = "ideograph"
    CIRCLED_IDEOGRAPH = "circled_ideograph"


class NumberType2(Enum):
    """Extended number type for section numbering."""

    DIGIT = "digit"
    CIRCLED_DIGIT = "circled_digit"
    ROMAN_CAPITAL = "roman_capital"
    ROMAN_SMALL = "roman_small"
    LATIN_CAPITAL = "latin_capital"
    LATIN_SMALL = "latin_small"
    CIRCLED_LATIN_CAPTION = "circled_latin_capital"
    CIRCLED_LATIN_SMALL = "circled_latin_small"
    HANGUL_SYLLABLE = "hangul_syllable"
    CIRCLED_HANGUL_SYLLABLE = "circled_hangul_syllable"
    HANGUL_JAMO = "hangul_jamo"
    CIRCLED_HANGUL_JAMO = "circled_hangul_jamo"
    HANGUL_PHONETIC = "hangul_phonetic"
    IDEOGRAPH = "ideograph"
    CIRCLED_IDEOGRAPH = "circled_ideograph"
    DECAGON_CIRCLE = "decagon_circle"
    DECAGON_CIRCLE_HANJA = "decagon_circle_hanja"
    SYMBOL = "symbol"
    USER_CHAR = "user_char"


class ValueUnit1(Enum):
    """Value unit type enumeration."""

    PERCENT = "percent"
    HWPUNIT = "hwpunit"


class NumType(Enum):
    """Auto number type enumeration."""

    PAGE = "page"
    FOOTNOTE = "footnote"
    ENDNOTE = "endnote"
    PICTURE = "picture"
    TABLE = "table"
    EQUATION = "equation"


class ApplyPageType(Enum):
    """Apply page type for headers/footers."""

    BOTH = "both"
    EVEN = "even"
    ODD = "odd"


class ArcType(Enum):
    """Arc type enumeration."""

    NORMAL = "normal"
    PIE = "pie"
    CHORD = "chord"
