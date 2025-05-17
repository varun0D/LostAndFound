package com.college.lostfound.controller;

import com.college.lostfound.model.Item;
import com.college.lostfound.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping("/post")
    public Item postItem(@RequestBody Item item) {
        return service.postItem(item);
    }

    @GetMapping("/lost")
    public List<Item> getLostItems() {
        return service.getItemsByStatus("lost");
    }

    @GetMapping("/found")
    public List<Item> getFoundItems() {
        return service.getItemsByStatus("found");
    }
}
