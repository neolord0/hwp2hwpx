package kr.dogfoot.hwp2hwpx;

import kr.dogfoot.hwp2hwpx.util.Util;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.writer.HWPXWriter;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class TabInPara {
    @Test
    public void test() throws Exception {
        String testPath = "test/tab_in_para";

        HWPFile fromFile = HWPReader.fromFile(testPath + FileName.InputFile);
        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, testPath + FileName.OutputFile);

        {
            String resultXML = Util.loadXMLString(testPath + "/result/section0.xml", StandardCharsets.UTF_8);
            String toXML = Util.zipFileString(testPath + FileName.OutputFile, "Contents/section0.xml", StandardCharsets.UTF_8);
            Assert.assertEquals(resultXML, toXML);
        }
    }
}
