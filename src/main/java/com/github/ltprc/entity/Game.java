package com.github.ltprc.entity;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.github.ltprc.entity.Player;
import com.github.ltprc.entity.Subject;

@Component
public abstract class Game {

    /**
     * Status
     * -1 error
     * 0  ready
     * 1  running
     * 2  paused
     * 3  finished
     */
    public static final int STATUS_ERROR = -1;
    public static final int STATUS_READY = 0;
    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_PAUSED = 2;
    public static final int STATUS_FINISHED = 3;

    private String name;
    private Class<Subject> subjectClass;
    private int status = STATUS_READY;
    @NonNull
    private Set<String> playerNameSet = new LinkedHashSet<>();
    private Set<String> notReadyplayerNameSet = new HashSet<>();

    public Game() {
        
    }

    public Game(Class<Subject> subjectClass, String name) {
        this.setSubjectClass(subjectClass);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<Subject> getSubjectClass() {
        return subjectClass;
    }

    public void setSubjectClass(Class<Subject> subjectClass) {
        this.subjectClass = subjectClass;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<String> getPlayerNameSet() {
        return playerNameSet;
    }

    public void setPlayerNameSet(Set<String> playerNameSet) {
        this.playerNameSet = playerNameSet;
    }

    public Set<String> getNotReadyplayerNameSet() {
        return notReadyplayerNameSet;
    }

    public void setNotReadyplayerNameSet(Set<String> notReadyplayerNameSet) {
        this.notReadyplayerNameSet = notReadyplayerNameSet;
    }

    public boolean addPlayer(@NonNull Player player) {
        if (playerNameSet.size() >= Subject.getMaxPlayerNum(subjectClass)
                || playerNameSet.contains(player.getName()) || status != STATUS_READY) {
            return false;
        }
        playerNameSet.add(player.getName());
        notReadyplayerNameSet.add(player.getName());
        return true;
    }

    public boolean removePlayer(String playerName) {
        if (!playerNameSet.contains(playerName) || status != STATUS_READY) {
            return false;
        }
        playerNameSet.remove(playerName);
        notReadyplayerNameSet.remove(playerName);
        return true;
    }

    public boolean readyPlayer(String playerName) {
        if (!playerNameSet.contains(playerName)) {
            return false;
        }
        notReadyplayerNameSet.remove(playerName);
        return true;
    }

    public boolean unreadyPlayer(Player player) {
        if (!playerNameSet.contains(player.getName())) {
            return false;
        }
        notReadyplayerNameSet.add(player.getName());
        return true;
    }

    public boolean startGame() {
        if (playerNameSet.size() < Subject.getMinPlayerNum(subjectClass) || !notReadyplayerNameSet.isEmpty()) {
            return false;
        }
        status = STATUS_RUNNING;
        return true;
    }

    public void endGame() {
        status = STATUS_FINISHED;
    }
}
