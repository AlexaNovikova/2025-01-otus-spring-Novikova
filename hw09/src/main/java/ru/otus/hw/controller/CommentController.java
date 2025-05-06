package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comment/delete")
    public String deleteComment(@RequestParam Long id,
                                @RequestParam Long bookId) {
        commentService.deleteById(id);
        return "redirect:/bookWithComments?id=" + bookId;
    }

    @PostMapping("/comment/save")
    public String saveComment(@RequestParam("id") Long bookId,
                              @RequestParam("text") String text) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(text);
        commentDto.setBookId(bookId);
        commentService.save(commentDto);
        return "redirect:/bookWithComments?id=" + bookId;
    }
}
