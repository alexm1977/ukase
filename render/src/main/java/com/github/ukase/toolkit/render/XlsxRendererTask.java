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

public class XlsxRendererTask implements RenderTask {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(XlsxRendererTask.class);
    private final UkasePayload payload;
    private final Renderer<UkasePayload, String> htmlRenderer;
    private final Renderer<String, byte[]> xlsxRenderer;

    XlsxRendererTask(UkasePayload payload,
                     Renderer<UkasePayload, String> htmlRenderer,
                     Renderer<String, byte[]> xlsxRenderer) {
        this.payload = payload;
        this.htmlRenderer = htmlRenderer;
        this.xlsxRenderer = xlsxRenderer;
    }

    @Override
    public byte[] call() throws RenderException {
        try {
            log.debug("Start processing: {}", payload.getIndex());
            String html = htmlRenderer.render(payload);
            log.debug("Prepared xhtml:\n{}\n", html);
            byte[] renderedData = xlsxRenderer.render(html);
            log.debug("Processed successfully: {}", payload.getIndex());
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

    public Renderer<String, byte[]> getXlsxRenderer() {
        return this.xlsxRenderer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof XlsxRendererTask)) return false;
        final XlsxRendererTask other = (XlsxRendererTask) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$payload = this.getPayload();
        final Object other$payload = other.getPayload();
        if (this$payload == null ? other$payload != null : !this$payload.equals(other$payload)) return false;
        final Object this$htmlRenderer = this.getHtmlRenderer();
        final Object other$htmlRenderer = other.getHtmlRenderer();
        if (this$htmlRenderer == null ? other$htmlRenderer != null : !this$htmlRenderer.equals(other$htmlRenderer))
            return false;
        final Object this$xlsxRenderer = this.getXlsxRenderer();
        final Object other$xlsxRenderer = other.getXlsxRenderer();
        if (this$xlsxRenderer == null ? other$xlsxRenderer != null : !this$xlsxRenderer.equals(other$xlsxRenderer))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof XlsxRendererTask;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $payload = this.getPayload();
        result = result * PRIME + ($payload == null ? 43 : $payload.hashCode());
        final Object $htmlRenderer = this.getHtmlRenderer();
        result = result * PRIME + ($htmlRenderer == null ? 43 : $htmlRenderer.hashCode());
        final Object $xlsxRenderer = this.getXlsxRenderer();
        result = result * PRIME + ($xlsxRenderer == null ? 43 : $xlsxRenderer.hashCode());
        return result;
    }

    public String toString() {
        return "XlsxRendererTask(payload=" + this.getPayload() + ", htmlRenderer=" + this.getHtmlRenderer() + ", xlsxRenderer=" + this.getXlsxRenderer() + ")";
    }
}
