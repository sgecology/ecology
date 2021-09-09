package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.TaxRate;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface TaxRatePersistence extends BasePersistence<TaxRate, Long>{
}
