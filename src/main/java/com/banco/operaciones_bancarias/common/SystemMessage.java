package com.banco.operaciones_bancarias.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessage {

    @JsonProperty("source")
    private String source;

    @JsonProperty("message")
    private String message;
}
