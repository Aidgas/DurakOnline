package durakonline.sk.durakonline.other;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

import durakonline.sk.durakonline.CardDraw_RectPoint;
import durakonline.sk.durakonline.CardInfo;
import durakonline.sk.durakonline.Cards;
import durakonline.sk.durakonline.DrawTextRect;

public class RoomInfo_User
{
    public int uid, info_raiting, info_count_games, info_count_wins, info_count_defeats, info_count_draw;
    public String first_name, last_name;
    public Bitmap user_photo = null;
    public ArrayList<CardInfo> cards_user = new ArrayList<>();             // карты игрока
    public ArrayList<CardDraw_RectPoint> cards_points = new ArrayList<>(); // карты координаты угловых точек
    //public ArrayList<Matrix> list_matrix_user_cards = null;

    public float pos_x;  // точка x отрисовка на канве
    public float pos_y;  // точка y отрисовка на канве

    public boolean /*delete_all_cards = false,*/ init_user = false, ready = false, selected = false, exists_in_friends = false, is_bot = false;
    public boolean loaded_user_photo_from_url = false;

    public int offset_position_num; // позиция в комнате с 1

    public int tmp_count_cards = 0; // карт у игрока
    public Path timer_path = new Path();

    // TIMER XOD
    private long start_timer_tms = 0;
    private long count_second = 0;
    public boolean timerWork = false;
    public boolean drawUserCards = false;
    public boolean cached_draw_my_cards = false;

    public DrawTextRect draw_bery_text = null;

    public Rect uname_draw_rect;
    public RectF mOval = null;

    public void resetAll()
    {
        cards_user = new ArrayList<>();
        cards_points = new ArrayList<>();
        tmp_count_cards = 0;
    }

    public void resetMatrixDrawCardsUser()
    {
        cached_draw_my_cards = false;

        for(int i = 0, _len = cards_user.size(); i < _len; i++)
        {
            cards_user.get(i).matrix_draw = null;
        }
    }

    public void startTimer(int count_second)
    {
        this.count_second = count_second * 1000;
        start_timer_tms = System.currentTimeMillis();
        timerWork = true;
    }

    public void stopTimer()
    {
        timerWork = false;
    }

    // return porcent complete
    public int checkTimer()
    {
        long diff = System.currentTimeMillis() - start_timer_tms;

        if( diff >= count_second )
        {
            timerWork = false;
            return 360;
        }
        else
        {
            return (int) ( diff * 360.0f / count_second );
        }
    }

    public void sortCardsABC()
    {
        for(int i = 0, _len = cards_user.size(); i < _len - 1; i++)
        {
            for(int j = i + 1; j < _len; j++)
            {
                if( cards_user.get(i).sort_num > cards_user.get(j).sort_num )
                {
                    CardInfo tmp = cards_user.get(i);
                    CardDraw_RectPoint tmp2 = cards_points.get(i);

                    cards_user.set(i, cards_user.get(j));
                    cards_user.set(j, tmp);

                    cards_points.set(i, cards_points.get(j));
                    cards_points.set(j, tmp2);
                }
            }
        }
    }

    public void sortCardsDESC()
    {
        for(int i = 0, _len = cards_user.size(); i < _len - 1; i++)
        {
            for(int j = i + 1; j < _len; j++)
            {
                if( cards_user.get(i).sort_num < cards_user.get(j).sort_num )
                {
                    CardInfo tmp = cards_user.get(i);
                    CardDraw_RectPoint tmp2 = cards_points.get(i);

                    cards_user.set(i, cards_user.get(j));
                    cards_user.set(j, tmp);

                    cards_points.set(i, cards_points.get(j));
                    cards_points.set(j, tmp2);
                }
            }
        }
    }

    public void sortTrumpCardsLeft(int trump_card)
    {
        CardInfo t = Cards.getInfoCard(trump_card);

        ArrayList<CardInfo> list_cards                   = new ArrayList<>();
        ArrayList<CardDraw_RectPoint> list_cards_rects   = new ArrayList<>();

        boolean found;

        while (true)
        {
            found = false;

            for(int i = 0; i < cards_user.size(); i++)
            {
                if(cards_user.get(i).type == t.type)
                {
                    list_cards.add( cards_user.get(i) );
                    list_cards_rects.add( cards_points.get(i) );

                    cards_user.remove(i);
                    cards_points.remove(i);

                    found = true;
                    break;
                }
            }

            if( ! found )
            {
                break;
            }
        }

        for(int i = list_cards.size() - 1; i >= 0; i--)
        {
            cards_user.add( 0, list_cards.get(i) );
            cards_points.add(0, list_cards_rects.get(i));
        }
    }

    public void sortTrumpCardsOnRight(int trump_card)
    {
        CardInfo t = Cards.getInfoCard(trump_card);

        ArrayList<CardInfo> list_cards                   = new ArrayList<>();
        ArrayList<CardDraw_RectPoint> list_cards_rects   = new ArrayList<>();

        boolean found;

        while (true)
        {
            found = false;

            for(int i = 0, _len = cards_user.size(); i < _len; i++)
            {
                if(cards_user.get(i).type == t.type)
                {
                    list_cards.add( cards_user.get(i) );
                    list_cards_rects.add( cards_points.get(i) );

                    cards_user.remove(i);
                    cards_points.remove(i);

                    found = true;
                    break;
                }
            }

            if( ! found )
            {
                break;
            }
        }

        for(int i = 0; i < list_cards.size(); i++)
        {
            cards_user.add( list_cards.get(i) );
            cards_points.add(list_cards_rects.get(i));
        }
    }

    public int countDrawCards()
    {
        int result = 0;

        for(int i = 0, _len = cards_points.size(); i < _len; i++)
        {
            if( cards_points.get(i).need_draw_card )
            {
                result += 1;
            }
        }

        return result;
    }
}
