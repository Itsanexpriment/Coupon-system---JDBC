package com.paulgougassian.service;

import com.paulgougassian.db.model.Customer;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> get(long id);

    Customer insert(Customer customer);

    void delete(long id);

    void updateEmail(long id, String email);

    void updatePassword(long id, String password);

    void purchase(long customerId, long couponId);
}
