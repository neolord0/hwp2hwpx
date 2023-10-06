package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlAdditionalText;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.additionaltext.AdditionalTextPosition;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.DutmalPosType;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Dutmal;

public class ForDutmal {
    public static void convert(Dutmal dutmal, ControlAdditionalText hwpAdditionalText) {
        dutmal
                .posTypeAnd(dutmalPosType(hwpAdditionalText.getHeader().getPosition()))
                .szRatioAnd((int) hwpAdditionalText.getHeader().getFsizeratio())
                .optionAnd((int) hwpAdditionalText.getHeader().getOption())
                .styleIDRefAnd(ValueConvertor.refID(hwpAdditionalText.getHeader().getStyleId()))
                .align(ValueConvertor.horizontalAlign2(hwpAdditionalText.getHeader().getAlignment()));

        dutmal.createMainText();
        dutmal.mainText().addText(hwpAdditionalText.getHeader().getMainText().toUTF16LEString());

        dutmal.createSubText();
        dutmal.subText().addText(hwpAdditionalText.getHeader().getSubText().toUTF16LEString());
    }

    private static DutmalPosType dutmalPosType(AdditionalTextPosition hwpAdditionalTextPosition) {
        switch (hwpAdditionalTextPosition) {
            case Top:
                return DutmalPosType.TOP;
            case Bottom:
                return DutmalPosType.BOTTOM;
            case Center:
                return DutmalPosType.TOP;
        }
        return DutmalPosType.TOP;
    }
}
