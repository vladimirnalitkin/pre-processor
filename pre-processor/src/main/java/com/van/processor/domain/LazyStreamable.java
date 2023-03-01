package com.van.processor.domain;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Lazy implementation of {@link Streamable} obtains a {@link Stream} from a given {@link Supplier}.
 *
 * @author Oliver Gierke
 * @since 2.0
 */
class LazyStreamable<T> implements Streamable<T> {

    private final Supplier<? extends Stream<T>> stream;

    private LazyStreamable(Supplier<? extends Stream<T>> stream) {
        this.stream = stream;
    }

    public static <T> LazyStreamable<T> of(Supplier<? extends Stream<T>> stream) {
        return new LazyStreamable<T>(stream);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.util.Streamable#stream()
     */
    @Override
    public Stream<T> stream() {
        return stream.get();
    }
}
