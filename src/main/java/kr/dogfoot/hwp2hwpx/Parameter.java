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

import java.util.*;

public class Parameter {
    private final HWPInfo hwpInfo;
    private final HWPXInfo hwpxInfo;
    private final Map<BatangPageInfo, MasterPageInfo> masterPageIdMap;
    private final Map<Integer, String> binDataIdMap;
    private final Stack<FieldBegin> fieldBeginStack;
    private final Stack<ForSubList> subListConverterStack;

    public Parameter(HWPFile hwpFile, HWPXFile hwpxFile) {
        hwpInfo = new HWPInfo(hwpFile);
        hwpxInfo = new HWPXInfo(hwpxFile);

        masterPageIdMap = new HashMap<BatangPageInfo, MasterPageInfo>();
        binDataIdMap = new HashMap<Integer, String>();
        fieldBeginStack = new Stack<FieldBegin>();
        subListConverterStack = new Stack<ForSubList>();
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
        if (subListConverterStack.isEmpty()) {
            // 새로 만든 변환기는 쓰는 동안 풀에 넣지 않는다 — 반납(releaseSubListConverter) 때만 넣는다.
            // 여기서도 push 하면, 이 변환기가 일하는 중에 안쪽 목록(캡션·메모 등)을 변환할 때 같은 변환기를 다시 꺼내 쓰게 되어
            // 바깥 문단의 ForChars 상태가 덮인다 → 바깥 글이 안쪽 목록 마지막 문단으로 새어 들어가거나,
            // ForChars.extendControl 에서 IndexOutOfBoundsException 이 난다(이슈 #17 수정 뒤에도 남은 경우).
            return new ForSubList(this);
        }

        return subListConverterStack.pop();
    }

    public void releaseSubListConverter(ForSubList subListConverter) {
        subListConverterStack.push(subListConverter);
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
