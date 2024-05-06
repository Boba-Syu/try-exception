package cn.bobasyu.te;

import java.util.Objects;

public abstract class Try<V> {

    private Try() {
    }

    public abstract Boolean isSuccess();

    public abstract Boolean isFailure();

    public abstract void throwException();

    public abstract Throwable getMessage();

    public abstract V get();

    public abstract <U> Try<U> map(CheckedFunction<? super V, ? extends U> f);

    public abstract <U> Try<U> flatMap(CheckedFunction<? super V, Try<U>> f);

    public static <V> Try<V> failure(Throwable t) {
        Objects.requireNonNull(t);
        return new Failure<>(t);
    }

    public static <V> Try<V> success(V value) {
        Objects.requireNonNull(value);
        return new Success<>(value);
    }

    public static <T> Try<T> failable(CheckedSupplier<T> f) {
        Objects.requireNonNull(f);

        try {
            return Try.success(f.get());
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    private static class Failure<V> extends Try<V> {

        private RuntimeException exception;

        public Failure(Throwable t) {
            super();
            this.exception = new RuntimeException(t);
        }

        @Override
        public Boolean isSuccess() {
            return false;
        }

        @Override
        public void throwException() {
            throw this.exception;
        }

        @Override
        public V get() {
            throw exception;
        }

        @Override
        public Boolean isFailure() {
            return true;
        }

        @Override
        public <U> Try<U> map(CheckedFunction<? super V, ? extends U> f) {
            Objects.requireNonNull(f);
            return Try.failure(exception);
        }

        @Override
        public <U> Try<U> flatMap(CheckedFunction<? super V, Try<U>> f) {
            Objects.requireNonNull(f);
            return Try.failure(exception);
        }

        @Override
        public Throwable getMessage() {
            return exception;
        }
    }

    private static class Success<V> extends Try<V> {

        private final V value;

        public Success(V value) {
            super();
            this.value = value;
        }

        @Override
        public Boolean isSuccess() {
            return true;
        }

        @Override
        public void throwException() {
            return;
        }

        @Override
        public V get() {
            return value;
        }

        @Override
        public Boolean isFailure() {
            return false;
        }

        @Override
        public <U> Try<U> map(CheckedFunction<? super V, ? extends U> f) {
            Objects.requireNonNull(f);
            try {
                return Try.success(f.apply(value));
            } catch (Throwable t) {
                return Try.failure(t);
            }
        }

        @Override
        public <U> Try<U> flatMap(CheckedFunction<? super V, Try<U>> f) {
            Objects.requireNonNull(f);
            try {
                return f.apply(value);
            } catch (Throwable t) {
                return Try.failure(t);
            }
        }

        @Override
        public Throwable getMessage() {
            throw new IllegalStateException("no messages when success");
        }
    }
}