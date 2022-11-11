package com.example.finalproject4.withdraw.entity;


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
public class Withdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Member member;

    @Column
    String bankName;

    @Column
    String bankAccountNo;

    @Column
    int price;

    @Column
    boolean isWithdraw;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    public boolean withdrawAvailable(){
        if(isWithdraw == true || price > member.getRestCash())
            return false;

        return true;
    }

    public void withdrawDone(){
        isWithdraw = true;
    }
}
