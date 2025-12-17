"""Control models for HWP format."""

from dataclasses import dataclass, field
from typing import TYPE_CHECKING, List, Optional, Union

from hwp2hwpx.hwp.enums.control import (
    ControlType,
    GsoType,
    HeaderFooterApplyPage,
    HeightCriterion,
    HorzAlign,
    HorzRelTo,
    LineArrowShape,
    LineArrowSize,
    LineEndShape,
    LineType,
    NumberSort,
    TextFlowType,
    TextVerticalAlignment,
    VertAlign,
    VertRelTo,
    WidthCriterion,
)
from hwp2hwpx.util.color import Color4Byte

if TYPE_CHECKING:
    from hwp2hwpx.hwp.models.paragraph import Paragraph


@dataclass
class CtrlHeaderCommon:
    """Common control header properties."""

    ctrl_id: str = ""  # 4-byte control ID
    properties: int = 0
    vertical_offset: int = 0
    horizontal_offset: int = 0
    width: int = 0
    height: int = 0
    z_order: int = 0
    margin_left: int = 0
    margin_right: int = 0
    margin_top: int = 0
    margin_bottom: int = 0
    instance_id: int = 0
    page_offset: int = 0
    description: str = ""

    @property
    def text_flow_type(self) -> TextFlowType:
        """Get text flow type."""
        value = self.properties & 0x07
        try:
            return TextFlowType(value)
        except ValueError:
            return TextFlowType.SQUARE

    @property
    def text_side(self) -> int:
        """Get which side text flows (0=both, 1=left, 2=right, 3=largest)."""
        return (self.properties >> 3) & 0x03

    @property
    def horz_rel_to(self) -> HorzRelTo:
        """Get horizontal relative position."""
        value = (self.properties >> 5) & 0x03
        try:
            return HorzRelTo(value)
        except ValueError:
            return HorzRelTo.PAPER

    @property
    def horz_align(self) -> HorzAlign:
        """Get horizontal alignment."""
        value = (self.properties >> 8) & 0x07
        try:
            return HorzAlign(value)
        except ValueError:
            return HorzAlign.LEFT

    @property
    def vert_rel_to(self) -> VertRelTo:
        """Get vertical relative position."""
        value = (self.properties >> 11) & 0x03
        try:
            return VertRelTo(value)
        except ValueError:
            return VertRelTo.PAPER

    @property
    def vert_align(self) -> VertAlign:
        """Get vertical alignment."""
        value = (self.properties >> 13) & 0x07
        try:
            return VertAlign(value)
        except ValueError:
            return VertAlign.TOP

    @property
    def flow_with_text(self) -> bool:
        """Check if flows with text."""
        return bool(self.properties & 0x10000)

    @property
    def allow_overlap(self) -> bool:
        """Check if allows overlap."""
        return bool(self.properties & 0x20000)

    @property
    def width_criterion(self) -> WidthCriterion:
        """Get width criterion."""
        value = (self.properties >> 18) & 0x07
        try:
            return WidthCriterion(value)
        except ValueError:
            return WidthCriterion.ABSOLUTE

    @property
    def height_criterion(self) -> HeightCriterion:
        """Get height criterion."""
        value = (self.properties >> 21) & 0x03
        try:
            return HeightCriterion(value)
        except ValueError:
            return HeightCriterion.ABSOLUTE

    @property
    def protect_size(self) -> bool:
        """Check if size is protected."""
        return bool(self.properties & 0x800000)


@dataclass
class Control:
    """Base control class."""

    ctrl_id: str = ""
    header: Optional[CtrlHeaderCommon] = None

    @property
    def control_type(self) -> str:
        """Get control type string."""
        return self.ctrl_id


@dataclass
class ListHeaderAppend:
    """List header append data."""

    caption_list_id: int = 0
    caption_width: int = 0
    caption_spacing: int = 0
    caption_max_width: int = 0


@dataclass
class TextBox:
    """Text box properties for shapes containing text."""

    margin_left: int = 0
    margin_right: int = 0
    margin_top: int = 0
    margin_bottom: int = 0
    vert_align: TextVerticalAlignment = TextVerticalAlignment.TOP
    paragraphs: List["Paragraph"] = field(default_factory=list)


@dataclass
class ShapeComponent:
    """Shape component properties."""

    offset_x: int = 0
    offset_y: int = 0
    group_level: int = 0
    local_file_version: int = 0
    width: int = 0
    height: int = 0
    flip_horizontal: bool = False
    flip_vertical: bool = False
    angle: int = 0
    center_x: int = 0
    center_y: int = 0
    render_matrix: List[float] = field(default_factory=list)
    scale_matrix: List[float] = field(default_factory=list)
    rotate_matrix: List[float] = field(default_factory=list)
    instance_id: int = 0


@dataclass
class LineInfo:
    """Line drawing properties."""

    line_type: LineType = LineType.NONE
    line_width: int = 0
    line_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    outline_style: int = 0
    end_cap: LineEndShape = LineEndShape.FLAT
    start_arrow: LineArrowShape = LineArrowShape.NONE
    end_arrow: LineArrowShape = LineArrowShape.NONE
    start_arrow_size: LineArrowSize = LineArrowSize.SMALL_SMALL
    end_arrow_size: LineArrowSize = LineArrowSize.SMALL_SMALL
    start_arrow_fill: bool = False
    end_arrow_fill: bool = False


@dataclass
class FillInfoShort:
    """Short fill info for shapes."""

    fill_type: int = 0
    face_color: Color4Byte = field(default_factory=lambda: Color4Byte(0xFFFFFFFF))
    hatch_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    hatch_style: int = 0
    alpha: int = 0


@dataclass
class ShadowInfo:
    """Shadow properties for shapes."""

    shadow_type: int = 0
    color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    offset_x: int = 0
    offset_y: int = 0
    alpha: int = 0


@dataclass
class GsoControl(Control):
    """Graphic shape object control."""

    gso_type: GsoType = GsoType.RECTANGLE
    shape_component: Optional[ShapeComponent] = None
    line_info: Optional[LineInfo] = None
    fill_info: Optional[FillInfoShort] = None
    shadow_info: Optional[ShadowInfo] = None
    text_box: Optional[TextBox] = None
    caption: Optional[TextBox] = None


@dataclass
class LineShape(GsoControl):
    """Line shape."""

    start_x: int = 0
    start_y: int = 0
    end_x: int = 0
    end_y: int = 0
    is_reverse_horz: bool = False
    is_reverse_vert: bool = False


@dataclass
class RectangleShape(GsoControl):
    """Rectangle shape."""

    round_rate: int = 0
    points: List[tuple] = field(default_factory=list)


@dataclass
class EllipseShape(GsoControl):
    """Ellipse shape."""

    properties: int = 0
    center_x: int = 0
    center_y: int = 0
    axis1_x: int = 0
    axis1_y: int = 0
    axis2_x: int = 0
    axis2_y: int = 0
    start_x: int = 0
    start_y: int = 0
    end_x: int = 0
    end_y: int = 0
    start_x2: int = 0
    start_y2: int = 0
    end_x2: int = 0
    end_y2: int = 0


@dataclass
class ArcShape(GsoControl):
    """Arc shape."""

    arc_type: int = 0


@dataclass
class PolygonShape(GsoControl):
    """Polygon shape."""

    points: List[tuple] = field(default_factory=list)


@dataclass
class CurveShape(GsoControl):
    """Curve shape (Bezier)."""

    points: List[tuple] = field(default_factory=list)
    segment_types: List[int] = field(default_factory=list)


@dataclass
class PictureShape(GsoControl):
    """Picture/image shape."""

    border_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    border_thickness: int = 0
    border_properties: int = 0
    bright: int = 0
    contrast: int = 0
    effect: int = 0
    bin_item_id: int = 0
    border_transparency: int = 0
    instance_id: int = 0
    picture_effect: bytes = field(default_factory=bytes)


@dataclass
class OleShape(GsoControl):
    """OLE object shape."""

    properties: int = 0
    extent_x: int = 0
    extent_y: int = 0
    bin_item_id: int = 0
    border_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    border_thickness: int = 0
    border_properties: int = 0


@dataclass
class ContainerShape(GsoControl):
    """Container (group) shape."""

    children: List["GsoControl"] = field(default_factory=list)


@dataclass
class TextArtShape(GsoControl):
    """Text art shape."""

    text: str = ""
    font_name: str = ""
    font_style: str = ""
    font_type: int = 0
    outline_type: int = 0
    shadow_type: int = 0
    text_effect: bytes = field(default_factory=bytes)


@dataclass
class ConnectLineShape(GsoControl):
    """Connect line shape."""

    connect_type: int = 0
    start_subject_id: int = 0
    start_subject_index: int = 0
    end_subject_id: int = 0
    end_subject_index: int = 0


# Table-related controls are in table.py

@dataclass
class SectionDefine(Control):
    """Section definition control."""

    properties: int = 0
    column_gap: int = 0
    vert_rel: int = 0
    horz_rel: int = 0
    default_tab: int = 0
    numbering_shape: int = 0
    page_starting_number: int = 0
    picture_starting_number: int = 0
    table_starting_number: int = 0
    equation_starting_number: int = 0
    hide_header: bool = False
    hide_footer: bool = False
    hide_page_num: bool = False
    hide_border: bool = False
    hide_fill: bool = False
    hide_page_num_position: bool = False
    column_def: Optional["ColumnDefine"] = None
    page_def: Optional["PageDef"] = None
    footnote_shape: Optional["FootnoteShape"] = None
    endnote_shape: Optional["EndnoteShape"] = None
    page_border_fill: Optional["PageBorderFill"] = None
    master_page_info: Optional["MasterPageInfo"] = None


@dataclass
class ColumnDefine(Control):
    """Column definition."""

    properties: int = 0
    column_count: int = 1
    column_direction: int = 0
    same_width: bool = True
    gap: int = 0
    widths: List[int] = field(default_factory=list)
    border_type: int = 0
    border_thickness: int = 0
    border_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))


@dataclass
class PageDef:
    """Page definition."""

    paper_width: int = 59528  # A4 width
    paper_height: int = 84188  # A4 height
    margin_left: int = 8504
    margin_right: int = 8504
    margin_top: int = 5668
    margin_bottom: int = 4252
    header_margin: int = 4252
    footer_margin: int = 4252
    gutter_margin: int = 0
    properties: int = 0

    @property
    def landscape(self) -> bool:
        """Check if landscape orientation."""
        return bool(self.properties & 0x01)


@dataclass
class FootnoteShape:
    """Footnote shape definition."""

    properties: int = 0
    user_char: str = ""
    prefix_char: str = ""
    suffix_char: str = ""
    starting_number: int = 1
    divider_length: int = 0
    divider_margin_top: int = 0
    divider_margin_bottom: int = 0
    notes_spacing: int = 0
    line_type: int = 0
    line_width: int = 0
    line_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))


@dataclass
class EndnoteShape:
    """Endnote shape definition."""

    properties: int = 0
    user_char: str = ""
    prefix_char: str = ""
    suffix_char: str = ""
    starting_number: int = 1
    divider_length: int = 0
    divider_margin_top: int = 0
    divider_margin_bottom: int = 0
    notes_spacing: int = 0
    line_type: int = 0
    line_width: int = 0
    line_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))


@dataclass
class PageBorderFill:
    """Page border fill definition."""

    properties: int = 0
    offset_left: int = 0
    offset_right: int = 0
    offset_top: int = 0
    offset_bottom: int = 0
    border_fill_id: int = 0


@dataclass
class MasterPageInfo:
    """Master page information."""

    master_page_type: int = 0
    instance_id: int = 0


@dataclass
class HeaderControl(Control):
    """Header control."""

    properties: int = 0
    apply_page: HeaderFooterApplyPage = HeaderFooterApplyPage.BOTH_PAGE
    paragraphs: List["Paragraph"] = field(default_factory=list)


@dataclass
class FooterControl(Control):
    """Footer control."""

    properties: int = 0
    apply_page: HeaderFooterApplyPage = HeaderFooterApplyPage.BOTH_PAGE
    paragraphs: List["Paragraph"] = field(default_factory=list)


@dataclass
class FootnoteControl(Control):
    """Footnote control."""

    number: int = 0
    suffix: str = ""
    instance_id: int = 0
    paragraphs: List["Paragraph"] = field(default_factory=list)


@dataclass
class EndnoteControl(Control):
    """Endnote control."""

    number: int = 0
    suffix: str = ""
    instance_id: int = 0
    paragraphs: List["Paragraph"] = field(default_factory=list)


@dataclass
class AutoNumberControl(Control):
    """Auto number control."""

    number_sort: NumberSort = NumberSort.PAGE
    properties: int = 0
    custom_char: str = ""


@dataclass
class NewNumberControl(Control):
    """New number control."""

    number_sort: NumberSort = NumberSort.PAGE
    number: int = 0


@dataclass
class PageHidingControl(Control):
    """Page hiding control."""

    hide_header: bool = False
    hide_footer: bool = False
    hide_master_page: bool = False
    hide_page_border: bool = False
    hide_page_fill: bool = False
    hide_page_num: bool = False


@dataclass
class PageNumberPosition(Control):
    """Page number position control."""

    properties: int = 0
    user_char: str = ""
    prefix: str = ""
    suffix: str = ""
    start_page: int = 0


@dataclass
class BookmarkControl(Control):
    """Bookmark control."""

    bookmark_type: int = 0
    name: str = ""
    page_num: int = 0


@dataclass
class FieldBeginControl(Control):
    """Field begin control."""

    field_type: int = 0
    properties: int = 0
    extra_properties: int = 0
    command: str = ""
    field_id: int = 0
    z_order: int = 0


@dataclass
class OverlappingLetterControl(Control):
    """Overlapping letter control (compose)."""

    char_shapes: List[int] = field(default_factory=list)
    chars: str = ""


@dataclass
class DutmalControl(Control):
    """Dutmal (annotation) control."""

    main_text: str = ""
    annotation_text: str = ""
    position: int = 0
    face_name_id: int = 0
    face_name_id2: int = 0
    char_shape_id: int = 0
    char_shape_id2: int = 0


@dataclass
class HiddenCommentControl(Control):
    """Hidden comment control."""

    paragraphs: List["Paragraph"] = field(default_factory=list)


@dataclass
class EquationControl(Control):
    """Equation control."""

    script: str = ""
    char_shape_id: int = 0
    font_name: str = ""
    version: int = 0
    base_size: int = 1000
    text_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    line_mode: bool = False


# Type alias for all control types
AnyControl = Union[
    Control,
    GsoControl,
    LineShape,
    RectangleShape,
    EllipseShape,
    ArcShape,
    PolygonShape,
    CurveShape,
    PictureShape,
    OleShape,
    ContainerShape,
    TextArtShape,
    ConnectLineShape,
    SectionDefine,
    ColumnDefine,
    HeaderControl,
    FooterControl,
    FootnoteControl,
    EndnoteControl,
    AutoNumberControl,
    NewNumberControl,
    PageHidingControl,
    PageNumberPosition,
    BookmarkControl,
    FieldBeginControl,
    OverlappingLetterControl,
    DutmalControl,
    HiddenCommentControl,
    EquationControl,
]
