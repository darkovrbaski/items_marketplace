package me.darkovrbaski.items.marketplace.repository;

import me.darkovrbaski.items.marketplace.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends EntityRepository<User> {

  Page<User> findByUsernameContainingIgnoreCase(String name, Pageable pageable);
  
}
