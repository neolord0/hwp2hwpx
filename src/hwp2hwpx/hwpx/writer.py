"""HWPX file writer."""

import zipfile
from pathlib import Path
from typing import Union

from hwp2hwpx.hwpx.builder.container import build_container_xml
from hwp2hwpx.hwpx.builder.content_hpf import build_content_hpf
from hwp2hwpx.hwpx.builder.manifest import build_manifest_xml
from hwp2hwpx.hwpx.builder.version import build_version_xml
from hwp2hwpx.hwpx.models.file import HWPXFile


# HWPX mimetype
HWPX_MIMETYPE = "application/hwp+zip"


class HWPXWriteError(Exception):
    """Exception raised when HWPX file writing fails."""

    pass


class HWPXWriter:
    """HWPX file writer.

    Writes HWPXFile objects to disk in HWPX (ZIP-based) format.

    Usage:
        HWPXWriter.write(hwpx_file, "output.hwpx")
    """

    @staticmethod
    def write(hwpx: HWPXFile, filepath: Union[str, Path]) -> None:
        """Write HWPX file to disk.

        Args:
            hwpx: HWPXFile object to write
            filepath: Output file path

        Raises:
            HWPXWriteError: If file cannot be written
        """
        filepath = Path(filepath)

        try:
            with zipfile.ZipFile(filepath, "w", zipfile.ZIP_DEFLATED) as zf:
                # mimetype (must be first and uncompressed)
                zf.writestr(
                    "mimetype",
                    HWPX_MIMETYPE,
                    compress_type=zipfile.ZIP_STORED,
                )

                # version.xml
                version_xml = build_version_xml(hwpx.version)
                zf.writestr("version.xml", version_xml)

                # META-INF/container.xml
                container_xml = build_container_xml(hwpx.container)
                zf.writestr("META-INF/container.xml", container_xml)

                # META-INF/manifest.xml
                manifest_xml = build_manifest_xml(hwpx.manifest)
                zf.writestr("META-INF/manifest.xml", manifest_xml)

                # Contents/content.hpf
                content_hpf = build_content_hpf(hwpx.content_hpf)
                zf.writestr("Contents/content.hpf", content_hpf)

                # Contents/header.xml
                if hwpx.header_xml:
                    zf.writestr("Contents/header.xml", hwpx.header_xml)

                # Contents/section*.xml
                for i, section_xml in enumerate(hwpx.section_xml_list):
                    zf.writestr(f"Contents/section{i}.xml", section_xml)

                # Contents/MasterPage/masterPage*.xml
                for i, master_page_xml in enumerate(hwpx.master_page_xml_list):
                    zf.writestr(
                        f"Contents/MasterPage/masterPage{i}.xml",
                        master_page_xml,
                    )

                # settings.xml (optional)
                if hwpx.settings_xml:
                    zf.writestr("settings.xml", hwpx.settings_xml)

                # BinData/*
                for name, data in hwpx.bin_data.items():
                    zf.writestr(f"BinData/{name}", data)

        except Exception as e:
            raise HWPXWriteError(f"Failed to write HWPX file: {e}") from e


def write_hwpx(hwpx: HWPXFile, filepath: Union[str, Path]) -> None:
    """Convenience function to write HWPX file.

    Args:
        hwpx: HWPXFile object to write
        filepath: Output file path
    """
    HWPXWriter.write(hwpx, filepath)
