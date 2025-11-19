package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {
    @Mapping(source = "request.id", target = "requestId")
    ItemDto toItemDto(Item item);

    @Mapping(source = "requestId", target = "request.id")
    Item fromDto(ItemDto itemDto);

    List<ItemDto> toItemDtoList(List<Item> items);

    void updateItemFromDto(ItemDto dto, @MappingTarget Item item);
}
