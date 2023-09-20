package kr.dogfoot.hwp2hwpx.header.inner;

import kr.dogfoot.hwp2hwpx.util.HWPUtil;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.docinfo.FaceName;
import kr.dogfoot.hwplib.object.docinfo.facename.FontTypeInfo;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.FontFamilyType;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.FontType;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.fontface.Font;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.fontface.TypeInfo;

public class ForFont {
    public static void convert(Font font, FaceName hwpFaceName) {
        font
                .faceAnd(hwpFaceName.getName())
                .typeAnd(fontType(hwpFaceName.getProperty().getType()))
                .isEmbedded(false);

        if (hwpFaceName.getProperty().hasFontInfo()) {
            font.createTypeInfo();
            typeInfo(font.typeInfo(), hwpFaceName.getFontTypeInfo());
        }

        if (hwpFaceName.getProperty().hasSubstituteFont()) {
            // TODO: 대체폰트 정보
        }
    }

    private static FontType fontType(kr.dogfoot.hwplib.object.docinfo.facename.FontType hwpFontType) {
        switch (hwpFontType) {
            case Unknown:
                return FontType.REP;
            case TTF:
                return FontType.TTF;
            case HFT:
                return FontType.HFT;
        }
        return FontType.REP;
    }

    private static void typeInfo(TypeInfo typeInfo, FontTypeInfo hwpFontTypeInfo) {
        typeInfo
                .familyTypeAnd(ValueConvertor.toFontFamilyType(hwpFontTypeInfo.getFontType()))
                .weightAnd((int) hwpFontTypeInfo.getThickness())
                .proportionAnd((int) hwpFontTypeInfo.getRatio())
                .contrastAnd((int) hwpFontTypeInfo.getContrast())
                .strokeVariationAnd((int) hwpFontTypeInfo.getStrokeDeviation())
                .armStyleAnd(ValueConvertor.toBoolean(hwpFontTypeInfo.getStrokeDeviation()))
                .letterformAnd(ValueConvertor.toBoolean(hwpFontTypeInfo.getCharacterShape()))
                .midlineAnd((int) hwpFontTypeInfo.getMiddleLine())
                .xHeight((int) hwpFontTypeInfo.getxHeight());
    }
}
