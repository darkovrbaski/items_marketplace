package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Embeddable
@Immutable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {

  String country;

  String city;

  String street;

  String number;

}