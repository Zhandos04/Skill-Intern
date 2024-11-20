package project.by.skillintern.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.by.skillintern.dto.requests.FilterVacancyDTO;
import project.by.skillintern.dto.requests.VacancyDTO;
import project.by.skillintern.dto.responses.VacancyResponseDTO;
import project.by.skillintern.entities.User;
import project.by.skillintern.services.UserService;
import project.by.skillintern.services.VacancyService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vacancy")
@CrossOrigin(origins = "*")
@Tag(name="Vacancy", description="Взаймодействие с вакансиями")
@RequiredArgsConstructor
public class VacancyController {
    private final VacancyService vacancyService;
    private final UserService userService;

    @PostMapping("/add")
    @Operation(summary = "Add a new vacancy. Only Employers.")
    @ApiResponse(responseCode = "202", description = "Vacancy created successfully!")
    private ResponseEntity<?> addNewVacancy(@RequestBody @Valid VacancyDTO vacancyDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        vacancyService.createVacancy(vacancyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vacancy created successfully!");
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "Get vacancy detail by id. All Users(Токен керек емес)")
    private ResponseEntity<VacancyDTO> vacancyDetail(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getVacancyDetail(id));
    }

    @PutMapping("/edit/{id}")
    @Operation(summary = "Update vacancy detail. Employers")
    private ResponseEntity<String> editVacancy(@PathVariable Long id, @RequestBody VacancyDTO vacancyDTO) {
        vacancyService.updateVacancy(id, vacancyDTO);
        return ResponseEntity.ok("Vacancy updated successfully!");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete vacancy. Employers")
    private ResponseEntity<String> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.ok("Vacancy deleted successfully!");
    }

    @GetMapping("/all")
    @Operation(summary = "Get all vacancies. All Users(Токен керек емес)")
    @ApiResponse(responseCode = "200")
    private ResponseEntity<List<VacancyResponseDTO>> all() {
        return ResponseEntity.ok(vacancyService.getAllVacancies());
    }
    @GetMapping("/by-filter")
    @Operation(summary = "Get vacancies by filter. All Users(Токен керек емес)")
    private ResponseEntity<List<VacancyResponseDTO>> vacanciesByFilter(@RequestBody FilterVacancyDTO filterVacancyDTO) {
        return ResponseEntity.ok(vacancyService.getVacanciesByFilter(filterVacancyDTO));
    }
    @GetMapping("/my-vacancies")
    @Operation(summary = "Get my vacancies.")
    private ResponseEntity<List<VacancyResponseDTO>> getMyVacancies() {
        User currentUser = userService.getUserByUsername(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(vacancyService.getVacanciesByEmployer(currentUser));
    }
}
