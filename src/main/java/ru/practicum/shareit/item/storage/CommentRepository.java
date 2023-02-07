package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "SELECT * FROM comments WHERE item_id = ?1", nativeQuery = true)
    List<Comment> getCommentsOfItem(int itemId);

    @Query("SELECT i FROM Comment i WHERE i.item.id = ?1")
    List<Comment> getCommentForItem(int itemId);

    @Query("SELECT i FROM Comment i WHERE i.item.id in :itemId")
    List<Comment> getCommentForOwner(List<Integer> itemId);

}
