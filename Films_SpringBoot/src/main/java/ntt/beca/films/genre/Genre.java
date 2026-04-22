package ntt.beca.films.genre;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.base.AbstractEntity;
import ntt.beca.films.film.Film;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "genres")
public class Genre extends AbstractEntity {

      @Column(nullable = false, length = 50)
      private String label;

      @OneToMany(mappedBy = "genre", cascade = CascadeType.REMOVE)
      @JsonIgnore
      private List<Film> films;
}
