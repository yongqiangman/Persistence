package com.yqman.persistence.file;

import java.io.IOException;

public class FileAccessErrException extends IOException {

    public FileAccessErrException(String errMsg) {
        super(errMsg);
    }
}
