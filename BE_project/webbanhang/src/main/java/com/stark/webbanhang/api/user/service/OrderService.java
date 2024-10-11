package com.stark.webbanhang.api.user.service;

import com.stark.webbanhang.api.user.dto.request.OrderRequest;
import com.stark.webbanhang.api.user.dto.response.OrderResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.entity.Order;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface OrderService {
    public OrderResponse createOder(OrderRequest request,String authHeader);
    public Order findById (UUID orderId);
    public PageResponse<OrderResponse> getAllOrder(int page, int size, int limit);
    public OrderResponse updateOrder(UUID idOrder,int status);
    public void deleteOrder(UUID idOrder);
}