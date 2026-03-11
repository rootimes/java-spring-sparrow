package sparrow.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sparrow.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @EntityGraph(attributePaths = {"user", "postSource", "postTags.tag"})
    Optional<Post> findBySlug(String slug);

    @Override
    @EntityGraph(attributePaths = {"user", "postSource", "postTags.tag"})
    Optional<Post> findById(Integer id);

    @EntityGraph(attributePaths = "user")
    @Query("select p from Post p")
    Page<Post> findAllForList(Pageable pageable);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, int id);
}
