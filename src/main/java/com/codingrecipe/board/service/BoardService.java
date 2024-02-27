package com.codingrecipe.board.service;

import com.codingrecipe.board.dto.BoardDTO;
import com.codingrecipe.board.dto.PageDTO;
import com.codingrecipe.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    public int save(BoardDTO boardDTO) {
        return boardRepository.save(boardDTO);
    }

    public List<BoardDTO> findAll() {
        return boardRepository.findAll();
    }

    public BoardDTO findById(Long id) {
        return boardRepository.findById(id);
    }

    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public void delete(Long id) {
        boardRepository.delete(id);
    }

    public void update(BoardDTO boardDTO) {
        boardRepository.update(boardDTO);
    }

    int pageLimit = 3; // 한 페이지당 보여줄 글 개수
    int blockLimit = 3; // 하단에 보여줄 페이지 번호 개수
    public List<BoardDTO> pagingList(int page) {
        // 한 페이지에 보여지는 개수
        /*
        1페이지당 보여지는 글 개수 3개
            1 page => 0
            2 page => 3
            3 page => 6
         */
        int pagingStart = (page - 1) * pageLimit;
        // 2개 이상 서로 다른 객체를 보내야 할 경운 map을 이용하여 key와 value 값을 넘겨준다
        // 여러 곳에서 사용된다하면 DTO로 정의해서 넘겨주는 게 나을 수도 있다.
        Map<String, Integer> pagingParams = new HashMap<>();
        pagingParams.put("start", pagingStart); // 어디서 부터 출력되는 지 시작점, 1page면 0부터 시작이니까 가장 최근꺼부터 출력
        pagingParams.put("limit", pageLimit); //출력되는 개수(어디까지 출력이 되는지)
        List<BoardDTO> pagingList = boardRepository.pagingList(pagingParams);
        // soutv 단축키
        System.out.println("pagingList = " + pagingList);
        return pagingList;
    }

    public PageDTO pagingParam(int page) {
        //페이지 하단에 [이전] 1 2 3 [다음] 과 같은 페이지 번호 부여하는 부분
        // 전체 글 개수 조회
        int boardCount = boardRepository.boardCount();
        // 전체 페이지 개수 계산(10/3=3.33333 => 4)
        int maxPage = (int) (Math.ceil((double) boardCount / pageLimit));
        // 시작 페이지 값 계산(1, 4, 7, 10, ~~~~)
        int startPage = (((int)(Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        // 끝 페이지 값 계산(3, 6, 9, 12, ~~~~)
        int endPage = startPage + blockLimit - 1;
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setMaxPage(maxPage);
        pageDTO.setStartPage(startPage);
        pageDTO.setEndPage(endPage);
        return pageDTO;
    }
}
