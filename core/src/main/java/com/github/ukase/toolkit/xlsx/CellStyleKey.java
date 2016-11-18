/*
 * Copyright (c) 2016 Konstantin Lepa <konstantin+ukase@lepabox.net>
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

import lombok.Data;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.xhtmlrenderer.css.parser.FSColor;
import org.xhtmlrenderer.css.parser.FSRGBColor;

import java.util.function.Supplier;

@Data
class CellStyleKey {
    private BorderStyle borderTop;
    private BorderStyle borderRight;
    private BorderStyle borderBottom;
    private BorderStyle borderLeft;
    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;
    private boolean wordWrap;
    private FSColor backgroundColor;
    private boolean bold;
    private Short fontSize;

    void applyToStyle(XSSFCellStyle style, Supplier<Font> fontSupplier) {
        applyBorders(style);
        applyAlignment(style);
        applyFont(style, fontSupplier);
        applyTextWrap(style);
        applyBackgroundColor(style);
    }

    private void applyBackgroundColor(XSSFCellStyle style) {
        if (backgroundColor instanceof FSRGBColor) {
            FSRGBColor rgbColor = (FSRGBColor) backgroundColor;
            byte[] colors = new byte[3];
            colors[0] = (byte)rgbColor.getRed();
            colors[1] = (byte)rgbColor.getGreen();
            colors[2] = (byte)rgbColor.getBlue();

            XSSFColor xlsxColor = new XSSFColor(colors);
            style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
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
        if (!bold && fontSize == null) {
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
}
