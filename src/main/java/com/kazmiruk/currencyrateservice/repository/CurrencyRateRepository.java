package com.kazmiruk.currencyrateservice.repository;

import com.kazmiruk.currencyrateservice.model.entity.CurrencyRate;
import com.kazmiruk.currencyrateservice.model.type.CurrencyRateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, UUID> {

    @Query("""
            select cr from CurrencyRate cr
            where cr.type = :type
            order by cr.createdAt desc
            limit 3
            """)
    List<CurrencyRate> findLatestByType(CurrencyRateType type);
}
