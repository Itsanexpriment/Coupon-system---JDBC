package com.paulgougassian.db.dao;

import com.paulgougassian.common.ConnectionPool;
import com.paulgougassian.db.Schema;
import com.paulgougassian.db.dao.wrapper.PreparedStatementWrapper;
import com.paulgougassian.db.dao.wrapper.functions.PreparedStatementFunction;
import com.paulgougassian.db.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl implements CustomerDao {
    private final PreparedStatementWrapper psWrapper;

    public CustomerDaoImpl(PreparedStatementWrapper psWrapper) {
        this.psWrapper = psWrapper;
    }

    @Override
    public Customer insert(Customer customer) {
        PreparedStatementFunction<Customer> function = ps -> {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPassword());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {
                customer.setId(keys.getInt(1));
            }
            return customer;
        };

        return psWrapper.executeQuery(Schema.INSERT_INTO_CUSTOMER,
                                      Statement.RETURN_GENERATED_KEYS, function);
    }

    @Override
    public boolean delete(long id) {
        PreparedStatementFunction<Boolean> function = ps -> {
            ps.setLong(1, id);
            return ps.executeUpdate() != 0;
        };

        return psWrapper.executeQuery(Schema.DELETE_FROM_CUSTOMER, function);
    }

    @Override
    public boolean updateEmail(long id, String email) {
        PreparedStatementFunction<Boolean> function = ps -> {
            ps.setString(1, email);
            ps.setLong(2, id);

            return ps.executeUpdate() != 0;
        };

        return psWrapper.executeQuery(Schema.UPDATE_CUSTOMER_EMAIL, function);
    }

    @Override
    public boolean updatePassword(long id, String password) {
        PreparedStatementFunction<Boolean> function = ps -> {
            ps.setString(1, password);
            ps.setLong(2, id);

            return ps.executeUpdate() != 0;
        };

        return psWrapper.executeQuery(Schema.UPDATE_CUSTOMER_PASSWORD, function);
    }

    @Override
    public Optional<Customer> findById(long id) {
        PreparedStatementFunction<Optional<Customer>> function = ps -> {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(createCustomer(rs));
            }
            return Optional.empty();
        };

        return psWrapper.executeQuery(Schema.SELECT_CUSTOMER_BY_ID, function);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        PreparedStatementFunction<Optional<Customer>> function = ps -> {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(createCustomer(rs));
            }
            return Optional.empty();
        };

        return psWrapper.executeQuery(Schema.SELECT_CUSTOMER_BY_EMAIL, function);
    }

    @Override
    public Optional<Customer> findByEmailAndPassword(String email, String password) {
        PreparedStatementFunction<Optional<Customer>> function = ps -> {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(createCustomer(rs));
            }
            return Optional.empty();
        };

        return psWrapper.executeQuery(Schema.SELECT_CUSTOMER_BY_EMAIL_AND_PASS, function);
    }

    @Override
    public List<Customer> findAll() {
        PreparedStatementFunction<List<Customer>> function = ps -> {
            List<Customer> customers = new ArrayList<>();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customers.add(createCustomer(rs));
            }
            return customers;
        };

        return psWrapper.executeQuery(Schema.SELECT_ALL_CUSTOMERS, function);
    }

    @Override
    public void purchase(long customerId, long couponId) {
        ConnectionPool cp = ConnectionPool.getInstance();
        Connection connection = cp.take();

        try {
            connection.setAutoCommit(false);

            PreparedStatement selectLockQuery = connection.prepareStatement(
                    Schema.SELECT_COUPON_AMOUNT_BY_ID_FOR_UPDATE);
            selectLockQuery.setLong(1, couponId);

            ResultSet rs = selectLockQuery.executeQuery();
            if (!rs.next() || rs.getInt(Schema.COL_AMOUNT) <= 0) {
                connection.rollback();
                return;
            }

            PreparedStatement decrementQuery = connection.prepareStatement(
                    Schema.DECREMENT_COUPON_AMOUNT);
            decrementQuery.setInt(1, 1);
            decrementQuery.setLong(2, couponId);
            decrementQuery.executeUpdate();

            PreparedStatement insertQuery = connection.prepareStatement(
                    Schema.INSERT_INTO_CUSTOMER_COUPON);
            insertQuery.setLong(1, customerId);
            insertQuery.setLong(2, couponId);
            insertQuery.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Unable to prepare statement: %s", e.getMessage()));
        } finally {
            cp.put(connection);
        }
    }

    private Customer createCustomer(ResultSet rs) throws SQLException {
        long id = rs.getLong(Schema.COL_ID);
        String firstName = rs.getString(Schema.COL_FIRST_NAME);
        String lastName = rs.getString(Schema.COL_LAST_NAME);
        String email = rs.getString(Schema.COL_EMAIL);
        String password = rs.getString(Schema.COL_PASSWORD);

        return new Customer(id, firstName, lastName, email, password);
    }
}
