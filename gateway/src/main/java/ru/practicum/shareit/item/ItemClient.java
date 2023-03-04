package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addNewItem(ItemDto itemDto, int userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(ItemPatchDto itemPatchDto, int userId, int itemId) {
        return patch("/" + itemId, userId, itemPatchDto);
    }

    public ResponseEntity<Object> getItemById(int itemId, int userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllItemsForOwner(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItem(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", null, parameters);
    }

    public ResponseEntity<Object> addComment(CommentDto commentDto, int itemId, int userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}

