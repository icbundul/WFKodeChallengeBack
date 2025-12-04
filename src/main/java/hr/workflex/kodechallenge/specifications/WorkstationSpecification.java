package hr.workflex.kodechallenge.specifications;

import hr.workflex.kodechallenge.domain.Workstation;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WorkstationSpecification {

    public static Specification<Workstation> filterBy(String sortColumn, String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (sortColumn != null && !sortColumn.isEmpty()) {
                Path<?> sortField = root.get(sortColumn);
                query.orderBy("ASC".equalsIgnoreCase(sortDirection) ? criteriaBuilder.asc(sortField) : criteriaBuilder.desc(sortField));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
