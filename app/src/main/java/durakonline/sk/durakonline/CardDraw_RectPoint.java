package durakonline.sk.durakonline;

import android.graphics.Matrix;

/**
 * Created by sk on 01.10.17.
 */

public class CardDraw_RectPoint
{
    public double x1, y1;
    public double x2, y2;
    public double x3, y3;
    public double x4, y4;

    public double center_x
                , center_y
                , rotate_angle
                , rotate_angle_x
                , rotate_angle_y
                , end_center_x
                , end_center_y

                , icon_new_card_in_board_x
                , icon_new_card_in_board_y

                , icon_new_card_in_board2_x
                , icon_new_card_in_board2_y
            ;

    public Matrix matrix_icon_new_tossed_card;
    public Matrix matrix_icon_new_tossed_card2;

    public boolean need_draw_card;

    public CardDraw_RectPoint()
    {
        x1 = y1 = x2 = y2 = x3 = y3 = x4 = y4 = 0;
        center_x = 0;
        center_y = 0;

        need_draw_card = true;
    }
}
