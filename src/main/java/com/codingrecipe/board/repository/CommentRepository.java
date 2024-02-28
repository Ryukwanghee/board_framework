package com.codingrecipe.board.repository;

import com.codingrecipe.board.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

    // @RequiredArgsConstructor => 얘는 생성자 주입 방식이고 Autowired는 필드 주입 방식, 둘다 의존성 주입
    @Autowired
    private SqlSessionTemplate sql;

    public void save(CommentDTO commentDTO) {
        sql.insert("Comment.save", commentDTO);
    }

    public List<CommentDTO> findAll(Long boardId) {
        return sql.selectList("Comment.findAll", boardId);
    }

    public void delete(Long id) {
        sql.delete("Comment.delete", id);
    }
}
