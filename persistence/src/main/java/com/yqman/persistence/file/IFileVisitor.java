package com.yqman.persistence.file;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 抽象的文件访问其
 */
public interface IFileVisitor {
    /**
     * 写字符串
     * @param isAppendMode 是否是追加到文件的末尾
     */
    void writeString(String value, boolean isAppendMode) throws FileAccessErrException;

    /**
     * 写字符串到新行
     * @param isAppendMode 是否是追加到文件的末尾
     */
    void writeStringNewLine(String value, boolean isAppendMode) throws FileAccessErrException;

    /**
     * 读取单行
     */
    String readNewLine() throws FileAccessErrException;

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
