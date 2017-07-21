package br.com.wasys.gn.motorista.repository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.wasys.library.utils.DateUtils;

/**
 * Created by pascke on 29/05/16.
 */
public class Filtro extends HashMap<String, Object[]> {

    private String clauses;
    private String[] arguments;
    private Map<String, Comparison> comparisons = new HashMap<>();

    public enum Comparison {
        EQUAL ("= %1$s"),
        NOT_EQUAL ("<> %1$s"),
        LESS_THAN ("< %1$s"),
        LESS_EQUAL ("<= %1$s"),
        GREATER_THAN ("> %1$s"),
        GREATER_EQUAL (">= %1$s"),
        IN ("in (%1$s)"),
        NOT_IN ("not in (%1$s)");
        private final String format;
        Comparison(String format) {
            this.format = format;
        }
        public String format(String wildcard) {
            return String.format(format, wildcard);
        }
    }

    private void make() {
        clauses = null;
        arguments = null;
        Set<Entry<String, Object[]>> entries = entrySet();
        if (CollectionUtils.isNotEmpty(entries)) {
            StringBuilder sql = new StringBuilder();
            List<String> objects = new LinkedList<>();
            for (Entry<String, Object[]> entry : entries) {
                String key = entry.getKey();
                Object[] values = entry.getValue();
                if (sql.length() > 0) {
                    sql.append(" and ");
                }
                sql.append(key);
                sql.append(" ");
                Comparison comparison = comparisons.get(key);
                if (comparison == null) {
                    if (values.length > 1) {
                        comparison = Comparison.IN;
                    }
                    else {
                        comparison = Comparison.EQUAL;
                    }
                }
                if (!Comparison.IN.equals(comparison) && !Comparison.NOT_IN.equals(comparison)) {
                    sql.append(comparison.format("?"));
                    objects.add((String) values[0]);
                }
                else {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0) {
                            builder.append(", ");
                        }
                        builder.append("?");
                        objects.add((String) values[i]);
                    }
                    sql.append(comparison.format(String.valueOf(builder)));
                }
            }
            clauses = String.valueOf(sql);
            arguments = objects.toArray(new String[objects.size()]);
        }
    }

    @Override
    public Object[] put(String key, Object... values) {
        Object[] oldValues = null;
        if (ArrayUtils.isNotEmpty(values)) {
            oldValues = super.put(key, values);
        }
        else {
            oldValues = get(key);
            remove(key);
        }
        make();
        return oldValues;
    }

    public void add(String key, Comparison comparison, Object... objects) {
        comparisons.put(key, comparison);
        add(key, objects);
    }

    public void add(String key, Object... objects) {
        if (ArrayUtils.isEmpty(objects)) {
            put(key, null);
        }
        else {
            List<String> values = new ArrayList<>();
            for (Object object : objects) {
                if (object != null) {
                    if (object instanceof Date) {
                        values.add(DateUtils.format((Date) object, DateUtils.DateType.DATA_BASE.getPattern()));
                    }
                    else {
                        values.add(String.valueOf(object));
                    }
                }
            }
            put(key, values.toArray(new String[values.size()]));
        }
    }

    public String getClauses() {
        return clauses;
    }

    public String[] getArguments() {
        return arguments;
    }
}
