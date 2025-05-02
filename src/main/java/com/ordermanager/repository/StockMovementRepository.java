package com.ordermanager.repository;


import com.ordermanager.model.StockMovement;
import com.ordermanager.utils.BaseRepository;


import java.util.List;

public class StockMovementRepository extends BaseRepository<StockMovement> {
    public StockMovementRepository() {
        super(StockMovement.class);

    }

    public List<StockMovement> findByItemId(Long itemId) {
        return super.em.createQuery("SELECT sm FROM StockMovement sm WHERE sm.item.id = :itemId", StockMovement.class)
                .setParameter("itemId", itemId)
                .getResultList();
    }


}