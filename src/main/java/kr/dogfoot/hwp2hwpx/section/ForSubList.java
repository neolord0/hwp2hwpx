package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.*;
import kr.dogfoot.hwplib.object.bodytext.control.gso.textbox.LineChange;
import kr.dogfoot.hwplib.object.bodytext.control.gso.textbox.TextBox;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.BatangPageInfo;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.ParagraphList;
import kr.dogfoot.hwpxlib.object.content.masterpage_xml.MasterPageXMLFile;
import kr.dogfoot.hwpxlib.object.content.section_xml.SubList;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.LineWrapMethod;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.TextDirection;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.*;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.drawingobject.DrawText;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.shapeobject.Caption;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.table.Tc;

public class ForSubList extends Converter {
    private ForPara paraConverter;

    public ForSubList(Parameter parameter) {
        super(parameter);
        paraConverter = new ForPara(parameter);
    }

    public void convertForCaption(Caption caption, kr.dogfoot.hwplib.object.bodytext.control.gso.caption.Caption hwpCaption) {
        caption.createSubList();
        caption.subList()
                .idAnd("")
                .textDirectionAnd(textDirection(hwpCaption.getListHeader().getProperty().getTextDirection()))
                .lineWrapAnd(lineWrapMethod(hwpCaption.getListHeader().getProperty().getLineChange()))
                .vertAlignAnd(ValueConvertor.verticalAlign2(hwpCaption.getListHeader().getProperty().getTextVerticalAlignment()))
                .linkListIDRefAnd(String.valueOf(0))
                .linkListNextIDRefAnd(String.valueOf(0))
                .textWidthAnd((int) hwpCaption.getListHeader().getTextWidth())
                .textHeightAnd(0)
                .hasTextRefAnd(false)
                .hasNumRef(false);
        // todo : linkListIDRef, linkListNextIDRef, textHeight, hasTextRef, hasNumRef ??

        paraList(caption.subList(), hwpCaption.getParagraphList());
    }

    private void paraList(SubList subList, ParagraphList paragraphList) {
        for (Paragraph hwpPara : paragraphList) {
            paraConverter.convert(subList.addNewPara(), hwpPara);
        }
    }

    private TextDirection textDirection(kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.sectiondefine.TextDirection hwpTextDirection) {
        switch (hwpTextDirection) {
            case Horizontal:
                return TextDirection.HORIZONTAL;
            case VerticalWithEnglishLayDown:
                return TextDirection.VERTICAL;
            case VerticalWithEnglishStanding:
                return TextDirection.VERTICALALL;
        }
        return TextDirection.HORIZONTAL;
    }

    private LineWrapMethod lineWrapMethod(LineChange lineChange) {
        switch (lineChange) {
            case Normal:
                return LineWrapMethod.BREAK;
            case KeepOneLineByAdjustWordSpace:
                return LineWrapMethod.SQUEEZE;
            case IncreaseWidthByContent:
                return LineWrapMethod.KEEP;
        }
        return LineWrapMethod.BREAK;
    }

    public void convertForCell(Tc tc, Cell hwpCell) {
        tc.createSubList();
        tc.subList()
                .idAnd("")
                .textDirectionAnd(textDirection(hwpCell.getListHeader().getProperty().getTextDirection()))
                .lineWrapAnd(lineWrapMethod(hwpCell.getListHeader().getProperty().getLineChange()))
                .vertAlignAnd(ValueConvertor.verticalAlign2(hwpCell.getListHeader().getProperty().getTextVerticalAlignment()))
                .linkListIDRefAnd(String.valueOf(0))
                .linkListNextIDRefAnd(String.valueOf(0))
                .textWidthAnd((int) hwpCell.getListHeader().getTextWidth())
                .textHeightAnd(0)
                .hasTextRefAnd(false)
                .hasNumRef(false);
        // todo : linkListIDRef, linkListNextIDRef, textHeight, hasTextRef, hasNumRef ??

        paraList(tc.subList(), hwpCell.getParagraphList());
    }

    public void convertForHeader(Header header, ControlHeader hwpHeader) {
        header.createSubList();
        header.subList()
                .idAnd("")
                .textDirectionAnd(textDirection(hwpHeader.getListHeader().getProperty().getTextDirection()))
                .lineWrapAnd(lineWrapMethod(hwpHeader.getListHeader().getProperty().getLineChange()))
                .vertAlignAnd(ValueConvertor.verticalAlign2(hwpHeader.getListHeader().getProperty().getTextVerticalAlignment()))
                .linkListIDRefAnd(String.valueOf(0))
                .linkListNextIDRefAnd(String.valueOf(0))
                .textWidthAnd((int) hwpHeader.getListHeader().getTextWidth())
                .textHeightAnd((int) hwpHeader.getListHeader().getTextHeight())
                .hasTextRefAnd(false)
                .hasNumRef(false);
        // todo : linkListIDRef, linkListNextIDRef, hasTextRef, hasNumRef ??

        paraList(header.subList(), hwpHeader.getParagraphList());
    }

    public void convertForFooter(Footer footer, ControlFooter hwpFooter) {
        footer.createSubList();
        footer.subList()
                .idAnd("")
                .textDirectionAnd(textDirection(hwpFooter.getListHeader().getProperty().getTextDirection()))
                .lineWrapAnd(lineWrapMethod(hwpFooter.getListHeader().getProperty().getLineChange()))
                .vertAlignAnd(ValueConvertor.verticalAlign2(hwpFooter.getListHeader().getProperty().getTextVerticalAlignment()))
                .linkListIDRefAnd(String.valueOf(0))
                .linkListNextIDRefAnd(String.valueOf(0))
                .textWidthAnd((int) hwpFooter.getListHeader().getTextWidth())
                .textHeightAnd((int) hwpFooter.getListHeader().getTextHeight())
                .hasTextRefAnd(false)
                .hasNumRef(false);
        // todo : linkListIDRef, linkListNextIDRef, hasTextRef, hasNumRef ??

        paraList(footer.subList(), hwpFooter.getParagraphList());
    }

    public void convertForFootnote(FootNote footnote, ControlFootnote hwpFootnote) {
        footnote.createSubList();
        footnote.subList()
                .idAnd("")
                .textDirectionAnd(textDirection(hwpFootnote.getListHeader().getProperty().getTextDirection()))
                .lineWrapAnd(lineWrapMethod(hwpFootnote.getListHeader().getProperty().getLineChange()))
                .vertAlignAnd(ValueConvertor.verticalAlign2(hwpFootnote.getListHeader().getProperty().getTextVerticalAlignment()))
                .linkListIDRefAnd(String.valueOf(0))
                .linkListNextIDRefAnd(String.valueOf(0))
                .textWidthAnd(0)
                .textHeightAnd(0)
                .hasTextRefAnd(false)
                .hasNumRef(false);
        // todo : linkListIDRef, linkListNextIDRef, hasTextRef, hasNumRef ??

        paraList(footnote.subList(), hwpFootnote.getParagraphList());
    }

    public void convertForEndnote(EndNote endnote, ControlEndnote hwpEndnote) {
        endnote.createSubList();
        endnote.subList()
                .idAnd("")
                .textDirectionAnd(textDirection(hwpEndnote.getListHeader().getProperty().getTextDirection()))
                .lineWrapAnd(lineWrapMethod(hwpEndnote.getListHeader().getProperty().getLineChange()))
                .vertAlignAnd(ValueConvertor.verticalAlign2(hwpEndnote.getListHeader().getProperty().getTextVerticalAlignment()))
                .linkListIDRefAnd(String.valueOf(0))
                .linkListNextIDRefAnd(String.valueOf(0))
                .textWidthAnd(0)
                .textHeightAnd(0)
                .hasTextRefAnd(false)
                .hasNumRef(false);
        // todo : linkListIDRef, linkListNextIDRef, hasTextRef, hasNumRef ??

        paraList(endnote.subList(), hwpEndnote.getParagraphList());
    }

    public void convertForHiddenComment(HiddenComment hiddenComment, ControlHiddenComment hwpHiddenComment) {
        hiddenComment.createSubList();
        hiddenComment.subList()
                .idAnd("")
                .textDirectionAnd(textDirection(hwpHiddenComment.getListHeader().getProperty().getTextDirection()))
                .lineWrapAnd(lineWrapMethod(hwpHiddenComment.getListHeader().getProperty().getLineChange()))
                .vertAlignAnd(ValueConvertor.verticalAlign2(hwpHiddenComment.getListHeader().getProperty().getTextVerticalAlignment()))
                .linkListIDRefAnd(String.valueOf(0))
                .linkListNextIDRefAnd(String.valueOf(0))
                .textWidthAnd(0)
                .textHeightAnd(0)
                .hasTextRefAnd(false)
                .hasNumRef(false);
        // todo : linkListIDRef, linkListNextIDRef,  hasTextRef, hasNumRef ??

        paraList(hiddenComment.subList(), hwpHiddenComment.getParagraphList());
    }

    public void convertForDrawText(DrawText drawText, TextBox hwpTextBox) {
        drawText.createSubList();
        drawText.subList()
                .idAnd("")
                .textDirectionAnd(textDirection(hwpTextBox.getListHeader().getProperty().getTextDirection()))
                .lineWrapAnd(lineWrapMethod(hwpTextBox.getListHeader().getProperty().getLineChange()))
                .vertAlignAnd(ValueConvertor.verticalAlign2(hwpTextBox.getListHeader().getProperty().getTextVerticalAlignment()))
                .linkListIDRefAnd(String.valueOf(0))
                .linkListNextIDRefAnd(String.valueOf(0))
                .textWidthAnd(0)
                .textHeightAnd(0)
                .hasTextRefAnd(false)
                .hasNumRef(false);
        // todo : linkListIDRef, linkListNextIDRef,  hasTextRef, hasNumRef ??

        paraList(drawText.subList(), hwpTextBox.getParagraphList());
    }

    public void convertForMasterPage(MasterPageXMLFile masterPageXMLFile, BatangPageInfo hwpBatangPageInfo) {
        masterPageXMLFile.createSubList();
        masterPageXMLFile.subList()
                .idAnd("")
                .textDirectionAnd(textDirection(hwpBatangPageInfo.getListHeader().getProperty().getTextDirection()))
                .lineWrapAnd(lineWrapMethod(hwpBatangPageInfo.getListHeader().getProperty().getLineChange()))
                .vertAlignAnd(ValueConvertor.verticalAlign2(hwpBatangPageInfo.getListHeader().getProperty().getTextVerticalAlignment()))
                .linkListIDRefAnd(String.valueOf(0))
                .linkListNextIDRefAnd(String.valueOf(0))
                .textWidthAnd((int) hwpBatangPageInfo.getListHeader().getTextWidth())
                .textHeightAnd((int) hwpBatangPageInfo.getListHeader().getTextHeight())
                .hasTextRefAnd(false)
                .hasNumRef(false);

        paraList(masterPageXMLFile.subList(), hwpBatangPageInfo.getParagraphList());
    }
}
