package kr.dogfoot.hwp2hwpx;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.writer.HWPXWriter;
import org.junit.Test;

public class 빈파일 {
    @Test
    public void test() throws Exception {
        HWPFile fromFile = HWPReader.fromFile("test/빈파일/from.hwp");
        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, "test/빈파일/to.zip");
    }
}
