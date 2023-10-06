package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlPageNumberPosition;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.pagenumberposition.NumberPosition;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.PageNumPosition;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.PageNum;

public class ForPageNum {
    public static void convert(PageNum pageNum, ControlPageNumberPosition hwpPageNumberPosition) {
        pageNum
                .posAnd(pageNumPosition(hwpPageNumberPosition.getHeader().getProperty().getNumberPosition()))
                .formatTypeAnd(ValueConvertor.toNumberType1(hwpPageNumberPosition.getHeader().getProperty().getNumberShape()))
                .sideChar(ValueConvertor.stringNullCheck(hwpPageNumberPosition.getHeader().getAfterDecorationLetter().toUTF16LEString()));
    }

    private static PageNumPosition pageNumPosition(NumberPosition hwpNumberPosition) {
        switch (hwpNumberPosition) {
            case None:
                return PageNumPosition.NONE;
            case LeftTop:
                return PageNumPosition.TOP_LEFT;
            case CenterTop:
                return PageNumPosition.TOP_CENTER;
            case RightTop:
                return PageNumPosition.TOP_RIGHT;
            case LeftBottom:
                return PageNumPosition.BOTTOM_LEFT;
            case CenterBottom:
                return PageNumPosition.BOTTOM_CENTER;
            case RightBottom:
                return PageNumPosition.BOTTOM_RIGHT;
            case OutsideTop:
                return PageNumPosition.OUTSIDE_TOP;
            case OutsideBottom:
                return PageNumPosition.OUTSIDE_BOTTOM;
            case InsideTop:
                return PageNumPosition.INSIDE_TOP;
            case InsideBottom:
                return PageNumPosition.INSIDE_BOTTOM;
        }
        return PageNumPosition.NONE;
    }
}
