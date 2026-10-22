package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.docinfo.DocInfo;
import kr.dogfoot.hwplib.object.docinfo.documentproperties.StartNumber;
import kr.dogfoot.hwpxlib.object.content.header_xml.CompatibleDocument;
import kr.dogfoot.hwpxlib.object.content.header_xml.DocOption;
import kr.dogfoot.hwpxlib.object.content.header_xml.HeaderXMLFile;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.TargetProgramSort;

public class ForHeaderXMLFile extends Converter {
    private HeaderXMLFile headerXMLFile;
    private DocInfo hwpDocInfo;

    public ForHeaderXMLFile(Parameter parameter) {
        super(parameter);
    }

    public void convert() {
        headerXMLFile = parameter.hwpx().headerXMLFile();
        hwpDocInfo = parameter.hwp().docInfo();

        headerXMLFile
                .versionAnd("1.4")
                .secCnt((short) hwpDocInfo.getDocumentProperties().getSectionCount());

        beginNum();
        refList();
        compatibleDocument();
        docOption();
        trackChangeConfig();
    }

    private void beginNum() {
        StartNumber hwpStartNumber = hwpDocInfo.getDocumentProperties().getStartNumber();

        headerXMLFile.createBeginNum();
        headerXMLFile.beginNum()
                .pageAnd(hwpStartNumber.getPage())
                .footnoteAnd(hwpStartNumber.getFootnote())
                .endnoteAnd(hwpStartNumber.getEndnote())
                .picAnd(hwpStartNumber.getPicture())
                .tblAnd(hwpStartNumber.getTable())
                .equation(hwpStartNumber.getEquation());
    }

    private void refList() {
        headerXMLFile.createRefList();

        new ForFontfaces(parameter).convert(headerXMLFile.refList(), hwpDocInfo);
        new ForBorderFills(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getBorderFillList());
        new ForCharProperties(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getCharShapeList());
        new ForTabProperties(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getTabDefList());
        new ForNumberings(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getNumberingList());
        new ForBullets(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getBulletList());
        new ForParaProperties(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getParaShapeList());
        new ForStyles(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getStyleList());
        new ForMemoProperties(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getMemoShapeList());
        new ForTrackChanges(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getTrackChange2List());
        new ForTrackChangeAuthors(parameter).convert(headerXMLFile.refList(), hwpDocInfo.getTrackChangeAuthorList());
    }

    /**
     * 호환 문서 대상과 배치 호환 항목을 HWP 에서 옮긴다.
     * 대상을 HWP201X 로 고정하고 항목을 비우면 「MS Word 호환」 문서의 줄 나눔·쪽 수가 크게 달라진다.
     * 한글 동작(빈 HWP 에 대상·배치 호환 비트를 넣고 한글로 HWPX 저장 — 23가지 조합 대조):
     *   - 항목은 비트별로 옮겨지지 않는다 — 5개 단위 비트가 하나라도 켜져 있으면 대상별 고정 묶음, 전부 0 이면 빈 목록
     *   - MSWord → MS_WORD + 35개 · HWP2007 → HWP200X + 12개(켜진 패턴과 무관하게 같음)
     *   - HWPCurrent → HWP201X + 빈 목록(비트를 다 켜도 빈 목록)
     */
    static final String[] MS_WORD_ITEMS = {
            "applyFontWeightToBold", "useInnerUnderline", "useLowercaseStrikeout", "extendLineheightToOffset",
            "treatQuotationAsLatin", "doNotAlignWhitespaceOnRight", "doNotAdjustWordInJustify", "baseCharUnitOnEAsian",
            "baseCharUnitOfIndentOnFirstChar", "adjustLineheightToFont", "adjustBaselineInFixedLinespacing",
            "applyPrevspacingBeneathObject", "applyNextspacingOfLastPara", "adjustParaBorderfillToSpacing",
            "connectParaBorderfillOfEqualBorder", "adjustParaBorderOffsetWithBorder", "extendLineheightToParaBorderOffset",
            "applyParaBorderToOutside", "applyMinColumnWidthTo1mm", "applyTabPosBasedOnSegment", "breakTabOverLine",
            "adjustVertPosOfLine", "doNotAlignLastForbidden", "adjustMarginFromAdjustLineheight", "baseLineSpacingOnLineGrid",
            "applyCharSpacingToCharGrid", "doNotApplyGridInHeaderFooter", "applyExtendHeaderFooterEachSection",
            "doNotApplyLinegridAtNoLinespacing", "doNotAdjustEmptyAnchorLine", "overlapBothAllowOverlap",
            "extendVertLimitToPageMargins", "doNotHoldAnchorOfTable", "doNotFormattingAtBeneathAnchor",
            "adjustBaselineOfObjectToBottom"};
    static final String[] HWP200X_ITEMS = {
            "fixedUnderlineWidth", "doNotApplyStrikeoutWithUnderline", "doNotApplyDiacSymMarkOfNoneAndSix",
            "applyAtLeastToPercent100Pct", "doNotApplyAutoSpaceEAsianEng", "doNotApplyAutoSpaceEAsianNum",
            "doNotApplyHeaderFooterAtNoSpace", "doNotApplyColSeparatorAtNoGap", "doNotApplyImageEffect",
            "doNotApplyShapeComment", "doNotApplyVertOffsetOfForward", "doNotApplyExtensionCharCompose"};

    private void compatibleDocument() {
        headerXMLFile.createCompatibleDocument();
        CompatibleDocument compatibleDocument = headerXMLFile.compatibleDocument();

        TargetProgramSort target = TargetProgramSort.HWP201X;
        String[] items = new String[0];
        kr.dogfoot.hwplib.object.docinfo.CompatibleDocument hwpCompat = hwpDocInfo.getCompatibleDocument();
        if (hwpCompat != null && hwpCompat.getTargetProgream() != null) {
            switch (hwpCompat.getTargetProgream()) {
                case MSWord:
                    target = TargetProgramSort.MS_WORD;
                    items = MS_WORD_ITEMS;
                    break;
                case HWP2007:
                    target = TargetProgramSort.HWP200X;
                    items = HWP200X_ITEMS;
                    break;
                default:
                    break;
            }
        }
        compatibleDocument.targetProgram(target);
        compatibleDocument.createLayoutCompatibility();

        kr.dogfoot.hwplib.object.docinfo.LayoutCompatibility l = hwpDocInfo.getLayoutCompatibility();
        boolean anyBit = l != null && (l.getLetterLevelFormat() | l.getParagraphLevelFormat() | l.getSectionLevelFormat()
                | l.getObjectLevelFormat() | l.getFieldLevelFormat()) != 0;
        if (anyBit) {
            for (String name : items) {
                compatibleDocument.layoutCompatibility().addNew().name(name);
            }
        }
    }

    private void docOption() {
        headerXMLFile.createDocOption();
        DocOption docOption = headerXMLFile.docOption();
        docOption.createLinkinfo();
        docOption.linkinfo()
                .pathAnd("")
                .pageInheritAnd(false)
                .footnoteInherit(false);
    }

    private void trackChangeConfig() {
        headerXMLFile.createTrackChangeConfig();
        headerXMLFile.trackChangeConfig().flags(56);
    }
}
