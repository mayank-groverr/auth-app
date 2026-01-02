package practice.mayank.auth.mapper;

import org.mapstruct.Mapper;
import practice.mayank.auth.dto.UserRequest;
import practice.mayank.auth.dto.UserResponse;
import practice.mayank.auth.entity.User;

@Mapper(componentModel = "spring")
public interface GenericMapper {

    UserResponse userToUserResponse (User user);

    User userRequestToUser(UserRequest userRequest);
}

