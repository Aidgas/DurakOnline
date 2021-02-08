package durakonline.sk.durakonline;

import android.graphics.Matrix;

public class CardInfo
{
	public String litera = "", litera2 = "";
	public int type, card, sort_num;
	public boolean draw_icon_new_card;
	public boolean draw_icon_new_card2;
	public boolean draw_icon_tossed_card;
	public long timestamp_add;
	public long timestamp_add2;
	public Matrix matrix_draw = null;

	public CardInfo(String _litera, String _litera2, int _card, int _type, int _sort_num)
	{
		litera = _litera;
		litera2 = _litera2;
		type   = _type;
		card   = _card;
		sort_num = _sort_num;
		draw_icon_new_card     = false;
		draw_icon_new_card2    = false;
		draw_icon_tossed_card  = false;
        timestamp_add  = 0;
        timestamp_add2 = 0;
	}
}
