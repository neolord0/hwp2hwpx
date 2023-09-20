package kr.dogfoot.hwp2hwpx;

public abstract class Converter {
    protected Parameter parameter;

    public Converter(Parameter parameter) {
        this.parameter = parameter;
    }
}
