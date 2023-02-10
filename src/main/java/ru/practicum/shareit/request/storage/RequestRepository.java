package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
