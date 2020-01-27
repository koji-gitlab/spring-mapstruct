package com.mitrais.app.api;

import com.mitrais.app.mapper.UserMapper;
import com.mitrais.app.mapper.model.RegisterUser;
import com.mitrais.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("${app.rest.user.endpoint.path}")
@RestController
public class UserController {

    private UserMapper userMapper;
    private UserService userService;

    @Autowired
    public void setUserMapper(UserMapper mapper) {
        this.userMapper = mapper;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public RegisterUser register(@Valid @RequestBody RegisterUser user) {
        return userMapper.userToRegisterUser(userService.add(userMapper.registerUserToUser(user)));
    }

}
