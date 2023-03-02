package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void addItem() {
        itemRepository.save(Item.builder()
                .name("testName")
                .description("testDescription")
                .available(true)
                .build());
        itemRepository.save(Item.builder()
                .name("testName2")
                .description("testDescription2")
                .available(true)
                .build());
    }

    @AfterEach
    public void clearDataBase() {
        itemRepository.deleteAll();
    }

    @Test
    public void searchItem_whenSearchTest_thenReturn2Records() {
        List<Item> items = itemRepository.searchItem("test");
        assertEquals(2, items.size());
    }

    @Test
    public void searchItem_whenSearch2_thenReternOneRecord() {
        List<Item> items = itemRepository.searchItem("2");
        assertEquals(1, items.size());
        assertEquals("testName2", items.get(0).getName());
    }
}