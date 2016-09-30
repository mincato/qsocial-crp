package com.qsocialnow.services;

import java.io.File;
import java.util.List;

import org.zkoss.util.media.Media;

import com.qsocialnow.common.model.cases.Case;

public interface FileService {

    void upload(List<Media> files, Case caseObject);

    File download(String file, Case caseObject);

}
