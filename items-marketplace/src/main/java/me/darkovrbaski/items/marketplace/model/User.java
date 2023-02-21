package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`user`")
public class User extends EntityDb {

  @Size(min = 3, message = "Username must be at least 3 characters long")
  @Column(nullable = false, unique = true)
  String username;

  @Size(min = 3, message = "Password must be at least 3 characters long")
  @Column(nullable = false)
  String password;

  @Column
  String firstName;

  @Column
  String lastName;

  @Email(message = "Email should be valid")
  @Column
  String email;

  @Column
  String phone;

  @Column
  String image;

  @Embedded
  Address address;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    final User user = (User) o;
    return Objects.equals(getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}