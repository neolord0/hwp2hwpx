"""HWP tag ID definitions."""

from enum import IntEnum


class HWPTag(IntEnum):
    """HWP tag ID enumeration for record headers."""

    # DocInfo tags (0x10 ~ 0x1F)
    DOCUMENT_PROPERTIES = 0x10
    ID_MAPPINGS = 0x11
    BIN_DATA = 0x12
    FACE_NAME = 0x13
    BORDER_FILL = 0x14
    CHAR_SHAPE = 0x15
    TAB_DEF = 0x16
    NUMBERING = 0x17
    BULLET = 0x18
    PARA_SHAPE = 0x19
    STYLE = 0x1A
    DOC_DATA = 0x1B
    DISTRIBUTE_DOC_DATA = 0x1C
    COMPATIBLE_DOCUMENT = 0x1E
    LAYOUT_COMPATIBILITY = 0x1F

    # Additional DocInfo tags (0x20 ~ 0x2F)
    TRACK_CHANGE = 0x20
    MEMO_SHAPE = 0x21
    FORBIDDEN_CHAR = 0x22
    TRACK_CHANGE_AUTHOR = 0x23

    # Section/Body tags (0x42 ~ 0x90)
    PARA_HEADER = 0x42
    PARA_TEXT = 0x43
    PARA_CHAR_SHAPE = 0x44
    PARA_LINE_SEG = 0x45
    PARA_RANGE_TAG = 0x46
    CTRL_HEADER = 0x47
    LIST_HEADER = 0x48
    PAGE_DEF = 0x49
    FOOTNOTE_SHAPE = 0x4A
    PAGE_BORDER_FILL = 0x4B
    SHAPE_COMPONENT = 0x4C
    TABLE = 0x4D
    SHAPE_COMPONENT_LINE = 0x4E
    SHAPE_COMPONENT_RECTANGLE = 0x4F
    SHAPE_COMPONENT_ELLIPSE = 0x50
    SHAPE_COMPONENT_ARC = 0x51
    SHAPE_COMPONENT_POLYGON = 0x52
    SHAPE_COMPONENT_CURVE = 0x53
    SHAPE_COMPONENT_OLE = 0x54
    SHAPE_COMPONENT_PICTURE = 0x55
    SHAPE_COMPONENT_CONTAINER = 0x56
    CTRL_DATA = 0x57
    EQEDIT = 0x58
    CTRL_FIELD_BEGIN = 0x59

    # Reserved (0x5A ~ 0x5F)
    SHAPE_COMPONENT_TEXTART = 0x5A
    FORM_OBJECT = 0x5B
    MEMO_COMMENT = 0x5C
    MEMO_LIST = 0x5D
    CHART_DATA = 0x5E
    VIDEO_DATA = 0x5F

    # Section tags (0x60 ~ 0x6F)
    SHAPE_COMPONENT_UNKNOWN = 0x60

    # Additional tags
    LIST_HEADER_APPEND = 0x62
    TRACK_CHANGE_CONTENT = 0x63

    # Table cell properties
    TABLE_CELL_SPLIT_INFO = 0x8000
    TABLE_CELL_ZONE_INFO = 0x8001

    # Extension tags
    SHAPE_COMPONENT_CONNECT_LINE = 0x8002

    # Unknown tag marker
    UNKNOWN = 0xFFFF


# Tag ID ranges for DocInfo
DOCINFO_TAG_START = 0x10
DOCINFO_TAG_END = 0x40

# Tag ID ranges for BodyText
BODYTEXT_TAG_START = 0x42
BODYTEXT_TAG_END = 0x90


def is_docinfo_tag(tag_id: int) -> bool:
    """Check if tag ID belongs to DocInfo section."""
    return DOCINFO_TAG_START <= tag_id <= DOCINFO_TAG_END


def is_bodytext_tag(tag_id: int) -> bool:
    """Check if tag ID belongs to BodyText section."""
    return BODYTEXT_TAG_START <= tag_id <= BODYTEXT_TAG_END


def get_tag_name(tag_id: int) -> str:
    """Get human-readable tag name from ID."""
    try:
        return HWPTag(tag_id).name
    except ValueError:
        return f"UNKNOWN_0x{tag_id:04X}"
