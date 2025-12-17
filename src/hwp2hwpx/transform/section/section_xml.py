"""Section XML converter for HWP to HWPX conversion."""

from lxml import etree

from hwp2hwpx.hwp.models.body_text import Section
from hwp2hwpx.hwp.models.paragraph import Paragraph, CharType
from hwp2hwpx.hwpx.builder.base import NSMAP_SECTION, hc, hp, hs
from hwp2hwpx.transform.converter import Converter
from hwp2hwpx.transform.parameter import Parameter


class SectionXMLConverter(Converter):
    """Converter for section XML generation."""

    def __init__(self, param: Parameter) -> None:
        super().__init__(param)

    def convert(self, section: Section, section_index: int) -> str:
        """Convert HWP Section to HWPX section XML.

        Args:
            section: HWP Section object
            section_index: Section index

        Returns:
            Section XML content as string
        """
        self.param.current_section_index = section_index

        # Create root element
        root = etree.Element(hs("sec"), nsmap=NSMAP_SECTION)

        # Section properties
        if section.section_define and section.section_define.page_def:
            self._build_sec_pr(root, section)

        # Paragraphs
        for i, para in enumerate(section.paragraphs):
            self.param.current_paragraph_index = i
            self._build_paragraph(root, para)

        return etree.tostring(
            root,
            xml_declaration=True,
            encoding="UTF-8",
            pretty_print=True,
        ).decode("UTF-8")

    def _build_sec_pr(self, root: etree._Element, section: Section) -> None:
        """Build section properties element."""
        sec_def = section.section_define
        if not sec_def or not sec_def.page_def:
            return

        page_def = sec_def.page_def

        sec_pr = etree.SubElement(root, hs("secPr"))
        sec_pr.set("textDirection", "HORIZONTAL")
        sec_pr.set("spaceColumns", str(sec_def.column_gap))

        # Page properties
        page_pr = etree.SubElement(sec_pr, hs("pagePr"))
        page_pr.set("landscape", "1" if page_def.landscape else "0")
        page_pr.set("width", str(page_def.paper_width))
        page_pr.set("height", str(page_def.paper_height))

        # Margins
        margin = etree.SubElement(page_pr, hs("margin"))
        margin.set("left", str(page_def.margin_left))
        margin.set("right", str(page_def.margin_right))
        margin.set("top", str(page_def.margin_top))
        margin.set("bottom", str(page_def.margin_bottom))
        margin.set("header", str(page_def.header_margin))
        margin.set("footer", str(page_def.footer_margin))
        margin.set("gutter", str(page_def.gutter_margin))

    def _build_paragraph(self, parent: etree._Element, para: Paragraph) -> None:
        """Build paragraph element."""
        p_elem = etree.SubElement(parent, hp("p"))
        p_elem.set("paraPrIDRef", str(para.header.para_shape_id))
        p_elem.set("styleIDRef", str(para.header.style_id))

        # Build runs from text and char shapes
        self._build_runs(p_elem, para)

    def _build_runs(self, p_elem: etree._Element, para: Paragraph) -> None:
        """Build run elements from paragraph text and character shapes."""
        if not para.text.text:
            return

        text = para.text.text
        char_shapes = para.char_shapes

        # Simple approach: create runs based on char shape positions
        if not char_shapes:
            # Single run with all text
            run = etree.SubElement(p_elem, hp("run"))
            run.set("charPrIDRef", "0")
            self._add_text_to_run(run, text)
            return

        # Build runs based on char shape positions
        run_boundaries = []
        for cs in char_shapes:
            run_boundaries.append((cs.position, cs.char_shape_id))

        # Add end boundary
        run_boundaries.append((len(text), -1))

        for i in range(len(run_boundaries) - 1):
            start_pos, char_shape_id = run_boundaries[i]
            end_pos = run_boundaries[i + 1][0]

            if start_pos >= len(text):
                break

            run_text = text[start_pos:end_pos]
            if not run_text:
                continue

            run = etree.SubElement(p_elem, hp("run"))
            run.set("charPrIDRef", str(char_shape_id))
            self._add_text_to_run(run, run_text)

    def _add_text_to_run(self, run: etree._Element, text: str) -> None:
        """Add text content to run element.

        Args:
            run: Run element
            text: Text content
        """
        # Split by line breaks and create multiple T elements
        i = 0
        current_text = []

        while i < len(text):
            char = text[i]
            char_code = ord(char)

            # Check for control characters
            if char_code == 0x0D:  # Paragraph break
                # Flush current text
                if current_text:
                    t_elem = etree.SubElement(run, hp("t"))
                    t_elem.text = ''.join(current_text)
                    current_text = []
                i += 1
                continue

            elif char_code == 0x0A:  # Line break
                # Flush current text and add line break
                if current_text:
                    t_elem = etree.SubElement(run, hp("t"))
                    t_elem.text = ''.join(current_text)
                    current_text = []
                # Add line break element
                etree.SubElement(run, hp("linesegbreak"))
                i += 1
                continue

            elif char_code == 0x09:  # Tab
                # Flush current text and add tab
                if current_text:
                    t_elem = etree.SubElement(run, hp("t"))
                    t_elem.text = ''.join(current_text)
                    current_text = []
                # Add tab element
                etree.SubElement(run, hp("tab"))
                i += 1
                continue

            elif char_code < 0x20:  # Other control chars - skip
                # For extended controls (table, gso, etc.), skip 8 chars
                if char_code in (0x02, 0x03, 0x04, 0x08, 0x09, 0x0B, 0x0C):
                    i += 8
                else:
                    i += 1
                continue

            else:
                # Normal character
                current_text.append(char)
                i += 1

        # Flush remaining text
        if current_text:
            t_elem = etree.SubElement(run, hp("t"))
            t_elem.text = ''.join(current_text)
