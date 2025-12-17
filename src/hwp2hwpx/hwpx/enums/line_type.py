"""Line type enumerations for HWPX format."""

from enum import Enum


class LineType2(Enum):
    """Line type enumeration for borders and shapes."""

    NONE = "none"
    SOLID = "solid"
    DASH = "dash"
    DOT = "dot"
    DASH_DOT = "dash_dot"
    DASH_DOT_DOT = "dash_dot_dot"
    LONG_DASH = "long_dash"
    CIRCLE = "circle"
    DOUBLE_SLIM = "double_slim"
    SLIM_THICK = "slim_thick"
    THICK_SLIM = "thick_slim"
    SLIM_THICK_SLIM = "slim_thick_slim"


class LineType3(Enum):
    """Line type enumeration for character shapes."""

    SOLID = "solid"
    DASH = "dash"
    DOT = "dot"
    DASH_DOT = "dash_dot"
    DASH_DOT_DOT = "dash_dot_dot"
    LONG_DASH = "long_dash"
    CIRCLE = "circle"
    DOUBLE_SLIM = "double_slim"
    SLIM_THICK = "slim_thick"
    THICK_SLIM = "thick_slim"
    SLIM_THICK_SLIM = "slim_thick_slim"
    WAVE = "wave"
    DOUBLEWAVE = "double_wave"


class LineWidth(Enum):
    """Line width enumeration (in millimeters)."""

    MM_0_1 = "0.1mm"
    MM_0_12 = "0.12mm"
    MM_0_15 = "0.15mm"
    MM_0_2 = "0.2mm"
    MM_0_25 = "0.25mm"
    MM_0_3 = "0.3mm"
    MM_0_4 = "0.4mm"
    MM_0_5 = "0.5mm"
    MM_0_6 = "0.6mm"
    MM_0_7 = "0.7mm"
    MM_1_0 = "1.0mm"
    MM_1_5 = "1.5mm"
    MM_2_0 = "2.0mm"
    MM_3_0 = "3.0mm"
    MM_4_0 = "4.0mm"
    MM_5_0 = "5.0mm"


class LineCap(Enum):
    """Line cap (end shape) enumeration."""

    FLAT = "flat"
    ROUND = "round"


class ArrowType(Enum):
    """Arrow type enumeration."""

    NORMAL = "normal"
    ARROW = "arrow"
    SPEAR = "spear"
    CONCAVE_ARROW = "concave_arrow"
    EMPTY_DIAMOND = "empty_diamond"
    EMPTY_CIRCLE = "empty_circle"
    EMPTY_BOX = "empty_box"
    FILLED_DIAMOND = "filled_diamond"
    FILLED_CIRCLE = "filled_circle"
    FILLED_BOX = "filled_box"


class ArrowSize(Enum):
    """Arrow size enumeration."""

    SMALL_SMALL = "small_small"
    SMALL_MEDIUM = "small_medium"
    SMALL_LARGE = "small_large"
    MEDIUM_SMALL = "medium_small"
    MEDIUM_MEDIUM = "medium_medium"
    MEDIUM_LARGE = "medium_large"
    LARGE_SMALL = "large_small"
    LARGE_MEDIUM = "large_medium"
    LARGE_LARGE = "large_large"
