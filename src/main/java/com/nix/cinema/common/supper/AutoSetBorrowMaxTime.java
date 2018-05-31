package com.nix.cinema.common.supper;

import com.nix.cinema.service.WebSocketServer;
import freemaker.util.log.LogKit;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Kiss
 * @date 2018/05/30 23:51
 */
@Component
public class AutoSetBorrowMaxTime {
    private final ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    @PostConstruct
    public void auto() {
        LogKit.info("开启定时任务执行发送借阅时间信息");
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            WebSocketServer.setBorrowMaxTimeBook();
            WebSocketServer.setBasicMsgTimeoutReturnBackCount();
        },1,10,TimeUnit.MINUTES);
    }
}
