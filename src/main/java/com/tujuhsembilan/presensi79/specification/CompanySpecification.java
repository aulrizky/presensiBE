package com.tujuhsembilan.presensi79.specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.tujuhsembilan.presensi79.dto.response.CompanyFilterRequest;
import com.tujuhsembilan.presensi79.model.Company;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompanySpecification {

    public static void applyCompanyFilter(List<Predicate> predicates, From<?, Company> root,
            CriteriaBuilder criteriaBuilder, CompanyFilterRequest companyFilterRequest) {

        if (companyFilterRequest.getCompanyName() != null) {
            String companyName = "%" + companyFilterRequest.getCompanyName().toLowerCase() + "%";
            Predicate companyNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("companyName")),
                    companyName);
            predicates.add(companyNamePredicate);
        }

        if (companyFilterRequest.getStartDateJoined() != null) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateJoined = LocalDate.parse(companyFilterRequest.getStartDateJoined(), dtf);

            Predicate startedDateJoinedPredicate = criteriaBuilder.greaterThanOrEqualTo(
                    root.get("createdDate").as(LocalDate.class),
                    startDateJoined);

            predicates.add(startedDateJoinedPredicate);
        }
        if (companyFilterRequest.getEndDateJoined() != null) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate endDateJoined = LocalDate.parse(companyFilterRequest.getEndDateJoined(), dtf);

            Predicate startedDateJoinedPredicate = criteriaBuilder.lessThanOrEqualTo(
                    root.get("createdDate").as(LocalDate.class),
                    endDateJoined);

            predicates.add(startedDateJoinedPredicate);
        }
    }

    public static Specification<Company> companyFilterAll(CompanyFilterRequest companyFilterRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            applyCompanyFilter(predicates, root, criteriaBuilder, companyFilterRequest);

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}
