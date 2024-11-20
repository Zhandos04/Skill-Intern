package project.by.skillintern.dto.responses;

import lombok.Data;

@Data
public class VacancyResponseDTO {
    private Long id;
    private String title;
    private String location;
    private Long salaryStart;
    private Long salaryEnd;
    private Integer experience;
    private String companyName;

    public VacancyResponseDTO(){}
    public VacancyResponseDTO(Long id, String title, String location, Long salaryStart, Long salaryEnd, Integer experience, String companyName) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.salaryStart = salaryStart;
        this.salaryEnd = salaryEnd;
        this.experience = experience;
        this.companyName = companyName;
    }
}
