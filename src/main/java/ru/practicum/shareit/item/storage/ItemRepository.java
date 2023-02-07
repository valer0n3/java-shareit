package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("SELECT i FROM Item i WHERE i.owner.id = ?1 ORDER BY i.id")
    List<Item> getAllItemsForOwner(int ownerId);



    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " + "" +
            "or upper(i.description) like upper(concat('%', ?1, '%'))" +
            "and i.available = true")
    List<Item> searchItem(String text);
}
