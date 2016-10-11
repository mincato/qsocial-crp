package com.qsocialnow.persistence;

import java.util.List;

import com.qsocialnow.common.model.config.Thematic;

public interface ThematicRepository {

    List<Thematic> findAll();

}
