package kr.dogfoot.hwp2hwpx.section.object.gso.form;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeObject;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlForm;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.ComboBox;

import java.util.Map;

public class ForComboBox extends ForShapeObject {
    private ComboBox comboBox;
    private ControlForm hwpForm;
    private Map<String, String> nameValues;


    public ForComboBox(Parameter parameter) {
        super(parameter);
    }

    public void convert(ComboBox comboBox, ControlForm hwpForm) {
        this.comboBox = comboBox;
        this.hwpForm = hwpForm;

        nameValues = NameValuesGetter.get(hwpForm.getFormObject().getProperties());

        comboBox
                .listBoxRowsAnd(Integer.parseInt(nameValues.get(PropertyName.ListBoxRows)))
                .listBoxWidthAnd(Integer.parseInt(nameValues.get(PropertyName.ListBoxWidth)))
                .editEnableAnd(ValueConvertor.bool(nameValues.get(PropertyName.EditEnable)))
                .selectedValueAnd("")
                .nameAnd(nameValues.get(PropertyName.Name))
                .foreColorAnd(ValueConvertor.color(nameValues.get(PropertyName.ForeColor)))
                .backColorAnd(ValueConvertor.color(nameValues.get(PropertyName.BackColor)))
                .groupNameAnd(nameValues.get(PropertyName.GroupName))
                .tabStopAnd(ValueConvertor.bool(nameValues.get(PropertyName.TabStop)))
                .tabOrderAnd(Integer.parseInt(nameValues.get(PropertyName.TabOrder)))
                .editableAnd(ValueConvertor.bool(nameValues.get(PropertyName.Editable)))
                .enabledAnd(ValueConvertor.bool(nameValues.get(PropertyName.Enabled)))
                .borderTypeIDRefAnd(nameValues.get(PropertyName.BorderType))
                .drawFrameAnd(ValueConvertor.bool(nameValues.get(PropertyName.DrawFrame)))
                .printableAnd(ValueConvertor.bool(nameValues.get(PropertyName.Printable)))
                .command(nameValues.get(PropertyName.Command));
        // todo : selectedValue ??

        formCharPr();
        listItems();

        this.shapeObject = comboBox;
        this.hwpGSOHeader = hwpForm.getHeader();
        this.hwpCaption = null;

        sz();
        pos();
        outMargin();
    }


    private void formCharPr() {
        comboBox.createFormCharPr();
        comboBox.formCharPr()
                .charPrIDRefAnd(nameValues.get(PropertyName.CharShapeID))
                .followContextAnd(ValueConvertor.bool(nameValues.get(PropertyName.FollowContext)))
                .autoSzAnd(ValueConvertor.bool(nameValues.get(PropertyName.AutoSize)))
                .wordWrap(ValueConvertor.bool(nameValues.get(PropertyName.WordWrap)));
    }

    private void listItems() {
        comboBox.addNewListItem()
                .displayTextAnd("")
                .value(nameValues.get(PropertyName.Text));
        // todo : displayText ?
    }
}
