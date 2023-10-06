package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlNewNumber;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.NewNum;

public class ForNewNum {
    public static void convert(NewNum newNum, ControlNewNumber hwpNewNumber) {
        newNum
                .numAnd(hwpNewNumber.getHeader().getNumber())
                .numType(ValueConvertor.toNumType(hwpNewNumber.getHeader().getProperty().getNumberSort()));
    }
}
