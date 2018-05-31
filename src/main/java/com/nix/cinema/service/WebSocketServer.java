package com.nix.cinema.service;

import com.nix.cinema.dao.BorrowTimeMapper;
import com.nix.cinema.model.*;
import com.nix.cinema.service.impl.BookInfoService;
import com.nix.cinema.service.impl.BorrowRecordService;
import com.nix.cinema.service.impl.MemberService;
import com.nix.cinema.util.SQLUtil;
import com.nix.cinema.util.log.LogKit;
import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Kiss
 * @date 2018/05/30 20:10
 */
@ServerEndpoint("/webSocketServer")
@Component
public class WebSocketServer {
    private static BookInfoService bookInfoService = SpringContextHolder.getBean("bookInfoService");
    private static BorrowRecordService borrowRecordService = SpringContextHolder.getBean("borrowRecordService") ;
    private static MemberService memberService = SpringContextHolder.getBean("memberService") ;
    private static BorrowTimeMapper borrowTimeMapper = SpringContextHolder.getBean("borrowTimeMapper");

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    private boolean isAdmin = true;

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        try {
            webSocketSet.add(this);
            //发送刚连接时的信息
            sendMessage(getOpenMsg());
        } catch (IOException e) {
            LogKit.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        //群发消息
//        for (WebSocketServer item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LogKit.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message,boolean isAdmin) throws IOException {
        System.out.println(message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (isAdmin == item.isAdmin) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }


    private String getOpenMsg() {
        if (isAdmin) {
            //发送基本统计表数据
            setBasicBorrowRecordCunt();
            setBasicMsgSumCount();
            setBasicMsgBorrowCount();
            setBasicMsgTimeoutReturnBackCount();
            setBasicMsgWaitReturnBackCount();
            setBasicMsgMemberCount();
            //发送最大借阅时间统计图
            setBorrowMaxTimeBook();
            //发送近七天归还记录
            for (int i = 0;i < 7;i ++) {
                setBorrowAndReturnBackMgsDate(new Date(System.currentTimeMillis() - i * 24 * 60 * 60 *1000), 6 - i);
            }
            //发送通知
            List<BorrowRecordModel> list = borrowRecordService.list(1,6,"updateDate","desc",null);
            for (int i = list.size() - 1;i >= 0;i --) {
                setNotice(list.get(i));
            }
        } else {
            return getMemberOpenMsg();
        }
        return "";
    }

    /**
     * 发送书籍总数信息
     * */
    public static void setBasicMsgSumCount() {
        try {
            sendInfo(SQLUtil.sqlFormat("{\"type\":\"basicMsg\",\"data\":{\"index\":0,\"value\":?}}",
                    bookInfoService.count()),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setBasicMsgBorrowCount() {
        try {
            sendInfo(SQLUtil.sqlFormat("{\"type\":\"basicMsg\",\"data\":{\"index\":1,\"value\":?}}",
                    bookInfoService.select("`status` = 0").size()),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setBasicMsgTimeoutReturnBackCount() {
        try {
            sendInfo(SQLUtil.sqlFormat("{\"type\":\"basicMsg\",\"data\":{\"index\":2,\"value\":?}}",
                    borrowRecordService.select("shouldTime < now() and `status` = 0").size()),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setBasicMsgWaitReturnBackCount() {
        try {
            sendInfo(SQLUtil.sqlFormat("{\"type\":\"basicMsg\",\"data\":{\"index\":3,\"value\":?}}",
                    bookInfoService.select("`status` = 0").size()),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setBasicMsgMemberCount() {
        try {
            sendInfo(SQLUtil.sqlFormat("{\"type\":\"basicMsg\",\"data\":{\"index\":4,\"value\":?}}",
                    memberService.select("role = 3").size()),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setBasicBorrowRecordCunt() {
        try {
            sendInfo(SQLUtil.sqlFormat("{\"type\":\"basicMsg\",\"data\":{\"index\":5,\"value\":?}}",
                    borrowRecordService.count()),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setBorrowAndReturnBackMgsDate(Date date,int index) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date now = format1.parse(format1.format(new Date()));
            date = format1.parse(format1.format(date));
            sendInfo(SQLUtil.sqlFormat("{\"type\":\"borrowAndReturnBackMgs\",\"data\":{\"index\":?,\"time\":\"?\",\"borrow\":?,\"returnBack\":?}}",
                    index,format.format(date),
                    borrowRecordService.select("borrowTime BETWEEN '? 00:00:00' and '? 23:59:59'",format1.format(date),format1.format(date)).size(),
                    borrowRecordService.select("returnTime BETWEEN '? 00:00:00' and '? 23:59:59'",format1.format(date),format1.format(date)).size()),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setBorrowMaxTimeBook() {
        try {
            StringBuilder json = new StringBuilder("[");
            List<BorrowTime> list = borrowTimeMapper.list(null);
            String sql = "";
            for (int i = 0;i < list.size();i ++) {
                BookInfoModel bookInfoModel = bookInfoService.findById(list.get(i).getBookInfoId());
                json.append(SQLUtil.sqlFormat("{\"name\":\"?\",\"value\" : ?},",
                        bookInfoModel.getName(),
                        list.get(i).getSumTime()));
                sql += (" bookInfo != " + bookInfoModel.getId() + " and");
            }
            sql += " 1 = 1";
            list = borrowTimeMapper.list(sql);
            json.append(SQLUtil.sqlFormat("{\"name\":\"?\",\"value\" : ?}",
                    "其他",
                    list !=null && list.size() == 1 && list.get(0) != null ? list.get(0).getSumTime() : 0));
            json.append("]}");
            sendInfo(SQLUtil.sqlFormat("{\"type\":\"borrowMaxTimeBook\",\"data\":?",
                    json.toString()),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setNotice(BorrowRecordModel borrowRecordModel) {
        String type = borrowRecordModel.getStatus() ? "returnBack" : "borrow";
        try {
            sendInfo(SQLUtil.sqlFormat("{\"type\":\"notice\",\"data\":{\"type\":\"?\",\"member\":\"?\",\"bookInfo\":\"?\",\"time\":\"?\"}}",
                    type,borrowRecordModel.getMember().getName(),borrowRecordModel.getBookInfo().getName(),DATE_FORMAT.format(borrowRecordModel.getUpdateDate())),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMemberOpenMsg() {
        return null;
    }
}
