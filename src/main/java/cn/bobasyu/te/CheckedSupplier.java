package cn.bobasyu.te;

import java.io.IOException;

public interface CheckedSupplier<T> {
    T get() throws IOException;
}
