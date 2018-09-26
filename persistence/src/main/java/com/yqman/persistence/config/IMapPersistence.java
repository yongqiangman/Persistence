package com.yqman.persistence.config;

public interface IMapPersistence {
    short getShort(String key);
    int getInt(String key);
    long getLong(String key);
    float getFloat(String key);
    double getDouble(String key);
    char getChar(String key);
    String getString(String key);
    short putShort(String key, short value);
    int putInt(String key, int value);
    long putLong(String key, long value);
    float putFloat(String key, float value);
    double putDouble(String key, double value);
    char putChar(String key, char value);
    String putString(String key, String value);
}
