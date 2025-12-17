"""Face name (font) models for HWP format."""

from dataclasses import dataclass, field
from typing import Optional


@dataclass
class FontSubstituteInfo:
    """Font substitution information."""

    font_type: int = 0  # 0: Unknown, 1: TrueType, 2: HWP font
    serif: int = 0  # 0: Unknown, 1: Serif, 2: Sans-serif
    weight: int = 0  # 1-9 (1=thin, 9=heavy)


@dataclass
class FontMetrics:
    """Font metrics information."""

    panose: bytes = field(default_factory=lambda: bytes(10))
    default_width: int = 0
    default_height: int = 0


@dataclass
class FaceName:
    """Face name (font family) definition.

    HWP supports 7 script types, and each can have different fonts:
    - Korean (한글)
    - English (영문)
    - Chinese (한자)
    - Japanese (일본어)
    - Other (기타)
    - Symbol (기호)
    - User (사용자)
    """

    name: str
    font_type: int = 0  # Font type flags
    substitute_info: Optional[FontSubstituteInfo] = None
    metrics: Optional[FontMetrics] = None
    default_font_name: str = ""
    script_type: int = 0  # Which script type this font is for

    @property
    def has_substitute_info(self) -> bool:
        """Check if font has substitute info."""
        return bool(self.font_type & 0x20)

    @property
    def has_metrics(self) -> bool:
        """Check if font has metrics info."""
        return bool(self.font_type & 0x40)

    @property
    def has_default_font(self) -> bool:
        """Check if font has default font name."""
        return bool(self.font_type & 0x80)


@dataclass
class FaceNameList:
    """Container for all face names organized by script type."""

    korean: list[FaceName] = field(default_factory=list)
    english: list[FaceName] = field(default_factory=list)
    chinese: list[FaceName] = field(default_factory=list)
    japanese: list[FaceName] = field(default_factory=list)
    other: list[FaceName] = field(default_factory=list)
    symbol: list[FaceName] = field(default_factory=list)
    user: list[FaceName] = field(default_factory=list)

    def get_by_script_type(self, script_type: int) -> list[FaceName]:
        """Get face names for specified script type."""
        mapping = {
            0: self.korean,
            1: self.english,
            2: self.chinese,
            3: self.japanese,
            4: self.other,
            5: self.symbol,
            6: self.user,
        }
        return mapping.get(script_type, [])

    def add(self, face_name: FaceName, script_type: int) -> None:
        """Add face name to specified script type list."""
        face_name.script_type = script_type
        self.get_by_script_type(script_type).append(face_name)

    def get_font_name(self, script_type: int, index: int) -> str:
        """Get font name by script type and index."""
        font_list = self.get_by_script_type(script_type)
        if 0 <= index < len(font_list):
            return font_list[index].name
        return ""

    def all_fonts(self) -> list[FaceName]:
        """Get all fonts across all script types."""
        return (
            self.korean
            + self.english
            + self.chinese
            + self.japanese
            + self.other
            + self.symbol
            + self.user
        )
