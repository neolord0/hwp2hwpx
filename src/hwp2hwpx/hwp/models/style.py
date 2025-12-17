"""Style models for HWP format."""

from dataclasses import dataclass, field
from typing import List


@dataclass
class Style:
    """Style definition.

    Styles combine paragraph shape and character shape settings
    with a name for easy reuse.
    """

    name: str = ""
    english_name: str = ""

    # Style type flags
    properties: int = 0

    # References to shapes
    para_shape_id: int = 0  # Index into ParaShapeList
    char_shape_id: int = 0  # Index into CharShapeList

    # Next style reference
    next_style_id: int = 0

    # Language ID
    lang_id: int = 0

    @property
    def is_para_style(self) -> bool:
        """Check if this is a paragraph style."""
        return (self.properties & 0x07) == 0

    @property
    def is_char_style(self) -> bool:
        """Check if this is a character style."""
        return (self.properties & 0x07) == 1

    @property
    def is_builtin(self) -> bool:
        """Check if this is a built-in style."""
        return bool(self.properties & 0x08)

    @property
    def lock_form(self) -> bool:
        """Check if form is locked."""
        return bool(self.properties & 0x10)


@dataclass
class StyleList:
    """Container for styles."""

    items: List[Style] = field(default_factory=list)

    def get(self, index: int) -> Style:
        """Get style by index."""
        if 0 <= index < len(self.items):
            return self.items[index]
        return Style()

    def add(self, style: Style) -> int:
        """Add style and return index."""
        self.items.append(style)
        return len(self.items) - 1

    def find_by_name(self, name: str) -> int:
        """Find style index by name. Returns -1 if not found."""
        for i, style in enumerate(self.items):
            if style.name == name:
                return i
        return -1
