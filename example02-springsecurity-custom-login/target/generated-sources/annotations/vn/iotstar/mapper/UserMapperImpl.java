package vn.iotstar.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import vn.iotstar.dto.UserDTO;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-22T09:51:14+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260528-0407, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.Builder userDTO = UserDTO.builder();

        userDTO.roleName( userRoleName( user ) );
        userDTO.id( user.getId() );
        userDTO.username( user.getUsername() );
        userDTO.email( user.getEmail() );
        userDTO.fullName( user.getFullName() );
        userDTO.images( user.getImages() );
        userDTO.enabled( user.isEnabled() );

        return userDTO.build();
    }

    private String userRoleName(User user) {
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        return role.getName();
    }
}
