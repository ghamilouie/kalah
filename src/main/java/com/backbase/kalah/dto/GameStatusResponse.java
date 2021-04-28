package com.backbase.kalah.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@ApiModel(parent = RestResponse.class)
public class GameStatusResponse extends RestResponse {
    private Long id;
    private Map<String, String> status;
}

