package com.duongw.stayeasy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
public class PageResponse<T> implements Serializable {
    private int pageNo;
    private int pageSize;
    private long total;
    private T items;

}
