package durakonline.sk.durakonline.other;

import java.util.List;

import durakonline.sk.durakonline.CardDrawInfo;
import durakonline.sk.durakonline.RoomSurfaceView;

/**
 * Created by sk on 23.03.18.
 */

public class AnimationFlyCardsOutOfBoard
{
    private int count_steps;

    private List<CardDrawInfo> cards_on_boars;

    public AnimationFlyCardsOutOfBoard(
              int steps
            , double final_pos_x
            , double final_pos_y
            , List<CardDrawInfo> cards
    )
    {
        this.count_steps = steps;
        this.cards_on_boars = cards;

        for(int i = 0, l = cards_on_boars.size(); i < l; i++)
        {
            double _dx = final_pos_x - cards_on_boars.get(i).draw_pos_x;
            double _dy = final_pos_y - cards_on_boars.get(i).draw_pos_y;

            double _dd = RoomSurfaceView.distance_point(cards_on_boars.get(i).draw_pos_x, cards_on_boars.get(i).draw_pos_y, final_pos_x, final_pos_y);

            _dx /= _dd;
            _dy /= _dd;

            _dx *= _dd / (double) count_steps;
            _dy *= _dd / (double) count_steps;

            cards_on_boars.get(i).animation_step_x = (float)_dx;
            cards_on_boars.get(i).animation_step_y = (float)_dy;
        }
    }

    public boolean run_step()
    {
        for(int i = 0, l = cards_on_boars.size(); i < l; i++)
        {
            cards_on_boars.get(i).animation_offset_x += cards_on_boars.get(i).animation_step_x;
            cards_on_boars.get(i).animation_offset_y += cards_on_boars.get(i).animation_step_y;
        }

        this.count_steps -= 1;

        if( this.count_steps <= 0 )
        {
            return false;
        }

        return true;
    }
}
