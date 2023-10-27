package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.control.gso.*;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Run;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Container;

public class ForGso extends Converter {
    private ForLine lineConverter;
    private ForRectangle rectangleConverter;
    private ForEllipse ellipseConverter;
    private ForArc arcConverter;
    private ForPolygon polygonConverter;
    private ForCurve curveConverter;
    private ForPicture pictureConverter;
    private ForOLE oleConverter;
    private ForContainer containerConverter;
    private ForConnectLine connectLineConverter;
    private ForTextArt textArtConverter;


    public ForGso(Parameter parameter) {
        super(parameter);

        lineConverter = new ForLine(parameter);
        rectangleConverter = new ForRectangle(parameter);
        ellipseConverter = new ForEllipse(parameter);
        arcConverter = new ForArc(parameter);
        polygonConverter = new ForPolygon(parameter);
        pictureConverter = new ForPicture(parameter);
        curveConverter = new ForCurve(parameter);
        oleConverter = new ForOLE(parameter);
        containerConverter = new ForContainer(parameter, this);
        connectLineConverter = new ForConnectLine(parameter);
        textArtConverter = new ForTextArt(parameter);
    }

    public void convert(Run currentRun, GsoControl hwpGSO) {
        switch (hwpGSO.getGsoType()) {
            case Line:
                lineConverter.convert(currentRun.addNewLine(), (ControlLine) hwpGSO);
                break;
            case Rectangle:
                rectangleConverter.convert(currentRun.addNewRectangle(), (ControlRectangle) hwpGSO);
                break;
            case Ellipse:
                ellipseConverter.convert(currentRun.addNewEllipse(), (ControlEllipse) hwpGSO);
                break;
            case Arc:
                arcConverter.convert(currentRun.addNewArc(), (ControlArc) hwpGSO);
                break;
            case Polygon:
                polygonConverter.convert(currentRun.addNewPolygon(), (ControlPolygon) hwpGSO);
                break;
            case Curve:
                curveConverter.convert(currentRun.addNewCurve(), (ControlCurve) hwpGSO);
                break;
            case Picture:
                pictureConverter.convert(currentRun.addNewPicture(), (ControlPicture) hwpGSO);
                break;
            case OLE:
                oleConverter.convert(currentRun.addNewOLE(), (ControlOLE) hwpGSO);
                break;
            case Container:
                containerConverter.convert(currentRun.addNewContainer(), (ControlContainer) hwpGSO);
                break;
            case ObjectLinkLine:
                connectLineConverter.convert(currentRun.addNewConnectLine(), (ControlObjectLinkLine) hwpGSO);
                break;
            case TextArt:
                textArtConverter.convert(currentRun.addNewTextArt(), (ControlTextArt) hwpGSO);
                break;
        }
    }

    public void convert(Container container, GsoControl hwpGSO) {
        switch (hwpGSO.getGsoType()) {
            case Line:
                lineConverter.convert(container.addNewLine(), (ControlLine) hwpGSO);
                break;
            case Rectangle:
                rectangleConverter.convert(container.addNewRectangle(), (ControlRectangle) hwpGSO);
                break;
            case Ellipse:
                ellipseConverter.convert(container.addNewEllipse(), (ControlEllipse) hwpGSO);
                break;
            case Arc:
                arcConverter.convert(container.addNewArc(), (ControlArc) hwpGSO);
                break;
            case Polygon:
                polygonConverter.convert(container.addNewPolygon(), (ControlPolygon) hwpGSO);
                break;
            case Curve:
                curveConverter.convert(container.addNewCurve(), (ControlCurve) hwpGSO);
                break;
            case Picture:
                pictureConverter.convert(container.addNewPicture(), (ControlPicture) hwpGSO);
                break;
            case OLE:
                oleConverter.convert(container.addNewOLE(), (ControlOLE) hwpGSO);
                break;
            case Container:
                containerConverter.convert(container.addNewContainer(), (ControlContainer) hwpGSO);
                break;
            case ObjectLinkLine:
                connectLineConverter.convert(container.addNewConnectLine(), (ControlObjectLinkLine) hwpGSO);
                break;
            case TextArt:
                textArtConverter.convert(container.addNewTextArt(), (ControlTextArt) hwpGSO);
                break;
        }
    }
}

