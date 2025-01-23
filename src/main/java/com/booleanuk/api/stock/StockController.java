package com.booleanuk.api.stock;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private StockRepository stockrepo;

    public StockController() throws SQLException {
        this.stockrepo = new StockRepository();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public StockItem create(@RequestBody StockItem item) throws SQLException {
        StockItem theItem = this.stockrepo.createItem(item);
        if (theItem == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error!");
        }
        return theItem;
    }

    @GetMapping
    public List<StockItem> getAll() throws SQLException {
        List<StockItem> stocks = stockrepo.getAll();
        if (stocks == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Empty list");
        }
        return stocks;
    }

    @GetMapping("/{id}")
    public StockItem getItem(@PathVariable long id) throws SQLException {
        StockItem item = stockrepo.getItem(id);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item not found");
        }
        return item;
    }

    @PutMapping("/{id}")
    public StockItem updateItem(@PathVariable long id, @RequestBody StockItem item) throws SQLException {
        StockItem updatedItem = stockrepo.getItem(id);
        if (updatedItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item not found");
        }
        return this.stockrepo.updateItem(id, item);
    }

    @DeleteMapping("/{id}")
    public StockItem deleteItem(@PathVariable long id) throws SQLException {
        StockItem deleteItem = stockrepo.deleteItem(id);
        if (deleteItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item not found");
        }
        return deleteItem;
    }
}
