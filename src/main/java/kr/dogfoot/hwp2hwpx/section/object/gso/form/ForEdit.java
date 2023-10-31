package kr.dogfoot.hwp2hwpx.section.object.gso.form;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeObject;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlForm;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.HorizontalAlign1;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.DisplayScrollBar;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.TabKeyBehavior;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Edit;

import java.util.Map;

public class ForEdit extends ForShapeObject {
    private Edit edit;
    private ControlForm hwpForm;
    private Map<String, String> nameValues;

    public ForEdit(Parameter parameter) {
        super(parameter);
    }

    public void convert(Edit edit, ControlForm hwpForm) {
        this.edit = edit;
        this.hwpForm = hwpForm;

        nameValues = NameValuesGetter.get(hwpForm.getFormObject().getProperties());

        edit
                .multiLineAnd(ValueConvertor.bool(nameValues.get(PropertyName.MultiLine)))
                .passwordCharAnd(nameValues.get(PropertyName.PasswordChar))
                .maxLengthAnd(Integer.parseInt(nameValues.get(PropertyName.MaxLength)))
                .scrollBarsAnd(scrollBars(nameValues.get(PropertyName.ScrollBars)))
                .tabKeyBehaviorAnd(tabKeyBehavior(nameValues.get(PropertyName.TabKeyBehavior)))
                .numOnlyAnd(ValueConvertor.bool(nameValues.get(PropertyName.Number)))
                .readOnlyAnd(ValueConvertor.bool(nameValues.get(PropertyName.ReadOnly)))
                .alignTextAnd(alignText(nameValues.get(PropertyName.AlignText)))
                .nameAnd(nameValues.get(PropertyName.Name))
                .foreColorAnd(ValueConvertor.color(nameValues.get(PropertyName.ForeColor)))
                .backColorAnd(ValueConvertor.color(nameValues.get(PropertyName.BackColor)))
                .groupNameAnd(nameValues.get(PropertyName.GroupName))
                .tabStopAnd(ValueConvertor.bool(nameValues.get(PropertyName.TabStop)))
                .editableAnd(ValueConvertor.bool(nameValues.get(PropertyName.Editable)))
                .tabOrderAnd(Integer.parseInt(nameValues.get(PropertyName.TabOrder)))
                .enabledAnd(ValueConvertor.bool(nameValues.get(PropertyName.Enabled)))
                .borderTypeIDRefAnd(nameValues.get(PropertyName.BorderType))
                .drawFrameAnd(ValueConvertor.bool(nameValues.get(PropertyName.DrawFrame)))
                .printableAnd(ValueConvertor.bool(nameValues.get(PropertyName.Printable)))
                .command(nameValues.get(PropertyName.Command));

        formCharPr();
        text();

        this.shapeObject = edit;
        this.hwpGSOHeader = hwpForm.getHeader();
        this.hwpCaption = null;

        sz();
        pos();
        outMargin();
    }

    private HorizontalAlign1 alignText(String hwpAlignText) {
        switch (hwpAlignText) {
            case "0":
                return HorizontalAlign1.LEFT;
            case "1":
                return HorizontalAlign1.CENTER;
            case "2":
                return HorizontalAlign1.RIGHT;
            default:
                return HorizontalAlign1.LEFT;
        }
    }

    private DisplayScrollBar scrollBars(String hwpScrollBars) {
        switch (hwpScrollBars) {
            case "0":
                return DisplayScrollBar.NONE;
            // todo
        }
        return null;
    }

    private TabKeyBehavior tabKeyBehavior(String hwpTabKeyBehavior) {
        switch (hwpTabKeyBehavior) {
            case "0":
                return TabKeyBehavior.NEXT_OBJECT;
            case "1":
                return TabKeyBehavior.INSERT_TAB;
            default:
                return TabKeyBehavior.NEXT_OBJECT;
        }
    }

    private void formCharPr() {
        edit.createFormCharPr();
        edit.formCharPr()
                .charPrIDRefAnd(nameValues.get(PropertyName.CharShapeID))
                .followContextAnd(ValueConvertor.bool(nameValues.get(PropertyName.FollowContext)))
                .autoSzAnd(ValueConvertor.bool(nameValues.get(PropertyName.AutoSize)))
                .wordWrap(ValueConvertor.bool(nameValues.get(PropertyName.WordWrap)));
    }

    private void text() {
        edit.createText();
        edit.text().addText(nameValues.get(PropertyName.Text));
    }
}
