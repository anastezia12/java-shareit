package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {
    ItemRequestDto toItemDto(Item item);

    ItemResponseDto toItemResponseDto(Item item);

    Item fromDto(ItemRequestDto itemRequestDto);

    List<ItemRequestDto> toItemDtoList(List<Item> items);

    void updateItemFromDto(ItemRequestDto dto, @MappingTarget Item item);

    List<ItemResponseDto> toItemResponseDtoList(List<Item> items);
}
