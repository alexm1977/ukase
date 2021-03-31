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

package com.github.ukase.toolkit.helpers.barcode;

import com.github.jknack.handlebars.Options;
import com.github.ukase.toolkit.helpers.AbstractHandlebarsHelper;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.*;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.github.ukase.toolkit.helpers.barcode.BarcodeCommons.*;

@Component
public class BarcodeHandlebarsHelper extends AbstractHandlebarsHelper<Object> {
    private static final Map<BarcodeFormat, Writer> WRITERS = new EnumMap<>(BarcodeFormat.class);
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BarcodeHandlebarsHelper.class);

    static {
        WRITERS.put(BarcodeFormat.ITF, new ITFWriter());
        WRITERS.put(BarcodeFormat.QR_CODE, new QRCodeWriter());
        WRITERS.put(BarcodeFormat.CODE_128, new Code128Writer());
        WRITERS.put(BarcodeFormat.DATA_MATRIX, new DataMatrixWriter());
    }

    public BarcodeHandlebarsHelper() {
        super("$barcode");
    }

    @Override
    public Object apply(Object context, Options options) throws IOException {
        if (context == null) {
            return "";
        }

        String encoding = options.hash("encoding", StandardCharsets.UTF_8.name());
        String data = convertData(context.toString(), encoding);
        int width = options.hash("width", 100);
        int height = options.hash("height", 30);
        BarcodeFormat format = options.hash("format", BarcodeFormat.ITF);
        Writer writer = WRITERS.get(format);

        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hintMap.put(EncodeHintType.CHARACTER_SET, encoding);

        try {
            BitMatrix byteMatrix = writer.encode(data, format, width, height, hintMap);
            return bitMatrixToImage(byteMatrix);
        } catch (WriterException e) {
            log.error("Can't encode to barcode format!", e);
        } catch (IOException e) {
            log.error("Can't write image!", e);
        }
        return "";
    }
}
