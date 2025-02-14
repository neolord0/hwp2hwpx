package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.docinfo.MemoShape;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.MemoType;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.MemoPr;

import java.util.ArrayList;

public class ForMemoProperties extends Converter {
    private MemoPr memoPr;
    private MemoShape hwpMemoShape;

    public ForMemoProperties(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList, ArrayList<MemoShape> memoShapeList) {
        if (memoShapeList.size() == 0) return;

        refList.createMemoProperties();

        int id = 1;
        for (MemoShape item : memoShapeList) {
            memoPr = refList.memoProperties().addNew().idAnd(String.valueOf(id));
            hwpMemoShape = item;

            memoPr();
            id++;
        }
    }

    private void memoPr() {
        memoPr
                .widthAnd((int) hwpMemoShape.getWidth())
                .lineTypeAnd(ValueConvertor.lineType2(hwpMemoShape.getLineType()))
                .lineWidthAnd(ValueConvertor.lineWidth(hwpMemoShape.getLineWidth()))
                .lineColorAnd(ValueConvertor.color(hwpMemoShape.getLineColor()))
                .fillColorAnd(ValueConvertor.color(hwpMemoShape.getFillColor()))
                .activeColorAnd(ValueConvertor.color(hwpMemoShape.getActiveColor()))
                .memoType(MemoType.NORMAL);
        // todo : memoType ??
    }
}
