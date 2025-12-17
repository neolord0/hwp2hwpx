"""Version XML builder for HWPX format."""

from lxml import etree

from hwp2hwpx.hwpx.models.file import VersionXML


def build_version_xml(version: VersionXML) -> str:
    """Build version.xml content.

    Args:
        version: Version information

    Returns:
        XML string
    """
    root = etree.Element("HWPMLVersionXMLFile")
    root.set("version", version.version)
    root.set("application", version.app_name)
    root.set("app-version", version.app_version)

    return etree.tostring(
        root,
        xml_declaration=True,
        encoding="UTF-8",
        pretty_print=True,
    ).decode("UTF-8")
