package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.BatangPageInfo;
import kr.dogfoot.hwpxlib.object.content.masterpage_xml.MasterPageXMLFile;
import kr.dogfoot.hwpxlib.object.content.masterpage_xml.enumtype.MasterPageType;

public class ForMasterPageXMLFileList extends Converter {
    public ForMasterPageXMLFileList(Parameter parameter) {
        super(parameter);
    }

    public void convert() {
        for (BatangPageInfo hwpBatangPageInfo : parameter.masterPageIdMap().keySet()) {
            Parameter.MasterPageInfo masterPageInfo = parameter.masterPageIdMap().get(hwpBatangPageInfo);
            MasterPageXMLFile masterPageXMLFile = parameter.hwpx().masterPageXMLFileList().addNew();

            masterPageXMLFile
                    .idAnd(masterPageInfo.id())
                    .typeAnd(masterPageInfo.type())
                    .pageNumberAnd(0)
                    .pageDuplicateAnd(false)
                    .pageFront(false);
            // todo : pageNumber, pageDuplicate, pageFront ??

            parameter.subListConverter().convertForMasterPage(masterPageXMLFile, hwpBatangPageInfo);
        }
    }
}
