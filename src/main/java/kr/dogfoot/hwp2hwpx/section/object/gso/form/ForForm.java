package kr.dogfoot.hwp2hwpx.section.object.gso.form;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.bodytext.control.ControlForm;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.Run;

public class ForForm extends Converter {
    private ForButton buttonConverter;
    private ForComboBox comboBoxConverter;
    private ForEdit editConverter;

    public ForForm(Parameter parameter) {
        super(parameter);

        buttonConverter = new ForButton(parameter);
        comboBoxConverter = new ForComboBox(parameter);
        editConverter = new ForEdit(parameter);
    }

    public void convert(Run currentRun, ControlForm hwpForm) {
        switch (hwpForm.getFormObject().getType()) {
            case PushButton:
                buttonConverter.convertForBtn(currentRun.addNewButton(), hwpForm);
                break;
            case CheckBox:
                buttonConverter.convertForCheckBtn(currentRun.addNewCheckButton(), hwpForm);
                break;
            case ComboBox:
                comboBoxConverter.convert(currentRun.addNewComboBox(), hwpForm);
                break;
            case RadioButton:
                buttonConverter.covertForRadioBtn(currentRun.addNewRadioButton(), hwpForm);
                break;
            case EditorBox:
                editConverter.convert(currentRun.addNewEdit(), hwpForm);
                break;
        }
    }
}
