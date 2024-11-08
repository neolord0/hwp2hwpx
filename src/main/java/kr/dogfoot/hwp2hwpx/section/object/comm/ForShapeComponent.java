package kr.dogfoot.hwp2hwpx.section.object.comm;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.gso.GsoControl;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.renderingnfo.ScaleRotateMatrixPair;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.shapecomponent.Matrix;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.shapecomponent.ShapeComponent;

public class ForShapeComponent extends ForShapeObject {
    private ShapeComponent shapeComponent;
    private GsoControl hwpGSO;

    public ForShapeComponent(Parameter parameter) {
        super(parameter);
    }

    protected void shapeComponent(ShapeComponent shapeComponent, GsoControl hwpGSO) {
        shapeObject(shapeComponent, hwpGSO.getHeader(), hwpGSO.getCaption());
        this.shapeComponent = shapeComponent;
        this.hwpGSO = hwpGSO;

        shapeComponent.href("");
        shapeComponent.groupLevel((short) hwpGSO.getShapeComponent().getGroupingCount());

        offset();
        orgSz();
        curSz();
        flip();
        rotationInfo();
        renderingInfo();
    }

    private void offset() {
        shapeComponent.createOffset();
        shapeComponent.offset()
                .xAnd(ValueConvertor.toUnsigned(hwpGSO.getShapeComponent().getOffsetX()))
                .y(ValueConvertor.toUnsigned(hwpGSO.getShapeComponent().getOffsetY()));
    }

    private void orgSz() {
        shapeComponent.createOrgSz();
        shapeComponent.orgSz()
                .widthAnd((long) hwpGSO.getShapeComponent().getWidthAtCreate())
                .height((long) hwpGSO.getShapeComponent().getHeightAtCreate());
    }

    private void curSz() {
        shapeComponent.createCurSz();
        if (hwpGSO.getShapeComponent().getWidthAtCreate() == hwpGSO.getShapeComponent().getWidthAtCurrent()) {
            shapeComponent.curSz().width(0L);
        } else {
            shapeComponent.curSz().width((long) hwpGSO.getShapeComponent().getWidthAtCurrent());
        }

        if (hwpGSO.getShapeComponent().getHeightAtCreate() == hwpGSO.getShapeComponent().getHeightAtCurrent()) {
            shapeComponent.curSz().height(0L);
        } else {
            shapeComponent.curSz().height((long) hwpGSO.getShapeComponent().getHeightAtCurrent());
        }
    }

    private void flip() {
        shapeComponent.createFlip();
        shapeComponent.flip()
                .horizontalAnd(hwpGSO.getShapeComponent().getProperty().isFlipHorizontal())
                .vertical(hwpGSO.getShapeComponent().getProperty().isFlipVertical());
    }

    private void rotationInfo() {
        shapeComponent.createRotationInfo();
        shapeComponent.rotationInfo()
                .angleAnd((short) hwpGSO.getShapeComponent().getRotateAngle())
                .centerXAnd((long) hwpGSO.getShapeComponent().getRotateXCenter())
                .centerYAnd((long) hwpGSO.getShapeComponent().getRotateYCenter())
                .rotateimage(true);
        // todo : rotateimage ??
    }

    private void renderingInfo() {
        shapeComponent.createRenderingInfo();

        matrix(shapeComponent.renderingInfo().addNewTransMatrix(), hwpGSO.getShapeComponent().getRenderingInfo().getTranslationMatrix());
        for (ScaleRotateMatrixPair matrixPair : hwpGSO.getShapeComponent().getRenderingInfo().getScaleRotateMatrixPairList()) {
            matrix(shapeComponent.renderingInfo().addNewScaMatrix(), matrixPair.getScaleMatrix());
            matrix(shapeComponent.renderingInfo().addNewRotMatrix(), matrixPair.getRotateMatrix());
        }
    }

    private void matrix(Matrix matrix, kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.renderingnfo.Matrix hwpMatrix) {
        matrix
                .e1And((float) hwpMatrix.getValue(0))
                .e2And((float) hwpMatrix.getValue(1))
                .e3And((float) hwpMatrix.getValue(2))
                .e4And((float) hwpMatrix.getValue(3))
                .e5And((float) hwpMatrix.getValue(4))
                .e6((float) hwpMatrix.getValue(5));
    }
}
