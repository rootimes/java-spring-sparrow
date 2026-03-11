package sparrow.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sparrow.post.entity.PostTag;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Integer> {
}
