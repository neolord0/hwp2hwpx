"""HWP file model."""

from dataclasses import dataclass, field
from typing import Optional

from hwp2hwpx.hwp.models.bin_data import BinData
from hwp2hwpx.hwp.models.body_text import BodyText
from hwp2hwpx.hwp.models.doc_info import DocInfo
from hwp2hwpx.hwp.models.file_header import FileHeader


@dataclass
class SummaryInfo:
    """Document summary information.

    Contains metadata about the document like title, author, etc.
    """

    title: str = ""
    subject: str = ""
    author: str = ""
    date: str = ""
    keywords: str = ""
    comments: str = ""
    revision_number: int = 0
    app_name: str = ""
    security: int = 0


@dataclass
class HWPFile:
    """Complete HWP file representation.

    This is the top-level container for all HWP file data:
    - File header with version and property flags
    - Document info with shared resources (fonts, styles, etc.)
    - Body text with sections and paragraphs
    - Binary data (embedded images, OLE objects, etc.)
    - Optional summary information
    """

    file_header: FileHeader = field(default_factory=FileHeader)
    doc_info: DocInfo = field(default_factory=DocInfo)
    body_text: BodyText = field(default_factory=BodyText)
    bin_data: BinData = field(default_factory=BinData)
    summary_info: Optional[SummaryInfo] = None

    @property
    def version(self) -> str:
        """Get file version string."""
        return str(self.file_header.version)

    @property
    def is_compressed(self) -> bool:
        """Check if file data is compressed."""
        return self.file_header.is_compressed

    @property
    def section_count(self) -> int:
        """Get number of sections."""
        return self.body_text.section_count

    @property
    def has_bin_data(self) -> bool:
        """Check if file has embedded binary data."""
        return len(self.bin_data.embedded_list) > 0 or len(self.bin_data.items) > 0
