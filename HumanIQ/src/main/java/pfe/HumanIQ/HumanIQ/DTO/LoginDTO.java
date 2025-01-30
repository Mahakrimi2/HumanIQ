package pfe.HumanIQ.HumanIQ.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class LoginDTO {
    private String token;

    public LoginDTO(String token) {
        this.token = token;
    }
}
