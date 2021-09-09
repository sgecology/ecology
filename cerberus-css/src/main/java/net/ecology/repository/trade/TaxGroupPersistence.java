package net.ecology.repository.trade;

import org.springframework.stereotype.Repository;

import net.ecology.entity.general.TaxGroup;
import net.ecology.framework.persistence.NamePersistence;

@Repository
public interface TaxGroupPersistence extends NamePersistence<TaxGroup, Long>{
}
