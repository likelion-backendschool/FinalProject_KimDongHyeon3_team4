package com.example.finalproject4.product.entity;

import com.example.finalproject4.keyword.entity.Keyword;
import com.example.finalproject4.member.entity.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Member author;

    @ManyToOne
    Keyword keyword;

    @Column
    String subject;

    @Column
    int price;  //소비가

    @Column
    int wholesalePrice; //도매가

    @Column
    int salePrice;  //판매가

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    public String getJdenticon() {
        return "product__" + getId();
    }

    public boolean isOrderable() {  //상품 판매가 중지되면, 해당 부분을 수정해줘야한다.
        return true;
    }
}
