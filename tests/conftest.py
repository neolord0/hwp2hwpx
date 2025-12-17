"""Pytest configuration and fixtures."""

import pytest
from pathlib import Path


@pytest.fixture
def fixtures_dir() -> Path:
    """Return path to test fixtures directory."""
    return Path(__file__).parent / "fixtures"


@pytest.fixture
def test_dir() -> Path:
    """Return path to project test directory (with HWP samples)."""
    return Path(__file__).parent.parent / "test"


@pytest.fixture
def sample_hwp_files(test_dir: Path) -> list[Path]:
    """Return list of sample HWP files from test directory."""
    hwp_files = []
    if test_dir.exists():
        for subdir in test_dir.iterdir():
            if subdir.is_dir():
                for hwp_file in subdir.glob("*.hwp"):
                    hwp_files.append(hwp_file)
                for hwp_file in subdir.glob("from.hwp"):
                    hwp_files.append(hwp_file)
    return hwp_files
