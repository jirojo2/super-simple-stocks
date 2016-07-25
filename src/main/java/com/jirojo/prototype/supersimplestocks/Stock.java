package com.jirojo.prototype.supersimplestocks;

import java.time.Duration;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

public class Stock {
    private String symbol;
    private StockType type;
    private double lastDividend;
    private double fixedDividend;
    private double parValue;
    private List<Trade> trades = new LinkedList<>();

    public Stock(String symbol, StockType type, double lastDividend, Double fixedDividend, double parValue) {
        this.symbol = symbol;
        this.type = type;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend != null ? fixedDividend : 0.0;
        this.parValue = parValue;
    }

    private double tickerPrice() {
        if (trades.size() >= 1) {
            return trades.listIterator(trades.size()).previous().getPrice();
        }
        return 0.0;
    }

    public double price() {
        Date now = new Date();
        Date threshold = new Date(now.getTime() - Duration.ofMinutes(15).toMillis());

        DoubleAdder priceAdder = new DoubleAdder();
        LongAdder sharesAdder = new LongAdder();
        for (Trade trade : trades) {
            if (trade.getTimestamp().before(threshold)) {
                continue;
            }
            priceAdder.add(trade.getPrice() * trade.getShares());
            sharesAdder.add(trade.getShares());
        }
        return priceAdder.sum() / sharesAdder.sum();
    }

    public double dividendYield() {
        double tickerPrice = tickerPrice();
        if (tickerPrice == 0.0) {
            return 0.0;
        }
        else if (type == StockType.Common) {
            return lastDividend / tickerPrice;
        }
        else if (type == StockType.Preferred) {
            return fixedDividend * parValue / tickerPrice;
        }
        return 0.0;
    }

    public double peRatio() {
        if (lastDividend == 0) {
            return 0.0;
        }
        return tickerPrice() / lastDividend;
    }

    public void recordTrade(long shares, TradeDirection direction, double price) {
        Trade trade = new Trade();
        trade.setTimestamp(new Date());
        trade.setShares(shares);
        trade.setDirection(direction);
        trade.setPrice(price);
        trades.add(trade);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public StockType getType() {
        return type;
    }

    public void setType(StockType type) {
        this.type = type;
    }

    public double getLastDividend() {
        return lastDividend;
    }

    public void setLastDividend(Double lastDividend) {
        this.lastDividend = lastDividend;
    }

    public double getFixedDividend() {
        return fixedDividend;
    }

    public void setFixedDividend(Double fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public double getParValue() {
        return parValue;
    }

    public void setParValue(Double parValue) {
        this.parValue = parValue;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}
