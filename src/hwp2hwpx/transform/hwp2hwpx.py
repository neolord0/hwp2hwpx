"""Main HWP to HWPX converter."""

from pathlib import Path
from typing import Union

from hwp2hwpx.hwp.models.file import HWPFile
from hwp2hwpx.hwp.reader import HWPReader
from hwp2hwpx.hwpx.models.file import (
    ContainerXML,
    ContentHPF,
    HWPXFile,
    ManifestXML,
    VersionXML,
)
from hwp2hwpx.hwpx.writer import HWPXWriter
from hwp2hwpx.transform.header.header_xml import HeaderXMLConverter
from hwp2hwpx.transform.parameter import Parameter
from hwp2hwpx.transform.section.section_xml import SectionXMLConverter


class Hwp2HwpxConverter:
    """Main converter for HWP to HWPX conversion.

    Orchestrates the entire conversion process from HWP to HWPX format.

    Usage:
        hwpx = Hwp2HwpxConverter.to_hwpx(hwp_file)
        # or
        converter = Hwp2HwpxConverter(hwp_file)
        hwpx = converter.convert()
    """

    def __init__(self, hwp: HWPFile) -> None:
        """Initialize converter with HWP file.

        Args:
            hwp: Source HWP file object
        """
        self.hwp = hwp
        self.hwpx = HWPXFile()
        self.param = Parameter(hwp=hwp, hwpx=self.hwpx)

    def convert(self) -> HWPXFile:
        """Perform the conversion.

        Returns:
            Converted HWPXFile object
        """
        # Initialize basic HWPX structure
        self._init_hwpx()

        # Convert header (DocInfo -> header.xml)
        self._convert_header()

        # Convert sections (BodyText -> section*.xml)
        self._convert_sections()

        # Process binary data
        self._process_bin_data()

        # Build content.hpf manifest
        self._build_content_hpf()

        # Build META-INF/manifest.xml
        self._build_manifest()

        return self.hwpx

    def _init_hwpx(self) -> None:
        """Initialize HWPX structure."""
        # Version info
        self.hwpx.version = VersionXML(
            version="1.4",
            app_name="HWP Document",
            app_version=str(self.hwp.file_header.version),
        )

        # Container
        self.hwpx.container = ContainerXML()

        # Initialize empty content HPF
        self.hwpx.content_hpf = ContentHPF()

        # Initialize manifest
        self.hwpx.manifest = ManifestXML()

    def _convert_header(self) -> None:
        """Convert DocInfo to header.xml."""
        converter = HeaderXMLConverter(self.param)
        self.hwpx.header_xml = converter.convert()

    def _convert_sections(self) -> None:
        """Convert BodyText sections to section XML files."""
        converter = SectionXMLConverter(self.param)

        for i, section in enumerate(self.hwp.body_text.sections):
            section_xml = converter.convert(section, i)
            self.hwpx.add_section_xml(section_xml)

    def _process_bin_data(self) -> None:
        """Process binary data (images, etc.)."""
        for embedded in self.hwp.bin_data.embedded_list:
            self.hwpx.add_bin_data(embedded.name, embedded.data)
            self.param.register_bin_data(embedded.bin_data_id, embedded.name)

    def _build_content_hpf(self) -> None:
        """Build content.hpf manifest."""
        content = self.hwpx.content_hpf

        # Add header.xml
        content.add_manifest_item("header", "header.xml", "application/xml")

        # Add section files
        for i in range(self.hwpx.section_count):
            content.add_manifest_item(
                f"section{i}",
                f"section{i}.xml",
                "application/xml",
            )
            content.add_spine_item(f"section{i}")

        # Add binary data files
        for name in self.hwpx.bin_data.keys():
            # Determine media type from extension
            ext = name.split(".")[-1].lower() if "." in name else ""
            media_type = self._get_media_type(ext)
            content.add_manifest_item(
                name.replace(".", "_"),
                f"../BinData/{name}",
                media_type,
            )

    def _build_manifest(self) -> None:
        """Build META-INF/manifest.xml."""
        manifest = self.hwpx.manifest

        # Root
        manifest.add_item("/", "application/hwp+zip")

        # Contents directory
        manifest.add_item("Contents/", "")

        # Header
        manifest.add_item("Contents/header.xml", "application/xml")

        # Sections
        for i in range(self.hwpx.section_count):
            manifest.add_item(f"Contents/section{i}.xml", "application/xml")

        # Binary data
        for name in self.hwpx.bin_data.keys():
            ext = name.split(".")[-1].lower() if "." in name else ""
            media_type = self._get_media_type(ext)
            manifest.add_item(f"BinData/{name}", media_type)

    def _get_media_type(self, extension: str) -> str:
        """Get MIME type from file extension."""
        type_map = {
            "emf": "image/emf",
            "gif": "image/gif",
            "jpg": "image/jpeg",
            "jpeg": "image/jpeg",
            "png": "image/png",
            "svg": "image/svg+xml",
            "tif": "image/tiff",
            "tiff": "image/tiff",
            "wmf": "image/wmf",
            "bmp": "image/bmp",
        }
        return type_map.get(extension, "application/octet-stream")

    @staticmethod
    def to_hwpx(hwp: HWPFile) -> HWPXFile:
        """Convert HWP file to HWPX format.

        Args:
            hwp: Source HWP file object

        Returns:
            Converted HWPXFile object
        """
        return Hwp2HwpxConverter(hwp).convert()


def convert_file(
    input_path: Union[str, Path],
    output_path: Union[str, Path],
) -> None:
    """Convert HWP file to HWPX file.

    Args:
        input_path: Path to input HWP file
        output_path: Path to output HWPX file
    """
    # Read HWP file
    hwp = HWPReader.read(input_path)

    # Convert to HWPX
    hwpx = Hwp2HwpxConverter.to_hwpx(hwp)

    # Write HWPX file
    HWPXWriter.write(hwpx, output_path)
