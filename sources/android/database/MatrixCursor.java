package android.database;

import android.annotation.UnsupportedAppUsage;
import com.xiaopeng.util.FeatureOption;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class MatrixCursor extends AbstractCursor {
    private final int columnCount;
    private final String[] columnNames;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Object[] data;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int rowCount;

    public MatrixCursor(String[] columnNames, int initialCapacity) {
        this.rowCount = 0;
        this.columnNames = columnNames;
        this.columnCount = columnNames.length;
        this.data = new Object[this.columnCount * (initialCapacity < 1 ? 1 : initialCapacity)];
    }

    public MatrixCursor(String[] columnNames) {
        this(columnNames, 16);
    }

    @UnsupportedAppUsage
    private Object get(int column) {
        if (column < 0 || column >= this.columnCount) {
            throw new CursorIndexOutOfBoundsException("Requested column: " + column + ", # of columns: " + this.columnCount);
        } else if (this.mPos < 0) {
            throw new CursorIndexOutOfBoundsException("Before first row.");
        } else {
            if (this.mPos >= this.rowCount) {
                throw new CursorIndexOutOfBoundsException("After last row.");
            }
            return this.data[(this.mPos * this.columnCount) + column];
        }
    }

    public RowBuilder newRow() {
        int row = this.rowCount;
        this.rowCount = row + 1;
        int endIndex = this.rowCount * this.columnCount;
        ensureCapacity(endIndex);
        return new RowBuilder(row);
    }

    public void addRow(Object[] columnValues) {
        int length = columnValues.length;
        int i = this.columnCount;
        if (length != i) {
            throw new IllegalArgumentException("columnNames.length = " + this.columnCount + ", columnValues.length = " + columnValues.length);
        }
        int i2 = this.rowCount;
        this.rowCount = i2 + 1;
        int start = i2 * i;
        ensureCapacity(i + start);
        System.arraycopy(columnValues, 0, this.data, start, this.columnCount);
    }

    public void addRow(Iterable<?> columnValues) {
        int i = this.rowCount;
        int i2 = this.columnCount;
        int start = i * i2;
        int end = i2 + start;
        ensureCapacity(end);
        if (columnValues instanceof ArrayList) {
            addRow((ArrayList) columnValues, start);
            return;
        }
        int current = start;
        Object[] localData = this.data;
        for (Object columnValue : columnValues) {
            if (current == end) {
                throw new IllegalArgumentException("columnValues.size() > columnNames.length");
            }
            localData[current] = columnValue;
            current++;
        }
        if (current != end) {
            throw new IllegalArgumentException("columnValues.size() < columnNames.length");
        }
        this.rowCount++;
    }

    private void addRow(ArrayList<?> columnValues, int start) {
        int size = columnValues.size();
        if (size != this.columnCount) {
            throw new IllegalArgumentException("columnNames.length = " + this.columnCount + ", columnValues.size() = " + size);
        }
        this.rowCount++;
        Object[] localData = this.data;
        for (int i = 0; i < size; i++) {
            localData[start + i] = columnValues.get(i);
        }
    }

    private void ensureCapacity(int size) {
        Object[] objArr = this.data;
        if (size > objArr.length) {
            Object[] oldData = this.data;
            int newSize = objArr.length * 2;
            if (newSize < size) {
                newSize = size;
            }
            this.data = new Object[newSize];
            System.arraycopy(oldData, 0, this.data, 0, oldData.length);
        }
    }

    /* loaded from: classes.dex */
    public class RowBuilder {
        private final int endIndex;
        private int index;
        private final int row;

        RowBuilder(int row) {
            this.row = row;
            this.index = MatrixCursor.this.columnCount * row;
            this.endIndex = this.index + MatrixCursor.this.columnCount;
        }

        public RowBuilder add(Object columnValue) {
            if (this.index != this.endIndex) {
                Object[] objArr = MatrixCursor.this.data;
                int i = this.index;
                this.index = i + 1;
                objArr[i] = columnValue;
                return this;
            }
            throw new CursorIndexOutOfBoundsException("No more columns left.");
        }

        public RowBuilder add(String columnName, Object value) {
            for (int i = 0; i < MatrixCursor.this.columnNames.length; i++) {
                if (columnName.equals(MatrixCursor.this.columnNames[i])) {
                    MatrixCursor.this.data[(this.row * MatrixCursor.this.columnCount) + i] = value;
                }
            }
            return this;
        }

        public final RowBuilder add(int columnIndex, Object value) {
            MatrixCursor.this.data[(this.row * MatrixCursor.this.columnCount) + columnIndex] = value;
            return this;
        }
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getCount() {
        return this.rowCount;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String[] getColumnNames() {
        return this.columnNames;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String getString(int column) {
        Object value = get(column);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public short getShort(int column) {
        Object value = get(column);
        if (value == null) {
            return (short) 0;
        }
        return value instanceof Number ? ((Number) value).shortValue() : Short.parseShort(value.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getInt(int column) {
        Object value = get(column);
        if (value == null) {
            return 0;
        }
        return value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public long getLong(int column) {
        Object value = get(column);
        if (value == null) {
            return 0L;
        }
        return value instanceof Number ? ((Number) value).longValue() : Long.parseLong(value.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public float getFloat(int column) {
        Object value = get(column);
        if (value == null) {
            return 0.0f;
        }
        return value instanceof Number ? ((Number) value).floatValue() : Float.parseFloat(value.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public double getDouble(int column) {
        Object value = get(column);
        return value == null ? FeatureOption.FO_BOOT_POLICY_CPU : value instanceof Number ? ((Number) value).doubleValue() : Double.parseDouble(value.toString());
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public byte[] getBlob(int column) {
        Object value = get(column);
        return (byte[]) value;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getType(int column) {
        return DatabaseUtils.getTypeOfObject(get(column));
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean isNull(int column) {
        return get(column) == null;
    }
}
