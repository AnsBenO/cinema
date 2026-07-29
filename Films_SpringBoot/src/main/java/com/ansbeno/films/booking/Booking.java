package com.ansbeno.films.booking;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.ansbeno.films.screening.Screening;
import com.ansbeno.films.shared.base.AbstractEntity;
import com.ansbeno.films.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking extends AbstractEntity {

      @ManyToOne
      @JoinColumn(name = "customer_id", nullable = false)
      private UserEntity customer;

      @ManyToOne
      @JoinColumn(name = "screening_id", nullable = false)
      private Screening screening;

      @Column(nullable = false)
      private int quantity;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false, length = 20)
      private BookingStatus status;

      @CreationTimestamp
      @Column(name = "booked_at", nullable = false)
      private LocalDateTime bookedAt;
}
