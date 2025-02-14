package kr.dogfoot.hwp2hwpx.section;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.control.ControlField;
import kr.dogfoot.hwplib.object.bodytext.control.ControlType;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.FieldType;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.ctrl.FieldBegin;

public class ForFieldBegin extends Converter {
    public ForFieldBegin(Parameter parameter) {
        super(parameter);
    }


    public void convent(FieldBegin fieldBegin, ControlField hwpField) {
        fieldBegin
                .idAnd(String.valueOf(hwpField.getHeader().getInstanceId()))
                .typeAnd(fieldType(hwpField.getType()))
                .nameAnd(name(hwpField))
                .editableAnd(hwpField.getHeader().getProperty().canModifyInReadOnlyState())
                .dirtyAnd(hwpField.getHeader().getProperty().isModifiedContent())
                .zorderAnd(-1)
                .fieldid(String.valueOf(hwpField.getHeader().getCtrlId()));

        parameters(fieldBegin, hwpField);

        parameter.fieldBeginStack().push(fieldBegin);
    }

    private String name(ControlField hwpField) {
        if (hwpField.getHeader().getCommand() != null) return "";
        else if (hwpField.getName() != null) return hwpField.getName();

        return "";
    }

    private FieldType fieldType(ControlType type) {
        switch (type) {
            case FIELD_UNKNOWN:
                break;
            case FIELD_DATE:
                return FieldType.DATE;
            case FIELD_DOCDATE:
                return FieldType.DOC_DATE;
            case FIELD_PATH:
                return FieldType.PATH;
            case FIELD_BOOKMARK:
                return FieldType.BOOKMARK;
            case FIELD_MAILMERGE:
                return FieldType.MAILMERGE;
            case FIELD_CROSSREF:
                return FieldType.CROSSREF;
            case FIELD_FORMULA:
                return FieldType.FORMULA;
            case FIELD_CLICKHERE:
                return FieldType.CLICK_HERE;
            case FIELD_SUMMARY:
                return FieldType.SUMMARY;
            case FIELD_USERINFO:
                return FieldType.USER_INFO;
            case FIELD_HYPERLINK:
                return FieldType.HYPERLINK;
            case FIELD_REVISION_SIGN:
                break;
            case FIELD_REVISION_DELETE:
                break;
            case FIELD_REVISION_ATTACH:
                break;
            case FIELD_REVISION_CLIPPING:
                break;
            case FIELD_REVISION_THINKING:
                break;
            case FIELD_REVISION_PRAISE:
                break;
            case FIELD_REVISION_LINE:
                break;
            case FIELD_REVISION_SIMPLECHANGE:
                break;
            case FIELD_REVISION_HYPERLINK:
                break;
            case FIELD_REVISION_LINEATTACH:
                break;
            case FIELD_REVISION_LINELINK:
                break;
            case FIELD_REVISION_LINETRANSFER:
                break;
            case FIELD_REVISION_RIGHTMOVE:
                break;
            case FIELD_REVISION_LEFTMOVE:
                break;
            case FIELD_REVISION_TRANSFER:
                break;
            case FIELD_REVISION_SIMPLEINSERT:
                break;
            case FIELD_REVISION_SPLIT:
                break;
            case FIELD_REVISION_CHANGE:
                break;
            case FIELD_MEMO:
                return FieldType.MEMO;
            case FIELD_PRIVATE_INFO_SECURITY:
                return FieldType.PRIVATE_INFO;
            case FIELD_TABLEOFCONTENTS:
                break;
        }
        return null;
    }

    private void parameters(FieldBegin fieldBegin, ControlField hwpField) {
        fieldBegin.createParameters();
        fieldBegin.parameters().name("");
        fieldBegin.parameters().addNewIntegerParam()
                .nameAnd("Prop")
                .value(9);

        fieldBegin.parameters().addNewStringParam()
                .nameAnd("Command")
                .xml_spaceAnd("preserve")
                .value(hwpField.getHeader().getCommand().toUTF16LEString());

    }
}
