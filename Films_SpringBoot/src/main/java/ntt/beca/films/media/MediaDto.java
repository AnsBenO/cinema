package ntt.beca.films.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import ntt.beca.films.shared.base.BaseDto;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaDto extends BaseDto {
      @NotNull
      private Long id;
      private String media;
      private MediaType mediaType;
      @NotNull
      private Long filmId;
}
