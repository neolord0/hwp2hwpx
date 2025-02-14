package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlColumnDefine;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.CtrlHeaderColumnDefine;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.columndefine.ColumnInfo;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.columndefine.ColumnSort;
import kr.dogfoot.hwplib.object.docinfo.borderfill.BorderType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.ColumnDirection;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.MultiColumnType;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.ColPr;

public class ForColPr {
    public static void convert(ColPr colPr, ControlColumnDefine hwpColumnDefine) {
        CtrlHeaderColumnDefine header = hwpColumnDefine.getHeader();

        colPr
                .idAnd("")
                .typeAnd(multicolumnType(header.getProperty().getColumnSort()))
                .layoutAnd(columnDirection(header.getProperty().getColumnDirection()))
                .colCountAnd((int) header.getProperty().getColumnCount())
                .sameSzAnd(header.getProperty().isSameWidth())
                .sameGap(header.getGapBetweenColumn());

        colSz(colPr, header);
        colLine(colPr, header);
    }

    private static MultiColumnType multicolumnType(ColumnSort hwpColumnSort) {
        switch (hwpColumnSort) {
            case Normal:
                return MultiColumnType.NEWSPAPER;
            case Distribution:
                return MultiColumnType.BALANCED_NEWSPAPER;
            case Parallel:
                return MultiColumnType.PARALLEL;
        }
        return MultiColumnType.NEWSPAPER;
    }

    private static ColumnDirection columnDirection(kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.columndefine.ColumnDirection hwpColumnDirection) {
        switch (hwpColumnDirection) {
            case FromLeft:
                return ColumnDirection.LEFT;
            case FromRight:
                return ColumnDirection.RIGHT;
            case Both:
                return ColumnDirection.MIRROR;
        }
        return ColumnDirection.LEFT;
    }

    private static void colSz(ColPr colPr, CtrlHeaderColumnDefine header) {
        for (ColumnInfo columnInfo : header.getColumnInfoList()) {
            colPr.addNewColSz()
                    .widthAnd(columnInfo.getWidth())
                    .gap(columnInfo.getGap());
        }
    }

    private static void colLine(ColPr colPr, CtrlHeaderColumnDefine header) {
        if (header.getDivideLine().getType() == BorderType.None) return;

        colPr.createColLine();
        colPr.colLine()
                .typeAnd(ValueConvertor.lineType2(header.getDivideLine().getType()))
                .widthAnd(ValueConvertor.lineWidth(header.getDivideLine().getThickness()))
                .color(ValueConvertor.color(header.getDivideLine().getColor()));
    }
}
