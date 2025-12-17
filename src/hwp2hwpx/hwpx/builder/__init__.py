"""HWPX XML builders."""

from hwp2hwpx.hwpx.builder.base import (
    NAMESPACES,
    NSMAP_CONTAINER,
    NSMAP_CONTENT_HPF,
    NSMAP_HEAD,
    NSMAP_MASTER_PAGE,
    NSMAP_PARA,
    NSMAP_SECTION,
    XMLBuilder,
    dc,
    hc,
    hh,
    hmh,
    hp,
    hs,
    ns,
    odf,
    opf,
)
from hwp2hwpx.hwpx.builder.container import build_container_xml
from hwp2hwpx.hwpx.builder.content_hpf import build_content_hpf
from hwp2hwpx.hwpx.builder.manifest import build_manifest_xml
from hwp2hwpx.hwpx.builder.version import build_version_xml

__all__ = [
    # base
    "XMLBuilder",
    "NAMESPACES",
    "NSMAP_HEAD",
    "NSMAP_SECTION",
    "NSMAP_PARA",
    "NSMAP_CONTENT_HPF",
    "NSMAP_CONTAINER",
    "NSMAP_MASTER_PAGE",
    "ns",
    "hh",
    "hs",
    "hp",
    "hc",
    "opf",
    "dc",
    "odf",
    "hmh",
    # builders
    "build_version_xml",
    "build_container_xml",
    "build_manifest_xml",
    "build_content_hpf",
]
