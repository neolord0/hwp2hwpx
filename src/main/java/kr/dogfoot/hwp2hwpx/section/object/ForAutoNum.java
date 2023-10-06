package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlAutoNumber;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.AutoNum;

public class ForAutoNum {
    public static void convert(AutoNum autoNum, ControlAutoNumber hwpAutoNumber) {
        autoNum
                .numAnd(hwpAutoNumber.getHeader().getNumber())
                .numType(ValueConvertor.toNumType(hwpAutoNumber.getHeader().getProperty().getNumberSort()));

        autoNum.createAutoNumFormat();
        autoNum.autoNumFormat()
                .typeAnd(ValueConvertor.toNumberType2(hwpAutoNumber.getHeader().getProperty().getNumberShape()))
                .userCharAnd(ValueConvertor.stringNullCheck(hwpAutoNumber.getHeader().getUserSymbol().toUTF16LEString()))
                .prefixCharAnd(ValueConvertor.stringNullCheck(hwpAutoNumber.getHeader().getBeforeDecorationLetter().toUTF16LEString()))
                .suffixCharAnd(ValueConvertor.stringNullCheck(hwpAutoNumber.getHeader().getAfterDecorationLetter().toUTF16LEString()))
                .supscript(hwpAutoNumber.getHeader().getProperty().isSuperScript());
    }
}
