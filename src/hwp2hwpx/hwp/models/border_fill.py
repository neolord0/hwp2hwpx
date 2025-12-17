"""Border and fill models for HWP format."""

from dataclasses import dataclass, field
from typing import List, Optional

from hwp2hwpx.hwp.enums.border_fill import (
    BorderThickness,
    BorderType,
    DiagonalType,
    FillType,
    GradientType,
    ImageFillType,
    PatternType,
)
from hwp2hwpx.util.color import Color4Byte


@dataclass
class BorderLine:
    """Single border line definition."""

    border_type: BorderType = BorderType.NONE
    thickness: BorderThickness = BorderThickness.MM0_1
    color: Color4Byte = field(default_factory=lambda: Color4Byte(0))


@dataclass
class Border:
    """Border definition with all four sides and diagonal."""

    left: BorderLine = field(default_factory=BorderLine)
    right: BorderLine = field(default_factory=BorderLine)
    top: BorderLine = field(default_factory=BorderLine)
    bottom: BorderLine = field(default_factory=BorderLine)
    diagonal: BorderLine = field(default_factory=BorderLine)
    diagonal_type: DiagonalType = DiagonalType.NONE


@dataclass
class PatternFill:
    """Pattern fill definition."""

    pattern_type: PatternType = PatternType.NONE
    back_color: Color4Byte = field(default_factory=lambda: Color4Byte(0xFFFFFFFF))
    pattern_color: Color4Byte = field(default_factory=lambda: Color4Byte(0))


@dataclass
class GradientColor:
    """Single color in gradient."""

    color: Color4Byte = field(default_factory=lambda: Color4Byte(0))
    position: int = 0  # Position in gradient (0-100)


@dataclass
class GradientFill:
    """Gradient fill definition."""

    gradient_type: GradientType = GradientType.LINEAR
    angle: int = 0  # Angle in degrees (0-360)
    center_x: int = 0  # Center X for radial/conical (percentage)
    center_y: int = 0  # Center Y for radial/conical (percentage)
    step: int = 50  # Step count
    step_center: int = 50  # Step center
    colors: List[GradientColor] = field(default_factory=list)
    blur: int = 0
    blur_center: int = 0


@dataclass
class ImageFill:
    """Image fill definition."""

    fill_type: ImageFillType = ImageFillType.TILE_ALL
    bin_item_id: int = 0  # Reference to BinData
    bright: int = 0  # Brightness (-100 to 100)
    contrast: int = 0  # Contrast (-100 to 100)
    effect: int = 0  # Effect type
    alpha: int = 0  # Alpha (0-255)

    # Scale
    scale_x: int = 0
    scale_y: int = 0

    # Offset
    offset_x: int = 0
    offset_y: int = 0


@dataclass
class FillInfo:
    """Fill information container."""

    fill_type: int = 0  # FillType bit flags
    pattern_fill: Optional[PatternFill] = None
    gradient_fill: Optional[GradientFill] = None
    image_fill: Optional[ImageFill] = None

    def has_pattern_fill(self) -> bool:
        """Check if has pattern fill."""
        return bool(self.fill_type & FillType.PATTERN_FILL)

    def has_gradient_fill(self) -> bool:
        """Check if has gradient fill."""
        return bool(self.fill_type & FillType.GRADIENT_FILL)

    def has_image_fill(self) -> bool:
        """Check if has image fill."""
        return bool(self.fill_type & FillType.IMAGE_FILL)

    def has_any_fill(self) -> bool:
        """Check if has any fill."""
        return self.has_pattern_fill() or self.has_gradient_fill() or self.has_image_fill()


@dataclass
class BorderFill:
    """Border and fill definition.

    Combined border and fill settings that can be applied to:
    - Paragraphs
    - Table cells
    - Shapes
    """

    # Properties
    properties: int = 0

    # Border settings
    border: Border = field(default_factory=Border)

    # Fill settings
    fill_info: FillInfo = field(default_factory=FillInfo)

    @property
    def is_3d(self) -> bool:
        """Check if 3D effect."""
        return bool(self.properties & 0x01)

    @property
    def is_shadow(self) -> bool:
        """Check if has shadow."""
        return bool(self.properties & 0x02)

    @property
    def slash_type(self) -> int:
        """Get slash diagonal type."""
        return (self.properties >> 2) & 0x07

    @property
    def backslash_type(self) -> int:
        """Get backslash diagonal type."""
        return (self.properties >> 5) & 0x07

    @property
    def broken_cell_separate(self) -> bool:
        """Check if broken cell is separate."""
        return bool(self.properties & 0x100)


@dataclass
class BorderFillList:
    """Container for border fills."""

    items: List[BorderFill] = field(default_factory=list)

    def get(self, index: int) -> BorderFill:
        """Get border fill by index (1-based in HWP)."""
        if 1 <= index <= len(self.items):
            return self.items[index - 1]
        return BorderFill()

    def add(self, border_fill: BorderFill) -> int:
        """Add border fill and return 1-based index."""
        self.items.append(border_fill)
        return len(self.items)
