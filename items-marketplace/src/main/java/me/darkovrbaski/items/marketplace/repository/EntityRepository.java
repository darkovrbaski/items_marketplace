package me.darkovrbaski.items.marketplace.repository;

import jakarta.persistence.EntityNotFoundException;
import me.darkovrbaski.items.marketplace.model.EntityDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityRepository<T extends EntityDb> extends JpaRepository<T, Long> {

  default T findByIdOrThrow(final Long id) {
    return findById(id).orElseThrow(
        () -> new EntityNotFoundException("Entity with id " + id + " not found"));
  }

}
