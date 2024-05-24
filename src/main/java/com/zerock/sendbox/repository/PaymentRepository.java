package com.zerock.sendbox.repository;


import com.zerock.sendbox.entity.Payment;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query("select p from Payment p where p.orders.userMember.userNo =:userNo order by p.paymentNo desc limit 1")
    Payment findPayment(@Param("userNo") Integer userNo);
}
