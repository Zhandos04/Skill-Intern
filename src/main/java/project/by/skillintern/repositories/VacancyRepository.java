package project.by.skillintern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;;
import project.by.skillintern.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {
}
