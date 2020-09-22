package org.itcase.basic;

import java.util.Map;

/**
 * @Description: Map结构
 */
@FunctionalInterface
public interface MapData<K, V> {

    Map<K, V> getData();

}
