package com.duongw.stayeasy.configuration;

public interface AppConstant {
    String API_PREFIX = "/api/v1";
    String API_PREFIX_USER = "/api/v1/users";
    String API_PREFIX_CUSTOMER = "/api/v1/customers";
    String API_PREFIX_ROOM = "/api/v1/rooms";
    String API_PREFIX_BOOKING = "/api/v1/bookings";
    String API_PREFIX_IMAGE = "/api/v1/images";
    String API_PREFIX_CATEGORY = "/api/v1/categories";
    String API_PREFIX_ORDER = "/api/v1/orders";
    String SEARCH_OPERATOR = "(\\w+?)(:|<|>)(.*)";
    String SEARCH_SPEC_OPERATOR = "(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)";
    String SORT_BY = "(\\w+?)(:)(.*)";



}
