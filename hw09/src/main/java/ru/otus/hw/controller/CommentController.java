package ru.otus.hw.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comment/delete")
    public String deleteComment(@RequestParam("id") Long id,
                                @RequestParam("bookId") Long bookId) {
        var comment = commentService.findById(id)
                .orElseThrow(NotFoundException::new);
        commentService.deleteById(comment.getId());
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
