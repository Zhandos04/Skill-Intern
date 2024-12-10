package project.by.skillintern.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.by.skillintern.dto.requests.NewsDTO;
import project.by.skillintern.dto.requests.UserDTO;
import project.by.skillintern.services.NewsService;
import project.by.skillintern.services.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@Tag(name="admin", description="Взаймодействие с админами")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final NewsService newsService;
    @GetMapping("/allUsers")
    @Operation(summary = "Get all users. Only ADMIN")
    public ResponseEntity<List<UserDTO>> allUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }
    @PostMapping("/addNews")
    @Operation(summary = "Add news. Only ADMIN")
    public ResponseEntity<?> addNews(@RequestParam("title") String title,
                                     @RequestParam("description") String description,
                                     @RequestParam("image") MultipartFile image) {
        try {
            NewsDTO newsDTO = new NewsDTO();
            newsDTO.setTitle(title);
            newsDTO.setDescription(description);
            newsDTO.setImage(image);

            newsService.addNews(newsDTO);
            return ResponseEntity.status(200).body("News added successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image or save news");
        }
    }
}
