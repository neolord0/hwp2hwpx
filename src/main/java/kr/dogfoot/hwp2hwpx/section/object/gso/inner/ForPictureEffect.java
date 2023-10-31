package kr.dogfoot.hwp2hwpx.section.object.gso.inner;

import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.picture.ColorEffect;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponenteach.picture.*;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.AlignStyleType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.ColorEffectType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.ColorType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.ShadowStyle;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.picture.effects.*;

public class ForPictureEffect {
    public static void convert(Effects effects, PictureEffect hwpPictureEffect) {
        if (hwpPictureEffect.getProperty().hasShadowEffect()) {
            effects.createShadow();
            shadow(effects.shadow(), hwpPictureEffect.getShadowEffect());
        }
        if (hwpPictureEffect.getProperty().hasNeonEffect()) {
            effects.createGlow();
            glow(effects.glow(), hwpPictureEffect.getNeonEffect());
        }
        if (hwpPictureEffect.getProperty().hasSoftBorderEffect()) {
            effects.createSoftEdge();
            softEdge(effects.softEdge(), hwpPictureEffect.getSoftEdgeEffect());
        }
        if (hwpPictureEffect.getProperty().hasReflectionEffect()) {
            effects.createReflection();
            reflection(effects.reflection(), hwpPictureEffect.getReflectionEffect());
        }
    }


    private static void shadow(EffectsShadow shadow, ShadowEffect hwpShadow) {
        shadow
                .styleAnd(shadowStyle(hwpShadow.getStyle()))
                .alphaAnd(hwpShadow.getTransparency())
                .radiusAnd(hwpShadow.getCloudy())
                .directionAnd((short) hwpShadow.getDirection())
                .distanceAnd(hwpShadow.getDistance())
                .alignStyleAnd(alignStyleType(hwpShadow.getSort()))
                .rotationStyleAnd(ValueConvertor.bool((short) hwpShadow.getRotateWithShape()));

        shadow.createSkew();
        shadow.skew()
                .xAnd(hwpShadow.getTiltAngleX())
                .y(hwpShadow.getTiltAngleY());

        shadow.createScale();
        shadow.scale()
                .xAnd(hwpShadow.getZoomRateX())
                .y(hwpShadow.getZoomRateY());

        shadow.createEffectsColor();
        effectsColor(shadow.effectsColor(), hwpShadow.getColor());
    }

    private static void effectsColor(EffectsColor effectsColor, ColorWithEffect hwpColorWithEffect) {
        effectsColor
                .typeAnd(ColorType.RGB)
                .schemeIdxAnd(-1)
                .systemIdxAnd(-1)
                .presetIdx(-1);

        effectsColor.createRgb();;
        effectsColor.rgb()
                .rAnd(ValueConvertor.toUnsigned(hwpColorWithEffect.getColor()[2]))
                .bAnd(ValueConvertor.toUnsigned(hwpColorWithEffect.getColor()[0]))
                .g(ValueConvertor.toUnsigned(hwpColorWithEffect.getColor()[1]));
        for (ColorEffect hwpColorEffect : hwpColorWithEffect.getColorEffectList()) {
            effectsColor.addNewEffect()
                    .typeAnd(colorEffectType(hwpColorEffect.getSort()))
                    .value(String.valueOf(hwpColorEffect.getValue()));
        }
    }

    private static ShadowStyle shadowStyle(int hwpStyle) {
        switch (hwpStyle) {
            case 0:
                return ShadowStyle.OUTSIDE;
            case 1:
                return ShadowStyle.INSIDE;
            default:
                return ShadowStyle.OUTSIDE;
        }
    }

    private static AlignStyleType alignStyleType(int hwpSort) {
        switch (hwpSort) {
            case 0:
                return AlignStyleType.TOP_LEFT;
            case 1:
                return AlignStyleType.TOP;
            case 2:
                return AlignStyleType.TOP_RIGHT;
            case 3:
                return AlignStyleType.LEFT;
            case 4:
                return AlignStyleType.CENTER;
            case 5:
                return AlignStyleType.RIGHT;
            case 6:
                return AlignStyleType.BOTTOM_LEFT;
            case 7:
                return AlignStyleType.BOTTOM;
            case 8:
                return AlignStyleType.BOTTOM_RIGHT;
            default:
                return AlignStyleType.TOP_LEFT;
        }
    }


    private static ColorEffectType colorEffectType(ColorEffectSort hwpColorEffectSort) {
        switch (hwpColorEffectSort) {
            case Alpha:
                return ColorEffectType.ALPHA;
            case Alpha_Mod:
                return ColorEffectType.ALPHA_MOD;
            case Alpha_Off:
                return ColorEffectType.ALPHA_OFF;
            case Red:
                return ColorEffectType.RED;
            case Red_Mod:
                return ColorEffectType.RED_MOD;
            case Ref_Off:
                return ColorEffectType.RED_OFF;
            case Green:
                return ColorEffectType.GREEN;
            case Green_Mod:
                return ColorEffectType.GREEN_MOD;
            case Green_Off:
                return ColorEffectType.GREEN_OFF;
            case Blue:
                return ColorEffectType.BLUE;
            case Blue_Mod:
                return ColorEffectType.BLUE_MOD;
            case Blue_Off:
                return ColorEffectType.BLUE_OFF;
            case Hue:
                return ColorEffectType.HUE;
            case Hue_Mod:
                return ColorEffectType.HUE_MOD;
            case Hue_Off:
                return ColorEffectType.HUE_OFF;
            case Sat:
                return ColorEffectType.SAT;
            case Sat_Mod:
                return ColorEffectType.SAT_MOD;
            case Sat_Off:
                return ColorEffectType.SAT_OFF;
            case Lum:
                return ColorEffectType.LUM;
            case Lum_Mod:
                return ColorEffectType.LUM_MOD;
            case Lum_Off:
                return ColorEffectType.LUM_OFF;
            case Shade:
                return ColorEffectType.SHADE;
            case Tint:
                return ColorEffectType.TINT;
            case Gray:
                return ColorEffectType.GRAY;
            case Comp:
                return ColorEffectType.COMP;
            case Gamma:
                return ColorEffectType.GAMMA;
            case Inv_Gamma:
                return ColorEffectType.INV_GAMMA;
            case Inv:
                return ColorEffectType.INV;
        }
        return ColorEffectType.ALPHA;
    }

    private static void glow(EffectsGlow glow, NeonEffect hwpNeon) {
        glow
                .alphaAnd(hwpNeon.getTransparency())
                .radius(hwpNeon.getRadius());

        glow.createEffectsColor();
        effectsColor(glow.effectsColor(), hwpNeon.getColor());
    }

    private static void softEdge(EffectsSoftEdge softEdge, SoftEdgeEffect hwpSoftEdge) {
        softEdge.radius(hwpSoftEdge.getRadius());
    }

    private static void reflection(EffectsReflection reflection, ReflectionEffect hwpReflection) {
        reflection
                .alignStyleAnd(alignStyleType(hwpReflection.getStyle()))
                .radiusAnd(hwpReflection.getRadius())
                .directionAnd((int) hwpReflection.getDirection())
                .distanceAnd(hwpReflection.getDistance())
                .rotationStyleAnd(ValueConvertor.bool((short) hwpReflection.getRotationStyle()))
                .fadeDirectionAnd((int) hwpReflection.getOffsetDirection());

        reflection.createSkew();
        reflection.skew()
                .xAnd(hwpReflection.getTiltAngleX())
                .y(hwpReflection.getTiltAngleY());

        reflection.createScale();
        reflection.scale()
                .xAnd(hwpReflection.getZoomRateX())
                .y(hwpReflection.getZoomRateY());

        reflection.createAlpha();
        reflection.alpha()
                .startAnd(hwpReflection.getStartTransparency())
                .end(hwpReflection.getEndTransparency());

        reflection.createPos();
        reflection.pos()
                .startAnd(hwpReflection.getStartPosition())
                .end(hwpReflection.getEndPosition());
    }
}


