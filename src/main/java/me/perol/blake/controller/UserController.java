package me.perol.blake.controller;

import me.perol.blake.dao.UserDao;
import me.perol.blake.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private UserDao userDao;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserDao userDao,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        user.setPwd(bCryptPasswordEncoder.encode(user.getPwd()));
        userDao.save(user);
    }
    @GetMapping
    public User getUserByName(@RequestParam String name) {
        User user = userDao.findByName(name);
        user.setPwd("");
        return user;
    }


//    @GetMapping
//    public List<User> getAllUser() {
//        return userDao.findAll();
//    }
}
