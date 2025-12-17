"""Base converter class for HWP to HWPX conversion."""

from hwp2hwpx.hwp.models.file import HWPFile
from hwp2hwpx.hwpx.models.file import HWPXFile
from hwp2hwpx.transform.parameter import Parameter


class Converter:
    """Base class for converters.

    Provides common properties and methods for all conversion operations.
    """

    def __init__(self, param: Parameter) -> None:
        """Initialize converter with transformation context.

        Args:
            param: Transformation context/parameter
        """
        self._param = param

    @property
    def param(self) -> Parameter:
        """Get transformation context."""
        return self._param

    @property
    def hwp(self) -> HWPFile:
        """Get source HWP file."""
        return self._param.hwp

    @property
    def hwpx(self) -> HWPXFile:
        """Get target HWPX file."""
        return self._param.hwpx
