package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.Payment;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface PaymentPersistence extends BasePersistence<Payment, Long>{
}
