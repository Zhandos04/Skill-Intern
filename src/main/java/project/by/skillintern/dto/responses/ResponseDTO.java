package project.by.skillintern.dto.responses;

import lombok.Data;

@Data
public class ResponseDTO {
    private Long id;
    private String email;
    private String username;
    private String status;
    private String resume;
    public ResponseDTO(Long id, String email, String username, String status, String resume) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.status = status;
        this.resume = resume;
    }
}
