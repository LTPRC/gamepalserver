package com.github.ltprc.gamepal.task;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.ltprc.gamepal.util.ServerUtil;

@Component
public class CheckOnlineTask {

    private static final long TIMEOUT_SECOND = 300;

    @Scheduled(cron = "0 */5 * * * ?")
    public void execute() {
        if (!ServerUtil.onlineMap.isEmpty() && Instant.now().getEpochSecond() - ServerUtil.onlineMap.entrySet().iterator().next().getValue() > TIMEOUT_SECOND) {
            ServerUtil.afterLogoff(ServerUtil.onlineMap.entrySet().iterator().next().getKey());
        }
    }
}
