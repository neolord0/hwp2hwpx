package kr.dogfoot.hwp2hwpx.section.object.comm;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.lineinfo.LineInfo;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.shadowinfo.ShadowInfo;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.shadowinfo.ShadowType;
import kr.dogfoot.hwplib.object.bodytext.control.gso.textbox.TextBox;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.DrawingShadowType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.OutlineStyle;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.drawingobject.DrawText;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.drawingobject.DrawingShadow;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.picture.LineShape;

public class ForDrawingObject {
    public static void lineShape(LineShape lineShape, LineInfo hwpLineInfo) {
        lineShape
                .colorAnd(ValueConvertor.color(hwpLineInfo.getColor()))
                .widthAnd(hwpLineInfo.getThickness())
                .styleAnd(ValueConvertor.lineType2(hwpLineInfo.getProperty().getLineType()))
                .endCapAnd(ValueConvertor.lineCap(hwpLineInfo.getProperty().getLineEndShape()))
                .headStyleAnd(ValueConvertor.arrowStyle(hwpLineInfo.getProperty().getStartArrowShape()))
                .tailStyleAnd(ValueConvertor.arrowStyle(hwpLineInfo.getProperty().getEndArrowShape()))
                .headfillAnd(hwpLineInfo.getProperty().isFillStartArrow())
                .tailfillAnd(hwpLineInfo.getProperty().isFillEndArrow())
                .headSzAnd(ValueConvertor.arrowSize(hwpLineInfo.getProperty().getStartArrowSize()))
                .tailSzAnd(ValueConvertor.arrowSize(hwpLineInfo.getProperty().getEndArrowSize()))
                .outlineStyleAnd(outLineStyle(hwpLineInfo.getOutlineStyle()))
                .alpha((float) 0);
        // todo : alpha ??
    }

    private static OutlineStyle outLineStyle(kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.lineinfo.OutlineStyle hwpOutLineStyle) {
        switch (hwpOutLineStyle) {
            case Normal:
                return OutlineStyle.NORMAL;
            case Outter:
                return OutlineStyle.OUTER;
            case Inner:
                return OutlineStyle.INNER;
            default:
                return OutlineStyle.NORMAL;
        }
    }



    public static void shadow(DrawingShadow shadow, ShadowInfo hwpShadowInfo) {
        shadow
                .typeAnd(drawingShadowType(hwpShadowInfo.getType()))
                .colorAnd(ValueConvertor.color(hwpShadowInfo.getColor()))
                .offsetXAnd((long) hwpShadowInfo.getOffsetX())
                .offsetYAnd((long) hwpShadowInfo.getOffsetY())
                .alpha((float) hwpShadowInfo.getTransparent());
    }

    private static DrawingShadowType drawingShadowType(ShadowType hwpShadowType) {
        switch (hwpShadowType) {
            case None:
                return DrawingShadowType.NONE;
            case LeftTop:
                return DrawingShadowType.PARELLEL_LEFTTOP;
            case RightTop:
                return DrawingShadowType.PARELLEL_RIGHTTOP;
            case LeftBottom:
                return DrawingShadowType.PARELLEL_LEFTBOTTOM;
            case RightBottom:
                return DrawingShadowType.PARELLEL_RIGHTBOTTOM;
            case LeftBack:
                return DrawingShadowType.SHEAR_LEFTTOP;
            case RightBack:
                return DrawingShadowType.SHEAR_RIGHTTOP;
            case LeftFront:
                return DrawingShadowType.SHEAR_LEFTBOTTOM;
            case RightFront:
                return DrawingShadowType.SHEAR_RIGHTBOTTOM;
            case Small:
                return DrawingShadowType.SCALE_NARROW;
            case Large:
                return DrawingShadowType.SCALE_ENLARGE;
            default:
                return DrawingShadowType.NONE;
        }
    }

    public static void drawText(DrawText drawText, TextBox hwpTextBox, Parameter parameter) {
        String hwpFieldName = hwpTextBox.getListHeader().getFieldName();

        drawText
                .lastWidthAnd(hwpTextBox.getListHeader().getTextWidth())
                .nameAnd(hwpFieldName == null ? "" : hwpFieldName)
                .editable(hwpTextBox.getListHeader().isEditableAtFormMode());

        textMargin(drawText, hwpTextBox);
        parameter.subListConverter().convertForDrawText(drawText, hwpTextBox);
    }

    private static void textMargin(DrawText drawText, TextBox hwpTextBox) {
        if (hwpTextBox.getListHeader().getLeftMargin() != 0 ||
                hwpTextBox.getListHeader().getRightMargin() != 0 ||
                hwpTextBox.getListHeader().getTopMargin() != 0 ||
                hwpTextBox.getListHeader().getBottomMargin() != 0) {
            drawText.createTextMargin();
            drawText.textMargin()
                    .leftAnd((long) hwpTextBox.getListHeader().getLeftMargin())
                    .rightAnd((long) hwpTextBox.getListHeader().getRightMargin())
                    .topAnd((long) hwpTextBox.getListHeader().getTopMargin())
                    .bottom((long) hwpTextBox.getListHeader().getBottomMargin()) ;
        }
    }
}
