package kr.dogfoot.hwp2hwpx.section.object.comm;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.CtrlHeaderGso;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.gso.*;
import kr.dogfoot.hwplib.object.bodytext.control.gso.caption.Caption;
import kr.dogfoot.hwplib.object.bodytext.control.gso.caption.CaptionDirection;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.HorzRelTo;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.VertRelTo;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.*;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.shapeobject.ShapeObject;

import java.util.HashMap;

public class ForShapeObject extends Converter {
    protected ShapeObject shapeObject;
    protected CtrlHeaderGso hwpGSOHeader;
    protected Caption hwpCaption;

    public ForShapeObject(Parameter parameter) {
        super(parameter);
    }

    protected void shapeObject(ShapeObject shapeObject, CtrlHeaderGso hwpGSOHeader, Caption hwpCaption) {
        this.shapeObject = shapeObject;
        this.hwpGSOHeader = hwpGSOHeader;
        this.hwpCaption = hwpCaption;

        if (hwpGSOHeader != null) {
            shapeObject.id(String.valueOf(hwpGSOHeader.getInstanceId()));
            shapeObject.zOrder(hwpGSOHeader.getzOrder());
            shapeObject.numberingType(numberingType(hwpGSOHeader.getProperty().getObjectNumberSort()));
            shapeObject.textWrap(textWrapMethod(hwpGSOHeader.getProperty().getTextFlowMethod()));
            shapeObject.textFlow(textFlowSide(hwpGSOHeader.getProperty().getTextHorzArrange()));
        } else {
            shapeObject.id(String.valueOf(0));
            shapeObject.zOrder(0);
            shapeObject.numberingType(NumberingType.NONE);
            shapeObject.textWrap(TextWrapMethod.TOP_AND_BOTTOM);
            shapeObject.textFlow(TextFlowSide.BOTH_SIDES);
        }
        shapeObject.lock(false);
        shapeObject.dropcapstyle(DropCapStyle.None);
        // todo : lock, dropcapstyle ??

        sz();
        pos();
        outMargin();
        caption();
        shapeComment();
    }

    private NumberingType numberingType(ObjectNumberSort hwpObjectNumberSort) {
        switch (hwpObjectNumberSort) {
            case None:
                return NumberingType.NONE;
            case Figure:
                return NumberingType.PICTURE;
            case Table:
                return NumberingType.TABLE;
            case Equation:
                return NumberingType.EQUATION;
        }
        return NumberingType.NONE;
    }

    private TextWrapMethod textWrapMethod(TextFlowMethod hwpTextFlowMethod) {
        switch (hwpTextFlowMethod) {
            case FitWithText:
                return TextWrapMethod.SQUARE;
            case TakePlace:
                return TextWrapMethod.TOP_AND_BOTTOM;
            case BehindText:
                return TextWrapMethod.BEHIND_TEXT;
            case InFrontOfText:
                return TextWrapMethod.IN_FRONT_OF_TEXT;
        }
        return TextWrapMethod.TOP_AND_BOTTOM;
    }

    private TextFlowSide textFlowSide(TextHorzArrange hwpTextHorzArrange) {
        switch (hwpTextHorzArrange) {
            case BothSides:
                return TextFlowSide.BOTH_SIDES;
            case LeftOnly:
                return TextFlowSide.LEFT_ONLY;
            case RightOnly:
                return TextFlowSide.RIGHT_ONLY;
            case LargestOnly:
                return TextFlowSide.LARGEST_ONLY;
        }
        return TextFlowSide.BOTH_SIDES;
    }

    protected void sz() {
        if (hwpGSOHeader == null) return;

        shapeObject.createSZ();
        shapeObject.sz()
                .widthAnd(hwpGSOHeader.getWidth())
                .widthRelToAnd(widthRelTo(hwpGSOHeader.getProperty().getWidthCriterion()))
                .heightAnd(hwpGSOHeader.getHeight())
                .heightRelToAnd(heightRelTo(hwpGSOHeader.getProperty().getHeightCriterion()))
                .protect(hwpGSOHeader.getProperty().isProtectSize());
    }

    private WidthRelTo widthRelTo(WidthCriterion hwpWidthCriterion) {
        switch (hwpWidthCriterion) {
            case Paper:
                return WidthRelTo.PAPER;
            case Page:
                return WidthRelTo.PAGE;
            case Column:
                return WidthRelTo.COLUMN;
            case Para:
                return WidthRelTo.PARA;
            case Absolute:
                return WidthRelTo.ABSOLUTE;
        }
        return WidthRelTo.PAPER;
    }

    private HeightRelTo heightRelTo(HeightCriterion hwpHeightCriterion) {
        switch (hwpHeightCriterion) {
            case Paper:
                return HeightRelTo.PAPER;
            case Page:
                return HeightRelTo.PAGE;
            case Absolute:
                return HeightRelTo.ABSOLUTE;
        }
        return HeightRelTo.PAPER;
    }

    protected void pos() {
        if (hwpGSOHeader == null) return;

        shapeObject.createPos();
        shapeObject.pos()
                .treatAsCharAnd(hwpGSOHeader.getProperty().isLikeWord())
                .affectLSpacingAnd(hwpGSOHeader.getProperty().isApplyLineSpace())
                .flowWithTextAnd(hwpGSOHeader.getProperty().isVertRelToParaLimit())
                .allowOverlapAnd(hwpGSOHeader.getProperty().isAllowOverlap())
                .holdAnchorAndSOAnd(hwpGSOHeader.isPreventPageDivide())
                .vertRelToAnd(vertRelTo(hwpGSOHeader.getProperty().getVertRelTo()))
                .horzRelToAnd(horzRelTo(hwpGSOHeader.getProperty().getHorzRelTo()))
                .vertAlignAnd(vertAlign(hwpGSOHeader.getProperty().getVertRelativeArrange()))
                .horzAlignAnd(horzAlign(hwpGSOHeader.getProperty().getHorzRelativeArrange()))
                .vertOffsetAnd(hwpGSOHeader.getyOffset())
                .horzOffset(hwpGSOHeader.getxOffset());
        // todo : holdAnchorAndSO
    }

    private VertRelTo vertRelTo(kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.gso.VertRelTo hwpVertRelTo) {
        switch (hwpVertRelTo) {
            case Paper:
                return VertRelTo.PAPER;
            case Page:
                return VertRelTo.PAGE;
            case Para:
                return VertRelTo.PARA;
        }
        return VertRelTo.PAPER;
    }

    private HorzRelTo horzRelTo(kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.gso.HorzRelTo hwpHorzRelTo) {
        switch (hwpHorzRelTo) {
            case Paper:
                return HorzRelTo.PAPER;
            case Page:
                return HorzRelTo.PAGE;
            case Column:
                return HorzRelTo.COLUMN;
            case Para:
                return HorzRelTo.PARA;
        }
        return HorzRelTo.PAPER;
    }

    private VertAlign vertAlign(RelativeArrange hwpRelativeArrange) {
        switch (hwpRelativeArrange) {
            case TopOrLeft:
                return VertAlign.TOP;
            case Center:
                return VertAlign.CENTER;
            case BottomOrRight:
                return VertAlign.BOTTOM;
            case Inside:
                return VertAlign.INSIDE;
            case Outside:
                return VertAlign.OUTSIDE;
        }
        return VertAlign.TOP;
    }

    private HorzAlign horzAlign(RelativeArrange hwpRelativeArrange) {
        switch (hwpRelativeArrange) {
            case TopOrLeft:
                return HorzAlign.LEFT;
            case Center:
                return HorzAlign.CENTER;
            case BottomOrRight:
                return HorzAlign.RIGHT;
            case Inside:
                return HorzAlign.INSIDE;
            case Outside:
                return HorzAlign.OUTSIDE;
        }
        return HorzAlign.LEFT;
    }

    protected void outMargin() {
    if (hwpGSOHeader == null) return;

        shapeObject.createOutMargin();
        shapeObject.outMargin()
                .leftAnd((long) hwpGSOHeader.getOutterMarginLeft())
                .rightAnd((long) hwpGSOHeader.getOutterMarginRight())
                .topAnd((long) hwpGSOHeader.getOutterMarginTop())
                .bottom((long) hwpGSOHeader.getOutterMarginBottom());
    }

    private void caption() {
        if (hwpGSOHeader == null
                || !hwpGSOHeader.getProperty().hasCaption()
                || hwpCaption == null) return;

        shapeObject.createCaption();
        shapeObject.caption()
                .sideAnd(captionSide(hwpCaption.getListHeader().getCaptionProperty().getDirection()))
                .fullSzAnd(hwpCaption.getListHeader().getCaptionProperty().isIncludeMargin())
                .widthAnd(hwpCaption.getListHeader().getCaptionWidth())
                .gapAnd((long) hwpCaption.getListHeader().getSpaceBetweenCaptionAndFrame())
                .lastWidth(hwpCaption.getListHeader().getTextWidth());

        parameter.subListConverter().convertForCaption(shapeObject.caption(), hwpCaption);
    }

    private CaptionSide captionSide(CaptionDirection hwpCaptionDirection) {
        switch (hwpCaptionDirection) {
            case Left:
                return CaptionSide.LEFT;
            case Right:
                return CaptionSide.RIGHT;
            case Top:
                return CaptionSide.TOP;
            case Bottom:
                return CaptionSide.BOTTOM;
        }
        return CaptionSide.BOTTOM;
    }


    private void shapeComment() {
        if (hwpGSOHeader == null) return;

        String hwpExplanation = hwpGSOHeader.getExplanation().toUTF16LEString();
        if (hwpExplanation != null && hwpExplanation.length() > 0) {
            shapeObject.createShapeComment();
            shapeObject.shapeComment()
                    .addText(hwpExplanation);
        }
    }
}
