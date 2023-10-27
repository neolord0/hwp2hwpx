package kr.dogfoot.hwp2hwpx.section.object.gso;

import kr.dogfoot.hwp2hwpx.Parameter;
import kr.dogfoot.hwp2hwpx.section.object.comm.ForShapeComponent;
import kr.dogfoot.hwplib.object.bodytext.control.gso.ControlContainer;
import kr.dogfoot.hwplib.object.bodytext.control.gso.GsoControl;
import kr.dogfoot.hwplib.object.bodytext.control.gso.shapecomponent.ShapeComponentContainer;
import kr.dogfoot.hwpxlib.object.content.section_xml.paragraph.object.Container;

public class ForContainer extends ForShapeComponent {
    private ForGso gsoConverter;

    private Container container;
    private ControlContainer hwpContainer;

    public ForContainer(Parameter parameter, ForGso gsoConverter) {
        super(parameter);
        this.gsoConverter = gsoConverter;
    }

    public void convert(Container container, ControlContainer hwpContainer) {
        shapeComponent(container, hwpContainer);

        this.container = container;
        this.hwpContainer = hwpContainer;

        ShapeComponentContainer hwpSCC = ((ShapeComponentContainer) hwpContainer.getShapeComponent());
        container
                .instid(String.valueOf(hwpSCC.getInstid()));

        children();
    }

    private void children() {
        for (GsoControl hwpChild : hwpContainer.getChildControlList()) {
            gsoConverter.convert(container, hwpChild);
        }
    }
}
