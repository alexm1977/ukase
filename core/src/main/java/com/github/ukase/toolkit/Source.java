/*
 * Copyright (c) 2018 Pavel Uvarov <pauknone@yahoo.com>
 *
 * This file is part of Ukase.
 *
 *  Ukase is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.ukase.toolkit;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

public interface Source {
    int ORDER_UPLOAD = 0;
    int ORDER_FS = 1;
    int ORDER_JAR = 2;

    Predicate<String> IS_FONT = fileName -> fileName.toLowerCase().endsWith("ttf");
    Predicate<String> IS_HELPERS_CONFIGURATION = fileName ->
            fileName.startsWith("imported-handlers") &&
                    fileName.endsWith(".properties");

    boolean hasResource(String url);

    InputStream getResource(String url);

    int order();

    default Collection<String> getFontsUrls() {
        return Collections.emptyList();
    }
}
