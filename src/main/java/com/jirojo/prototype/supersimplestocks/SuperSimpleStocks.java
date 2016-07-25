package com.jirojo.prototype.supersimplestocks;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SuperSimpleStocks {
    private Map<String, Stock> stocks = new HashMap<>();

    public double calculateBGCEAllShareIndex() {
        double product = 1.0;
        for (Stock stock : stocks.values()) {
            product *= stock.price();
        }
        if (product == 0.0) {
            return 0.0;
        }
        return Math.pow(product, 1.0 / stocks.size());
    }

    public Map<String, Stock> getStocks() {
        return stocks;
    }

    public void printBanner() {
        String bannerFilePath = "/banner.txt";
        InputStream bannerFile = getClass().getResourceAsStream(bannerFilePath);
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(bannerFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processCmd(String cmd) {
        String[] params = cmd.split(" ");
        String op = params[0];
        Stock stock;

        switch (op) {
            case "help":
                System.out.println("Available commands:");
                System.out.println("  help");
                System.out.println("  exit");
                System.out.println("  list");
                System.out.println("  trades");
                System.out.println("  gbce");
                System.out.println("  price <stock>");
                System.out.println("  pe-ratio <stock>");
                System.out.println("  dividend-yield <stock>");
                System.out.println("  record <stock> <quantity> <buy/sell> <price>");
                System.out.println();
                break;
            case "exit":
                System.exit(0);
                break;
            case "list":
                for (Stock s : stocks.values()) {
                    System.out.println(String.format(" %s", s.getSymbol()));
                }
                System.out.println();
                break;
            case "trades":
                if (params.length < 2) {
                    processCmd("help");
                    return;
                }
                stock = stocks.get(params[1]);
                System.out.println(String.format("Trades for Stock %s", stock.getSymbol()));
                for (Trade trade : stock.getTrades()) {
                    System.out.println(String.format(" * [%s] %s %d at %.2f",
                            trade.getTimestamp(),
                            trade.getDirection(),
                            trade.getShares(),
                            trade.getPrice()));
                }
                System.out.println();
                break;
            case "price":
                if (params.length < 2) {
                    processCmd("help");
                    return;
                }
                stock = stocks.get(params[1]);
                System.out.println(String.format("Price of %.2f for Stock %s",
                        stock.price(),
                        stock.getSymbol()));
                System.out.println();
                break;
            case "pe-ratio":
                if (params.length < 2) {
                    processCmd("help");
                    return;
                }
                stock = stocks.get(params[1]);
                System.out.println(String.format("P/E Ratio of %.2f for Stock %s",
                        stock.peRatio(),
                        stock.getSymbol()));
                System.out.println();
                break;
            case "dividend-yield":
                if (params.length < 2) {
                    processCmd("help");
                    return;
                }
                stock = stocks.get(params[1]);
                System.out.println(String.format("Dividend Yield of %.2f for Stock %s",
                        stock.dividendYield(),
                        stock.getSymbol()));
                System.out.println();
                break;
            case "record":
                if (params.length < 5) {
                    processCmd("help");
                    return;
                }
                stock = stocks.get(params[1]);
                long quantity = Long.parseLong(params[2]);
                TradeDirection direction = params[3].equalsIgnoreCase("BUY")
                        ? TradeDirection.Buy
                        : TradeDirection.Sell;
                double price = Double.parseDouble(params[4]);
                stock.recordTrade(quantity, direction, price);
                break;
            case "gbce":
                System.out.println(String.format("GBCE All Share Index of %.2f",
                        calculateBGCEAllShareIndex()));
                System.out.println();
                break;
            default:
                System.out.println("Unknown command " + params[0]);
                processCmd("help");
        }
    }

    public static void main(String[] args) {
        SuperSimpleStocks sss = new SuperSimpleStocks();
        sss.printBanner();

        sss.getStocks().put("TEA", new Stock("TEA", StockType.Common, 0, null, 100));
        sss.getStocks().put("POP", new Stock("POP", StockType.Common, 8, null, 100));
        sss.getStocks().put("ALE", new Stock("ALE", StockType.Common, 23, null, 60));
        sss.getStocks().put("GIN", new Stock("GIN", StockType.Preferred, 8, 0.02, 100));
        sss.getStocks().put("JOE", new Stock("JOE", StockType.Common, 13, null, 250));

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String cmd;
            sss.processCmd("help");
            System.out.print("CLI> ");
            while ((cmd = br.readLine()) != null) {
                sss.processCmd(cmd);
                System.out.print("CLI> ");
            }
        } catch(IOException io){
            io.printStackTrace();
        }
    }
}
