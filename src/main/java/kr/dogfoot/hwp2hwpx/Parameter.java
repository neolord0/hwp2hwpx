package kr.dogfoot.hwp2hwpx;

import kr.dogfoot.hwp2hwpx.section.ForSubList;
import kr.dogfoot.hwp2hwpx.util.HWPUtil;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bindata.BinData;
import kr.dogfoot.hwplib.object.bodytext.BodyText;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.BatangPageInfo;
import kr.dogfoot.hwplib.object.docinfo.DocInfo;
import kr.dogfoot.hwplib.object.fileheader.FileHeader;
import kr.dogfoot.hwplib.org.apache.poi.hpsf.SummaryInformation;
import kr.dogfoot.hwpxlib.object.HWPXFile;
import kr.dogfoot.hwpxlib.object.common.ObjectList;
import kr.dogfoot.hwpxlib.object.content.context_hpf.ContentHPFFile;
import kr.dogfoot.hwpxlib.object.content.header_xml.HeaderXMLFile;
import kr.dogfoot.hwpxlib.object.content.masterpage_xml.MasterPageXMLFile;
import kr.dogfoot.hwpxlib.object.content.masterpage_xml.enumtype.MasterPageType;
import kr.dogfoot.hwpxlib.object.content.section_xml.SectionXMLFile;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.FieldBegin;
import kr.dogfoot.hwpxlib.object.metainf.ContainerXMLFile;
import kr.dogfoot.hwpxlib.object.metainf.ManifestXMLFile;
import kr.dogfoot.hwpxlib.object.root.SettingsXMLFile;
import kr.dogfoot.hwpxlib.object.root.VersionXMLFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Parameter {
    private HWPInfo hwpInfo;
    private HWPXInfo hwpxInfo;
    private Map<BatangPageInfo, MasterPageInfo> masterPageIdMap;
    private Map<Integer, String> binDataIdMap;
    private Stack<FieldBegin> fieldBeginStack;

    private ForSubList subListConverter;

    public Parameter(HWPFile hwpFile, HWPXFile hwpxFile) {
        hwpInfo = new HWPInfo(hwpFile);
        hwpxInfo = new HWPXInfo(hwpxFile);

        masterPageIdMap = new HashMap<BatangPageInfo, MasterPageInfo>();
        binDataIdMap = new HashMap<Integer, String>();
        fieldBeginStack = new Stack<FieldBegin>();

        subListConverter = new ForSubList(this);
    }

    public HWPInfo hwp() {
        return hwpInfo;
    }

    public HWPXInfo hwpx() {
        return hwpxInfo;
    }

    public Map<BatangPageInfo, MasterPageInfo> masterPageIdMap() {
        return masterPageIdMap;
    }

    public Map<Integer, String> binDataIdMap() {
        return binDataIdMap;
    }

    public Stack<FieldBegin> fieldBeginStack() {
        return fieldBeginStack;
    }

    public ForSubList subListConverter() {
        return subListConverter;
    }

    public static class HWPInfo {
        private HWPFile file;
        private int sectionCount = 0;
        private DocInfo docInfo = null;

        private HWPInfo(HWPFile file) {
            this.file = file;
            sectionCount = HWPUtil.sectionCount(file);
        }

        public FileHeader fileHeader() {
            return file.getFileHeader();
        }

        public DocInfo docInfo() {
            if (docInfo == null) {
                docInfo = file.getDocInfo();
            }
            return docInfo;
        }

        public BodyText bodyText() {
            return file.getBodyText();
        }

        public BinData binData() {
            return file.getBinData();
        }

        public SummaryInformation summaryInformation() {
            return file.getSummaryInformation();
        }

        public int sectionCount() {
            return sectionCount;
        }
    }

    public static class HWPXInfo {
        private HWPXFile file;

        private HWPXInfo(HWPXFile file) {
            this.file = file;
        }

        public HWPXFile file() {
            return file;
        }

        public VersionXMLFile versionXMLFile() {
            return file.versionXMLFile();
        }

        public ManifestXMLFile manifestXMLFile() {
            return file.manifestXMLFile();
        }

        public ContainerXMLFile containerXMLFile() {
            return file.containerXMLFile();
        }

        public ContentHPFFile contentHPFFile() {
            return file.contentHPFFile();
        }

        public HeaderXMLFile headerXMLFile() {
            return file.headerXMLFile();
        }

        public ObjectList<MasterPageXMLFile> masterPageXMLFileList() {
            return file.masterPageXMLFileList();
        }

        public ObjectList<SectionXMLFile> sectionXMLFileList() {
            return file.sectionXMLFileList();
        }

        public SettingsXMLFile settingsXMLFile() {
            return file.settingsXMLFile();
        }
    }

    public static class MasterPageInfo {
        private String id;
        private MasterPageType type;

        public MasterPageInfo(String id, MasterPageType type) {
            this.id = id;
            this.type = type;
        }

        public String id() {
            return id;
        }

        public MasterPageType type() {
            return type;
        }
    }
}

