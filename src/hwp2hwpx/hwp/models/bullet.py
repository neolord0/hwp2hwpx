"""Bullet models for HWP format."""

from dataclasses import dataclass, field
from typing import List


@dataclass
class Bullet:
    """Bullet definition.

    Defines bullet character settings for paragraph lists.
    """

    # Bullet character
    bullet_char: str = ""

    # Use image as bullet
    use_image: bool = False

    # Image bullet settings
    image_bin_id: int = 0  # Reference to BinData
    image_bright: int = 0
    image_contrast: int = 0

    # Check bullet (checkbox style)
    check_char: str = ""

    # Character shape reference
    char_shape_id: int = 0

    # Width settings
    width_adjust: int = 0

    # Text offset
    text_offset: int = 0

    # Alignment
    alignment: int = 0


@dataclass
class BulletList:
    """Container for bullet definitions."""

    items: List[Bullet] = field(default_factory=list)

    def get(self, index: int) -> Bullet:
        """Get bullet by index (1-based in HWP)."""
        if 1 <= index <= len(self.items):
            return self.items[index - 1]
        return Bullet()

    def add(self, bullet: Bullet) -> int:
        """Add bullet and return 1-based index."""
        self.items.append(bullet)
        return len(self.items)
