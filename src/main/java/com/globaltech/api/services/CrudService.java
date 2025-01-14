package com.globaltech.api.services;

import java.util.List;

public interface CrudService <T, ID, DTO>  {
    List<T> findAll();
    T findById(ID id);
    T findByNome(String name);
    T create(DTO entity);
    T delete(ID id);
    T update(ID id, DTO entity);
}
