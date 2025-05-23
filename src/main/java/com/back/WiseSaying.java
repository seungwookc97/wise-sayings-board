package com.back;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WiseSaying{
    int id;
    String content;
    String author;

    WiseSaying(int id, String content, String author){
        this.id = id;
        this.content = content;
        this.author = author;
    }
}
