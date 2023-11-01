package kr.dogfoot.hwp2hwpx.section.object.gso.form;

import kr.dogfoot.hwplib.object.bodytext.control.form.properties.Property;
import kr.dogfoot.hwplib.object.bodytext.control.form.properties.PropertyNormal;
import kr.dogfoot.hwplib.object.bodytext.control.form.properties.PropertySet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NameValuesGetter {
    public static Map<String, String> get(PropertySet propertySet) {
        Map<String, String> nameValues = new HashMap<String, String>();
        core(propertySet, nameValues);
        return nameValues;
    }

    private static void core(PropertySet propertySet, Map<String, String> nameValues) {
        Set<String> names = propertySet.getNames();
        for (String name : names) {
            Property property = propertySet.getProperty(name);
            switch (property.getType()) {
                case Set:
                    core((PropertySet)property, nameValues);
                    break;
                case WString:
                case Int:
                case Bool:
                    addProperty((PropertyNormal) property, nameValues);
                    break;
                case NULL:
                    break;
            }
        }
    }

    private static void addProperty(PropertyNormal property, Map<String, String> nameValues) {
        nameValues.put(property.getName(), property.getValue());
    }
}
