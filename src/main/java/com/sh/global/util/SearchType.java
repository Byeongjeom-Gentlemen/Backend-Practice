package com.sh.global.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchType {
    TITLE("title", "게시글 제목으로 검색"),
    WRITER("writer", "유저명으로 검색");

    private final String type;

    private final String description;

    public static SearchType convertToType(String stringType) {
        return Arrays.stream(values())
                .filter(searchType -> searchType.type.equals(stringType))
                .findAny()
                .orElse(null);
    }
}
