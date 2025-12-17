"""HWP file reader."""

from pathlib import Path
from typing import List, Union

import olefile

from hwp2hwpx.hwp.models.bin_data import BinData, EmbeddedBinaryData
from hwp2hwpx.hwp.models.file import HWPFile
from hwp2hwpx.hwp.models.file_header import FileHeader
from hwp2hwpx.hwp.parser.body_text import parse_body_text
from hwp2hwpx.hwp.parser.doc_info import parse_doc_info
from hwp2hwpx.util.compression import decompress_stream


class HWPReadError(Exception):
    """Exception raised when HWP file reading fails."""

    pass


class HWPReader:
    """HWP file reader.

    Reads HWP files (OLE2/CFB format) and parses them into HWPFile objects.

    Usage:
        hwp = HWPReader.read("document.hwp")
    """

    @staticmethod
    def read(filepath: Union[str, Path]) -> HWPFile:
        """Read and parse an HWP file.

        Args:
            filepath: Path to the HWP file

        Returns:
            Parsed HWPFile object

        Raises:
            HWPReadError: If file cannot be read or parsed
        """
        filepath = Path(filepath)

        if not filepath.exists():
            raise HWPReadError(f"File not found: {filepath}")

        try:
            ole = olefile.OleFileIO(str(filepath))
        except Exception as e:
            raise HWPReadError(f"Failed to open OLE file: {e}") from e

        try:
            hwp_file = HWPFile()

            # Read file header
            hwp_file.file_header = HWPReader._read_file_header(ole)

            # Validate file header
            is_valid, error_msg = hwp_file.file_header.validate()
            if not is_valid:
                raise HWPReadError(error_msg)

            # Read DocInfo
            hwp_file.doc_info = HWPReader._read_doc_info(ole, hwp_file.file_header)

            # Read BodyText (sections)
            hwp_file.body_text = HWPReader._read_body_text(ole, hwp_file.file_header)

            # Read BinData
            hwp_file.bin_data = HWPReader._read_bin_data(ole)

            return hwp_file

        finally:
            ole.close()

    @staticmethod
    def _read_file_header(ole: olefile.OleFileIO) -> FileHeader:
        """Read and parse file header stream."""
        if not ole.exists("FileHeader"):
            raise HWPReadError("FileHeader stream not found")

        data = ole.openstream("FileHeader").read()
        if len(data) < 256:
            raise HWPReadError("FileHeader too short")

        return FileHeader.from_bytes(data)

    @staticmethod
    def _read_doc_info(ole: olefile.OleFileIO, file_header: FileHeader):
        """Read and parse DocInfo stream."""
        if not ole.exists("DocInfo"):
            raise HWPReadError("DocInfo stream not found")

        data = ole.openstream("DocInfo").read()

        # Decompress if necessary
        if file_header.is_compressed:
            try:
                data = decompress_stream(data)
            except Exception as e:
                raise HWPReadError(f"Failed to decompress DocInfo: {e}") from e

        return parse_doc_info(data)

    @staticmethod
    def _read_body_text(ole: olefile.OleFileIO, file_header: FileHeader):
        """Read and parse BodyText section streams."""
        section_data_list: List[bytes] = []

        # Find all section streams (BodyText/Section0, BodyText/Section1, ...)
        section_index = 0
        while True:
            stream_name = f"BodyText/Section{section_index}"

            if not ole.exists(stream_name):
                break

            data = ole.openstream(stream_name).read()

            # Decompress if necessary
            if file_header.is_compressed:
                try:
                    data = decompress_stream(data)
                except Exception as e:
                    raise HWPReadError(f"Failed to decompress {stream_name}: {e}") from e

            section_data_list.append(data)
            section_index += 1

        if not section_data_list:
            raise HWPReadError("No BodyText sections found")

        return parse_body_text(section_data_list)

    @staticmethod
    def _read_bin_data(ole: olefile.OleFileIO) -> BinData:
        """Read binary data streams (embedded images, etc.)."""
        bin_data = BinData()

        # List all entries in BinData directory
        if ole.exists("BinData"):
            for entry in ole.listdir():
                if len(entry) >= 2 and entry[0] == "BinData":
                    name = entry[1]
                    stream_path = "/".join(entry)

                    try:
                        data = ole.openstream(stream_path).read()

                        # Try to decompress (some files may not be compressed)
                        try:
                            data = decompress_stream(data)
                        except Exception:
                            pass  # Use raw data if decompression fails

                        # Determine extension from name
                        extension = ""
                        if "." in name:
                            extension = name.split(".")[-1].lower()

                        embedded = EmbeddedBinaryData(
                            name=name,
                            data=data,
                            extension=extension,
                        )
                        bin_data.add_embedded(embedded)

                    except Exception:
                        pass  # Skip files that can't be read

        return bin_data


def read_hwp(filepath: Union[str, Path]) -> HWPFile:
    """Convenience function to read HWP file.

    Args:
        filepath: Path to the HWP file

    Returns:
        Parsed HWPFile object
    """
    return HWPReader.read(filepath)
