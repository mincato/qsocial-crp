package com.qsocialnow.services;

import java.util.List;

public interface AutocompleteService<T> {

    List<T> findBy(String query, int maxRows, Object parameters);

}
