"""Paragraph models for HWP format."""

from dataclasses import dataclass, field
from enum import IntEnum
from typing import TYPE_CHECKING, List, Optional

if TYPE_CHECKING:
    from hwp2hwpx.hwp.models.control import Control


class CharType(IntEnum):
    """Character type in paragraph text."""

    NORMAL = 0  # Normal character
    CHAR_CONTROL = 1  # Single character control (1 char)
    INLINE_CONTROL = 2  # Inline control (8 chars)
    EXTEND_CONTROL = 3  # Extended control (8 chars)


# Special control characters
CHAR_LINE_BREAK = 0x0A  # Line break within paragraph
CHAR_PARA_BREAK = 0x0D  # Paragraph break (end of paragraph)
CHAR_HYPHEN = 0x1E  # Hard hyphen
CHAR_SPACE = 0x20  # Normal space
CHAR_NBSP = 0xA0  # Non-breaking space

# Extended control markers
CHAR_SECTION_COLUMN_DEF = 0x02  # Section/column definition
CHAR_FIELD_BEGIN = 0x03  # Field start
CHAR_FIELD_END = 0x04  # Field end
CHAR_INLINE_CTRL = 0x08  # Inline control
CHAR_EXTENDED_CTRL = 0x09  # Extended control
CHAR_TABLE = 0x0B  # Table
CHAR_GSO = 0x0C  # Graphic shape object

# Inline controls (8 chars)
CHAR_BOOKMARK = 0x10  # Bookmark
CHAR_DUTMAL = 0x11  # Dutmal (annotation)
CHAR_HIDDEN_COMMENT = 0x12  # Hidden comment
CHAR_HEADER_FOOTER = 0x13  # Header/footer
CHAR_FOOTNOTE = 0x14  # Footnote
CHAR_ENDNOTE = 0x15  # Endnote
CHAR_AUTO_NUMBER = 0x16  # Auto number
CHAR_NEW_NUMBER = 0x17  # New number
CHAR_PAGE_CTRL = 0x18  # Page control


@dataclass
class ParaHeader:
    """Paragraph header information."""

    text_length: int = 0  # Length of text in characters
    control_mask: int = 0  # Bit mask for control types
    para_shape_id: int = 0  # Reference to ParaShape
    style_id: int = 0  # Reference to Style
    column_type: int = 0  # Column type
    char_shape_count: int = 0
    range_tag_count: int = 0
    line_align_count: int = 0
    instance_id: int = 0

    @property
    def has_para_shape(self) -> bool:
        """Check if has paragraph shape reference."""
        return self.para_shape_id > 0


@dataclass
class ParaCharShape:
    """Character shape position mapping in paragraph.

    Maps positions in paragraph text to character shape indices.
    """

    position: int = 0  # Starting position in text
    char_shape_id: int = 0  # Reference to CharShape


@dataclass
class ParaLineSeg:
    """Line segment information for paragraph layout."""

    text_start: int = 0
    line_vertical_position: int = 0
    line_height: int = 0
    text_part_height: int = 0
    distance_from_baseline: int = 0
    line_spacing: int = 0
    column_index: int = 0
    segment_width: int = 0
    tag: int = 0


@dataclass
class ParaRangeTag:
    """Range tag for special formatting regions."""

    start: int = 0
    end: int = 0
    tag: int = 0
    data: bytes = field(default_factory=bytes)


@dataclass
class ParaText:
    """Paragraph text content.

    Contains raw character codes and provides methods
    to iterate over characters and controls.
    """

    # Raw text data (UTF-16LE)
    text: str = ""

    # Extended control data (for INLINE_CTRL and EXTENDED_CTRL)
    control_chars: List[int] = field(default_factory=list)

    def __len__(self) -> int:
        return len(self.text)

    def __iter__(self):
        return iter(self.text)

    def get_char_type(self, index: int) -> CharType:
        """Get character type at index."""
        if index >= len(self.text):
            return CharType.NORMAL

        char_code = ord(self.text[index])

        # Check for special control characters
        if char_code in (CHAR_SECTION_COLUMN_DEF, CHAR_FIELD_BEGIN, CHAR_FIELD_END,
                         CHAR_TABLE, CHAR_GSO):
            return CharType.EXTEND_CONTROL

        if char_code == CHAR_EXTENDED_CTRL:
            return CharType.EXTEND_CONTROL

        if char_code == CHAR_INLINE_CTRL:
            return CharType.INLINE_CONTROL

        if char_code < 0x20 and char_code not in (CHAR_LINE_BREAK, CHAR_PARA_BREAK, CHAR_HYPHEN):
            return CharType.CHAR_CONTROL

        return CharType.NORMAL


@dataclass
class Paragraph:
    """Paragraph in HWP document.

    A paragraph consists of:
    - Header with metadata
    - Text content
    - Character shape mappings
    - Line segment information
    - Optional controls (tables, shapes, etc.)
    """

    header: ParaHeader = field(default_factory=ParaHeader)
    text: ParaText = field(default_factory=ParaText)
    char_shapes: List[ParaCharShape] = field(default_factory=list)
    line_segs: List[ParaLineSeg] = field(default_factory=list)
    range_tags: List[ParaRangeTag] = field(default_factory=list)
    controls: List["Control"] = field(default_factory=list)

    def get_char_shape_at(self, position: int) -> int:
        """Get character shape ID at text position."""
        result_id = 0
        for cs in self.char_shapes:
            if cs.position <= position:
                result_id = cs.char_shape_id
            else:
                break
        return result_id

    def get_control_at(self, index: int) -> Optional["Control"]:
        """Get control at specified index in controls list."""
        if 0 <= index < len(self.controls):
            return self.controls[index]
        return None

    @property
    def is_empty(self) -> bool:
        """Check if paragraph has no text content."""
        return len(self.text) <= 1  # Only paragraph break

    @property
    def plain_text(self) -> str:
        """Get plain text without control characters."""
        result = []
        for i, char in enumerate(self.text.text):
            char_type = self.text.get_char_type(i)
            if char_type == CharType.NORMAL:
                if char not in ('\r', '\n'):
                    result.append(char)
            elif char_type == CharType.EXTEND_CONTROL:
                # Skip 8 characters for extended control
                pass
            elif char_type == CharType.INLINE_CONTROL:
                # Skip 8 characters for inline control
                pass
        return ''.join(result)
