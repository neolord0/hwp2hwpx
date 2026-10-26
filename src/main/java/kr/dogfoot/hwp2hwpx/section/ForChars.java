package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.*;
import kr.dogfoot.hwp2hwpx.section.object.gso.ForGso;
import kr.dogfoot.hwp2hwpx.section.object.gso.form.ForForm;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.*;
import kr.dogfoot.hwplib.object.bodytext.control.gso.GsoControl;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.*;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.LineType2;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.TabItemType;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Para;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Run;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.T;


public class ForChars extends Converter {
    private Para para;
    private Paragraph hwpPara;

    int runCount;
    int runIndex;
    private Run currentRun;
    private final StringBuilder textBuffer;
    private T currentT;

    private final ForInlineControl inlineControlConverter;
    private final ForFieldBegin fieldBeginConverter;
    private final ForTable tableConverter;
    private final ForGso gsoConverter;
    private final ForEquation equationConverter;
    private final ForSecPr secPrConverter;
    private final ForHeader headerConverter;
    private final ForFooter footerConverter;
    private final ForFootnote footnoteConverter;
    private final ForEndnote endnoteConverter;
    private final ForForm formConverter;

    public ForChars(Parameter parameter) {
        super(parameter);
        textBuffer = new StringBuilder();

        inlineControlConverter = new ForInlineControl(parameter);
        fieldBeginConverter = new ForFieldBegin(parameter);
        tableConverter = new ForTable(parameter);
        gsoConverter = new ForGso(parameter);
        equationConverter = new ForEquation(parameter);
        secPrConverter = new ForSecPr(parameter);
        headerConverter = new ForHeader(parameter);
        footerConverter = new ForFooter(parameter);
        footnoteConverter = new ForFootnote(parameter);
        endnoteConverter = new ForEndnote(parameter);
        formConverter = new ForForm(parameter);
    }

    public void convert(Para para, Paragraph hwpPara) {
        if (hwpPara.getText() == null) return;

        this.para = para;
        this.hwpPara = hwpPara;

        runCount = para.countOfRun();
        runIndex = 0;
        currentRun = para.getRun(runIndex);
        currentT = null;

        int extendControlIndex = 0;
        long charPosition = 0;

        for (HWPChar hwpChar : hwpPara.getText().getCharList()) {
            switch (hwpChar.getType()) {
                case Normal:
                    normal((HWPCharNormal)hwpChar);
                    break;
                case ControlChar:
                    charControl((HWPCharControlChar)hwpChar);
                    break;
                case ControlInline:
                    inlineControl((HWPCharControlInline)hwpChar);
                    break;
                case ControlExtend:
                    extendControl((HWPCharControlExtend)hwpChar, extendControlIndex);
                    extendControlIndex++;
                    break;
            }

            charPosition += hwpChar.getCharSize();

            if (isNextRun(charPosition)) {
                nextRun();
                endT();
            }
        }

        endT();
    }

    private void nextRun() {
        runIndex++;
        currentRun = para.getRun(runIndex);
    }

    private boolean isNextRun(long charPosition) {
        long nextStartPosition = (runIndex + 1 < runCount) ? hwpPara.getCharShape().getPositonShapeIdPairList().get(runIndex + 1).getPosition() : 0xffff;
        return charPosition >= nextStartPosition;
    }

    private void normal(HWPCharNormal hwpChar) {
        startT();

        textBuffer.append((char) hwpChar.getCode());
    }

    private void charControl(HWPCharControlChar hwpChar) {
        switch (hwpChar.getCode()) {
            case 10: // 한 줄 끝(line break)
                addLineBreakFromCurrentT();
                break;
            case 13:
                startT();
                break;
            case 24: // 하이픈
                addHyphenFromCurrentT();
                break;
            case 30: // 묶음 빈칸
                addNBSpaceFromCurrentT();
                break;
            case 31: // 고정폭 빈칸
                addFWSpaceFromCurrentT();
                break;
        }
    }

    private void addLineBreakFromCurrentT() {
        startT();
        if (textBuffer.length() > 0) {
            currentT.addText(textBuffer.toString());
            textBuffer.setLength(0);
        }
        currentT.addNewLineBreak();
    }

    private void addHyphenFromCurrentT() {
        startT();
        if (textBuffer.length() > 0) {
            textBuffer.setLength(0);
        }
        currentT.addNewHyphen();
    }

    private void addNBSpaceFromCurrentT() {
        startT();
        if (textBuffer.length() > 0) {
            currentT.addText(textBuffer.toString());
            textBuffer.setLength(0);
        }
        currentT.addNewNBSpace();
    }

    private void addFWSpaceFromCurrentT() {
        startT();
        if (textBuffer.length() > 0) {
            currentT.addText(textBuffer.toString());
            textBuffer.setLength(0);
        }
        currentT.addNewFWSpace();
    }

    private void inlineControl(HWPCharControlInline hwpChar) {
        if (hwpChar.getCode() == 4) {
            endT();
            inlineControlConverter.fieldEnd(currentRun.addNewCtrl().addNewFieldEnd(), hwpChar);
        } else if (hwpChar.getCode() == 9) {
            addTab(hwpChar);
        }
    }

    /**
     * 탭 문자의 폭·채움·종류를 HWP 에서 옮긴다.
     * HWP 탭 문자의 부가 12바이트 = 폭(UINT32) + 채움(1바이트) + 종류(1바이트) + 나머지 — 한글은 이 값을 HWPX `hp:tab` 에 그대로 쓴다(한글 저장본 대조).
     * 4000·없음·왼쪽으로 고정하면 차례의 점선 채움과 오른쪽 정렬 쪽 번호가 사라진다.
     */
    private void addTab(HWPCharControlInline hwpChar) {
        startT();
        if (textBuffer.length() > 0) {
            currentT.addText(textBuffer.toString());
            textBuffer.setLength(0);
        }

        TabInfo tabInfo = tabInfo(hwpChar.getAddition());

        currentT.addNewTab()
                .widthAnd(tabInfo.width())
                .leaderAnd(tabInfo.leader())
                .type(tabInfo.type());
    }

    private TabInfo tabInfo(byte[] tabAddition) {
        if (tabAddition == null || tabAddition.length < 6) return new TabInfo(4000, LineType2.NONE, TabItemType.LEFT);

        int width = (tabAddition[0] & 0xff) | ((tabAddition[1] & 0xff) << 8) | ((tabAddition[2] & 0xff) << 16) | ((tabAddition[3] & 0xff) << 24);
        LineType2 leader = LineType2.fromIndex(tabAddition[4] & 0xff);
        TabItemType type = TabItemType.fromIndex(tabAddition[5] & 0xff);

        return new TabInfo(width,
                (leader == null) ? LineType2.NONE : leader,
                (type == null) ? TabItemType.LEFT : type);
    }

    private void extendControl(HWPCharControlExtend hwpChar, int extendControlIndex) {
        endT();

        if (hwpPara == null || hwpPara.getControlList() == null) return;

        Control hwpControl = hwpPara.getControlList().get(extendControlIndex);
        if (hwpControl.isField()) {
            fieldBeginConverter.convent(currentRun.addNewCtrl().addNewFieldBegin(), (ControlField) hwpControl);
        } else {
            switch (hwpControl.getType()) {
                case Table:
                    tableConverter.convert(currentRun.addNewTable(), (ControlTable) hwpControl);
                    break;
                case Gso:
                    gsoConverter.convert(currentRun, (GsoControl) hwpControl);
                    break;
                case Equation:
                    equationConverter.convert(currentRun.addNewEquation(), (ControlEquation) hwpControl);
                    break;
                case SectionDefine:
                    currentRun.createSecPr();
                    secPrConverter.convert(currentRun.secPr(), (ControlSectionDefine) hwpControl);
                    break;
                case ColumnDefine:
                    ForColPr.convert(currentRun.addNewCtrl().addNewColPr(), (ControlColumnDefine) hwpControl);
                    break;
                case Header:
                    headerConverter.convert(currentRun.addNewCtrl().addNewHeader(), (ControlHeader) hwpControl);
                    break;
                case Footer:
                    footerConverter.convert(currentRun.addNewCtrl().addNewFooter(), (ControlFooter) hwpControl);
                    break;
                case Footnote:
                    footnoteConverter.convert(currentRun.addNewCtrl().addNewFootNote(), (ControlFootnote) hwpControl);
                    break;
                case Endnote:
                    endnoteConverter.convert(currentRun.addNewCtrl().addNewEndNote(), (ControlEndnote) hwpControl);
                    break;
                case AutoNumber:
                    ForAutoNum.convert(currentRun.addNewCtrl().addNewAutoNum(), (ControlAutoNumber) hwpControl);
                    break;
                case NewNumber:
                    ForNewNum.convert(currentRun.addNewCtrl().addNewNewNum(), (ControlNewNumber) hwpControl);
                    break;
                case PageHide:
                    ForPageHiding.convert(currentRun.addNewCtrl().addNewPageHiding(), (ControlPageHide) hwpControl);
                case PageOddEvenAdjust:
                    // todo : ??
                    break;
                case PageNumberPosition:
                    ForPageNum.convert(currentRun.addNewCtrl().addNewPageNum(), (ControlPageNumberPosition) hwpControl);
                    break;
                case IndexMark:
                    // todo : ??
                    break;
                case Bookmark:
                    ForBookmark.convert(currentRun.addNewCtrl().addNewBookmark(), (ControlBookmark) hwpControl);
                    break;
                case OverlappingLetter:
                    ForCompose.convert(currentRun.addNewCompose(), (ControlOverlappingLetter) hwpControl);
                    break;
                case AdditionalText:
                    ForDutmal.convert(currentRun.addNewDutmal(), (ControlAdditionalText) hwpControl);
                    break;
                case HiddenComment:
                    parameter.subListConverter().convertForHiddenComment(currentRun.addNewCtrl().addNewHiddenComment(), (ControlHiddenComment) hwpControl);
                    break;
                case Form:
                    formConverter.convert(currentRun, (ControlForm) hwpControl);
                    break;
            }
        }
    }

    private void startT() {
        if (currentT != null) return;

        currentT = currentRun.addNewT();
        textBuffer.setLength(0);
    }

    private void endT() {
        if (currentT == null) return;

        currentT.addText(textBuffer.toString());
        currentT = null;
    }

    private static class TabInfo {
        private int width;
        private LineType2 leader;
        private TabItemType type;

        public TabInfo(int width, LineType2 leader, TabItemType type) {
            this.width = width;
            this.leader = leader;
            this.type = type;
        }

        public int width() {
            return width;
        }

        public LineType2 leader() {
            return leader;
        }

        public TabItemType type() {
            return type;
        }
    }
}
