package durakonline.sk.durakonline;

import android.graphics.Matrix;

/**
 * Created by sk on 08.10.17.
 * Позиция карты на игровом поле
 * Пара карт в позиции
 */

public class ItemBoardCard
{
    public CardDrawInfo c1, c2;
    public boolean need_draw1, need_draw2;
    public Matrix matrix_draw = new Matrix();

    public ItemBoardCard()
    {
        need_draw1 = false;
        need_draw2 = false;
    }
}
