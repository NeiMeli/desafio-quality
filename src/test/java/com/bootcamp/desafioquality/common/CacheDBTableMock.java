package com.bootcamp.desafioquality.common;

import com.bootcamp.desafioquality.entity.Persistable;
import com.bootcamp.desafioquality.repository.CacheDBTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class CacheDBTableMock<K, V extends Persistable<K>> extends CacheDBTable<K, V> {
    public CacheDBTableMock(List<V> elements) {
        super();
        elements.forEach(e -> table.put(e.getPrimaryKey(), e));
    }

    @Override
    protected @NotNull K generateNextId() {
        throw new RuntimeException("Not implemented");
    }
}
