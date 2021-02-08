package durakonline.sk.durakonline;

import android.graphics.Bitmap;

/**
 * Created by sk on 27.09.17.
 */

public class CardDrawInfo
{
    public int type;
    public CardInfo card_info;
    public Bitmap img;

    public float draw_pos_x;
    public float draw_pos_y;

    public float animation_offset_x;
    public float animation_offset_y;

    public float animation_rotate_x;
    public float animation_rotate_y;

    public float animation_step_x, animation_step_y;

    public CardDrawInfo(int _type, CardInfo _card_info, Bitmap _img)
    {
        type = _type;
        card_info = _card_info;
        img = _img;

        animation_offset_x = 0;
        animation_offset_y = 0;

        animation_rotate_x = 0;
        animation_rotate_y = 0;
    }
}
