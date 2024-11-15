package project.by.skillintern.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "vacancies")
@Data
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "salary_start", nullable = false)
    private Long salaryStart;

    @Column(name = "salary_end", nullable = false)
    private Long salaryEnd;

    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Column(name = "size_of_team", nullable = false)
    private Integer sizeOfTeam;

    @Column(name = "operating_mode", nullable = false)
    private String operatingMode;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "english_level", nullable = false)
    private String englishLevel;

    @Column(name = "profession", nullable = false)
    private String profession;

    @Column(name = "description")
    private String description;

    @Column(name = "telegram")
    private String telegram;

    @Column(name = "whatsapp_number")
    private String whatsappNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "remote_work")
    private Boolean remoteWork;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;
}
