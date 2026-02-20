package ru.example.atm.mapper;

import org.mapstruct.*;
import ru.example.atm.dto.AccountCreateDto;
import ru.example.atm.dto.AccountResponseDto;
import ru.example.atm.dto.AccountUpdateDto;
import ru.example.atm.entity.AccountEntity;

@Mapper(componentModel = "sspring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    AccountEntity toEntity(AccountCreateDto accountCreateDto);

    AccountResponseDto toResponse(AccountEntity accountEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateEntity(AccountUpdateDto dto, @MappingTarget AccountEntity entity);
}
