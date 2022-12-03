package com.FileExplorer.interfaces;

import org.springframework.data.domain.Sort;

import java.util.List;

public interface Slice<T> {
    int getNumber();

    int getSize();

    int getNumberOfElements();

    List<T> getContent();

    boolean hasContent();

    Sort getSort();

    boolean isFirst();

    boolean isLast();

    boolean hasNext();

    boolean hasPrevious();
}
