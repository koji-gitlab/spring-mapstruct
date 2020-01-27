package com.mitrais.app.mapper;

import com.mitrais.app.domain.User;
import com.mitrais.app.mapper.model.RegisterUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User registerUserToUser(RegisterUser dto);

    RegisterUser userToRegisterUser(User entity);

}
