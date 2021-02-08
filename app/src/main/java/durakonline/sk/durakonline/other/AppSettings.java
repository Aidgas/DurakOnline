package durakonline.sk.durakonline.other;

import java.util.ArrayList;
import java.util.List;

public class AppSettings
{
    public static final int SORT_MY_CARDS_OFF    = 0x00;
    public static final int SORT_MY_CARDS_ABC    = 0x01;
    public static final int SORT_MY_CARDS_DESC   = 0x02;

    public static final int SORT_TRUMP_CARDS_LEFT    = 0x00;
    public static final int SORT_TRUMP_CARDS_RIGHT   = 0x01;
    public static final int SORT_TRUMP_CARDS_OFF     = 0x02;

    //public static final int TYPE_OPT__TYPE_SKIN           = 0x01;
    public static final int TYPE_OPT__SORT_MY_CARDS        = 0x02;
    public static final int TYPE_OPT__SORT_TRUMP_CARDS     = 0x03;
    public static final int TYPE_OPT__SOUND_IN_GAME        = 0x04;
    public static final int TYPE_OPT__VIBRATION            = 0x05;
    public static final int TYPE_OPT__HINTS_CARDS          = 0x06;
    public static final int TYPE_OPT__TYPE_BACKCARDS       = 0x07;
    public static final int TYPE_OPT__TYPE_CARDSTYLE       = 0x08;
    public static final int TYPE_OPT__SOUND_READY_STROKE   = 0x09;

    //public int opt_type_skin         = 0;
    public int opt_sort_my_cards     = SORT_MY_CARDS_ABC;
    public int opt_sort_trump_cards  = SORT_TRUMP_CARDS_LEFT;
    public boolean opt_sound_in_game = true;
    public boolean opt_vibration     = true;
    public boolean opt_hints_cards   = false;
    public boolean opt_sound_ready_stroke = false;
    public int opt_type_backcard     = 1;
    public int opt_type_cardsstyle   = 1;
    public long opt_uid              = 0;


    public static final int PURCHASED_GOODS__CARDBACK_1    = 0x01;
    public static final int PURCHASED_GOODS__CARDBACK_2    = 0x02;
    public static final int PURCHASED_GOODS__CARDBACK_3    = 0x03;
    public static final int PURCHASED_GOODS__CARDBACK_4    = 0x04;
    public static final int PURCHASED_GOODS__CARDBACK_5    = 0x05;

    public static final int PURCHASED_GOODS__STYLECARDS_1  = 0x06;
    public static final int PURCHASED_GOODS__STYLECARDS_2  = 0x07;
    public static final int PURCHASED_GOODS__STYLECARDS_3  = 0x08;

    public List<Integer> mylist_purchased_goods = new ArrayList<>();

    //private MainActivity ma;
    //public DB db = null;

    public AppSettings()
    {
        MyListPurchasedReset();

        //this.ma = ma;

        //db = new DB(ct);
        //db._db_cteate_tables();
    }

    public void MyListPurchasedReset()
    {
        mylist_purchased_goods.clear();
        mylist_purchased_goods.add(AppSettings.PURCHASED_GOODS__CARDBACK_1);
        mylist_purchased_goods.add(AppSettings.PURCHASED_GOODS__CARDBACK_2);
        mylist_purchased_goods.add(AppSettings.PURCHASED_GOODS__STYLECARDS_1);
    }

    /*public long getAccountInfoUid()
    {
        return Long.parseLong( db.getKeyValue("account_info.uid", "0"));
    }

    public void setAccountInfoUid(long uid)
    {
        db.setKeyValue("account_info.uid", String.valueOf( uid));
    }

    public void saveTypeSkin(int v)
    {
        this.type_skin = v;

        db.setKeyValue("type_skin", String.valueOf( this.type_skin));
    }

    public int getTypeSkin()
    {
        return Integer.parseInt( db.getKeyValue("type_skin", String.valueOf( TYPE_SKIN_DAY)));
    }

    public void saveSortMyCards(int v)
    {
        this.sort_my_cards = v;

        db.setKeyValue("sort_my_cards", String.valueOf( this.sort_my_cards));
    }

    public int getSortMyCards()
    {
        return Integer.parseInt( db.getKeyValue("sort_my_cards", String.valueOf( SORT_MY_CARDS_ABC)));
    }


    public void saveSortTrumpCards(int v)
    {
        this.sort_trump_cards = v;

        db.setKeyValue("sort_trump_cards", String.valueOf( this.sort_trump_cards));
    }

    public int getSortTrumpCards()
    {
        return Integer.parseInt( db.getKeyValue("sort_trump_cards", String.valueOf( SORT_TRUMP_CARDS_LEFT)));
    }

    public boolean getSetting_SoundInGame()
    {
        return db.getKeyValue( "sound_in_game", "1").equals("1") ? true : false;
    }

    public void setSetting_SoundInGame(boolean v)
    {
        this.sound_in_game = v;
        db.setKeyValue("sound_in_game", v ? "1" : "0");
    }

    public boolean getSetting_Vibration()
    {
        return db.getKeyValue( "vibration", "1").equals("1") ? true : false;
    }

    public void setSetting_Vibration(boolean v)
    {
        this.vibration = v;
        db.setKeyValue("vibration", v ? "1" : "0");
    }

    public boolean getSetting_HintsCards()
    {
        return db.getKeyValue( "hints_cards", "0").equals("1") ? true : false;
    }

    public void setSetting_HintsCards(boolean v)
    {
        this.hints_cards = v;
        db.setKeyValue("hints_cards", v ? "1" : "0");
    }*/
}
