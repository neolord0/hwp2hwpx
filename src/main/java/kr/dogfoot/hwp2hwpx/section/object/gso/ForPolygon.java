package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFillBrush;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForDrawingObject;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlPolygon;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentNormal;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ShapeComponentPolygon;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.polygon.PositionXY;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Polygon;

public class ForPolygon extends ForShapeComponent {
    public ForPolygon(Parameter parameter) {
        super(parameter);
    }

    public void convert(Polygon polygon, ControlPolygon hwpPolygon) {
        shapeComponent(polygon, hwpPolygon);

        ShapeComponentPolygon hwpSEP = hwpPolygon.getShapeComponentPolygon();
        ShapeComponentNormal hwpSCN = ((ShapeComponentNormal) hwpPolygon.getShapeComponent());
        polygon
                .instidAnd(String.valueOf(hwpSCN.getInstid()));

        polygon.createLineShape();
        ForDrawingObject.lineShape(polygon.lineShape(), hwpSCN.getLineInfo());

        polygon.createFillBrush();
        ForFillBrush.convertForDrawingObject(polygon.fillBrush(), hwpSCN.getFillInfo(), parameter);

        polygon.createShadow();
        ForDrawingObject.shadow(polygon.shadow(), hwpSCN.getShadowInfo());

        if (hwpPolygon.getTextBox() != null) {
            polygon.createDrawText();
            ForDrawingObject.drawText(polygon.drawText(), hwpPolygon.getTextBox(), parameter);
        }

        for (PositionXY hwpPosition : hwpSEP.getPositionList()) {
            polygon.addNewPt()
                    .xAnd(hwpPosition.getX())
                    .y(hwpPosition.getY());
        }
    }
}
