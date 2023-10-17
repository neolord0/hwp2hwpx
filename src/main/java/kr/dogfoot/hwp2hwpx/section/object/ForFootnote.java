package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlFootnote;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.FootNote;

public class ForFootnote extends Converter {
    public ForFootnote(Parameter parameter) {
        super(parameter);
    }

    public void convert(FootNote footnote, ControlFootnote hwpFootnote) {
        footnote
                .numberAnd((int) hwpFootnote.getHeader().getNumber())
                .suffixCharAnd(ValueConvertor.stringNullCheck(hwpFootnote.getHeader().getAfterDecorationLetter().toUTF16LEString()))
                .instId(String.valueOf(ValueConvertor.toUnsigned(hwpFootnote.getHeader().getInstanceId())));

        parameter.subListConverter().convertForFootnote(footnote, hwpFootnote);
    }
}
