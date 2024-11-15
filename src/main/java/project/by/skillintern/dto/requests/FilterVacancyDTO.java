package project.by.skillintern.dto.requests;

import lombok.Data;

@Data
public class FilterVacancyDTO {
    private String[] levels; // Уровень: Intern, Junior, Middle, Senior, Lead
    private String[] companies; // Компании
    private String[] technologies; // Технологии (например, Java, Python)
    private String employmentType; // Тип занятости: Full-time, Part-time, Contract, Hybrid
    private Boolean remoteWork;
}
