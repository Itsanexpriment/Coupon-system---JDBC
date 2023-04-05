package com.paulgougassian.db.dao;

import com.paulgougassian.db.Schema;
import com.paulgougassian.db.dao.wrapper.PreparedStatementWrapper;
import com.paulgougassian.db.dao.wrapper.functions.PreparedStatementConsumer;
import com.paulgougassian.db.dao.wrapper.functions.PreparedStatementFunction;
import com.paulgougassian.db.model.Coupon;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CouponDaoImpl implements CouponDao {
    private final PreparedStatementWrapper psWrapper;

    public CouponDaoImpl(PreparedStatementWrapper psWrapper) {
        this.psWrapper = psWrapper;
    }

    @Override
    public Coupon insert(Coupon coupon) {
        PreparedStatementFunction<Coupon> function = ps -> {

            ps.setLong(1, coupon.getCompanyId());
            ps.setInt(2, coupon.getCategory());
            ps.setString(3, coupon.getTitle());
            ps.setDate(4, coupon.getStartDate());
            ps.setDate(5, coupon.getEndDate());
            ps.setInt(6, coupon.getAmount());
            ps.setString(7, coupon.getDescription());
            ps.setDouble(8, coupon.getPrice());
            ps.setString(9, coupon.getImageUrl());

            ps.executeUpdate();
            ResultSet key = ps.getGeneratedKeys();

            if (key.next()) {
                coupon.setId(key.getLong(1));
            }
            return coupon;
        };

        return psWrapper.executeQuery(Schema.INSERT_INTO_COUPON,
                                      Statement.RETURN_GENERATED_KEYS, function);
    }

    @Override
    public Optional<Coupon> findById(long id) {
        PreparedStatementFunction<Optional<Coupon>> function = ps -> {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(createCoupon(rs));
            }
            return Optional.empty();
        };

        return psWrapper.executeQuery(Schema.SELECT_COUPON_BY_ID, function);
    }

    @Override
    public Optional<Coupon> findByTitleAndCompanyId(String title, long companyId) {
        PreparedStatementFunction<Optional<Coupon>> function = ps -> {
            ps.setString(1, title);
            ps.setLong(2, companyId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(createCoupon(rs));
            }
            return Optional.empty();
        };

        return psWrapper.executeQuery(Schema.SELECT_COUPON_BY_TITLE_AND_COMPANY_ID,
                                      function);
    }

    @Override
    public int deleteAllCompanyCoupons(long companyId) {
        PreparedStatementFunction<Integer> function = ps -> {
            ps.setLong(1, companyId);

            return ps.executeUpdate();
        };

        return psWrapper.executeQuery(Schema.DELETE_ALL_COMPANY_COUPONS, function);
    }

    @Override
    public void detachCoupons(Collection<? extends Coupon> coupons) {
        PreparedStatementConsumer consumer = ps -> {

            for (Coupon coupon : coupons) {
                ps.setLong(1, coupon.getId());
                ps.addBatch();
            }

            ps.executeBatch();
        };
        psWrapper.executeUpdate(Schema.DELETE_CUSTOMER_COUPON, consumer);
    }

    @Override
    public List<Coupon> findAllCustomerCoupons(long customerId) {
        return psWrapper.executeQuery(Schema.SELECT_ALL_CUSTOMER_COUPONS, ps -> {
            ps.setLong(1, customerId);

            return getListOfMatchingCoupons(ps.executeQuery());
        });
    }

    @Override
    public List<Coupon> findAllCompanyCoupons(long companyId) {
        return psWrapper.executeQuery(Schema.SELECT_ALL_COMPANY_COUPONS, ps -> {
            ps.setLong(1, companyId);

            return getListOfMatchingCoupons(ps.executeQuery());
        });
    }

    @Override
    public List<Coupon> findAllExpired() {
        return findAllBefore(Date.valueOf(LocalDate.now()));
    }

    @Override
    public List<Coupon> findAllBefore(Date date) {
        return psWrapper.executeQuery(Schema.SELECT_COUPONS_BEFORE_END_DATE, ps -> {
            ps.setDate(1, date);

            return getListOfMatchingCoupons(ps.executeQuery());
        });
    }

    @Override
    public List<Coupon> findAll() {
        return psWrapper.executeQuery(Schema.SELECT_ALL_COUPONS,
                                      ps -> getListOfMatchingCoupons(ps.executeQuery()));
    }

    private List<Coupon> getListOfMatchingCoupons(ResultSet rs) throws SQLException {
        List<Coupon> coupons = new ArrayList<>();

        while (rs.next()) {
            coupons.add(createCoupon(rs));
        }
        return coupons;
    }

    private Coupon createCoupon(ResultSet rs) throws SQLException {
        return new Coupon(rs.getLong(Schema.COL_ID), rs.getLong(Schema.COL_COMPANY_ID),
                          rs.getString(Schema.COL_TITLE), rs.getInt(Schema.COL_CATEGORY),
                          rs.getDate(Schema.COL_START_DATE),
                          rs.getDate(Schema.COL_END_DATE), rs.getInt(Schema.COL_AMOUNT),
                          rs.getString(Schema.COL_DESC), rs.getDouble(Schema.COL_PRICE),
                          rs.getString(Schema.COL_IMAGE_URL));
    }
}
