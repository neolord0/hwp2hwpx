package kr.dogfoot.hwp2hwpx.error;

import kr.dogfoot.hwp2hwpx.Hwp2Hwpx;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.docinfo.FaceName;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.writer.HWPXWriter;
import org.junit.Test;

import java.util.ArrayList;

public class Test_20250418 {
    @Test
    public void test() throws Exception {
        HWPFile fromFile = HWPReader.fromFile("test/오류/20250418/R25BD00004367-000_제안요청서.hwp");
        checkFontName(fromFile.getDocInfo().getHangulFaceNameList());
        checkFontName(fromFile.getDocInfo().getEnglishFaceNameList());
        checkFontName(fromFile.getDocInfo().getHanjaFaceNameList());
        checkFontName(fromFile.getDocInfo().getHangulFaceNameList());
        checkFontName(fromFile.getDocInfo().getJapaneseFaceNameList());
        checkFontName(fromFile.getDocInfo().getEtcFaceNameList());
        checkFontName(fromFile.getDocInfo().getSymbolFaceNameList());
        checkFontName(fromFile.getDocInfo().getUserFaceNameList());

        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, "test/오류/20250418/R25BD00004367-000_제안요청서.hwpx");
    }

    private void checkFontName(ArrayList<FaceName> hangulFaceNameList) {
        for (FaceName faceName : hangulFaceNameList) {
            String name = faceName.getName();
            if (name.charAt(0) == 0x17) {
                faceName.setName(name.substring(1));
                System.out.println("Font name changed: " + name + " -> " + faceName.getName());
            }
        }
    }
}
