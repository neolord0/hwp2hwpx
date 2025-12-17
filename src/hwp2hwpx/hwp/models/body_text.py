"""Body text models for HWP format."""

from dataclasses import dataclass, field
from typing import List, Optional

from hwp2hwpx.hwp.models.control import SectionDefine
from hwp2hwpx.hwp.models.paragraph import Paragraph


@dataclass
class Section:
    """Section in HWP document body.

    A section defines page layout properties and contains paragraphs.
    Each section can have different:
    - Page size and orientation
    - Margins
    - Columns
    - Headers/footers
    """

    paragraphs: List[Paragraph] = field(default_factory=list)
    section_define: Optional[SectionDefine] = None

    @property
    def first_paragraph(self) -> Optional[Paragraph]:
        """Get first paragraph in section."""
        if self.paragraphs:
            return self.paragraphs[0]
        return None

    @property
    def last_paragraph(self) -> Optional[Paragraph]:
        """Get last paragraph in section."""
        if self.paragraphs:
            return self.paragraphs[-1]
        return None

    @property
    def paragraph_count(self) -> int:
        """Get number of paragraphs."""
        return len(self.paragraphs)


@dataclass
class BodyText:
    """Body text container.

    Contains all sections that make up the document body.
    """

    sections: List[Section] = field(default_factory=list)

    @property
    def section_count(self) -> int:
        """Get number of sections."""
        return len(self.sections)

    def get_section(self, index: int) -> Optional[Section]:
        """Get section by index."""
        if 0 <= index < len(self.sections):
            return self.sections[index]
        return None

    @property
    def all_paragraphs(self) -> List[Paragraph]:
        """Get all paragraphs across all sections."""
        result = []
        for section in self.sections:
            result.extend(section.paragraphs)
        return result

    @property
    def total_paragraph_count(self) -> int:
        """Get total number of paragraphs."""
        return sum(section.paragraph_count for section in self.sections)
