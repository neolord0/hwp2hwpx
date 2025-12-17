"""Header XML converter for HWP to HWPX conversion."""

from lxml import etree

from hwp2hwpx.hwpx.builder.base import NSMAP_HEAD, hc, hh
from hwp2hwpx.transform.converter import Converter
from hwp2hwpx.transform.parameter import Parameter


class HeaderXMLConverter(Converter):
    """Converter for header.xml generation."""

    def __init__(self, param: Parameter) -> None:
        super().__init__(param)

    def convert(self) -> str:
        """Convert HWP DocInfo to HWPX header.xml.

        Returns:
            Header XML content as string
        """
        # Create root element
        root = etree.Element(hh("head"), nsmap=NSMAP_HEAD)
        root.set("version", "1.4")
        root.set("secCnt", str(self.hwp.body_text.section_count))

        # Begin Num (starting numbers)
        self._build_begin_num(root)

        # Mapping table (RefList)
        self._build_ref_list(root)

        # Compatible document
        self._build_compatible_document(root)

        # Doc option
        self._build_doc_option(root)

        return etree.tostring(
            root,
            xml_declaration=True,
            encoding="UTF-8",
            pretty_print=True,
        ).decode("UTF-8")

    def _build_begin_num(self, root: etree._Element) -> None:
        """Build BeginNum element."""
        doc_props = self.hwp.doc_info.document_properties

        begin_num = etree.SubElement(root, hh("beginNum"))
        begin_num.set("page", str(doc_props.starting_page_number))
        begin_num.set("footnote", str(doc_props.starting_footnote_number))
        begin_num.set("endnote", str(doc_props.starting_endnote_number))
        begin_num.set("pic", str(doc_props.starting_picture_number))
        begin_num.set("tbl", str(doc_props.starting_table_number))
        begin_num.set("equation", str(doc_props.starting_equation_number))

    def _build_ref_list(self, root: etree._Element) -> None:
        """Build RefList (mapping tables) element."""
        ref_list = etree.SubElement(root, hh("refList"))

        # Font faces
        self._build_fontfaces(ref_list)

        # Border fills
        self._build_border_fills(ref_list)

        # Char properties (character shapes)
        self._build_char_properties(ref_list)

        # Tab properties
        self._build_tab_properties(ref_list)

        # Numberings
        self._build_numberings(ref_list)

        # Bullets
        self._build_bullets(ref_list)

        # Para properties (paragraph shapes)
        self._build_para_properties(ref_list)

        # Styles
        self._build_styles(ref_list)

    def _build_fontfaces(self, ref_list: etree._Element) -> None:
        """Build fontfaces element."""
        fontfaces = etree.SubElement(ref_list, hh("fontfaces"))

        # Korean
        self._build_fontface_group(fontfaces, "HANGUL", self.hwp.doc_info.face_names.korean)
        # English
        self._build_fontface_group(fontfaces, "LATIN", self.hwp.doc_info.face_names.english)
        # Chinese
        self._build_fontface_group(fontfaces, "HANJA", self.hwp.doc_info.face_names.chinese)
        # Japanese
        self._build_fontface_group(fontfaces, "JAPANESE", self.hwp.doc_info.face_names.japanese)
        # Other
        self._build_fontface_group(fontfaces, "OTHER", self.hwp.doc_info.face_names.other)
        # Symbol
        self._build_fontface_group(fontfaces, "SYMBOL", self.hwp.doc_info.face_names.symbol)
        # User
        self._build_fontface_group(fontfaces, "USER", self.hwp.doc_info.face_names.user)

    def _build_fontface_group(
        self, fontfaces: etree._Element, lang: str, fonts: list
    ) -> None:
        """Build fontface group for a language."""
        if not fonts:
            return

        fontface = etree.SubElement(fontfaces, hh("fontface"))
        fontface.set("lang", lang)
        fontface.set("count", str(len(fonts)))

        for i, font in enumerate(fonts):
            font_elem = etree.SubElement(fontface, hh("font"))
            font_elem.set("id", str(i))
            font_elem.set("face", font.name)

            if font.substitute_info:
                font_elem.set("type", str(font.substitute_info.font_type))

    def _build_border_fills(self, ref_list: etree._Element) -> None:
        """Build borderFills element."""
        border_fills_elem = etree.SubElement(ref_list, hh("borderFills"))
        border_fills_elem.set("itemCnt", str(len(self.hwp.doc_info.border_fills.items)))

        for i, bf in enumerate(self.hwp.doc_info.border_fills.items):
            bf_elem = etree.SubElement(border_fills_elem, hh("borderFill"))
            bf_elem.set("id", str(i + 1))  # 1-based index

            # Build border
            self._build_border(bf_elem, bf)

    def _build_border(self, parent: etree._Element, bf) -> None:
        """Build border sub-elements."""
        # Left border
        left = etree.SubElement(parent, hc("leftBorder"))
        left.set("type", bf.border.left.border_type.name.lower())
        left.set("width", bf.border.left.thickness.name.replace("MM", "").replace("_", ".") + "mm")
        left.set("color", bf.border.left.color.to_hex())

        # Right border
        right = etree.SubElement(parent, hc("rightBorder"))
        right.set("type", bf.border.right.border_type.name.lower())
        right.set("width", bf.border.right.thickness.name.replace("MM", "").replace("_", ".") + "mm")
        right.set("color", bf.border.right.color.to_hex())

        # Top border
        top = etree.SubElement(parent, hc("topBorder"))
        top.set("type", bf.border.top.border_type.name.lower())
        top.set("width", bf.border.top.thickness.name.replace("MM", "").replace("_", ".") + "mm")
        top.set("color", bf.border.top.color.to_hex())

        # Bottom border
        bottom = etree.SubElement(parent, hc("bottomBorder"))
        bottom.set("type", bf.border.bottom.border_type.name.lower())
        bottom.set("width", bf.border.bottom.thickness.name.replace("MM", "").replace("_", ".") + "mm")
        bottom.set("color", bf.border.bottom.color.to_hex())

    def _build_char_properties(self, ref_list: etree._Element) -> None:
        """Build charProperties element."""
        char_props = etree.SubElement(ref_list, hh("charProperties"))
        char_props.set("itemCnt", str(len(self.hwp.doc_info.char_shapes.items)))

        for i, cs in enumerate(self.hwp.doc_info.char_shapes.items):
            cp_elem = etree.SubElement(char_props, hh("charPr"))
            cp_elem.set("id", str(i))
            cp_elem.set("height", str(cs.base_size))
            cp_elem.set("textColor", cs.text_color.to_hex())

            if cs.is_bold:
                cp_elem.set("bold", "1")
            if cs.is_italic:
                cp_elem.set("italic", "1")

    def _build_tab_properties(self, ref_list: etree._Element) -> None:
        """Build tabProperties element."""
        tab_props = etree.SubElement(ref_list, hh("tabProperties"))
        tab_props.set("itemCnt", str(len(self.hwp.doc_info.tab_defs.items)))

        for i, td in enumerate(self.hwp.doc_info.tab_defs.items):
            tp_elem = etree.SubElement(tab_props, hh("tabPr"))
            tp_elem.set("id", str(i))
            tp_elem.set("autoTabLeft", "1" if td.auto_tab_left else "0")
            tp_elem.set("autoTabRight", "1" if td.auto_tab_right else "0")

    def _build_numberings(self, ref_list: etree._Element) -> None:
        """Build numberings element."""
        numberings_elem = etree.SubElement(ref_list, hh("numberings"))
        numberings_elem.set("itemCnt", str(len(self.hwp.doc_info.numberings.items)))

        for i, num in enumerate(self.hwp.doc_info.numberings.items):
            num_elem = etree.SubElement(numberings_elem, hh("numbering"))
            num_elem.set("id", str(i + 1))  # 1-based

    def _build_bullets(self, ref_list: etree._Element) -> None:
        """Build bullets element."""
        bullets_elem = etree.SubElement(ref_list, hh("bullets"))
        bullets_elem.set("itemCnt", str(len(self.hwp.doc_info.bullets.items)))

        for i, bullet in enumerate(self.hwp.doc_info.bullets.items):
            bullet_elem = etree.SubElement(bullets_elem, hh("bullet"))
            bullet_elem.set("id", str(i + 1))  # 1-based
            bullet_elem.set("char", bullet.bullet_char)

    def _build_para_properties(self, ref_list: etree._Element) -> None:
        """Build paraProperties element."""
        para_props = etree.SubElement(ref_list, hh("paraProperties"))
        para_props.set("itemCnt", str(len(self.hwp.doc_info.para_shapes.items)))

        for i, ps in enumerate(self.hwp.doc_info.para_shapes.items):
            pp_elem = etree.SubElement(para_props, hh("paraPr"))
            pp_elem.set("id", str(i))
            pp_elem.set("align", ps.alignment.name.lower())
            pp_elem.set("lineSpacing", str(ps.line_spacing))
            pp_elem.set("marginLeft", str(ps.margin.left))
            pp_elem.set("marginRight", str(ps.margin.right))

    def _build_styles(self, ref_list: etree._Element) -> None:
        """Build styles element."""
        styles_elem = etree.SubElement(ref_list, hh("styles"))
        styles_elem.set("itemCnt", str(len(self.hwp.doc_info.styles.items)))

        for i, style in enumerate(self.hwp.doc_info.styles.items):
            style_elem = etree.SubElement(styles_elem, hh("style"))
            style_elem.set("id", str(i))
            style_elem.set("name", style.name)
            style_elem.set("engName", style.english_name)
            style_elem.set("paraPrIDRef", str(style.para_shape_id))
            style_elem.set("charPrIDRef", str(style.char_shape_id))

    def _build_compatible_document(self, root: etree._Element) -> None:
        """Build compatibleDocument element."""
        compat = self.hwp.doc_info.compatible_document
        if compat:
            compat_elem = etree.SubElement(root, hh("compatibleDocument"))
            compat_elem.set("targetProgram", str(compat.target_program))

    def _build_doc_option(self, root: etree._Element) -> None:
        """Build docOption element."""
        # Basic doc option - can be extended
        doc_option = etree.SubElement(root, hh("docOption"))
        doc_option.set("linkInfo", "0")
