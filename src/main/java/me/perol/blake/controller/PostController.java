package me.perol.blake.controller;

import lombok.extern.slf4j.Slf4j;
import me.perol.blake.dao.PostDao;
import me.perol.blake.dao.TagDao;
import me.perol.blake.dao.UserDao;
import me.perol.blake.entity.Post;
import me.perol.blake.entity.Tag;
import me.perol.blake.entity.User;
import me.perol.blake.form.PostForm;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("posts")
public class PostController {
    private final
    PostDao postDao;
    private final
    UserDao userDao;
    private final
    TagDao tagDao;

    public PostController(PostDao postDao, UserDao userDao, TagDao tagDao) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.tagDao = tagDao;
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postDao.getOne(id);
    }

    @PostMapping
    public void createPost(Principal principal, @RequestBody PostForm postForm) {

        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        if (postForm.getTags() != null)
            if (postForm.getTags().isEmpty()) {
                String[] tags = postForm.getTags().split(",");
                for (String i : tags
                ) {
                    Tag tag = tagDao.findByName(i);
                    if (tag == null) {
                        tag = new Tag();
                        tag.setName(i);
                        tag = tagDao.save(tag);
                    }
                    post.getTags().add(tag);
                }
            }
        post.setDate(ZonedDateTime.now());
        User user = userDao.findByName(principal.getName());
        post.setName(user.getName());
        post.setUser(user);
        postDao.save(post);
    }

    @PatchMapping("/{id}")
    public void updatePost(Principal principal,@RequestBody  PostForm postForm, @PathVariable Long id) {
        Post post = new Post();
        post.setContent(postForm.getContent());
        Optional<Post> byId = postDao.findById(id);
        if (byId.isPresent()) {
            post.setId(byId.get().getId());
        } else {
            throw new RuntimeException();
        }
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        if (postForm.getTags() != null)
            if (postForm.getTags().isEmpty()) {
                String[] tags = postForm.getTags().split(",");
                for (String i : tags
                ) {
                    Tag tag = tagDao.findByName(i);
                    if (tag == null) {
                        tag = new Tag();
                        tag.setName(i);
                        tag = tagDao.save(tag);
                    }
                    post.getTags().add(tag);
                }
            }
        post.setDate(ZonedDateTime.now());
        User user = userDao.findByName(principal.getName());
        post.setName(user.getName());
        post.setUser(user);
        postDao.save(post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(Principal principal, @PathVariable Long id) {
        postDao.deleteById(id);
    }

    @GetMapping
    public List<Post> getAllPost(@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String tagName) {
        List<Post> collect;
        Pageable pageable = null;
        if (pageNumber != null && pageSize != null) {
            pageable = PageRequest.of(pageNumber, pageSize);
            if (tagName != null) {
                Stream<Post> postStream = postDao.findByTag(tagName, pageable).get();
                collect = postStream.collect(Collectors.toList());
            } else {
                Stream<Post> postStream = postDao.findAll(pageable).get();
                collect = postStream.collect(Collectors.toList());
            }
        } else {
            if (tagName != null) {
                Stream<Post> postStream = postDao.findByTag(tagName, pageable).get();
                collect = postStream.collect(Collectors.toList());
            } else {
                collect = postDao.findAll();
            }

        }

        return collect;
    }
}
