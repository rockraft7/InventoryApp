package com.faizalsidek.inventory.config.audit;

import com.faizalsidek.inventory.config.util.AutowireHelper;
import com.faizalsidek.inventory.domain.Item;
import com.faizalsidek.inventory.domain.ItemHistory;
import com.faizalsidek.inventory.repository.ItemHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

/**
 * Created by faizal on 2/4/16.
 */
@Component
public class ItemAuditEventListener extends AuditingEntityListener {
    public static final Logger log = LoggerFactory.getLogger(ItemAuditEventListener.class);

    @Inject
    private ItemHistoryRepository historyRepository;

    @PostPersist
    public void afterCreation(Object target) {
        createEntry((Item) target);
    }

    @PostUpdate
    public void afterUpdate(Object object) {
        createEntry((Item) object);
    }

    @Async
    public void createEntry(Item item) {
        log.debug("Create item history entry...");
        try {
            ItemHistory itemHistory = new ItemHistory();
            itemHistory.setItem(item);
            itemHistory.setStatus(item.getStatus());
            if(historyRepository == null) {
                log.debug("Manually autowiring this.");
                AutowireHelper.autowire(this, this.historyRepository);
            }

            historyRepository.save(itemHistory);
        } catch (Exception e) {
            log.error("Failed to persist.", e);
        }
    }
}
