"""HWP record parsing utilities."""

from dataclasses import dataclass
from typing import List, Tuple

from hwp2hwpx.util.binary import read_uint32


@dataclass
class RecordHeader:
    """HWP record header.

    Each record in HWP starts with a 4-byte header containing:
    - Tag ID (10 bits)
    - Level (10 bits)
    - Size (12 bits)

    If size == 0xFFF, the actual size is stored in the next 4 bytes.
    """

    tag_id: int
    level: int
    size: int


def parse_record_header(data: bytes, offset: int) -> Tuple[RecordHeader, int]:
    """Parse a single record header from data.

    Args:
        data: Raw bytes
        offset: Starting offset

    Returns:
        Tuple of (RecordHeader, next offset)
    """
    header_value, offset = read_uint32(data, offset)

    tag_id = header_value & 0x3FF  # 10 bits
    level = (header_value >> 10) & 0x3FF  # 10 bits
    size = (header_value >> 20) & 0xFFF  # 12 bits

    # Check for extended size
    if size == 0xFFF:
        size, offset = read_uint32(data, offset)

    return RecordHeader(tag_id=tag_id, level=level, size=size), offset


def parse_all_records(data: bytes) -> List[Tuple[RecordHeader, bytes]]:
    """Parse all records from data.

    Args:
        data: Raw bytes containing records

    Returns:
        List of (RecordHeader, record_data) tuples
    """
    records: List[Tuple[RecordHeader, bytes]] = []
    offset = 0

    while offset < len(data):
        header, new_offset = parse_record_header(data, offset)
        record_data = data[new_offset : new_offset + header.size]
        records.append((header, record_data))
        offset = new_offset + header.size

    return records


def parse_records_with_children(data: bytes) -> List[Tuple[RecordHeader, bytes, List]]:
    """Parse records maintaining parent-child relationship based on level.

    Args:
        data: Raw bytes containing records

    Returns:
        List of (RecordHeader, record_data, children) tuples
        where children is a list with the same structure
    """
    all_records = parse_all_records(data)
    return _build_record_tree(all_records)


def _build_record_tree(
    records: List[Tuple[RecordHeader, bytes]],
) -> List[Tuple[RecordHeader, bytes, List]]:
    """Build hierarchical record tree from flat list.

    Args:
        records: Flat list of (header, data) tuples

    Returns:
        Hierarchical list with children
    """
    if not records:
        return []

    result: List[Tuple[RecordHeader, bytes, List]] = []
    stack: List[Tuple[RecordHeader, bytes, List]] = []

    for header, data in records:
        node: Tuple[RecordHeader, bytes, List] = (header, data, [])

        # Pop items from stack until we find parent level
        while stack and stack[-1][0].level >= header.level:
            stack.pop()

        if stack:
            # Add as child of current parent
            stack[-1][2].append(node)
        else:
            # Top-level record
            result.append(node)

        # Push current node to stack for potential children
        stack.append(node)

    return result


@dataclass
class Record:
    """Parsed HWP record with data and children."""

    header: RecordHeader
    data: bytes
    children: List["Record"]

    @property
    def tag_id(self) -> int:
        """Get tag ID."""
        return self.header.tag_id

    @property
    def level(self) -> int:
        """Get level."""
        return self.header.level

    @property
    def size(self) -> int:
        """Get data size."""
        return self.header.size

    def get_child_by_tag(self, tag_id: int) -> "Record | None":
        """Get first child with specified tag ID."""
        for child in self.children:
            if child.tag_id == tag_id:
                return child
        return None

    def get_children_by_tag(self, tag_id: int) -> List["Record"]:
        """Get all children with specified tag ID."""
        return [child for child in self.children if child.tag_id == tag_id]


def parse_records_as_objects(data: bytes) -> List[Record]:
    """Parse records and return as Record objects.

    Args:
        data: Raw bytes containing records

    Returns:
        List of Record objects with hierarchical structure
    """
    tree = parse_records_with_children(data)
    return [_tuple_to_record(node) for node in tree]


def _tuple_to_record(node: Tuple[RecordHeader, bytes, List]) -> Record:
    """Convert tuple tree node to Record object."""
    header, data, children = node
    return Record(
        header=header,
        data=data,
        children=[_tuple_to_record(child) for child in children],
    )
