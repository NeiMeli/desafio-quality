package com.bootcamp.desafioquality.date;

import java.util.Date;

public class DateRange {
    private Date dateFrom;
    private Date dateTo;

    public DateRange(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateFrom(Date date) {
        this.dateFrom = date;
    }

    public void setDateTo(Date date) {
        this.dateTo = date;
    }

    public boolean isWithinRange(DateRange dateRange) {
        return isWithinRange(dateRange.dateFrom) && isWithinRange(dateRange.dateTo);
    }

    public boolean isWithinRange(Date date) {
        return dateFrom.compareTo(date) <= 0 && dateTo.compareTo(date) >= 0;
    }
}
