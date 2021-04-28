package com.backbase.kalah.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel(parent = RestResponse.class)
public class GameResponse extends RestResponse {
    private Long id;
}
