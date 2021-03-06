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

import java.util.ArrayList;

/**
 * 抽象的目录访问器
 */
public interface IDirectoryVisitor {
    /**
     * 当前目录中创建该文件
     *
     * @param displayName 文件名
     *
     * @return 返回可以访问的文件器
     */
    IFileVisitor createNewFile(String displayName) throws FileAccessErrException;

    /**
     * 复制文件到指定目录
     *
     * @param sourceFile 源文件
     * @param targetDir  目标目录
     *
     * @return 操作是否成功的标志位
     */
    boolean copyFile(IFileVisitor sourceFile, IDirectoryVisitor targetDir) throws FileAccessErrException;

    /**
     * 移动文件到指定目录
     *
     * @param sourceFile 源文件
     * @param targetDir  目标目录
     *
     * @return 操作是否成功的标志位
     */
    boolean moveFile(IFileVisitor sourceFile, IDirectoryVisitor targetDir) throws FileAccessErrException;

    /**
     * 当前目录下删除文件
     *
     * @param targetFile 需要删除的文件
     *
     * @return 操作是否成功的标志位
     */
    boolean deleteFile(IFileVisitor targetFile);

    /**
     * 获取文件对应的uri标识，可能对应的就是文件的路径, 或者uri对应的String
     */
    String getIdentifier();

    IDirectoryVisitor getParent();

    String getDisplayName();

    long getMTime();

    /**
     * 返回当前目录下所有文件
     */
    ArrayList<IFileVisitor> listFiles();

    /**
     * 返回当前目录下所有文件夹
     */
    ArrayList<IDirectoryVisitor> listDirectories();
}
