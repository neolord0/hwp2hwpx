"""Basic tests for hwp2hwpx package."""

import pytest
from pathlib import Path

from hwp2hwpx import __version__
from hwp2hwpx.util.binary import (
    read_uint8,
    read_uint16,
    read_uint32,
    read_wchar_string,
    get_bits,
    get_bit,
)
from hwp2hwpx.util.color import Color4Byte, color_to_hex


class TestVersion:
    """Version tests."""

    def test_version_exists(self):
        """Test that version is defined."""
        assert __version__ is not None
        assert isinstance(__version__, str)

    def test_version_format(self):
        """Test that version follows semver format."""
        parts = __version__.split(".")
        assert len(parts) >= 2


class TestBinaryUtils:
    """Binary utility tests."""

    def test_read_uint8(self):
        """Test reading uint8."""
        data = bytes([0x42, 0x00])
        value, offset = read_uint8(data, 0)
        assert value == 0x42
        assert offset == 1

    def test_read_uint16(self):
        """Test reading uint16 little-endian."""
        data = bytes([0x34, 0x12])
        value, offset = read_uint16(data, 0)
        assert value == 0x1234
        assert offset == 2

    def test_read_uint32(self):
        """Test reading uint32 little-endian."""
        data = bytes([0x78, 0x56, 0x34, 0x12])
        value, offset = read_uint32(data, 0)
        assert value == 0x12345678
        assert offset == 4

    def test_read_wchar_string(self):
        """Test reading UTF-16LE string."""
        # "AB" in UTF-16LE
        data = bytes([0x41, 0x00, 0x42, 0x00])
        value, offset = read_wchar_string(data, 0, 2)
        assert value == "AB"
        assert offset == 4

    def test_get_bits(self):
        """Test extracting bits from value."""
        value = 0b11010110
        assert get_bits(value, 0, 3) == 0b0110
        assert get_bits(value, 4, 7) == 0b1101

    def test_get_bit(self):
        """Test extracting single bit."""
        value = 0b10101010
        assert get_bit(value, 1) is True
        assert get_bit(value, 0) is False


class TestColorUtils:
    """Color utility tests."""

    def test_color4byte_rgb(self):
        """Test Color4Byte RGB extraction."""
        color = Color4Byte(0x00112233)
        assert color.r == 0x33
        assert color.g == 0x22
        assert color.b == 0x11

    def test_color4byte_to_hex(self):
        """Test Color4Byte to hex conversion."""
        color = Color4Byte(0x00FF8040)
        assert color.to_hex() == "#4080FF"

    def test_color4byte_none(self):
        """Test Color4Byte none detection."""
        color = Color4Byte(0xFFFFFFFF)
        assert color.is_none() is True

        color2 = Color4Byte(0x00000000)
        assert color2.is_none() is False

    def test_color_to_hex_function(self):
        """Test color_to_hex function."""
        assert color_to_hex(0x00FF0000) == "#0000FF"
        assert color_to_hex(0xFFFFFFFF) == "none"


class TestEnums:
    """Enum tests."""

    def test_hwp_tag_enum(self):
        """Test HWP tag enumeration."""
        from hwp2hwpx.hwp.enums.tags import HWPTag

        assert HWPTag.DOCUMENT_PROPERTIES == 0x10
        assert HWPTag.PARA_HEADER == 0x42

    def test_border_type_enum(self):
        """Test border type enumeration."""
        from hwp2hwpx.hwp.enums.border_fill import BorderType

        assert BorderType.NONE.value == 0
        assert BorderType.SOLID.value == 1

    def test_alignment_enum(self):
        """Test alignment enumeration."""
        from hwp2hwpx.hwp.enums.para_shape import Alignment

        assert Alignment.JUSTIFY.value == 0
        assert Alignment.LEFT.value == 1
        assert Alignment.CENTER.value == 3


class TestRecordParser:
    """Record parser tests."""

    def test_parse_record_header(self):
        """Test parsing record header."""
        from hwp2hwpx.hwp.parser.record import parse_record_header

        # Tag 0x10, Level 0, Size 10
        # Header = (Size << 20) | (Level << 10) | Tag
        header_value = (10 << 20) | (0 << 10) | 0x10
        data = header_value.to_bytes(4, "little") + bytes(10)

        header, offset = parse_record_header(data, 0)
        assert header.tag_id == 0x10
        assert header.level == 0
        assert header.size == 10
        assert offset == 4

    def test_parse_all_records(self):
        """Test parsing all records from data."""
        from hwp2hwpx.hwp.parser.record import parse_all_records

        # Create two records
        header1 = (5 << 20) | (0 << 10) | 0x10
        header2 = (3 << 20) | (0 << 10) | 0x11
        data = (
            header1.to_bytes(4, "little") + bytes(5) +
            header2.to_bytes(4, "little") + bytes(3)
        )

        records = parse_all_records(data)
        assert len(records) == 2
        assert records[0][0].tag_id == 0x10
        assert records[0][0].size == 5
        assert records[1][0].tag_id == 0x11
        assert records[1][0].size == 3
