package durakonline.sk.durakonline.other;

import java.util.ArrayList;

import durakonline.sk.durakonline.CardInfo;
import durakonline.sk.durakonline.Cards;
import durakonline.sk.durakonline.ItemBoardCard;
import durakonline.sk.durakonline.OffGameCardItem;

/**
 * Created by sk on 27.09.17.
 * Класс - информация о комнате
 */

public class RoomInfo
{
    // статус игры
    public static final int STATUS_ROOM_WAIT_START    = 0x02;
    public static final int STATUS_ROOM_RUN           = 0x03;

    // тип игры
    public static final int TYPE_GAME__SIMPLE         = 0x15;
    public static final int TYPE_GAME__TRANSFER_FOOL  = 0x16;

    public final static int TOSS_ALL       = 0x01;  // Подкидывают все
    public final static int TOSS_NEAREST   = 0x02;  // Подкидывают соседи

    public int count_deck_of_cards;    // карт в колоде осталось
    public int real_count_cards_off_game;    // карт вне колоды реалньо

    public ArrayList<OffGameCardItem> cards_off_game;   // карт вне игры (random)

    public int suit_of_the_card;       // масть козырной карты
    public int trump_card_in_the_deck; // козырная карта в колоде (отображаеться внизу)

    public int rate, count_users, count_cards, type_game, toss, have_password, current_my_position;
    public int current_pos_user_xod, current_pos_user_beat, current_pos_user_main_xod, status_room, count_bito_bery;

    public String current_user_course_personId; // текущий ход пользователя

    public ArrayList<RoomInfo_User> users_in_room;  // пользователи в игре

    public ArrayList<ItemBoardCard> cards_boards;   // карты на столе

    public Object lock_draw;
    public Object lock_list_matrix_users_cards;
    public Object lock_cards_boards;
    public Object card_off_game_lock = new Object();

    public RoomInfo_User my_userinfo;
    public boolean cache_draw_cards_board = false;

    public RoomInfo()
    {
        users_in_room   = new ArrayList<>();
        lock_draw         = new Object();
        lock_list_matrix_users_cards = new Object();
        lock_cards_boards = new Object();
        cards_boards    = new ArrayList<>();
        cards_off_game  = new ArrayList<>();
        my_userinfo     = new RoomInfo_User();
    }

    public void resetAll()
    {
        trump_card_in_the_deck = 0;

        synchronized (card_off_game_lock)
        {
            cards_off_game = new ArrayList<>();
        }

        synchronized (lock_cards_boards)
        {
            cards_boards = new ArrayList<>();
        }

        synchronized (lock_draw)
        {
            my_userinfo.resetAll();
        }

        for(int i = 0; i < users_in_room.size(); i++)
        {
            users_in_room.get(i).resetAll();
            //users_in_room.get(i).init_user = false;
            //users_in_room.get(i).loaded_user_photo_from_url = false;
        }
    }

    public boolean allUsersInit()
    {
        for(int i = 0; i < users_in_room.size(); i++)
        {
            if( ! users_in_room.get(i).init_user )
            {
                return false;
            }
        }

        return true;
    }

    public boolean validateTossXodOnBoard2(int card)
    {
        CardInfo ci = Cards.getInfoCard(card);

        if(ci == null || type_game != RoomInfo.TYPE_GAME__TRANSFER_FOOL)
        {
            return false;
        }

        for(int i = 0; i < cards_boards.size(); i++ )
        {
            if(        cards_boards.get(i).c1 != null
                    && cards_boards.get(i).c2 == null
                    && cards_boards.get(i).c1.card_info != null
                    && cards_boards.get(i).c1.card_info.sort_num == ci.sort_num )
            {
                return true;
            }
        }

        return false;
    }

    public boolean inBoardHaveTrumpCard()
    {
        CardInfo trump_card = Cards.getInfoCard(trump_card_in_the_deck);

        for(int i = 0; i < cards_boards.size(); i++ )
        {
            if(        cards_boards.get(i).c1 != null
                    && cards_boards.get(i).c1.card_info != null
                    && cards_boards.get(i).c1.card_info.type == trump_card.type )
            {
                return true;
            }
        }

        return false;
    }

    /// можно ли подкинуть карту
    public boolean validateTossXodOnBoard(int card)
    {
        CardInfo ci = Cards.getInfoCard(card);

        if(ci == null)
        {
            return false;
        }

        for(int i = 0; i < cards_boards.size(); i++ )
        {
            if(        cards_boards.get(i).c1 != null
                    && cards_boards.get(i).c1.card_info != null
                    && cards_boards.get(i).c1.card_info.sort_num == ci.sort_num )
            {
                return true;
            }

            if(        cards_boards.get(i).c2 != null
                    && cards_boards.get(i).c2.card_info != null
                    && cards_boards.get(i).c2.card_info.sort_num == ci.sort_num )
            {
                return true;
            }
        }

        return false;
    }

    public boolean myUserHaveCardsTossed()
    {
        boolean result = false;

        for(int i = 0; i < my_userinfo.cards_user.size(); i++)
        {
            if( validateTossXodOnBoard( my_userinfo.cards_user.get(i).card ) )
            {
                result = true; break;
            }
        }

        return result;
    }

    public void resetUsersInRoomDrawTextBery()
    {
        for(int k = 0, l = users_in_room.size(); k < l; k++)
        {
            users_in_room.get(k).draw_bery_text = null;
        }
    }

    public void resetAllUserListMatrixCards()
    {
        for(int k = 0, l = users_in_room.size(); k < l; k++)
        {
            users_in_room.get(k).mOval = null;
        }
    }

    public void resetAllusersCountCards()
    {
        for(int k = 0, l = users_in_room.size(); k < l; k++)
        {
            users_in_room.get(k).tmp_count_cards = 0;
        }
    }

    public void resetAllUsersDrawBeryText()
    {
        for(int k = 0, l = users_in_room.size(); k < l; k++)
        {
            users_in_room.get(k).draw_bery_text = null;
        }
    }

    public void resetAllUsersReady()
    {
        for(int k = 0, l = users_in_room.size(); k < l; k++)
        {
            users_in_room.get(k).ready = false;
        }
    }

    public void setCountCardsInUser(int uid, int count)
    {
        for(int k = 0, l = users_in_room.size(); k < l; k++)
        {
            if( users_in_room.get(k).uid == uid )
            {
                users_in_room.get(k).tmp_count_cards = count;
                return;
            }
        }

        try
        {
            throw new Exception("setCountCardsInUser", new Throwable(String.valueOf(uid)) );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public ItemBoardCard getItem(int card_first_level)
    {
        for(int i = 0; i < cards_boards.size(); i++ )
        {
            if( cards_boards.get(i).c1 != null && cards_boards.get(i).c1.card_info.card == card_first_level )
            {
                return cards_boards.get(i);
            }
        }

        return null;
    }

    public boolean allCardsIsBeated()
    {
        for(int i = 0; i < cards_boards.size(); i++ )
        {
            if( cards_boards.get(i).c1 != null && cards_boards.get(i).c2 == null )
            {
                return false;
            }
        }

        return true;
    }

    public boolean allCardsIsNotBeated()
    {
        for(int i = 0; i < cards_boards.size(); i++ )
        {
            if( cards_boards.get(i).c1 != null && cards_boards.get(i).c2 != null )
            {
                return false;
            }
        }

        return true;
    }

    public int getOneCardNotBeated()
    {
        int result = -1;

        for(int i = 0; i < cards_boards.size(); i++ )
        {
            if ( cards_boards.get(i).c1 != null && cards_boards.get(i).c2 == null )
            {
                if( result == -1 )
                {
                    result = i;
                }
                else
                {
                    return -1;
                }
            }
        }

        return result;
    }

    public int countCardsNotBeatedBoard()
    {
        int res = 0;

        for(int i = 0; i < cards_boards.size(); i++ )
        {
            if( cards_boards.get(i).c2 == null )
            {
                res += 1;
            }
        }

        return res;
    }

    public int getNextPos(int curr_pos)
    {
        if( curr_pos + 1 <= count_users )
        {
            return curr_pos + 1;
        }
        else
        {
            return 1;
        }
    }

}
