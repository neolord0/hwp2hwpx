package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.docinfo.numbering.LevelNumbering;
import kr.dogfoot.hwplib.object.docinfo.numbering.ParagraphAlignment;
import kr.dogfoot.hwplib.object.docinfo.numbering.ParagraphHeadInfo;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.HorizontalAlign1;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.Numbering;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.numbering.ParaHead;

import java.util.ArrayList;

public class ForNumberings extends Converter {
    private Numbering numbering;
    private kr.dogfoot.hwplib.object.docinfo.Numbering hwpNumbering;

    public ForNumberings(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList, ArrayList<kr.dogfoot.hwplib.object.docinfo.Numbering> hwpNumberingList) {
        if (hwpNumberingList.size() == 0) return;

        refList.createNumberings();

        int id = 1;
        for (kr.dogfoot.hwplib.object.docinfo.Numbering item : hwpNumberingList) {
            numbering = refList.numberings().addNew().idAnd(String.valueOf(id));
            hwpNumbering = item;

            numbering();
            id++;
        }
    }

    private void numbering() {
        numbering.start(hwpNumbering.getStartNumber());

        for (int level = 1; level <= 7; level++) {
            ParaHead paraHead = numbering.addNewParaHead().levelAnd((byte) level);
            LevelNumbering hwpLevelNumbering = null;
            try {
                hwpLevelNumbering = hwpNumbering.getLevelNumbering(level);
            } catch (Exception e) {
                hwpLevelNumbering = null;
            }
            if (hwpLevelNumbering != null) {
                paraHead(paraHead, hwpLevelNumbering);
            }
        }
    }

    private void paraHead(ParaHead paraHead, LevelNumbering hwpLevelNumbering) {
        paraHead
                .startAnd((int) hwpLevelNumbering.getStartNumber())
                .text(ValueConvertor.stringNullCheck(hwpLevelNumbering.getNumberFormat().toUTF16LEString()));


        paraHead(paraHead, hwpLevelNumbering.getParagraphHeadInfo());
    }

    public static void paraHead(ParaHead paraHead, ParagraphHeadInfo hwpParaHeadInfo) {
        paraHead
                .alignAnd(numberingAlign(hwpParaHeadInfo.getProperty().getParagraphAlignment()))
                .useInstWidthAnd(hwpParaHeadInfo.getProperty().isFollowStringWidth())
                .autoIndentAnd(hwpParaHeadInfo.getProperty().isAutoIndent())
                .widthAdjustAnd(hwpParaHeadInfo.getCorrectionValueForWidth())
                .textOffsetTypeAnd(ValueConvertor.valueUnit1(hwpParaHeadInfo.getProperty().getValueTypeForDistanceFromBody()))
                .textOffsetAnd(hwpParaHeadInfo.getDistanceFromBody())
                .numFormatAnd(ValueConvertor.numberType1(hwpParaHeadInfo.getProperty().getParagraphNumberFormat()))
                .charPrIDRefAnd(ValueConvertor.refID(hwpParaHeadInfo.getCharShapeID()))
                .checkable(false);
        // todo : checkable ??
    }


    private static HorizontalAlign1 numberingAlign(ParagraphAlignment hwpParaAlign) {
        switch (hwpParaAlign) {
            case Left:
                return HorizontalAlign1.LEFT;
            case Center:
                return HorizontalAlign1.CENTER;
            case Right:
                return HorizontalAlign1.RIGHT;
        }
        return HorizontalAlign1.LEFT;
    }
}

