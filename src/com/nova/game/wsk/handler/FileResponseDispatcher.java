package com.nova.game.wsk.handler;

import java.io.FileOutputStream;

import com.badlogic.gdx.Gdx;
import com.nova.game.wsk.Constant;
import com.nova.game.wsk.model.RoomInfoController;

import io.netty.buffer.ByteBuf;

public class FileResponseDispatcher {

	public void processor(int commandId, ByteBuf message) {
		int playerId = message.readInt();
		int length = message.readInt();
		byte[] c = new byte[length];
		message.readBytes(c);
		switch (commandId) {
		case 9000:
			RoomInfoController.getInstance().addPlayerMessage(playerId, new String(c));
		break;
		
		case 9001:
			try {
				String file = "wsk/audios/" + System.currentTimeMillis() + ".amr";
				FileOutputStream fos = new FileOutputStream("/sdcard/" + file);
				fos.write(c);
				fos.close();
				
				RoomInfoController.getInstance().addPlayerSound(playerId, Gdx.audio.newSound(Gdx.files.external(file)));
				RoomInfoController.getInstance().getPlayerSound(playerId).play(1.0f);
			} catch (Exception e) {
				android.util.Log.e("zhangxx", " e : " + e.toString());
			}
			break;

		case 9002:
			RoomInfoController.getInstance().addPlayerMessage(playerId, Constant.COMMON_CHAT_MESSAGE[Integer.valueOf(new String(c))]);
			RoomInfoController.getInstance().addPlayerSound(playerId, Gdx.audio.newSound(Gdx.files.internal("sound/msg_w_" + Integer.valueOf(new String(c)) + ".ogg")));
			RoomInfoController.getInstance().getPlayerSound(playerId).play(1.0f);
			break;
		default:
			break;
		}
	}
}
