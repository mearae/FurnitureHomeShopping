package com.example.funitureOnlineShop.board;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;

    private Long userId;

    private String title;

    private String contents;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    public Board toEntity(){
        return Board.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .createTime(createTime)
                .updateTime(updateTime)
                .build();
    }
    public static BoardDTO toBoardDTO(Board board){
        return new BoardDTO(
                board.getId(),
                board.getUser().getId(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime());
    }

}
