package project.by.skillintern.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {
    private String email;
    private String password;
}