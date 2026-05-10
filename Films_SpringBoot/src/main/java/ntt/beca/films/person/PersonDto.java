package ntt.beca.films.person;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.nationality.NationalityDto;
import ntt.beca.films.shared.base.BaseDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto extends BaseDto {
      private Long id;

      @NotBlank(message = "First name is required")
      @Size(max = 100, message = "First name must not exceed 100 characters")
      private String firstName;

      @NotBlank(message = "Last name is required")
      @Size(max = 100, message = "Last name must not exceed 100 characters")
      private String lastName;

      private String photo;

      @NotNull(message = "Birth date is required")
      @Past(message = "Birth date must be in the past")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      private LocalDate birthDate;

      @NotNull(message = "Person type is required")
      private PersonType personType;

      @Valid
      @NotNull(message = "Nationality is required")
      private NationalityDto nationality;

      private LocalDateTime createdAt;

      private LocalDateTime updatedAt;
}
