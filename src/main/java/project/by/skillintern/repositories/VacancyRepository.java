package project.by.skillintern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.by.skillintern.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {
    @Query("SELECT v FROM Vacancy v WHERE v.level = 'Intern'")
    List<Vacancy> findAllWhereLevelIsIntern();

    @Query(value = "SELECT DISTINCT u.company_name " +
            "FROM vacancies v " +
            "JOIN users u ON v.employer_id = u.id " +
            "WHERE u.role = 'ROLE_EMPLOYER'", nativeQuery = true)
    List<String> findAllCompanyNamesOfEmployers();

    @Query("SELECT v FROM Vacancy v WHERE " +
            "LOWER(v.title) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(v.location) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(v.description) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Vacancy> searchVacancies(@Param("searchText") String searchText);
}
