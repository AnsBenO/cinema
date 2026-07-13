package com.ansbeno.films.shared.base;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @CreationTimestamp
      @Column(nullable = false, updatable = false)
      private LocalDateTime createdAt;

      @UpdateTimestamp
      @Column(nullable = false)
      private LocalDateTime updatedAt;

      @Override
      public boolean equals(Object o) {
            if (this == o)
                  return true;
            if (o == null || getClass() != o.getClass())
                  return false;
            AbstractEntity that = (AbstractEntity) o;
            return this.getId().equals(that.getId());
      }

      @Override
      public int hashCode() {
            return this.getId().hashCode();
      }
}
