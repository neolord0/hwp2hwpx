package kr.dogfoot.hwp2hwpx.util;

import kr.dogfoot.hwplib.object.bodytext.control.gso.textbox.TextVerticalAlignment;
import kr.dogfoot.hwplib.object.bodytext.control.sectiondefine.NumberShape;
import kr.dogfoot.hwplib.object.docinfo.borderfill.BorderThickness;
import kr.dogfoot.hwplib.object.docinfo.borderfill.BorderType;
import kr.dogfoot.hwplib.object.docinfo.charshape.BorderType2;
import kr.dogfoot.hwplib.object.docinfo.numbering.ParagraphNumberFormat;
import kr.dogfoot.hwplib.object.docinfo.numbering.ValueType;
import kr.dogfoot.hwplib.object.docinfo.parashape.Alignment;
import kr.dogfoot.hwplib.object.docinfo.parashape.VerticalAlignment;
import kr.dogfoot.hwplib.object.etc.Color4Byte;
import kr.dogfoot.hwpxlib.object.content.header_xml.enumtype.*;
import kr.dogfoot.hwpxlib.object.content.section_xml.enumtype.*;

public class ValueConvertor {
    public static Boolean toBoolean(short value) {
        return (value == 0) ? false : true;
    }

    public static LineType2 lineType2(BorderType hwpType) {
        switch (hwpType) {
            case None:
                return LineType2.NONE;
            case Solid:
                return LineType2.SOLID;
            case Dash:
                return LineType2.DASH;
            case Dot:
                return LineType2.DOT;
            case DashDot:
                return LineType2.DASH_DOT;
            case DashDotDot:
                return LineType2.DASH_DOT_DOT;
            case LongDash:
                return LineType2.LONG_DASH;
            case CircleDot:
                return LineType2.CIRCLE;
            case Double:
                return LineType2.DOUBLE_SLIM;
            case ThinThick:
                return LineType2.SLIM_THICK;
            case ThickThin:
                return LineType2.THICK_SLIM;
            case ThinThickThin:
                return LineType2.SLIM_THICK_SLIM;
        }
        return LineType2.NONE;
    }

    public static LineWidth lineWidth(BorderThickness hwpThickness) {
        switch (hwpThickness) {
            case MM0_1:
                return LineWidth.MM_0_1;
            case MM0_12:
                return LineWidth.MM_0_12;
            case MM0_15:
                return LineWidth.MM_0_15;
            case MM0_2:
                return LineWidth.MM_0_2;
            case MM0_25:
                return LineWidth.MM_0_25;
            case MM0_3:
                return LineWidth.MM_0_3;
            case MM0_4:
                return LineWidth.MM_0_4;
            case MM0_5:
                return LineWidth.MM_0_5;
            case MM0_6:
                return LineWidth.MM_0_6;
            case MM0_7:
                return LineWidth.MM_0_7;
            case MM1_0:
                return LineWidth.MM_1_0;
            case MM1_5:
                return LineWidth.MM_1_5;
            case MM2_0:
                return LineWidth.MM_2_0;
            case MM3_0:
                return LineWidth.MM_3_0;
            case MM4_0:
                return LineWidth.MM_4_0;
            case MM5_0:
                return LineWidth.MM_5_0;
        }
        return LineWidth.MM_0_1;
    }

    public static String color(Color4Byte hwpColor) {
        return String.format("#%02X%02X%02X",
                hwpColor.getR(),
                hwpColor.getG(),
                hwpColor.getB());
    }

    public static String colorWithNone(Color4Byte hwpColor, int noneValue) {
        if (noneValue == hwpColor.getValue()) {
            return "none";
        } else {
            return color(hwpColor);
        }
    }


    public static LineType3 lineType3(BorderType2 hwpType) {
        switch (hwpType) {
            case Solid:
                return LineType3.SOLID;
            case Dash:
                return LineType3.DASH;
            case Dot:
                return LineType3.DOT;
            case DashDot:
                return LineType3.DASH_DOT;
            case DashDotDot:
                return LineType3.DASH_DOT_DOT;
            case LongDash:
                return LineType3.LONG_DASH;
            case CircleDot:
                return LineType3.CIRCLE;
            case Double:
                return LineType3.DOUBLE_SLIM;
            case ThinThick:
                return LineType3.SLIM_THICK;
            case ThickThin:
                return LineType3.THICK_SLIM;
            case ThinThickThin:
                return LineType3.SLIM_THICK_SLIM;
            case Wave:
                return LineType3.WAVE;
            case DoubleWave:
                return LineType3.DOUBLEWAVE;
            case Thick3D:
            case Thick3DReverseLighting:
            case Solid3D:
            case Solid3DReverseLighting:
                break;
        }
        return LineType3.SOLID;
    }

    public static LineType2 lineType2(BorderType2 hwpType) {
        switch (hwpType) {
            case Solid:
                return LineType2.SOLID;
            case Dash:
                return LineType2.DASH;
            case Dot:
                return LineType2.DOT;
            case DashDot:
                return LineType2.DASH_DOT;
            case DashDotDot:
                return LineType2.DASH_DOT_DOT;
            case LongDash:
                return LineType2.LONG_DASH;
            case CircleDot:
                return LineType2.CIRCLE;
            case Double:
                return LineType2.DOUBLE_SLIM;
            case ThinThick:
                return LineType2.SLIM_THICK;
            case ThickThin:
                return LineType2.THICK_SLIM;
            case ThinThickThin:
                return LineType2.SLIM_THICK_SLIM;
            case Wave:
            case DoubleWave:
            case Thick3D:
            case Thick3DReverseLighting:
            case Solid3D:
            case Solid3DReverseLighting:
                break;
        }
        return LineType2.NONE;
    }

    public static ValueUnit1 valueUnit1(ValueType hwpType) {
        switch (hwpType) {
            case RatioForLetter:
                return ValueUnit1.PERCENT;
            case Value:
                return ValueUnit1.HWPUNIT;
        }
        return ValueUnit1.HWPUNIT;
    }

    public static HorizontalAlign2 horizontalAlign2(Alignment hwpAlign) {
        switch (hwpAlign) {
            case Justify:
                return HorizontalAlign2.JUSTIFY;
            case Left:
                return HorizontalAlign2.LEFT;
            case Right:
                return HorizontalAlign2.RIGHT;
            case Center:
                return HorizontalAlign2.CENTER;
            case Distribute:
                return HorizontalAlign2.DISTRIBUTE;
            case Divide:
                return HorizontalAlign2.DISTRIBUTE_SPACE;
        }
        return HorizontalAlign2.JUSTIFY;
    }

    public static VerticalAlign1 verticalAlign1(VerticalAlignment hwpVertAlign) {
        switch (hwpVertAlign) {
            case ByFont:
                return VerticalAlign1.BASELINE;
            case Top:
                return VerticalAlign1.TOP;
            case Center:
                return VerticalAlign1.CENTER;
            case Bottom:
                return VerticalAlign1.BOTTOM;
        }
        return VerticalAlign1.BASELINE;
    }

    public static String refID(long hwpRefID) {
        if (hwpRefID == -1) {
            return "4294967295";
        } else {
            return String.valueOf(hwpRefID);
        }
    }

    public static NumberType1 numberType1(ParagraphNumberFormat hwpParaNumberFormat) {
        switch (hwpParaNumberFormat) {
            case Number:
                return NumberType1.DIGIT;
            case CircledNumber:
                return NumberType1.CIRCLED_DIGIT;
            case UppercaseRomanNumber:
                return NumberType1.ROMAN_CAPITAL;
            case LowercaseRomanNumber:
                return NumberType1.ROMAN_SMALL;
            case UppercaseAlphabet:
                return NumberType1.LATIN_CAPITAL;
            case LowercaseAlphabet:
                return NumberType1.LATIN_SMALL;
            case CircledUppercaseAlphabet:
                return NumberType1.CIRCLED_LATIN_CAPTION;
            case CircledLowercaseAlphabet:
                return NumberType1.CIRCLED_LATIN_SMALL;
            case Hangul:
                return NumberType1.HANGUL_SYLLABLE;
            case CircledHangul:
                return NumberType1.CIRCLED_HANGUL_SYLLABLE;
            case HangulJamo:
                return NumberType1.HANGUL_JAMO;
            case CircledHangulJamo:
                return NumberType1.CIRCLED_HANGUL_JAMO;
            case HangulNumber:
                return NumberType1.HANGUL_PHONETIC;
            case HanjaNumber:
                return NumberType1.IDEOGRAPH;
            case CircledHanjaNumber:
                return NumberType1.CIRCLED_IDEOGRAPH;
        }
        return NumberType1.DIGIT;
    }

    public static NumberType2 toNumberType2(NumberShape hwpNumberShape) {
        switch (hwpNumberShape) {
            case Number:
                return NumberType2.DIGIT;
            case CircledNumber:
                return NumberType2.CIRCLED_DIGIT;
            case UppercaseRomanNumber:
                return NumberType2.ROMAN_CAPITAL;
            case LowercaseRomanNumber:
                return NumberType2.ROMAN_SMALL;
            case UppercaseAlphabet:
                return NumberType2.LATIN_CAPITAL;
            case LowercaseAlphabet:
                return NumberType2.LATIN_SMALL;
            case CircledUppercaseAlphabet:
                return NumberType2.CIRCLED_LATIN_CAPTION;
            case CircledLowercaseAlphabet:
                return NumberType2.CIRCLED_LATIN_SMALL;
            case Hangul:
                return NumberType2.HANGUL_SYLLABLE;
            case CircledHangul:
                return NumberType2.CIRCLED_HANGUL_SYLLABLE;
            case HangulJamo:
                return NumberType2.HANGUL_JAMO;
            case CircledHangulJamo:
                return NumberType2.CIRCLED_HANGUL_JAMO;
            case HangulNumber:
                return NumberType2.HANGUL_PHONETIC;
            case HanjaNumber:
                return NumberType2.IDEOGRAPH;
            case CircledHanjaNumber:
                return NumberType2.CIRCLED_IDEOGRAPH;
            case HangulSibgan:
                return NumberType2.DECAGON_CIRCLE;
            case HanjaSibgan:
                return NumberType2.DECAGON_CIRCLE_HANJA;
            case Symbol:
                return NumberType2.SYMBOL;
            case UserChar:
                return NumberType2.USER_CHAR;
        }
        return NumberType2.DIGIT;
    }

    public static String stringNullCheck(String str) {
        if (str != null && str.length() == 1 && str.charAt(0) == 0) {
            return "";
        } else {
            return str;
        }
    }

    public static VerticalAlign2 verticalAlign2(TextVerticalAlignment hwpTextVerticalAlignment) {
        switch (hwpTextVerticalAlignment) {
            case Top:
                return VerticalAlign2.TOP;
            case Center:
                return VerticalAlign2.CENTER;
            case Bottom:
                return VerticalAlign2.BOTTOM;
        }
        return VerticalAlign2.CENTER;
    }
}

