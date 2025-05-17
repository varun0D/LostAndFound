package com.college.lostfound.service;

import com.college.lostfound.model.Item;
import com.college.lostfound.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public Item postItem(Item item) {
        return repo.save(item);
    }

    public List<Item> getItemsByStatus(String status) {
        return repo.findByStatus(status);
    }
}
