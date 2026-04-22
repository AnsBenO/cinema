package ntt.beca.films.media;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.base.AbstractEntity;
import ntt.beca.films.film.Film;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "medias")
public class Media extends AbstractEntity {
      public enum MediaType {
            IMAGE, VIDEO, DOCUMENT
      }

      @Column(nullable = false, length = 100)
      private String media;

      @Column(nullable = true, length = 50)
      @Enumerated(EnumType.STRING)
      private MediaType mediaType;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "film_id")
      private Film film;
}
