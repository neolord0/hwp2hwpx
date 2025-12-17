"""Container XML builder for HWPX format."""

from lxml import etree

from hwp2hwpx.hwpx.builder.base import NAMESPACES, NSMAP_CONTAINER, odf
from hwp2hwpx.hwpx.models.file import ContainerXML


def build_container_xml(container: ContainerXML) -> str:
    """Build META-INF/container.xml content.

    Args:
        container: Container information

    Returns:
        XML string
    """
    root = etree.Element(odf("container"), nsmap=NSMAP_CONTAINER)
    root.set("version", "1.0")

    rootfiles = etree.SubElement(root, odf("rootfiles"))

    rootfile = etree.SubElement(rootfiles, odf("rootfile"))
    rootfile.set("full-path", container.root_file_path)
    rootfile.set("media-type", container.root_file_media_type)

    return etree.tostring(
        root,
        xml_declaration=True,
        encoding="UTF-8",
        pretty_print=True,
    ).decode("UTF-8")
