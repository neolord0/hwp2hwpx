"""Control enumerations for HWP format."""

from enum import Enum


class ControlType(Enum):
    """Control type enumeration."""

    SECTION_DEFINE = "secd"
    COLUMN_DEFINE = "cold"
    TABLE = "tbl "
    EQUATION = "eqed"
    HEADER = "head"
    FOOTER = "foot"
    FOOTNOTE = "fn  "
    ENDNOTE = "en  "
    AUTO_NUMBER = "atno"
    NEW_NUMBER = "nwno"
    PAGE_HIDE = "pghd"
    PAGE_NUMBER_POSITION = "pgnp"
    PAGE_ODD_EVEN_ADJUST = "pgod"
    PAGE_NUMBER = "pgno"
    INDEX_MARK = "idxm"
    BOOKMARK = "bokm"
    OVER_LAPPING_LETTER = "tcps"
    ADDITIONAL_TEXT = "tdut"
    HIDDEN_COMMENT = "tcmt"
    FORM = "form"
    FIELD_BEGIN = "%???",  # Field types start with %
    FIELD_END = "????",
    GSO = "gso "


class GsoType(Enum):
    """Graphic shape object type enumeration."""

    LINE = 1
    RECTANGLE = 2
    ELLIPSE = 3
    ARC = 4
    POLYGON = 5
    CURVE = 6
    PICTURE = 7
    OLE = 8
    CONTAINER = 9
    CONNECT_LINE = 10
    TEXTART = 11
    VIDEO = 12


class LineEndShape(Enum):
    """Line end shape enumeration."""

    ROUND = 0
    FLAT = 1


class LineArrowShape(Enum):
    """Line arrow shape enumeration."""

    NONE = 0
    ARROW = 1
    LINED_ARROW = 2
    CONCAVE_ARROW = 3
    DIAMOND = 4
    CIRCLE = 5
    RECTANGLE = 6


class LineArrowSize(Enum):
    """Line arrow size enumeration."""

    SMALL_SMALL = 0
    SMALL_MIDDLE = 1
    SMALL_BIG = 2
    MIDDLE_SMALL = 3
    MIDDLE_MIDDLE = 4
    MIDDLE_BIG = 5
    BIG_SMALL = 6
    BIG_MIDDLE = 7
    BIG_BIG = 8


class LineType(Enum):
    """Line type enumeration for GSO."""

    NONE = 0
    SOLID = 1
    DASH = 2
    DOT = 3
    DASH_DOT = 4
    DASH_DOT_DOT = 5
    LONG_DASH = 6
    CIRCLE_DOT = 7
    DOUBLE = 8
    THIN_BOLD = 9
    BOLD_THIN = 10
    THIN_BOLD_THIN = 11


class TextVerticalAlignment(Enum):
    """Text vertical alignment for text boxes."""

    TOP = 0
    CENTER = 1
    BOTTOM = 2


class NumberSort(Enum):
    """Number sort for auto numbering."""

    PAGE = 0
    FOOTNOTE = 1
    ENDNOTE = 2
    PICTURE = 3
    TABLE = 4
    EQUATION = 5


class HeaderFooterApplyPage(Enum):
    """Header/footer apply page enumeration."""

    BOTH_PAGE = 0
    EVEN_PAGE = 1
    ODD_PAGE = 2


class ArcType(Enum):
    """Arc type enumeration."""

    ARC = 0
    CIRCULAR_SECTOR = 1
    BOW = 2


class HorzRelTo(Enum):
    """Horizontal relative to enumeration."""

    PAPER = 0
    PAGE = 1
    COLUMN = 2
    PARA = 3


class VertRelTo(Enum):
    """Vertical relative to enumeration."""

    PAPER = 0
    PAGE = 1
    PARA = 2


class HorzAlign(Enum):
    """Horizontal alignment for positioned objects."""

    LEFT = 0
    CENTER = 1
    RIGHT = 2
    INSIDE = 3
    OUTSIDE = 4


class VertAlign(Enum):
    """Vertical alignment for positioned objects."""

    TOP = 0
    CENTER = 1
    BOTTOM = 2
    INSIDE = 3
    OUTSIDE = 4


class WidthCriterion(Enum):
    """Width criterion enumeration."""

    PAPER = 0
    PAGE = 1
    COLUMN = 2
    PARA = 3
    ABSOLUTE = 4


class HeightCriterion(Enum):
    """Height criterion enumeration."""

    PAPER = 0
    PAGE = 1
    ABSOLUTE = 2


class TextFlowType(Enum):
    """Text flow type enumeration."""

    SQUARE = 0
    TIGHT = 1
    THROUGH = 2
    TOP_BOTTOM = 3
    BEHIND_TEXT = 4
    IN_FRONT_OF_TEXT = 5


class ObjectNumberSort(Enum):
    """Object number sort for captions."""

    NONE = 0
    FIGURE = 1
    TABLE = 2
    EQUATION = 3
