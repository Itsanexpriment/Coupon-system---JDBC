package com.paulgougassian.service;

import com.paulgougassian.db.dao.CouponDao;
import com.paulgougassian.db.dao.CustomerDao;
import com.paulgougassian.db.model.Coupon;
import com.paulgougassian.db.model.Customer;
import com.paulgougassian.service.exceptions.DuplicateEmailException;
import com.paulgougassian.service.exceptions.InvalidCouponPurchaseException;
import com.paulgougassian.service.exceptions.InvalidPasswordException;
import com.paulgougassian.service.util.ServiceUtils;

import java.util.List;
import java.util.Optional;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerDao customerDao;
    private final CouponDao couponDao;

    public CustomerServiceImpl(CustomerDao customerDao, CouponDao couponDao) {
        this.customerDao = customerDao;
        this.couponDao = couponDao;
    }

    @Override
    public Optional<Customer> get(long id) {
        Optional<Customer> optCustomer = customerDao.findById(id);

        if (optCustomer.isEmpty()) {
            return Optional.empty();
        }
        Customer customer = optCustomer.get();

        List<Coupon> coupons = couponDao.findAllCustomerCoupons(id);
        customer.addCoupons(coupons);

        return Optional.of(customer);
    }

    @Override
    public Customer insert(Customer customer) {
        String email = customer.getEmail();

        if (isEmailDuplicate(email)) {
            throw new DuplicateEmailException(String.format(
                    "Invalid email address, %s already exists in the system", email));
        }

        return customerDao.insert(customer);
    }

    @Override
    public void delete(long id) {
        customerDao.delete(id);
    }

    @Override
    public void updateEmail(long id, String email) {
        if (isEmailDuplicate(email)) {
            throw new DuplicateEmailException(String.format(
                    "Invalid email address, %s already exists in the system", email));
        }

        customerDao.updateEmail(id, email);
    }

    @Override
    public void updatePassword(long id, String password) {
        if (!ServiceUtils.isPasswordValid(password)) {
            throw new InvalidPasswordException("The provided password is invalid");
        }

        customerDao.updatePassword(id, password);
    }

    @Override
    public void purchase(long customerId, long couponId) {
        Optional<Coupon> optCoupon = couponDao.findById(couponId);
        Coupon coupon = optCoupon.orElseThrow(() -> new IllegalArgumentException(
                String.format("Couldn't find a coupon with id no. %d", couponId)));

        Optional<Customer> optCustomer = customerDao.findById(customerId);
        Customer customer = optCustomer.orElseThrow(() -> new IllegalArgumentException(
                String.format("Couldn't find a customer with id no. %d", customerId)));

        if (coupon.getAmount() == 0) {
            throw new InvalidCouponPurchaseException("Coupon amount can't be zero");
        }

        if (customer.getCoupons().contains(coupon)) {
            throw new InvalidCouponPurchaseException(
                    String.format("Coupon - %s\n is already owned by customer - %s",
                                  coupon, customer));
        }
        customerDao.purchase(customerId, couponId);
    }

    private boolean isEmailDuplicate(String email) {
        return customerDao.findByEmail(email).isPresent();
    }
}
