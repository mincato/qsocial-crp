package com.qsocialnow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeleteOnCloseInputStream extends FileInputStream {

    private File file;

    public DeleteOnCloseInputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void dismiss() throws IOException {
        file = null;
        super.close();
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            file.delete();
            file = null;
        }
    }
}
