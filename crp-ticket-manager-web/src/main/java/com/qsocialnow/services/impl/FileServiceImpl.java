package com.qsocialnow.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.zkoss.util.media.Media;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.config.AmazonS3Config;
import com.qsocialnow.services.FileService;

@Service("fileService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private AmazonS3Config amazonS3Config;

    @Override
    public void upload(List<Media> files, Case caseObject) {
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(amazonS3Config)
                .withRegion(amazonS3Config.getRegion()).build();
        try {
            files.stream().forEach(media -> {
                File file = null;
                try {
                    file = createTempFile(media);
                    String folder = "centaurico/" + caseObject.getSource() + "/" + caseObject.getId() + "/";
                    s3.putObject(amazonS3Config.getBucketName(), folder + media.getName(), file);
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (file != null) {
                        file.delete();
                    }
                }
            });
        } catch (Exception e) {
            log.error("There was an error trying to upload file: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public File download(String file, Case caseObject) {
        File newFile = null;
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(amazonS3Config)
                .withRegion(amazonS3Config.getRegion()).build();
        try {
            String tDir = System.getProperty("java.io.tmpdir");
            newFile = File.createTempFile(tDir + file, "");
            String folder = "centaurico/" + caseObject.getSource() + "/" + caseObject.getId() + "/";
            S3Object object = s3.getObject(new GetObjectRequest(amazonS3Config.getBucketName(), folder + file));
            S3ObjectInputStream objectContent = object.getObjectContent();
            FileUtils.copyInputStreamToFile(objectContent, newFile);
        } catch (Exception e) {
            log.error("There was an error trying to upload file: " + e.getMessage(), e);
            if (newFile != null) {
                newFile.delete();
            }
            throw new RuntimeException(e);
        }
        return newFile;
    }

    private File createTempFile(Media file) {
        try {
            File newFile = File.createTempFile(file.getName(), null);
            FileUtils.writeByteArrayToFile(newFile, file.getByteData());
            return newFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
