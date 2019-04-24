package com.components.ras.ras;

public class income_model  {
    private String eventName;
    private double outcome;
    private Boolean isWin;

    public Boolean getWin() {
        return isWin;
    }

    public income_model(String eventName, double outcome, Boolean isWin) {
        this.eventName = eventName;
        this.outcome = outcome;
        this.isWin = isWin;
    }

    public void setWin(Boolean win) {
        isWin = win;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getOutcome() {
        return outcome;
    }

    public void setOutcome(double outcome) {
        this.outcome = outcome;
    }

}
