"""Binary parsing utilities for HWP file format."""

import struct
from typing import Tuple


def read_uint8(data: bytes, offset: int) -> Tuple[int, int]:
    """Read unsigned 8-bit integer from data at offset."""
    return data[offset], offset + 1


def read_uint16(data: bytes, offset: int) -> Tuple[int, int]:
    """Read unsigned 16-bit integer (little-endian) from data at offset."""
    value = struct.unpack_from("<H", data, offset)[0]
    return value, offset + 2


def read_int16(data: bytes, offset: int) -> Tuple[int, int]:
    """Read signed 16-bit integer (little-endian) from data at offset."""
    value = struct.unpack_from("<h", data, offset)[0]
    return value, offset + 2


def read_uint32(data: bytes, offset: int) -> Tuple[int, int]:
    """Read unsigned 32-bit integer (little-endian) from data at offset."""
    value = struct.unpack_from("<I", data, offset)[0]
    return value, offset + 4


def read_int32(data: bytes, offset: int) -> Tuple[int, int]:
    """Read signed 32-bit integer (little-endian) from data at offset."""
    value = struct.unpack_from("<i", data, offset)[0]
    return value, offset + 4


def read_uint64(data: bytes, offset: int) -> Tuple[int, int]:
    """Read unsigned 64-bit integer (little-endian) from data at offset."""
    value = struct.unpack_from("<Q", data, offset)[0]
    return value, offset + 8


def read_int64(data: bytes, offset: int) -> Tuple[int, int]:
    """Read signed 64-bit integer (little-endian) from data at offset."""
    value = struct.unpack_from("<q", data, offset)[0]
    return value, offset + 8


def read_float32(data: bytes, offset: int) -> Tuple[float, int]:
    """Read 32-bit float (little-endian) from data at offset."""
    value = struct.unpack_from("<f", data, offset)[0]
    return value, offset + 4


def read_float64(data: bytes, offset: int) -> Tuple[float, int]:
    """Read 64-bit float (little-endian) from data at offset."""
    value = struct.unpack_from("<d", data, offset)[0]
    return value, offset + 8


def read_bytes(data: bytes, offset: int, length: int) -> Tuple[bytes, int]:
    """Read specified number of bytes from data at offset."""
    return data[offset : offset + length], offset + length


def read_wchar_string(data: bytes, offset: int, char_count: int) -> Tuple[str, int]:
    """Read UTF-16LE string of specified character count from data at offset."""
    byte_length = char_count * 2
    string_bytes = data[offset : offset + byte_length]
    # Decode UTF-16LE and strip null characters
    string_value = string_bytes.decode("utf-16-le").rstrip("\x00")
    return string_value, offset + byte_length


def read_wchar_string_with_length(data: bytes, offset: int) -> Tuple[str, int]:
    """Read UTF-16LE string with length prefix (uint16) from data at offset."""
    char_count, offset = read_uint16(data, offset)
    if char_count == 0:
        return "", offset
    return read_wchar_string(data, offset, char_count)


def read_hwpunit(data: bytes, offset: int) -> Tuple[int, int]:
    """Read HWPUnit (32-bit signed integer) from data at offset.

    HWPUnit is 1/7200 inch or 1/100 mm.
    """
    return read_int32(data, offset)


def read_hwpunit16(data: bytes, offset: int) -> Tuple[int, int]:
    """Read HWPUnit16 (16-bit signed integer) from data at offset."""
    return read_int16(data, offset)


def read_shwpunit(data: bytes, offset: int) -> Tuple[int, int]:
    """Read SHWPUnit (16-bit signed integer) from data at offset.

    SHWPUnit is 1/7200 inch or 1/100 mm (compact form).
    """
    return read_int16(data, offset)


def get_bits(value: int, start: int, end: int) -> int:
    """Extract bits from value.

    Args:
        value: The integer value to extract bits from
        start: Starting bit position (inclusive)
        end: Ending bit position (inclusive)

    Returns:
        The extracted bits as an integer
    """
    mask = (1 << (end - start + 1)) - 1
    return (value >> start) & mask


def get_bit(value: int, position: int) -> bool:
    """Get single bit value as boolean.

    Args:
        value: The integer value
        position: Bit position

    Returns:
        True if bit is set, False otherwise
    """
    return bool((value >> position) & 1)


def to_unsigned(value: int, bits: int = 32) -> int:
    """Convert signed value to unsigned.

    Args:
        value: The signed integer value
        bits: Number of bits (default 32)

    Returns:
        The unsigned integer value
    """
    mask = (1 << bits) - 1
    return value & mask
