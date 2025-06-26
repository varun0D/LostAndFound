package com.college.lostfound.controller;

import com.college.lostfound.model.Item;
import com.college.lostfound.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping(value = "/post", consumes = "multipart/form-data")
    public Item postItem(
            @RequestParam("item") String itemJson,
            @RequestParam("photo") MultipartFile photo
    ) {
        try {
            // Save photo to /uploads/
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            String uploadDir = "uploads/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) uploadFolder.mkdirs();

            File file = new File(uploadDir + fileName);
            photo.transferTo(file);

            // Convert JSON string to Item
            ObjectMapper mapper = new ObjectMapper();
            Item item = mapper.readValue(itemJson, Item.class);

            item.setPhoto("/" + uploadDir + fileName); // save image path

            return service.postItem(item);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading item", e);
        }
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
