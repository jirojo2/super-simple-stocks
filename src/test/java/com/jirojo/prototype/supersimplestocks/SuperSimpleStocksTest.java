package com.jirojo.prototype.supersimplestocks;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public class SuperSimpleStocksTest {
    private static SuperSimpleStocks stocks;

    @BeforeClass
    public static void SetUpClass() {
        stocks = new SuperSimpleStocks();

        // set up some stocks
        stocks.getStocks().put("TEA", new Stock("TEA", StockType.Common, 0, null, 100));
        stocks.getStocks().put("POP", new Stock("POP", StockType.Common, 8, null, 100));
        stocks.getStocks().put("ALE", new Stock("ALE", StockType.Common, 23, null, 60));
        stocks.getStocks().put("GIN", new Stock("GIN", StockType.Preferred, 8, 0.02, 100));
        stocks.getStocks().put("JOE", new Stock("JOE", StockType.Common, 13, null, 250));

        // add some trades to the stocks
        for (int i = 0; i < 10; i++) {
            stocks.getStocks().get("TEA").recordTrade(1, TradeDirection.Buy, 20.0);
            stocks.getStocks().get("POP").recordTrade(1, TradeDirection.Sell, 20.0);
            stocks.getStocks().get("GIN").recordTrade(1, TradeDirection.Buy, 25.0);
        }
        for (int i = 0; i < 2; i++) {
            stocks.getStocks().get("TEA").recordTrade(2, TradeDirection.Sell, 30.0);
            stocks.getStocks().get("ALE").recordTrade(2, TradeDirection.Buy, 20.0);
            stocks.getStocks().get("JOE").recordTrade(2, TradeDirection.Buy, 20.0);
        }

        stocks.getStocks().get("TEA").getTrades().get(0).setTimestamp(
                new Date(new Date().getTime() - Duration.ofMinutes(16).toMillis()));
    }

    @Test
    public void recordTrade() {
        List<Trade> trades = stocks.getStocks().get("TEA").getTrades();
        assertEquals(12, trades.size());

        assertEquals(1, trades.get(0).getShares());
        assertEquals(TradeDirection.Buy, trades.get(0).getDirection());
        assertEquals(20.0, trades.get(0).getPrice(), 0.001);

        assertEquals(2, trades.get(11).getShares());
        assertEquals(TradeDirection.Sell, trades.get(11).getDirection());
        assertEquals(30.0, trades.get(11).getPrice(), 0.001);
    }

    @Test
    public void calculateStockDividendYield() {
        assertEquals(0.0, stocks.getStocks().get("TEA").dividendYield(), 0.001);
        assertEquals(0.4, stocks.getStocks().get("POP").dividendYield(), 0.001);
        assertEquals(1.15, stocks.getStocks().get("ALE").dividendYield(), 0.001);
        assertEquals(0.08, stocks.getStocks().get("GIN").dividendYield(), 0.001);
        assertEquals(0.65, stocks.getStocks().get("JOE").dividendYield(), 0.001);
    }

    @Test
    public void calculateStockPERatio() {
        assertEquals(0.0, stocks.getStocks().get("TEA").peRatio(), 0.001);
        assertEquals(2.5, stocks.getStocks().get("POP").peRatio(), 0.001);
        assertEquals(0.869, stocks.getStocks().get("ALE").peRatio(), 0.001);
        assertEquals(3.125, stocks.getStocks().get("GIN").peRatio(), 0.001);
        assertEquals(1.538, stocks.getStocks().get("JOE").peRatio(), 0.001);
    }

    @Test
    public void calculateStockPriceLast15min() {
        assertEquals(23.077, stocks.getStocks().get("TEA").price(), 0.001);
        assertEquals(20.0, stocks.getStocks().get("POP").price(), 0.001);
        assertEquals(20.0, stocks.getStocks().get("ALE").price(), 0.001);
        assertEquals(25.0, stocks.getStocks().get("GIN").price(), 0.001);
        assertEquals(20.0, stocks.getStocks().get("JOE").price(), 0.001);
    }

    @Test
    public void calculateBGCEAllShareIndex() {
        assertEquals(21.519, stocks.calculateBGCEAllShareIndex(), 0.001);
    }
}
