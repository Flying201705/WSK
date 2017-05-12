package com.nova.game.wsk;

import java.util.HashMap;
import java.util.Map;

import nova.common.game.wsk.data.PlayerInfo;

public class Constant {
    public static final int DEFAULT_WIDTH = 1920;
    public static final int DEFAULT_HEIGHT = 1080;
    
    public static final String[] COMMON_CHAT_MESSAGE = {
    	"快点吧,我等的花儿都谢了!",
    	"大家好,很高兴见到大家",
    	"又断线了,网络怎么这么差!",
    	"和你合作真是太愉快了",
    	"我们交个朋友吧,能不能告诉我你的联系方式呀",
    	"你是MM还是GG",
    	"不要吵了,有什么好吵的,专心玩游戏吧",
    	"不要走,决战到天亮",
    	"各们,真不好意思,我要离开一会",
    	"再见了,我们想念大家的"
    };
    
    // 用于测试
    public static final int TEST_PLAYERID = 1234;
    
    public static final Map<String, PlayerInfo> PLAYERS = new HashMap<String, PlayerInfo>();
    static {
        PLAYERS.put("18682316960", new PlayerInfo(6960, "lh", "1", 1));
        PLAYERS.put("13480122949", new PlayerInfo(2949, "zxx", "7", 1));
        PLAYERS.put("18665826223", new PlayerInfo(6223, "gionee", "10", 0));
        PLAYERS.put("13751056934", new PlayerInfo(6934, "wei", "11", 0));
    }
}
