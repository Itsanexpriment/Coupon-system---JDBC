package com.paulgougassian.db.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Customer {
    private static final int DEFAULT_ID = 0;

    private long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final Set<Coupon> coupons;

    public Customer(String firstName, String lastName, String email, String password) {
        this(DEFAULT_ID, firstName, lastName, email, password);
    }

    public Customer(long id, String firstName, String lastName, String email,
                    String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;

        coupons = new HashSet<>();
    }

    public void addCoupons(Collection<? extends Coupon> couponsToAdd) {
        coupons.addAll(couponsToAdd);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return firstName.equals(customer.firstName)
               && lastName.equals(customer.lastName)
               && email.equals(customer.email)
               && password.equals(customer.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return String.format("firstName = %s, lastName = %s", firstName, lastName);
    }
}
