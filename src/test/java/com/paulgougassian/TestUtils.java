package com.paulgougassian;

import com.paulgougassian.common.ConnectionPool;
import com.paulgougassian.db.dao.CompanyDAOImpl;
import com.paulgougassian.db.dao.CouponDao;
import com.paulgougassian.db.dao.CouponDaoImpl;
import com.paulgougassian.db.dao.CustomerDaoImpl;
import com.paulgougassian.db.dao.wrapper.PreparedStatementWrapper;
import com.paulgougassian.db.dao.wrapper.PreparedStatementWrapperImpl;
import com.paulgougassian.db.model.Company;
import com.paulgougassian.db.model.Coupon;
import com.paulgougassian.db.model.Customer;
import com.paulgougassian.service.CompanyService;
import com.paulgougassian.service.CompanyServiceImpl;
import com.paulgougassian.service.CustomerService;
import com.paulgougassian.service.CustomerServiceImpl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {
    private static final PreparedStatementWrapper PS_WRAPPER = new PreparedStatementWrapperImpl(
            ConnectionPool.getInstance());

    public static void clearDB() {
        clearTable("customer_coupon");
        clearTable("coupon");
        clearTable("customer");
        clearTable("company");
    }

    public static CompanyService generateCompanyService() {
        return new CompanyServiceImpl(new CompanyDAOImpl(PS_WRAPPER),
                                      new CouponDaoImpl(PS_WRAPPER));
    }

    public static CustomerService generateCustomerService() {
        return new CustomerServiceImpl(new CustomerDaoImpl(PS_WRAPPER),
                                       new CouponDaoImpl(PS_WRAPPER));
    }

    public static Company generateCompany() {
        return new Company("Umbrella corp.", "ucorp@gmail.com", "Umbrella123");
    }

    public static Customer generateCustomer() {
        return new Customer("John", "McClane", "livepeacefully@gmail.com",
                            "YippeeKiYay1");
    }

    public static Coupon generateCoupon(long companyId) {
        return new Coupon(companyId, "Summer Sale", 5, Date.valueOf("2022-07-27"),
                          Date.valueOf("2023-10-01"), 60,
                          "30% Summer discount for club members", 35,
                          "http://umbrella.com/image.png");
    }

    public static Coupon generateCoupon(long companyId, int amount) {
        return new Coupon(companyId, "Summer Sale", 5, Date.valueOf("2022-07-27"),
                          Date.valueOf("2023-10-01"), amount,
                          "30% Summer discount for club members", 35,
                          "http://umbrella.com/image.png");
    }

    public static Set<Coupon> generateCoupons(long companyId, int limit) {
        return IntStream.rangeClosed(1, limit)
                        .mapToObj(i -> new Coupon(companyId, "generic title" + i, 5,
                                                  Date.valueOf("2022-07-27"),
                                                  Date.valueOf("2023-10-01"), 60 + i,
                                                  "30% Summer discount for club members",
                                                  35 + i,
                                                  "http://umbrella.com/image.png"))
                        .collect(Collectors.toSet());
    }

    public static CouponDao createCouponDAO() {
        return new CouponDaoImpl(PS_WRAPPER);
    }

    public static String generateInvalidPassword() {
        return "56454";
    }

    private static void clearTable(String tableName) {
        PS_WRAPPER.executeUpdate("delete from " + tableName,
                                 PreparedStatement::executeUpdate);
    }
}
