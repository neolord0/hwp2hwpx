package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlOLE;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentNormal;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ShapeComponentOLE;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ole.DVASPECT;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ole.ObjectSort;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.OLEDrawAspect;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.OLEObjectType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.OutlineStyle;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.OLE;

public class ForOLE extends ForShapeComponent {
    public ForOLE(Parameter parameter) {
        super(parameter);
    }

    public void convert(OLE ole, ControlOLE hwpOLE) {
        shapeComponent(ole, hwpOLE);


        ShapeComponentNormal hwpSCN = ((ShapeComponentNormal) hwpOLE.getShapeComponent());
        ShapeComponentOLE hwpSCO = hwpOLE.getShapeComponentOLE();
        ole
                .objectTypeAnd(oleObjectType(hwpSCO.getProperty().getObjectSort()))
                .binaryItemIDRefAnd(parameter.binDataIdMap().get(hwpSCO.getBinDataId()))
                .hasMonikerAnd(hwpSCO.getProperty().isMoniker())
                .drawAspectAnd(oleDrawAspect(hwpSCO.getProperty().getDVASPECT()))
                .eqBaseLine((int) hwpSCO.getProperty().getBaseLine());

        ole.createExtent();
        ole.extent()
                .xAnd((long) hwpSCO.getExtentWidth())
                .y((long) hwpSCO.getExtentHeight());


        ole.createLineShape();
        ole.lineShape()
                .colorAnd(ValueConvertor.color(hwpSCO.getBorderColor()))
                .widthAnd(hwpSCO.getBorderThickness())
                .styleAnd(ValueConvertor.lineType2(hwpSCO.getBorderProperty().getLineType()))
                .endCapAnd(ValueConvertor.lineCap(hwpSCO.getBorderProperty().getLineEndShape()))
                .headStyleAnd(ValueConvertor.arrowStyle(hwpSCO.getBorderProperty().getStartArrowShape()))
                .tailStyleAnd(ValueConvertor.arrowStyle(hwpSCO.getBorderProperty().getEndArrowShape()))
                .headfillAnd(hwpSCO.getBorderProperty().isFillStartArrow())
                .tailfillAnd(hwpSCO.getBorderProperty().isFillEndArrow())
                .headSzAnd(ValueConvertor.arrowSize(hwpSCO.getBorderProperty().getStartArrowSize()))
                .tailSzAnd(ValueConvertor.arrowSize(hwpSCO.getBorderProperty().getEndArrowSize()))
                .outlineStyleAnd(OutlineStyle.OUTER)
                .alpha((float) 0);
        // todo : outlineStyle, alpha ??
    }

    private OLEObjectType oleObjectType(ObjectSort hwpObjSort) {
        switch (hwpObjSort) {
            case Unknown:
                return OLEObjectType.UNKNOWN;
            case Embedded:
                return OLEObjectType.EMBEDDED;
            case Link:
                return OLEObjectType.LINK;
            case Static:
                return OLEObjectType.STATIC;
            case Equation:
                return OLEObjectType.EQUATION;
            default:
                return OLEObjectType.UNKNOWN;
        }
    }


    private OLEDrawAspect oleDrawAspect(DVASPECT hwpDVAspect) {
        switch (hwpDVAspect) {
            case CONTENT:
                return OLEDrawAspect.CONTENT;
            case THUMBNAIL:
                return OLEDrawAspect.THUMB_NAIL;
            case ICON:
                return OLEDrawAspect.ICON;
            case DOCPRINT:
                return OLEDrawAspect.DOC_PRINT;
            default:
                return OLEDrawAspect.CONTENT;
        }

    }

}