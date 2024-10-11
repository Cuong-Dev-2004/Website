package com.stark.webbanhang.api.user.service.impl;

import com.stark.webbanhang.api.user.dto.request.OrderRequest;
import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.dto.response.OrderResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;
import com.stark.webbanhang.api.user.dto.response.UserResponse;
import com.stark.webbanhang.api.user.entity.Category;
import com.stark.webbanhang.api.user.entity.Order;
import com.stark.webbanhang.api.user.entity.User;
import com.stark.webbanhang.api.user.mapper.OrderMapper;
import com.stark.webbanhang.api.user.repository.OrderRepository;
import com.stark.webbanhang.api.user.repository.UserRepository;
import com.stark.webbanhang.api.user.service.OrderDetailService;
import com.stark.webbanhang.api.user.service.OrderService;
import com.stark.webbanhang.helper.exception.AppException;
import com.stark.webbanhang.helper.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.control.MappingControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor// thay co contructer nó sẽ tự động inject
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderDetailService orderDetailService;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    UserRepository userRepository;
    AuthenticationService authenticationService;

    @Override
    public OrderResponse createOder(OrderRequest orderRequest,String authHeader) {
        String token = authHeader.substring(7);
        UUID idUser = authenticationService.getCurrentUserId(token);
        Order order = orderMapper.toOrder(orderRequest);
        User user = userRepository.findById(idUser)
                .orElseThrow(()-> new AppException(ErrorCode.USER_EXISTED));
        order.setUser(user);
        Order savedOrder = orderRepository.save(order);
        orderDetailService.createOrderDetail(savedOrder, orderRequest.getOrderDetails());
        OrderResponse response = this.convertToOrderResponse(savedOrder);
        response.setUserName(user.getLastName());

        return response;
    }

    @Override
    public Order findById (UUID orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }

    @Override
    public PageResponse<OrderResponse> getAllOrder(int page, int size, int limit) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<Order> pageData = orderRepository.findAll(pageable);
        List<Order> listOrder = pageData.getContent();
        if (limit > 0) {
            listOrder = listOrder.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPage(pageData.getTotalPages())
                .totalElement(pageData.getTotalElements())
                .data(listOrder.stream().map(this::convertToOrderResponse).toList())
                .build();
    }

    @Override
    public OrderResponse updateOrder(UUID idOrder, int status) {
        Order order = orderRepository.findById(idOrder)
                .orElseThrow(()-> new RuntimeException("Not found Order"));
        order.setStatus(status);
        OrderResponse response = this.convertToOrderResponse(order);
        return response;
    }

    @Override
    public void deleteOrder(UUID idOrder) {
        orderDetailService.deleteOrderDetail(idOrder);
        orderRepository.deleteById(idOrder);
    }

    public OrderResponse convertToOrderResponse(Order order){
        OrderResponse response = orderMapper.toOrderResponse(order);
        User user = userRepository.findById(order.getUser().getId())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        response.setUserName(user.getLastName());
        return response;
    }
}
