package com.example.ejb;

import com.example.persistence.repository.jpa.entity.BeneficioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

@Stateless(name = "BeneficioEjbService")
public class BeneficioEjbService implements BeneficioEjbServiceRemote {

    @PersistenceContext
    private EntityManager em;

    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo");
        }

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Não é possível transferir para o mesmo benefício");
        }

        Long firstId = fromId < toId ? fromId : toId;
        Long secondId = fromId < toId ? toId : fromId;

        BeneficioEntity first = em.find(BeneficioEntity.class, firstId, LockModeType.PESSIMISTIC_WRITE);
        BeneficioEntity second = em.find(BeneficioEntity.class, secondId, LockModeType.PESSIMISTIC_WRITE);

        BeneficioEntity from = fromId.equals(firstId) ? first : second;
        BeneficioEntity to = fromId.equals(firstId) ? second : first;

        if (from == null) {
            throw new IllegalArgumentException("Benefício origem não encontrado: " + fromId);
        }

        if (to == null) {
            throw new IllegalArgumentException("Benefício destino não encontrado: " + toId);
        }

        if (!from.getAtivo()) {
            throw new IllegalStateException("Benefício origem está inativo: " + from.getNome());
        }

        if (!to.getAtivo()) {
            throw new IllegalStateException("Benefício destino está inativo: " + to.getNome());
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.merge(from);
        em.merge(to);
    }
}
