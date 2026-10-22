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
        memo(fieldBegin, hwpField);

        parameter.fieldBeginStack().push(fieldBegin);
    }

    /**
     * 메모 필드.
     * HWP 는 메모를 「모르는 필드(%unk)」 + 명령 「MEMO/모양/번호/시각 하위/시각 상위/작성자/」 로 두고, 본문은 BodyText 의 메모 목록에 따로 둔다.
     * 한글은 이를 HWPX `fieldBegin type="MEMO"`(식별자 %%me) + 매개변수 7개 + 본문 subList 로 쓴다(한글 저장본 대조).
     * 이전에는 종류 없는 빈 필드만 남아 메모 글이 사라졌다.
     * 작성 시각: FILETIME(UTC)에 9시간을 더해 「Z」를 붙인다 — 한글 저장본이 그렇게 쓴다(한국 시각을 UTC 로 표기하는 한글 동작을 그대로 따름).
     */
    private void memo(FieldBegin fieldBegin, ControlField hwpField) {
        if (hwpField.getHeader().getCommand() == null) return;
        String cmd = hwpField.getHeader().getCommand().toUTF16LEString();
        if (cmd == null || !cmd.startsWith("MEMO/")) return;
        String[] part = cmd.split("/", -1);
        if (part.length < 6) return;
        int number;
        try {
            number = Integer.parseInt(part[2]);
        } catch (NumberFormatException e) {
            return;
        }

        fieldBegin.type(FieldType.MEMO);
        fieldBegin.fieldid(String.valueOf(ControlType.FIELD_MEMO.getCtrlId()));
        fieldBegin.zorder(number);

        fieldBegin.parameters().removeAllParams();
        fieldBegin.parameters().addNewIntegerParam().nameAnd("Prop").value(0);
        fieldBegin.parameters().addNewStringParam().nameAnd("Command").value(cmd);
        fieldBegin.parameters().addNewStringParam().nameAnd("ID").value("memo" + number);
        fieldBegin.parameters().addNewIntegerParam().nameAnd("Number").value(number);
        fieldBegin.parameters().addNewStringParam().nameAnd("Author").value(part[5]);
        fieldBegin.parameters().addNewStringParam().nameAnd("MemoShapeIDRef").value(part[1]);
        String created = createDateTime(part[3], part[4]);
        if (created != null) {
            fieldBegin.parameters().addNewStringParam().nameAnd("CreateDateTime").value(created);
        }

        kr.dogfoot.hwplib.object.bodytext.paragraph.memo.Memo body = findMemo(number);
        if (body != null) {
            parameter.subListConverter().convertForMemo(fieldBegin, body);
        }
    }

    private kr.dogfoot.hwplib.object.bodytext.paragraph.memo.Memo findMemo(int number) {
        if (parameter.hwp().bodyText() == null || parameter.hwp().bodyText().getMemoList() == null) return null;
        for (kr.dogfoot.hwplib.object.bodytext.paragraph.memo.Memo m : parameter.hwp().bodyText().getMemoList()) {
            if (m.getMemoList().getMemoIndex() == number) return m;
        }
        return null;
    }

    private static String createDateTime(String low, String high) {
        try {
            long ft = (Long.parseLong(high) << 32) + Long.parseLong(low);
            long epochMillis = ft / 10000L - 11644473600000L;
            java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            f.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            return f.format(new java.util.Date(epochMillis + 9L * 3600L * 1000L)) + "Z";
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * 책갈피 필드는 이름을 CtrlData(매개변수 묶음 539 · 항목 0x4000)에서 옮긴다.
     * 이전에는 「명령 객체가 있으면 이름 없음」이라, 명령 문자열이 비어 있는 책갈피의 이름이 모두 빠졌다(한글 저장본은 이름을 씀).
     * 다른 종류는 이전 규칙 그대로 — 누름틀은 CtrlData 에 이름이 있어도 한글이 빈 이름으로 쓴다(test/field 의 from.hwpx).
     */
    private String name(ControlField hwpField) {
        if (hwpField.getType() == ControlType.FIELD_BOOKMARK && hwpField.getCtrlData() != null && hwpField.getName() != null) {
            return hwpField.getName();
        }
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

    /**
     * 매개변수.
     * - Prop = 필드 머리의 「기타 속성」 값(이전에는 9 고정). 한글 저장본 대조: 누름틀 9 · 책갈피 2 · 하이퍼링크 0 · 메모 0
     * - Command 는 명령 문자열이 있을 때만 — 한글은 빈 명령(책갈피·하이퍼링크)에 Command 를 쓰지 않는다
     */
    private void parameters(FieldBegin fieldBegin, ControlField hwpField) {
        fieldBegin.createParameters();
        fieldBegin.parameters().name("");
        fieldBegin.parameters().addNewIntegerParam()
                .nameAnd("Prop")
                .value((int) hwpField.getHeader().getEtcProperty());

        String cmd = hwpField.getHeader().getCommand() == null ? null : hwpField.getHeader().getCommand().toUTF16LEString();
        if (cmd != null && cmd.length() > 0) {
            fieldBegin.parameters().addNewStringParam()
                    .nameAnd("Command")
                    .xml_spaceAnd("preserve")
                    .value(cmd);
        }
    }
}
