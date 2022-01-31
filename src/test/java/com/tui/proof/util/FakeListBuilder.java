package com.tui.proof.util;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class FakeListBuilder {

    public static <T> List<T> buildList(Supplier<T> supplier) {
        int size = 5;
        return IntStream.range(0, size).mapToObj( i -> supplier.get()).toList();
    }

    public static <T> List<T> buildList(int size, Supplier<T> supplier) {
        return IntStream.range(0, size).mapToObj( i -> supplier.get()).toList();
    }
}
