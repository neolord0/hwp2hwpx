package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.docinfo.CharShape;
import kr.dogfoot.hwplib.object.docinfo.charshape.*;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.*;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.CharPr;

import java.util.ArrayList;

public class ForCharProperties extends Converter {
    private CharPr charPr;
    private CharShape hwpCharShape;

    public ForCharProperties(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList, ArrayList<CharShape> hwpCharShapeList) {
        if (hwpCharShapeList.size() == 0) return;

        refList.createCharProperties();

        int id = 0;
        for (CharShape item : hwpCharShapeList) {
            charPr = refList.charProperties().addNew().idAnd(String.valueOf(id));
            hwpCharShape = item;

            charPr();
            id++;
        }
    }

    private void charPr() {
        charPr
                .heightAnd(hwpCharShape.getBaseSize())
                .textColorAnd(ValueConvertor.color(hwpCharShape.getCharColor()))
                .shadeColorAnd(ValueConvertor.colorWithNone(hwpCharShape.getShadeColor(), 0xFFFFFFFFl))
                .useFontSpaceAnd(hwpCharShape.getProperty().isUsingSpaceAppropriateForFont())
                .useKerningAnd(hwpCharShape.getProperty().isKerning())
                .symMarkAnd(sysMark(hwpCharShape.getProperty().getEmphasisSort()))
                .borderFillIDRef(String.valueOf(hwpCharShape.getBorderFillId()));

        fontRef(hwpCharShape.getFaceNameIds());
        ratio(hwpCharShape.getRatios());
        spacing(hwpCharShape.getCharSpaces());
        relSz(hwpCharShape.getRelativeSizes());
        offset(hwpCharShape.getCharOffsets());
        underline();
        strikeout();
        outline();
        shadow();

        if (hwpCharShape.getProperty().isItalic()) {
            charPr.createItalic();
        }
        if (hwpCharShape.getProperty().isBold()) {
            charPr.createBold();
        }
        if (hwpCharShape.getProperty().isEmboss()) {
            charPr.createEmboss();
        }
        if (hwpCharShape.getProperty().isEngrave()) {
            charPr.createEngrave();
        }
        if (hwpCharShape.getProperty().isSuperScript()) {
            charPr.createSupscript();
        }
        if (hwpCharShape.getProperty().isSubScript()) {
            charPr.createSubscript();
        }
    }


    private SymMarkSort sysMark(EmphasisSort hwpEmphasisSort) {
        switch (hwpEmphasisSort) {
            case None:
                return SymMarkSort.NONE;
            case DotAbove:
                return SymMarkSort.DOT_ABOVE;
            case RingAbove:
                return SymMarkSort.RING_ABOVE;
            case Tilde:
                return SymMarkSort.TILDE;
            case Caron:
                return SymMarkSort.CARON;
            case Side:
                return SymMarkSort.SIDE;
            case Colon:
                return SymMarkSort.COLON;
            case GraveAccent:
                return SymMarkSort.GRAVE_ACCENT;
            case AcuteAccent:
                return SymMarkSort.ACUTE_ACCENT;
            case Circumflex:
                return SymMarkSort.CIRCUMFLEX;
            case Macron:
                return SymMarkSort.MACRON;
            case HookAbove:
                return SymMarkSort.HOOK_ABOVE;
            case DotBelow:
                return SymMarkSort.DOT_BELOW;
        }
        return SymMarkSort.NONE;
    }

    private void fontRef(FaceNameIds hwpFaceNameIds) {
        charPr.createFontRef();
        charPr.fontRef().set(String.valueOf(hwpFaceNameIds.getHangul()),
                String.valueOf(hwpFaceNameIds.getLatin()),
                String.valueOf(hwpFaceNameIds.getHanja()),
                String.valueOf(hwpFaceNameIds.getJapanese()),
                String.valueOf(hwpFaceNameIds.getOther()),
                String.valueOf(hwpFaceNameIds.getSymbol()),
                String.valueOf(hwpFaceNameIds.getUser()));
    }

    private void ratio(Ratios hwpRatios) {
        charPr.createRatio();
        charPr.ratio().set(hwpRatios.getHangul(),
                hwpRatios.getLatin(),
                hwpRatios.getHanja(),
                hwpRatios.getJapanese(),
                hwpRatios.getOther(),
                hwpRatios.getSymbol(),
                hwpRatios.getUser());
    }

    private void spacing(CharSpaces hwpCharSpaces) {
        charPr.createSpacing();
        charPr.spacing().set((short) hwpCharSpaces.getHangul(),
                (short) hwpCharSpaces.getLatin(),
                (short) hwpCharSpaces.getHanja(),
                (short) hwpCharSpaces.getJapanese(),
                (short) hwpCharSpaces.getOther(),
                (short) hwpCharSpaces.getSymbol(),
                (short) hwpCharSpaces.getUser());
    }

    private void relSz(RelativeSizes hwpRelativeSizes) {
        charPr.createRelSz();
        charPr.relSz().set(hwpRelativeSizes.getHangul(),
                hwpRelativeSizes.getLatin(),
                hwpRelativeSizes.getHanja(),
                hwpRelativeSizes.getJapanese(),
                hwpRelativeSizes.getOther(),
                hwpRelativeSizes.getSymbol(),
                hwpRelativeSizes.getUser());
    }

    private void offset(CharOffsets hwpCharOffsets) {
        charPr.createOffset();
        charPr.offset().set((short) hwpCharOffsets.getHangul(),
                (short) hwpCharOffsets.getLatin(),
                (short) hwpCharOffsets.getHanja(),
                (short) hwpCharOffsets.getJapanese(),
                (short) hwpCharOffsets.getOther(),
                (short) hwpCharOffsets.getSymbol(),
                (short) hwpCharOffsets.getUser());
    }

    private void underline() {
        charPr.createUnderline();
        charPr.underline()
                .typeAnd(underLineType(hwpCharShape.getProperty().getUnderLineSort()))
                .shapeAnd(ValueConvertor.lineType3(hwpCharShape.getProperty().getUnderLineShape()))
                .color(ValueConvertor.color(hwpCharShape.getUnderLineColor()));
    }

    private UnderlineType underLineType(UnderLineSort hwpUnderLineSort) {
        switch (hwpUnderLineSort) {
            case None:
                return UnderlineType.NONE;
            case Bottom:
                return UnderlineType.BOTTOM;
            case Top:
                return UnderlineType.TOP;
        }
        return UnderlineType.NONE;
    }

    private void strikeout() {
        charPr.createStrikeout();
        if (hwpCharShape.getProperty().isStrikeLine()) {
            charPr.strikeout()
                    .shapeAnd(ValueConvertor.lineType2(hwpCharShape.getProperty().getStrikeLineShape()))
                    .color(ValueConvertor.color(hwpCharShape.getStrikeLineColor()));
        } else {
            charPr.strikeout()
                    .shapeAnd(LineType2.NONE)
                    .color("#000000");
        }
    }

    private void outline() {
        charPr.createOutline();

        charPr.outline()
                .type(outlineType(hwpCharShape.getProperty().getOutterLineSort()));
    }

    private LineType1 outlineType(OutterLineSort hwpOutterLineSort) {
        switch (hwpOutterLineSort) {
            case None:
                return LineType1.NONE;
            case Solid:
                return LineType1.SOLID;
            case Dot:
                return LineType1.DOT;
            case BoldSolid:
                return LineType1.THICK;
            case Dash:
                return LineType1.DASH;
            case DashDot:
                return LineType1.DASH_DOT;
            case DashDotDot:
                return LineType1.DASH_DOT_DOT;
        }
        return LineType1.NONE;
    }

    private void shadow() {
        charPr.createShadow();

        charPr.shadow()
                .typeAnd(charShadowType(hwpCharShape.getProperty().getShadowSort()))
                .colorAnd(ValueConvertor.color(hwpCharShape.getShadowColor()))
                .offsetXAnd((short) hwpCharShape.getShadowGap1())
                .offsetY((short) hwpCharShape.getShadowGap2());
    }

    private CharShadowType charShadowType(ShadowSort hwpShadowSort) {
        switch (hwpShadowSort) {
            case None:
                return CharShadowType.NONE;
            case Discontinuous:
                return CharShadowType.DROP;
            case Continuous:
                return CharShadowType.CONTINUOUS;
        }
        return CharShadowType.NONE;
    }
}
