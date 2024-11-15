package project.by.skillintern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.by.skillintern.entities.Vacancy;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

}
