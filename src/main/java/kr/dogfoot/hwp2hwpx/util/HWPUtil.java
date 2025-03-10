package kr.dogfoot.hwp2hwpx.util;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bindata.EmbeddedBinaryData;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.control.Control;
import kr.dogfoot.hwplib.object.bodytext.control.ControlSectionDefine;
import kr.dogfoot.hwplib.object.bodytext.control.ControlType;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.docinfo.BinData;
import kr.dogfoot.hwplib.object.docinfo.borderfill.fillinfo.FillInfo;
import kr.dogfoot.hwplib.object.docinfo.borderfill.fillinfo.FillType;

public class HWPUtil {
    public static ControlSectionDefine sectionDefine(Section section) {
        Paragraph firstPara = section.getParagraph(0);
        for (Control control : firstPara.getControlList()) {
            if (control.getType() == ControlType.SectionDefine) {
                return (ControlSectionDefine)control;
            }
        }
        return null;
    }

    public static int sectionCount(HWPFile hwpFile) {
        return hwpFile.getBodyText().getSectionList().size();
    }
    public static boolean hasFillInfo(FillInfo hwpFillInfo) {
        FillType hwpFillType = hwpFillInfo.getType();
        if (hwpFillType.hasPatternFill()
                || hwpFillType.hasGradientFill()
                || hwpFillType.hasImageFill()) {
            return true;
        }
        return false;
    }

    public static String mediaTypeFromFilepath(String filepath) {
        return mediaType(extension(filepath));
    }

    private static String extension(String filepath) {
        int index = filepath.lastIndexOf('.');
        if (index >= 0) {
            return filepath.substring(index + 1);
        }
        return "";
    }

    public static String mediaType(String extension) {
        switch (extension) {
            case "emf":
                return "image/emf";
            case "gif":
                return "image/gif";
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "svg":
                return "image/svg+xml";
            case "tif":
                return "image/tiff";
            case "wmf":
                return "image/wmf";
        }
        return "image/unknown";
    }

    public static EmbeddedBinaryData embeddedBinaryData(BinData binData, Parameter.HWPInfo hwp) {
        for (EmbeddedBinaryData ebd : hwp.binData().getEmbeddedBinaryDataList()) {
            String[] name_ext = ebd.getName().split("\\.");
            if(Integer.parseInt(name_ext[0].substring(3), 16) == binData.getBinDataID()) {
                return ebd;
            }
        }
        return null;
    }
}
