package project.by.skillintern.repositories;
import org.springframework.data.jpa.domain.Specification;
import project.by.skillintern.dto.requests.FilterVacancyDTO;
import project.by.skillintern.entities.Vacancy;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class VacancySpecification {
    public static Specification<Vacancy> filterVacancies(FilterVacancyDTO filterDTO) {
        return (Root<Vacancy> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Фильтр по уровню
            if (filterDTO.getLevels() != null && filterDTO.getLevels().length > 0) {
                predicates.add(root.get("level").in((Object[]) filterDTO.getLevels()));
            }

            // Фильтр по компаниям
            if (filterDTO.getCompanies() != null && filterDTO.getCompanies().length > 0) {
                predicates.add(root.get("employer").get("username").in((Object[]) filterDTO.getCompanies()));
            }

            // Фильтр по типу занятости
            if (filterDTO.getEmploymentType() != null) {
                predicates.add(cb.equal(root.get("operatingMode"), filterDTO.getEmploymentType()));
            }

            // Фильтр по удалённой работе
            if (filterDTO.getRemoteWork() != null) {
                predicates.add(cb.equal(root.get("remoteWork"), filterDTO.getRemoteWork()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

