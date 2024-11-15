package project.by.skillintern.services;

import project.by.skillintern.dto.requests.FilterVacancyDTO;
import project.by.skillintern.entities.User;
import project.by.skillintern.entities.Vacancy;

import java.util.List;

public interface VacancyService {
    void createVacancy(Vacancy vacancy);
    List<Vacancy> getAllVacancies();
    List<Vacancy> getVacanciesByEmployer(User employer);
    List<Vacancy> getVacanciesByFilter(FilterVacancyDTO filterVacancyDTO);
}
