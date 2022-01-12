package com.github.ltprc.gamepal.task;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.ltprc.gamepal.entity.UserOnline;
import com.github.ltprc.gamepal.repository.UserOnlineRepository;
import com.github.ltprc.gamepal.util.ServerUtil;

@Component
public class CheckOnlineTask {

    @Autowired
    UserOnlineRepository userOnlineRepository;

    private static final long TIMEOUT_SECOND = 300;

    @Scheduled(cron = "0 */5 * * * ?")
    public void execute() {
        if (!ServerUtil.onlineMap.isEmpty() && Instant.now().getEpochSecond() - ServerUtil.onlineMap.entrySet().iterator().next().getValue() > TIMEOUT_SECOND) {
            afterLogoff(ServerUtil.onlineMap.entrySet().iterator().next().getKey());
        }
    }

    public void afterLogoff(String uuid, String token) {
        afterLogoff(uuid);
        if (token.equals(ServerUtil.tokenMap.get(uuid))) {
            ServerUtil.tokenMap.remove(uuid);
        }
    }

    public void afterLogoff(String uuid) {
        List<UserOnline> userOnlineList = userOnlineRepository.queryUserOnlineByUuid(uuid);
        if (!userOnlineList.isEmpty()) {
            userOnlineRepository.delete(userOnlineList.get(0));
        }
        //      if (ServerUtil.positionMap.containsKey(uuid)) {
        //      Position position = ServerUtil.positionMap.get(uuid);
        //      int sceneNo = position.getSceneNo();
        //      if (ServerUtil.userLocationMap.containsKey(sceneNo)) {
        //          Set<String> uuidSet = ServerUtil.userLocationMap.get(sceneNo);
        //          uuidSet.remove(uuid);
        //      }
        //  }
        ServerUtil.chatMap.remove(uuid);
        ServerUtil.voiceMap.remove(uuid);
        ServerUtil.onlineMap.remove(uuid);
    }
}
