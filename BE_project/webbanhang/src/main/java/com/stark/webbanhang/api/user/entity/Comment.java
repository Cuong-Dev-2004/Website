package com.stark.webbanhang.api.user.entity;

import com.stark.webbanhang.helper.base.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "comment")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    private int rating;
    private String message;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
