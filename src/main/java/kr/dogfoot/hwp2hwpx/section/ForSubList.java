package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.gso.textbox.LineChange;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.ParagraphList;
import kr.dogfoot.hwpxlib.object.content.section_xml.SubList;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.LineWrapMethod;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.TextDirection;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.shapeobject.Caption;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.table.Tc;

public class ForSubList extends Converter {
    private ForPara forPara;

    public ForSubList(Parameter parameter) {
        super(parameter);
        forPara = new ForPara(parameter);
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
            forPara.convert(subList.addNewPara(), hwpPara);
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
}
