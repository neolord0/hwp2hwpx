"""Content HPF builder for HWPX format."""

from lxml import etree

from hwp2hwpx.hwpx.builder.base import NAMESPACES, NSMAP_CONTENT_HPF, dc, opf
from hwp2hwpx.hwpx.models.file import ContentHPF


def build_content_hpf(content: ContentHPF) -> str:
    """Build Contents/content.hpf content.

    Args:
        content: Content HPF information

    Returns:
        XML string
    """
    root = etree.Element(opf("package"), nsmap=NSMAP_CONTENT_HPF)
    root.set("version", "2.0")
    root.set("unique-identifier", "bookid")

    # Metadata
    metadata = etree.SubElement(root, opf("metadata"))

    if content.title:
        title_elem = etree.SubElement(metadata, dc("title"))
        title_elem.text = content.title

    lang_elem = etree.SubElement(metadata, dc("language"))
    lang_elem.text = content.language

    if content.description:
        desc_elem = etree.SubElement(metadata, dc("description"))
        desc_elem.text = content.description

    if content.creator:
        creator_elem = etree.SubElement(metadata, dc("creator"))
        creator_elem.text = content.creator

    if content.date:
        date_elem = etree.SubElement(metadata, dc("date"))
        date_elem.text = content.date

    # Manifest
    manifest = etree.SubElement(root, opf("manifest"))

    for item in content.manifest:
        item_elem = etree.SubElement(manifest, opf("item"))
        item_elem.set("id", item.id)
        item_elem.set("href", item.href)
        item_elem.set("media-type", item.media_type)

    # Spine
    spine = etree.SubElement(root, opf("spine"))

    for spine_item in content.spine:
        itemref = etree.SubElement(spine, opf("itemref"))
        itemref.set("idref", spine_item.idref)

    return etree.tostring(
        root,
        xml_declaration=True,
        encoding="UTF-8",
        pretty_print=True,
    ).decode("UTF-8")
