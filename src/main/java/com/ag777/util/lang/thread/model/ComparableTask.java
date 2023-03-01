package com.ag777.util.lang.thread.model;

import java.util.concurrent.Callable;

/**
 * 同时继承Callable和Comparable的接口
 * @author ag777 <837915770@vip.qq.com>
 * Time: created at 2023/3/1. last modify at 2023/3/1.
 */
public interface ComparableTask<E, T extends ComparableTask<E, T>> extends Comparable<T>, Callable<E> {
}
