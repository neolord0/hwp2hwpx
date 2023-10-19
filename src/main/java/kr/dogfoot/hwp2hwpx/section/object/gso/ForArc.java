package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFillBrush;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForDrawingObject;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlArc;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentNormal;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ShapeComponentArc;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Arc;

public class ForArc extends ForShapeComponent {
    private Arc arc;
    private ControlArc hwpArc;
    private ShapeComponentArc hwpSCA;

    public ForArc(Parameter parameter) {
        super(parameter);
    }

    public void convert(Arc arc, ControlArc hwpArc) {
        shapeComponent(arc, hwpArc);

        this.arc = arc;
        this.hwpArc = hwpArc;
        this.hwpSCA = hwpArc.getShapeComponentArc();

        ShapeComponentNormal hwpSCN = ((ShapeComponentNormal) hwpArc.getShapeComponent());
        arc
                .instidAnd(String.valueOf(hwpSCN.getInstid()))
                .type(ValueConvertor.arcType(hwpSCA.getArcType()));

        arc.createLineShape();
        ForDrawingObject.lineShape(arc.lineShape(), hwpSCN.getLineInfo());

        arc.createFillBrush();
        ForFillBrush.convertForDrawingObject(arc.fillBrush(), hwpSCN.getFillInfo(), parameter);

        arc.createShadow();
        ForDrawingObject.shadow(arc.shadow(), hwpSCN.getShadowInfo());

        if (hwpArc.getTextBox() != null) {
            arc.createDrawText();
            ForDrawingObject.drawText(arc.drawText(), hwpArc.getTextBox(), parameter);
        }

        center();
        ax1();
        ax2();
    }

    private void center() {
        arc.createCenter();
        arc.center()
                .xAnd((long) hwpSCA.getCenterX())
                .y((long) hwpSCA.getCenterY());
    }

    private void ax1() {
        arc.createAx1();
        arc.ax1()
                .xAnd((long) hwpSCA.getAxis1X())
                .y((long) hwpSCA.getAxis1Y());
    }

    private void ax2() {
        arc.createAx2();
        arc.ax2()
                .xAnd((long) hwpSCA.getAxis2X())
                .y((long) hwpSCA.getAxis2Y());
    }
}
