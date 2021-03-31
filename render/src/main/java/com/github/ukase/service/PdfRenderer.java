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
import com.github.ukase.toolkit.pdf.PdfSaucerRenderer;
import com.github.ukase.toolkit.render.RenderException;
import com.lowagie.text.DocumentException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfRenderer implements Renderer<String, byte[]> {
    private static final Logger log = Logger.getLogger(PdfRenderer.class);
    private final ResourceProvider provider;

    @Autowired
    public PdfRenderer(ResourceProvider provider) {
        this.provider = provider;
    }

    public byte[] render(String html) throws RenderException {
        try {
            log.debug("Start rendering pdf from html with size of " + html.length());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfSaucerRenderer renderer = provider.getRenderer(html);

            log.debug("Renderer prepared");

            renderer.createPDF(baos);
            renderer.finishPDF();

            log.debug("pdf rendered");
            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new RenderException("Cannot render pdf", e, "pdf");
        }
    }
}
