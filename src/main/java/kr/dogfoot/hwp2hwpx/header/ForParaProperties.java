package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.docinfo.ParaShape;
import kr.dogfoot.hwplib.object.docinfo.parashape.LineDivideForEnglish;
import kr.dogfoot.hwplib.object.docinfo.parashape.LineDivideForHangul;
import kr.dogfoot.hwplib.object.docinfo.parashape.LineSpaceSort;
import kr.dogfoot.hwplib.object.docinfo.parashape.ParaHeadShape;
import kr.dogfoot.hwpxlib.object.common.compatibility.Case;
import kr.dogfoot.hwpxlib.object.common.compatibility.Default;
import kr.dogfoot.hwpxlib.object.common.compatibility.Switch;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.*;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.ParaPr;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.parapr.LineSpacing;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.parapr.ParaMargin;

import java.util.ArrayList;

public class ForParaProperties extends Converter {
    private ParaPr paraPr;
    private ParaShape hwpParaShape;

    public ForParaProperties(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList, ArrayList<ParaShape> hwpParaShapeList) {
        if (hwpParaShapeList.size() == 0) return;

        refList.createParaProperties();

        int id = 0;
        for (ParaShape item : hwpParaShapeList) {
            paraPr = refList.paraProperties().addNew().idAnd(String.valueOf(id));
            hwpParaShape = item;

            paraPr();
            id++;
        }

    }

    private void paraPr() {
        paraPr
                .tabPrIDRefAnd(String.valueOf(hwpParaShape.getTabDefId()))
                .condenseAnd(hwpParaShape.getProperty1().getMinimumSpace())
                .fontLineHeightAnd(hwpParaShape.getProperty1().isLineHeightForFont())
                .snapToGridAnd(hwpParaShape.getProperty1().isUseGrid())
                .suppressLineNumbersAnd(hwpParaShape.getProperty2().isSuppressLineNumbers())
                .checked(false);
        // todo: checked ??
        align();
        heading();
        breakSetting();
        switchForMarginAndLineSpacing();
        border();
        autoSpacing();
    }



    private void align() {
        paraPr.createAlign();
        paraPr.align()
                .horizontalAnd(ValueConvertor.horizontalAlign2(hwpParaShape.getProperty1().getAlignment()))
                .vertical(ValueConvertor.verticalAlign1(hwpParaShape.getProperty1().getVerticalAlignment()));
    }

    private void heading() {
        paraPr.createHeading();
        paraPr.heading()
                .typeAnd(headingType(hwpParaShape.getProperty1().getParaHeadShape()))
                .idRefAnd(String.valueOf(hwpParaShape.getParaHeadId()))
                .level(hwpParaShape.getProperty1().getParaLevel());
    }

    private ParaHeadingType headingType(ParaHeadShape hwpHeadShape) {
        switch (hwpHeadShape) {
            case None:
                return ParaHeadingType.NONE;
            case Outline:
                return ParaHeadingType.OUTLINE;
            case Numbering:
                return ParaHeadingType.NUMBER;
            case Bullet:
                return ParaHeadingType.BULLET;
        }
        return ParaHeadingType.NONE;
    }

    private void breakSetting() {
        paraPr.createBreakSetting();
        paraPr.breakSetting()
                .breakLatinWordAnd(breakLatinWord(hwpParaShape.getProperty1().getLineDivideForEnglish()))
                .breakNonLatinWordAnd(breakNonLatinWord(hwpParaShape.getProperty1().getLineDivideForHangul()))
                .widowOrphanAnd(hwpParaShape.getProperty1().isProtectLoner())
                .keepWithNextAnd(hwpParaShape.getProperty1().isTogetherNextPara())
                .keepLinesAnd(hwpParaShape.getProperty1().isProtectPara())
                .pageBreakBeforeAnd(hwpParaShape.getProperty1().isSplitPageBeforePara())
                .lineWrap(lineWrap(hwpParaShape.getProperty2().isInputSingleLine()));
    }

    private LineBreakForLatin breakLatinWord(LineDivideForEnglish hwpLineDivideForEnglish) {
        switch (hwpLineDivideForEnglish) {
            case ByWord:
                return LineBreakForLatin.KEEP_WORD;
            case ByHypen:
                return LineBreakForLatin.HYPHENATION;
            case ByLetter:
                return LineBreakForLatin.BREAK_WORD;
        }
        return LineBreakForLatin.BREAK_WORD;
    }

    private LineBreakForNonLatin breakNonLatinWord(LineDivideForHangul hwpLineDivideForHangul) {
        switch (hwpLineDivideForHangul) {
            case ByLetter:
                return LineBreakForNonLatin.KEEP_WORD;
            case ByWord:
                return LineBreakForNonLatin.BREAK_WORD;
        }
        return LineBreakForNonLatin.BREAK_WORD;
    }

    private LineWrap lineWrap(boolean hwpInputSingleLine) {
        if (hwpInputSingleLine) {
            return LineWrap.SQUEEZE;
        } else {
            return LineWrap.BREAK;
        }
    }


    private void switchForMarginAndLineSpacing() {
        Switch switchObject = paraPr.addNewSwitch();
        switchObject.position(5);

        {
            Case caseObject = switchObject.addNewCaseObject()
                    .requiredNamespaceAnd("http://www.hancom.co.kr/hwpml/2016/HwpUnitChar");

            caseObject.addChild(margin(true));
            caseObject.addChild(lineSpacing(true));
        }
        {
            switchObject.createDefaultObject();
            Default defaultObject = switchObject.defaultObject();
            defaultObject.addChild(margin(false));
            defaultObject.addChild(lineSpacing(false));
        }
    }

    private ParaMargin margin(boolean inCase) {
        ParaMargin margin = new ParaMargin();

        margin.createIntent();
        margin.intent()
                .valueAnd((inCase == true) ? hwpParaShape.getIndent() / 2 : hwpParaShape.getIndent())
                .unit(ValueUnit2.HWPUNIT);

        margin.createLeft();
        margin.left()
                .valueAnd((inCase == true) ? hwpParaShape.getLeftMargin() / 2 : hwpParaShape.getLeftMargin())
                .unit(ValueUnit2.HWPUNIT);

        margin.createRight();
        margin.right()
                .valueAnd((inCase == true) ? hwpParaShape.getRightMargin() / 2 : hwpParaShape.getRightMargin())
                .unit(ValueUnit2.HWPUNIT);

        margin.createPrev();
        margin.prev()
                .valueAnd((inCase == true) ? hwpParaShape.getTopParaSpace() / 2 : hwpParaShape.getTopParaSpace())
                .unit(ValueUnit2.HWPUNIT);

        margin.createNext();
        margin.next()
                .valueAnd((inCase == true) ? hwpParaShape.getBottomParaSpace() / 2 : hwpParaShape.getBottomParaSpace())
                .unit(ValueUnit2.HWPUNIT);

        return margin;
    }

    private LineSpacing lineSpacing(boolean inCase) {
        LineSpacing lineSpacing = new LineSpacing()
                .typeAnd(lineSpacingType(hwpParaShape.getProperty1().getLineSpaceSort()))
                .unitAnd(ValueUnit2.HWPUNIT);

        if (inCase == true && hwpParaShape.getProperty1().getLineSpaceSort() != LineSpaceSort.RatioForLetter) {
            lineSpacing.value((int) (hwpParaShape.getLineSpace2() / 2));
        } else {
            lineSpacing.value((int) hwpParaShape.getLineSpace2());
        }
        return lineSpacing;
    }

    private LineSpacingType lineSpacingType(LineSpaceSort hwpLineSpaceSort) {
        switch (hwpLineSpaceSort) {
            case RatioForLetter:
                return LineSpacingType.PERCENT;
            case FixedValue:
                return LineSpacingType.PERCENT;
            case OnlyMargin:
                return LineSpacingType.BETWEEN_LINES;
            case AtLeast:
                return LineSpacingType.AT_LEAST;
        }
        return LineSpacingType.PERCENT;
    }

    private void border() {
        paraPr.createBorder();
        paraPr.border()
                .borderFillIDRefAnd(String.valueOf(hwpParaShape.getBorderFillId()))
                .offsetLeftAnd((int) hwpParaShape.getLeftBorderSpace())
                .offsetRightAnd((int) hwpParaShape.getRightBorderSpace())
                .offsetTopAnd((int) hwpParaShape.getTopBorderSpace())
                .offsetBottomAnd((int) hwpParaShape.getBottomBorderSpace())
                .connectAnd(hwpParaShape.getProperty1().isLinkBorder())
                .ignoreMargin(hwpParaShape.getProperty1().isIgnoreParaMargin());
    }


    private void autoSpacing() {
        paraPr.createAutoSpacing();
        paraPr.autoSpacing()
                .eAsianEngAnd(hwpParaShape.getProperty2().isAutoAdjustGapHangulEnglish())
                .eAsianNum(hwpParaShape.getProperty2().isAutoAdjustGapHangulNumber());
    }
}
