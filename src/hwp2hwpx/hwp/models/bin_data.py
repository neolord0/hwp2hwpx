"""Binary data models for HWP format."""

from dataclasses import dataclass, field
from enum import Enum
from typing import Optional


class BinDataType(Enum):
    """Binary data type enumeration."""

    LINK = 0  # Linked external file
    EMBEDDING = 1  # Embedded binary data
    STORAGE = 2  # OLE storage


class BinDataCompress(Enum):
    """Binary data compression type."""

    DEFAULT = 0  # Use default compression
    COMPRESS = 1  # Always compress
    NO_COMPRESS = 2  # Never compress


class BinDataState(Enum):
    """Binary data access state."""

    NOT_ACCESSED = 0
    ACCESS_SUCCESS = 1
    ACCESS_FAILED = 2
    LINK_ACCESS_FAILED_BUT_HAS_STORAGE = 3


@dataclass
class BinDataItem:
    """Single binary data item metadata from DocInfo."""

    bin_data_id: int
    data_type: BinDataType
    compress_mode: BinDataCompress
    state: BinDataState
    absolute_path: str = ""
    relative_path: str = ""
    extension: str = ""

    def get_storage_name(self) -> str:
        """Get storage stream name for embedded data."""
        return f"BIN{self.bin_data_id:04X}.{self.extension}"


@dataclass
class EmbeddedBinaryData:
    """Embedded binary data with actual content."""

    name: str
    data: bytes
    extension: str = ""

    @property
    def bin_data_id(self) -> int:
        """Extract bin data ID from name (e.g., 'BIN0001.png' -> 1)."""
        try:
            name_part = self.name.split(".")[0]
            if name_part.upper().startswith("BIN"):
                return int(name_part[3:], 16)
        except (ValueError, IndexError):
            pass
        return 0


@dataclass
class BinData:
    """Container for all binary data in HWP file."""

    items: list[BinDataItem] = field(default_factory=list)
    embedded_list: list[EmbeddedBinaryData] = field(default_factory=list)

    def get_item_by_id(self, bin_data_id: int) -> Optional[BinDataItem]:
        """Get bin data item by ID."""
        for item in self.items:
            if item.bin_data_id == bin_data_id:
                return item
        return None

    def get_embedded_by_id(self, bin_data_id: int) -> Optional[EmbeddedBinaryData]:
        """Get embedded binary data by ID."""
        for embedded in self.embedded_list:
            if embedded.bin_data_id == bin_data_id:
                return embedded
        return None

    def add_item(self, item: BinDataItem) -> None:
        """Add bin data item."""
        self.items.append(item)

    def add_embedded(self, embedded: EmbeddedBinaryData) -> None:
        """Add embedded binary data."""
        self.embedded_list.append(embedded)
