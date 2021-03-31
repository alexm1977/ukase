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

package com.github.ukase.toolkit.render;

import com.github.ukase.model.UkasePayload;
import com.github.ukase.service.Renderer;
import org.slf4j.Logger;

class PdfRenderTask implements RenderTask {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PdfRenderTask.class);
    private final UkasePayload payload;
    private final Renderer<UkasePayload, String> htmlRenderer;
    private final Renderer<String, byte[]> pdfRenderer;
    private final Renderer<byte[], byte[]> watermarkRenderer;

    PdfRenderTask(UkasePayload payload,
                  Renderer<UkasePayload, String> htmlRenderer,
                  Renderer<String, byte[]> pdfRenderer,
                  Renderer<byte[], byte[]> watermarkRenderer) {
        this.payload = payload;
        this.htmlRenderer = htmlRenderer;
        this.pdfRenderer = pdfRenderer;
        this.watermarkRenderer = watermarkRenderer;
    }

    @Override
    public byte[] call() throws RenderException {
        try {
            log.debug("Start processing: {}", payload.getIndex());
            String html = htmlRenderer.render(payload);
            log.debug("Prepared xhtml:\n{}\n", html);
            byte[] renderedData = pdfRenderer.render(html);
            log.debug("Processed successfully: {}", payload.getIndex());
            if (payload.isSample()) {
                renderedData = watermarkRenderer.render(renderedData);
                log.debug("Processed sample watermark for: {}", payload.getIndex());
            }
            return renderedData;
        } catch (RenderException e) {
            e.setPayload(payload);
            throw e;
        }
    }

    @Override
    public String getTemplateName() {
        return payload.getIndex();
    }

    public UkasePayload getPayload() {
        return this.payload;
    }

    public Renderer<UkasePayload, String> getHtmlRenderer() {
        return this.htmlRenderer;
    }

    public Renderer<String, byte[]> getPdfRenderer() {
        return this.pdfRenderer;
    }

    public Renderer<byte[], byte[]> getWatermarkRenderer() {
        return this.watermarkRenderer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PdfRenderTask)) return false;
        final PdfRenderTask other = (PdfRenderTask) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$payload = this.getPayload();
        final Object other$payload = other.getPayload();
        if (this$payload == null ? other$payload != null : !this$payload.equals(other$payload)) return false;
        final Object this$htmlRenderer = this.getHtmlRenderer();
        final Object other$htmlRenderer = other.getHtmlRenderer();
        if (this$htmlRenderer == null ? other$htmlRenderer != null : !this$htmlRenderer.equals(other$htmlRenderer))
            return false;
        final Object this$pdfRenderer = this.getPdfRenderer();
        final Object other$pdfRenderer = other.getPdfRenderer();
        if (this$pdfRenderer == null ? other$pdfRenderer != null : !this$pdfRenderer.equals(other$pdfRenderer))
            return false;
        final Object this$watermarkRenderer = this.getWatermarkRenderer();
        final Object other$watermarkRenderer = other.getWatermarkRenderer();
        if (this$watermarkRenderer == null ? other$watermarkRenderer != null : !this$watermarkRenderer.equals(other$watermarkRenderer))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PdfRenderTask;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $payload = this.getPayload();
        result = result * PRIME + ($payload == null ? 43 : $payload.hashCode());
        final Object $htmlRenderer = this.getHtmlRenderer();
        result = result * PRIME + ($htmlRenderer == null ? 43 : $htmlRenderer.hashCode());
        final Object $pdfRenderer = this.getPdfRenderer();
        result = result * PRIME + ($pdfRenderer == null ? 43 : $pdfRenderer.hashCode());
        final Object $watermarkRenderer = this.getWatermarkRenderer();
        result = result * PRIME + ($watermarkRenderer == null ? 43 : $watermarkRenderer.hashCode());
        return result;
    }

    public String toString() {
        return "PdfRenderTask(payload=" + this.getPayload() + ", htmlRenderer=" + this.getHtmlRenderer() + ", pdfRenderer=" + this.getPdfRenderer() + ", watermarkRenderer=" + this.getWatermarkRenderer() + ")";
    }
}
