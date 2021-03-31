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

import com.github.jknack.handlebars.Helper;
import com.github.ukase.toolkit.jar.JarSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.*;

@Service
public class CompoundSource {
    private final Collection<String> fontsUrls;
    private final JarSource jarSource;
    private final List<Source> sources;

    @Autowired
    public CompoundSource(List<Source> sources) {
        this.sources = new ArrayList<>(sources);
        this.sources.sort(new SourceComparator());

        this.jarSource = getSource(JarSource.class);
        sources = new ArrayList<>(3);

        Collection<String> fonts = new HashSet<>();
        for (Source source : sources) {
            fonts.addAll(source.getFontsUrls());
        }
        fonts.addAll(getDefaultFonts());
        this.fontsUrls = Collections.unmodifiableCollection(fonts);
    }

    public InputStream getResource(String url) {
        return sources.stream()
                .filter(s -> s.hasResource(url))
                .map(s -> s.getResource(url))
                .findFirst()
                .orElse(null);
    }

    public boolean hasResource(String url) {
        for (Source source : sources) {
            if (source.hasResource(url)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Helper<?>> getHelpers() {
        if (jarSource != null) {
            return jarSource.getHelpers();
        }
        return Collections.emptyMap();
    }

    String getDefaultFontUrl() {
        return getFontsUrls().stream()
                .filter(this::isRegularFont)
                .findAny().orElse(null);
    }

    private Collection<String> getDefaultFonts() {
        ClassLoader loader = getClass().getClassLoader();
        return Stream.of(
                "LiberationSans-Bold.ttf",
                "LiberationSans-BoldItalic.ttf",
                "LiberationSans-Italic.ttf",
                "LiberationSans-Regular.ttf",
                "LiberationSerif-Regular.ttf",
                "LiberationSerif-Bold.ttf",
                "LiberationSerif-Italic.ttf",
                "LiberationSerif-BoldItalic.ttf",
                "Roboto-Regular.ttf",
                "Roboto-Bold.ttf",
                "Roboto-Italic.ttf",
                "Roboto-BoldItalic.ttf")
                .map(loader::getResource)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    private boolean isRegularFont(String fontName) {
        String name = fontName.toLowerCase();
        return !(name.contains("bold") || name.contains("italic"));
    }

    public Collection<String> getFontsUrls() {
        return this.fontsUrls;
    }

    private static class SourceComparator implements Comparator<Source> {
        @Override
        public int compare(Source o1, Source o2) {
            return o1.order() - o2.order();
        }
    }

    private <T extends Source> T getSource(Class<T> tClass) {
        return this.sources.stream().filter(tClass::isInstance).map(tClass::cast).findAny().orElse(null);
    }
}
