package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlHeader;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.Header;

public class ForHeader extends Converter {
    public ForHeader(Parameter parameter) {
        super(parameter);
    }

    public void convert(Header header, ControlHeader hwpHeader) {
        header
                .idAnd(String.valueOf(hwpHeader.getHeader().getCreateIndex()))
                .applyPageType(ValueConvertor.toApplyPageType(hwpHeader.getHeader().getApplyPage()));

        parameter.subListConverter().convertForHeader(header, hwpHeader);
    }
}
