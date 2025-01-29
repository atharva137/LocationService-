package com.ray.LocationService.repository;



public interface GenericCache<K,V> {

    V get(K key);
    void  put(K key, V value);
    void evict(K key);
}
