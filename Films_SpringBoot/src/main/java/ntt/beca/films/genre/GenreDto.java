package ntt.beca.films.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.base.BaseDto;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto extends BaseDto {
      private Long id;

      @NotBlank(message = "Genre label is required")
      @Size(max = 100, message = "Genre label must not exceed 100 characters")
      private String label;
}
