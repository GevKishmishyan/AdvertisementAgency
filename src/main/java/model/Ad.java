package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class Ad implements Serializable, Comparable<Ad> {

    private long id;
    private String title;
    private String text;
    private double price;
    private Date date;
    private Category category;
    private User author;

    public Ad(String title, String text, double price, Date date, Category category, User author) {
        this.title = title;
        this.text = text;
        this.price = price;
        this.date = date;
        this.category = category;
        this.author = author;
    }

    @Override
    public int compareTo(Ad o) {
        return this.title.compareTo(o.title);
    }
}
