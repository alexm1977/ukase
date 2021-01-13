package com.github.ukase.toolkit.pdf;

import com.lowagie.text.Image;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.*;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import java.io.InputStream;
import java.util.Base64;

public class MediaReplacedElementFactory implements ReplacedElementFactory {

    private static String IMG_TAG = "img";
    private static String IMG_CLASS = "media";
    private static String IMG_ATTR = "src";
    private static String IMG_STRING = "data:image/png;base64, ";

    private static String EXCEPTION_MSG = String.join("", "An element with class `", IMG_CLASS, "` is missing a `", IMG_ATTR, "` attribute indicating the media date.");

    private final ReplacedElementFactory superFactory;

    public MediaReplacedElementFactory(ReplacedElementFactory superFactory) {
        this.superFactory = superFactory;
    }

    @Override
    public ReplacedElement createReplacedElement(LayoutContext layoutContext,
                                                 BlockBox blockBox,
                                                 UserAgentCallback userAgentCallback,
                                                 int cssWidth,
                                                 int cssHeight) {
        Element element = blockBox.getElement();
        if (element == null) {
            return null;
        }
        var nodeName = element.getNodeName();
        var className = element.getAttribute("class");
        // Replace any <div class="media" data-src="image.png" /> with the
        // binary data of `image.png` into the PDF.
        if (IMG_TAG.equals(nodeName) && IMG_CLASS.equals(className)) {
            if (!element.hasAttribute(IMG_ATTR)) {
                throw new RuntimeException(EXCEPTION_MSG);
            }

            try {
                var img_data_str = element.getAttribute(IMG_ATTR).replace(IMG_STRING, "");
                byte[] img_data_byte = Base64.getDecoder().decode(img_data_str);

                /*input = new FileInputStream("/base/folder/" + element.getAttribute("data-src"));
                final byte[] bytes = IOUtils.toByteArray(input);*/

                Image image = Image.getInstance(img_data_byte);
                FSImage fsImage = new ITextFSImage(image);
                if ((cssWidth != -1) || (cssHeight != -1)) {
                    fsImage.scale(cssWidth, cssHeight);
                }
                return new ITextImageElement(fsImage);
            } catch (Exception e) {
                throw new RuntimeException("There was a problem trying to read a template embedded graphic.", e);
            }
        }
        return this.superFactory.createReplacedElement(layoutContext, blockBox, userAgentCallback, cssWidth, cssHeight);
    }

    @Override
    public void reset() {
        this.superFactory.reset();
    }

    @Override
    public void remove(Element e) {
        this.superFactory.remove(e);
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {
        this.superFactory.setFormSubmissionListener(listener);
    }
}