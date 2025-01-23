package com.booleanuk.api.stock;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        StockRepository repo = new StockRepository();
        StockController stockController = new StockController();
        StockItem item = new StockItem(1,"Keyboard","Computer","Used for typing");
        StockItem item2 = new StockItem(8,"Screen","Computer","Used for viewing");
        //stockController.create(item);
        StockItem updatedItem = stockController.updateItem(4,item2);
        System.out.println(stockController.getItem(4));
        System.out.println(stockController.getAll());
    }
}
