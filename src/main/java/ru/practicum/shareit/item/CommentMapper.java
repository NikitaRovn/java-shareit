package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.CommentRegisterDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static Comment mapFromCommentToCommentDto(Comment comment) {
        return null;
    }

    public static Comment mapFromCommentRegisterDtoToComment(CommentRegisterDto commentRegisterDto) {
        User user = User.builder()
                .id(commentRegisterDto.getUserId())
                .build();
        Item item = Item.builder()
                .id(commentRegisterDto.getItemId())
                .build();
        return Comment.builder()
                .text(commentRegisterDto.getText())
                .item(item)
                .author(user)
                .create(LocalDateTime.now())
                .build();
    }
}
