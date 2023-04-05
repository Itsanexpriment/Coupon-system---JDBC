package com.paulgougassian.db.dao;

import com.paulgougassian.db.Schema;
import com.paulgougassian.db.dao.wrapper.PreparedStatementWrapper;
import com.paulgougassian.db.dao.wrapper.functions.PreparedStatementFunction;
import com.paulgougassian.db.model.Company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyDAOImpl implements CompanyDAO {
    private final PreparedStatementWrapper psWrapper;

    public CompanyDAOImpl(PreparedStatementWrapper psWrapper) {
        this.psWrapper = psWrapper;
    }

    @Override
    public Company insert(Company company) {
        PreparedStatementFunction<Company> function = ps -> {
            ps.setString(1, company.getName());
            ps.setString(2, company.getEmail());
            ps.setString(3, company.getPassword());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {
                company.setId(keys.getLong(1));
            }
            return company;
        };

        return psWrapper.executeQuery(Schema.INSERT_INTO_COMPANY,
                                      Statement.RETURN_GENERATED_KEYS, function);
    }

    @Override
    public boolean delete(long id) {
        PreparedStatementFunction<Boolean> function = ps -> {
            ps.setLong(1, id);

            return ps.executeUpdate() != 0;
        };

        return psWrapper.executeQuery(Schema.DELETE_FROM_COMPANY, function);
    }

    @Override
    public boolean updateEmail(long id, String email) {
        PreparedStatementFunction<Boolean> function = ps -> {
            ps.setString(1, email);
            ps.setLong(2, id);

            return ps.executeUpdate() != 0;
        };

        return psWrapper.executeQuery(Schema.UPDATE_COMPANY_EMAIL, function);
    }

    @Override
    public boolean updatePassword(long id, String password) {
        PreparedStatementFunction<Boolean> function = ps -> {
            ps.setString(1, password);
            ps.setLong(2, id);

            return ps.executeUpdate() != 0;
        };

        return psWrapper.executeQuery(Schema.UPDATE_COMPANY_PASSWORD, function);
    }

    @Override
    public Optional<Company> findById(long id) {
        PreparedStatementFunction<Optional<Company>> function = ps -> {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(createCompany(rs));
            }
            return Optional.empty();
        };

        return psWrapper.executeQuery(Schema.SELECT_COMPANY_BY_ID, function);
    }

    @Override
    public Optional<Company> findByEmail(String email) {
        PreparedStatementFunction<Optional<Company>> function = ps -> {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(createCompany(rs));
            }
            return Optional.empty();
        };

        return psWrapper.executeQuery(Schema.SELECT_COMPANY_BY_EMAIL, function);
    }

    @Override
    public Optional<Company> findByEmailAndPassword(String email, String password) {
        PreparedStatementFunction<Optional<Company>> function = ps -> {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(createCompany(rs));
            }
            return Optional.empty();
        };

        return psWrapper.executeQuery(Schema.SELECT_COMPANY_BY_EMAIL_AND_PASS, function);
    }

    @Override
    public List<Company> finalAll() {
        PreparedStatementFunction<List<Company>> function = ps -> {
            List<Company> companies = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                companies.add(createCompany(rs));
            }
            return companies;
        };

        return psWrapper.executeQuery(Schema.SELECT_ALL_COMPANIES, function);
    }

    private Company createCompany(ResultSet rs) throws SQLException {
        long id = rs.getLong(Schema.COL_ID);
        String name = rs.getString(Schema.COL_NAME);
        String email = rs.getString(Schema.COL_EMAIL);
        String password = rs.getString(Schema.COL_PASSWORD);

        return new Company(id, name, email, password);
    }
}
