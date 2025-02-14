package kr.dogfoot.hwp2hwpx.section.object;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeObject;
import kr.dogfoot.hwp2hwpx.util.ValueConvertor;
import kr.dogfoot.hwplib.object.bodytext.control.ControlEquation;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.EquationLineMode;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Equation;

public class ForEquation extends ForShapeObject {
    public ForEquation(Parameter parameter) {
        super(parameter);
    }

    public void convert(Equation equation, ControlEquation hwpEquation) {
        shapeObject(equation, hwpEquation.getHeader(), hwpEquation.getCaption());

        equation
                .versionAnd(hwpEquation.getEQEdit().getVersionInfo().toUTF16LEString())
                .baseLineAnd((int) hwpEquation.getEQEdit().getBaseLine())
                .textColorAnd(ValueConvertor.color(hwpEquation.getEQEdit().getLetterColor()))
                .baseUnitAnd((int) hwpEquation.getEQEdit().getLetterSize())
                .lineModeAnd(equationLineMode(hwpEquation.getEQEdit().getProperty()))
                .font(hwpEquation.getEQEdit().getFontName().toUTF16LEString());

        equation.createScript();
        equation.script()
                .addText(hwpEquation.getEQEdit().getScript().toUTF16LEString());
    }

    private EquationLineMode equationLineMode(long property) {
        if ((property & 0x1) == 0x1) return EquationLineMode.LINE;

        return EquationLineMode.CHAR;
    }
 }
