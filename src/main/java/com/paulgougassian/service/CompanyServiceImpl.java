package com.paulgougassian.service;

import com.paulgougassian.db.dao.CompanyDAO;
import com.paulgougassian.db.dao.CouponDao;
import com.paulgougassian.db.model.Company;
import com.paulgougassian.db.model.Coupon;
import com.paulgougassian.service.exceptions.DuplicateCouponException;
import com.paulgougassian.service.exceptions.DuplicateEmailException;
import com.paulgougassian.service.exceptions.InvalidPasswordException;
import com.paulgougassian.service.util.ServiceUtils;

import java.util.List;
import java.util.Optional;

public class CompanyServiceImpl implements CompanyService {
    private final CompanyDAO companyDAO;
    private final CouponDao couponDao;

    public CompanyServiceImpl(CompanyDAO companyDAO, CouponDao couponDao) {
        this.companyDAO = companyDAO;
        this.couponDao = couponDao;
    }

    @Override
    public Optional<Company> get(long id) {
        Optional<Company> optCompany = companyDAO.findById(id);

        if (optCompany.isEmpty()) {
            return Optional.empty();
        }
        Company company = optCompany.get();

        List<Coupon> coupons = couponDao.findAllCompanyCoupons(id);
        company.addCoupons(coupons);

        return Optional.of(company);
    }

    @Override
    public Company insert(Company company) {
        String email = company.getEmail();

        if (isEmailDuplicate(email)) {
            throw new DuplicateEmailException(String.format(
                    "Invalid email address, %s already exists in the system", email));
        }

        return companyDAO.insert(company);
    }

    @Override
    public void delete(long id) {
        List<Coupon> coupons = couponDao.findAllCompanyCoupons(id);

        couponDao.detachCoupons(coupons);
        couponDao.deleteAllCompanyCoupons(id);

        companyDAO.delete(id);
    }

    @Override
    public void updateEmail(long id, String email) {
        if (isEmailDuplicate(email)) {
            throw new DuplicateEmailException(String.format(
                    "Invalid email address, %s already exists in the system", email));
        }

        companyDAO.updateEmail(id, email);
    }

    @Override
    public void updatePassword(long id, String password) {
        if (!ServiceUtils.isPasswordValid(password)) {
            throw new InvalidPasswordException("The provided password is invalid");
        }

        companyDAO.updatePassword(id, password);
    }

    @Override
    public void create(Coupon coupon) {
        if (isCouponDuplicate(coupon)) {
            throw new DuplicateCouponException(String.format(
                    "Unable to create coupon -\n%s already exists in the system.",
                    coupon));
        }

        couponDao.insert(coupon);
    }

    private boolean isCouponDuplicate(Coupon coupon) {
        return couponDao.findByTitleAndCompanyId(coupon.getTitle(), coupon.getCompanyId())
                        .isPresent();
    }

    private boolean isEmailDuplicate(String email) {
        return companyDAO.findByEmail(email).isPresent();
    }
}
