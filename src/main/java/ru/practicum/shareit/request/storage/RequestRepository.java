package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByRequestorIdOrderByCreatedDesc(int ownerId);

    Page<Request> findByRequestorIdIsNot(int userId, Pageable pageable);
}
