package com.rest.marketplace.domain.models.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationRequest {

	private Integer page;
	private Integer size;
}
