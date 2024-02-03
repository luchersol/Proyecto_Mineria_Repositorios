package aiss.gitminer.repository;

import aiss.gitminer.model.Commit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepository extends JpaRepository<Commit,String> {

    Page<Commit> findByAuthorName(String name , Pageable page);
    Page<Commit> findByAuthorEmail(String email, Pageable page);
    Page<Commit> findByAuthorNameAndAuthorEmail(String name, String email, Pageable page);

}