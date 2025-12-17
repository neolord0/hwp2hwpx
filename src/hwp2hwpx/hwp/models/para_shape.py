"""Paragraph shape models for HWP format."""

from dataclasses import dataclass, field
from typing import List

from hwp2hwpx.hwp.enums.para_shape import (
    Alignment,
    BreakType,
    HeadingType,
    LineSpacingType,
    VerticalAlignment,
)


@dataclass
class ParaMargin:
    """Paragraph margin settings."""

    left: int = 0  # Left margin in HWPUnit
    right: int = 0  # Right margin in HWPUnit
    indent: int = 0  # First line indent (can be negative for hanging indent)
    prev: int = 0  # Space before paragraph
    next: int = 0  # Space after paragraph


@dataclass
class ParaShape:
    """Paragraph shape definition.

    Defines visual properties for paragraph formatting:
    - Alignment
    - Line spacing
    - Margins
    - Borders and fills
    - Numbering/bullets
    """

    # Properties packed in bit flags
    properties1: int = 0
    properties2: int = 0
    properties3: int = 0

    # Margins
    margin: ParaMargin = field(default_factory=ParaMargin)

    # Line spacing
    line_spacing: int = 160  # Line spacing value
    line_spacing_type: LineSpacingType = LineSpacingType.PERCENT

    # Tab definition reference
    tab_def_id: int = 0

    # Numbering/bullet reference
    numbering_id: int = 0
    bullet_id: int = 0

    # Border fill reference
    border_fill_id: int = 0

    # Horizontal offset for border
    border_offset_left: int = 0
    border_offset_right: int = 0
    border_offset_top: int = 0
    border_offset_bottom: int = 0

    # Additional properties (version specific)
    line_wrap: int = 0
    auto_spacing_para_english: int = 0
    auto_spacing_para_number: int = 0

    @property
    def alignment(self) -> Alignment:
        """Get paragraph alignment."""
        value = self.properties1 & 0x07
        try:
            return Alignment(value)
        except ValueError:
            return Alignment.JUSTIFY

    @property
    def vertical_alignment(self) -> VerticalAlignment:
        """Get vertical alignment."""
        value = (self.properties1 >> 3) & 0x03
        try:
            return VerticalAlignment(value)
        except ValueError:
            return VerticalAlignment.BY_FONT

    @property
    def heading_type(self) -> HeadingType:
        """Get heading type."""
        value = (self.properties1 >> 5) & 0x03
        try:
            return HeadingType(value)
        except ValueError:
            return HeadingType.NONE

    @property
    def heading_level(self) -> int:
        """Get heading level (0-6)."""
        return (self.properties1 >> 7) & 0x07

    @property
    def break_type_before(self) -> BreakType:
        """Get break type before paragraph."""
        value = (self.properties1 >> 10) & 0x03
        try:
            return BreakType(value)
        except ValueError:
            return BreakType.NONE

    @property
    def break_type_after(self) -> BreakType:
        """Get break type after paragraph."""
        value = (self.properties1 >> 12) & 0x03
        try:
            return BreakType(value)
        except ValueError:
            return BreakType.NONE

    @property
    def widow_orphan_control(self) -> bool:
        """Check if widow/orphan control is enabled."""
        return bool(self.properties1 & 0x4000)

    @property
    def keep_with_next(self) -> bool:
        """Check if paragraph keeps with next."""
        return bool(self.properties1 & 0x8000)

    @property
    def protect(self) -> bool:
        """Check if paragraph is protected."""
        return bool(self.properties1 & 0x10000)

    @property
    def page_break_before(self) -> bool:
        """Check if page break before paragraph."""
        return bool(self.properties1 & 0x20000)

    @property
    def has_border(self) -> bool:
        """Check if paragraph has border."""
        return self.border_fill_id > 0


@dataclass
class ParaShapeList:
    """Container for paragraph shapes."""

    items: List[ParaShape] = field(default_factory=list)

    def get(self, index: int) -> ParaShape:
        """Get paragraph shape by index."""
        if 0 <= index < len(self.items):
            return self.items[index]
        return ParaShape()

    def add(self, para_shape: ParaShape) -> int:
        """Add paragraph shape and return index."""
        self.items.append(para_shape)
        return len(self.items) - 1
