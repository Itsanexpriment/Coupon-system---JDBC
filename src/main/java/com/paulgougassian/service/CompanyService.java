package com.paulgougassian.service;

import com.paulgougassian.db.model.Company;
import com.paulgougassian.db.model.Coupon;

import java.util.Optional;

public interface CompanyService {
    Optional<Company> get(long id);

    Company insert(Company company);

    void delete(long id);

    void updateEmail(long id, String email);

    void updatePassword(long id, String password);

    void create(Coupon coupon);
}
