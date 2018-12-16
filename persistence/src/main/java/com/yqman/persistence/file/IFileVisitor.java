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

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 抽象的文件访问其
 */
public interface IFileVisitor {
    /**
     * 写字符串
     *
     * @param isAppendMode 是否是追加到文件的末尾
     */
    void writeString(String value, boolean isAppendMode) throws FileAccessErrException;

    /**
     * 写字符串到新行
     *
     * @param isAppendMode 是否是追加到文件的末尾
     */
    void writeStringNewLine(String value, boolean isAppendMode) throws FileAccessErrException;

    OutputStream getOutputStream(boolean isAppendMode) throws FileAccessErrException;

    InputStream getInputStream() throws FileAccessErrException;

    /**
     * 获取文件对应的uri标识，可能对应的就是文件的路径, 或者uri对应的String
     */
    String getIdentifier();

    String getDisplayName();

    long getSize();

    long getMTime();

    /**
     * 获取文件对应的目录
     */
    IDirectoryVisitor getDirectoryVisitor();
}
