package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.charshape.CharPositionShapeIdPair;
import kr.dogfoot.hwplib.object.bodytext.paragraph.lineseg.LineSegItem;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Para;

public class ForPara extends Converter {
    private ForChars charConverter;

    public ForPara(Parameter parameter) {
        super(parameter);

        charConverter = new ForChars(parameter);
    }

    public void convert(Para para, Paragraph hwpPara) {
        para
                .idAnd(String.valueOf(ValueConvertor.toUnsigned(hwpPara.getHeader().getInstanceID())))
                .paraPrIDRefAnd(ValueConvertor.refID(hwpPara.getHeader().getParaShapeId()))
                .styleIDRefAnd(ValueConvertor.refID(hwpPara.getHeader().getStyleId()))
                .pageBreakAnd(hwpPara.getHeader().getDivideSort().isDividePage())
                .columnBreakAnd(hwpPara.getHeader().getDivideSort().isDivideColumn())
                .merged(hwpPara.getHeader().getIsMergedByTrack() != 0);

        runs(para, hwpPara);
        charConverter.convert(para, hwpPara);
        lineSegArray(para, hwpPara);
    }

    private static void runs(Para para, Paragraph hwpPara) {
        for (CharPositionShapeIdPair charPositionShapeIdPair : hwpPara.getCharShape().getPositonShapeIdPairList()) {
            para.addNewRun()
                    .charPrIDRef(ValueConvertor.refID(charPositionShapeIdPair.getShapeId()));
        }
    }

    private void lineSegArray(Para para, Paragraph hwpPara) {
        if (hwpPara.getLineSeg() == null || hwpPara.getLineSeg().getLineSegItemList().size() == 0) return;

        para.createLineSegArray();

        for (LineSegItem hwpLineSegItem : hwpPara.getLineSeg().getLineSegItemList()) {
            para.lineSegArray().addNew()
                    .textposAnd((int) hwpLineSegItem.getTextStartPosition())
                    .vertposAnd(hwpLineSegItem.getLineVerticalPosition())
                    .vertsizeAnd(hwpLineSegItem.getLineHeight())
                    .textheightAnd(hwpLineSegItem.getTextPartHeight())
                    .baselineAnd(hwpLineSegItem.getDistanceBaseLineToLineVerticalPosition())
                    .spacingAnd(hwpLineSegItem.getLineSpace())
                    .horzposAnd(hwpLineSegItem.getStartPositionFromColumn())
                    .horzsizeAnd(hwpLineSegItem.getSegmentWidth())
                    .flags((int) hwpLineSegItem.getTag().getValue());
        }
    }
}
