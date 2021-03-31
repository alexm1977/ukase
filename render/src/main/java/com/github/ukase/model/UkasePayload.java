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

package com.github.ukase.model;

import com.github.ukase.model.validation.HtmlTemplateLocationExists;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

public class UkasePayload {
    @HtmlTemplateLocationExists
    @NotNull
    @Valid
    private String index;
    private Map<String, Object> data = new HashMap<>();
    private byte[] binary;
    private boolean sample;

    public UkasePayload() {
    }

    public @NotNull @Valid String getIndex() {
        return this.index;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public byte[] getBinary() {
        return this.binary;
    }

    public boolean isSample() {
        return this.sample;
    }

    public void setIndex(@NotNull @Valid String index) {
        this.index = index;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }

    public void setSample(boolean sample) {
        this.sample = sample;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UkasePayload)) return false;
        final UkasePayload other = (UkasePayload) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$index = this.getIndex();
        final Object other$index = other.getIndex();
        if (this$index == null ? other$index != null : !this$index.equals(other$index)) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
        if (!Arrays.equals(this.getBinary(), other.getBinary())) return false;
        if (this.isSample() != other.isSample()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UkasePayload;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $index = this.getIndex();
        result = result * PRIME + ($index == null ? 43 : $index.hashCode());
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        result = result * PRIME + Arrays.hashCode(this.getBinary());
        result = result * PRIME + (this.isSample() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "UkasePayload(index=" + this.getIndex() + ", data=" + this.getData() + ", binary=" + Arrays.toString(this.getBinary()) + ", sample=" + this.isSample() + ")";
    }
}
