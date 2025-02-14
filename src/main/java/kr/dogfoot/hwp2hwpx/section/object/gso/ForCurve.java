package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFillBrush;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForDrawingObject;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlCurve;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentNormal;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ShapeComponentCurve;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.polygon.PositionXY;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.CurveSegmentType;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Curve;

public class

ForCurve extends ForShapeComponent {
    private Curve curve;
    private ControlCurve hwpCurve;

    public ForCurve(Parameter parameter) {
        super(parameter);
    }

    public void convert(Curve curve, ControlCurve hwpCurve) {
        shapeComponent(curve, hwpCurve);

        this.curve = curve;
        this.hwpCurve = hwpCurve;

        ShapeComponentNormal hwpSCN = ((ShapeComponentNormal) hwpCurve.getShapeComponent());
        curve
                .instidAnd(String.valueOf(hwpSCN.getInstid()));

        curve.createLineShape();
        ForDrawingObject.lineShape(curve.lineShape(), hwpSCN.getLineInfo());

        curve.createFillBrush();
        ForFillBrush.convertForDrawingObject(curve.fillBrush(), hwpSCN.getFillInfo(), parameter);

        curve.createShadow();
        ForDrawingObject.shadow(curve.shadow(), hwpSCN.getShadowInfo());

        if (hwpCurve.getTextBox() != null) {
            curve.createDrawText();
            ForDrawingObject.drawText(curve.drawText(), hwpCurve.getTextBox(), parameter);
        }

        segs(curve, hwpCurve);
    }

    private void segs(Curve curve, ControlCurve hwpCurve) {
        ShapeComponentCurve hwpSEC = hwpCurve.getShapeComponentCurve();

        int count = hwpSEC.getSegmentTypeList().size();
        PositionXY point1 = hwpSEC.getPositionList().get(0);
        for (int index = 0; index < count; index++) {
            PositionXY point2 = hwpSEC.getPositionList().get(index + 1);

            curve.addNewSeg()
                    .typeAnd(curveSegmentType(hwpSEC.getSegmentTypeList().get(index)))
                    .x1And((int) point1.getX())
                    .y1And((int) point1.getY())
                    .x2And((int) point2.getX())
                    .y2((int) point2.getY());

            point1 = point2;
        }
    }

    private CurveSegmentType curveSegmentType(kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.curve.CurveSegmentType hwpCurveSegmentType) {
        switch (hwpCurveSegmentType) {
            case Line:
                return CurveSegmentType.LINE;
            case Curve:
                return CurveSegmentType.CURVE;
            default:
                return CurveSegmentType.CURVE;
        }
    }
}
