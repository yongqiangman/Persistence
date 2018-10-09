/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yqman.persistence.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class LocalDirectory implements IDirectoryVisitor {

    private File mFile;

    public LocalDirectory(File file) throws FileAccessErrException {
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new FileAccessErrException("file is not directory");
            }
            mFile = file;
        } else {
            if (file.getParentFile().mkdirs()) {
                mFile = file;
            } else {
                throw new FileAccessErrException("mkdirs failed");
            }
        }
    }

    @Override
    public IFileVisitor createNewFile(String displayName) throws FileAccessErrException {
        try {
            File file = new File(mFile, displayName);
            if (file.exists() || file.createNewFile()) {
                return new LocalFile(file);
            }
            return null;
        } catch (IOException e) {
            throw new FileAccessErrException(e.getMessage());
        }
    }

    @Override
    public boolean copyFile(IFileVisitor sourceFile, IDirectoryVisitor targetDir) throws FileAccessErrException {
        File newFile = new File(targetDir.getIdentifier(), sourceFile.getIdentifier());
        try {
            if (newFile.exists() || newFile.createNewFile()) {
                IFileVisitor targetFile = new LocalFile(newFile);
                copyData(sourceFile, targetFile);
                return true;
            }
        } catch (IOException e) {
            throw new FileAccessErrException(e.getMessage());
        }
        return false;
    }

    private void copyData(IFileVisitor sourceFile, IFileVisitor targetFile) throws FileAccessErrException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sourceFile.getOutputStream(false)));
        BufferedReader reader = new BufferedReader(new InputStreamReader(targetFile.getInputStream()));
        try {
            int data = reader.read();
            while (data != -1) {
                writer.write(data);
                data = reader.read();
            }
        } catch (IOException e) {
            throw new FileAccessErrException(e.getMessage());
        } finally {
            try {
                writer.close();
                reader.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    @Override
    public boolean moveFile(IFileVisitor sourceFile, IDirectoryVisitor targetDir) {
        File rawFile = new File(mFile, sourceFile.getIdentifier());
        if (rawFile.exists()) {
            File newFile = new File(targetDir.getIdentifier(), sourceFile.getIdentifier());
            return rawFile.renameTo(newFile);
        }
        return false;
    }

    @Override
    public String getIdentifier() {
        return mFile.getAbsolutePath();
    }

    @Override
    public String getDisplayName() {
        return mFile.getName();
    }

    @Override
    public long getMTime() {
        return mFile.lastModified();
    }

    @Override
    public IDirectoryVisitor getParent() {
        try {
            return new LocalDirectory(mFile.getParentFile());
        } catch (FileAccessErrException e) {
            return null;
        }
    }

    @Override
    public boolean deleteFile(IFileVisitor sourceFile) {
        File file = new File(mFile, sourceFile.getIdentifier());
        return file.delete();
    }

    @Override
    public ArrayList<IFileVisitor> listFiles() {
        ArrayList<IFileVisitor> fileVisitors = new ArrayList<>();
        for (File file : mFile.listFiles()) {
            if (!file.isDirectory()) {
                try {
                    fileVisitors.add(new LocalFile(file));
                } catch (IOException e) {
                    // do not handle
                }
            }
        }
        return fileVisitors;
    }

    @Override
    public ArrayList<IDirectoryVisitor> listDirectories() {
        ArrayList<IDirectoryVisitor> directoryVisitors = new ArrayList<>();
        for (File file : mFile.listFiles()) {
            if (file.isDirectory()) {
                try {
                    directoryVisitors.add(new LocalDirectory(file));
                } catch (FileAccessErrException e) {
                    // do not handle
                }
            }
        }
        return directoryVisitors;
    }
}
