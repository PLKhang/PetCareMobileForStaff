package com.petcare.staff.utils;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CaseInsensitiveEnumAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<T> rawType = (Class<T>) typeToken.getRawType();
        if (!rawType.isEnum()) {
            return null;
        }

        // Tạo map ánh xạ: lowercase string -> enum constant
        Map<String, T> lookup = new HashMap<>();
        for (T constant : rawType.getEnumConstants()) {
            String name = constant.toString();
            lookup.put(name.toLowerCase(), constant); // theo tên enum

            // thêm @SerializedName nếu có
            try {
                Field field = rawType.getField(name);
                SerializedName annotation = field.getAnnotation(SerializedName.class);
                if (annotation != null) {
                    lookup.put(annotation.value().toLowerCase(), constant); // theo SerializedName
                }
            } catch (NoSuchFieldException ignored) {}
        }

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                try {
                    Field field = rawType.getField(value.toString());
                    SerializedName annotation = field.getAnnotation(SerializedName.class);
                    if (annotation != null) {
                        out.value(annotation.value());
                        return;
                    }
                } catch (NoSuchFieldException ignored) {}
                out.value(value.toString());
            }

            @Override
            public T read(JsonReader in) throws IOException {
                switch (in.peek()) {
                    case STRING:
                        String value = in.nextString();
                        T result = lookup.get(value.toLowerCase());
                        if (result == null) {
                            throw new JsonParseException("Unknown enum value: " + value + " for type " + rawType.getName());
                        }
                        return result;

                    case NUMBER:  // Nếu enum được gửi về là số (int)
                        int ordinal = in.nextInt();
                        T[] constants = rawType.getEnumConstants();
                        if (ordinal < 0 || ordinal >= constants.length) {
                            throw new JsonParseException("Invalid ordinal " + ordinal + " for enum " + rawType.getName());
                        }
                        return constants[ordinal];

                    default:
                        throw new JsonParseException("Unexpected token " + in.peek() + " when parsing enum " + rawType.getName());
                }
            }

        };
    }
}
