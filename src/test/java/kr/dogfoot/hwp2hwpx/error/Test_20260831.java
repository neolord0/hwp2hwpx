package kr.dogfoot.hwp2hwpx.error;

import kr.dogfoot.hwp2hwpx.Hwp2Hwpx;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.writer.HWPXWriter;
import org.junit.Test;

public class Test_20260831 {
    @Test
    public void test() throws Exception {
        HWPFile fromFile = HWPReader.fromFile("test/오류/20260831/from.hwp");
        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, "test/오류/20260831/to.hwpx");
    }
}
