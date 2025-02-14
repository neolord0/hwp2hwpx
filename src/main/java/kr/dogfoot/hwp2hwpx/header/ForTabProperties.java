package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.docinfo.TabDef;
import kr.dogfoot.hwplib.object.docinfo.tabdef.TabInfo;
import kr.dogfoot.hwplib.object.docinfo.tabdef.TabSort;
import kr.dogfoot.hwpxlib.object.common.compatibility.Case;
import kr.dogfoot.hwpxlib.object.common.compatibility.Default;
import kr.dogfoot.hwpxlib.object.common.compatibility.Switch;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.TabItemType;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.TabPr;
import kr.dogfoot.hwpxlib.object.content.header_xml.references.tabpr.TabItem;

import java.util.ArrayList;

public class ForTabProperties extends Converter {
    private TabPr tabPr;
    private TabDef hwpTabDef;

    public ForTabProperties(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList, ArrayList<TabDef> hwpTabDefList) {
        if (hwpTabDefList.size() == 0) return;

        refList.createTabProperties();

        int id = 0;
        for (TabDef item : hwpTabDefList) {
            tabPr = refList.tabProperties().addNew().idAnd(String.valueOf(id));
            hwpTabDef = item;

            tabPr();
            id++;

        }
    }

    private void tabPr() {
        tabPr
                .autoTabLeftAnd(hwpTabDef.getProperty().isAutoTabAtParagraphLeftEnd())
                .autoTabRight(hwpTabDef.getProperty().isAutoTabAtParagraphRightEnd());

        switchForTabItem();
    }

    private void switchForTabItem() {
        int switchPosition = 1;

        for (TabInfo hwpTabInfo : hwpTabDef.getTabInfoList()) {
            Switch switchObject = tabPr.addNewSwitch();
            switchObject.position(switchPosition);
            {
                Case caseObject = switchObject.addNewCaseObject()
                        .requiredNamespaceAnd("http://www.hancom.co.kr/hwpml/2016/HwpUnitChar");

                TabItem tabItem = new TabItem()
                        .posAnd((int) hwpTabInfo.getPosition() / 2)
                        .typeAnd(tabItemType(hwpTabInfo.getTabSort()))
                        .leaderAnd(ValueConvertor.lineType2(hwpTabInfo.getFillSort()));
                caseObject.addChild(tabItem);
            }

            {
                switchObject.createDefaultObject();
                Default defaultObject = switchObject.defaultObject();

                TabItem tabItem = new TabItem()
                        .posAnd((int) hwpTabInfo.getPosition())
                        .typeAnd(tabItemType(hwpTabInfo.getTabSort()))
                        .leaderAnd(ValueConvertor.lineType2(hwpTabInfo.getFillSort()));
                defaultObject.addChild(tabItem);
            }
            switchPosition++;
        }
    }

    private TabItemType tabItemType(TabSort tabSort) {
        switch (tabSort) {
            case Left:
                return TabItemType.LEFT;
            case Right:
                return TabItemType.RIGHT;
            case Center:
                return TabItemType.CENTER;
            case DecimalPoint:
                return TabItemType.DECIMAL;
        }
        return TabItemType.LEFT;
    }
}
