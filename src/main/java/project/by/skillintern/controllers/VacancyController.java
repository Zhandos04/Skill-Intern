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
import project.by.skillintern.dto.requests.VacancyDTO;
import project.by.skillintern.dto.responses.VacancyResponseDTO;
import project.by.skillintern.entities.User;
import project.by.skillintern.entities.Vacancy;
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

    @PatchMapping("/addCompany")
    @Operation(summary = "Add a company for employer. Only Employers")
    public ResponseEntity<String> addCompanyForEmployer(@RequestParam String company) {
        String employerName = userService.getCurrentUser().getUsername();
        userService.addCompany(employerName, company);

        return ResponseEntity.ok("Company added successfully!");
    }

    @GetMapping("/companies")
    @Operation(summary = "Get all company names. All users")
    public ResponseEntity<List<String>> getAllCompanyNames() {
        return ResponseEntity.ok(vacancyService.getAllCompanyNames());
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new vacancy. Only Employers.")
    @ApiResponse(responseCode = "202", description = "Vacancy created successfully!")
    public ResponseEntity<?> addNewVacancy(@RequestBody @Valid VacancyDTO vacancyDTO, BindingResult bindingResult) {
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
    public ResponseEntity<VacancyDTO> vacancyDetail(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getVacancyDetail(id));
    }

    @PutMapping("/edit/{id}")
    @Operation(summary = "Update vacancy detail. Employers")
    public ResponseEntity<String> editVacancy(@PathVariable Long id, @RequestBody VacancyDTO vacancyDTO) {
        vacancyService.updateVacancy(id, vacancyDTO);
        return ResponseEntity.ok("Vacancy updated successfully!");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete vacancy. Employers")
    public ResponseEntity<String> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.ok("Vacancy deleted successfully!");
    }

    @GetMapping("/all")
    @Operation(summary = "Get all vacancies. All Users(Токен керек емес)")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<VacancyResponseDTO>> all() {
        return ResponseEntity.ok(vacancyService.getAllVacancies());
    }
    @GetMapping("/by-filter")
    @Operation(summary = "Get vacancies by filter. All Users(Токен керек емес)")
    public ResponseEntity<List<VacancyResponseDTO>> vacanciesByFilter(
            @RequestParam String[] levels,
            @RequestParam String[] companies,
            @RequestParam String[] technologies,
            @RequestParam String employmentType,
            @RequestParam Boolean remoteWork
    ) {
        return ResponseEntity.ok(vacancyService.getVacanciesByFilter(levels, companies, technologies, employmentType, remoteWork));
    }
    @GetMapping("/my-vacancies")
    @Operation(summary = "Get my vacancies.")
    public ResponseEntity<List<VacancyResponseDTO>> getMyVacancies() {
        User currentUser = userService.getUserByUsername(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(vacancyService.getVacanciesByEmployer(currentUser));
    }
    @GetMapping("/search")
    @Operation(summary = "Get vacancies by search text.")
    public ResponseEntity<List<VacancyResponseDTO>> searchVacancies(@RequestParam String searchText) {
        List<VacancyResponseDTO> vacancies = vacancyService.searchVacancies(searchText);
        return new ResponseEntity<>(vacancies, HttpStatus.OK);
    }
}
