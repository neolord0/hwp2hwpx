"""Tab definition models for HWP format."""

from dataclasses import dataclass, field
from typing import List

from hwp2hwpx.hwp.enums.para_shape import TabLeader, TabType


@dataclass
class TabItem:
    """Single tab stop definition."""

    position: int = 0  # Position in HWPUnit
    tab_type: TabType = TabType.LEFT
    leader: TabLeader = TabLeader.NONE


@dataclass
class TabDef:
    """Tab definition with multiple tab stops.

    Defines tab stops for a paragraph.
    """

    # Properties
    properties: int = 0

    # Tab items
    items: List[TabItem] = field(default_factory=list)

    @property
    def auto_tab_left(self) -> bool:
        """Check if auto tab on left."""
        return bool(self.properties & 0x01)

    @property
    def auto_tab_right(self) -> bool:
        """Check if auto tab on right."""
        return bool(self.properties & 0x02)


@dataclass
class TabDefList:
    """Container for tab definitions."""

    items: List[TabDef] = field(default_factory=list)

    def get(self, index: int) -> TabDef:
        """Get tab definition by index."""
        if 0 <= index < len(self.items):
            return self.items[index]
        return TabDef()

    def add(self, tab_def: TabDef) -> int:
        """Add tab definition and return index."""
        self.items.append(tab_def)
        return len(self.items) - 1
