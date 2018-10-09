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

package com.yqman.persistence.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.yqman.persistence.file.IFileVisitor;
import com.yqman.persistence.util.RC4Util;

/**
 * Created by manyongqiang on 2018/2/2.
 */

public class Config {
    private static final String TAG = "Config";
    private final Properties mProperties = new Properties();
    private final boolean mIsNeedEncrypt;
    private final IFileVisitor mFile;

    public Config(boolean isNeedEncrypt, IFileVisitor file) {
        mIsNeedEncrypt = isNeedEncrypt;
        mFile = file;
    }

    public <T> void put(final String key, final T value) {
        if (value == null) {
            return;
        }
        if (load()) {
            mProperties.setProperty(key, String.valueOf(value));
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        if (load()) {
            return Boolean.parseBoolean(getString(key, String.valueOf(defValue)));
        } else {
            return defValue;
        }
    }

    public String getString(String key, String defValue) {
        if (load()) {
            return mProperties.getProperty(key, defValue);
        } else {
            return defValue;
        }
    }

    public int getInt(String key, int defValue) {
        if (load()) {
            return Integer.parseInt(mProperties.getProperty(key, String.valueOf(defValue)));
        } else {
            return defValue;
        }
    }

    public long getLong(String key, long defValue) {
        if (load()) {
            return Long.parseLong(mProperties.getProperty(key, String.valueOf(defValue)));
        } else {
            return defValue;
        }
    }

    public float getFloat(String key, float defValue) {
        if (load()) {
            return Float.parseFloat(mProperties.getProperty(key, String.valueOf(defValue)));
        } else {
            return defValue;
        }
    }

    public boolean has(String key) {
        if (key == null) {
            return false;
        }
        if (load()) {
            return mProperties.containsKey(key);
        } else {
            return false;
        }
    }

    public void remove(String key) {
        if (key != null) {
            if (load()) {
                mProperties.remove(key);
            }
        }
    }

    public void clear() {
        if (load()) {
            mProperties.clear();
        }
    }

    public void addAll(Properties properties) {
        if (load()) {
            mProperties.putAll(properties);
        }
    }

    public Set<Map.Entry<Object, Object>> getEntrySet() {
        if (load()) {
            return mProperties.entrySet();
        }
        return null;
    }

    public void commit() {
        store();
    }

    private boolean store() {
        OutputStream fos = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            synchronized(mProperties) {
                fos = mFile.getOutputStream(false);
                if (mIsNeedEncrypt) {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    mProperties.store(byteArrayOutputStream, "");
                    // RC4加密
                    fos.write(RC4Util.encrypt(byteArrayOutputStream.toByteArray()));
                } else {
                    mProperties.store(fos, "");
                }
                return true;
            }
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            try {
                if (null != byteArrayOutputStream) {
                    byteArrayOutputStream.close();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    private boolean load() {
        if (mProperties.size() == 0) {
            InputStream fis = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                fis = mFile.getInputStream();
                if (mIsNeedEncrypt) {
                    byteArrayOutputStream = new ByteArrayOutputStream(1024);
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = fis.read(bytes)) != -1) {
                        byteArrayOutputStream.write(bytes, 0, len);
                    }
                    byte[] buffer = byteArrayOutputStream.toByteArray();
                    if (null == buffer) {
                        return false;
                    }
                    mProperties.load(new ByteArrayInputStream(RC4Util.decrypt(buffer)));
                } else {
                    mProperties.load(fis);
                }
                return true;
            } catch (FileNotFoundException ex) {
                return false;
            } catch (IOException ex) {
                return false;
            } catch (ExceptionInInitializerError ex) {
                return false;
            } finally {
                try {
                    if (null != fis) {
                        fis.close();
                    }
                    if (null != byteArrayOutputStream) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException ex) {
                    // do nothing
                }
            }
        }
        return true;
    }
}
