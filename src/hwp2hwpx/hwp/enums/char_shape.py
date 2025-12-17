"""Character shape enumerations for HWP format."""

from enum import Enum


class UnderlineType(Enum):
    """Underline type enumeration."""

    NONE = 0
    BOTTOM = 1
    CENTER = 2
    TOP = 3


class StrikeoutType(Enum):
    """Strikeout type enumeration."""

    NONE = 0
    SINGLE = 1


class OutlineType(Enum):
    """Outline type enumeration."""

    NONE = 0
    SOLID = 1
    DOT = 2
    BOLD = 3
    DASHED = 4
    LOOSE_DASHED = 5
    DASH_DOT = 6
    DASH_DOT_DOT = 7


class ShadowType(Enum):
    """Shadow type enumeration."""

    NONE = 0
    DISCRETE = 1
    CONTINUOUS = 2


class EmphasisType(Enum):
    """Emphasis type enumeration."""

    NONE = 0
    DOT_ABOVE = 1
    RING_ABOVE = 2
    TILDE_ABOVE = 3
    COMMA_ABOVE = 4
    HIDDEN = 5
    DOT_BELOW = 6
    RING_BELOW = 7
    TILDE_BELOW = 8
    COMMA_BELOW = 9


class FontType(Enum):
    """Font type enumeration."""

    NONE = 0
    TRUE_TYPE = 1
    HWP_FONT = 2


class SymbolMarkSort(Enum):
    """Symbol mark sort enumeration."""

    CHECK = 0
    CIRCLE = 1
    CROSS = 2
    NUMBER = 3
    FILLED = 4


class CharOffsetType(Enum):
    """Character offset type enumeration."""

    PERCENT = 0
    HWPUNIT = 1


class ScriptType(Enum):
    """Script type enumeration for font families."""

    KOREAN = 0
    ENGLISH = 1
    CHINESE = 2
    JAPANESE = 3
    OTHER = 4
    SYMBOL = 5
    USER = 6
