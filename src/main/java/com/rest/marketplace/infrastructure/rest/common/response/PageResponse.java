package com.rest.marketplace.infrastructure.rest.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

	private List<T> content;

	private Integer page;

	private Integer size;

	private Long totalElements;

	private Integer totalPages;
}
