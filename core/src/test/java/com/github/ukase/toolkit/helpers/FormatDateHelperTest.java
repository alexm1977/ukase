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

package com.github.ukase.toolkit.helpers;

import com.github.jknack.handlebars.*;
import com.github.ukase.config.properties.FormatDateProperties;
import com.github.ukase.toolkit.helpers.datetime.FormatDateHelper;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormatDateHelperTest {
    private static final FormatDateProperties PROPERTIES = new FormatDateProperties();

    static {
        PROPERTIES.setDatePattern("^\\d+.\\d+.\\d+( \\d+:\\d+$)?");
        PROPERTIES.setParseFormat("dd.MM.yyyy[ HH:mm]");
        PROPERTIES.setDisablePatterns(false);
        PROPERTIES.setFormatDate("dd.MM.yyyy");
    }

    private static final FormatDateHelper HELPER = new FormatDateHelper(PROPERTIES);
    private static final String REASON_WRONG = "Wrong render";
    private static final Long LONG_DATE = 12312612341234L;
    private static final String STRING_DATE_TIME = "04.03.2360 05:05";
    private static final String STRING_DATE = "04.03.2360";

    @Test
    public void testNull() throws Exception {
        Options options = getOptions(null);
        Object result = HELPER.apply(null, options);
        assertNotNull(REASON_WRONG, result);
        assertEquals(REASON_WRONG, 0, result.toString().length());
    }

    @Test
    public void testNullGenerateMode() throws Exception {
        Options options = getOptions(null, FormatDateHelper.DATE_FORMAT);
        options.hash.put("mode", "generate");
        Object result = HELPER.apply(null, options);
        assertNotNull(REASON_WRONG, result);
        assertEquals(REASON_WRONG, STRING_DATE.length(), result.toString().length());
    }

    @Test
    public void testNullStrictMode() throws Exception {
        Options options = getOptions(null, FormatDateHelper.DATE_FORMAT);
        options.hash.put("mode", "strict");
        try {
            HELPER.apply(null, options);
            throw new IllegalStateException("should fail with IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //all ok
        }
    }

    @Test
    public void testNullCommonMode() throws Exception {
        Options options = getOptions(null, FormatDateHelper.DATE_FORMAT);
        Object result = HELPER.apply(null, options);
        assertNotNull(REASON_WRONG, result);
        assertEquals(REASON_WRONG, result, "");
    }

    @Test
    public void testLong() throws Exception {
        test(LONG_DATE, FormatDateHelper.DATE_FORMAT, STRING_DATE);
    }

    @Test
    public void testStringDateTime() throws Exception {
        test(STRING_DATE_TIME, FormatDateHelper.DATE_FORMAT, STRING_DATE);
    }

    @Test
    public void testStringDate() throws Exception {
        test(STRING_DATE, FormatDateHelper.DATE_FORMAT, STRING_DATE);
    }

    @Test
    public void testOffsetDateTime() throws Exception {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse("2018-12-11T13:46:10.629+03:00");
        String offsetDateTimeStr = "11.12.2018";
        test(offsetDateTime, FormatDateHelper.DATE_FORMAT, offsetDateTimeStr);
    }

    @Test
    public void testWrongString() throws Exception {
        String context = "dg213f43f";
        Options options = getOptions(context, FormatDateHelper.DATE_FORMAT);
        assertEquals(REASON_WRONG, "", HELPER.apply(context, options));
    }

    @Test
    public void testStringCustomFormat() throws Exception {
        String parseFormat = "yyyy.'['MM']'.dd' в 'HH:mm";//
        String context = "2360.[03].04 в 05:05";
        Options options = getOptions(context, FormatDateHelper.DATE_FORMAT);
        options.hash.put("parseFormat", parseFormat);
        assertEquals(REASON_WRONG, STRING_DATE, HELPER.apply(context, options));
    }

    private void test(Object context, String format, String result) throws Exception {
        Options options = getOptions(context, format);
        assertEquals(REASON_WRONG, result, HELPER.apply(context, options));
    }

    private Options getOptions(Object context, String... params) {
        return new Options(null,
                "format_date",
                TagType.VAR,
                Context.newContext(context),
                null,
                null,
                params,
                new HashMap<>(),
                Collections.emptyList());
    }
}