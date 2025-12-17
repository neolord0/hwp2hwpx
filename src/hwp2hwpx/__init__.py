"""HWP to HWPX converter in Python.

This package provides tools to convert HWP (Hangul Word Processor) files
to HWPX (Hangul Word Processor XML) format.

Usage:
    from hwp2hwpx import HWPReader, HWPXWriter, Hwp2HwpxConverter

    # Read HWP file
    hwp = HWPReader.read("document.hwp")

    # Convert to HWPX
    hwpx = Hwp2HwpxConverter.to_hwpx(hwp)

    # Write HWPX file
    HWPXWriter.write(hwpx, "document.hwpx")

    # Or use convenience function
    from hwp2hwpx import convert_file
    convert_file("document.hwp", "document.hwpx")
"""

__version__ = "0.1.0"

from hwp2hwpx.hwp.reader import HWPReader, HWPReadError
from hwp2hwpx.hwpx.writer import HWPXWriter, HWPXWriteError
from hwp2hwpx.transform.hwp2hwpx import Hwp2HwpxConverter, convert_file

__all__ = [
    "__version__",
    "HWPReader",
    "HWPReadError",
    "HWPXWriter",
    "HWPXWriteError",
    "Hwp2HwpxConverter",
    "convert_file",
]
