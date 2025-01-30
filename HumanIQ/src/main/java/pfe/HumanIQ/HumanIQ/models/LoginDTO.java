package pfe.HumanIQ.HumanIQ.models;

import lombok.AllArgsConstructor;
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
