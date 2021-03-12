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

import com.github.ukase.toolkit.xlsx.translators.Translator;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.xhtmlrenderer.render.BlockBox;

import java.util.Collection;

@Component
public class RenderingTableFactory {
    private final Collection<Translator> translators;

    @Autowired
    public RenderingTableFactory(Collection<Translator> translators) {
        this.translators = translators;
    }

    public RenderingTable build(Workbook wb, BlockBox box, Element table) {
        return new RenderingTable(wb, table, box, translators);
    }
}
