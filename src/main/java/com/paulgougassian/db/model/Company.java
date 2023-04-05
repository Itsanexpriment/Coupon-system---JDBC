package com.paulgougassian.db.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Company {
    public static final int DEFAULT_ID = 0;

    private long id;
    private final String name;
    private final String email;
    private final String password;
    private final Set<Coupon> coupons;

    public Company(String name, String email, String password) {
        this(DEFAULT_ID, name, email, password);
    }

    public Company(long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;

        coupons = new HashSet<>();
    }

    public void addCoupons(Collection<? extends Coupon> couponsToAdd) {
        coupons.addAll(couponsToAdd);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return name.equals(company.name)
               && email.equals(company.email)
               && password.equals(company.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }
}
