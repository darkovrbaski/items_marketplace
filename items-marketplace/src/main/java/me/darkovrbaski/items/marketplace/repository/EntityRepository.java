package me.darkovrbaski.items.marketplace.repository;

import jakarta.persistence.EntityNotFoundException;
import me.darkovrbaski.items.marketplace.model.EntityDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityRepository<T extends EntityDB> extends JpaRepository<T, Long> {

  default T get(final Long id) {
    return findById(id).orElseThrow(EntityNotFoundException::new);
  }

}
