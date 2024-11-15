package project.by.skillintern.dto.requests;

import lombok.Data;

@Data
public class VacancyDTO {
    private String title;
    private String location;
    private Long salaryStart;
    private Long salaryEnd;
    private Integer experience;
    private Integer sizeOfTeam;
    private String operatingMode;
    private String level;
    private String englishLevel;
    private String profession;
    private String description;
    private String telegram;
    private String whatsappNumber;
    private String email;
    private Boolean remoteWork;
}
