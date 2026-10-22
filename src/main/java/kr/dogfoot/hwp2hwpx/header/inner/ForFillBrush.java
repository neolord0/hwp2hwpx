package kr.dogfoot.hwp2hwpx.header.inner;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.docinfo.borderfill.fillinfo.*;
import kr.dogfoot.hwplib.object.etc.Color4Byte;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.GradationType;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.HatchStyle;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.ImageBrushMode;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.ImageEffect;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.borderfill.*;

public class ForFillBrush {
    public static void convert(FillBrush fillBrush, FillInfo hwpFillInfo, Parameter parameter) {
        if (hwpFillInfo.getType().hasPatternFill()) {
            fillBrush.createWinBrush();
            winBrush(fillBrush.winBrush(), hwpFillInfo.getPatternFill());
        }
        if (hwpFillInfo.getType().hasGradientFill()) {
            fillBrush.createGradation();
            gradation(fillBrush.gradation(), hwpFillInfo.getGradientFill());
        }
        if (hwpFillInfo.getType().hasImageFill()) {
            fillBrush.createImgBrush();
            imgBrush(fillBrush.imgBrush(), hwpFillInfo.getImageFill(), parameter);
        }
    }

    /**
     * 채우기 종류가 하나도 없으면 도형에 fillBrush 를 만들지 않는다.
     * 이전에는 늘 만들어 빈 `<hc:fillBrush/>` 가 남았다 — 한글 저장본은 이 경우 요소를 쓰지 않는다.
     */
    public static boolean hasAnyFill(FillInfo hwpFillInfo) {
        return hwpFillInfo != null && hwpFillInfo.getType() != null
                && (hwpFillInfo.getType().hasPatternFill() || hwpFillInfo.getType().hasGradientFill() || hwpFillInfo.getType().hasImageFill());
    }

    public static void convertForDrawingObject(FillBrush fillBrush, FillInfo hwpFillInfo, Parameter parameter) {
        if (hwpFillInfo.getType().hasPatternFill()) {
            fillBrush.createWinBrush();
            winBrushForDrawingObject(fillBrush.winBrush(), hwpFillInfo.getPatternFill());
        }
        if (hwpFillInfo.getType().hasGradientFill()) {
            fillBrush.createGradation();
            gradation(fillBrush.gradation(), hwpFillInfo.getGradientFill());
        }
        if (hwpFillInfo.getType().hasImageFill()) {
            fillBrush.createImgBrush();
            imgBrush(fillBrush.imgBrush(), hwpFillInfo.getImageFill(), parameter);
        }
    }


    private static void winBrush(WinBrush winBrush, PatternFill hwpPatternFill) {
        if (hwpPatternFill.getPatternType() == PatternType.None) {
            winBrush
                    .faceColorAnd(ValueConvertor.color(hwpPatternFill.getBackColor()))
                    .hatchColorAnd(ValueConvertor.color(hwpPatternFill.getPatternColor()))
                    .alpha((float) 0);
        } else {
            winBrush
                    .faceColorAnd(ValueConvertor.color(hwpPatternFill.getBackColor()))
                    .hatchColorAnd(ValueConvertor.color(hwpPatternFill.getPatternColor()))
                    .hatchStyleAnd(hatchStyle(hwpPatternFill.getPatternType()))
                    .alpha((float) 0);
        }
    }

    private static void winBrushForDrawingObject(WinBrush winBrush, PatternFill hwpPatternFill) {
        winBrush
                .faceColorAnd(ValueConvertor.color(hwpPatternFill.getBackColor()))
                .hatchColorAnd(ValueConvertor.color(hwpPatternFill.getPatternColor()))
                .alpha((float) 0);
        if (hwpPatternFill.getPatternType() != PatternType.None) {
            winBrush.hatchStyle(hatchStyle(hwpPatternFill.getPatternType()));
        }

    }

    private static HatchStyle hatchStyle(PatternType hwpPatternType) {
        switch (hwpPatternType) {
            case HorizontalLine:
                return HatchStyle.HORIZONTAL;
            case VerticalLine:
                return HatchStyle.VERTICAL;
            case BackDiagonalLine:
                return HatchStyle.BACK_SLASH;
            case FrontDiagonalLine:
                return HatchStyle.SLASH;
            case CrossLine:
                return HatchStyle.CROSS;
            case CrossDiagonalLine:
                return HatchStyle.CROSS_DIAGONAL;
            default:
                return null;
        }
    }

    private static void gradation(Gradation gradation, GradientFill hwpGradientFill) {
        gradation
                .typeAnd(gradationType(hwpGradientFill.getGradientType()))
                .angleAnd((int) hwpGradientFill.getStartAngle())
                .centerXAnd((int) hwpGradientFill.getCenterX())
                .centerYAnd((int) hwpGradientFill.getCenterY())
                .stepAnd((short) hwpGradientFill.getBlurringDegree())
                .stepCenterAnd(hwpGradientFill.getBlurringCenter())
                .alpha((float) 0);

        for (Color4Byte hwpColor : hwpGradientFill.getColorList()) {
            gradation.addNewColor()
                    .value(ValueConvertor.color(hwpColor));
        }
    }

    private static GradationType gradationType(GradientType hwpGradientType) {
        switch (hwpGradientType) {
            case Stripe:
                return GradationType.LINEAR;
            case Circle:
                return GradationType.RADIAL;
            case Cone:
                return GradationType.CONICAL;
            case Square:
                return GradationType.SQUARE;
        }
        return GradationType.LINEAR;
    }

    private static void imgBrush(ImgBrush imgBrush, ImageFill hwpImageFill, Parameter parameter) {
        imgBrush.mode(imageBrushMode(hwpImageFill.getImageFillType()));
        if (hwpImageFill.getPictureInfo() != null) {
            imgBrush.createImg();
            image(imgBrush.img(), hwpImageFill.getPictureInfo(), parameter);
        }
    }

    private static ImageBrushMode imageBrushMode(ImageFillType hwpImageFillType) {
        switch (hwpImageFillType) {
            case TileAll:
                return ImageBrushMode.TILE;
            case TileHorizonalTop:
                return ImageBrushMode.TILE_HORZ_TOP;
            case TileHorizonalBottom:
                return ImageBrushMode.TILE_HORZ_BOTTOM;
            case TileVerticalLeft:
                return ImageBrushMode.TILE_VERT_LEFT;
            case TileVerticalRight:
                return ImageBrushMode.TILE_VERT_RIGHT;
            case FitSize:
                return ImageBrushMode.TOTAL;
            case Center:
                return ImageBrushMode.CENTER;
            case CenterTop:
                return ImageBrushMode.CENTER_TOP;
            case CenterBottom:
                return ImageBrushMode.CENTER_BOTTOM;
            case LeftCenter:
                return ImageBrushMode.LEFT_CENTER;
            case LeftTop:
                return ImageBrushMode.LEFT_TOP;
            case LeftBottom:
                return ImageBrushMode.LEFT_BOTTOM;
            case RightCenter:
                return ImageBrushMode.RIGHT_CENTER;
            case RightTop:
                return ImageBrushMode.RIGHT_TOP;
            case RightBottom:
                return ImageBrushMode.RIGHT_BOTTOM;
            case Zoom:
                return ImageBrushMode.ZOOM;
        }
        return ImageBrushMode.CENTER;
    }

    public static void image(Image image, PictureInfo hwpPictureInfo, Parameter parameter) {
        image
                .binaryItemIDRefAnd(parameter.binDataIdMap().get(hwpPictureInfo.getBinItemID()))
                .brightAnd((int) hwpPictureInfo.getBrightness())
                .contrastAnd((int) hwpPictureInfo.getContrast())
                .effectAnd(imageEffect(hwpPictureInfo.getEffect()))
                .alpha((float) 0);
    }

    private static ImageEffect imageEffect(PictureEffect hwpPictureEffect) {
        switch (hwpPictureEffect) {
            case RealPicture:
                return ImageEffect.REAL_PIC;
            case GrayScale:
                return ImageEffect.GRAY_SCALE;
            case BlackWhite:
                return ImageEffect.BLACK_WHITE;
            case Pattern8x8:
                break;
        }
        return ImageEffect.REAL_PIC;
    }
}
