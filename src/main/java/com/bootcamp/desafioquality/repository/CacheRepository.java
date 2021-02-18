package com.bootcamp.desafioquality.repository;


import com.bootcamp.desafioquality.entity.Persistable;

public interface CacheRepository<K, V extends Persistable<K>> {
    CacheDBTable<K, V> getDatabase();
}
