package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class EntityDb {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

}
