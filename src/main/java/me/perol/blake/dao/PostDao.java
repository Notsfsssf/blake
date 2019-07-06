package me.perol.blake.dao;

import me.perol.blake.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p INNER JOIN p.tags t WHERE t.name = :tag")
    Page<Post> findByTag(@Param("tag") String tag, Pageable pageable);
}
