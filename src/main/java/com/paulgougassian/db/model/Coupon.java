package com.paulgougassian.db.model;

import java.sql.Date;
import java.util.Objects;

public class Coupon {
    private static final int DEFAULT_ID = 0;

    private long id;
    private final long companyId;
    private final String title;
    private final int category;
    private final Date startDate;
    private final Date endDate;
    private final int amount;
    private final String description;
    private final double price;
    private final String imageUrl;

    public Coupon(long id, long companyId, String title, int category, Date startDate,
                  Date endDate, int amount, String description, double price,
                  String imageUrl) {
        this.id = id;
        this.companyId = companyId;
        this.title = title;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Coupon(long companyId, String title, int category, Date startDate,
                  Date endDate, int amount, String description, double price,
                  String imageUrl) {

        this(DEFAULT_ID, companyId, title, category, startDate,
             endDate, amount, description, price, imageUrl);
    }

    public long getId() {
        return id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public String getTitle() {
        return title;
    }

    public int getCategory() {
        return category;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return companyId == coupon.companyId && title.equals(coupon.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, title);
    }

    @Override
    public String toString() {
        return String.format("companyId = %d, title = %s", companyId, title);
    }
}
