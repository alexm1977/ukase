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

package com.github.ukase.toolkit.xlsx;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.xhtmlrenderer.css.parser.*;

import java.util.function.Supplier;

import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;

public class CellStyleKey {
    private BorderStyle borderTop = BorderStyle.NONE;
    private BorderStyle borderRight = BorderStyle.NONE;
    private BorderStyle borderBottom = BorderStyle.NONE;
    private BorderStyle borderLeft = BorderStyle.NONE;
    private HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;
    private VerticalAlignment verticalAlignment = VerticalAlignment.BOTTOM;
    private boolean wordWrap;
    private FSColor backgroundColor;
    private Boolean bold;
    private Short fontSize;
    private short format;

    public CellStyleKey() {
    }

    void applyToStyle(XSSFCellStyle style, Supplier<Font> fontSupplier) {
        applyBorders(style);
        applyAlignment(style);
        applyFont(style, fontSupplier);
        applyTextWrap(style);
        applyBackgroundColor(style);
        style.setDataFormat(format);
    }

    private void applyBackgroundColor(XSSFCellStyle style) {
        if (backgroundColor instanceof FSRGBColor) {
            FSRGBColor rgbColor = (FSRGBColor) backgroundColor;
            byte[] colors = new byte[3];
            colors[0] = (byte) rgbColor.getRed();
            colors[1] = (byte) rgbColor.getGreen();
            colors[2] = (byte) rgbColor.getBlue();

            XSSFColor xlsxColor = new XSSFColor(colors, new DefaultIndexedColorMap());
            style.setFillPattern(SOLID_FOREGROUND);
            style.setFillForegroundColor(xlsxColor);
        }
    }

    private void applyTextWrap(XSSFCellStyle style) {
        style.setWrapText(wordWrap);
        if (wordWrap) {
            style.setShrinkToFit(true);
        }
    }

    private void applyFont(XSSFCellStyle style, Supplier<Font> fontSupplier) {
        if ((bold == null || !bold) && fontSize == null) {
            return;
        }
        Font font = fontSupplier.get();
        if (bold) {
            font.setBold(bold);
        }
        if (fontSize != null) {
            font.setFontHeightInPoints(fontSize);
        }
        style.setFont(font);
    }

    private void applyAlignment(XSSFCellStyle style) {
        style.setAlignment(horizontalAlignment);
        style.setVerticalAlignment(verticalAlignment);
    }

    private void applyBorders(XSSFCellStyle style) {
        style.setBorderTop(borderTop);
        style.setBorderRight(borderRight);
        style.setBorderBottom(borderBottom);
        style.setBorderLeft(borderLeft);
    }

    public BorderStyle getBorderTop() {
        return this.borderTop;
    }

    public BorderStyle getBorderRight() {
        return this.borderRight;
    }

    public BorderStyle getBorderBottom() {
        return this.borderBottom;
    }

    public BorderStyle getBorderLeft() {
        return this.borderLeft;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return this.verticalAlignment;
    }

    public boolean isWordWrap() {
        return this.wordWrap;
    }

    public FSColor getBackgroundColor() {
        return this.backgroundColor;
    }

    public Boolean getBold() {
        return this.bold;
    }

    public Short getFontSize() {
        return this.fontSize;
    }

    public short getFormat() {
        return this.format;
    }

    public void setBorderTop(BorderStyle borderTop) {
        this.borderTop = borderTop;
    }

    public void setBorderRight(BorderStyle borderRight) {
        this.borderRight = borderRight;
    }

    public void setBorderBottom(BorderStyle borderBottom) {
        this.borderBottom = borderBottom;
    }

    public void setBorderLeft(BorderStyle borderLeft) {
        this.borderLeft = borderLeft;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public void setBackgroundColor(FSColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public void setFontSize(Short fontSize) {
        this.fontSize = fontSize;
    }

    public void setFormat(short format) {
        this.format = format;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CellStyleKey)) return false;
        final CellStyleKey other = (CellStyleKey) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$borderTop = this.getBorderTop();
        final Object other$borderTop = other.getBorderTop();
        if (this$borderTop == null ? other$borderTop != null : !this$borderTop.equals(other$borderTop)) return false;
        final Object this$borderRight = this.getBorderRight();
        final Object other$borderRight = other.getBorderRight();
        if (this$borderRight == null ? other$borderRight != null : !this$borderRight.equals(other$borderRight))
            return false;
        final Object this$borderBottom = this.getBorderBottom();
        final Object other$borderBottom = other.getBorderBottom();
        if (this$borderBottom == null ? other$borderBottom != null : !this$borderBottom.equals(other$borderBottom))
            return false;
        final Object this$borderLeft = this.getBorderLeft();
        final Object other$borderLeft = other.getBorderLeft();
        if (this$borderLeft == null ? other$borderLeft != null : !this$borderLeft.equals(other$borderLeft))
            return false;
        final Object this$horizontalAlignment = this.getHorizontalAlignment();
        final Object other$horizontalAlignment = other.getHorizontalAlignment();
        if (this$horizontalAlignment == null ? other$horizontalAlignment != null : !this$horizontalAlignment.equals(other$horizontalAlignment))
            return false;
        final Object this$verticalAlignment = this.getVerticalAlignment();
        final Object other$verticalAlignment = other.getVerticalAlignment();
        if (this$verticalAlignment == null ? other$verticalAlignment != null : !this$verticalAlignment.equals(other$verticalAlignment))
            return false;
        if (this.isWordWrap() != other.isWordWrap()) return false;
        final Object this$backgroundColor = this.getBackgroundColor();
        final Object other$backgroundColor = other.getBackgroundColor();
        if (this$backgroundColor == null ? other$backgroundColor != null : !this$backgroundColor.equals(other$backgroundColor))
            return false;
        final Object this$bold = this.getBold();
        final Object other$bold = other.getBold();
        if (this$bold == null ? other$bold != null : !this$bold.equals(other$bold)) return false;
        final Object this$fontSize = this.getFontSize();
        final Object other$fontSize = other.getFontSize();
        if (this$fontSize == null ? other$fontSize != null : !this$fontSize.equals(other$fontSize)) return false;
        if (this.getFormat() != other.getFormat()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CellStyleKey;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $borderTop = this.getBorderTop();
        result = result * PRIME + ($borderTop == null ? 43 : $borderTop.hashCode());
        final Object $borderRight = this.getBorderRight();
        result = result * PRIME + ($borderRight == null ? 43 : $borderRight.hashCode());
        final Object $borderBottom = this.getBorderBottom();
        result = result * PRIME + ($borderBottom == null ? 43 : $borderBottom.hashCode());
        final Object $borderLeft = this.getBorderLeft();
        result = result * PRIME + ($borderLeft == null ? 43 : $borderLeft.hashCode());
        final Object $horizontalAlignment = this.getHorizontalAlignment();
        result = result * PRIME + ($horizontalAlignment == null ? 43 : $horizontalAlignment.hashCode());
        final Object $verticalAlignment = this.getVerticalAlignment();
        result = result * PRIME + ($verticalAlignment == null ? 43 : $verticalAlignment.hashCode());
        result = result * PRIME + (this.isWordWrap() ? 79 : 97);
        final Object $backgroundColor = this.getBackgroundColor();
        result = result * PRIME + ($backgroundColor == null ? 43 : $backgroundColor.hashCode());
        final Object $bold = this.getBold();
        result = result * PRIME + ($bold == null ? 43 : $bold.hashCode());
        final Object $fontSize = this.getFontSize();
        result = result * PRIME + ($fontSize == null ? 43 : $fontSize.hashCode());
        result = result * PRIME + this.getFormat();
        return result;
    }

    public String toString() {
        return "CellStyleKey(borderTop=" + this.getBorderTop() + ", borderRight=" + this.getBorderRight() + ", borderBottom=" + this.getBorderBottom() + ", borderLeft=" + this.getBorderLeft() + ", horizontalAlignment=" + this.getHorizontalAlignment() + ", verticalAlignment=" + this.getVerticalAlignment() + ", wordWrap=" + this.isWordWrap() + ", backgroundColor=" + this.getBackgroundColor() + ", bold=" + this.getBold() + ", fontSize=" + this.getFontSize() + ", format=" + this.getFormat() + ")";
    }
}
