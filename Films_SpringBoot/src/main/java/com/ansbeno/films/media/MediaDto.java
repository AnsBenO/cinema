package com.ansbeno.films.media;

import com.ansbeno.films.shared.base.BaseDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaDto extends BaseDto {
      private Long id;

      @NotBlank(message = "Media URL is required")
      private String media;

      @NotNull(message = "Media type is required")
      private MediaType mediaType;

      @NotNull(message = "Film is required")
      private Long filmId;
}
