package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeObject;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlTable;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwplib.object.bodytext.control.table.DivideAtPageBoundary;
import kr.dogfoot.hwplib.object.bodytext.control.table.Row;
import kr.dogfoot.hwplib.object.bodytext.control.table.ZoneInfo;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.TablePageBreak;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Table;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.table.CellZone;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.table.Tr;

public class ForTable extends ForShapeObject {
    private Table table;
    private ControlTable hwpTable;
    private ForCell cellConverter;

    public ForTable(Parameter parameter) {
        super(parameter);

        cellConverter = new ForCell(parameter);
    }

    public void convert(Table table, ControlTable hwpTable) {
        this.table = table;
        this.hwpTable = hwpTable;

        shapeObject(table, hwpTable.getHeader(), hwpTable.getCaption());

        table
                .pageBreakAnd(tablePageBreak(hwpTable.getTable().getProperty().getDivideAtPageBoundary()))
                .repeatHeaderAnd(hwpTable.getTable().getProperty().isAutoRepeatTitleRow())
                .rowCntAnd((short) hwpTable.getTable().getRowCount())
                .colCntAnd((short) hwpTable.getTable().getColumnCount())
                .cellSpacingAnd(hwpTable.getTable().getCellSpacing())
                .borderFillIDRefAnd(ValueConvertor.refID(hwpTable.getTable().getBorderFillId()))
                .noAdjust(false);

        inMargin();
        cellzoneList();
        trs();
    }


    private TablePageBreak tablePageBreak(DivideAtPageBoundary hwpDivideAtPageBoundary) {
        switch (hwpDivideAtPageBoundary) {
            case NoDivide:
                return TablePageBreak.NONE;
            case DivideByCell:
                return TablePageBreak.CELL;
            case Divide:
                return TablePageBreak.TABLE;
        }
        return TablePageBreak.CELL;
    }

    private void inMargin() {
        table.createInMargin();
        table.inMargin()
                .leftAnd((long) hwpTable.getTable().getLeftInnerMargin())
                .rightAnd((long) hwpTable.getTable().getRightInnerMargin())
                .topAnd((long) hwpTable.getTable().getTopInnerMargin())
                .bottom((long) hwpTable.getTable().getBottomInnerMargin());
    }

    private void cellzoneList() {
        if (hwpTable.getTable().getZoneInfoList().size() == 0) return;

        table.createCellzoneList();
        for (ZoneInfo zoneInfo : hwpTable.getTable().getZoneInfoList()) {
            cellzone(table.cellzoneList().addNew(), zoneInfo);
        }
    }

    private void cellzone(CellZone cellZone, ZoneInfo zoneInfo) {
        cellZone
                .startRowAddrAnd((short) zoneInfo.getStartRow())
                .startColAddrAnd((short) zoneInfo.getStartColumn())
                .endRowAddrAnd((short) zoneInfo.getEndRow())
                .endColAddrAnd((short) zoneInfo.getEndColumn())
                .borderFillIDRef(ValueConvertor.refID(zoneInfo.getBorderFillId()));
    }

    private void trs() {
        for (Row hwpRow : hwpTable.getRowList()) {
            Tr tr = table.addNewTr();

            for (Cell hwpCell : hwpRow.getCellList()) {
                cellConverter.convert(tr.addNewTc(), hwpCell);
            }
        }
    }
}
