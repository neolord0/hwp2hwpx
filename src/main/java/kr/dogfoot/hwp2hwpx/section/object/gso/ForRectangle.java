package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFillBrush;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForDrawingObject;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlRectangle;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentNormal;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Rectangle;

public class ForRectangle extends ForShapeComponent {
    private Rectangle rectangle;
    private ControlRectangle hwpRectangle;

    public ForRectangle(Parameter parameter) {
        super(parameter);
    }

    public void convert(Rectangle rectangle, ControlRectangle hwpRectangle) {
        shapeComponent(rectangle, hwpRectangle);

        this.rectangle = rectangle;
        this.hwpRectangle = hwpRectangle;

        ShapeComponentNormal hwpSCN = ((ShapeComponentNormal) hwpRectangle.getShapeComponent());
        rectangle
                .instidAnd(String.valueOf(hwpSCN.getInstid()))
                .ratio((short) hwpRectangle.getShapeComponentRectangle().getRoundRate());

        rectangle.createLineShape();
        ForDrawingObject.lineShape(rectangle.lineShape(), hwpSCN.getLineInfo());

        rectangle.createFillBrush();
        ForFillBrush.convertForDrawingObject(rectangle.fillBrush(), hwpSCN.getFillInfo(), parameter);

        rectangle.createShadow();
        ForDrawingObject.shadow(rectangle.shadow(), hwpSCN.getShadowInfo());

        if (hwpRectangle.getTextBox() != null) {
            rectangle.createDrawText();
            ForDrawingObject.drawText(rectangle.drawText(), hwpRectangle.getTextBox(), parameter);
        }

        pt0();
        pt1();
        pt2();
        pt3();
    }

    private void pt0() {
        rectangle.createPt0();
        rectangle.pt0()
                .xAnd((long) hwpRectangle.getShapeComponentRectangle().getX1())
                .y((long) hwpRectangle.getShapeComponentRectangle().getY1());
    }

    private void pt1() {
        rectangle.createPt1();
        rectangle.pt1()
                .xAnd((long) hwpRectangle.getShapeComponentRectangle().getX2())
                .y((long) hwpRectangle.getShapeComponentRectangle().getY2());
    }

    private void pt2() {
        rectangle.createPt2();
        rectangle.pt2()
                .xAnd((long) hwpRectangle.getShapeComponentRectangle().getX3())
                .y((long) hwpRectangle.getShapeComponentRectangle().getY3());
    }

    private void pt3() {
        rectangle.createPt3();
        rectangle.pt3()
                .xAnd((long) hwpRectangle.getShapeComponentRectangle().getX4())
                .y((long) hwpRectangle.getShapeComponentRectangle().getY4());
    }
}
