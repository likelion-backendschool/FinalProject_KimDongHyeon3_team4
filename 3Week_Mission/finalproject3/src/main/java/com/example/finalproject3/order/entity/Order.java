package com.example.finalproject3.order.entity;

import com.example.finalproject3.member.entity.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_order")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Member member;

    @Column
    private String name;

    @Column
    private boolean isPaid;

    @Column
    private boolean isCanceled;

    @Column
    private boolean isRefunded;

    @Builder.Default
    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);

        orderItems.add(orderItem);
    }

    public int getPayPrice() {
        int payPrice = 0;

        for ( OrderItem orderItem : orderItems ) {
            payPrice += orderItem.getSalePrice();   //만약, 쿠폰/이벤트를 적용한다면 SalePrice를 가져오면 안된다. 적용된 값을 가져와야 한다.
        }
        return payPrice;
    }

    public void setPaymentDone() {

        for ( OrderItem orderItem : orderItems ) {
            orderItem.setPaymentDone();
        }
        isPaid = true;
    }

    public void setRefundDone() {
        for ( OrderItem orderItem : orderItems ) {
            orderItem.setRefundDone();
        }
        isRefunded = true;
    }

    public void makeName() {
        String name = orderItems.get(0).getProduct().getSubject();

        if (orderItems.size() > 1) {
            name += " 외 %d권".formatted(orderItems.size() - 1);
        }

        this.name = name;
    }
}
