"""Manifest XML builder for HWPX format."""

from lxml import etree

from hwp2hwpx.hwpx.models.file import ManifestXML

# Manifest namespace
MANIFEST_NS = "urn:oasis:names:tc:opendocument:xmlns:manifest:1.0"
MANIFEST_NSMAP = {None: MANIFEST_NS}


def build_manifest_xml(manifest: ManifestXML) -> str:
    """Build META-INF/manifest.xml content.

    Args:
        manifest: Manifest information

    Returns:
        XML string
    """
    root = etree.Element(f"{{{MANIFEST_NS}}}manifest", nsmap=MANIFEST_NSMAP)
    root.set("version", "1.2")

    for item in manifest.items:
        file_entry = etree.SubElement(root, f"{{{MANIFEST_NS}}}file-entry")
        file_entry.set(f"{{{MANIFEST_NS}}}full-path", item.href)
        file_entry.set(f"{{{MANIFEST_NS}}}media-type", item.media_type)

    return etree.tostring(
        root,
        xml_declaration=True,
        encoding="UTF-8",
        pretty_print=True,
    ).decode("UTF-8")
