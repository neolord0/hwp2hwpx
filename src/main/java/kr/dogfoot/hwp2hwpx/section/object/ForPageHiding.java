package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwplib.object.bodytext.control.ControlPageHide;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.PageHiding;

public class ForPageHiding {
    public static void convert(PageHiding pageHiding, ControlPageHide hwpPageHide) {
        pageHiding
                .hideHeaderAnd(hwpPageHide.getHeader().getProperty().isHideHeader())
                .hideFooterAnd(hwpPageHide.getHeader().getProperty().isHideFooter())
                .hideMasterPageAnd(hwpPageHide.getHeader().getProperty().isHideBatangPage())
                .hideBorderAnd(hwpPageHide.getHeader().getProperty().isHideBorder())
                .hideFillAnd(hwpPageHide.getHeader().getProperty().isHideFill())
                .hidePageNum(hwpPageHide.getHeader().getProperty().isHidePageNumber());
    }
}
