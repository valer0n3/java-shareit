package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(value = "SELECT * FROM items  WHERE owner_id = ?1", nativeQuery = true)
    List<Item> getAllItemsForOwner(int ownerId);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " + "" +
            "or upper(i.description) like upper(concat('%', ?1, '%'))" +
            "and i.available = true")
    List<Item> searchItem(String text);
}
