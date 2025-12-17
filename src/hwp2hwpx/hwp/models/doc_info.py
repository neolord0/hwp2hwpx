"""Document information models for HWP format."""

from dataclasses import dataclass, field
from typing import List, Optional

from hwp2hwpx.hwp.models.border_fill import BorderFillList
from hwp2hwpx.hwp.models.bullet import BulletList
from hwp2hwpx.hwp.models.char_shape import CharShapeList
from hwp2hwpx.hwp.models.face_name import FaceNameList
from hwp2hwpx.hwp.models.numbering import NumberingList
from hwp2hwpx.hwp.models.para_shape import ParaShapeList
from hwp2hwpx.hwp.models.style import StyleList
from hwp2hwpx.hwp.models.tab_def import TabDefList


@dataclass
class IDMappings:
    """ID mappings for document elements.

    Maps counts of various document elements that have been
    assigned IDs in the document.
    """

    bin_data_count: int = 0
    korean_font_count: int = 0
    english_font_count: int = 0
    chinese_font_count: int = 0
    japanese_font_count: int = 0
    other_font_count: int = 0
    symbol_font_count: int = 0
    user_font_count: int = 0
    border_fill_count: int = 0
    char_shape_count: int = 0
    tab_def_count: int = 0
    numbering_count: int = 0
    bullet_count: int = 0
    para_shape_count: int = 0
    style_count: int = 0
    memo_count: int = 0
    track_change_count: int = 0
    track_change_author_count: int = 0


@dataclass
class DocumentProperties:
    """Document-level properties."""

    section_count: int = 0
    starting_page_number: int = 1
    starting_footnote_number: int = 1
    starting_endnote_number: int = 1
    starting_picture_number: int = 1
    starting_table_number: int = 1
    starting_equation_number: int = 1
    list_id: int = 0
    paragraph_id: int = 0
    paragraph_char_unit_location: int = 0


@dataclass
class CompatibleDocument:
    """Compatible document settings."""

    target_program: int = 0


@dataclass
class LayoutCompatibility:
    """Layout compatibility settings."""

    char_width: int = 0
    char_spacing: int = 0
    word_spacing: int = 0
    line_height: int = 0
    char_offset: int = 0
    strip_tail_spaces: int = 0


@dataclass
class TrackChange:
    """Track change information."""

    change_id: int = 0
    author_id: int = 0
    change_type: int = 0
    date: str = ""


@dataclass
class TrackChangeAuthor:
    """Track change author information."""

    author_id: int = 0
    author_name: str = ""


@dataclass
class MemoShape:
    """Memo shape definition."""

    memo_id: int = 0
    width: int = 0
    properties: int = 0


@dataclass
class DocInfo:
    """Document information container.

    Contains all document-level settings and shared resources:
    - Document properties
    - Font definitions
    - Character shapes
    - Paragraph shapes
    - Border fills
    - Numbering/bullet definitions
    - Styles
    """

    # Document properties
    document_properties: DocumentProperties = field(default_factory=DocumentProperties)
    id_mappings: IDMappings = field(default_factory=IDMappings)

    # Font definitions
    face_names: FaceNameList = field(default_factory=FaceNameList)

    # Shape definitions
    char_shapes: CharShapeList = field(default_factory=CharShapeList)
    para_shapes: ParaShapeList = field(default_factory=ParaShapeList)
    tab_defs: TabDefList = field(default_factory=TabDefList)
    border_fills: BorderFillList = field(default_factory=BorderFillList)

    # Numbering and bullets
    numberings: NumberingList = field(default_factory=NumberingList)
    bullets: BulletList = field(default_factory=BulletList)

    # Styles
    styles: StyleList = field(default_factory=StyleList)

    # Memo shapes
    memo_shapes: List[MemoShape] = field(default_factory=list)

    # Track changes
    track_changes: List[TrackChange] = field(default_factory=list)
    track_change_authors: List[TrackChangeAuthor] = field(default_factory=list)

    # Compatibility
    compatible_document: Optional[CompatibleDocument] = None
    layout_compatibility: Optional[LayoutCompatibility] = None
