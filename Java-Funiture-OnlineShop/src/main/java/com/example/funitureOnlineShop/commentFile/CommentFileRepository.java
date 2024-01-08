package com.example.funitureOnlineShop.commentFile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentFileRepository extends JpaRepository<CommentFile, Long> {
    void deleteByProductComment_id(Long id);

    List<CommentFile> findAllByProductCommentId(Long id);
}
