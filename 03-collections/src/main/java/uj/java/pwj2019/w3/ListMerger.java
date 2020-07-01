package uj.java.pwj2019.w3;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ListMerger {
    public static List<Object> mergeLists(List<Object> l1, List<Object> l2) {
        /* Pierwsze rozwiązanie:

        if(l1==null && l2==null) return Collections.unmodifiableList(Collections.emptyList());
        if(l1==null) return l2.stream().collect(Collectors.toUnmodifiableList());
        if(l2==null) return l1.stream().collect(Collectors.toUnmodifiableList());

        List<Object> l3=(
                l1.size()>l2.size() ?
                l1.subList(l2.size(), l1.size()) :
                l2.subList(l1.size(), l2.size())
        );

        return Stream.concat(
                IntStream.range(0, Math.min(l1.size(), l2.size()))
                .boxed()
                .collect(Collectors.toMap(l1::get, l2::get))
                .entrySet()
                .stream()
                .map(x -> List.of(x.getKey(), x.getValue()))
                .flatMap(List::stream)
                        ,
                l3.stream()
        ).collect(Collectors.toUnmodifiableList());

        */
        /* Drugie rozwiązanie:

        int size = Math.max(
                Optional.ofNullable(l1)
                        .orElseGet(Collections::emptyList)
                        .size(),
                Optional.ofNullable(l2)
                        .orElseGet(Collections::emptyList)
                        .size());

        return IntStream.range(0, size)
                .boxed()
                .map(i -> {
                    List<Object> pair = new LinkedList<>();

                    List<Object> fixedL1=Optional.ofNullable(l1)
                            .orElseGet(Collections::emptyList);
                    if(i < fixedL1.size())
                        pair.add(fixedL1.get(i));

                    List<Object> fixedL2=Optional.ofNullable(l2)
                            .orElseGet(Collections::emptyList);
                    if(i < fixedL2.size())
                        pair.add(fixedL2.get(i));
                    return pair;
                })
                .flatMap(List::stream)
                .collect(Collectors.toUnmodifiableList());

        */
        //* Trzecie rozwiązanie:

        if(l1==null && l2==null) return Collections.unmodifiableList(Collections.emptyList());
        if(l1==null) return l2.stream().collect(Collectors.toUnmodifiableList());
        if(l2==null) return l1.stream().collect(Collectors.toUnmodifiableList());

        List<Object> l3=(
                l1.size()>l2.size() ?
                l1.subList(l2.size(), l1.size()) :
                l2.subList(l1.size(), l2.size())
        );

        return Stream.concat(
                IntStream.range(0, Math.min(l1.size(), l2.size()))
                .boxed()
                .map(i -> List.of(l1.get(i), l2.get(i)))
                .flatMap(List::stream)
                ,
                l3.stream()
        ).collect(Collectors.toUnmodifiableList());
    }
}
