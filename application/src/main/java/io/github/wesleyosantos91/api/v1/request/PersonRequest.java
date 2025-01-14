package io.github.wesleyosantos91.api.v1.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {

    private String name;
    private LocalDate dateOfBirth;
    private String cpf;
    private String email;
}
