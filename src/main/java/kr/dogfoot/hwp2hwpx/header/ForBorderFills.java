package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFillBrush;
import kr.dogfoot.hwp2hwpx.util.HWPUtil;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.docinfo.borderfill.BackSlashDiagonalShape;
import kr.dogfoot.hwplib.object.docinfo.borderfill.BorderFillProperty;
import kr.dogfoot.hwplib.object.docinfo.borderfill.EachBorder;
import kr.dogfoot.hwplib.object.docinfo.borderfill.SlashDiagonalShape;
import kr.dogfoot.hwpxlib.object.common.ObjectList;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.CenterLineSort;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.LineType2;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.LineWidth;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.SlashType;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.BorderFill;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.borderfill.Border;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.borderfill.SlashCore;

import java.util.ArrayList;

public class ForBorderFills extends Converter {
    private BorderFill borderFill;
    private kr.dogfoot.hwplib.object.docinfo.BorderFill hwpBorderFill;

    public ForBorderFills(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList,
                        ArrayList<kr.dogfoot.hwplib.object.docinfo.BorderFill> hwpBorderFillList) {
        if (hwpBorderFillList.size() == 0) return;

        refList.createBorderFills();

        int id = 1;
        if (anyZeroReference()) {   // 한글처럼 1번에 기본 테두리/배경을 넣는다 — Parameter.borderFillRef 주석
            addDefaultBorderFill(refList.borderFills());
            parameter.borderFillShift(1);
            id++;
        }

        for (kr.dogfoot.hwplib.object.docinfo.BorderFill item : hwpBorderFillList) {
            borderFill = refList.borderFills().addNew().idAnd(String.valueOf(id));
            hwpBorderFill = item;

            borderFill();
            id++;
        }
    }


    private boolean anyZeroReference() {
        for (kr.dogfoot.hwplib.object.docinfo.CharShape c : parameter.hwp().docInfo().getCharShapeList()) {
            if (c.getBorderFillId() == 0) return true;
        }
        for (kr.dogfoot.hwplib.object.docinfo.ParaShape p : parameter.hwp().docInfo().getParaShapeList()) {
            if (p.getBorderFillId() == 0) return true;
        }
        return false;
    }

    private void addDefaultBorderFill(ObjectList<BorderFill> borderFillObjectList) {
        BorderFill bf = borderFillObjectList.addNew()
                .idAnd("1")
                .threeDAnd(false)
                .shadowAnd(false)
                .centerLineAnd(CenterLineSort.NONE)
                .breakCellSeparateLineAnd(false);

        bf.createSlash();
        bf.slash()
                .typeAnd(SlashType.NONE)
                .CrookedAnd(false)
                .isCounter(false);

        bf.createBackSlash();
        bf.backSlash()
                .typeAnd(SlashType.NONE)
                .CrookedAnd(false)
                .isCounter(false);

        bf.createLeftBorder();
        bf.createRightBorder();
        bf.createTopBorder();
        bf.createBottomBorder();
        for (Border b : new Border[]{bf.leftBorder(), bf.rightBorder(), bf.topBorder(), bf.bottomBorder()}) {
            b
                    .typeAnd(LineType2.NONE)
                    .widthAnd(LineWidth.MM_0_1)
                    .color("#000000");
        }

        bf.createDiagonal();
        bf.diagonal()
                .typeAnd(LineType2.SOLID)
                .widthAnd(LineWidth.MM_0_1)
                .color("#000000");
    }

    private void borderFill() {
        borderFill
                .threeDAnd(hwpBorderFill.getProperty().is3DEffect())
                .shadowAnd(hwpBorderFill.getProperty().isShadowEffect())
                .centerLineAnd(centerLine(hwpBorderFill.getProperty()))
                .breakCellSeparateLine(false);
        // todo: breakCellSeparateLine ??

        slash();
        backSlash();
        borders();

        if (HWPUtil.hasFillInfo(hwpBorderFill.getFillInfo())) {
            borderFill.createFillBrush();
            ForFillBrush.convert(borderFill.fillBrush(), hwpBorderFill.getFillInfo(), parameter);
        }
    }

    private CenterLineSort centerLine(BorderFillProperty hwpProperty) {
        if (hwpProperty.hasCenterLine() == true) {
            switch (hwpProperty.getCenterLineSort()) {
                case None:
                    return CenterLineSort.NONE;
                case Horizontal:
                    return CenterLineSort.HORIZONTAL;
                case Vertical:
                    return CenterLineSort.VERTICAL;
                case Cross:
                    return CenterLineSort.CROSS;
            }
        }
        return CenterLineSort.NONE;
    }

    private void slash() {
        borderFill.createSlash();
        SlashCore slash = borderFill.slash();

        slash
                .typeAnd(slashType(hwpBorderFill.getProperty().getSlashDiagonalShape()))
                .CrookedAnd(hwpBorderFill.getProperty().isBrokenSlashDiagonal())
                .isCounter(false);
    }

    private void backSlash() {
        borderFill.createBackSlash();
        SlashCore backSlash = borderFill.backSlash();

        backSlash
                .typeAnd(backSlashType(hwpBorderFill.getProperty().getBackSlashDiagonalShape()))
                .CrookedAnd(hwpBorderFill.getProperty().isBrokenSlashDiagonal())
                .isCounter(false);
    }

    private SlashType slashType(SlashDiagonalShape slashDiagonalShape) {
        switch (slashDiagonalShape) {
            case None:
                return SlashType.NONE;
            case Slash:
                return SlashType.CENTER;
            case RightTopToBottomEdge:
                return SlashType.CENTER_BELOW;
            case RightTopToLeftEdge:
                return SlashType.CENTER_ABOVE;
            case RightTopToBottomLeftEdge:
                return SlashType.ALL;
        }
        return SlashType.NONE;
    }

    private SlashType backSlashType(BackSlashDiagonalShape backSlashDiagonalShape) {
        switch (backSlashDiagonalShape) {
            case None:
                return SlashType.NONE;
            case BackSlash:
                return SlashType.CENTER;
            case LeftTopToBottomEdge:
                return SlashType.CENTER_BELOW;
            case LeftTopToRightEdg:
                return SlashType.CENTER_ABOVE;
            case LeftTopToBottomRightEdge:
                return SlashType.ALL;
        }
        return SlashType.NONE;
    }

    private void borders() {
        borderFill.createLeftBorder();
        borderFill.createRightBorder();
        borderFill.createTopBorder();
        borderFill.createBottomBorder();
        borderFill.createDiagonal();

        border(borderFill.leftBorder(), hwpBorderFill.getLeftBorder());
        border(borderFill.rightBorder(), hwpBorderFill.getRightBorder());
        border(borderFill.topBorder(), hwpBorderFill.getTopBorder());
        border(borderFill.bottomBorder(), hwpBorderFill.getBottomBorder());
        border(borderFill.diagonal(), hwpBorderFill.getDiagonalBorder());
    }

    private void border(Border border, EachBorder hwpBorder) {
        border
                .typeAnd(ValueConvertor.lineType2(hwpBorder.getType()))
                .widthAnd(ValueConvertor.lineWidth(hwpBorder.getThickness()))
                .color(ValueConvertor.color(hwpBorder.getColor()));
    }
}
