package com.github.ltprc.gamepal.task;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.ltprc.gamepal.repository.UserOnlineRepository;
import com.github.ltprc.gamepal.util.ServerUtil;

@Component
public class CheckOnlineTask {

    @Autowired
    UserOnlineRepository userOnlineRepository;

    private static final long TIMEOUT_SECOND = 300;

    //@Scheduled(cron = "0 */5 * * * ?")
    //Temporary ignored 02/08
    public void execute() {
        if (!ServerUtil.onlineMap.isEmpty() && Instant.now().getEpochSecond() - ServerUtil.onlineMap.entrySet().iterator().next().getValue() > TIMEOUT_SECOND) {
            ServerUtil.tokenMap.remove(ServerUtil.onlineMap.entrySet().iterator().next().getKey()); // log off
        }
    }
}
