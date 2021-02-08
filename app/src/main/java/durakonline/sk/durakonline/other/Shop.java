package durakonline.sk.durakonline.other;

import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import durakonline.sk.durakonline.MainActivity;
import durakonline.sk.durakonline.R;

public class Shop
{
    public static int TYPE_CARD_BACK_1 = 0x01;
    public static int TYPE_CARD_BACK_2 = 0x02;
    public static int TYPE_CARD_BACK_3 = 0x03;
    public static int TYPE_CARD_BACK_4 = 0x04;
    public static int TYPE_CARD_BACK_5 = 0x05;

    public static int TYPE_CARD_STYLE_1 = 0x01;
    public static int TYPE_CARD_STYLE_2 = 0x02;
    public static int TYPE_CARD_STYLE_3 = 0x03;

    public boolean page1_draw_empty = false;

    private MainActivity ma;
    //private DB db;

    public Shop(MainActivity context)
    {
        this.ma = context;




    }

    public LinearLayout drawUiPage2(List<Integer> mylist_purchased_goods)
    {
        LinearLayout result = new LinearLayout(ma);
        result.setOrientation(LinearLayout.VERTICAL);

        HashMap<Integer, ShopItem> costs = new HashMap<>();

        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_1, new ShopItem(1500, R.drawable.r_card) );
        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_2, new ShopItem(1500, R.drawable.r_card2) );
        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_3, new ShopItem(1500, R.drawable.r_card3) );
        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_4, new ShopItem(1500, R.drawable.r_card4) );
        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_5, new ShopItem(1500, R.drawable.r_card5) );

        costs.put( AppSettings.PURCHASED_GOODS__STYLECARDS_1, new ShopItem(2500, R.drawable.option_type_card_1) );
        costs.put( AppSettings.PURCHASED_GOODS__STYLECARDS_2, new ShopItem(2500, R.drawable.option_type_card_2) );
        costs.put( AppSettings.PURCHASED_GOODS__STYLECARDS_3, new ShopItem(2500, R.drawable.option_type_card_3) );

        List<Integer>    goods_ids_1 = new ArrayList<>();
        List<CheckBox> added_btns = new ArrayList<>();

        goods_ids_1.add( AppSettings.PURCHASED_GOODS__CARDBACK_1 );
        goods_ids_1.add( AppSettings.PURCHASED_GOODS__CARDBACK_2 );
        goods_ids_1.add( AppSettings.PURCHASED_GOODS__CARDBACK_3 );
        goods_ids_1.add( AppSettings.PURCHASED_GOODS__CARDBACK_4 );
        goods_ids_1.add( AppSettings.PURCHASED_GOODS__CARDBACK_5 );

        for(int i = 0; i < goods_ids_1.size(); i++)
        {
            if (mylist_purchased_goods.contains( goods_ids_1.get(i) ))
            {
                ShopItem si = costs.get(goods_ids_1.get(i));

                LinearLayout ll_item = new LinearLayout(ma);
                ll_item.setOrientation(LinearLayout.HORIZONTAL);
                ll_item.setPadding(
                           (int) ( 5 * ma.get_scale_px())
                        ,  (int) ( 5 * ma.get_scale_px())
                        ,  (int) ( 5 * ma.get_scale_px())
                        ,  (int) ( 5 * ma.get_scale_px())
                );
                ll_item.setClickable(true);
                ll_item.setBackgroundResource(R.drawable.style_select_menu_1);
                //ll_item.setDuplicateParentStateEnabled(true);
                ll_item.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CheckBox ch = (CheckBox)v.getTag();
                        ch.callOnClick();
                    }
                });


                LinearLayout.LayoutParams lp_rb = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp_rb.gravity = Gravity.CENTER;

                // Create a Radio Button for RadioGroup
                //RadioButton rb_coldfusion = new RadioButton(ma);
                CheckBox rb_coldfusion = new CheckBox(new ContextThemeWrapper(ma, R.style.MyCheckbox), null, R.style.MyCheckbox);

                rb_coldfusion.setLayoutParams(lp_rb);
                rb_coldfusion.setClickable(true);
                rb_coldfusion.setPadding((int) (3 * ma.get_scale_px()), (int) (3 * ma.get_scale_px()), (int) (3 * ma.get_scale_px()), (int) (3 * ma.get_scale_px()));
                rb_coldfusion.setTag( goods_ids_1.get(i) );

                ll_item.addView(rb_coldfusion, lp_rb);

                if( ma.getAppSettings().opt_type_backcard == goods_ids_1.get(i) )
                {
                    rb_coldfusion.setChecked(true);
                }

                ImageView iv = new ImageView(ma);

                LinearLayout.LayoutParams lp_iv = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) (50 * ma.get_scale_px()));
                lp_iv.gravity = Gravity.CENTER;
                lp_iv.leftMargin = (int)( 7 * ma.get_scale_px() );

                iv.setLayoutParams(lp_iv);
                iv.setImageResource( si.img_res_id );
                iv.setAdjustViewBounds(true);

                ll_item.addView(iv);
                ll_item.setTag(rb_coldfusion);

                result.addView(ll_item);

                added_btns.add( rb_coldfusion );

                TableRow tr2 = new TableRow(ma);
                tr2.setBackgroundColor(Color.parseColor("#111111"));
                tr2.setPadding(0, 0, 0, 1);

                result.addView(tr2);
            }
        }

        GCheckboxGroup grg = new GCheckboxGroup(added_btns)
        {
            @Override
            public void callback_checkboxClicked(CheckBox ch)
            {
                Integer v = (Integer)ch.getTag();

                ma.getAppSettings().opt_type_backcard = v;
                ma.sendSaveOption( AppSettings.TYPE_OPT__TYPE_BACKCARDS, String.valueOf(v) );
            }
        };

        //******************************************************************************************

        List<Integer>    goods_ids_2 = new ArrayList<>();
        List<CheckBox> added_btns2   = new ArrayList<>();

        goods_ids_2.add( AppSettings.PURCHASED_GOODS__STYLECARDS_1 );
        goods_ids_2.add( AppSettings.PURCHASED_GOODS__STYLECARDS_2 );
        goods_ids_2.add( AppSettings.PURCHASED_GOODS__STYLECARDS_3 );

        for(int i = 0; i < goods_ids_2.size(); i++)
        {
            if (mylist_purchased_goods.contains( goods_ids_2.get(i) ))
            {
                ShopItem si = costs.get(goods_ids_2.get(i));

                LinearLayout ll_item = new LinearLayout(ma);
                ll_item.setOrientation(LinearLayout.HORIZONTAL);
                ll_item.setPadding(
                        (int) ( 5 * ma.get_scale_px())
                        ,  (int) ( 5 * ma.get_scale_px())
                        ,  (int) ( 5 * ma.get_scale_px())
                        ,  (int) ( 5 * ma.get_scale_px())
                );
                ll_item.setClickable(true);
                ll_item.setBackgroundResource(R.drawable.style_select_menu_1);
                //ll_item.setDuplicateParentStateEnabled(true);
                ll_item.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CheckBox ch = (CheckBox)v.getTag();
                        ch.callOnClick();
                    }
                });


                LinearLayout.LayoutParams lp_rb = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp_rb.gravity = Gravity.CENTER;

                // Create a Radio Button for RadioGroup
                //RadioButton rb_coldfusion = new RadioButton(ma);
                CheckBox rb_coldfusion = new CheckBox(new ContextThemeWrapper(ma, R.style.MyCheckbox), null, R.style.MyCheckbox);

                rb_coldfusion.setLayoutParams(lp_rb);
                rb_coldfusion.setClickable(true);
                rb_coldfusion.setPadding((int) (3 * ma.get_scale_px()), (int) (3 * ma.get_scale_px()), (int) (3 * ma.get_scale_px()), (int) (3 * ma.get_scale_px()));
                rb_coldfusion.setTag( goods_ids_1.get(i) );

                ll_item.addView(rb_coldfusion, lp_rb);

                if( ma.getAppSettings().opt_type_cardsstyle == goods_ids_1.get(i) )
                {
                    rb_coldfusion.setChecked(true);
                }

                ImageView iv = new ImageView(ma);

                LinearLayout.LayoutParams lp_iv = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) (50 * ma.get_scale_px()));
                lp_iv.gravity = Gravity.CENTER;
                lp_iv.leftMargin = (int)( 7 * ma.get_scale_px() );

                iv.setLayoutParams(lp_iv);
                iv.setImageResource( si.img_res_id );
                iv.setAdjustViewBounds(true);

                ll_item.addView(iv);
                ll_item.setTag(rb_coldfusion);

                result.addView(ll_item);

                added_btns2.add( rb_coldfusion );

                TableRow tr2 = new TableRow(ma);
                tr2.setBackgroundColor(Color.parseColor("#111111"));
                tr2.setPadding(0, 0, 0, 1);

                result.addView(tr2);
            }
        }

        GCheckboxGroup grg2 = new GCheckboxGroup(added_btns2)
        {
            @Override
            public void callback_checkboxClicked(CheckBox ch)
            {
                Integer v = (Integer)ch.getTag();

                ma.getAppSettings().opt_type_cardsstyle = v;
                ma.sendSaveOption( AppSettings.TYPE_OPT__TYPE_CARDSTYLE, String.valueOf(v) );
            }
        };

        return result;
    }

    public LinearLayout drawUiPage1(List<Integer> mylist_purchased_goods)
    {
        LinearLayout result = new LinearLayout(ma);
        result.setOrientation(LinearLayout.VERTICAL);

        HashMap<Integer, ShopItem> costs = new HashMap<>();

        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_1, new ShopItem(1500, R.drawable.r_card) );
        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_2, new ShopItem(1500, R.drawable.r_card2) );
        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_3, new ShopItem(1500, R.drawable.r_card3) );
        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_4, new ShopItem(1500, R.drawable.r_card4) );
        costs.put( AppSettings.PURCHASED_GOODS__CARDBACK_5, new ShopItem(1500, R.drawable.r_card5) );

        costs.put( AppSettings.PURCHASED_GOODS__STYLECARDS_1, new ShopItem(2500, R.drawable.option_type_card_1) );
        costs.put( AppSettings.PURCHASED_GOODS__STYLECARDS_2, new ShopItem(2500, R.drawable.option_type_card_2) );
        costs.put( AppSettings.PURCHASED_GOODS__STYLECARDS_3, new ShopItem(2500, R.drawable.option_type_card_3) );

        int count_draw = 0;

        for(Map.Entry<Integer, ShopItem> entry : costs.entrySet())
        {
            Integer key    = entry.getKey();
            final ShopItem shop_item  = entry.getValue();

            if( ! mylist_purchased_goods.contains(key) )
            {
                count_draw += 1;

                LinearLayout.LayoutParams lp_item = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                lp_item.bottomMargin = (int)( 2 * ma.get_scale_px() );


                LinearLayout item = new LinearLayout(ma);
                item.setOrientation(LinearLayout.HORIZONTAL);
                item.setLayoutParams(lp_item);
                item.setBackgroundResource(R.drawable.style_select_menu_1);
                item.setClickable(true);

                item.setPadding( (int) (ma.get_scale_px() * 10), (int) (ma.get_scale_px() * 5), (int) (ma.get_scale_px() * 3), (int) (ma.get_scale_px() * 5) );

                {
                    LinearLayout.LayoutParams lp_tv = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1 );
                    lp_tv.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                    lp_tv.rightMargin = (int)( 5 * ma.get_scale_px() );

                    TextView tv = new TextView(ma);
                    tv.setTypeface(ma.get_fontAppBold());
                    tv.setText( String.valueOf(shop_item.cost) + " " + Utils.getCorrectSuffix(shop_item.cost, ma.getString(R.string.txt_68), ma.getString(R.string.txt_69), ma.getString(R.string.txt_70)) );
                    tv.setLayoutParams( lp_tv );
                    tv.setTextSize(19);
                    tv.setTextColor(Color.WHITE);

                    item.addView(tv);


                    LinearLayout.LayoutParams lp_iv = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, (int)( 50 * ma.get_scale_px() ) );
                    lp_iv.gravity = Gravity.CENTER;

                    ImageView iv = new ImageView(ma);
                    iv.setImageResource(shop_item.img_res_id);
                    iv.setLayoutParams(lp_iv);
                    iv.setAdjustViewBounds(true);

                    item.addView(iv);
                }

                item.setTag( key );
                item.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ma.sendBuyGoods(v, (Integer)(v.getTag()) );
                    }
                });

                result.addView(item);

                TableRow tr2 = new TableRow(ma);
                tr2.setBackgroundColor(Color.parseColor("#111111"));
                tr2.setPadding(0, 0, 0, 1);

                result.addView(tr2);
            }
        }

        page1_draw_empty = count_draw == 0;

        if( count_draw == 0 )
        {
            TextView tv = new TextView(ma);
            tv.setText( ma.getString(R.string.txt_115 ) );
            tv.setTypeface( ma.get_fontAppBold() );
            tv.setTextSize( 18 );
            tv.setTextColor(Color.WHITE);
            tv.setPadding(
                    (int)( 10 * ma.get_scale_px() )
                    , (int)( 10 * ma.get_scale_px() )
                    , (int)( 10 * ma.get_scale_px() )
                    , (int)( 10 * ma.get_scale_px() )
            );

            result.addView(tv);
        }


        return result;
    }

}
