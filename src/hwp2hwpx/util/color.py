"""Color utilities for HWP file format."""

from dataclasses import dataclass
from typing import Tuple

# Constant for "none" color value
NONE_COLOR_VALUE = 0xFFFFFFFF


@dataclass
class Color4Byte:
    """4-byte color representation (ARGB or BGRX format).

    HWP stores colors as 32-bit values in BGR format with the high byte unused or alpha.
    """

    value: int

    @property
    def r(self) -> int:
        """Red component (0-255)."""
        return self.value & 0xFF

    @property
    def g(self) -> int:
        """Green component (0-255)."""
        return (self.value >> 8) & 0xFF

    @property
    def b(self) -> int:
        """Blue component (0-255)."""
        return (self.value >> 16) & 0xFF

    @property
    def a(self) -> int:
        """Alpha component (0-255)."""
        return (self.value >> 24) & 0xFF

    def to_hex(self) -> str:
        """Convert to hex color string (#RRGGBB format)."""
        return f"#{self.r:02X}{self.g:02X}{self.b:02X}"

    def to_hex_with_alpha(self) -> str:
        """Convert to hex color string with alpha (#AARRGGBB format)."""
        return f"#{self.a:02X}{self.r:02X}{self.g:02X}{self.b:02X}"

    def is_none(self) -> bool:
        """Check if this is a 'none' color value."""
        return self.value == NONE_COLOR_VALUE

    def to_rgb_tuple(self) -> Tuple[int, int, int]:
        """Convert to RGB tuple."""
        return (self.r, self.g, self.b)

    def to_rgba_tuple(self) -> Tuple[int, int, int, int]:
        """Convert to RGBA tuple."""
        return (self.r, self.g, self.b, self.a)

    @classmethod
    def from_rgb(cls, r: int, g: int, b: int, a: int = 0) -> "Color4Byte":
        """Create Color4Byte from RGB values."""
        value = (a << 24) | (b << 16) | (g << 8) | r
        return cls(value)

    @classmethod
    def from_hex(cls, hex_str: str) -> "Color4Byte":
        """Create Color4Byte from hex string (#RRGGBB or #AARRGGBB)."""
        hex_str = hex_str.lstrip("#")
        if len(hex_str) == 6:
            r = int(hex_str[0:2], 16)
            g = int(hex_str[2:4], 16)
            b = int(hex_str[4:6], 16)
            return cls.from_rgb(r, g, b)
        elif len(hex_str) == 8:
            a = int(hex_str[0:2], 16)
            r = int(hex_str[2:4], 16)
            g = int(hex_str[4:6], 16)
            b = int(hex_str[6:8], 16)
            return cls.from_rgb(r, g, b, a)
        else:
            raise ValueError(f"Invalid hex color string: {hex_str}")

    @classmethod
    def none(cls) -> "Color4Byte":
        """Create a 'none' color value."""
        return cls(NONE_COLOR_VALUE)


def color_to_hex(value: int) -> str:
    """Convert 32-bit color value to hex string.

    Args:
        value: 32-bit color value in BGR format

    Returns:
        Hex color string (#RRGGBB) or 'none' if special value
    """
    if value == NONE_COLOR_VALUE:
        return "none"

    r = value & 0xFF
    g = (value >> 8) & 0xFF
    b = (value >> 16) & 0xFF
    return f"#{r:02X}{g:02X}{b:02X}"


def color_to_hex_with_none_check(value: int, none_value: int) -> str:
    """Convert 32-bit color value to hex string with custom none value.

    Args:
        value: 32-bit color value in BGR format
        none_value: Value that represents 'none'

    Returns:
        Hex color string (#RRGGBB) or 'none' if matches none_value
    """
    if value == none_value:
        return "none"
    return color_to_hex(value)


def parse_color_string(color_str: str) -> str:
    """Parse color from numeric string to hex format.

    HWP sometimes stores colors as decimal strings.

    Args:
        color_str: Decimal color string

    Returns:
        Hex color string (#RRGGBB)
    """
    long_color = int(color_str)
    # Note: HWP stores as RGB, so we swap to get proper order
    red = (long_color >> 16) & 0xFF
    green = (long_color >> 8) & 0xFF
    blue = long_color & 0xFF
    return f"#{blue:02X}{green:02X}{red:02X}"


def hwp_color_to_string(color: Color4Byte) -> str:
    """Convert HWP Color4Byte to hex string or 'none'.

    Args:
        color: Color4Byte instance

    Returns:
        Hex color string (#RRGGBB) or 'none'
    """
    if color.is_none():
        return "none"
    return color.to_hex()
