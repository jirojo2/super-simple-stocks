package com.jirojo.prototype.supersimplestocks;

import java.util.Date;

public class Trade {
    private Date timestamp;
    private long shares;
    private TradeDirection direction;
    private double price;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getShares() {
        return shares;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

    public TradeDirection getDirection() {
        return direction;
    }

    public void setDirection(TradeDirection direction) {
        this.direction = direction;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
