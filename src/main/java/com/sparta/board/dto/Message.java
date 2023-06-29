package com.sparta.board.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Message {
    private StatusEnum status;
    private String message;
    private Object data;

    public Message() {
        this.status = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }

    public Message(StatusEnum status, String message, Object data) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
