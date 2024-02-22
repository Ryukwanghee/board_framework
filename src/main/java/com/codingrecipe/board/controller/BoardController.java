package com.codingrecipe.board.controller;

import com.codingrecipe.board.dto.BoardDTO;
import com.codingrecipe.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor //의존성 주입
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService; //의존성 주입 받고
    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }

    @PostMapping("/save") //front에서 controller로 값(파라미터) 전달할때 서블릿에서 제공하는 RequestParam을 이용, but 항목많고 형식이 정해져있을땐
    // dto 생성하고 ModelAttribute사용=> save.jsp의 form을 전달하여 BoardDTO에 담음
    public String save(@ModelAttribute BoardDTO boardDTO) {
        //@ModelAttribute는 HTTP Body 내용과 HTTP 파라미터의 값들을 Getter, Setter, 생성자를 통해 주입하기 위해 사용한다.
        // 즉 요청파라미터를 받아서 필요한 객체에 값을 주입하는데 @RequestParam을 사용하면 하나씩 set으로 지정해줘서 저장시키지만 ModelAttribute를 사용하면 이 과정을 자동으로 시켜줌

        /* 예시
        * public void item(@RequestParam String name,
                 @RequestParam int price,
                 Model model){
                Item item = new Item();
                item.setName(name);
                item.setPrice(price);
                model.addAttribute("item", item);
            }
        *
        *
        *
        * */
        int saveResult = boardService.save(boardDTO);
        if (saveResult > 0) {
            return "redirect:/board/";
        } else {
            return "save";
        }
    }

    @GetMapping("/")
    public String findAll(Model model) { //db에서 뭔가를 가져가는 게 있느냐 없느냐에 따라 매개변수 유무 갈림. DB에서 뭔가를 가져가야하면 Model이라는 객체를 포함시켜줘야함
        //Model 객체는 Controller 에서 생성된 데이터를 담아 View 로 전달할 때 사용하는 객체이다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";
    }

    @GetMapping
    public String findById(@RequestParam("id") Long id, Model model){
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "detail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) { //DB쪽에서 꺼내는게 없으니까 model X
        boardService.delte(id);
        return "redirect:/board/";
    }

    @GetMapping("/update")
    public String updateForm(@RequestParam("id") Long id, Model model){ //수정 페이지에 기존에 저장되어있던 내용을 가져가야하니까 model이 필요
        //현재 DB에 저장된 정보를 가져와야함
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        boardService.update(boardDTO);
        BoardDTO dto = boardService.findById(boardDTO.getId());
        model.addAttribute("board", dto);
        return "detail";
        // 얘는 조회수를 더 올려버리는 코드가 있기때문에 얘는 생략 return "redirect:/board?id=" + boardDTO.getId();
    }
}
