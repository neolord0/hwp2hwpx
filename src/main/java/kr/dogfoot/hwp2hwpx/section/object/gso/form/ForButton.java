package kr.dogfoot.hwp2hwpx.section.object.gso.form;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeObject;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlForm;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.BackStyle;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.ButtonCheckValue;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Button;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.CheckButton;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.RadioButton;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.formobject.ButtonCore;

import java.util.Map;

public class ForButton extends ForShapeObject {
    private ButtonCore buttonCore;
    private ControlForm hwpForm;
    private Map<String, String> nameValues;

    public ForButton(Parameter parameter) {
        super(parameter);
    }

    public void convertForBtn(Button button, ControlForm hwpForm) {
        nameValues = NameValuesGetter.get(hwpForm.getFormObject().getProperties());

        buttonCore(button, hwpForm);
    }

    private void buttonCore(ButtonCore buttonCore, ControlForm hwpForm) {
        this.buttonCore = buttonCore;
        this.hwpForm = hwpForm;

        buttonCore
                .captionTextAnd(nameValues.get(PropertyName.Caption))
                .radioGroupNameAnd("")
                .triStateAnd(false)
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

        this.shapeObject = buttonCore;
        this.hwpGSOHeader = hwpForm.getHeader();
        this.hwpCaption = null;

        sz();
        pos();
        outMargin();
    }

    private void formCharPr() {
        buttonCore.createFormCharPr();
        buttonCore.formCharPr()
                .charPrIDRefAnd(nameValues.get(PropertyName.CharShapeID))
                .followContextAnd(ValueConvertor.bool(nameValues.get(PropertyName.FollowContext)))
                .autoSzAnd(ValueConvertor.bool(nameValues.get(PropertyName.AutoSize)))
                .wordWrap(ValueConvertor.bool(nameValues.get(PropertyName.WordWrap)));
    }

    public void convertForCheckBtn(CheckButton checkBtn, ControlForm hwpForm) {
        nameValues = NameValuesGetter.get(hwpForm.getFormObject().getProperties());

        buttonCore(checkBtn, hwpForm);

        checkBtn
                .valueAnd(checkValue(nameValues.get(PropertyName.Value)))
                .triStateAnd(ValueConvertor.bool(nameValues.get(PropertyName.TriState)))
                .backStyle(backStyle(nameValues.get(PropertyName.BackStyle)));
    }

    private ButtonCheckValue checkValue(String hwpValue) {
        switch (hwpValue) {
            case "0":
                return ButtonCheckValue.UNCHECKED;
            case "1":
                return ButtonCheckValue.CHECKED;
            default:
                return ButtonCheckValue.INDETERMINATE;
        }
    }

    private BackStyle backStyle(String hwpBackStyle) {
        return hwpBackStyle.equals("0") ?  BackStyle.TRANSPARENT : BackStyle.OPAQUE;
    }

    public void covertForRadioBtn(RadioButton radioButton, ControlForm hwpForm) {
        nameValues = NameValuesGetter.get(hwpForm.getFormObject().getProperties());

        buttonCore(radioButton, hwpForm);

        radioButton
                .valueAnd(checkValue(nameValues.get(PropertyName.Value)))
                .triStateAnd(ValueConvertor.bool(nameValues.get(PropertyName.TriState)))
                .backStyle(backStyle(nameValues.get(PropertyName.BackStyle)));
    }
}
