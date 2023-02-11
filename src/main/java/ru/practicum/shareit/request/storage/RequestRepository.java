package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    //@Query("SELECT i FROM Request i WHERE i.requestor.id = ?1 ORDER BY i.created DESC")
    List<Request> findByRequestorIdOrderByCreatedDesc(int ownerId);

    Page<Request> findByRequestorIdIsNot(int userId, Pageable pageable);

}
