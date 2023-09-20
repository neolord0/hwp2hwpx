package kr.dogfoot.hwp2hwpx;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.writer.HWPXWriter;
import org.junit.Test;

public class Multi_Run {
    @Test
    public void test() throws Exception {
        HWPFile fromFile = HWPReader.fromFile("test/Multi_Run/from.hwp");
        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, "test/Multi_Run/to.zip");
    }
}
