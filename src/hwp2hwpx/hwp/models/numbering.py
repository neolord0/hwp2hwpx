"""Numbering models for HWP format."""

from dataclasses import dataclass, field
from typing import List

from hwp2hwpx.hwp.enums.para_shape import ParagraphNumberFormat, ValueType


@dataclass
class NumberingLevel:
    """Single level in numbering definition."""

    # Number format
    number_format: ParagraphNumberFormat = ParagraphNumberFormat.NUMBER

    # Format string (e.g., "^1.", "^1)", "(^1)")
    format_string: str = ""

    # Paragraph properties
    para_shape_id: int = 0  # Reference to paragraph shape
    char_shape_id: int = 0  # Reference to character shape

    # Start number
    start_number: int = 1

    # Width settings
    width_adjust_type: ValueType = ValueType.VALUE
    width_adjust: int = 0

    # Text offset
    text_offset_type: ValueType = ValueType.VALUE
    text_offset: int = 0

    # Alignment and correction
    alignment: int = 0
    use_instance_number: bool = False
    correct_width: int = 0


@dataclass
class Numbering:
    """Numbering definition with up to 7 levels.

    Numbering is used for:
    - Outline numbering (heading numbers)
    - List numbering
    """

    # Levels (up to 7)
    levels: List[NumberingLevel] = field(default_factory=list)

    # Start numbers for each level
    start_numbers: List[int] = field(default_factory=lambda: [1] * 7)

    def get_level(self, level: int) -> NumberingLevel:
        """Get numbering level (0-based)."""
        if 0 <= level < len(self.levels):
            return self.levels[level]
        return NumberingLevel()


@dataclass
class NumberingList:
    """Container for numbering definitions."""

    items: List[Numbering] = field(default_factory=list)

    def get(self, index: int) -> Numbering:
        """Get numbering by index (1-based in HWP)."""
        if 1 <= index <= len(self.items):
            return self.items[index - 1]
        return Numbering()

    def add(self, numbering: Numbering) -> int:
        """Add numbering and return 1-based index."""
        self.items.append(numbering)
        return len(self.items)
