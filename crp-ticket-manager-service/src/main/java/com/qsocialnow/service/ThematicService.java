package com.qsocialnow.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.persistence.ThematicRepository;

@Service
public class ThematicService {

    private static final Logger log = LoggerFactory.getLogger(ThematicService.class);

    @Autowired
    private ThematicRepository thematicRepository;

    public List<Thematic> findAll() {
        try {
            return thematicRepository.findAll();
        } catch (Exception e) {
            log.error("There was an error finding thematics", e);
            throw new RuntimeException(e);
        }
    }

    public List<CategoryGroup> findCategoryGroupsBySerieId(String thematicId, String serieId) {
        try {
            return thematicRepository.findCategoryGroupsBySerieId(Long.parseLong(thematicId), Long.parseLong(serieId));
        } catch (Exception e) {
            log.error("There was an error finding category groups", e);
            throw new RuntimeException(e);
        }
    }

}
