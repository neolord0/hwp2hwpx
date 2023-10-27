package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFillBrush;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForDrawingObject;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlEllipse;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentNormal;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ShapeComponentEllipse;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Ellipse;

public class ForEllipse extends ForShapeComponent {
    private Ellipse ellipse;
    private ControlEllipse hwpEllipse;
    private ShapeComponentEllipse hwpSCE;

    public ForEllipse(Parameter parameter) {
        super(parameter);
    }

    public void convert(Ellipse ellipse, ControlEllipse hwpEllipse) {
        shapeComponent(ellipse, hwpEllipse);

        this.ellipse = ellipse;
        this.hwpEllipse = hwpEllipse;
        this.hwpSCE = hwpEllipse.getShapeComponentEllipse();

        ShapeComponentNormal hwpSCN = ((ShapeComponentNormal) hwpEllipse.getShapeComponent());
        ellipse
                .instidAnd(String.valueOf(hwpSCN.getInstid()))
                .intervalDirtyAnd(hwpSCE.getProperty().isRecalculateIntervalWhenChangingArc())
                .hasArcPrAnd(hwpSCE.getProperty().isChangeArc())
                .arcType(ValueConvertor.arcType(hwpSCE.getProperty().getArcType()));

        ellipse.createLineShape();
        ForDrawingObject.lineShape(ellipse.lineShape(), hwpSCN.getLineInfo());

        ellipse.createFillBrush();
        ForFillBrush.convertForDrawingObject(ellipse.fillBrush(), hwpSCN.getFillInfo(), parameter);

        ellipse.createShadow();
        ForDrawingObject.shadow(ellipse.shadow(), hwpSCN.getShadowInfo());

        if (hwpEllipse.getTextBox() != null) {
            ellipse.createDrawText();
            ForDrawingObject.drawText(ellipse.drawText(), hwpEllipse.getTextBox(), parameter);
        }

        center();
        ax1();
        ax2();
        start1();
        end1();
        start2();
        end2();
    }

    private void center() {
        ellipse.createCenter();
        ellipse.center()
                .xAnd((long) hwpSCE.getCenterX())
                .y((long) hwpSCE.getCenterY());
    }

    private void ax1() {
        ellipse.createAx1();
        ellipse.ax1()
                .xAnd((long) hwpSCE.getAxis1X())
                .y((long) hwpSCE.getAxis1Y());
    }

    private void ax2() {
        ellipse.createAx2();
        ellipse.ax2()
                .xAnd((long) hwpSCE.getAxis2X())
                .y((long) hwpSCE.getAxis2Y());
    }

    private void start1() {
        ellipse.createStart1();
        ellipse.start1()
                .xAnd((long) hwpSCE.getStartX())
                .y((long) hwpSCE.getStartY());
    }

    private void end1() {
        ellipse.createEnd1();
        ellipse.end1()
                .xAnd((long) hwpSCE.getEndX())
                .y((long) hwpSCE.getEndY());
    }

    private void start2() {
        ellipse.createStart2();
        ellipse.start2()
                .xAnd((long) hwpSCE.getStartX2())
                .y((long) hwpSCE.getStartY2());
    }

    private void end2() {
        ellipse.createEnd2();
        ellipse.end2()
                .xAnd((long) hwpSCE.getEndX2())
                .y((long) hwpSCE.getEndY2());
    }

}
