package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.HWPCharControlInline;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.FieldBegin;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.FieldEnd;

public class ForInlineControl extends Converter {
    public ForInlineControl(Parameter parameter) {
        super(parameter);
    }

    public void fieldEnd(FieldEnd fieldEnd, HWPCharControlInline hwpChar) {
        FieldBegin fieldBegin = parameter.fieldBeginStack().pop();

        fieldEnd
                .beginIDRefAnd(fieldBegin.id())
                .fieldid(fieldBegin.fieldid());
    }
}
