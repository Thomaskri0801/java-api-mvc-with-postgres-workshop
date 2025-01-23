package com.booleanuk.api.stock;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StockRepository {

    DataSource datasource;
    String dbUser;
    String dbURL;
    String dbPassword;
    String dbDatabase;
    Connection connection;

    public StockRepository() throws SQLException {
        this.getDatabaseCredentials();
        this.datasource = this.createDataSource();
        this.connection = this.datasource.getConnection();
    }

    private void getDatabaseCredentials() {
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            this.dbUser = prop.getProperty("db.user");
            this.dbURL = prop.getProperty("db.url");
            this.dbPassword = prop.getProperty("db.password");
            this.dbDatabase = prop.getProperty("db.database");
        } catch(Exception e) {
            System.out.println("Oops: " + e);
        }
    }

    private DataSource createDataSource() {
        // The url specifies the address of our database along with username and password credentials
        // you should replace these with your own username and password
        final String url = "jdbc:postgresql://" + this.dbURL + ":5432/" + this.dbDatabase + "?user=" + this.dbUser +"&password=" + this.dbPassword;
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    public StockItem createItem(StockItem item) throws SQLException {
        String sql = "INSERT INTO stocks (name, category, description) VALUES (?,?,?)";
        PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, item.getName());
        statement.setString(2, item.getCategory());
        statement.setString(3, item.getDescription());

        int rowsAffected = statement.executeUpdate();
        long newId = 0;
        if (rowsAffected > 0) {
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    newId = rs.getLong(1);
                }
            } catch (Exception e) {
                System.out.println("Error: "+e);
            }
            item.setId(newId);
        } else {
            item = null;
        }
        return item;
    }

    public List<StockItem> getAll() throws SQLException {
        List<StockItem> stocks = new ArrayList<>();
        String sql = "SELECT * FROM stocks";
        PreparedStatement statement = this.connection.prepareStatement(sql);

        ResultSet results = statement.executeQuery();

        while (results.next()) {
            StockItem theItem = new StockItem(results.getLong("id"),results.getString("name"), results.getString("category"), results.getString("description"));
            stocks.add(theItem);
        }
        return stocks;
    }

    public StockItem getItem(long id) throws SQLException {
        String sql = "SELECT * FROM stocks WHERE id = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setLong(1,id);
        ResultSet results = statement.executeQuery();
        StockItem item = null;
        if(results.next()) {
            item = new StockItem(results.getLong("id"),results.getString("name"), results.getString("category"), results.getString("description"));
        }
        return item;
    }

    public StockItem updateItem(long id, StockItem item) throws SQLException {
        String sql = "UPDATE stocks " +
                "SET name = ?" +
                "category = ?" +
                "description = ?" +
                "WHERE id = ?";

        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setString(1, item.getName());
        statement.setString(2, item.getCategory());
        statement.setString(3, item.getDescription());
        statement.setLong(4,id);
        int rowsAffected = statement.executeUpdate();
        StockItem updatedItem = null;
        if (rowsAffected > 0) {
            updatedItem = this.getItem(id);
        }
        return updatedItem;
    }

    public StockItem deleteItem(long id) throws SQLException {
        String sql = "DELETE FROM stocks WHERE id = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);

        StockItem deletedItem = null;
        deletedItem = this.getItem(id);

        statement.setLong(1, id);
        int rowsAffected = statement.executeUpdate();
        if (rowsAffected == 0) {
            deletedItem = null;
        }
        return deletedItem;
    }
}
