package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlOverlappingLetter;
import kr.dogfoot.hwplib.object.etc.HWPString;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.ComposeCircleType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.ComposeType;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Compose;

public class ForCompose {
    public static void convert(Compose compose, ControlOverlappingLetter hwpOverlappingLetter) {
        compose
                .circleTypeAnd(composeCircleType(firstChar(hwpOverlappingLetter)))
                .charSzAnd((short) hwpOverlappingLetter.getHeader().getInternalFontSize())
                .composeType(composeType(hwpOverlappingLetter.getHeader().getExpendInsideLetter()));
        composeText(compose, hwpOverlappingLetter);
        charPrs(compose, hwpOverlappingLetter);
    }

    private static String firstChar(ControlOverlappingLetter hwpOverlappingLetter) {
        return hwpOverlappingLetter.getHeader().getOverlappingLetterList().get(0).toUTF16LEString();
    }

    private static ComposeCircleType composeCircleType(String firstChar) {
        switch (firstChar) {
            case "◯":
                return ComposeCircleType.SHAPE_CIRCLE;
            case "●":
                return ComposeCircleType.SHAPE_REVERSAL_CIRCLE;
            case "□":
                return ComposeCircleType.SHAPE_RECTANGLE;
            case "■":
                return ComposeCircleType.SHAPE_REVERSAL_RECTANGLE;
            case "△":
                return ComposeCircleType.SHAPE_TRIANGLE;
            case "▲":
                return ComposeCircleType.SHAPE_REVERSAL_TIRANGLE;
            case "☼":
                return ComposeCircleType.SHAPE_LIGHT;
            case "◇":
                return ComposeCircleType.SHAPE_RHOMBUS;
            case "◆":
                return ComposeCircleType.SHAPE_REVERSAL_RHOMBUS;
            case "▢":
                return ComposeCircleType.SHAPE_ROUNDED_RECTANGLE;
            case "♲":
                return ComposeCircleType.SHAPE_EMPTY_CIRCULATE_TRIANGLE;
            case "♺":
                return ComposeCircleType.SHAPE_THIN_CIRCULATE_TRIANGLE;
            case "♻":
                return ComposeCircleType.SHAPE_THICK_CIRCULATE_TRIANGLE;
        }
        return ComposeCircleType.CHAR;
    }

    private static ComposeType composeType(short expendInsideLetter) {
        switch (expendInsideLetter) {
            case 0:
                return ComposeType.SPREAD;
            case 1:
                return ComposeType.OVERLAP;
        }
        return ComposeType.SPREAD;
    }

    private static void composeText(Compose compose, ControlOverlappingLetter hwpOverlappingLetter) {
        String firstChar = firstChar(hwpOverlappingLetter);

        StringBuilder sb = new StringBuilder();
        if (compose.circleType() == ComposeCircleType.CHAR && !firstChar.equals("　")) {
            for (HWPString hwpStr : hwpOverlappingLetter.getHeader().getOverlappingLetterList()) {
                sb.append(hwpStr.toUTF16LEString());
            }
        } else {
            boolean first = true;
            for (HWPString hwpStr : hwpOverlappingLetter.getHeader().getOverlappingLetterList()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(hwpStr.toUTF16LEString());
                }
            }
        }
        compose.composeText(sb.toString());
    }

    private static void charPrs(Compose compose, ControlOverlappingLetter hwpOverlappingLetter) {
        for (Long id : hwpOverlappingLetter.getHeader().getCharShapeIdList()) {
            compose.addNewCharPr()
                    .prIDRef(ValueConvertor.refID(id));
        }
    }
}
