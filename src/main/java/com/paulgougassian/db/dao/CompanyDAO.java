package com.paulgougassian.db.dao;

import com.paulgougassian.db.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDAO {
    Company insert(Company company);

    boolean delete(long id);

    boolean updateEmail(long id, String email);

    boolean updatePassword(long id, String password);

    Optional<Company> findById(long id);

    Optional<Company> findByEmail(String email);

    Optional<Company> findByEmailAndPassword(String email, String password);

    List<Company> finalAll();
}
