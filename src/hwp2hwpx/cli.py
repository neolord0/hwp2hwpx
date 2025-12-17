"""Command-line interface for HWP to HWPX converter."""

import argparse
import sys
from pathlib import Path

from hwp2hwpx import __version__
from hwp2hwpx.hwp.reader import HWPReader, HWPReadError
from hwp2hwpx.hwpx.writer import HWPXWriter, HWPXWriteError
from hwp2hwpx.transform.hwp2hwpx import Hwp2HwpxConverter


def main() -> int:
    """Main entry point for CLI.

    Returns:
        Exit code (0 for success, 1 for error)
    """
    parser = argparse.ArgumentParser(
        prog="hwp2hwpx",
        description="Convert HWP files to HWPX format",
    )

    parser.add_argument(
        "input",
        type=Path,
        help="Input HWP file path",
    )

    parser.add_argument(
        "output",
        type=Path,
        nargs="?",
        help="Output HWPX file path (default: input file with .hwpx extension)",
    )

    parser.add_argument(
        "-v", "--verbose",
        action="store_true",
        help="Enable verbose output",
    )

    parser.add_argument(
        "--version",
        action="version",
        version=f"%(prog)s {__version__}",
    )

    args = parser.parse_args()

    # Validate input file
    input_path: Path = args.input
    if not input_path.exists():
        print(f"Error: Input file not found: {input_path}", file=sys.stderr)
        return 1

    if not input_path.is_file():
        print(f"Error: Input path is not a file: {input_path}", file=sys.stderr)
        return 1

    # Determine output path
    output_path: Path = args.output
    if output_path is None:
        output_path = input_path.with_suffix(".hwpx")

    # Perform conversion
    try:
        if args.verbose:
            print(f"Reading: {input_path}")

        hwp = HWPReader.read(input_path)

        if args.verbose:
            print(f"HWP version: {hwp.file_header.version}")
            print(f"Sections: {hwp.body_text.section_count}")
            print(f"Compressed: {hwp.file_header.is_compressed}")

        if args.verbose:
            print("Converting to HWPX...")

        hwpx = Hwp2HwpxConverter.to_hwpx(hwp)

        if args.verbose:
            print(f"Writing: {output_path}")

        HWPXWriter.write(hwpx, output_path)

        print(f"Converted: {input_path} -> {output_path}")
        return 0

    except HWPReadError as e:
        print(f"Error reading HWP file: {e}", file=sys.stderr)
        return 1

    except HWPXWriteError as e:
        print(f"Error writing HWPX file: {e}", file=sys.stderr)
        return 1

    except Exception as e:
        print(f"Unexpected error: {e}", file=sys.stderr)
        if args.verbose:
            import traceback
            traceback.print_exc()
        return 1


if __name__ == "__main__":
    sys.exit(main())
