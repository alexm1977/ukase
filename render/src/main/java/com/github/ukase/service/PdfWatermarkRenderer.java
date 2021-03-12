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

package com.github.ukase.service;

import com.github.ukase.toolkit.ResourceProvider;
import com.github.ukase.toolkit.render.RenderException;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;

@Service
public class PdfWatermarkRenderer implements Renderer<byte[], byte[]> {
    private final IWaterMarkSettings waterMark;
    private final Font font;

    @Autowired
    public PdfWatermarkRenderer(IWaterMarkSettings waterMark, ResourceProvider provider)
            throws IOException, DocumentException {
        this.waterMark = waterMark;
        BaseFont baseFont = BaseFont.createFont(provider.getDefaultFont(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        this.font = new Font(baseFont, waterMark.getSize(), 0, Color.LIGHT_GRAY);
    }

    @Override
    public byte[] render(byte[] data) throws RenderException {
        try {
            PdfReader reader = new PdfReader(data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Phrase phrase = new Phrase(waterMark.getText(), font);
            PdfStamper stamper = new PdfStamper(reader, baos);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                PdfContentByte canvas = stamper.getUnderContent(i);
                ColumnText.showTextAligned(canvas,
                                           Element.ALIGN_CENTER,
                                           phrase, waterMark.getX(),
                                           waterMark.getY(),
                                           waterMark.getDegree());
            }
            stamper.close();
            reader.close();
            return baos.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new RenderException("Cannot add watermark", e, "watermark");
        }
    }
}
