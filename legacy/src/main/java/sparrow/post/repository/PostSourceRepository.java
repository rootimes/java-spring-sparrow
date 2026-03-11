package sparrow.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sparrow.post.entity.PostSource;

@Repository
public interface PostSourceRepository extends JpaRepository<PostSource, Integer> {
}
