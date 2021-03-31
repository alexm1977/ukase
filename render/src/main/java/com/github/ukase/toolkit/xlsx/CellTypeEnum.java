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

import org.apache.poi.ss.usermodel.CellType;

public enum CellTypeEnum {
    STRING("string", CellType.STRING),
    NUMERIC("numeric", CellType.NUMERIC),
    DATE("date", CellType.STRING),
    DEFAULT("common", CellType.BLANK);

    private final String stringValue;
    private final CellType cellType;

    private CellTypeEnum(String stringValue, CellType cellType) {
        this.stringValue = stringValue;
        this.cellType = cellType;
    }

    public static CellTypeEnum fromString(String type) {
        if (type == null || type.isEmpty()) {
            return DEFAULT;
        }
        for (CellTypeEnum cellTypeEnum : values()) {
            if (cellTypeEnum.stringValue.equals(type)) {
                return cellTypeEnum;
            }
        }
        return DEFAULT;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public CellType getCellType() {
        return this.cellType;
    }
}
