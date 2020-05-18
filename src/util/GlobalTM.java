package util;

public class GlobalTM {
    private String date;
    private int confirmed;
    private int recovered;
    private int death;

    @Override
    public String toString() {
        return "GlobalTM{" +
                "date='" + date + '\'' +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", death=" + death +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public GlobalTM(String date, int confirmed, int recovered, int death) {
        this.date = date;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.death = death;
    }
}
