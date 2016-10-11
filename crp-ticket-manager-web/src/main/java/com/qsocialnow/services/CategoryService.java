package com.qsocialnow.services;

import java.util.List;

import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.model.CategoryGroupBySerieIdInput;

public interface CategoryService {

    List<CategoryGroup> findBySerieId(CategoryGroupBySerieIdInput categoryGroupInput);

}
