package kr.dogfoot.hwp2hwpx;

import kr.dogfoot.hwp2hwpx.header.ForHeaderXMLFile;
import kr.dogfoot.hwp2hwpx.section.ForMasterPageXMLFileList;
import kr.dogfoot.hwp2hwpx.section.ForSectionXMLFileList;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.docinfo.documentproperties.CaretPosition;
import kr.dogfoot.hwplib.object.fileheader.FileVersion;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.TargetApplicationSort;
import kr.dogfoot.hwpxlib.object.metainf.ContainerXMLFile;
import kr.dogfoot.hwpxlib.object.metainf.ManifestXMLFile;
import kr.dogfoot.hwpxlib.object.root.SettingsXMLFile;
import kr.dogfoot.hwpxlib.object.root.VersionXMLFile;

public class Hwp2Hwpx extends Converter {
    public static HWPXFile toHWPX(HWPFile hwpFile) {
        Parameter parameter = new Parameter(hwpFile, new HWPXFile());
        new Hwp2Hwpx(parameter).convert();
        return parameter.hwpx().file();
    }

    protected Hwp2Hwpx(Parameter parameter) {
        super(parameter);
    }

    public void convert() {
        version_xml();

        manifest_xml();
        container_xml();

        new ForContentHPFFile(parameter).convert();
        new ForHeaderXMLFile(parameter).convert();
        new ForMasterPageXMLFileList(parameter).convert();
        new ForSectionXMLFileList(parameter).convert();

        setting_xml();
    }


    private void version_xml() {
        VersionXMLFile versionXMLFile = parameter.hwpx().versionXMLFile();
        versionXMLFile
                .targetApplicationAnd(TargetApplicationSort.WordProcessor)
                .osAnd("1")
                .applicationAnd("Hancom Office Hangul")
                .appVersion("9, 1, 1, 5656 WIN32LEWindows_Unknown_Version");

        FileVersion fileVersion = parameter.hwp().fileHeader().getVersion();
        versionXMLFile.version()
                .majorAnd((int) fileVersion.getMM())
                .minorAnd((int) fileVersion.getNN())
                .microAnd((int) fileVersion.getPP())
                .buildNumber((int) fileVersion.getRR());
    }

    private void manifest_xml() {
        ManifestXMLFile manifestXMLFile = parameter.hwpx().manifestXMLFile();
    }

    private void setting_xml() {
        SettingsXMLFile settingsXMLFile = parameter.hwpx().settingsXMLFile();

        CaretPosition caretPosition = parameter.hwp().docInfo()
                .getDocumentProperties().getCaretPosition();

        settingsXMLFile.createCaretPosition();

        settingsXMLFile.caretPosition()
                .listIDRefAnd((int) caretPosition.getListID())
                .paraIDRefAnd((int) caretPosition.getParagraphID())
                .pos((int) caretPosition.getPositionInParagraph());
    }

    private void container_xml() {
        ContainerXMLFile containerXMLFile = parameter.hwpx().containerXMLFile();

        containerXMLFile.createRootFiles();

        containerXMLFile.rootFiles().addNew()
                    .fullPathAnd("Contents/content.hpf")
                    .mediaType("application/hwpml-package+xml");

        // test
        containerXMLFile.rootFiles().addNew()
                .fullPathAnd("Preview/PrvText.txt")
                .mediaType("text/plain");
    }
}
