package project.by.skillintern.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.by.skillintern.dto.responses.MyResponsesDTO;
import project.by.skillintern.dto.responses.ResponseDTO;
import project.by.skillintern.entities.Response;
import project.by.skillintern.entities.ResponseStatus;
import project.by.skillintern.entities.User;
import project.by.skillintern.entities.Vacancy;
import project.by.skillintern.exceptions.VacancyNotFoundException;
import project.by.skillintern.repositories.ResponseRepository;
import project.by.skillintern.repositories.VacancyRepository;
import project.by.skillintern.services.EmailService;
import project.by.skillintern.services.ResponseService;
import project.by.skillintern.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseServiceImpl implements ResponseService {
    private final VacancyRepository vacancyRepository;
    private final UserService userService;
    private final ResponseRepository responseRepository;
    private final EmailService emailService;
    @Override
    @Transactional
    public void createResponse(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new VacancyNotFoundException("Vacancy not found"));

        User currentUser = userService.getUserByUsername(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Response response = new Response();
        response.setUser(currentUser);
        response.setVacancy(vacancy);
        response.setStatus(ResponseStatus.PENDING);

        emailService.sendEmail(
                vacancy.getEmployer().getEmail(),
                "New Response to Your Vacancy",
                String.format("User %s has responded to your vacancy: %s", currentUser.getUsername(), vacancy.getTitle())
        );

        responseRepository.save(response);
    }
    @Override
    @Transactional
    public List<ResponseDTO> getResponsesForEmployer(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new VacancyNotFoundException("Vacancy not found!"));
        return responseRepository.findByVacancy(vacancy)
                .stream()
                .map(response -> {
                    User applicant = response.getUser(); // Получаем пользователя, который откликнулся
                    return new ResponseDTO(response.getId(), applicant.getEmail(), applicant.getUsername(), response.getStatus().toString(), response.getUser().getResume());
                })
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void updateResponseStatus(Long responseId, ResponseStatus status) {
        Response response = responseRepository.findById(responseId)
                .orElseThrow(() -> new EntityNotFoundException("Response not found"));

        User employer = userService.getUserByUsername(userService.getCurrentUser().getUsername()).get();
        if (!response.getVacancy().getEmployer().equals(employer)) {
            throw new AccessDeniedException("You are not allowed to update this response");
        }

        response.setStatus(status);
        responseRepository.save(response);

        // Уведомление соискателю
        emailService.sendEmail(
                response.getUser().getEmail(),
                "Update on Your Response",
                String.format("Your response to the vacancy '%s' has been %s.",
                        response.getVacancy().getTitle(),
                        status.toString().toLowerCase())
        );
    }

    @Override
    @Transactional
    public List<MyResponsesDTO> getResponsesForUser() {
        User user = userService.getUserByUsername(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return responseRepository.findByUser(user)
                .stream()
                .map(response -> {
                    Vacancy vacancy = response.getVacancy();
                    return new MyResponsesDTO(vacancy.getId(), vacancy.getTitle(), vacancy.getLocation()
                            , vacancy.getSalaryStart(), vacancy.getSalaryEnd(), vacancy.getExperience()
                            , vacancy.getEmployer().getCompanyName(), response.getStatus());
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<MyResponsesDTO> getResponses(ResponseStatus status) {
        User user = userService.getUserByUsername(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return responseRepository.findByUserAndStatus(user, status)
                .stream()
                .map(response -> {
                    Vacancy vacancy = response.getVacancy();
                    return new MyResponsesDTO(vacancy.getId(), vacancy.getTitle(), vacancy.getLocation()
                            , vacancy.getSalaryStart(), vacancy.getSalaryEnd(), vacancy.getExperience()
                            , vacancy.getEmployer().getCompanyName(), response.getStatus());
                })
                .collect(Collectors.toList());
    }
}
