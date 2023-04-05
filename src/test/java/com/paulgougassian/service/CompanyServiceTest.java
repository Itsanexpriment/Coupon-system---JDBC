package com.paulgougassian.service;

import com.paulgougassian.TestUtils;
import com.paulgougassian.db.dao.CouponDao;
import com.paulgougassian.db.model.Company;
import com.paulgougassian.db.model.Coupon;
import com.paulgougassian.db.model.Customer;
import com.paulgougassian.service.exceptions.DuplicateCouponException;
import com.paulgougassian.service.exceptions.DuplicateEmailException;
import com.paulgougassian.service.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/*
    WARNING: before each test, all records are cleared from the database!
 */

public class CompanyServiceTest {
    private CompanyService companyService;
    private Company company;

    @BeforeEach
    void initializeFields() {
        initCompanyService();
        initCompany();

        TestUtils.clearDB(); // deletes all records from the DB!
    }

    @Test
    void insertingNewCompany() {
        long preInsertId = company.getId();

        Company postInsertCompany = companyService.insert(company);
        long postInsertId = postInsertCompany.getId();

        assertNotEquals(preInsertId, postInsertId);
    }

    @Test
    void insertingCompanyWithDuplicateEmail() {
        String email = company.getEmail();
        companyService.insert(company);

        Company sameEmailCompany = new Company("Tara", email, "Tara12344");

        assertThrows(DuplicateEmailException.class,
                     () -> companyService.insert(sameEmailCompany));
    }

    @Test
    void retrievingExistingCompany() {
        long id = companyService.insert(company).getId();

        Optional<Company> optCompany = companyService.get(id);
        Company postInsertCompany = optCompany.orElseThrow();

        assertEquals(company, postInsertCompany);
    }

    @Test
    void retrievingExistingCompanyReturnsItsCoupons() {
        long id = companyService.insert(company).getId();
        Set<Coupon> coupons = TestUtils.generateCoupons(id, 3);

        coupons.forEach(c -> companyService.create(c));

        Optional<Company> optCompany = companyService.get(id);
        Company postInsertCompany = optCompany.orElseThrow();

        assertEquals(coupons, postInsertCompany.getCoupons());
    }

    @Test
    void tryingToRetrieveNonExistingCompany() {
        Optional<Company> optCompany = companyService.get(1000L);
        assertTrue(optCompany.isEmpty());
    }

    @Test
    void deletingCompanyWithoutCoupons() {
        long id = companyService.insert(company).getId();

        companyService.delete(id);
        assertFalse(companyService.get(id).isPresent());
    }

    @Test
    void deletingCompanyAlsoDeletesAllItsCoupons() {
        long companyId = companyService.insert(company).getId();

        Set<Coupon> coupons = TestUtils.generateCoupons(companyId, 3);
        coupons.forEach(coupon -> companyService.create(coupon));

        companyService.delete(companyId);

        CouponDao couponDAO = TestUtils.createCouponDAO();
        assertTrue(couponDAO.findAllCompanyCoupons(companyId).isEmpty());
    }

    @Test
    void deletingCompanyAndItsCouponsAlsoRemovesThemFromCustomersCoupons() {
        long companyId = companyService.insert(company).getId();

        Set<Coupon> coupons = TestUtils.generateCoupons(companyId, 3);
        coupons.forEach(coupon -> companyService.create(coupon));

        CustomerService customerService = TestUtils.generateCustomerService();
        Customer customer = TestUtils.generateCustomer();
        CouponDao couponDAO = TestUtils.createCouponDAO();

        long customerId = customerService.insert(customer).getId();

        couponDAO.findAllCustomerCoupons(customerId)
                 .stream()
                 .map(Coupon::getId)
                 .forEach(id -> customerService.purchase(customerId, id));

        companyService.delete(companyId);

        assertTrue(couponDAO.findAllCustomerCoupons(customerId).isEmpty());
    }

    @Test
    void updatingEmail() {
        String oldEmail = company.getEmail();
        long id = companyService.insert(company).getId();

        String newEmail = "betterthancorp@gmail.com";
        companyService.updateEmail(id, newEmail);

        String postUpdateEmail = companyService.get(id)
                                               .map(Company::getEmail)
                                               .orElseThrow();

        assertEquals(newEmail, postUpdateEmail);
        assertNotEquals(oldEmail, postUpdateEmail);
    }

    @Test
    void tryingToUpdateDuplicateEmail() {
        String oldEmail = company.getEmail();
        long id = companyService.insert(company).getId();

        assertThrows(DuplicateEmailException.class,
                     () -> companyService.updateEmail(id, oldEmail));
    }

    @Test
    void updatingPassword() {
        String oldPassword = company.getPassword();
        long id = companyService.insert(company).getId();

        String newPassword = "underMyUmbrella8";
        companyService.updatePassword(id, newPassword);

        String postUpdatePassword = companyService.get(id)
                                                  .map(Company::getPassword)
                                                  .orElseThrow();

        assertEquals(newPassword, postUpdatePassword);
        assertNotEquals(oldPassword, postUpdatePassword);
    }

    @Test
    void tryingToUpdateInvalidPassword() {
        long id = companyService.insert(company).getId();

        String invalidPassword = TestUtils.generateInvalidPassword();
        assertThrows(InvalidPasswordException.class,
                     () -> companyService.updatePassword(id, invalidPassword));
    }

    @Test
    void creatingACoupon() {
        long companyId = companyService.insert(company).getId();

        Coupon coupon = TestUtils.generateCoupon(companyId);
        companyService.create(coupon);

        Set<Coupon> coupons = companyService.get(companyId).orElseThrow().getCoupons();
        assertTrue(coupons.contains(coupon));
    }

    @Test
    void tryingToCreateADuplicateCoupon() {
        long companyId = companyService.insert(company).getId();

        Coupon coupon = TestUtils.generateCoupon(companyId);
        companyService.create(coupon);

        assertThrows(DuplicateCouponException.class, () -> companyService.create(coupon));
    }

    private void initCompanyService() {
        companyService = TestUtils.generateCompanyService();
    }

    private void initCompany() {
        company = TestUtils.generateCompany();
    }
}