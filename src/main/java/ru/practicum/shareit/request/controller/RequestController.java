package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestGetAllDto;
import ru.practicum.shareit.request.service.RequestServiceImplementation;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.variables.Variables.X_SHARER_USER_ID;

@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class RequestController {
    private final RequestServiceImplementation requestServiceImplementation;

    @PostMapping
    public RequestDto addNewRequest(@Valid @RequestBody NewRequestDto newRequestDto,
                                    @RequestHeader(X_SHARER_USER_ID) int userId) {
        return requestServiceImplementation.addNewRequest(newRequestDto, userId);
    }

    @GetMapping
    public List<RequestGetAllDto> getOwnRequests(@RequestHeader(X_SHARER_USER_ID) int userId) {
        return requestServiceImplementation.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<RequestGetAllDto> getOtherUsersRequests(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                        @RequestParam(defaultValue = "0") @Min(0) int from,
                                                        @RequestParam(defaultValue = "10") @Min(1) int size) {
        return requestServiceImplementation.getOtherUsersRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestGetAllDto getRequestWithAnswers(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                  @PathVariable int requestId) {
        return requestServiceImplementation.getRequestWithAnswers(userId, requestId);
    }
}
