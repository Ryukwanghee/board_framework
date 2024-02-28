package com.codingrecipe.board.controller;

import com.codingrecipe.board.dto.CommentDTO;
import com.codingrecipe.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public @ResponseBody List<CommentDTO> save(@ModelAttribute CommentDTO commentDTO){
        System.out.println("commentDTO = " + commentDTO);
        commentService.save(commentDTO);
        // ----save가 끝나면 list가져와서 출력
        // findAll() 기준 해당 게시글에 작성된 댓글 리스트를 가져옴
        List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
        return commentDTOList;
    }

    @PostMapping("/delete")
    public @ResponseBody List<CommentDTO> delete(@RequestParam("id") Long id, @RequestParam("boardId") Long boardId) {
        commentService.delete(id);
        //삭제후 삭제된 댓글 제회하고 반환해야하므로
        List<CommentDTO> commentDTOList = commentService.findAll(boardId);
        return commentDTOList;
    }
}
