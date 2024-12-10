package project.by.skillintern.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.by.skillintern.dto.requests.VacancyDTO;
import project.by.skillintern.dto.responses.VacancyResponseDTO;
import project.by.skillintern.entities.User;
import project.by.skillintern.entities.Vacancy;
import project.by.skillintern.exceptions.VacancyNotFoundException;
import project.by.skillintern.repositories.VacancyRepository;
import project.by.skillintern.repositories.VacancySpecification;
import project.by.skillintern.services.UserService;
import project.by.skillintern.services.VacancyService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public void createVacancy(VacancyDTO vacancyDTO) {
        User currentUser = userService.getUserByUsername(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Vacancy vacancy = convertToVacancy(vacancyDTO);
        vacancy.setEmployer(currentUser);
        vacancyRepository.save(vacancy);
    }

    @Transactional
    @Override
    public List<VacancyResponseDTO> getAllVacancies() {
        return vacancyRepository.findAll().stream().map(this::convertToVacancyResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<VacancyResponseDTO> getVacanciesByEmployer(User employer) {
        return vacancyRepository.findAll().stream()
                .filter(job -> job.getEmployer().equals(employer))
                .toList().stream().map(this::convertToVacancyResponseDTO).collect(Collectors.toList());
    }
//    @Transactional
//    @Override
//    public List<VacancyResponseDTO> getVacanciesByFilter(FilterVacancyDTO filterVacancyDTO) {
//        Specification<Vacancy> specification = VacancySpecification.filterVacancies(filterVacancyDTO);
//        return vacancyRepository.findAll(specification).stream().map(this::convertToVacancyResponseDTO).collect(Collectors.toList());
//    }

    @Override
    @Transactional
    public List<VacancyResponseDTO> getVacanciesByFilter(String[] levels, String[] companies, String[] technologies, String employmentType, Boolean remoteWork) {
        Specification<Vacancy> specification = VacancySpecification.filterVacancies(levels, companies, technologies, employmentType, remoteWork);
        return vacancyRepository.findAll(specification).stream().map(this::convertToVacancyResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCompanyNames() {
        return vacancyRepository.findAllCompanyNamesOfEmployers();
    }

    @Override
    public List<VacancyResponseDTO> searchVacancies(String searchText) {
        return vacancyRepository.searchVacancies(searchText).stream().map(this::convertToVacancyResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<VacancyResponseDTO> getAllInternships() {
        return vacancyRepository.findAllWhereLevelIsIntern().stream().map(this::convertToVacancyResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VacancyDTO getVacancyDetail(Long id) {
        Optional<Vacancy> vacancyOptional = vacancyRepository.findById(id);
        if(vacancyOptional.isEmpty()) {
            throw new UsernameNotFoundException("Vacancy not found!");
        }
        VacancyDTO vacancyDTO = convertToVacancyDTO(vacancyOptional.get());
        vacancyDTO.setCompanyName(vacancyOptional.get().getEmployer().getCompanyName());
        return vacancyDTO;
    }

    @Override
    @Transactional
    public void updateVacancy(Long id, VacancyDTO vacancyDTO) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new VacancyNotFoundException("Vacancy with ID " + id + " not found"));

        modelMapper.map(vacancyDTO, vacancy);
        vacancyRepository.save(vacancy);
    }

    @Override
    @Transactional
    public void deleteVacancy(Long id) {
        vacancyRepository.deleteById(id);
    }

    private VacancyResponseDTO convertToVacancyResponseDTO(Vacancy vacancy) {
        VacancyResponseDTO vacancyResponseDTO = new VacancyResponseDTO();
        vacancyResponseDTO.setCompanyName(vacancy.getEmployer().getCompanyName());
        vacancyResponseDTO.setLocation(vacancy.getLocation());
        vacancyResponseDTO.setExperience(vacancy.getExperience());
        vacancyResponseDTO.setTitle(vacancy.getTitle());
        vacancyResponseDTO.setSalaryStart(vacancy.getSalaryStart());
        vacancyResponseDTO.setSalaryEnd(vacancy.getSalaryEnd());
        vacancyResponseDTO.setId(vacancy.getId());
        return vacancyResponseDTO;
    }

    private VacancyDTO convertToVacancyDTO(Vacancy vacancy) {
        return modelMapper.map(vacancy, VacancyDTO.class);
    }
    private Vacancy convertToVacancy(VacancyDTO vacancyDTO) {
        return modelMapper.map(vacancyDTO, Vacancy.class);
    }
}
