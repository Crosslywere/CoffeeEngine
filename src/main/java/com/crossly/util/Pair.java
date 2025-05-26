package com.crossly.util;

/**
 * @author Jude Ogboru
 * @param <F> first type
 * @param <S> second type
 */
public class Pair<F, S> {
    private F first = null;
    private S second = null;

    public Pair() {
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Pair<?, ?> other) {
            return other.first.equals(this.first) && other.second.equals(second);
        }
        return false;
    }
}
