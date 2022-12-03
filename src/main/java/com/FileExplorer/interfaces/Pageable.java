package com.FileExplorer.interfaces;

import com.FileExplorer.entity.Log;
import org.springframework.data.domain.Sort;

public interface Pageable {
    int getPageNumber();

    int getPageSize();

    long getOffset();

    Sort getSort();

    Pageable next();

    Pageable previousOrFirst();

    Pageable first();

    boolean hasPrevious();

    Page<Log> findAll(Pageable pageable);
    Page<Log> findByPublished(boolean published, Pageable pageable);
    Page<Log> findByTitleContaining(String title, Pageable pageable);
}
