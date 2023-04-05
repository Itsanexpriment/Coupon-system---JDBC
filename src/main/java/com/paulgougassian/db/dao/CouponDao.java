package com.paulgougassian.db.dao;

import com.paulgougassian.db.model.Coupon;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CouponDao {
    Coupon insert(Coupon coupon);

    Optional<Coupon> findById(long id);

    Optional<Coupon> findByTitleAndCompanyId(String title, long companyId);

    int deleteAllCompanyCoupons(long companyId);

    void detachCoupons(Collection<? extends Coupon> coupons);

    List<Coupon> findAllCustomerCoupons(long customerId);

    List<Coupon> findAllCompanyCoupons(long companyId);

    List<Coupon> findAllExpired();

    List<Coupon> findAllBefore(Date date);

    List<Coupon> findAll();
}
