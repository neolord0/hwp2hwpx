"""Border and fill enumerations for HWP format."""

from enum import Enum, IntEnum


class BorderType(Enum):
    """Border type enumeration."""

    NONE = 0
    SOLID = 1
    DASH = 2
    DOT = 3
    DASH_DOT = 4
    DASH_DOT_DOT = 5
    LONG_DASH = 6
    CIRCLE_DOT = 7
    DOUBLE = 8
    THIN_THICK = 9
    THICK_THIN = 10
    THIN_THICK_THIN = 11


class BorderType2(Enum):
    """Extended border type enumeration for character shapes."""

    SOLID = 0
    DASH = 1
    DOT = 2
    DASH_DOT = 3
    DASH_DOT_DOT = 4
    LONG_DASH = 5
    CIRCLE_DOT = 6
    DOUBLE = 7
    THIN_THICK = 8
    THICK_THIN = 9
    THIN_THICK_THIN = 10
    WAVE = 11
    DOUBLE_WAVE = 12
    THICK_3D = 13
    THICK_3D_REVERSE_LIGHTING = 14
    SOLID_3D = 15
    SOLID_3D_REVERSE_LIGHTING = 16


class BorderThickness(Enum):
    """Border thickness enumeration (in millimeters)."""

    MM0_1 = 0
    MM0_12 = 1
    MM0_15 = 2
    MM0_2 = 3
    MM0_25 = 4
    MM0_3 = 5
    MM0_4 = 6
    MM0_5 = 7
    MM0_6 = 8
    MM0_7 = 9
    MM1_0 = 10
    MM1_5 = 11
    MM2_0 = 12
    MM3_0 = 13
    MM4_0 = 14
    MM5_0 = 15


class FillType(IntEnum):
    """Fill type bit flags."""

    NONE = 0
    PATTERN_FILL = 1
    IMAGE_FILL = 2
    GRADIENT_FILL = 4


class PatternType(Enum):
    """Pattern fill type enumeration."""

    NONE = -1
    HORIZONTAL = 0
    VERTICAL = 1
    BACK_SLASH = 2
    SLASH = 3
    CROSS = 4
    CROSS_DIAGONAL = 5


class GradientType(Enum):
    """Gradient type enumeration."""

    LINEAR = 1
    RADIAL = 2
    CONICAL = 3
    SQUARE = 4


class ImageFillType(Enum):
    """Image fill type enumeration."""

    TILE_ALL = 0
    TILE_HORIZONTAL_TOP = 1
    TILE_HORIZONTAL_BOTTOM = 2
    TILE_VERTICAL_LEFT = 3
    TILE_VERTICAL_RIGHT = 4
    FIT = 5
    CENTER = 6
    CENTER_TOP = 7
    CENTER_BOTTOM = 8
    LEFT_CENTER = 9
    LEFT_TOP = 10
    LEFT_BOTTOM = 11
    RIGHT_CENTER = 12
    RIGHT_TOP = 13
    RIGHT_BOTTOM = 14
    NONE = 15


class DiagonalType(Enum):
    """Diagonal line type enumeration for table cells."""

    NONE = 0
    SLASH = 1
    BACK_SLASH = 2
    CROSS = 3
