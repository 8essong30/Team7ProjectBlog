package com.sparta.blog.dto.request;

import lombok.Getter;

@Getter
public class PageDTO {

    private int page;
    private int size;
    private String sortBy;
    private boolean isAsc;
}
