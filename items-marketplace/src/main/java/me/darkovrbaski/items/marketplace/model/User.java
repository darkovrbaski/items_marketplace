package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`user`")
public class User extends EntityDb implements UserDetails {

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

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Set.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

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