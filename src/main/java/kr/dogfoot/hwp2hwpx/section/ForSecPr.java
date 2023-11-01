package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlSectionDefine;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.CtrlHeaderSectionDefine;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.sectiondefine.SectionDefineHeaderProperty;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.sectiondefine.StartPageNumberType;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.*;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.ValueUnit1;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.*;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.secpr.SecPr;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.secpr.pageborder.PageBorderFill;

public class ForSecPr extends Converter {
    private SecPr secPr;
    private ControlSectionDefine hwpSectionDefine;

    public ForSecPr(Parameter parameter) {
        super(parameter);
    }

    public void convert(SecPr secPr, ControlSectionDefine hwpSectionDefine) {
        this.secPr = secPr;
        this.hwpSectionDefine = hwpSectionDefine;

        secPr
                .idAnd("")
                .textDirectionAnd(textDirection(hwpSectionDefine.getHeader().getProperty().getTextDirection()))
                .spaceColumnsAnd(hwpSectionDefine.getHeader().getColumnGap())
                .tabStopAnd((int) hwpSectionDefine.getHeader().getDefaultTabGap())
                .tabStopValAnd((int) (hwpSectionDefine.getHeader().getDefaultTabGap() / 2))
                .tabStopUnitAnd(ValueUnit1.HWPUNIT)
                .outlineShapeIDRefAnd(ValueConvertor.refID(hwpSectionDefine.getHeader().getNumberParaShapeId()))
                .memoShapeIDRefAnd(ValueConvertor.refID(0))
                .textVerticalWidthHead(false);
        // todo : memoShapeIDRef, textVerticalWidthHead ??

        grid();
        startNum();
        visibility();
        lineNumberShape();
        pagePr();
        ForFootNoteEndNotePr.footNotePr(secPr, hwpSectionDefine.getFootNoteShape());
        ForFootNoteEndNotePr.endNotePr(secPr, hwpSectionDefine.getEndNoteShape());
        pageBorderFills();
        masterPages();
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

    private void grid() {
        secPr.createGrid();
        secPr.grid()
                .lineGridAnd(0)
                .charGridAnd(0)
                .wonggojiFormat(hwpSectionDefine.getHeader().getProperty().isApplyWongoji());
        // todo : lineGrid, charGrid ??
    }

    private void startNum() {
        CtrlHeaderSectionDefine hwpHeader = hwpSectionDefine.getHeader();
        secPr.createStartNum();
        secPr.startNum()
                .pageStartsOnAnd(pageStartsOn(hwpHeader.getProperty().getStartPageNumberType()))
                .pageAnd(hwpHeader.getPageStartNumber())
                .picAnd(hwpHeader.getImageStartNumber())
                .tblAnd(hwpHeader.getTableStartNumber())
                .equation(hwpHeader.getEquationStartNumber());
    }

    private PageStartON pageStartsOn(StartPageNumberType hwpType) {
        switch (hwpType) {
            case Continue:
                return PageStartON.BOTH;
            case Even:
                return PageStartON.BOTH;
            case Odd:
                return PageStartON.EVEN;
            case Custom:
                return null;
        }
        return PageStartON.BOTH;
    }


    private void visibility() {
        SectionDefineHeaderProperty hwpProperty = hwpSectionDefine.getHeader().getProperty();

        secPr.createVisibility();
        secPr.visibility()
                .hideFirstHeaderAnd(hwpProperty.isHideHeader())
                .hideFirstFooterAnd(hwpProperty.isHideFooter())
                .hideFirstMasterPageAnd(hwpProperty.isHideBatangPage())
                .borderAnd(pageVisibleOption(hwpProperty.isHideBorder()))
                .fillAnd(pageVisibleOption(hwpProperty.isHideBackground()))
                .hideFirstPageNumAnd(hwpProperty.isHidePageNumberPosition())
                .hideFirstEmptyLineAnd(hwpProperty.isHideEmptyLine())
                .showLineNumber(false);
        // todo: showLineNumber ??
    }

    private VisibilityOption pageVisibleOption(boolean value) {
        return value ? VisibilityOption.HIDE_FIRST : VisibilityOption.SHOW_ALL;
    }


    private void lineNumberShape() {
        // todo : lineNumberShape ??
        secPr.createLineNumberShape();
        secPr.lineNumberShape()
                .restartTypeAnd(LineNumberRestartType.Unknown)
                .countByAnd(0)
                .distanceAnd(0)
                .startNumber(0);
    }

    private void pagePr() {
        PageDef hwpPageDef = hwpSectionDefine.getPageDef();

        secPr.createPagePr();
        secPr.pagePr()
                .landscapeAnd(pageDirection(hwpPageDef.getProperty().getPaperDirection()))
                .widthAnd((int) hwpPageDef.getPaperWidth())
                .heightAnd((int) hwpPageDef.getPaperHeight())
                .gutterType(gutterType(hwpPageDef.getProperty().getMakingBookMethod()));

        secPr.pagePr().createMargin();
        secPr.pagePr().margin()
                .leftAnd((int) hwpPageDef.getLeftMargin())
                .rightAnd((int) hwpPageDef.getRightMargin())
                .topAnd((int) hwpPageDef.getTopMargin())
                .bottomAnd((int) hwpPageDef.getBottomMargin())
                .headerAnd((int) hwpPageDef.getHeaderMargin())
                .footerAnd((int) hwpPageDef.getFooterMargin())
                .gutter((int) hwpPageDef.getGutterMargin());
    }


    private PageDirection pageDirection(PaperDirection hwpPaperDirection) {
        switch (hwpPaperDirection) {
            case Portrait:
                return PageDirection.WIDELY;
            case Landscape:
                return PageDirection.NARROWLY;
        }
        return PageDirection.WIDELY;
    }

    private GutterMethod gutterType(MakingBookMethod hwpMakingBookMethod) {
        switch (hwpMakingBookMethod) {
            case OneSideEditing:
                return GutterMethod.LEFT_ONLY;
            case BothSideEditing:
                return GutterMethod.LEFT_RIGHT;
            case BackFlip:
                return GutterMethod.TOP_BOTTOM;
        }
        return GutterMethod.LEFT_ONLY;
    }


    private void pageBorderFills() {
        pageBorderFill(
                secPr.addNewPageBorderFill().typeAnd(ApplyPageType.BOTH),
                hwpSectionDefine.getBothPageBorderFill());

        pageBorderFill(
                secPr.addNewPageBorderFill().typeAnd(ApplyPageType.EVEN),
                hwpSectionDefine.getEvenPageBorderFill());

        pageBorderFill(
                secPr.addNewPageBorderFill().typeAnd(ApplyPageType.ODD),
                hwpSectionDefine.getOddPageBorderFill());
    }

    private void pageBorderFill(PageBorderFill pageBorderFill, kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.PageBorderFill hwpPageBorderFill) {
        pageBorderFill
                .borderFillIDRefAnd(ValueConvertor.refID(hwpPageBorderFill.getBorderFillId()))
                .textBorderAnd(pageBorderFillPositionCriterion(hwpPageBorderFill.getProperty().getPositionCriterion()))
                .headerInsideAnd(hwpPageBorderFill.getProperty().isIncludeHeader())
                .footerInsideAnd(hwpPageBorderFill.getProperty().isIncludeFooter())
                .fillArea(pageFillArea(hwpPageBorderFill.getProperty().getFillArea()));

        pageBorderFill.createOffset();
        pageBorderFill.offset()
                .leftAnd((long) hwpPageBorderFill.getLeftGap())
                .rightAnd((long) hwpPageBorderFill.getRightGap())
                .topAnd((long) hwpPageBorderFill.getTopGap())
                .bottom((long) hwpPageBorderFill.getBottomGap());
    }

    private PageBorderPositionCriterion pageBorderFillPositionCriterion(PositionCriterion hwpPositionCriterion) {
        switch (hwpPositionCriterion) {
            case MainText:
                return PageBorderPositionCriterion.CONTENT;
            case Paper:
                return PageBorderPositionCriterion.PAPER;
        }
        return PageBorderPositionCriterion.PAPER;
    }

    private PageFillArea pageFillArea(FillArea hwpFillArea) {
        switch (hwpFillArea) {
            case Paper:
                return PageFillArea.PAPER;
            case Page:
                return PageFillArea.PAGE;
            case Border:
                return PageFillArea.BORDER;
        }
        return PageFillArea.PAPER;
    }

    private void masterPages() {
        for (BatangPageInfo batangPageInfo : hwpSectionDefine.getBatangPageInfoList()) {
            secPr.addNewMasterPage()
                    .idRef(parameter.masterPageIdMap().get(batangPageInfo).id());
        }
    }
}
