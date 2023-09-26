package kr.dogfoot.hwp2hwpx.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

public class Util {
    public static String loadXMLString(String filePath, Charset charset) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)), charset);
    }

    public static String zipFileString(String filePath, String zipEntryName, Charset charset) throws IOException {
        ZipFile zipFile = new ZipFile(filePath);

        InputStream is = zipFile.getInputStream(zipFile.getEntry(zipEntryName));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int length;
        byte[] buffer = new byte[2048];
        try {
            while ((length = is.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(bos.toByteArray(), charset);
    }

}
