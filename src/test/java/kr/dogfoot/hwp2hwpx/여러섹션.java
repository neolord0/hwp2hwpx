package kr.dogfoot.hwp2hwpx;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.writer.HWPXWriter;
import org.junit.Test;

public class 여러섹션 {
    @Test
    public void test() throws Exception {
        HWPFile fromFile = HWPReader.fromFile("test/여러섹션/from.hwp");
        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, "test/여러섹션/to.zip");
    }
}
