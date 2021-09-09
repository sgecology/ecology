package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.Pos;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository
public interface PosPersistence extends CodeNamePersistence<Pos, Long>{
}
