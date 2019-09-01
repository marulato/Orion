package org.akita.sql.converter;

import org.akita.sql.utility.NameUtils;

public final class PropertyConverter {

    private static final PropertyConverter converter = new PropertyConverter();
    private PropertyConverter() {}

    public static PropertyConverter getInstance() {
        return converter;
    }

    public String getProperty(String column) {
        StringBuilder property = new StringBuilder();
        if (!NameUtils.isEmpty(column)) {
            if (NameUtils.hasUnderline(column)) {
                String[] props = column.split("_");
                property.append(props[0].toLowerCase());
                for (int i = 1; i < props.length; i++) {
                    property.append(NameUtils.toTitleCase(props[i].toLowerCase()));
                }
            } else {
                property.append(column.toLowerCase());
            }
        }
        return property.toString();
    }

    public String getColumn(String property) {
        StringBuilder column = new StringBuilder();
        if (!NameUtils.isEmpty(property)) {
            if (NameUtils.hasTitleCase(property)) {
                String[] props = NameUtils.splitByUpperCase(property);
                for (String prop : props) {
                    column.append(prop.toUpperCase()).append("_");
                }
                column.deleteCharAt(column.length() -1);
            } else {
                column.append(property.toUpperCase());
            }
        }
        return column.toString();
    }
}
