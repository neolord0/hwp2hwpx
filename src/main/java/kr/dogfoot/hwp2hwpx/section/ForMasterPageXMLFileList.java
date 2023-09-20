package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.BatangPageInfo;
import kr.dogfoot.hwpxlib.object.content.masterpage_xml.MasterPageXMLFile;

public class ForMasterPageXMLFileList extends Converter {
    public ForMasterPageXMLFileList(Parameter parameter) {
        super(parameter);
    }

    public void convert() {
        for (BatangPageInfo hwpBatangPageInfo : parameter.masterPageIdMap().keySet()) {
            MasterPageXMLFile masterPageXMLFile = parameter.hwpx().masterPageXMLFileList().addNew();
        }
    }
}
