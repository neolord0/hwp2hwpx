package kr.dogfoot.hwp2hwpx;

import kr.dogfoot.hwp2hwpx.util.HWPUtil;
import kr.dogfoot.hwplib.object.bindata.EmbeddedBinaryData;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.control.ControlSectionDefine;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.BatangPageInfo;
import kr.dogfoot.hwplib.object.docinfo.BinData;
import kr.dogfoot.hwplib.object.docinfo.bindata.BinDataType;
import kr.dogfoot.hwplib.org.apache.poi.hpsf.SummaryInformation;
import kr.dogfoot.hwpxlib.object.content.context_hpf.ContentHPFFile;
import kr.dogfoot.hwpxlib.object.content.context_hpf.ManifestItem;
import kr.dogfoot.hwpxlib.object.content.context_hpf.MetaData;

import java.util.ArrayList;

public class ForContentHPFFile extends Converter {
    private ContentHPFFile contentHPFFile;
    private ArrayList<Section> hwpSectionList;

    public ForContentHPFFile(Parameter parameter) {
        super(parameter);
    }

    public void convert() {
        contentHPFFile = parameter.hwpx().contentHPFFile();
        hwpSectionList = parameter.hwp().bodyText().getSectionList();

        metadata();
        manifest();
        spine();
    }


    private void metadata() {
        SummaryInformation summaryInformation = parameter.hwp().summaryInformation();

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
            ControlSectionDefine hwpSectionDefine = HWPUtil.sectionDefine(hwpSection);
            for (BatangPageInfo hwpBatangPageInfo : hwpSectionDefine.getBatangPageInfoList()) {
                String masterPageId = "masterpage" + masterPageIndex;
                addNewManifestItem(masterPageId,
                        "Contents/masterpage" + masterPageIndex + ".xml",
                        "application/xml");
                parameter.masterPageIdMap().put(hwpBatangPageInfo, masterPageId);

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

    private void binData(int binDataId, BinData binData) {
        String id = "image" + binDataId;
        if (binData.getProperty().getType() == BinDataType.Link) {
            addNewManifestItemForLink(id,
                    binData.getAbsolutePathForLink(),
                    HWPUtil.mediaTypeFromFilepath(binData.getAbsolutePathForLink()));
        } else {
            addNewManifestItemForLinkForEmbedding(id,
                    hrefForEmbedding(id, binData.getExtensionForEmbedding()),
                    HWPUtil.mediaType(binData.getExtensionForEmbedding()),
                    parameter.hwp().binData().getEmbeddedBinaryDataList().get(binDataId - 1));
         }

        parameter.binDataIdMap().put(binDataId, id);
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

    private void addNewManifestItemForLinkForEmbedding(String id, String href, String mediaType, EmbeddedBinaryData embeddedBinaryData) {
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
