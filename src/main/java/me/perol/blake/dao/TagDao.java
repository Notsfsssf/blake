package me.perol.blake.dao;

import me.perol.blake.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagDao  extends JpaRepository<Tag,Long> {
    Tag findByName(String name);
}
