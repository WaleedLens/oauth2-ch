package org.example.database;

import java.util.List;
import java.util.Objects;

public class Table {
    private List<String> fields;
    private List<Object> values;
    private String name;

    public Table(List<String> fields, List<Object> values, String name) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Fields cannot be null or empty");
        }
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Values cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.fields = fields;
        this.values = values;
        this.name = name;
    }

    public List<String> getFields() {
        return fields;
    }

    public List<Object> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Table{" +
                "fields=" + fields +
                ", values=" + values +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(fields, table.fields) &&
                Objects.equals(values, table.values) &&
                Objects.equals(name, table.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields, values, name);
    }
}