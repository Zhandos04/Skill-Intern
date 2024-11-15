package project.by.skillintern.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.by.skillintern.dto.requests.VacancyDTO;
import project.by.skillintern.entities.User;
import project.by.skillintern.entities.Vacancy;
import project.by.skillintern.repositories.VacancyRepository;
import project.by.skillintern.services.VacancyService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public void createVacancy(Vacancy vacancy) {
        vacancyRepository.save(vacancy);
    }
    @Override
    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }
    @Override
    public List<Vacancy> getVacanciesByEmployer(User employer) {
        return vacancyRepository.findAll().stream()
                .filter(job -> job.getEmployer().equals(employer))
                .toList();
    }
}
