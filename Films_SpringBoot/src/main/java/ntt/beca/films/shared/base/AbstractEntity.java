package ntt.beca.films.shared.base;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @CreationTimestamp
      @Column(nullable = false)
      private LocalDateTime createdAt;

      @UpdateTimestamp
      @Column(nullable = false)
      private LocalDateTime updatedAt;

      public LocalDateTime getCreatedAt() {
            return createdAt;
      }

      public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
      }

      public LocalDateTime getUpdatedAt() {
            return updatedAt;
      }

      public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      @Override
      public boolean equals(Object obj) {
            if (this == obj) {
                  return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                  return false;
            }

            AbstractEntity entity = (AbstractEntity) obj;

            return id.equals(entity.id);
      }

      @Override
      public int hashCode() {
            return id.hashCode();
      }

}
