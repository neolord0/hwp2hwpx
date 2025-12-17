"""HWP file header model."""

from dataclasses import dataclass
from typing import Tuple

from hwp2hwpx.util.compression import (
    is_ccl_document,
    is_certificate_drm,
    is_certificate_encryption,
    is_compressed,
    is_distribution_document,
    is_document_history_saved,
    is_drm_secured,
    is_encrypted,
    is_script_saved,
    is_sign_certificate_saved,
    is_sign_information_saved,
    is_xml_template,
)

# HWP file signature
HWP_SIGNATURE = "HWP Document File"
HWP_SIGNATURE_BYTES = HWP_SIGNATURE.encode("utf-8")


@dataclass
class FileVersion:
    """HWP file version."""

    major: int
    minor: int
    build: int
    revision: int

    def __str__(self) -> str:
        return f"{self.major}.{self.minor}.{self.build}.{self.revision}"

    @classmethod
    def from_bytes(cls, data: bytes) -> "FileVersion":
        """Parse version from 4 bytes."""
        return cls(
            major=data[3],
            minor=data[2],
            build=data[1],
            revision=data[0],
        )

    def is_over_5_0_2_2(self) -> bool:
        """Check if version is 5.0.2.2 or higher."""
        if self.major > 5:
            return True
        if self.major == 5 and self.minor > 0:
            return True
        if self.major == 5 and self.minor == 0 and self.build > 2:
            return True
        if self.major == 5 and self.minor == 0 and self.build == 2 and self.revision >= 2:
            return True
        return False

    def is_over_5_1_0_0(self) -> bool:
        """Check if version is 5.1.0.0 or higher."""
        if self.major > 5:
            return True
        if self.major == 5 and self.minor >= 1:
            return True
        return False


@dataclass
class FileHeader:
    """HWP file header.

    The file header is stored in the "FileHeader" stream and contains:
    - Signature (32 bytes): "HWP Document File"
    - Version (4 bytes): File format version
    - Properties (4 bytes): Compression, encryption, etc.
    - Reserved (216 bytes)
    """

    signature: str = ""
    version: FileVersion = None  # type: ignore[assignment]
    properties: int = 0
    license: int = 0
    encryption_version: int = 0
    kogl_license_support: int = 0

    def __post_init__(self):
        """Initialize default version if not provided."""
        if self.version is None:
            self.version = FileVersion(5, 0, 0, 0)

    @property
    def is_compressed(self) -> bool:
        """Check if file is compressed."""
        return is_compressed(self.properties)

    @property
    def is_encrypted(self) -> bool:
        """Check if file is encrypted."""
        return is_encrypted(self.properties)

    @property
    def is_distribution_document(self) -> bool:
        """Check if file is a distribution document."""
        return is_distribution_document(self.properties)

    @property
    def is_script_saved(self) -> bool:
        """Check if script is saved."""
        return is_script_saved(self.properties)

    @property
    def is_drm_secured(self) -> bool:
        """Check if file is DRM secured."""
        return is_drm_secured(self.properties)

    @property
    def is_xml_template(self) -> bool:
        """Check if file is XML template."""
        return is_xml_template(self.properties)

    @property
    def is_document_history_saved(self) -> bool:
        """Check if document history is saved."""
        return is_document_history_saved(self.properties)

    @property
    def is_sign_information_saved(self) -> bool:
        """Check if sign information is saved."""
        return is_sign_information_saved(self.properties)

    @property
    def is_certificate_encryption(self) -> bool:
        """Check if certificate encryption is used."""
        return is_certificate_encryption(self.properties)

    @property
    def is_sign_certificate_saved(self) -> bool:
        """Check if sign certificate is saved."""
        return is_sign_certificate_saved(self.properties)

    @property
    def is_certificate_drm(self) -> bool:
        """Check if certificate DRM is used."""
        return is_certificate_drm(self.properties)

    @property
    def is_ccl_document(self) -> bool:
        """Check if file is CCL document."""
        return is_ccl_document(self.properties)

    @classmethod
    def from_bytes(cls, data: bytes) -> "FileHeader":
        """Parse FileHeader from raw bytes.

        Args:
            data: Raw bytes (256 bytes)

        Returns:
            FileHeader instance
        """
        # Signature (32 bytes, null-terminated)
        signature_bytes = data[0:32]
        signature = signature_bytes.split(b"\x00")[0].decode("utf-8", errors="replace")

        # Version (4 bytes at offset 32)
        version = FileVersion.from_bytes(data[32:36])

        # Properties (4 bytes at offset 36)
        properties = int.from_bytes(data[36:40], "little")

        # License (4 bytes at offset 40)
        license_value = int.from_bytes(data[40:44], "little")

        # Encryption version (4 bytes at offset 44)
        encryption_version = int.from_bytes(data[44:48], "little")

        # KOGL license support (1 byte at offset 48)
        kogl_license_support = data[48]

        return cls(
            signature=signature,
            version=version,
            properties=properties,
            license=license_value,
            encryption_version=encryption_version,
            kogl_license_support=kogl_license_support,
        )

    def validate(self) -> Tuple[bool, str]:
        """Validate file header.

        Returns:
            Tuple of (is_valid, error_message)
        """
        if not self.signature.startswith(HWP_SIGNATURE):
            return False, f"Invalid signature: {self.signature}"

        if self.is_encrypted:
            return False, "Encrypted files are not supported"

        if self.is_drm_secured:
            return False, "DRM secured files are not supported"

        return True, ""
