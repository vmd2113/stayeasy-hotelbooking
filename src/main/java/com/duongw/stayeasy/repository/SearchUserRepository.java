package com.duongw.stayeasy.repository;

import com.duongw.stayeasy.dto.response.PageResponse;
import com.duongw.stayeasy.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duongw.stayeasy.configuration.AppConstant.SORT_BY;

@Component
@Slf4j

public class SearchUserRepository {

    private static final String LIKE_FORMAT = "%%%s%%";


    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> searchUser(int pageNo, int pageSize, String searchKey, String sortBy) {
        log.info("Request get all of User");

        // write true query to get list of users by search (one column)

        StringBuilder sqlQuery = new StringBuilder("SELECT u FROM User u WHERE   1=1 ");
        if (StringUtils.hasLength(searchKey)) {
            sqlQuery.append("and lower(u.username) like lower(:username)");
            sqlQuery.append(" OR lower(u.email) like lower(:email)");

        }

        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                sqlQuery.append(String.format(" ORDER BY u.%s %s", matcher.group(1), matcher.group(3)));
            }
        }

        // query list user
        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        if (StringUtils.hasLength(searchKey)) {
            selectQuery.setParameter("username", String.format(LIKE_FORMAT, searchKey));
            selectQuery.setParameter("email", String.format(LIKE_FORMAT, searchKey));

        }

        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults(pageSize);
        List<Customer> customers = selectQuery.getResultList();

        // query to count customer
        StringBuilder countQuery = new StringBuilder("SELECT COUNT(*) FROM User u where 1=1");
        if (StringUtils.hasLength(searchKey)) {
            countQuery.append("and lower(u.username) like lower(?1)");
            countQuery.append("or lower(u.email) like lower(?2)");


        }
        Query countQuerySQl = entityManager.createQuery(countQuery.toString());
        System.out.println(countQuerySQl);
        if (StringUtils.hasLength(searchKey)) {
            countQuerySQl.setParameter(1, String.format(LIKE_FORMAT, searchKey));
            countQuerySQl.setParameter(2, String.format(LIKE_FORMAT, searchKey));
        }
        Long totalElement = (Long) countQuerySQl.getSingleResult();
        log.info("Count: " + "{}", totalElement);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<?> page = new PageImpl<>(customers, pageable, totalElement);


        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(page.getTotalPages())
                .items(customers)
                .build();

    }

}
