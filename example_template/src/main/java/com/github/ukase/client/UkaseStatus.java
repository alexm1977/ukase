package com.github.ukase.client;

import com.github.ukase.client.model.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UkaseStatus implements CodeEnum {

    ERROR("error"), PROCESSING("processing"), READY("ready"), UNKNOWN("");

    private final String code;

}
