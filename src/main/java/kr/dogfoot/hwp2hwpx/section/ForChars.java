package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.control.Control;
import kr.dogfoot.hwplib.object.bodytext.control.ControlColumnDefine;
import kr.dogfoot.hwplib.object.bodytext.control.ControlField;
import kr.dogfoot.hwplib.object.bodytext.control.ControlSectionDefine;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.*;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Ctrl;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Para;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Run;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.T;

import java.io.UnsupportedEncodingException;

public class ForChars extends Converter {
    private Para para;
    private Paragraph hwpPara;

    int runCount;
    int runIndex;
    private Run currentRun;
    private Ctrl currentCtrl;
    private StringBuilder textBuffer;
    private T currentT;
    private ForInlineControl inlineControlConvert;

    public ForChars(Parameter parameter) {
        super(parameter);
        textBuffer = new StringBuilder();
        inlineControlConvert = new ForInlineControl(parameter);
    }

    public void convert(Para para, Paragraph hwpPara) {
        if (hwpPara.getText() == null) {
            return;
        }

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
                endCtrl();
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
        endCtrl();
        startT();

        try {
            textBuffer.append(hwpChar.getCh());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    private void charControl(HWPCharControlChar hwpChar) {
        endT();

        System.out.println("CC: " + para.getRunIndex(currentRun) + " " + hwpChar.getCode());
    }

    private void inlineControl(HWPCharControlInline hwpChar) {
        endT();
        if (hwpChar.getCode() == 4) {
            startCtrl();
            inlineControlConvert.fieldEnd(currentCtrl.addNewFieldEnd(), hwpChar);
        } else {
            endCtrl();
        }
        System.out.println("IC: " + para.getRunIndex(currentRun) + " " + hwpChar.getCode());
    }

    private void extendControl(HWPCharControlExtend hwpChar, int extendControlIndex) {
        endT();

        Control hwpControl = hwpPara.getControlList().get(extendControlIndex);
        if (hwpControl.isField()) {
            startCtrl();
            new ForFieldBegin(parameter).convent(currentCtrl.addNewFieldBegin(), (ControlField) hwpControl);
        } else {
            switch (hwpControl.getType()) {
                case Table:
                    break;
                case Gso:
                    break;
                case Equation:
                    break;
                case SectionDefine:
                    endCtrl();
                    currentRun.createSecPr();
                    new ForSecPr(parameter).convert(currentRun.secPr(), (ControlSectionDefine) hwpControl);
                    break;
                case ColumnDefine:
                    startCtrl();
                    ForColPr.convert(currentCtrl.addNewColPr(), (ControlColumnDefine) hwpControl);
                    break;
                case Header:
                    break;
                case Footer:
                    break;
                case Footnote:
                    break;
                case Endnote:
                    break;
                case AutoNumber:
                    break;
                case NewNumber:
                    break;
                case PageHide:
                    break;
                case PageOddEvenAdjust:
                    break;
                case PageNumberPosition:
                    break;
                case IndexMark:
                    break;
                case Bookmark:
                    break;
                case OverlappingLetter:
                    break;
                case AdditionalText:
                    break;
                case HiddenComment:
                    break;
                case Form:
                    break;
            }
        }
    }

    private void startCtrl() {
        if (currentCtrl == null) {
            currentCtrl = currentRun.addNewCtrl();
        }
    }

    private void endCtrl() {
        currentCtrl = null;
    }

    private void startT() {
        if (currentT == null) {
            currentT = currentRun.addNewT();
            textBuffer.setLength(0);
        }
    }

    private void endT() {
        if (currentT != null) {
            currentT.addText(textBuffer.toString());
            currentT = null;
        }
    }
}
