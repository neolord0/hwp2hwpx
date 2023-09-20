package kr.dogfoot.hwp2hwpx.header;

import kr.dogfoot.hwp2hwpx.Converter;
import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwplib.object.etc.UnknownRecord;
import kr.dogfoot.hwpxlib.object.content.header_xml.RefList;

import java.util.ArrayList;

public class ForTrackChanges extends Converter {
    public ForTrackChanges(Parameter parameter) {
        super(parameter);
    }

    public void convert(RefList refList, ArrayList<UnknownRecord> trackChange2List) {
        // todo : TrackChanges ??
    }
}
