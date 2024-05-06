package cn.bobasyu.te;

public interface CheckedFunction<U, V> {
    V apply(U u) throws Exception;
}
