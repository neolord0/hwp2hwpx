package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwplib.object.bodytext.control.ControlBookmark;
import kr.dogfoot.hwplib.object.bodytext.control.bookmark.ParameterItem;
import kr.dogfoot.hwplib.object.bodytext.control.bookmark.ParameterType;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.Bookmark;

public class ForBookmark {
    public static void convert(Bookmark bookmark, ControlBookmark hwpBookmark) {
        bookmark.name(getName(hwpBookmark));
    }

    private static String getName(ControlBookmark hwpBookmark) {
        if (hwpBookmark.getCtrlData() != null) {
            ParameterItem parameterItem  = hwpBookmark.getCtrlData().getParameterSet().getParameterItem(16384);
            if (parameterItem != null && parameterItem.getType() == ParameterType.String) {
                return parameterItem.getValue_BSTR();
            }
        }
        return "";
    }

}
