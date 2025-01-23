package com.booleanuk.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        CustomerRepository myRepo = new CustomerRepository();
        try {
            myRepo.connectToDatabase();
        }
        catch(Exception e) {
            System.out.println("Oops: " + e);
        }
    }
}