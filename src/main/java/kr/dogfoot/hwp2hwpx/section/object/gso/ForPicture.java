package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.header.inner.ForFillBrush;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwp2hwpx.section.object.gso.inner.ForPictureEffect;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlPicture;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.lineinfo.LineType;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.ShapeComponentPicture;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.OutlineStyle;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Picture;

public class ForPicture extends ForShapeComponent {
    private Picture picture;
    private ControlPicture hwpPicture;
    private ShapeComponentPicture hwpSCP;

    public ForPicture(Parameter parameter) {
        super(parameter);
    }

    public void convert(Picture picture, ControlPicture hwpPicture) {
        shapeComponent(picture, hwpPicture);

        this.picture = picture;
        this.hwpPicture = hwpPicture;
        hwpSCP = hwpPicture.getShapeComponentPicture();


        picture
                .instidAnd(String.valueOf(hwpPicture.getShapeComponentPicture().getInstanceId()))
                .reverse(hwpPicture.getShapeComponent().getProperty().isReverseColor());

        lineShape();
        imgRect();
        imgClip();
        inMargin();
        imgDim();
        img();
        effects();
    }

    private void lineShape() {
        if (hwpSCP.getBorderProperty().getLineType() != LineType.None) {
            picture.createLineShape();
            picture.lineShape()
                    .colorAnd(ValueConvertor.color(hwpSCP.getBorderColor()))
                    .widthAnd(hwpSCP.getBorderThickness())
                    .styleAnd(ValueConvertor.lineType2(hwpSCP.getBorderProperty().getLineType()))
                    .endCapAnd(ValueConvertor.lineCap(hwpSCP.getBorderProperty().getLineEndShape()))
                    .headStyleAnd(ValueConvertor.arrowStyle(hwpSCP.getBorderProperty().getStartArrowShape()))
                    .tailStyleAnd(ValueConvertor.arrowStyle(hwpSCP.getBorderProperty().getEndArrowShape()))
                    .headfillAnd(ValueConvertor.arrowFill(hwpSCP.getBorderProperty().getStartArrowShape()))
                    .tailfillAnd(ValueConvertor.arrowFill(hwpSCP.getBorderProperty().getEndArrowShape()))
                    .headSzAnd(ValueConvertor.arrowSize(hwpSCP.getBorderProperty().getStartArrowSize()))
                    .tailSzAnd(ValueConvertor.arrowSize(hwpSCP.getBorderProperty().getEndArrowSize()))
                    .outlineStyleAnd(OutlineStyle.OUTER)
                    .alpha((float) hwpSCP.getBorderTransparency());
            // todo : outlineStyle
        }
    }

    private void imgRect() {
        picture.createImgRect();

        picture.imgRect().createPt0();
        picture.imgRect().pt0()
                .xAnd(hwpSCP.getLeftTop().getX())
                .y(hwpSCP.getLeftTop().getY());

        picture.imgRect().createPt1();
        picture.imgRect().pt1()
                .xAnd(hwpSCP.getRightTop().getX())
                .y(hwpSCP.getRightTop().getY());

        picture.imgRect().createPt2();
        picture.imgRect().pt2()
                .xAnd(hwpSCP.getRightBottom().getX())
                .y(hwpSCP.getRightBottom().getY());

        picture.imgRect().createPt3();
        picture.imgRect().pt3()
                .xAnd(hwpSCP.getLeftBottom().getX())
                .y(hwpSCP.getLeftBottom().getY());
    }

    private void imgClip() {
        picture.createImgClip();
        picture.imgClip()
                .leftAnd((long) hwpSCP.getLeftAfterCutting())
                .rightAnd((long) hwpSCP.getRightAfterCutting())
                .topAnd((long) hwpSCP.getTopAfterCutting())
                .bottom((long) hwpSCP.getBottomAfterCutting());
    }

    private void inMargin() {
        picture.createInMargin();
        picture.inMargin()
                .leftAnd((long) hwpSCP.getInnerMargin().getLeft())
                .rightAnd((long) hwpSCP.getInnerMargin().getTop())
                .topAnd((long) hwpSCP.getInnerMargin().getTop())
                .bottom((long) hwpSCP.getInnerMargin().getBottom());
    }

    private void imgDim() {
        picture.createImgDim();
        picture.imgDim()
                .dimwidthAnd(hwpSCP.getImageWidth())
                .dimheight(hwpSCP.getImageHeight());
    }

    private void img() {
        picture.createImg();
        ForFillBrush.image(picture.img(), hwpPicture.getShapeComponentPicture().getPictureInfo(), parameter);
    }

    private void effects() {
        picture.createEffects();
        ForPictureEffect.convert(picture.effects(), hwpSCP.getPictureEffect());
    }
}
