package kr.dogfoot.hwp2hwpx.error;

import kr.dogfoot.hwp2hwpx.Hwp2Hwpx;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.writer.HWPXWriter;
import org.junit.Test;

public class Test_20250307 {
    @Test
    public void test() throws Exception {
        HWPFile fromFile = HWPReader.fromFile("test/오류/20250307/20240317833-00-1_붙임2_제안요청서_2024년 전통문화 분야 메타버스 콘텐츠 구축_조달의견수렴_0313.hwp");
        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, "test/오류/20250307/20240317833-00-1_붙임2_제안요청서_2024년 전통문화 분야 메타버스 콘텐츠 구축_조달의견수렴_0313.hwpx");
    }

}
