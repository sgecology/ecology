package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.Portfolio;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository
public interface PortfolioPersistence extends CodeNamePersistence<Portfolio, Long>{
}
