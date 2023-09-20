package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.docinfo.DocInfo;
import kr.dogfoot.hwplib.object.docinfo.documentproperties.StartNumber;
import kr.dogfoot.hwpxlib.object.content.header_xml.CompatibleDocument;
import kr.dogfoot.hwpxlib.object.content.header_xml.DocOption;
import kr.dogfoot.hwpxlib.object.content.header_xml.HeaderXMLFile;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.TargetProgramSort;

public class ForHeaderXMLFile extends Converter {
    private HeaderXMLFile headerXMLFile;
    private DocInfo hwpDocInfo;

    public ForHeaderXMLFile(Parameter parameter) {
        super(parameter);
    }

    public void convert() {
        headerXMLFile = parameter.hwpx().headerXMLFile();
        hwpDocInfo = parameter.hwp().docInfo();

        headerXMLFile
                .versionAnd("1.4")
                .secCnt((short) hwpDocInfo.getDocumentProperties().getSectionCount());

        beginNum();
        refList();
        compatibleDocument();
        docOption();
        trackChangeConfig();
    }

    private void beginNum() {
        StartNumber hwpStartNumber = hwpDocInfo.getDocumentProperties().getStartNumber();

        headerXMLFile.createBeginNum();
        headerXMLFile.beginNum()
                .pageAnd(hwpStartNumber.getPage())
                .footnoteAnd(hwpStartNumber.getFootnote())
                .endnoteAnd(hwpStartNumber.getEndnote())
                .picAnd(hwpStartNumber.getPicture())
                .tblAnd(hwpStartNumber.getTable())
                .equation(hwpStartNumber.getEquation());
    }

    private void refList() {
        headerXMLFile.createRefList();

        new ForFontfaces(parameter).convert(headerXMLFile.refList(), hwpDocInfo);
        new ForBorderFills(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getBorderFillList());
        new ForCharProperties(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getCharShapeList());
        new ForTabProperties(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getTabDefList());
        new ForNumberings(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getNumberingList());
        new ForBullets(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getBulletList());
        new ForParaProperties(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getParaShapeList());
        new ForStyles(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getStyleList());
        new ForMemoProperties(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getMemoShapeList());
        new ForTrackChanges(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getTrackChange2List());
        new ForTrackChangeAuthors(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getTrackChangeAuthorList());
    }

    private void compatibleDocument() {
        headerXMLFile.createCompatibleDocument();
        CompatibleDocument compatibleDocument = headerXMLFile.compatibleDocument();

        compatibleDocument.targetProgram(TargetProgramSort.HWP201X);
        compatibleDocument.createLayoutCompatibility();
    }

    private void docOption() {
        headerXMLFile.createDocOption();
        DocOption docOption = headerXMLFile.docOption();
        docOption.createLinkinfo();
        docOption.linkinfo()
                .pathAnd("")
                .pageInheritAnd(false)
                .footnoteInherit(false);
    }

    private void trackChangeConfig() {
        headerXMLFile.createTrackChangeConfig();
        headerXMLFile.trackChangeConfig().flags(56);
    }
}
