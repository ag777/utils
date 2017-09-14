package com.ag777.util.lang.model;

import java.util.Objects;

public class Pair<K,V> {

	public K first;
	public V second;
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second);
	}
}
