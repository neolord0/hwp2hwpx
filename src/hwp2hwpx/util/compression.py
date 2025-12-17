"""Compression utilities for HWP file format."""

import zlib
from typing import Optional


def decompress_stream(data: bytes) -> bytes:
    """Decompress zlib compressed data.

    Args:
        data: Compressed bytes

    Returns:
        Decompressed bytes
    """
    return zlib.decompress(data, -15)  # -15 for raw deflate without header


def compress_stream(data: bytes) -> bytes:
    """Compress data using zlib.

    Args:
        data: Uncompressed bytes

    Returns:
        Compressed bytes
    """
    compress_obj = zlib.compressobj(9, zlib.DEFLATED, -15)
    compressed = compress_obj.compress(data)
    compressed += compress_obj.flush()
    return compressed


def is_compressed(properties: int) -> bool:
    """Check if HWP file is compressed based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if compressed, False otherwise
    """
    return bool(properties & 0x01)


def is_encrypted(properties: int) -> bool:
    """Check if HWP file is encrypted based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if encrypted, False otherwise
    """
    return bool(properties & 0x02)


def is_distribution_document(properties: int) -> bool:
    """Check if HWP file is a distribution document based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if distribution document, False otherwise
    """
    return bool(properties & 0x04)


def is_script_saved(properties: int) -> bool:
    """Check if HWP file has script saved based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if script saved, False otherwise
    """
    return bool(properties & 0x08)


def is_drm_secured(properties: int) -> bool:
    """Check if HWP file is DRM secured based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if DRM secured, False otherwise
    """
    return bool(properties & 0x10)


def is_xml_template(properties: int) -> bool:
    """Check if HWP file is XML template based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if XML template, False otherwise
    """
    return bool(properties & 0x20)


def is_document_history_saved(properties: int) -> bool:
    """Check if HWP file has document history saved based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if document history saved, False otherwise
    """
    return bool(properties & 0x40)


def is_sign_information_saved(properties: int) -> bool:
    """Check if HWP file has sign information saved based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if sign information saved, False otherwise
    """
    return bool(properties & 0x80)


def is_certificate_encryption(properties: int) -> bool:
    """Check if HWP file uses certificate encryption based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if certificate encryption, False otherwise
    """
    return bool(properties & 0x100)


def is_sign_certificate_saved(properties: int) -> bool:
    """Check if HWP file has sign certificate saved based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if sign certificate saved, False otherwise
    """
    return bool(properties & 0x200)


def is_certificate_drm(properties: int) -> bool:
    """Check if HWP file uses certificate DRM based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if certificate DRM, False otherwise
    """
    return bool(properties & 0x400)


def is_ccl_document(properties: int) -> bool:
    """Check if HWP file is CCL document based on properties flag.

    Args:
        properties: File header properties flag

    Returns:
        True if CCL document, False otherwise
    """
    return bool(properties & 0x800)


def decompress_stream_safe(data: bytes) -> Optional[bytes]:
    """Safely decompress zlib compressed data.

    Args:
        data: Compressed bytes

    Returns:
        Decompressed bytes or None if decompression fails
    """
    try:
        return decompress_stream(data)
    except zlib.error:
        return None
