"""Base XML builder utilities for HWPX format."""

from io import BytesIO
from typing import Dict, Optional

from lxml import etree

# HWPX XML Namespaces
NAMESPACES: Dict[str, str] = {
    "hh": "http://www.hancom.co.kr/hwpml/2011/head",
    "hs": "http://www.hancom.co.kr/hwpml/2011/section",
    "hp": "http://www.hancom.co.kr/hwpml/2011/paragraph",
    "hc": "http://www.hancom.co.kr/hwpml/2011/core",
    "hwp": "http://www.hancom.co.kr/hwpml/2011/hwp",
    "config": "urn:oasis:names:tc:opendocument:xmlns:config:1.0",
    "dc": "http://purl.org/dc/elements/1.1/",
    "opf": "http://www.idpf.org/2007/opf",
    "odf": "urn:oasis:names:tc:opendocument:xmlns:container",
    "hhs": "http://www.hancom.co.kr/hwpml/2011/HwpHistory",
    "hmh": "http://www.hancom.co.kr/hwpml/2011/MasterPage",
}

# Namespace maps for specific document types
NSMAP_HEAD = {
    None: NAMESPACES["hh"],
    "hc": NAMESPACES["hc"],
}

NSMAP_SECTION = {
    None: NAMESPACES["hs"],
    "hc": NAMESPACES["hc"],
    "hp": NAMESPACES["hp"],
}

NSMAP_PARA = {
    None: NAMESPACES["hp"],
    "hc": NAMESPACES["hc"],
}

NSMAP_CONTENT_HPF = {
    None: NAMESPACES["opf"],
    "dc": NAMESPACES["dc"],
}

NSMAP_CONTAINER = {
    None: NAMESPACES["odf"],
}

NSMAP_MASTER_PAGE = {
    None: NAMESPACES["hmh"],
    "hc": NAMESPACES["hc"],
    "hp": NAMESPACES["hp"],
    "hs": NAMESPACES["hs"],
}


class XMLBuilder:
    """Base class for XML builders."""

    @staticmethod
    def create_element(
        tag: str,
        nsmap: Optional[Dict[Optional[str], str]] = None,
        **attribs: str,
    ) -> etree._Element:
        """Create an XML element.

        Args:
            tag: Element tag name (with or without namespace prefix)
            nsmap: Namespace map
            **attribs: Element attributes

        Returns:
            Created element
        """
        # Handle namespace prefix in tag
        if ":" in tag:
            prefix, local = tag.split(":", 1)
            if nsmap and prefix in nsmap:
                ns = nsmap[prefix]
                tag = f"{{{ns}}}{local}"

        elem = etree.Element(tag, nsmap=nsmap)

        for key, value in attribs.items():
            if value is not None:
                elem.set(key, str(value))

        return elem

    @staticmethod
    def sub_element(
        parent: etree._Element,
        tag: str,
        **attribs: str,
    ) -> etree._Element:
        """Create and append a sub-element.

        Args:
            parent: Parent element
            tag: Element tag name
            **attribs: Element attributes

        Returns:
            Created element
        """
        elem = etree.SubElement(parent, tag)

        for key, value in attribs.items():
            if value is not None:
                elem.set(key, str(value))

        return elem

    @staticmethod
    def to_string(
        root: etree._Element,
        xml_declaration: bool = True,
        encoding: str = "UTF-8",
    ) -> str:
        """Convert element tree to XML string.

        Args:
            root: Root element
            xml_declaration: Include XML declaration
            encoding: Encoding for declaration

        Returns:
            XML string
        """
        return etree.tostring(
            root,
            xml_declaration=xml_declaration,
            encoding=encoding,
            pretty_print=True,
        ).decode(encoding)

    @staticmethod
    def to_bytes(
        root: etree._Element,
        xml_declaration: bool = True,
        encoding: str = "UTF-8",
    ) -> bytes:
        """Convert element tree to bytes.

        Args:
            root: Root element
            xml_declaration: Include XML declaration
            encoding: Encoding

        Returns:
            XML bytes
        """
        return etree.tostring(
            root,
            xml_declaration=xml_declaration,
            encoding=encoding,
            pretty_print=True,
        )


def ns(prefix: str, tag: str) -> str:
    """Create a namespaced tag.

    Args:
        prefix: Namespace prefix
        tag: Local tag name

    Returns:
        Full namespaced tag
    """
    if prefix in NAMESPACES:
        return f"{{{NAMESPACES[prefix]}}}{tag}"
    return tag


def hh(tag: str) -> str:
    """Create head namespace tag."""
    return ns("hh", tag)


def hs(tag: str) -> str:
    """Create section namespace tag."""
    return ns("hs", tag)


def hp(tag: str) -> str:
    """Create paragraph namespace tag."""
    return ns("hp", tag)


def hc(tag: str) -> str:
    """Create core namespace tag."""
    return ns("hc", tag)


def opf(tag: str) -> str:
    """Create OPF namespace tag."""
    return ns("opf", tag)


def dc(tag: str) -> str:
    """Create DC namespace tag."""
    return ns("dc", tag)


def odf(tag: str) -> str:
    """Create ODF namespace tag."""
    return ns("odf", tag)


def hmh(tag: str) -> str:
    """Create master page namespace tag."""
    return ns("hmh", tag)
