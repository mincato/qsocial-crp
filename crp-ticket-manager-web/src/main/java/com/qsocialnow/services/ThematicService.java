package com.qsocialnow.services;

import java.util.List;

import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.Thematic;

public interface ThematicService {

    List<Thematic> findAll();

    List<CategoryGroup> findCategoriesBySerieId(Long thematicId, Long serieId);

}
