package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFillBrush;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForDrawingObject;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlTextArt;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentNormal;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ShapeComponentTextArt;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.polygon.PositionXY;
import kr.dogfoot.hwplib.object.docinfo.facename.FontType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.DrawingShadowType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.FontType2;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.TextArtAlign;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.TextArtShape;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.TextArt;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.textart.TextArtPr;

public class ForTextArt extends ForShapeComponent {
    private TextArt textArt;
    private ControlTextArt hwpTextArt;
    private ShapeComponentTextArt hwpSCTA;

    public ForTextArt(Parameter parameter) {
        super(parameter);
    }

    public void convert(TextArt textArt, ControlTextArt hwpTextArt) {
        shapeComponent(textArt, hwpTextArt);

        this.textArt = textArt;
        this.hwpTextArt = hwpTextArt;


        this.hwpSCTA = hwpTextArt.getShapeComponentTextArt();

        ShapeComponentNormal hwpSCN = ((ShapeComponentNormal) hwpTextArt.getShapeComponent());
        textArt
                .instidAnd(String.valueOf(hwpSCN.getInstid()))
                .text(hwpSCTA.getContent().toUTF16LEString());

        textArt.createLineShape();
        ForDrawingObject.lineShape(textArt.lineShape(), hwpSCN.getLineInfo());

        textArt.createFillBrush();
        ForFillBrush.convertForDrawingObject(textArt.fillBrush(), hwpSCN.getFillInfo(), parameter);

        textArt.createShadow();
        ForDrawingObject.shadow(textArt.shadow(), hwpSCN.getShadowInfo());

        pt0();
        pt1();
        pt2();
        pt3();
        textartPr();
        outline();
    }

    private void pt0() {
        textArt.createPt0();
        textArt.pt0()
                .xAnd((long) hwpSCTA.getX1())
                .y((long) hwpSCTA.getY1());
    }

    private void pt1() {
        textArt.createPt1();
        textArt.pt1()
                .xAnd((long) hwpSCTA.getX2())
                .y((long) hwpSCTA.getY2());
    }

    private void pt2() {
        textArt.createPt2();
        textArt.pt2()
                .xAnd((long) hwpSCTA.getX3())
                .y((long) hwpSCTA.getY3());
    }

    private void pt3() {
        textArt.createPt3();
        textArt.pt3()
                .xAnd((long) hwpSCTA.getX4())
                .y((long) hwpSCTA.getY4());
    }

    private void textartPr() {

        textArt.createTextartPr();

        TextArtPr textArtPr = textArt.textartPr();
        textArtPr
                .fontNameAnd(hwpSCTA.getFontName().toUTF16LEString())
                .fontStyleAnd(hwpSCTA.getFontStyle().toUTF16LEString())
                .fontTypeAnd(fontType2(hwpSCTA.getFontType()))
                .textShapeAnd(textArtShape(hwpSCTA.getTextArtShape()))
                .lineSpacingAnd(hwpSCTA.getLineSpace())
                .charSpacingAnd(hwpSCTA.getCharSpace())
                .align(textArtAlign(hwpSCTA.getParaAlignment()));

        textArtPr.createShadow();
        textArtPr.shadow()
                .typeAnd(drawingShadowType(hwpSCTA.getShadowType()))
                .colorAnd(ValueConvertor.color(hwpSCTA.getShadowColor()))
                .offsetXAnd((long) hwpSCTA.getShadowOffsetX())
                .offsetYAnd((long) hwpSCTA.getShadowOffsetY())
                .alphaAnd((float) 0);
        // todo : alpha ??

    }

    private DrawingShadowType drawingShadowType(int hwpShadowType) {
        if (hwpShadowType == 0) {
            return DrawingShadowType.NONE;
        } else if (hwpShadowType == 1) {
            return DrawingShadowType.PARELLEL_LEFTTOP;
        }

        return DrawingShadowType.NONE;
    }

    private FontType2 fontType2(FontType hwpFontType) {
        switch (hwpFontType) {
            case Unknown:
                return null;
            case TTF:
                return FontType2.TTF;
            case HFT:
                return FontType2.HFT;
            default:
                return null;
        }
    }

    private TextArtShape textArtShape(kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.textart.TextArtShape hwpTextArtShape) {
        switch (hwpTextArtShape) {
            case PARALLELOGRAM:
                return TextArtShape.PARALLELOGRAM;
            case INVERTED_PARALLELOGRAM:
                return TextArtShape.INVERTED_PARALLELOGRAM;
            case INVERTED_UPWARD_CASCADE:
                return TextArtShape.INVERTED_UPWARD_CASCADE;
            case INVERTED_DOWNWARD_CASCADE:
                return TextArtShape.INVERTED_DOWNWARD_CASCADE;
            case UPWARD_CASCADE:
                return TextArtShape.UPWARD_CASCADE;
            case DOWNWARD_CASCADE:
                return TextArtShape.DOWNWARD_CASCADE;
            case REDUCE_RIGHT:
                return TextArtShape.REDUCE_RIGHT;
            case REDUCE_LEFT:
                return TextArtShape.REDUCE_LEFT;
            case ISOSCELES_TRAPEZOID:
                return TextArtShape.ISOSCELES_TRAPEZOID;
            case INVERTED_ISOSCELES_TRAPEZOID:
                return TextArtShape.INVERTED_ISOSCELES_TRAPEZOID;
            case TOP_RIBBON_RECTANGLE:
                return TextArtShape.TOP_RIBBON_RECTANGLE;
            case BOTTOM_RIBBON_RECTANGLE:
                return TextArtShape.BOTTOM_RIBBON_RECTANGLE;
            case CHEVRON_DOWN:
                return TextArtShape.CHEVRON_DOWN;
            case CHEVRON:
                return TextArtShape.CHEVRON;
            case BOW_TIE:
                return TextArtShape.BOW_TIE;
            case HEXAGON:
                return TextArtShape.HEXAGON;
            case WAVE1:
                return TextArtShape.WAVE1;
            case WAVE2:
                return TextArtShape.WAVE2;
            case WAVE3:
                return TextArtShape.WAVE3;
            case WAVE4:
                return TextArtShape.WAVE4;
            case LEFT_TILT_CYLINDER:
                return TextArtShape.LEFT_TILT_CYLINDER;
            case RIGHT_TILT_CYLINDER:
                return TextArtShape.RIGHT_TILT_CYLINDER;
            case BOTTOM_WIDE_CYLINDER:
                return TextArtShape.BOTTOM_WIDE_CYLINDER;
            case TOP_WIDE_CYLINDER:
                return TextArtShape.TOP_WIDE_CYLINDER;
            case THIN_CURVE_UP1:
                return TextArtShape.THIN_CURVE_UP1;
            case THIN_CURVE_UP2:
                return TextArtShape.THIN_CURVE_UP2;
            case THIN_CURVE_DOWN1:
                return TextArtShape.THIN_CURVE_DOWN1;
            case THIN_CURVE_DOWN2:
                return TextArtShape.THIN_CURVE_DOWN2;
            case INVERSED_FINGERNAIL:
                return TextArtShape.INVERSED_FINGERNAIL;
            case FINGERNAIL:
                return TextArtShape.FINGERNAIL;
            case GINKO_LEAF1:
                return TextArtShape.GINKO_LEAF1;
            case GINKO_LEAF2:
                return TextArtShape.GINKO_LEAF2;
            case INFLATE_RIGHT:
                return TextArtShape.INFLATE_RIGHT;
            case INFLATE_LEFT:
                return TextArtShape.INFLATE_LEFT;
            case INFLATE_UP_CONVEX:
                return TextArtShape.INFLATE_UP_CONVEX;
            case INFLATE_BOTTOM_CONVEX:
                return TextArtShape.INFLATE_BOTTOM_CONVEX;
            case DEFLATE_TOP:
                return TextArtShape.DEFLATE_TOP;
            case DEFLATE_BOTTOM:
                return TextArtShape.DEFLATE_BOTTOM;
            case DEFLATE:
                return TextArtShape.DEFLATE;
            case INFLATE:
                return TextArtShape.INFLATE;
            case INFLATE_TOP:
                return TextArtShape.INFLATE_TOP;
            case INFLATE_BOTTOM:
                return TextArtShape.INFLATE_BOTTOM;
            case RECTANGLE:
                return TextArtShape.RECTANGLE;
            case LEFT_CYLINDER:
                return TextArtShape.LEFT_CYLINDER;
            case CYLINDER:
                return TextArtShape.CYLINDER;
            case RIGHT_CYLINDER:
                return TextArtShape.RIGHT_CYLINDER;
            case CIRCLE:
                return TextArtShape.CIRCLE;
            case CURVE_DOWN:
                return TextArtShape.CURVE_DOWN;
            case ARCH_UP:
                return TextArtShape.ARCH_UP;
            case ARCH_DOWN:
                return TextArtShape.ARCH_DOWN;
            case SINGLE_LINE_CIRCLE1:
                return TextArtShape.SINGLE_LINE_CIRCLE1;
            case SINGLE_LINE_CIRCLE2:
                return TextArtShape.SINGLE_LINE_CIRCLE2;
            case TRIPLE_LINE_CIRCLE1:
                return TextArtShape.TRIPLE_LINE_CIRCLE1;
            case TRIPLE_LINE_CIRCLE2:
                return TextArtShape.TRIPLE_LINE_CIRCLE2;
            case DOUBLE_LINE_CIRCLE:
                return TextArtShape.DOUBLE_LINE_CIRCLE;
            default:
                return TextArtShape.PARALLELOGRAM;
        }
    }

    private TextArtAlign textArtAlign(kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.textart.TextArtAlign hwpAlign) {
        switch (hwpAlign) {
            case LEFT:
                return TextArtAlign.LEFT;
            case RIGHT:
                return TextArtAlign.RIGHT;
            case CENTER:
                return TextArtAlign.CENTER;
            case FULL:
                return TextArtAlign.FULL;
            case TABLE:
                return TextArtAlign.TABLE;
            default:
                return TextArtAlign.LEFT;
        }
    }

    private void outline() {
        if (hwpSCTA.getOutlinePointList().size() == 0) return;

        textArt.createOutline();
        for (PositionXY hwpPositionXY : hwpSCTA.getOutlinePointList()) {
            textArt.outline().addNew()
                    .xAnd(hwpPositionXY.getX())
                    .y(hwpPositionXY.getY());
        }
    }
}
