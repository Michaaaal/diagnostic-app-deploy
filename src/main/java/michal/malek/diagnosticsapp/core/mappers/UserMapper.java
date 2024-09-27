package michal.malek.diagnosticsapp.core.mappers;

import michal.malek.diagnosticsapp.auth.models.UserRegisterDTO;
import michal.malek.diagnosticsapp.core.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserMapper {

    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public abstract UserEntity userRegisterDTOToUserEntity(UserRegisterDTO userRegisterDTO);

    @ObjectFactory
    public UserEntity createUserEntity(UserRegisterDTO dto) {
        return new UserEntity(dto.getEmail(), dto.getPassword());
    }
}
