package com.github.ukase.client.model;

import java.util.EnumSet;
import java.util.Objects;

public class EnumUtils {

    public static <E extends Enum<E> & CodeEnum> E getByCode(Class<E> clazz, String code) {

        for (final E type : EnumSet.allOf(clazz)) {
            if ((type.getCode() == null && code == null)
                    || (type.getCode() != null && type.getCode().equals(code))) {
                return type;
            }
        }
        return null;
    }

    public static <E extends Enum<E> & CodeEnum> E getByEnumName(Class<E> clazz, String enumName) {

        for (final E type : EnumSet.allOf(clazz)) {
            if ((enumName == null)
                    || enumName.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return null;
    }

    public static <E extends Enum<E> & CodeEnum> E getByCodeWithDefault(Class<E> clazz, String code, E defaultValue) {
        E res = getByCode(clazz, code);
        if (Objects.isNull(res)) {
            res = defaultValue;
        }
        return res;
    }

    public static <E extends Enum<E> & CodeEnum> boolean isValidTypeCode(Class<E> clazz, String code) {
        return getByCode(clazz, code) != null;
    }

    public static <E extends Enum<E> & CodeEnum> boolean isValidByEnumName(Class<E> clazz, String enumName) {
        return getByEnumName(clazz, enumName) != null;
    }

}
