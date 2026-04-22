package ntt.beca.films.person;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.base.AbstractEntity;
import ntt.beca.films.nationality.Nationality;
import ntt.beca.films.film.Film;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "persons")
public class Person extends AbstractEntity {

      @Column(nullable = false, length = 50)
      private String lastName;

      @Column(nullable = false, length = 50)
      private String firstName;

      @Column(nullable = true, length = 100)
      private String photo;

      @Column(name = "birth_date")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      private LocalDate birthDate;

      @Column(nullable = false, length = 50)
      @Enumerated(EnumType.STRING)
      private PersonType personType;

      @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
      @JoinColumn(name = "nationality_id")
      private Nationality nationality;

      @ManyToMany(mappedBy = "actors", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
      @JsonIgnore
      private List<Film> films;

      @OneToMany(mappedBy = "director", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
      @JsonIgnore
      private List<Film> directedFilms;

}
