"""Character shape models for HWP format."""

from dataclasses import dataclass, field
from typing import List

from hwp2hwpx.hwp.enums.border_fill import BorderType2
from hwp2hwpx.hwp.enums.char_shape import (
    EmphasisType,
    OutlineType,
    ShadowType,
    StrikeoutType,
    UnderlineType,
)
from hwp2hwpx.util.color import Color4Byte


@dataclass
class CharShapeFontRef:
    """Font reference for each script type."""

    korean: int = 0
    english: int = 0
    chinese: int = 0
    japanese: int = 0
    other: int = 0
    symbol: int = 0
    user: int = 0

    def get_by_index(self, index: int) -> int:
        """Get font reference by script type index."""
        refs = [
            self.korean,
            self.english,
            self.chinese,
            self.japanese,
            self.other,
            self.symbol,
            self.user,
        ]
        if 0 <= index < len(refs):
            return refs[index]
        return 0


@dataclass
class CharShapeRatio:
    """Font size ratio for each script type (percentage)."""

    korean: int = 100
    english: int = 100
    chinese: int = 100
    japanese: int = 100
    other: int = 100
    symbol: int = 100
    user: int = 100


@dataclass
class CharShapeSpacing:
    """Character spacing for each script type."""

    korean: int = 0
    english: int = 0
    chinese: int = 0
    japanese: int = 0
    other: int = 0
    symbol: int = 0
    user: int = 0


@dataclass
class CharShapeRelSize:
    """Relative size for each script type (percentage)."""

    korean: int = 100
    english: int = 100
    chinese: int = 100
    japanese: int = 100
    other: int = 100
    symbol: int = 100
    user: int = 100


@dataclass
class CharShapeOffset:
    """Character offset for each script type."""

    korean: int = 0
    english: int = 0
    chinese: int = 0
    japanese: int = 0
    other: int = 0
    symbol: int = 0
    user: int = 0


@dataclass
class CharShape:
    """Character shape definition.

    Defines visual properties for text rendering:
    - Font references for each script type
    - Font size and ratios
    - Character spacing
    - Styling (bold, italic, underline, etc.)
    - Colors
    """

    # Font references (index into FaceNameList for each script type)
    font_refs: CharShapeFontRef = field(default_factory=CharShapeFontRef)

    # Font size ratios (percentage)
    ratios: CharShapeRatio = field(default_factory=CharShapeRatio)

    # Character spacing
    spacings: CharShapeSpacing = field(default_factory=CharShapeSpacing)

    # Relative sizes (percentage)
    rel_sizes: CharShapeRelSize = field(default_factory=CharShapeRelSize)

    # Character offsets
    offsets: CharShapeOffset = field(default_factory=CharShapeOffset)

    # Base font size in HWPUnit (1/7200 inch)
    base_size: int = 1000  # 10pt default

    # Properties packed in bit flags
    properties: int = 0

    # Shadow offset
    shadow_offset_x: int = 0
    shadow_offset_y: int = 0

    # Colors
    text_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    underline_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    shade_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    shadow_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))

    # Border character (for bordered text)
    border_fill_id: int = 0

    # Strike out color (version 5.0.2.1+)
    strikeout_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))

    @property
    def is_italic(self) -> bool:
        """Check if italic."""
        return bool(self.properties & 0x01)

    @property
    def is_bold(self) -> bool:
        """Check if bold."""
        return bool(self.properties & 0x02)

    @property
    def underline_type(self) -> UnderlineType:
        """Get underline type."""
        value = (self.properties >> 2) & 0x03
        return UnderlineType(value)

    @property
    def underline_shape(self) -> BorderType2:
        """Get underline shape."""
        value = (self.properties >> 4) & 0x0F
        try:
            return BorderType2(value)
        except ValueError:
            return BorderType2.SOLID

    @property
    def outline_type(self) -> OutlineType:
        """Get outline type."""
        value = (self.properties >> 8) & 0x07
        return OutlineType(value)

    @property
    def shadow_type(self) -> ShadowType:
        """Get shadow type."""
        value = (self.properties >> 11) & 0x03
        return ShadowType(value)

    @property
    def is_emboss(self) -> bool:
        """Check if emboss effect."""
        return bool(self.properties & 0x2000)

    @property
    def is_engrave(self) -> bool:
        """Check if engrave effect."""
        return bool(self.properties & 0x4000)

    @property
    def is_superscript(self) -> bool:
        """Check if superscript."""
        return bool(self.properties & 0x8000)

    @property
    def is_subscript(self) -> bool:
        """Check if subscript."""
        return bool(self.properties & 0x10000)

    @property
    def strikeout_type(self) -> StrikeoutType:
        """Get strikeout type."""
        value = (self.properties >> 18) & 0x07
        return StrikeoutType(value) if value <= 1 else StrikeoutType.NONE

    @property
    def emphasis_type(self) -> EmphasisType:
        """Get emphasis type."""
        value = (self.properties >> 21) & 0x0F
        try:
            return EmphasisType(value)
        except ValueError:
            return EmphasisType.NONE

    @property
    def use_kerning(self) -> bool:
        """Check if kerning is used."""
        return bool(self.properties & 0x4000000)

    @property
    def font_size_pt(self) -> float:
        """Get font size in points."""
        return self.base_size / 100.0


@dataclass
class CharShapeList:
    """Container for character shapes."""

    items: List[CharShape] = field(default_factory=list)

    def get(self, index: int) -> CharShape:
        """Get character shape by index."""
        if 0 <= index < len(self.items):
            return self.items[index]
        return CharShape()

    def add(self, char_shape: CharShape) -> int:
        """Add character shape and return index."""
        self.items.append(char_shape)
        return len(self.items) - 1
