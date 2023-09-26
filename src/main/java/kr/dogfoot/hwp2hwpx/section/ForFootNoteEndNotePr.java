package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.EndNoteDisplayMethod;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.FootEndNoteShape;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.FootNoteDisplayMethod;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.NumberingMethod;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.EndNoteNumberingType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.EndNotePlace;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.FootNoteNumberingType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.FootNotePlace;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.secpr.SecPr;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.secpr.notepr.EndNotePr;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.secpr.notepr.FootNotePr;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.secpr.notepr.NoteShape;

public class ForFootNoteEndNotePr {
    public static void footNotePr(SecPr secPr, FootEndNoteShape hwpFootNoteShape) {
        secPr.createFootNotePr();
        FootNotePr footNotePr = secPr.footNotePr();

        autoNumFormat(footNotePr, hwpFootNoteShape);
        noteLine(footNotePr, hwpFootNoteShape);
        noteSpacing(footNotePr, hwpFootNoteShape);
        numbering(footNotePr, hwpFootNoteShape);
        placement(footNotePr, hwpFootNoteShape);
    }

    private static void autoNumFormat(NoteShape noteShape, FootEndNoteShape hwpFootNoteShape) {
        noteShape.createAutoNumFormat();
        noteShape.autoNumFormat()
                .typeAnd(ValueConvertor.toNumberType2(hwpFootNoteShape.getProperty().getNumberShape()))
                .userCharAnd(ValueConvertor.stringNullCheck(hwpFootNoteShape.getUserSymbol().toUTF16LEString()))
                .prefixCharAnd(ValueConvertor.stringNullCheck(hwpFootNoteShape.getBeforeDecorativeLetter().toUTF16LEString()))
                .suffixCharAnd(ValueConvertor.stringNullCheck(hwpFootNoteShape.getAfterDecorativeLetter().toUTF16LEString()))
                .supscript(hwpFootNoteShape.getProperty().isDisplayWithSupscript());
    }

    private static void noteLine(NoteShape noteShape, FootEndNoteShape hwpFootNoteShape) {
        noteShape.createNoteLine();
        noteShape.noteLine()
                .lengthAnd((int) hwpFootNoteShape.getDivideLineLength())
                .typeAnd(ValueConvertor.lineType2(hwpFootNoteShape.getDivideLine().getType()))
                .widthAnd(ValueConvertor.lineWidth(hwpFootNoteShape.getDivideLine().getThickness()))
                .color(ValueConvertor.color(hwpFootNoteShape.getDivideLine().getColor()));
    }

    private static void noteSpacing(NoteShape noteShape, FootEndNoteShape hwpFootNoteShape) {
        noteShape.createNoteSpacing();
        noteShape.noteSpacing()
                .betweenNotesAnd(hwpFootNoteShape.getBetweenNotesMargin())
                .belowLineAnd(hwpFootNoteShape.getDivideLineBottomMargin())
                .aboveLine(hwpFootNoteShape.getDivideLineTopMargin());
    }

    private static void numbering(FootNotePr footNotePr, FootEndNoteShape hwpFootNoteShape) {
        footNotePr.createNumbering();
        footNotePr.numbering()
                .typeAnd(footNoteNumberingType(hwpFootNoteShape.getProperty().getNumberingMethod()))
                .newNum(hwpFootNoteShape.getStartNumber());
    }

    private static FootNoteNumberingType footNoteNumberingType(NumberingMethod hwpNumberingMethod) {
        switch (hwpNumberingMethod) {
            case Continue:
                return FootNoteNumberingType.CONTINUOUS;
            case Restart:
                return FootNoteNumberingType.ON_SECTION;
            case RestartAtEachPage:
                return FootNoteNumberingType.ON_PAGE;
        }
        return FootNoteNumberingType.CONTINUOUS;
    }

    private static void placement(FootNotePr footNotePr, FootEndNoteShape hwpFootNoteShape) {
        footNotePr.createPlacement();
        footNotePr.placement()
                .placeAnd(footNotePlace(hwpFootNoteShape.getProperty().getFootNoteDisplayMethod()))
                .beneathText(hwpFootNoteShape.getProperty().isContinueFromText());
    }

    private static FootNotePlace footNotePlace(FootNoteDisplayMethod hwpFootNoteDisplayMethod) {
        switch (hwpFootNoteDisplayMethod) {
            case EachColumn:
                return FootNotePlace.EACH_COLUMN;
            case AllColumn:
                return FootNotePlace.MERGED_COLUMN;
            case RightColumn:
                return FootNotePlace.RIGHT_MOST_COLUMN;
        }
        return FootNotePlace.EACH_COLUMN;
    }

    public static void endNotePr(SecPr secPr, FootEndNoteShape hwpFootNoteShape) {
        secPr.createEndNotePr();
        EndNotePr endNotePr = secPr.endNotePr();

        autoNumFormat(endNotePr, hwpFootNoteShape);
        noteLine(endNotePr, hwpFootNoteShape);
        noteSpacing(endNotePr, hwpFootNoteShape);
        numbering(endNotePr, hwpFootNoteShape);
        placement(endNotePr, hwpFootNoteShape);
    }

    private static void numbering(EndNotePr endNotePr, FootEndNoteShape hwpFootNoteShape) {
        endNotePr.createNumbering();
        endNotePr.numbering()
                .typeAnd(endNoteNumberingType(hwpFootNoteShape.getProperty().getNumberingMethod()))
                .newNum(hwpFootNoteShape.getStartNumber());
    }

    private static EndNoteNumberingType endNoteNumberingType(NumberingMethod hwpNumberingMethod) {
        switch (hwpNumberingMethod) {
            case Continue:
                return EndNoteNumberingType.CONTINUOUS;
            case Restart:
                return EndNoteNumberingType.ON_SECTION;
            case RestartAtEachPage:
                break;
        }
        return EndNoteNumberingType.CONTINUOUS;
    }

    private static void placement(EndNotePr endNotePr, FootEndNoteShape hwpFootNoteShape) {
        endNotePr.createPlacement();
        endNotePr.placement()
                .placeAnd(endNotePlace(hwpFootNoteShape.getProperty().getEndNoteDisplayMethod()))
                .beneathText(hwpFootNoteShape.getProperty().isContinueFromText());
    }

    private static EndNotePlace endNotePlace(EndNoteDisplayMethod hwpEndNoteDisplayMethod) {
        switch (hwpEndNoteDisplayMethod) {
            case DocumentEnd:
                return EndNotePlace.END_OF_DOCUMENT;
            case SectionEnd:
                return EndNotePlace.END_OF_SECTION;
        }
        return EndNotePlace.END_OF_DOCUMENT;
    }

}
