package kr.dogfoot.hwp2hwpx;

import kr.dogfoot.hwp2hwpx.util.HWPUtil;
import kr.dogfoot.hwplib.object.bindata.EmbeddedBinaryData;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.control.Control;
import kr.dogfoot.hwplib.object.bodytext.control.ControlSectionDefine;
import kr.dogfoot.hwplib.object.bodytext.control.ControlType;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.BatangPageInfo;
import kr.dogfoot.hwplib.object.docinfo.BinData;
import kr.dogfoot.hwplib.object.docinfo.bindata.BinDataType;
import kr.dogfoot.hwplib.org.apache.poi.hpsf.SummaryInformation;
import kr.dogfoot.hwpxlib.object.content.context_hpf.ContentHPFFile;
import kr.dogfoot.hwpxlib.object.content.context_hpf.ManifestItem;
import kr.dogfoot.hwpxlib.object.content.context_hpf.MetaData;
import kr.dogfoot.hwpxlib.object.content.masterpage_xml.MasterPageXMLFile;
import kr.dogfoot.hwpxlib.object.content.masterpage_xml.enumtype.MasterPageType;

import java.util.ArrayList;
import java.util.Map;

public class ForContentHPFFile extends Converter {
    private ContentHPFFile contentHPFFile;
    private ArrayList<Section> hwpSectionList;

    public ForContentHPFFile(Parameter parameter) {
        super(parameter);
    }

    public void convert() {
        contentHPFFile = parameter.hwpx().contentHPFFile()
                .idAnd("")
                .versionAnd("")
                .uniqueIdentifierAnd("");
        hwpSectionList = parameter.hwp().bodyText().getSectionList();

        metadata();
        manifest();
        spine();
    }


    private void metadata() {
        SummaryInformation summaryInformation = parameter.hwp().summaryInformation();
        if (summaryInformation == null) return;

        contentHPFFile.createMetaData();

        MetaData metaData  = contentHPFFile.metaData();
        metaData.createTitle();
        metaData.title().addText(summaryInformation.getTitle());

        metaData.createLanguage();
        metaData.language().addText("ko");

        addTextMeta("creator", summaryInformation.getAuthor());
        addTextMeta("subject", summaryInformation.getSubject());
        addTextMeta("description", summaryInformation.getComments());
        addTextMeta("lastsaveby", summaryInformation.getLastAuthor());
        addTextMeta("CreatedDate", summaryInformation.getCreateDateTime().toString());
        addTextMeta("ModifiedDate", summaryInformation.getLastSaveDateTime().toString());
        addTextMeta("date", summaryInformation.getCreateDateTime().toString());
        addTextMeta("keyword", summaryInformation.getKeywords());
    }

    private void addTextMeta(String name, String value) {
        contentHPFFile.metaData().addNewMeta()
                .nameAnd(name)
                .contentAnd("text")
                .text(value);
    }

    private void manifest() {
        contentHPFFile.createManifest();

        addNewManifestItem("header",
                "Contents/header.xml",
                "application/xml");


        // todo: image, chart 등에 bindata 처리

        int masterPageIndex = 0;
        int sectionIndex = 0;

        for (Section hwpSection : hwpSectionList) {
            ArrayList<MasterPageType> appliedMasterPageTypes = appliedMasterPageTypes(hwpSection);
            ControlSectionDefine hwpSectionDefine = HWPUtil.sectionDefine(hwpSection);
            int count = hwpSectionDefine.getBatangPageInfoList().size();
            for (int index = 0; index < count; index++) {
                BatangPageInfo hwpBatangPageInfo = hwpSectionDefine.getBatangPageInfoList().get(index);

                String masterPageId = "masterpage" + masterPageIndex;

                addNewManifestItem(masterPageId,
                        "Contents/masterpage" + masterPageIndex + ".xml",
                        "application/xml");

                parameter.masterPageIdMap().put(hwpBatangPageInfo,
                        new Parameter.MasterPageInfo(masterPageId, appliedMasterPageTypes.get(index)));

                masterPageIndex++;
            }

            addNewManifestItem("section" + sectionIndex,
                    "Contents/section" + sectionIndex + ".xml",
                    "application/xml");

            sectionIndex++;
        }

        int binDataId = 1;
        for (BinData binData : parameter.hwp().docInfo().getBinDataList()) {
            binData(binDataId, binData);
            binDataId++;
        }

        addNewManifestItem("settings",
                "settings.xml",
                "application/xml");
    }

    private void addNewManifestItem(String id, String href, String mediaType) {
        contentHPFFile.manifest().addNew()
                .idAnd(id)
                .hrefAnd(href)
                .mediaType(mediaType);
    }


    private ArrayList<MasterPageType> appliedMasterPageTypes(Section hwpSection) {
        ArrayList<MasterPageType> masterPageTypes = new ArrayList<MasterPageType>();
        ControlSectionDefine sectionDefine = sectionDefine(hwpSection);
        if (sectionDefine != null) {
            if (sectionDefine.getHeader().getProperty().isApplyBothBatangPage()) {
                masterPageTypes.add(MasterPageType.BOTH);
            }
            if (sectionDefine.getHeader().getProperty().isApplyEvenBatangPage()) {
                masterPageTypes.add(MasterPageType.EVEN);
            }
            if (sectionDefine.getHeader().getProperty().isApplyOddBatangPage()) {
                masterPageTypes.add(MasterPageType.ODD);
            }
        }
        return masterPageTypes;
    }

    private ControlSectionDefine sectionDefine(Section hwpSection) {
        for (Control hwpControl : hwpSection.getParagraph(0).getControlList()) {
            if (hwpControl.getType() == ControlType.SectionDefine) {
                return (ControlSectionDefine) hwpControl;
            }
        }
        return null;
    }

    private void binData(int binDataId, BinData binData) {
        String id = makeBinDataID(binData);
        if (binData.getProperty().getType() == BinDataType.Link) {
            addNewManifestItemForLink(id,
                    binData.getAbsolutePathForLink(),
                    HWPUtil.mediaTypeFromFilepath(binData.getAbsolutePathForLink()));
        } else {
            if (hasEmbeddedBinaryData(binData)) {
                addNewManifestItemForEmbedding(id,
                        hrefForEmbedding(id, binData.getExtensionForEmbedding()),
                        HWPUtil.mediaType(binData.getExtensionForEmbedding()),
                        HWPUtil.embeddedBinaryData(binData, parameter.hwp()));
            }
         }

        parameter.binDataIdMap().put(binDataId, id);
    }

    private String makeBinDataID(BinData binData) {
        if (binData.getExtensionForEmbedding().equalsIgnoreCase("OLE")) {
            return "ole" + binData.getBinDataID();
        } else {
            return "image" + binData.getBinDataID();
        }
    }

    private boolean hasEmbeddedBinaryData(BinData binData) {
        for(EmbeddedBinaryData data : parameter.hwp().binData().getEmbeddedBinaryDataList()) {
            String[] name_ext = data.getName().split("\\.");
            if(name_ext[0].endsWith(String.format("%03X", binData.getBinDataID()))) {
                return true;
            }
        }
        return false;
    }

    private int nameToID(String name) {
        String id = name.substring(3, 7);
        return Integer.parseInt(id, 16);
    }

    private void addNewManifestItemForLink(String id, String href, String mediaType) {
        contentHPFFile.manifest().addNew()
                .idAnd(id)
                .hrefAnd(href)
                .mediaTypeAnd(mediaType)
                .embedded(false);
    }

    private static String hrefForEmbedding(String filenameWithExt, String extension) {
        return new StringBuffer()
                .append("BinData/")
                .append(filenameWithExt)
                .append(".")
                .append(extension)
                .toString();
    }

    private void addNewManifestItemForEmbedding(String id, String href, String mediaType, EmbeddedBinaryData embeddedBinaryData) {
        ManifestItem manifestItem = contentHPFFile.manifest().addNew()
                .idAnd(id)
                .hrefAnd(href)
                .mediaTypeAnd(mediaType)
                .embeddedAnd(true);

        manifestItem.createAttachedFile();
        manifestItem.attachedFile().data(embeddedBinaryData.getData());
    }

    private void spine() {
        contentHPFFile.createSpine();

        contentHPFFile.spine().addNew().idref("header");

        int count = parameter.hwp().sectionCount();
        for (int index = 0; index < count; index++) {
            contentHPFFile.spine().addNew().idref("section" + index);
        }
    }
}
