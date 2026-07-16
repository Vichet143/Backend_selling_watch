package com.example.practice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationDTO {
    private int pageSize;
    private int pageNumber;
    private int totalPages;
    private int totalElements;
    private int numberOfElements;

    private boolean first;
    private boolean last;
    private boolean empty;
}
