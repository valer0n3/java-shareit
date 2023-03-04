package ru.practicum.shareit.booking.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOwnerDTO;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDto mapBookingToBookingDTO(Booking booking);

    Booking mapNewBookingDtoToBooking(NewBookingDto newBookingDto);

    @Mapping(target = "bookerId", source = "booking.booker.id")
    BookingOwnerDTO mapBookingToBookingOwnerDTO(Booking booking);
}
