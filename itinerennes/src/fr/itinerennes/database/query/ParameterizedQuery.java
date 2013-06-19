package fr.itinerennes.database.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.itinerennes.database.exception.QueryTemplateLoadException;
import fr.itinerennes.utils.IOUtils;

public abstract class ParameterizedQuery<T> {

    private final List<Param> parameters = new ArrayList<Param>();

    private String template;

    protected final void setParam(final String name, final String value) {
        parameters.add(new Param(name, value));
    }

    public final T execute(final SQLiteDatabase db) throws QueryTemplateLoadException {
        final Cursor results = db.rawQuery(getQuery(), null);
        try {
            return handleResult(results);
        } finally {
            IOUtils.close(results);
        }
    }

    protected abstract T handleResult(Cursor rawQuery);

    /**
     * @return the query string with parameterized values
     */
    private String getQuery() throws QueryTemplateLoadException {
        if (null == template) {
            template = loadQueryTemplate();
        }
        String query = null;
        for (final Param p : parameters) {
            query = template.replace(String.format(":%s", p.getName()), p.getValue());
        }
        return query;
    }

    /**
     * @return the query template, which is the content of the resource
     *         identified by <code>rawResId</code>
     */
    private String loadQueryTemplate() throws QueryTemplateLoadException {
        final String queryFileName = String.format("%s.sql", this.getClass().getName().replace('.', '/'));
        final InputStream in = this.getClass().getResourceAsStream(queryFileName);
        final BufferedReader r = new BufferedReader(new InputStreamReader(in));

        final StringBuilder queryBuf = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                queryBuf.append(line);
            }
            return queryBuf.toString();
        } catch (final IOException e) {
            throw new QueryTemplateLoadException(String.format("Can't load query, missing template: %s", queryFileName), e);
        } finally {
            try {
                r.close();
            } catch (final IOException e) {
            }
        }
    }

    private static class Param {
        private final String name;
        private final String value;

        public Param(final String name, final String value) {
            this.name = name;
            this.value = String.format("'%s'", value.replaceAll("'", "\\'"));
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
