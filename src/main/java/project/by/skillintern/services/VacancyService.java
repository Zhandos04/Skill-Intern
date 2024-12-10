package project.by.skillintern.services;

import project.by.skillintern.dto.requests.VacancyDTO;
import project.by.skillintern.dto.responses.VacancyResponseDTO;
import project.by.skillintern.entities.User;
import project.by.skillintern.entities.Vacancy;

import java.util.List;

public interface VacancyService {
    void createVacancy(VacancyDTO vacancyDTO);
    List<VacancyResponseDTO> getAllVacancies();
    List<VacancyResponseDTO> getVacanciesByEmployer(User employer);
    List<VacancyResponseDTO> getAllInternships();
    VacancyDTO getVacancyDetail(Long id);
    void updateVacancy(Long id, VacancyDTO vacancyDTO);
    void deleteVacancy(Long id);

    List<VacancyResponseDTO> getVacanciesByFilter(String[] levels, String[] companies, String[] technologies, String employmentType, Boolean remoteWork);

    List<String> getAllCompanyNames();
    List<VacancyResponseDTO> searchVacancies(String searchText);
}
