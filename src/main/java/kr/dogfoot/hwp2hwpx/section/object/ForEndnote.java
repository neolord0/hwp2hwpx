package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlEndnote;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.EndNote;

public class ForEndnote extends Converter {
    public ForEndnote(Parameter parameter) {
        super(parameter);
    }

    public void convert(EndNote endnote, ControlEndnote hwpEndnote) {
        endnote
                .numberAnd((int) hwpEndnote.getHeader().getNumber())
                .suffixCharAnd(ValueConvertor.stringNullCheck(hwpEndnote.getHeader().getAfterDecorationLetter().toUTF16LEString()))
                .instId(String.valueOf(ValueConvertor.toUnsigned(hwpEndnote.getHeader().getInstanceId())));

        parameter.subListConverter().convertForEndnote(endnote, hwpEndnote);
    }
}
