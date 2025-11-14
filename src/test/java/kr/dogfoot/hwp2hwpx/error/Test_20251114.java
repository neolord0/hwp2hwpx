package kr.dogfoot.hwp2hwpx.error;

import kr.dogfoot.hwp2hwpx.Hwp2Hwpx;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.writer.HWPXWriter;
import org.junit.Test;

public class Test_20251114 {
    @Test
    public void test() throws Exception {
        HWPFile fromFile = HWPReader.fromFile("test/오류/20251114/별첨_1_2025년도_초기창업패키지(딥테크_분야)_사업계획서_양식.hwp");

        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, "test/오류/20251114/별첨_1_2025년도_초기창업패키지(딥테크_분야)_사업계획서_양식.hwpx");
    }
}
