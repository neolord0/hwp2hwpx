package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.table.Tc;

public class ForCell extends Converter {
    private Tc tc;
    private Cell hwpCell;

    public ForCell(Parameter parameter) {
        super(parameter);
    }

    public void convert(Tc tc, Cell hwpCell) {
        this.tc = tc;
        this.hwpCell = hwpCell;

        tc
                .nameAnd(hwpCell.getListHeader().getFieldName())
                .headerAnd(hwpCell.getListHeader().getProperty().isTitleCell())
                .hasMarginAnd(hwpCell.getListHeader().getProperty().isApplyInnerMagin())
                .protectAnd(hwpCell.getListHeader().getProperty().isProtectCell())
                .editableAnd(hwpCell.getListHeader().getProperty().isEditableAtFormMode())
                .dirtyAnd(false)
                .borderFillIDRef(ValueConvertor.refID(hwpCell.getListHeader().getBorderFillId()));
        // todo : dirty ??

        cellAddr();
        cellSpan();
        cellSz();
        cellMargin();
        parameter.subListConverter().convertForCell(tc, hwpCell);
    }

    private void cellAddr() {
        tc.createCellAddr();
        tc.cellAddr()
                .colAddrAnd((short) hwpCell.getListHeader().getColIndex())
                .rowAddr((short) hwpCell.getListHeader().getRowIndex());
    }

    private void cellSpan() {
        tc.createCellSpan();
        tc.cellSpan()
                .colSpanAnd((short) hwpCell.getListHeader().getColSpan())
                .rowSpan((short) hwpCell.getListHeader().getRowSpan());
    }

    private void cellSz() {
        tc.createCellSz();
        tc.cellSz()
                .widthAnd(hwpCell.getListHeader().getWidth())
                .height(hwpCell.getListHeader().getHeight());
    }

    private void cellMargin() {
        tc.createCellMargin();
        tc.cellMargin()
                .leftAnd((long) hwpCell.getListHeader().getLeftMargin())
                .rightAnd((long) hwpCell.getListHeader().getRightMargin())
                .topAnd((long) hwpCell.getListHeader().getTopMargin())
                .bottom((long) hwpCell.getListHeader().getBottomMargin());
    }
}
