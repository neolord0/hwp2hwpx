package kr.dogfoot.hwp2hwpx;

import kr.dogfoot.hwp2hwpx.util.Util;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.writer.HWPXWriter;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class 여러섹션 {
    @Test
    public void test() throws Exception {
        String testPath = "test/여러섹션";

        HWPFile fromFile = HWPReader.fromFile(testPath + "/from.hwp");
        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, testPath + "/to.zip");

        {
            String resultXML = Util.loadXMLString(testPath + "/result/content.hpf", StandardCharsets.UTF_8);
            String toXML = Util.zipFileString(testPath + "/to.zip", "Contents/content.hpf", StandardCharsets.UTF_8);
            Assert.assertEquals(resultXML, toXML);
        }
    }
}
