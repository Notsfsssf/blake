package me.perol.blake.controller;

import me.perol.blake.dao.PostDao;
import me.perol.blake.dao.TagDao;
import me.perol.blake.entity.Post;
import me.perol.blake.entity.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("tags")
public class TagController {
    private final
    TagDao tagDao;
    private final
    PostDao postDao;

    public TagController(TagDao tagDao, PostDao postDao) {
        this.tagDao = tagDao;
        this.postDao = postDao;
    }

    @GetMapping
    public List<Tag> getTagsByPostId(@RequestParam(required = false) Long postId) {
        if (postId==null){
            return  tagDao.findAll();
        }
        Optional<Post> postOptional = postDao.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return new ArrayList<>(post.getTags());
        }
        return null;
    }
}
