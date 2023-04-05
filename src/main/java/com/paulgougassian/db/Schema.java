package com.paulgougassian.db;

public final class Schema {
    // Table names
    private static final String TABLE_NAME_COMPANY = "company";
    private static final String TABLE_NAME_CUSTOMER = "customer";
    private static final String TABLE_NAME_COUPON = "coupon";
    private static final String TABLE_NAME_CUSTOMER_COUPON = "customer_coupon";

    // Column names
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_COMPANY_ID = "company_id";
    public static final String COL_CATEGORY = "category";
    public static final String COL_TITLE = "title";
    public static final String COL_START_DATE = "start_date";
    public static final String COL_END_DATE = "end_date";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_DESC = "description";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE_URL = "image_url";
    private static final String COL_COUPON_ID = "coupon_id";
    private static final String COL_CUSTOMER_ID = "customer_id";

    // Customer DML
    public static final String SELECT_ALL_CUSTOMERS = "select * from "
                                                      + TABLE_NAME_CUSTOMER;

    public static final String SELECT_CUSTOMER_BY_ID = SELECT_ALL_CUSTOMERS
                                                       + " where "
                                                       + COL_ID
                                                       + " = ?";

    public static final String SELECT_CUSTOMER_BY_EMAIL = SELECT_ALL_CUSTOMERS
                                                          + " where "
                                                          + COL_EMAIL
                                                          + " = ?";

    public static final String SELECT_CUSTOMER_BY_EMAIL_AND_PASS = SELECT_ALL_CUSTOMERS
                                                                   + " where "
                                                                   + COL_EMAIL
                                                                   + " = ?"
                                                                   + " AND "
                                                                   + COL_PASSWORD
                                                                   + " = ?";


    public static final String UPDATE_CUSTOMER_EMAIL = "update "
                                                       + TABLE_NAME_CUSTOMER
                                                       + " set "
                                                       + COL_EMAIL
                                                       + " = ?"
                                                       + " where "
                                                       + COL_ID
                                                       + " = ?";

    public static final String UPDATE_CUSTOMER_PASSWORD = "update "
                                                          + TABLE_NAME_CUSTOMER
                                                          + " set "
                                                          + COL_PASSWORD
                                                          + " = ?"
                                                          + " where "
                                                          + COL_ID
                                                          + " = ?";

    public static final String DELETE_FROM_CUSTOMER = "delete from "
                                                      + TABLE_NAME_CUSTOMER
                                                      + " where "
                                                      + COL_ID
                                                      + " = ?";

    public static final String INSERT_INTO_CUSTOMER = "insert into "
                                                      + TABLE_NAME_CUSTOMER
                                                      + "("
                                                      + COL_FIRST_NAME
                                                      + ", "
                                                      + COL_LAST_NAME
                                                      + ", "
                                                      + COL_EMAIL
                                                      + ", "
                                                      + COL_PASSWORD
                                                      + ") "
                                                      + "values (?, ?, ?, ?)";
    // Company DML
    public static final String SELECT_ALL_COMPANIES = "select * from "
                                                      + TABLE_NAME_COMPANY;

    public static final String SELECT_COMPANY_BY_ID = SELECT_ALL_COMPANIES
                                                      + " where "
                                                      + COL_ID
                                                      + " = ?";

    public static final String SELECT_COMPANY_BY_EMAIL = SELECT_ALL_COMPANIES
                                                         + " where "
                                                         + COL_EMAIL
                                                         + " = ?";

    public static final String SELECT_COMPANY_BY_EMAIL_AND_PASS = SELECT_ALL_COMPANIES
                                                                  + " where "
                                                                  + COL_EMAIL
                                                                  + " = ?"
                                                                  + " AND "
                                                                  + COL_PASSWORD
                                                                  + " = ?";

    public static final String INSERT_INTO_COMPANY = "insert into "
                                                     + TABLE_NAME_COMPANY
                                                     + " ("
                                                     + COL_NAME
                                                     + ", "
                                                     + COL_EMAIL
                                                     + ", "
                                                     + COL_PASSWORD
                                                     + ")"
                                                     + " values (?, ?, ?)";

    public static final String UPDATE_COMPANY_EMAIL = "update "
                                                      + TABLE_NAME_COMPANY
                                                      + " set "
                                                      + COL_EMAIL
                                                      + " = ?"
                                                      + " where "
                                                      + COL_ID
                                                      + " = ?";

    public static final String UPDATE_COMPANY_PASSWORD = "update "
                                                         + TABLE_NAME_COMPANY
                                                         + " set "
                                                         + COL_PASSWORD
                                                         + " = ?"
                                                         + " where "
                                                         + COL_ID
                                                         + " = ?";

    public static final String DELETE_FROM_COMPANY = "delete from "
                                                     + TABLE_NAME_COMPANY
                                                     + " where "
                                                     + COL_ID
                                                     + " = ?";

    // Coupon DML
    public static final String SELECT_ALL_COUPONS = "select * from " + TABLE_NAME_COUPON;

    public static final String SELECT_COUPON_BY_ID = SELECT_ALL_COUPONS
                                                     + " where "
                                                     + COL_ID
                                                     + " = ?";

    public static final String SELECT_COUPONS_BEFORE_END_DATE = SELECT_ALL_COUPONS
                                                                + " where "
                                                                + COL_END_DATE
                                                                + " < ?";

    public static final String SELECT_ALL_CUSTOMER_COUPONS = SELECT_ALL_COUPONS
                                                             + " inner join "
                                                             + TABLE_NAME_CUSTOMER_COUPON
                                                             + " on "
                                                             + TABLE_NAME_COUPON
                                                             + "."
                                                             + COL_ID
                                                             + " = "
                                                             + TABLE_NAME_CUSTOMER_COUPON
                                                             + "."
                                                             + COL_COUPON_ID
                                                             + " where "
                                                             + TABLE_NAME_CUSTOMER_COUPON
                                                             + "."
                                                             + COL_CUSTOMER_ID
                                                             + " = ?";

    public static final String SELECT_ALL_COMPANY_COUPONS = SELECT_ALL_COUPONS
                                                            + " where "
                                                            + COL_COMPANY_ID
                                                            + " = ?";

    public static final String SELECT_COUPON_BY_TITLE_AND_COMPANY_ID = SELECT_ALL_COUPONS
                                                                       + " where "
                                                                       + COL_TITLE
                                                                       + " = ?"
                                                                       + " and "
                                                                       + COL_COMPANY_ID
                                                                       + " = ?";

    public static final String SELECT_COUPON_AMOUNT_BY_ID_FOR_UPDATE = "select "
                                                                       + COL_AMOUNT
                                                                       + " from "
                                                                       + TABLE_NAME_COUPON
                                                                       + " where "
                                                                       + COL_ID
                                                                       + " = ?"
                                                                       + " for update";

    public static final String DELETE_ALL_COMPANY_COUPONS = "delete from "
                                                            + TABLE_NAME_COUPON
                                                            + " where "
                                                            + COL_COMPANY_ID
                                                            + " = ?";

    public static final String DECREMENT_COUPON_AMOUNT = "update "
                                                         + TABLE_NAME_COUPON
                                                         + " set "
                                                         + COL_AMOUNT
                                                         + " = "
                                                         + COL_AMOUNT
                                                         + " - ?"
                                                         + " where "
                                                         + COL_ID
                                                         + " = ?";

    public static final String INSERT_INTO_COUPON = "insert into "
                                                    + TABLE_NAME_COUPON
                                                    + "("
                                                    + COL_COMPANY_ID
                                                    + ","
                                                    + COL_CATEGORY
                                                    + ","
                                                    + COL_TITLE
                                                    + ","
                                                    + COL_START_DATE
                                                    + ","
                                                    + COL_END_DATE
                                                    + ","
                                                    + COL_AMOUNT
                                                    + ","
                                                    + COL_DESC
                                                    + ","
                                                    + COL_PRICE
                                                    + ","
                                                    + COL_IMAGE_URL
                                                    + ") "
                                                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // Customer_Coupon DML
    public static final String INSERT_INTO_CUSTOMER_COUPON = "insert into "
                                                             + TABLE_NAME_CUSTOMER_COUPON
                                                             + "("
                                                             + COL_CUSTOMER_ID
                                                             + ", "
                                                             + COL_COUPON_ID
                                                             + ")"
                                                             + " values(?, ?)";

    public static final String DELETE_CUSTOMER_COUPON = "delete from "
                                                        + TABLE_NAME_CUSTOMER_COUPON
                                                        + " where "
                                                        + COL_COUPON_ID
                                                        + " = ?";

    private Schema() {
        throw new AssertionError("Enforce non instantiability");
    }
}
