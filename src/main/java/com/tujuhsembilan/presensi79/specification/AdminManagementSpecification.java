package com.tujuhsembilan.presensi79.specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.tujuhsembilan.presensi79.dto.request.AdminManagementRequest;
import com.tujuhsembilan.presensi79.model.Admin;
import com.tujuhsembilan.presensi79.model.Company;

public class AdminManagementSpecification {
    public static Specification<Admin> administratorFilter(AdminManagementRequest request) {
        return (Root<Admin> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join to access Company attributes
            Join<Admin, Company> companyJoin = root.join("company");

            // Filter by company name
            if (request.getCompanyName() != null && !request.getCompanyName().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(companyJoin.get("companyName")),
                    "%" + request.getCompanyName().toLowerCase() + "%"));
            }

            // Filter by combined admin name
            if (request.getAdminName() != null && !request.getAdminName().isEmpty()) {
                String[] nameParts = request.getAdminName().split(" ");
                if (nameParts.length == 2) {
                    String firstName = nameParts[0];
                    String lastName = nameParts[1];
                    Predicate firstNamePredicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("firstName")),
                        firstName.toLowerCase());
                    Predicate lastNamePredicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("lastName")),
                        lastName.toLowerCase());
                    predicates.add(criteriaBuilder.and(firstNamePredicate, lastNamePredicate));
                }
            }

            // Filter by start date
            if (request.getStartDateJoin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), request.getStartDateJoin()));
            }

            // Filter by isDeleted = false
            predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

            // Return the combined predicate
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
