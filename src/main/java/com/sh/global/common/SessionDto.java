package com.sh.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SessionDto {

    private String sessionId;
    private long createTime;
    private int validTime;
}
