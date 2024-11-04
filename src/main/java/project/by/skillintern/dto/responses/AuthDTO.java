package project.by.skillintern.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    private String accessToken;
    private String refreshToken;
}