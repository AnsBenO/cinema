package ntt.beca.films.user;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.security.Role;
import ntt.beca.films.rating.FilmRating;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
      @SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
      private Long id;

      @Column(unique = true)
      private String username;

      @Column(unique = true)
      private String email;

      private String password;

      @Enumerated(EnumType.STRING)
      private Role role;

      @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
      List<FilmRating> filmRatings;

      @CreationTimestamp
      @Column(nullable = false)
      private LocalDateTime createdAt;

      @UpdateTimestamp
      @Column(nullable = false)
      private LocalDateTime updatedAt;
}