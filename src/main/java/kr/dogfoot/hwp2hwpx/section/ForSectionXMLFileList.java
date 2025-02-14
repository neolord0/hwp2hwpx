package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwpxlib.object.content.section_xml.SectionXMLFile;

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
    }

    private void section() {
        for (Paragraph hwpPara : hwpSection.getParagraphs()) {
            forPara.convert(sectionXMLFile.addNewPara(), hwpPara);
        }
    }
}
