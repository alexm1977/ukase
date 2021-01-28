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

package com.github.ukase.toolkit.pdf;

import com.github.ukase.toolkit.CompoundSource;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.resource.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

class WrappedUserAgentCallback implements UserAgentCallback {
    private static final Logger log = LoggerFactory.getLogger(WrappedUserAgentCallback.class);

    private final UserAgentCallback delegate;
    private final CompoundSource source;
    private final int dotsPerPixel;

    WrappedUserAgentCallback(CompoundSource source, int dotsPerPixel, UserAgentCallback delegate) {
        this.source = source;
        this.dotsPerPixel = dotsPerPixel;
        this.delegate = delegate;
    }

    @Override
    public CSSResource getCSSResource(String uri) {
        if (source.hasResource(uri)) {
            // this stream will be read and closed in other context - at actual access in render process...
            // out of scope for resource provider
            return new CSSResource(source.getResource(uri));
        }
        return delegate.getCSSResource(uri);
    }

    @Override
    public ImageResource getImageResource(String uri) {
        if (source.hasResource(uri)) {
            try (InputStream stream = source.getResource(uri)) {
                Image image = Image.getInstance(StreamUtils.copyToByteArray(stream));
                scaleToOutputResolution(image);
                return new ImageResource(uri, new ITextFSImage(image));
            } catch (IOException | DocumentException e) {
                log.error("Cannot read image [" + uri + "]", e);
            }
        }
        return delegate.getImageResource(uri);
    }

    @Override
    public XMLResource getXMLResource(String uri) {
        if (source.hasResource(uri)) {
            try (InputStream stream = source.getResource(uri)) {
                return XMLResource.load(stream);
            } catch (IOException e) {
                log.error("Cannot read xml resource [" + uri + "]", e);
            }
        }
        return delegate.getXMLResource(uri);
    }

    @Override
    public byte[] getBinaryResource(String uri) {
        if (source.hasResource(uri)) {
            try (InputStream stream = source.getResource(uri)) {
                return StreamUtils.copyToByteArray(stream);
            } catch (IOException e) {
                log.error("Cannot map resource to byte array [" + uri + "]", e);
            }
        }
        return delegate.getBinaryResource(uri);
    }

    @Override
    public boolean isVisited(String uri) {
        return delegate.isVisited(uri);
    }

    @Override
    public void setBaseURL(String url) {
        delegate.setBaseURL(url);
    }

    @Override
    public String getBaseURL() {
        return delegate.getBaseURL();
    }

    @Override
    public String resolveURI(String uri) {
        URI resolvingUri = transformUri(uri);
        if (resolvingUri == null) {
            return null;
        }
        if (resolvingUri.isAbsolute()) {
            return resolvingUri.toString();
        }

        return resolveUri(resolvingUri);
    }

    private String resolveUri(URI resolvingUri) {
        URI baseURI = transformUri(getBaseURL());
        if (baseURI == null) {
            return resolvingUri.toString();
        }
        return baseURI.resolve(resolvingUri).toString();
    }

    private URI transformUri(String uri) {
        if (uri == null) {
            return null;
        }

        URI resolvingUri;
        try {
            if (uri.startsWith("/")) {
                uri = uri.substring(1);
            }
            resolvingUri = new URI(uri);
        } catch (URISyntaxException e) {
            log.warn("Incorrect uri transferred to resolver", e);
            return null;
        }
        return resolvingUri;
    }

    private void scaleToOutputResolution(Image image) {
        float factor = dotsPerPixel;
        if (factor != 1.0f) {
            image.scaleAbsolute(image.getPlainWidth() * factor, image.getPlainHeight() * factor);
        }
    }
}
