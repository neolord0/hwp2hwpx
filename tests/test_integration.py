"""Integration tests for HWP to HWPX conversion."""

import pytest
import tempfile
import zipfile
from pathlib import Path

from hwp2hwpx.hwp.reader import HWPReader, HWPReadError
from hwp2hwpx.hwpx.writer import HWPXWriter
from hwp2hwpx.transform.hwp2hwpx import Hwp2HwpxConverter, convert_file


class TestHWPReader:
    """HWP reader integration tests."""

    def test_read_nonexistent_file(self):
        """Test reading non-existent file raises error."""
        with pytest.raises(HWPReadError, match="not found"):
            HWPReader.read("nonexistent.hwp")

    def test_read_sample_file(self, test_dir: Path):
        """Test reading a sample HWP file if available."""
        # Find a sample file
        sample_file = None
        if test_dir.exists():
            for subdir in test_dir.iterdir():
                if subdir.is_dir():
                    hwp_file = subdir / "from.hwp"
                    if hwp_file.exists():
                        sample_file = hwp_file
                        break

        if sample_file is None:
            pytest.skip("No sample HWP file available")

        hwp = HWPReader.read(sample_file)
        assert hwp is not None
        assert hwp.file_header.signature.startswith("HWP Document")
        assert hwp.body_text.section_count > 0


class TestConversion:
    """Conversion integration tests."""

    def test_convert_sample_file(self, test_dir: Path):
        """Test converting a sample HWP file to HWPX."""
        # Find a sample file
        sample_file = None
        if test_dir.exists():
            for subdir in test_dir.iterdir():
                if subdir.is_dir():
                    hwp_file = subdir / "from.hwp"
                    if hwp_file.exists():
                        sample_file = hwp_file
                        break

        if sample_file is None:
            pytest.skip("No sample HWP file available")

        hwp = HWPReader.read(sample_file)
        hwpx = Hwp2HwpxConverter.to_hwpx(hwp)

        assert hwpx is not None
        assert hwpx.header_xml is not None
        assert len(hwpx.section_xml_list) > 0

    def test_write_hwpx_file(self, test_dir: Path):
        """Test writing HWPX file."""
        # Find a sample file
        sample_file = None
        if test_dir.exists():
            for subdir in test_dir.iterdir():
                if subdir.is_dir():
                    hwp_file = subdir / "from.hwp"
                    if hwp_file.exists():
                        sample_file = hwp_file
                        break

        if sample_file is None:
            pytest.skip("No sample HWP file available")

        hwp = HWPReader.read(sample_file)
        hwpx = Hwp2HwpxConverter.to_hwpx(hwp)

        # Write to temp file
        with tempfile.NamedTemporaryFile(suffix=".hwpx", delete=False) as f:
            output_path = Path(f.name)

        try:
            HWPXWriter.write(hwpx, output_path)
            assert output_path.exists()
            assert output_path.stat().st_size > 0

            # Verify it's a valid ZIP file
            assert zipfile.is_zipfile(output_path)

            # Check contents
            with zipfile.ZipFile(output_path, "r") as zf:
                names = zf.namelist()
                assert "mimetype" in names
                assert "version.xml" in names
                assert "Contents/header.xml" in names
                assert "Contents/section0.xml" in names

                # Check mimetype is first and uncompressed
                info = zf.getinfo("mimetype")
                assert info.compress_type == zipfile.ZIP_STORED
        finally:
            if output_path.exists():
                output_path.unlink()

    def test_convert_file_function(self, test_dir: Path):
        """Test convert_file convenience function."""
        # Find a sample file
        sample_file = None
        if test_dir.exists():
            for subdir in test_dir.iterdir():
                if subdir.is_dir():
                    hwp_file = subdir / "from.hwp"
                    if hwp_file.exists():
                        sample_file = hwp_file
                        break

        if sample_file is None:
            pytest.skip("No sample HWP file available")

        # Create temp output file
        with tempfile.NamedTemporaryFile(suffix=".hwpx", delete=False) as f:
            output_path = Path(f.name)

        try:
            convert_file(sample_file, output_path)
            assert output_path.exists()
            assert zipfile.is_zipfile(output_path)
        finally:
            if output_path.exists():
                output_path.unlink()


class TestHWPXStructure:
    """HWPX output structure tests."""

    def test_hwpx_xml_structure(self, test_dir: Path):
        """Test HWPX XML structure is valid."""
        # Find a sample file
        sample_file = None
        if test_dir.exists():
            for subdir in test_dir.iterdir():
                if subdir.is_dir():
                    hwp_file = subdir / "from.hwp"
                    if hwp_file.exists():
                        sample_file = hwp_file
                        break

        if sample_file is None:
            pytest.skip("No sample HWP file available")

        hwp = HWPReader.read(sample_file)
        hwpx = Hwp2HwpxConverter.to_hwpx(hwp)

        # Write to temp file
        with tempfile.NamedTemporaryFile(suffix=".hwpx", delete=False) as f:
            output_path = Path(f.name)

        try:
            HWPXWriter.write(hwpx, output_path)

            with zipfile.ZipFile(output_path, "r") as zf:
                # Check version.xml
                version_content = zf.read("version.xml").decode("utf-8")
                assert "HWPMLVersionXMLFile" in version_content

                # Check header.xml
                header_content = zf.read("Contents/header.xml").decode("utf-8")
                assert "<hh:head" in header_content or "<head" in header_content

                # Check section0.xml
                section_content = zf.read("Contents/section0.xml").decode("utf-8")
                assert "<hs:sec" in section_content or "<sec" in section_content
        finally:
            if output_path.exists():
                output_path.unlink()
