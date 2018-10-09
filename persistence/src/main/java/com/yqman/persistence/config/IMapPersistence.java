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
