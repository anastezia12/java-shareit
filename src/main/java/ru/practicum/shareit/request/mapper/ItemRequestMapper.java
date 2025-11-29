package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFullInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestSaveDto;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemRequestMapper {

    ItemRequest fromSaveDtoToItemRequest(ItemRequestSaveDto itemRequestSaveDto);

    @Mapping(source = "requester.id", target = "requesterId")
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> itemRequestList);

    @Mapping(source = "requester.id", target = "requesterId")
    ItemRequestFullInfoDto toItemRequestFullInfoDto(ItemRequest itemRequest);

    List<ItemRequestFullInfoDto> toItemRequestFullInfoDtoList(List<ItemRequest> itemRequest);

}
