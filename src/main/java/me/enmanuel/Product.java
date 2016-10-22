package me.enmanuel;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Enmanuel
 * Date: 27/07/2016
 * Time: 05:35 PM
 */
public class Product {
    Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate date;
    String name;
    double quantity;
    double price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
