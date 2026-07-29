package com.rest.marketplace.application.usecases.product;

import java.util.concurrent.CompletableFuture;

public interface ImportProductsUc {

	CompletableFuture<Integer> importFromFakeStore();
}
