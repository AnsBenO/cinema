package ntt.beca.films.hall;

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
import ntt.beca.films.screening.Screening;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "halls")
public class Hall extends AbstractEntity {
      @Column(nullable = false, length = 40)
      private int number;

      @Column(nullable = false, length = 40)
      private int capacity;

      @OneToMany(mappedBy = "hall",cascade = CascadeType.REMOVE)
      @JsonIgnore
      private List<Screening> screenings;
}
