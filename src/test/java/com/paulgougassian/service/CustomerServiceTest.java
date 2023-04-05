package com.paulgougassian.service;

import com.paulgougassian.TestUtils;
import com.paulgougassian.db.dao.CouponDao;
import com.paulgougassian.db.model.Company;
import com.paulgougassian.db.model.Coupon;
import com.paulgougassian.db.model.Customer;
import com.paulgougassian.service.exceptions.DuplicateEmailException;
import com.paulgougassian.service.exceptions.InvalidCouponPurchaseException;
import com.paulgougassian.service.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*
    WARNING: before each test, all records are cleared from the database!
 */

public class CustomerServiceTest {
    private CustomerService customerService;
    private Customer customer;

    @BeforeEach
    void initializeFields() {
        initCustomerService();
        initCustomer();

        TestUtils.clearDB(); // deletes all records from the DB!
    }

    @Test
    void insertingNewCustomer() {
        long preInsertId = customer.getId();

        Customer postInsertCustomer = customerService.insert(customer);
        long postInsertId = postInsertCustomer.getId();

        assertNotEquals(preInsertId, postInsertId);
    }

    @Test
    void insertingCustomerWithDuplicateEmail() {
        String email = customer.getEmail();
        customerService.insert(customer);

        Customer sameEmailCustomer = new Customer("Hans", "Gruber", email, "Hg122344");

        assertThrows(DuplicateEmailException.class,
                     () -> customerService.insert(sameEmailCustomer));
    }

    @Test
    void retrievingExistingCustomer() {
        long id = customerService.insert(customer).getId();

        Optional<Customer> optionalCustomer = customerService.get(id);
        Customer postInsertCustomer = optionalCustomer.orElseThrow();

        assertEquals(customer, postInsertCustomer);
    }

    @Test
    void tryingToRetrieveNonExistingCustomer() {
        Optional<Customer> optCustomer = customerService.get(1000L);
        assertTrue(optCustomer.isEmpty());
    }

    @Test
    void deletingCustomer() {
        long id = customerService.insert(customer).getId();

        customerService.delete(id);
        assertFalse(customerService.get(id).isPresent());
    }

    @Test
    void updatingEmail() {
        String oldEmail = customer.getEmail();
        long id = customerService.insert(customer).getId();

        String newEmail = "existingHarmoniously@aol.com";
        customerService.updateEmail(id, newEmail);

        String postUpdateEmail = customerService.get(id)
                                                .map(Customer::getEmail)
                                                .orElseThrow();

        assertEquals(newEmail, postUpdateEmail);
        assertNotEquals(oldEmail, postUpdateEmail);
    }

    @Test
    void tryingToUpdateDuplicateEmail() {
        String oldEmail = customer.getEmail();
        long id = customerService.insert(customer).getId();

        assertThrows(DuplicateEmailException.class,
                     () -> customerService.updateEmail(id, oldEmail));
    }

    @Test
    void updatingPassword() {
        String oldPassword = customer.getPassword();
        long id = customerService.insert(customer).getId();

        String newPassword = "ChristmasInJapan55";
        customerService.updatePassword(id, newPassword);

        String postUpdatePassword = customerService.get(id)
                                                   .map(Customer::getPassword)
                                                   .orElseThrow();

        assertEquals(newPassword, postUpdatePassword);
        assertNotEquals(oldPassword, postUpdatePassword);
    }

    @Test
    void tryingToUpdateInvalidPassword() {
        long id = customerService.insert(customer).getId();

        String invalidPassword = TestUtils.generateInvalidPassword();
        assertThrows(InvalidPasswordException.class,
                     () -> customerService.updatePassword(id, invalidPassword));
    }

    @Test
    void purchasingAValidCoupon() {
        Company company = TestUtils.generateCompany();
        CompanyService companyService = TestUtils.generateCompanyService();

        long companyId = companyService.insert(company).getId();

        Coupon preInsertCoupon = TestUtils.generateCoupon(companyId);
        companyService.create(preInsertCoupon);

        Coupon postInsertCoupon = companyService.get(companyId)
                                                .map(Company::getCoupons)
                                                .orElseThrow()
                                                .stream()
                                                .findFirst()
                                                .orElseThrow();

        long customerId = customerService.insert(customer).getId();
        customerService.purchase(customerId, postInsertCoupon.getId());

        Customer postInsertCustomer = customerService.get(customerId).orElseThrow();

        assertTrue(postInsertCustomer.getCoupons().contains(postInsertCoupon));
    }

    @Test
    void purchasingCouponAlsoDecrementsItsAmountByOne() {
        Company company = TestUtils.generateCompany();
        CompanyService companyService = TestUtils.generateCompanyService();

        long companyId = companyService.insert(company).getId();

        int prePurchaseAmount = 50;
        Coupon preInsertCoupon = TestUtils.generateCoupon(companyId, prePurchaseAmount);
        companyService.create(preInsertCoupon);

        long couponId = getCouponId(companyService, companyId);

        long customerId = customerService.insert(customer).getId();
        customerService.purchase(customerId, couponId);
        CouponDao couponDAO = TestUtils.createCouponDAO();

        int postPurchaseAmount = couponDAO.findById(couponId)
                                          .map(Coupon::getAmount)
                                          .orElseThrow();

        assertEquals(prePurchaseAmount - 1, postPurchaseAmount);
    }

    @Test
    void tryingToPurchaseCouponWithAmountEqualToZero() {
        Company company = TestUtils.generateCompany();
        CompanyService companyService = TestUtils.generateCompanyService();

        long companyId = companyService.insert(company).getId();

        Coupon coupon = TestUtils.generateCoupon(companyId, 0);
        companyService.create(coupon);

        long couponId = getCouponId(companyService, companyId);

        long customerId = customerService.insert(customer).getId();

        assertThrows(InvalidCouponPurchaseException.class,
                     () -> customerService.purchase(customerId, couponId));
    }

    private void initCustomerService() {
        customerService = TestUtils.generateCustomerService();
    }

    private void initCustomer() {
        customer = TestUtils.generateCustomer();
    }

    private long getCouponId(CompanyService companyService, long companyId) {
        return companyService.get(companyId)
                             .map(Company::getCoupons)
                             .orElseThrow()
                             .stream()
                             .findFirst()
                             .map(Coupon::getId)
                             .orElseThrow();
    }
}
