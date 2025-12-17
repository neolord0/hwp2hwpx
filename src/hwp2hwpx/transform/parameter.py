"""Transformation context/parameter for HWP to HWPX conversion."""

from dataclasses import dataclass, field
from typing import Dict, List, Optional

from hwp2hwpx.hwp.models.file import HWPFile
from hwp2hwpx.hwpx.models.file import HWPXFile


@dataclass
class MasterPageInfo:
    """Master page information during conversion."""

    master_page_id: int = 0
    instance_id: int = 0


@dataclass
class FieldStackItem:
    """Field stack item for nested field tracking."""

    field_type: str = ""
    field_id: int = 0


@dataclass
class Parameter:
    """Conversion context/parameter.

    Holds state and references during HWP to HWPX conversion.
    """

    # Source and target
    hwp: HWPFile = field(default_factory=HWPFile)
    hwpx: HWPXFile = field(default_factory=HWPXFile)

    # Mapping tables
    master_page_map: Dict[int, MasterPageInfo] = field(default_factory=dict)
    bin_data_map: Dict[int, str] = field(default_factory=dict)

    # Field tracking
    field_stack: List[FieldStackItem] = field(default_factory=list)

    # Current state
    current_section_index: int = 0
    current_paragraph_index: int = 0

    # Statistics
    converted_controls: int = 0
    skipped_controls: int = 0

    def get_bin_data_name(self, bin_data_id: int) -> Optional[str]:
        """Get binary data filename by ID."""
        return self.bin_data_map.get(bin_data_id)

    def register_bin_data(self, bin_data_id: int, filename: str) -> None:
        """Register binary data mapping."""
        self.bin_data_map[bin_data_id] = filename

    def push_field(self, field_type: str, field_id: int) -> None:
        """Push field onto stack."""
        self.field_stack.append(FieldStackItem(field_type=field_type, field_id=field_id))

    def pop_field(self) -> Optional[FieldStackItem]:
        """Pop field from stack."""
        if self.field_stack:
            return self.field_stack.pop()
        return None

    def current_field(self) -> Optional[FieldStackItem]:
        """Get current field from stack."""
        if self.field_stack:
            return self.field_stack[-1]
        return None
