package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFont;
import kr.dogfoot.hwplib.object.docinfo.DocInfo;
import kr.dogfoot.hwplib.object.docinfo.FaceName;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.LanguageType;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.Fontface;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.Fontfaces;

public class ForFontfaces extends Converter {
    private Fontfaces fontfaces;
    private DocInfo hwpDocInfo;

    public ForFontfaces(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList, DocInfo hwpDocInfo) {
        this.hwpDocInfo = hwpDocInfo;

        refList.createFontfaces();
        fontfaces = parameter.hwpx().headerXMLFile().refList().fontfaces();

        hangulFontface();
        latinFontface();
        hanjaFontface();
        japaneseFontface();
        otherFontface();
        symbolFontface();
        userFontface();
    }

    private void hangulFontface() {
        Fontface fontface = fontfaces.addNewFontface()
                .langAnd(LanguageType.HANGUL);

        int id = 0;
        for(FaceName hwpFaceName : hwpDocInfo.getHangulFaceNameList()) {
            ForFont.convert(
                    fontface.addNewFont().idAnd(String.valueOf(id)),
                    hwpFaceName);
            id++;
        }
    }

    private void latinFontface() {
        Fontface fontface = fontfaces.addNewFontface()
                .langAnd(LanguageType.LATIN);

        int id = 0;
        for(FaceName hwpFaceName : hwpDocInfo.getEnglishFaceNameList()) {
            ForFont.convert(
                    fontface.addNewFont().idAnd(String.valueOf(id)),
                    hwpFaceName);
            id++;
        }
    }

    private void hanjaFontface() {
        Fontface fontface = fontfaces.addNewFontface()
                .langAnd(LanguageType.HANJA);

        int id = 0;
        for(FaceName hwpFaceName : hwpDocInfo.getHanjaFaceNameList()) {
            ForFont.convert(
                    fontface.addNewFont().idAnd(String.valueOf(id)),
                    hwpFaceName);
            id++;
        }
    }

    private void japaneseFontface() {
        Fontface fontface = fontfaces.addNewFontface()
                .langAnd(LanguageType.JAPANESE);

        int id = 0;
        for(FaceName hwpFaceName : hwpDocInfo.getJapaneseFaceNameList()) {
            ForFont.convert(
                    fontface.addNewFont().idAnd(String.valueOf(id)),
                    hwpFaceName);
            id++;
        }
    }

    private void otherFontface() {
        Fontface fontface = fontfaces.addNewFontface()
                .langAnd(LanguageType.OTHER);

        int id = 0;
        for(FaceName hwpFaceName : hwpDocInfo.getEtcFaceNameList()) {
            ForFont.convert(
                    fontface.addNewFont().idAnd(String.valueOf(id)),
                    hwpFaceName);
            id++;
        }
    }

    private void symbolFontface() {
        Fontface fontface = fontfaces.addNewFontface()
                .langAnd(LanguageType.SYMBOL);

        int id = 0;
        for(FaceName hwpFaceName : hwpDocInfo.getSymbolFaceNameList()) {
            ForFont.convert(
                    fontface.addNewFont().idAnd(String.valueOf(id)),
                    hwpFaceName);
            id++;
        }
    }

    private void userFontface() {
        Fontface fontface = fontfaces.addNewFontface()
                .langAnd(LanguageType.USER);

        int id = 0;
        for(FaceName hwpFaceName : hwpDocInfo.getUserFaceNameList()) {
            ForFont.convert(
                    fontface.addNewFont().idAnd(String.valueOf(id)),
                    hwpFaceName);
            id++;
        }
    }
}