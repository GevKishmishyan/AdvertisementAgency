package model;

import java.io.Serializable;
import java.util.Date;

public class Ad implements Serializable, Comparable<Ad> {

    private long id;
    private String title;
    private String text;
    private double price;
    private Date date;
    private Category category;
    private User author;

    public Ad(long id, String title, String text, double price, Date date, Category category, User author) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.price = price;
        this.date = date;
        this.category = category;
        this.author = author;
    }

    public Ad() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ad ad = (Ad) o;

        if (id != ad.id) return false;
        if (Double.compare(ad.price, price) != 0) return false;
        if (title != null ? !title.equals(ad.title) : ad.title != null) return false;
        if (text != null ? !text.equals(ad.text) : ad.text != null) return false;
        if (date != null ? !date.equals(ad.date) : ad.date != null) return false;
        if (category != ad.category) return false;
        return author != null ? author.equals(ad.author) : ad.author == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", category=" + category +
                ", author=" + author +
                '}';
    }

    @Override
    public int compareTo(Ad o) {
        return this.title.compareTo(o.title);
    }
}
