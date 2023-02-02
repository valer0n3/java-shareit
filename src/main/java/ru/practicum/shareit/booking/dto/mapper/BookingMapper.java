package ru.practicum.shareit.booking.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking mapBookingDtoToBooking(BookingDto bookingDto);
    BookingDto mapBookingToBookingDTO(Booking booking);

    Booking mapNewBookingDtoToBooking(NewBookingDto newBookingDto);

    NewBookingDto mapBookingToNewBookingDto(Booking booking);

}
