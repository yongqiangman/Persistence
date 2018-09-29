package com.yqman.persistence.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class LocalFile implements IFileVisitor {
    private File mFile;

    public LocalFile(File file) throws FileAccessErrException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new FileAccessErrException("file is directory");
            }
            mFile = file;
        } else {
            if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
                try {
                    if (file.createNewFile()) {
                        mFile = file;
                    } else {
                        throw new FileAccessErrException("mkFile failed");
                    }
                } catch (IOException e) {
                    throw new FileAccessErrException(e.getMessage());
                }
            } else {
                throw new FileAccessErrException("mkdirs failed");
            }
        }
    }

    @Override
    public void writeString(String value, boolean isAppendMode) throws FileAccessErrException {
        try {
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mFile, isAppendMode)));
            writer.write(value);
            writer.close();
        } catch (FileNotFoundException e) {
            throw new FileAccessErrException(e.getMessage());
        } catch (IOException e) {
            throw new FileAccessErrException(e.getMessage());
        }
    }

    @Override
    public void writeStringNewLine(String value, boolean isAppendMode) throws FileAccessErrException {
        try {
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mFile, isAppendMode)));
            writer.newLine();
            writer.write(value);
            writer.close();
        } catch (FileNotFoundException e) {
            throw new FileAccessErrException(e.getMessage());
        } catch (IOException e) {
            throw new FileAccessErrException(e.getMessage());
        }
    }

    @Override
    public String readNewLine() throws FileAccessErrException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(mFile)));
            String value = reader.readLine();
            reader.close();
            return value;
        } catch (FileNotFoundException e) {
            throw new FileAccessErrException(e.getMessage());
        } catch (IOException e) {
            throw new FileAccessErrException(e.getMessage());
        }
    }

    @Override
    public OutputStream getOutputStream(boolean isAppendMode) throws FileAccessErrException {
        try {
            return new FileOutputStream(mFile, isAppendMode);
        } catch (FileNotFoundException e) {
            throw new FileAccessErrException(e.getMessage());
        }
    }

    @Override
    public InputStream getInputStream() throws FileAccessErrException {
        try {
            return new FileInputStream(mFile);
        } catch (FileNotFoundException e) {
            throw new FileAccessErrException(e.getMessage());
        }
    }

    @Override
    public String getIdentifier() {
        return mFile.getName();
    }

    @Override
    public String getDisplayName() {
        return mFile.getName();
    }

    @Override
    public long getSize() {
        return mFile.getTotalSpace();
    }

    @Override
    public long getMTime() {
        return mFile.lastModified();
    }

    @Override
    public IDirectoryVisitor getDirectoryVisitor() {
        try {
            return new LocalDirectory(mFile.getParentFile());
        } catch (FileAccessErrException e) {
            return null;
        }
    }
}
