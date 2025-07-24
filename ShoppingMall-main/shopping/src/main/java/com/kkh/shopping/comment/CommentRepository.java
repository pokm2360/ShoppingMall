package com.kkh.shopping.comment;

import com.kkh.shopping.Member.Member;
import com.kkh.shopping.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> { //Item의 데이터타입

    List<Comment> findAllByParentId(Long parentId);
}
