package aiss.gitminer.repository;

import aiss.gitminer.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Page<User> findByUsername(String userName, Pageable pages);

    Page<User> findByNameContaining(String name, Pageable pages);

    Page<User> findByUsernameAndNameContaining(String username, String name, Pageable pages);
}
