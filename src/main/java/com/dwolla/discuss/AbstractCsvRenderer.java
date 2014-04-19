package com.dwolla.discuss;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractCsvRenderer<T> implements Renderer {

    private final PrintWriter out;

    private char separator = ',';

    private boolean includeHeaderRow = true;

    private boolean quoteAllValues = true;

    private char quoteChar = '"';
    
    private boolean newlineAtEnd = true;

    private final Class<T> rowType;
    
    private List<Column<?>> columns = new LinkedList<Column<?>>();

    public AbstractCsvRenderer(Class<T> rowType) {
        this.out = new PrintWriter(System.out);
        this.rowType = rowType;
    }

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public boolean isQuoteAllValues() {
        return quoteAllValues;
    }

    public void setQuoteAllValues(boolean quoteAllValues) {
        this.quoteAllValues = quoteAllValues;
    }

    public boolean isNewlineAtEnd() {
        return newlineAtEnd;
    }

    public void setNewlineAtEnd(boolean newlineAtEnd) {
        this.newlineAtEnd = newlineAtEnd;
    }

    public PrintWriter getOut() {
        return out;
    }

    public Class<T> getRowType() {
        return this.rowType;
    }

    public boolean isIncludeHeaderRow() {
        return includeHeaderRow;
    }

    public void setIncludeHeaderRow(boolean includeHeaderRow) {
        this.includeHeaderRow = includeHeaderRow;
    }

    protected abstract void configure();

    public void begin() {
        this.columns.clear();
        configure();
        if (includeHeaderRow) {
            boolean first = true;
            for (final Column<?> column : columns) {
                if (!first) {
                    out.print(getSeparator());
                }
                out.print(escape(column.getName()));
                first = false;
            }
            out.println();
        }
    }

    public void row(T t) {
        boolean first = true;
        for (final Column<?> column : columns) {
            if (!first) {
                out.print(getSeparator());
            }
            out.print(column.render(t));
            first = false;
        }
        out.println();
    }

    public void end() {
        if (newlineAtEnd) {
            out.println();
        }
    }
    
    protected <C> void addColumn(String name, Class<C> type) {
        this.columns.add(new Column<C>(name, type));
    }
    
    protected String escape(String value) {
        StringBuilder sb = new StringBuilder();
        boolean useQuotes = quoteAllValues;
        useQuotes |= value.indexOf(separator) > -1;
        if (useQuotes) {
            sb.append(quoteChar);
        }
        for (int i = 0, n = value.length(); i < n; i++) {
            char c = value.charAt(i);
            if (c == '\\') {
                sb.append('\\');
            } else if (c == quoteChar) {
                sb.append('\\');
            }
            sb.append(c);
        }
        if (useQuotes) {
            sb.append(quoteChar);
        }
        return sb.toString();
    }

    public class Column<C> implements Serializable {

        private static final long serialVersionUID = -1189327791156116599L;

        private final String name;
        
        private final Class<C> type;
        
        public Column(String _name, Class<C> _type) {
            this.name = _name;
            this.type = _type;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Class<?> getType() {
            return this.type;
        }
        
        public String render(T obj) {
            return "TEST";
        }
        
        public AbstractCsvRenderer<T> getRenderer() {
            return AbstractCsvRenderer.this;
        }
        
    }

}
