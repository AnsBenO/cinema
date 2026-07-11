package com.ansbeno.films.nationality;

import java.util.List;

import com.ansbeno.films.person.Person;
import com.ansbeno.films.shared.base.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Table(name = "nationality")
public class Nationality extends AbstractEntity {

      @Column(nullable = false, length = 50)
      private String label;

      @OneToMany(mappedBy = "nationality")
      @JsonIgnore
      private List<Person> persons;

}
