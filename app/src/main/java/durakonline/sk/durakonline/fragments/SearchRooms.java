package durakonline.sk.durakonline.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import durakonline.sk.durakonline.ClassNetWork;
import durakonline.sk.durakonline.MainActivity;
import durakonline.sk.durakonline.ProtokolUtils;
import durakonline.sk.durakonline.QueueNetwork;
import durakonline.sk.durakonline.R;
import durakonline.sk.durakonline.ReadCommand;
import durakonline.sk.durakonline.other.RoomInfo;
import durakonline.sk.durakonline.other.SearchRoomInfo;
import durakonline.sk.durakonline.other.Utils;

/**
 * Created by sk on 25.02.18.
 */

public class SearchRooms extends Fragment
{
    private View    myView = null;
    private LinearLayout ll_list_main = null;
    private Thread  thread_load_rooms;
    private MainActivity m;

    final List<SearchRoomInfo> need_add        = new ArrayList<>();
    final List<Long> need_delete_list          = new ArrayList<>();

    private Object lock_need_add          = new Object();
    private Object lock_need_delete_list  = new Object();

    boolean flag = false;

    private class UiListRooms
    {
        LinearLayout ll;
        TableRow tr;
        TextView tv_count_user_now;
        SearchRoomInfo info;
    }

    private HashMap<Long, UiListRooms> list_main  = new HashMap<>();
    private Object lock_list_main                 = new Object();

    private long current_room_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        myView        = inflater.inflate(R.layout.fragment_serach_rooms, container, false);
        ll_list_main  = (LinearLayout) myView.findViewById(R.id.ll_fragment_search_rooms_list);

        m = (MainActivity)getActivity();

        TextView cancel = myView.findViewById(R.id.tv_page_search_alert_dialog_cancel);
        TextView ok_btn = myView.findViewById(R.id.tv_page_search_alert_dialog_ok);

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FrameLayout fl = myView.findViewById(R.id.fl_page_search_input_password);
                fl.setVisibility(View.GONE);

                EditText et = myView.findViewById(R.id.et_page_search_alert_dialog_password);
                //Utils.hideSoftKeyboard( getActivity(), et );
                m.hideKeyboard(et);
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText et = myView.findViewById(R.id.et_page_search_alert_dialog_password);
                String password = et.getText().toString().trim();


                if (password.length() > 2)
                {
                    m.UiPageSearchShowHideWait(true);

                    ClassNetWork network = m.getNetwork();

                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    try
                    {
                        buffer.write(ByteBuffer.allocate(4).putInt( ProtokolUtils.htonl( (int)m.getAccoutInfo().uid )).array());
                        buffer.write(ByteBuffer.allocate(8).putLong( ProtokolUtils.htonl2( current_room_id )).array());

                        buffer.write(ByteBuffer.allocate(4).putInt( ProtokolUtils.htonl(  m.getSessionInfo().session_socket_id )).array());

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    ArrayList<String> params = new ArrayList<String>();
                    params.add(password);

                    network.queue_network.add("CONNECTION_ROOM"
                            , params
                            , buffer.toByteArray()
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__LONG);

                    FrameLayout fl = myView.findViewById(R.id.fl_page_search_input_password);
                    fl.setVisibility(View.GONE);

                    //Utils.hideSoftKeyboard( getActivity(), et );
                    m.hideKeyboard(et);
                }
            }
        });

        return myView;
    }

    public int UiUpdateAll(final ReadCommand r)
    {
        int offset = 0;
        short count_rooms = 0;

        byte[] _len = ByteBuffer.allocate(2).array();
        System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

        ByteBuffer buffer = ByteBuffer.wrap(_len);
        buffer.order(ByteOrder.BIG_ENDIAN);

        count_rooms = buffer.getShort();

        //-----------------------------------------
        List<SearchRoomInfo> loaded = new ArrayList<>();

        for(int i = 0; i < count_rooms; i++)
        {
            SearchRoomInfo tmp = new SearchRoomInfo();

            byte[] buf = new byte[8];
            System.arraycopy(r.data, offset, buf, 0, 8); offset += 8;

            /*BigInteger unsigned = new BigInteger(1, buf);

            tmp.room_id =   unsigned.longValue();*/

            tmp.room_id = ProtokolUtils.htonl2( ByteBuffer.wrap(buf).getLong() );

            _len = ByteBuffer.allocate(1).array();
            System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

            tmp.have_password = _len[0];

            _len = ByteBuffer.allocate(1).array();
            System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

            tmp.type_game = _len[0];

            _len = ByteBuffer.allocate(1).array();
            System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

            tmp.toss = _len[0];

            _len = ByteBuffer.allocate(1).array();
            System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

            tmp.count_users = _len[0];

            _len = ByteBuffer.allocate(1).array();
            System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

            tmp.now_count_users = _len[0];

            _len = ByteBuffer.allocate(4).array();
            System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

            buffer = ByteBuffer.wrap(_len);
            buffer.order(ByteOrder.BIG_ENDIAN);

            tmp.rate = buffer.getInt();


            loaded.add(tmp);
        }

        //-----------------------------------------

        for(int i = 0; i < loaded.size(); i++)
        {
            if( list_main.containsKey( loaded.get(i).room_id ) )
            {
                list_main.get(loaded.get(i).room_id).info.now_count_users = loaded.get(i).now_count_users;
            }
            else
            {
                synchronized (lock_need_add)
                {
                    need_add.add( loaded.get(i) );
                }
            }
        }

        for(Map.Entry<Long, UiListRooms> entry : list_main.entrySet())
        {
            Long key          = entry.getKey();
            UiListRooms value = entry.getValue();

            flag = false;

            for(int i = 0; i < loaded.size(); i++)
            {
                if( value.info.room_id == loaded.get(i).room_id )
                {
                    flag = true;
                    break;
                }
            }

            if( ! flag )
            {
                synchronized (lock_need_delete_list)
                {
                    need_delete_list.add( value.info.room_id );
                }
            }
        }

        loaded = null;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(getActivity() == null)
                {
                    return;
                }

                for(int i = 0; i < need_delete_list.size(); i++)
                {
                    if( list_main.containsKey( need_delete_list.get(i) ) )
                    {
                        UiListRooms item = list_main.get( need_delete_list.get(i) );

                        View vv = item.ll;
                        ((ViewGroup) vv.getParent()).removeView(vv);

                        vv = item.tr;
                        ((ViewGroup) vv.getParent()).removeView(vv);

                        synchronized (lock_list_main)
                        {
                            list_main.remove( need_delete_list.get(i) );
                        }
                    }
                }

                synchronized (lock_need_delete_list)
                {
                    need_delete_list.clear();
                }

                for(int i = 0; i < need_add.size(); i++)
                {
                    LinearLayout ll_add = new LinearLayout( getActivity() );
                    ll_add.setOrientation(LinearLayout.HORIZONTAL);

                    ll_add.setTag( need_add.get(i) );
                    ll_add.setClickable(true);
                    ll_add.setBackgroundResource(R.drawable.style_select_menu_1);
                    ll_add.setPadding(
                              (int) Utils.dipToPixels( getContext(), 15 )
                            , (int) Utils.dipToPixels( getContext(), 12 )
                            , (int) Utils.dipToPixels( getContext(), 5 )
                            , (int) Utils.dipToPixels( getContext(), 12 )
                    );

                        if( need_add.get(i).have_password == 1 )
                        {
                            LinearLayout.LayoutParams lp_iv = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                            lp_iv.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                            lp_iv.rightMargin = (int) Utils.dipToPixels( getContext(), 10);

                            ImageView iv_0 = new ImageView(getActivity());
                            iv_0.setLayoutParams(lp_iv);

                            iv_0.setImageResource(R.drawable.lock);

                            ll_add.addView(iv_0);
                            ///---------------------------------------
                        }

                        LinearLayout.LayoutParams lp_iv_1 = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                        lp_iv_1.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                        lp_iv_1.rightMargin = (int) Utils.dipToPixels(getContext(), 7);

                        ImageView iv_1 = new ImageView(getActivity());
                        iv_1.setLayoutParams(lp_iv_1);

                        iv_1.setImageResource(R.drawable.money);
                        iv_1.setMaxHeight( (int) Utils.dipToPixels(getContext(), 17) );

                        ll_add.addView(iv_1);
                        ///---------------------------------------

                        LinearLayout.LayoutParams lp_tv_0 = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                        lp_tv_0.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

                        TextView tv_0 = new TextView(getActivity());
                        tv_0.setLayoutParams(lp_tv_0);

                        tv_0.setTextSize(22);
                        tv_0.setText( String.valueOf( need_add.get(i).rate ) + " / " );
                        tv_0.setTextColor(Color.WHITE);
                        tv_0.setTypeface( m.get_fontAppBold() );

                        ll_add.addView(tv_0);
                        ///---------------------------------------


                        LinearLayout.LayoutParams lp_tv_1 = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                        lp_tv_1.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

                        TextView tv_1 = new TextView(getActivity());
                        tv_1.setLayoutParams(lp_tv_1);

                        tv_1.setTextSize(22);
                        tv_1.setTypeface( m.get_fontAppBold() );
                        tv_1.setTextColor(Color.WHITE);

                        if( need_add.get(i).type_game == RoomInfo.TYPE_GAME__SIMPLE)
                        {
                            tv_1.setText( getString(R.string.txt_21) + " / ");
                        }
                        else
                        {
                            tv_1.setText( getString(R.string.txt_22) + " / ");
                        }

                        ll_add.addView(tv_1);
                        ///---------------------------------------

                        LinearLayout.LayoutParams lp_iv_2 = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                        lp_iv_2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                        lp_iv_2.rightMargin = (int) Utils.dipToPixels(getContext(), 5);
                        //lp_iv_2.leftMargin = (int) Utils.dipToPixels(getContext(), 1);

                        ImageView iv_2 = new ImageView(getActivity());
                        iv_2.setLayoutParams(lp_iv_2);

                        iv_2.setImageResource(R.drawable.user10);
                        iv_2.setMaxHeight( (int) Utils.dipToPixels(getContext(), 17) );

                        ll_add.addView(iv_2);


                        LinearLayout.LayoutParams lp_tv_2 = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                        lp_tv_2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

                        TextView tv_2 = new TextView(getActivity());
                        tv_2.setLayoutParams(lp_tv_2);

                        tv_2.setTextSize(22);

                        tv_2.setText( String.valueOf( need_add.get(i).count_users ) + " - ");
                        tv_2.setTypeface( m.get_fontAppBold() );
                        tv_2.setTextColor(Color.WHITE);

                        ll_add.addView(tv_2);
                        ///---------------------------------------

                        LinearLayout.LayoutParams lp_tv_4 = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                        lp_tv_4.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

                        TextView tv_4 = new TextView(getActivity());
                        tv_4.setLayoutParams(lp_tv_4);

                        tv_4.setTextSize(22);

                        tv_4.setText( String.valueOf( need_add.get(i).now_count_users ) );
                        tv_4.setTypeface( m.get_fontAppBold() );
                        tv_4.setTextColor(Color.YELLOW);

                        ll_add.addView(tv_4);
                        ///---------------------------------------

                        LinearLayout.LayoutParams lp_tv_3 = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
                        lp_tv_3.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

                        TextView tv_3 = new TextView(getActivity());
                        tv_3.setLayoutParams(lp_tv_3);

                        tv_3.setTextSize(22);
                        tv_3.setTypeface( m.get_fontAppBold() );
                        tv_3.setTextColor(Color.WHITE);

                        if( need_add.get(i).toss == RoomInfo.TOSS_ALL )
                        {
                            tv_3.setText(" / " + getString(R.string.txt_24) );
                        }
                        else
                        {
                            tv_3.setText(" / " + getString(R.string.txt_25) );
                        }

                        ll_add.addView(tv_3);
                        ///---------------------------------------

                    ll_add.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            final SearchRoomInfo si = (SearchRoomInfo) view.getTag();

                            if(si.have_password == 1)
                            {
                                current_room_id = si.room_id;

                                FrameLayout fl = myView.findViewById(R.id.fl_page_search_input_password);

                                if(fl != null)
                                {
                                    fl.setVisibility(View.VISIBLE);
                                }

                                final EditText et = (EditText) myView.findViewById(R.id.et_page_search_alert_dialog_password);
                                et.requestFocus();

                                InputMethodManager imm = (InputMethodManager) m.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);

                                /*AlertDialog.Builder alertadd = new AlertDialog.Builder(new ContextThemeWrapper( getContext(), R.style.AppTheme ) );

                                LayoutInflater factory = LayoutInflater.from( getContext() );
                                final View in_view = factory.inflate(R.layout.alert_dialog_set_name_file, null);

                                TextView tv = (TextView)view.findViewById(R.id.textView_alert_dialog_set_name_user);
                                tv.setText("Введите пароль");
                                tv.setTypeface( m.get_fontApp() );

                                tv.setGravity(Gravity.CENTER);

                                final EditText et = (EditText) view.findViewById(R.id.editText_alert_dialog_set_name_user);
                                et.setTypeface( m.get_fontApp() );*/

                                /*InputFilter userNameFilter = new InputFilter()
                                {
                                    @Override
                                    public CharSequence filter(CharSequence arg0, int arg1, int arg2, Spanned arg3, int arg4, int arg5)
                                    {
                                        for (int k = arg1; k < arg2; k++)
                                        {
                                            if(
                                                    ( arg0.charAt(k) == ';')
                                                            || ( arg0.charAt(k) == '=')
                                                            || ( arg0.charAt(k) == '+')
                                                            || ( arg0.charAt(k) == '{')
                                                            || ( arg0.charAt(k) == '}')
                                                            || ( arg0.charAt(k) == '&')
                                                            || ( arg0.charAt(k) == '?')
                                                            || ( arg0.charAt(k) == '#')
                                                            || ( arg0.charAt(k) == '@')
                                                            || ( arg0.charAt(k) == '!')
                                                            || ( arg0.charAt(k) == '~')
                                                            || ( arg0.charAt(k) == '/')
                                                            || ( arg0.charAt(k) == '\\')
                                                            || ( arg0.charAt(k) == '\'')
                                                            || ( arg0.charAt(k) == '`')
                                                            || ( arg0.charAt(k) == '*')
                                                    )
                                            {
                                                return "";
                                            }
                                        }
                                        return null;
                                    }
                                };


                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                alertDialog.setTitle(getString(R.string.txt_26));
                                alertDialog.setMessage(getString(R.string.txt_88));

                                final EditText input = new EditText(getContext());
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                input.setLayoutParams(lp);

                                input.setFilters(new InputFilter[]{ userNameFilter });

                                alertDialog.setView(input);
                                //alertDialog.setIcon(R.drawable.key);

                                alertDialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                String password = input.getText().toString();

                                                if (password.length() > 2)
                                                {
                                                    m.UiPageSearchShowHideWait(true);

                                                    ClassNetWork network = m.getNetwork();

                                                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                                                    try
                                                    {
                                                        buffer.write(ByteBuffer.allocate(4).putInt( ProtokolUtils.htonl( (int)m.getAccoutInfo().uid )).array());
                                                        buffer.write(ByteBuffer.allocate(8).putLong( ProtokolUtils.htonl2( si.room_id)).array());

                                                        buffer.write(ByteBuffer.allocate(4).putInt( ProtokolUtils.htonl(  m.getSessionInfo().session_socket_id )).array());

                                                    }
                                                    catch (IOException e)
                                                    {
                                                        e.printStackTrace();
                                                    }

                                                    ArrayList<String> params = new ArrayList<String>();
                                                    params.add(password);

                                                    network.queue_network.add("CONNECTION_ROOM"
                                                            , params
                                                            , buffer.toByteArray()
                                                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                                            , QueueNetwork.TYPE_SEND__LONG);

                                                    dialog.dismiss();
                                                }
                                            }
                                        });

                                alertDialog.setNegativeButton(getString(R.string.txt_89),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                alertDialog.show();*/
                            }
                            else
                            {
                                /*FrameLayout fl_wait = (FrameLayout) getActivity().findViewById(R.id.fl_page_search_wait);
                                if( fl_wait != null )
                                {
                                    fl_wait.setVisibility(View.VISIBLE);
                                }*/

                                m.UiPageSearchShowHideWait(true);

                                ClassNetWork network = m.getNetwork();

                                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                                try
                                {
                                    buffer.write(ByteBuffer.allocate(4).putInt( ProtokolUtils.htonl( (int)m.getAccoutInfo().uid )).array());
                                    buffer.write(ByteBuffer.allocate(8).putLong( ProtokolUtils.htonl2( si.room_id)).array());

                                    buffer.write(ByteBuffer.allocate(4).putInt( ProtokolUtils.htonl(  m.getSessionInfo().session_socket_id )).array());

                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }

                                ArrayList<String> params = new ArrayList<String>();
                                params.add("");

                                network.queue_network.add("CONNECTION_ROOM"
                                        , params
                                        , buffer.toByteArray()
                                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                        , QueueNetwork.TYPE_SEND__LONG);
                            }


                        }
                    });

                    ll_list_main.addView(ll_add);

                    TableRow tr = new TableRow( getActivity() );
                    tr.setPadding(0, 1, 0, 0);
                    tr.setBackgroundColor(Color.GRAY);

                    ll_list_main.addView(tr);

                    UiListRooms _tmp = new UiListRooms();
                    _tmp.ll    = ll_add;
                    _tmp.info  = need_add.get(i);
                    _tmp.tr    = tr;
                    _tmp.tv_count_user_now = tv_4;

                    synchronized (lock_list_main)
                    {
                        list_main.put(_tmp.info.room_id, _tmp);
                    }
                }

                synchronized (lock_need_add)
                {
                    need_add.clear();
                }

                synchronized (lock_list_main)
                {
                    for (Map.Entry<Long, UiListRooms> entry : list_main.entrySet())
                    {
                        Long key = entry.getKey();
                        UiListRooms value = entry.getValue();

                        value.tv_count_user_now.setText( String.valueOf(value.info.now_count_users) );
                    }
                }
            }
        });

        return list_main.size();
    }

    public void runThreadSearch()
    {
        ClassNetWork network = m.getNetwork();

        ArrayList<String> params = new ArrayList<String>();

        params.add( String.valueOf( m.getAccoutInfo().uid) );
        params.add( String.valueOf( m.getSessionInfo().session_socket_id ) );
        params.add( String.valueOf( m.getAccoutInfo().balans ) );

        network.queue_network.add("SEARCH_ROOMS"
                , params
                , null
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if( getActivity() == null ) { return; }

                ImageView iv = (ImageView) getActivity().findViewById(R.id.iv_page_search_tick);

                if( iv != null )
                {
                    iv.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
