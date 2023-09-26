package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwpxlib.object.content.section_xml.SectionXMLFile;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Para;

import java.util.ArrayList;

public class ForSectionXMLFileList extends Converter {
    private SectionXMLFile sectionXMLFile;
    private Section hwpSection;
    private ForPara forPara;

    public ForSectionXMLFileList(Parameter parameter) {
        super(parameter);
        forPara = new ForPara(parameter);
    }

    public void convert() {
        for (Section section : parameter.hwp().bodyText().getSectionList()) {
            sectionXMLFile = parameter.hwpx().sectionXMLFileList().addNew();
            hwpSection = section;
            section();
        }
        setFirstParaId();
    }

    private void setFirstParaId() {
        if (parameter.hwpx().sectionXMLFileList().count() > 0) {
            SectionXMLFile firstSection = parameter.hwpx().sectionXMLFileList().get(0);
            if (firstSection.countOfPara() > 0) {
                firstSection.getPara(0).id("2764991984");
            }
        }
    }

    private void section() {
        for(Paragraph hwpPara : hwpSection.getParagraphs()) {
            forPara.convert(sectionXMLFile.addNewPara(), hwpPara);
        }
    }
}
