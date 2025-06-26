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
            // Define upload directory inside user home folder
            String uploadDir = System.getProperty("user.home") + File.separator + "lostfound_uploads" + File.separator;
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // Prepare the file name and file object
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            File file = new File(uploadDir + fileName);

            // Save the uploaded file to the defined location
            photo.transferTo(file);

            // Convert JSON string to Item object
            ObjectMapper mapper = new ObjectMapper();
            Item item = mapper.readValue(itemJson, Item.class);

            // Set photo path relative to uploads folder (adjust this path if needed)
            item.setPhoto("/lostfound_uploads/" + fileName);

            // Save item to database and return it
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

    @GetMapping
    public List<Item> getAllItems() {
        return service.getAllItems();
    }
}
