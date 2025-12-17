"""Alignment enumerations for HWPX format."""

from enum import Enum


class HorizontalAlign2(Enum):
    """Horizontal alignment for paragraphs."""

    JUSTIFY = "justify"
    LEFT = "left"
    RIGHT = "right"
    CENTER = "center"
    DISTRIBUTE = "distribute"
    DISTRIBUTE_SPACE = "distribute_space"


class VerticalAlign1(Enum):
    """Vertical alignment for paragraph shapes."""

    BASELINE = "baseline"
    TOP = "top"
    CENTER = "center"
    BOTTOM = "bottom"


class VerticalAlign2(Enum):
    """Vertical alignment for text boxes and cells."""

    TOP = "top"
    CENTER = "center"
    BOTTOM = "bottom"


class TextDirection(Enum):
    """Text direction enumeration."""

    HORIZONTAL = "horizontal"
    VERTICAL = "vertical"


class LineWrap(Enum):
    """Line wrap type enumeration."""

    BREAK = "break"
    SQUEEZE = "squeeze"
    KEEP = "keep"


class TextWrap(Enum):
    """Text wrap type enumeration."""

    SQUARE = "square"
    TIGHT = "tight"
    THROUGH = "through"
    TOP_BOTTOM = "top_bottom"
    BEHIND_TEXT = "behind_text"
    IN_FRONT_OF_TEXT = "in_front_of_text"


class VPosType(Enum):
    """Vertical position type enumeration."""

    TOP = "top"
    CENTER = "center"
    BOTTOM = "bottom"
    INSIDE = "inside"
    OUTSIDE = "outside"


class HPosType(Enum):
    """Horizontal position type enumeration."""

    LEFT = "left"
    CENTER = "center"
    RIGHT = "right"
    INSIDE = "inside"
    OUTSIDE = "outside"


class VRelTo(Enum):
    """Vertical position relative to enumeration."""

    PAPER = "paper"
    PAGE = "page"
    PARA = "para"


class HRelTo(Enum):
    """Horizontal position relative to enumeration."""

    PAPER = "paper"
    PAGE = "page"
    COLUMN = "column"
    PARA = "para"


class WidthRelTo(Enum):
    """Width relative to enumeration."""

    PAPER = "paper"
    PAGE = "page"
    COLUMN = "column"
    PARA = "para"
    ABSOLUTE = "absolute"


class HeightRelTo(Enum):
    """Height relative to enumeration."""

    PAPER = "paper"
    PAGE = "page"
    ABSOLUTE = "absolute"
