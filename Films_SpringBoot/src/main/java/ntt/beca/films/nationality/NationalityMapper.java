package ntt.beca.films.nationality;

import org.springframework.stereotype.Component;

@Component
public class NationalityMapper {

      public NationalityDto toDto(Nationality nationality) {
            return NationalityDto.builder()
                        .id(nationality.getId())
                        .label(nationality.getLabel())
                        .build();
      }

      public Nationality toEntity(NationalityDto nationality) {
            return Nationality.builder()
                        .id(nationality.getId())
                        .label(nationality.getLabel())
                        .build();
      }

}
