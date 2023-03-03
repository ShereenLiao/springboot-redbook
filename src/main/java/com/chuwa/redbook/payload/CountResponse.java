package com.chuwa.redbook.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountResponse {
    private Long userId;
    private String userName;
    private Integer count;
}
