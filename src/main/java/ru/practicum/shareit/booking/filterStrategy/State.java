package ru.practicum.shareit.booking.filterStrategy;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum State {
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    ALL;

    @JsonCreator
    public static State from(String value) {
        return State.valueOf(value.toUpperCase());
    }
}
