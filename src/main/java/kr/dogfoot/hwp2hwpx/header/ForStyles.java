package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.docinfo.style.StyleSort;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.StyleType;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.Style;

import java.util.ArrayList;

public class ForStyles extends Converter {
    private Style style;
    private kr.dogfoot.hwplib.object.docinfo.Style hwpStyle;

    public ForStyles(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList, ArrayList<kr.dogfoot.hwplib.object.docinfo.Style> hwpStyleList) {
        if (hwpStyleList.size() == 0) return;

        refList.createStyles();

        int id = 0;
        for (kr.dogfoot.hwplib.object.docinfo.Style item : hwpStyleList) {
            style = refList.styles().addNew().idAnd(String.valueOf(id));
            hwpStyle = item;

            style();
            id++;
        }
    }

    private void style() {
        style
                .typeAnd(styleType(hwpStyle.getProeprty().getStyleSort()))
                .nameAnd(hwpStyle.getHangulName())
                .engNameAnd(hwpStyle.getEnglishName())
                .paraPrIDRefAnd(ValueConvertor.refID(hwpStyle.getParaShapeId()))
                .charPrIDRefAnd(ValueConvertor.refID(hwpStyle.getCharShapeId()))
                .nextStyleIDRefAnd(ValueConvertor.refID(hwpStyle.getNextStyleId()))
                .langIDAnd(String.valueOf(hwpStyle.getLanguageId()))
                .lockForm(false);

        // todo: lockForm ??
    }

    private StyleType styleType(StyleSort hwpStyleSort) {
        switch (hwpStyleSort) {
            case ParaStyle:
                return StyleType.PARA;
            case CharStyle:
                return StyleType.CHAR;
        }
        return StyleType.PARA;
    }
}
