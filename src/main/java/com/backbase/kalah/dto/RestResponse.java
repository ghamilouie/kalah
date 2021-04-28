package com.backbase.kalah.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(subTypes = {GameResponse.class, GameStatusResponse.class})

public abstract class RestResponse {
    private String uri;
}

