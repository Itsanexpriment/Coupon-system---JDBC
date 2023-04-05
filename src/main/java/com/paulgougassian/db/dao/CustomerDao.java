package com.paulgougassian.db.dao;

import com.paulgougassian.db.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    Customer insert(Customer customer);

    boolean delete(long id);

    boolean updateEmail(long id, String email);

    boolean updatePassword(long id, String password);

    Optional<Customer> findById(long id);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByEmailAndPassword(String email, String password);

    List<Customer> findAll();

    void purchase(long customerId, long couponId);
}
