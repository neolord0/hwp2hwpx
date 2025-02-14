package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForDrawingObject;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlObjectLinkLine;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentNormal;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ShapeComponentLineForObjectLinkLine;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.objectlinkline.ControlPoint;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.objectlinkline.LinkLineType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.ConnectLineType;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.ConnectLine;

public class ForConnectLine extends ForShapeComponent {
    private ConnectLine connectLine;
    private ControlObjectLinkLine hwpObjectLinkLine;
    private ShapeComponentLineForObjectLinkLine hwpSCL;

    public ForConnectLine(Parameter parameter) {
        super(parameter);
    }

    public void convert(ConnectLine connectLine, ControlObjectLinkLine hwpObjectLinkLine) {
        shapeComponent(connectLine, hwpObjectLinkLine);

        this.connectLine = connectLine;
        this.hwpObjectLinkLine = hwpObjectLinkLine;
        hwpSCL = hwpObjectLinkLine.getShapeComponentLine();

        ShapeComponentNormal hwpSCN = ((ShapeComponentNormal) hwpObjectLinkLine.getShapeComponent());
        connectLine
                .instidAnd(String.valueOf(hwpSCN.getInstid()))
                .type(connectLineType(hwpSCL.getType()));

        connectLine.createLineShape();
        ForDrawingObject.lineShape(connectLine.lineShape(), hwpSCN.getLineInfo());

        connectLine.createShadow();
        ForDrawingObject.shadow(connectLine.shadow(), hwpSCN.getShadowInfo());

        startPt();
        endPt();
        controlPoints();
    }


    private ConnectLineType connectLineType(LinkLineType hwpType) {
        switch (hwpType) {
            case Straight_NoArrow:
                return ConnectLineType.STRAIGHT_NOARROW;
            case Straight_OneWay:
                return ConnectLineType.STRAIGHT_ONEWAY;
            case Straight_Both:
                return ConnectLineType.STRAIGHT_BOTH;
            case Stroke_NoArrow:
                return ConnectLineType.STROKE_NOARROW;
            case Stoke_OneWay:
                return ConnectLineType.STROKE_ONEWAY;
            case Stoke_Both:
                return ConnectLineType.STROKE_BOTH;
            case Arc_NoArrow:
                return ConnectLineType.ARC_NOARROW;
            case Arc_OneWay:
                return ConnectLineType.ARC_ONEWAY;
            case Arc_Both:
                return ConnectLineType.ARC_BOTH;
            default:
                return ConnectLineType.STRAIGHT_NOARROW;
        }
    }

    private void startPt() {
        connectLine.createStartPt();
        connectLine.startPt()
                .xAnd((long) hwpSCL.getStartX())
                .yAnd((long) hwpSCL.getStartY())
                .subjectIDRefAnd(String.valueOf(hwpSCL.getStartSubjectID()))
                .subjectIdx((short) hwpSCL.getStartSubjectIndex());
    }

    private void endPt() {
        connectLine.createEndPt();
        connectLine.endPt()
                .xAnd((long) hwpSCL.getEndX())
                .yAnd((long) hwpSCL.getEndY())
                .subjectIDRefAnd(String.valueOf(hwpSCL.getEndSubjectID()))
                .subjectIdx((short) hwpSCL.getEndSubjectIndex());
    }

    private void controlPoints() {
        connectLine.createControlPoints();
        for (ControlPoint hwpCP : hwpSCL.getControlPoints()) {
            connectLine.controlPoints().addNew()
                    .xAnd(hwpCP.getX())
                    .yAnd(hwpCP.getY())
                    .type(hwpCP.getType());
        }
    }
}
