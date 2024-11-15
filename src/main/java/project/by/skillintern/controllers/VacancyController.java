package project.by.skillintern.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.by.skillintern.dto.requests.FilterVacancyDTO;
import project.by.skillintern.dto.requests.VacancyDTO;
import project.by.skillintern.entities.User;
import project.by.skillintern.entities.Vacancy;
import project.by.skillintern.services.impl.UserServiceImpl;
import project.by.skillintern.services.impl.VacancyServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyController {
    private final VacancyServiceImpl vacancyService;
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @PostMapping("/add")
    private ResponseEntity<?> addNewVacancy(@RequestBody @Valid VacancyDTO vacancyDTO, BindingResult bindingResult) {
        User currentUser = userService.getUserByUsername(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        Vacancy vacancy = convertToVacancy(vacancyDTO);
        vacancy.setEmployer(currentUser);
        vacancyService.createVacancy(vacancy);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vacancy created successfully!");
    }
    @GetMapping("/all")
    private ResponseEntity<List<Vacancy>> all() {
        return ResponseEntity.ok(vacancyService.getAllVacancies());
    }
    @GetMapping("/by-filter")
    private ResponseEntity<List<Vacancy>> vacanciesByFilter(@RequestBody FilterVacancyDTO filterVacancyDTO) {
        return ResponseEntity.ok(vacancyService.getVacanciesByFilter(filterVacancyDTO));
    }
    @GetMapping("/my-vacancies")
    public ResponseEntity<List<Vacancy>> getMyVacancies() {
        User currentUser = userService.getUserByUsername(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(vacancyService.getVacanciesByEmployer(currentUser));
    }

    private Vacancy convertToVacancy(VacancyDTO vacancyDTO) {
        return modelMapper.map(vacancyDTO, Vacancy.class);
    }
}
