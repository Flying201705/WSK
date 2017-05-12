package nova.common.game.wsk.util;

public class CardConstant {
	
	public final static int CARD_COUNT = 55;
	public final static int CARD_MAX_COUNT = 136;
	public final static int CARD_SINGLE_COUNT = 13;
	public final static int CARD_KING_COUNT = 2;
	public final static int CARD_DEFAULT = 0;
	public final static int CARD_FANG_A = 1;
	public final static int CARD_MEI_A = CARD_FANG_A + CARD_SINGLE_COUNT;
	public final static int CARD_TAO_A = CARD_MEI_A + CARD_SINGLE_COUNT;
	public final static int CARD_XIN_A = CARD_TAO_A + CARD_SINGLE_COUNT;
	public final static int CARD_KING_BEGIN = CARD_XIN_A + CARD_SINGLE_COUNT;
	public final static int CARD_FANG_J = CARD_FANG_A + 10;
	public final static int CARD_MEI_J = CARD_MEI_A + 10;
	public final static int CARD_TAO_J = CARD_TAO_A + 10;
	public final static int CARD_XIN_J = CARD_XIN_A + 10;
	
	public final static int CARD_WIDTH = 97;
	public final static int CARD_HEIGHT = 127;
	
	public final static int[] CARD_ELEMENTS = {
		1, 5, 8, 9, 10, 11, 12, 13,
		1, 5, 8, 9, 10, 11, 12, 13,
		1, 5, 8, 9, 10, 11, 12, 13,
		1, 5, 8, 9, 10, 11, 12, 13,
		14, 18, 21, 22, 23, 24, 25, 26,
		14, 18, 21, 22, 23, 24, 25, 26,
		14, 18, 21, 22, 23, 24, 25, 26,
		14, 18, 21, 22, 23, 24, 25, 26,
		27, 31, 34, 35, 36, 37, 38, 39,
		27, 31, 34, 35, 36, 37, 38, 39,
		27, 31, 34, 35, 36, 37, 38, 39,
		27, 31, 34, 35, 36, 37, 38, 39,
		40, 44, 47, 48, 49, 50, 51, 52,
		40, 44, 47, 48, 49, 50, 51, 52,
		40, 44, 47, 48, 49, 50, 51, 52,
		40, 44, 47, 48, 49, 50, 51, 52,
		53, 53, 53, 53,
		54, 54, 54, 54
	};
}
