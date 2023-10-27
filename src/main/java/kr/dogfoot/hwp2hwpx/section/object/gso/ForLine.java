package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForDrawingObject;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlLine;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentNormal;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Line;

public class ForLine extends ForShapeComponent {
    private Line line;
    private ControlLine hwpLine;

    public ForLine(Parameter parameter) {
        super(parameter);
    }

    public void convert(Line line, ControlLine hwpLine) {
        shapeComponent(line, hwpLine);

        this.line = line;
        this.hwpLine = hwpLine;
        ShapeComponentNormal hwpSCN = ((ShapeComponentNormal) hwpLine.getShapeComponent());
        line
                .instidAnd(String.valueOf(hwpSCN.getInstid()))
                .isReverseHV(hwpLine.getShapeComponentLine().isStartedRightOrBottom());

        line.createLineShape();
        ForDrawingObject.lineShape(line.lineShape(), hwpSCN.getLineInfo());

        line.createShadow();
        ForDrawingObject.shadow(line.shadow(), hwpSCN.getShadowInfo());

        startPt();
        endPt();
    }

    private void startPt() {
        line.createStartPt();
        line.startPt()
                .xAnd((long) hwpLine.getShapeComponentLine().getStartX())
                .y((long) hwpLine.getShapeComponentLine().getStartY());
    }

    private void endPt() {
        line.createEndPt();
        line.endPt()
                .xAnd((long) hwpLine.getShapeComponentLine().getEndX())
                .y((long) hwpLine.getShapeComponentLine().getEndY());
    }

}
