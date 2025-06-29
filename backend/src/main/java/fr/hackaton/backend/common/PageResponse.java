package fr.hackaton.backend.common;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class PageResponse<T> {
    private final List<T> items;
    private final int currentPage;
    private final int totalPages;
    private final long totalItems;
    private final int pageSize;
    private final boolean hasNext;
    private final boolean hasPrevious;

    public PageResponse(Page<T> page) {
        this.items = page.getContent();
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.pageSize = page.getSize();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
    }

    
}