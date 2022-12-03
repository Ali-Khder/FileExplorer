package com.FileExplorer.interfaces;

import org.springframework.data.domain.Pageable;

import java.util.function.Function;

public interface Page<T> extends Slice<T> {
    <T> Page<T> empty();
    <T> Page<T> empty(Pageable pageable);
    long getTotalElements();
    int getTotalPages();
    <U> Page<U> map(Function<? super T,? extends U> converter);
}
