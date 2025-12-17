"""Table models for HWP format."""

from dataclasses import dataclass, field
from typing import TYPE_CHECKING, List, Optional

from hwp2hwpx.hwp.enums.control import TextVerticalAlignment
from hwp2hwpx.hwp.models.control import Control, CtrlHeaderCommon
from hwp2hwpx.util.color import Color4Byte

if TYPE_CHECKING:
    from hwp2hwpx.hwp.models.paragraph import Paragraph


@dataclass
class CellMargin:
    """Cell margin settings."""

    left: int = 0
    right: int = 0
    top: int = 0
    bottom: int = 0


@dataclass
class Cell:
    """Table cell."""

    # Cell position
    col: int = 0
    row: int = 0
    col_span: int = 1
    row_span: int = 1

    # Cell size
    width: int = 0
    height: int = 0

    # Cell properties
    margin: CellMargin = field(default_factory=CellMargin)
    border_fill_id: int = 0
    vert_align: TextVerticalAlignment = TextVerticalAlignment.TOP

    # Field properties
    is_header: bool = False
    has_margin: bool = False
    protect: bool = False
    edit_dirty: bool = False
    dirty: bool = False

    # Content
    paragraphs: List["Paragraph"] = field(default_factory=list)


@dataclass
class Row:
    """Table row."""

    cells: List[Cell] = field(default_factory=list)
    height: int = 0


@dataclass
class ZoneInfo:
    """Table zone information."""

    start_col: int = 0
    start_row: int = 0
    end_col: int = 0
    end_row: int = 0
    border_fill_id: int = 0


@dataclass
class TableControl(Control):
    """Table control.

    HWP tables are represented as a grid of cells with properties
    for borders, fills, and layout.
    """

    header: Optional[CtrlHeaderCommon] = None

    # Table dimensions
    row_count: int = 0
    col_count: int = 0

    # Table properties
    properties: int = 0

    # Cell margins (default)
    cell_margin: CellMargin = field(default_factory=CellMargin)

    # Cell spacing
    cell_spacing: int = 0

    # Border fill for table
    border_fill_id: int = 0

    # Row sizes (heights)
    row_sizes: List[int] = field(default_factory=list)

    # Zone info (merged cells, etc.)
    zones: List[ZoneInfo] = field(default_factory=list)

    # Caption
    caption_paragraphs: List["Paragraph"] = field(default_factory=list)

    # Rows and cells
    rows: List[Row] = field(default_factory=list)

    # Layout
    repeat_header: bool = False
    page_break: bool = False

    @property
    def cell_list(self) -> List[Cell]:
        """Get flat list of all cells."""
        cells = []
        for row in self.rows:
            cells.extend(row.cells)
        return cells

    def get_cell(self, row: int, col: int) -> Optional[Cell]:
        """Get cell at specified position."""
        if 0 <= row < len(self.rows):
            row_obj = self.rows[row]
            for cell in row_obj.cells:
                if cell.col == col:
                    return cell
        return None

    def get_column_widths(self) -> List[int]:
        """Get widths of each column."""
        widths: List[int] = [0] * self.col_count
        for row in self.rows:
            for cell in row.cells:
                if cell.col_span == 1 and cell.col < self.col_count:
                    widths[cell.col] = max(widths[cell.col], cell.width)
        return widths

    @property
    def split_page_cell(self) -> bool:
        """Check if cells can be split across pages."""
        return bool(self.properties & 0x01)

    @property
    def repeat_header_row(self) -> bool:
        """Check if header row repeats on each page."""
        return bool(self.properties & 0x02)
