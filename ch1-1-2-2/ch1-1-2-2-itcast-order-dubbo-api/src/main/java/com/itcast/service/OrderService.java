package com.itcast.service;

import java.util.concurrent.CompletableFuture;

public interface OrderService {

    CompletableFuture<String> payAsync(String message);
}
