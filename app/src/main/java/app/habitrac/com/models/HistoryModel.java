package app.habitrac.com.models;

public class HistoryModel {
    long timestamp;
    String date;
    int count;

    public HistoryModel() {
    }

    public HistoryModel(long timestamp, String date, int count) {
        this.timestamp = timestamp;
        this.date = date;
        this.count = count;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
