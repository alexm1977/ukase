package com.github.ukase.client.model;

import lombok.Getter;

@Getter
public class UkasePayload {

    private final String index;
    private final Object data;
    private final Boolean sample;

    public UkasePayload(String index, Object data) {
        this(index, data, false);
    }

    public UkasePayload(String index, Object data, Boolean sample) {
        this.index = index;
        this.data = data;
        this.sample = sample;
    }
}
