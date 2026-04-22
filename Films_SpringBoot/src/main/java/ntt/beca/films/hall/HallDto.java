package ntt.beca.films.hall;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.screening.ScreeningDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HallDto {
      private Long id;
      private int number;
      private int capacity;
      private List<ScreeningDto> screenings;
}
