package com.ch.cache.listener;

import com.ch.cache.model.IRemoveContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveListener implements IRemoveListener{
    Logger log= LoggerFactory.getLogger(RemoveListener.class);
    @Override
    public void listen(IRemoveContext context) {
        log.info("【移除】：由于{}，移除了{}=={}",context.type(),context.key(),context.value());
    }
}
