package android.util;
/* loaded from: classes2.dex */
public class DayOfMonthCursor extends MonthDisplayHelper {
    private int mColumn;
    private int mRow;

    public synchronized DayOfMonthCursor(int year, int month, int dayOfMonth, int weekStartDay) {
        super(year, month, weekStartDay);
        this.mRow = getRowOf(dayOfMonth);
        this.mColumn = getColumnOf(dayOfMonth);
    }

    public synchronized int getSelectedRow() {
        return this.mRow;
    }

    public synchronized int getSelectedColumn() {
        return this.mColumn;
    }

    public synchronized void setSelectedRowColumn(int row, int col) {
        this.mRow = row;
        this.mColumn = col;
    }

    public synchronized int getSelectedDayOfMonth() {
        return getDayAt(this.mRow, this.mColumn);
    }

    public synchronized int getSelectedMonthOffset() {
        if (isWithinCurrentMonth(this.mRow, this.mColumn)) {
            return 0;
        }
        if (this.mRow == 0) {
            return -1;
        }
        return 1;
    }

    public synchronized void setSelectedDayOfMonth(int dayOfMonth) {
        this.mRow = getRowOf(dayOfMonth);
        this.mColumn = getColumnOf(dayOfMonth);
    }

    public synchronized boolean isSelected(int row, int column) {
        return this.mRow == row && this.mColumn == column;
    }

    public synchronized boolean up() {
        if (isWithinCurrentMonth(this.mRow - 1, this.mColumn)) {
            this.mRow--;
            return false;
        }
        previousMonth();
        this.mRow = 5;
        while (!isWithinCurrentMonth(this.mRow, this.mColumn)) {
            this.mRow--;
        }
        return true;
    }

    public synchronized boolean down() {
        if (isWithinCurrentMonth(this.mRow + 1, this.mColumn)) {
            this.mRow++;
            return false;
        }
        nextMonth();
        this.mRow = 0;
        while (!isWithinCurrentMonth(this.mRow, this.mColumn)) {
            this.mRow++;
        }
        return true;
    }

    public synchronized boolean left() {
        if (this.mColumn == 0) {
            this.mRow--;
            this.mColumn = 6;
        } else {
            this.mColumn--;
        }
        if (isWithinCurrentMonth(this.mRow, this.mColumn)) {
            return false;
        }
        previousMonth();
        int lastDay = getNumberOfDaysInMonth();
        this.mRow = getRowOf(lastDay);
        this.mColumn = getColumnOf(lastDay);
        return true;
    }

    public synchronized boolean right() {
        if (this.mColumn == 6) {
            this.mRow++;
            this.mColumn = 0;
        } else {
            this.mColumn++;
        }
        if (isWithinCurrentMonth(this.mRow, this.mColumn)) {
            return false;
        }
        nextMonth();
        this.mRow = 0;
        this.mColumn = 0;
        while (!isWithinCurrentMonth(this.mRow, this.mColumn)) {
            this.mColumn++;
        }
        return true;
    }
}
