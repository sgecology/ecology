package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.trade.PromissoryNote;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface PromissoryNotePersistence extends BasePersistence<PromissoryNote, Long>{
}
