package com.example.finalproject3.order.entity;


import com.example.finalproject3.product.entity.Product;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ToString.Exclude
    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    @Column
    private int price;  //소비가

    @Column
    private int salePrice;  //판매가

    @Column
    private int wholesalePrice; //도매가

    @Column
    private int payPrice; // 실제 결제 가격 (이벤트, 쿠폰으로 가격이 salePrice가 아닐 수 있다.)

    @Column
    private int refundPrice; // 환불 가격 (이벤트, 쿠폰으로 가격이 바뀌었으면, 실제 결제 금액만 환불해줘야된다.)

    @Column
    private int pgFee; // 결제 대행사 수수료

    @Column
    private boolean isPaid; // 결제여부

    @Column
    private  LocalDateTime payDate;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    public void setPaymentDone() {
        this.pgFee = 0;
        this.payPrice = salePrice;   //쿠폰, 이벤트 적용시 변경되어야됨
        this.isPaid = true;
        this.payDate = LocalDateTime.now();
    }

    public void setRefundDone() {
        this.refundPrice = payPrice;
    }
}
