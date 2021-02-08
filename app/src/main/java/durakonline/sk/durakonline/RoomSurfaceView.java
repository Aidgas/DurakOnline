package durakonline.sk.durakonline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import durakonline.sk.durakonline.graphic_ui.GButton;
import durakonline.sk.durakonline.graphic_ui.GUiStatus;
import durakonline.sk.durakonline.other.AnimationFlyCardsOutOfBoard;
import durakonline.sk.durakonline.other.AnimationImgToUsers;
import durakonline.sk.durakonline.other.AppSettings;
import durakonline.sk.durakonline.other.ColorBetwheen;
import durakonline.sk.durakonline.other.FlyMoneyInfo;
import durakonline.sk.durakonline.other.LoadingWait;
import durakonline.sk.durakonline.other.Log;
import durakonline.sk.durakonline.other.PointPP;
import durakonline.sk.durakonline.other.RoomInfo;
import durakonline.sk.durakonline.other.RoomInfo_User;
import durakonline.sk.durakonline.other.Utils;

public class RoomSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    public static final long STATUS_ROOM_WAIT_START = 0x02;
    public static final long STATUS_ROOM_RUN = 0x03;

    public static final long TIME_DRAW_NEW_CARD_ICON_IN_MS = 3300;
    public static final long TIME_DRAW_HINT_TOSS_ICON_IN_MS = 5300;

    public static final int ORIENTATION_PORTRAIT = 0x01;
    public static final int ORIENTATION_LANDSCAPE = 0x02;
    // ++ TOUCH
    //private static final long pressedTimeTreshold = 700;    //Если касание длилось меньше 700 мс, то курсор будет выведен ??
    private final Typeface _fontApp_bold;//, _fontApp;
    public String last_cmd_server = "";
    public int canvas_w, canvas_h;
    public int canvas_w2, canvas_h2;

    public class InfoOrientation
    {
        private float PADDING_X_DRAW_CARD;
        private float PADDING_Y_DRAW_CARD;

        int width_draw_card_in_board;
        int card_w, card_h, card_w2, card_h2, card_w4, card_h4;
        int card2_w, card2_h, card2_w2, card2_h2, card2_w4, card2_h4;

        public HashMap<Integer, CardDrawInfo> list_cards_draw = new HashMap<Integer, CardDrawInfo>(); // карты на доске
    }

    public HashMap<Integer, InfoOrientation> infoOrientation = new HashMap<Integer, InfoOrientation>();

    //public HashMap<Integer, CardDrawInfo> list_cards_draw = new HashMap<Integer, CardDrawInfo>();                       // карты на доске
    public HashMap<Integer, CardDrawInfo> list_cards_user = new HashMap<Integer, CardDrawInfo>();  // карты у пользователя

    public HashMap<Integer, Bitmap> list_cards_icons = new HashMap<Integer, Bitmap>();

    public ArrayList<AnimatedFlyDeck> animated_card_desk = new ArrayList<>();

    public boolean    animation_my_animated_card = false
                    , animation_fly_card = false
                    //, animation_fly_card_other_user = false
                    , animation_fly_desc_card = false
                    , animation_fly_card_new_pos = false
                    , animation_fly_card_off_board = false;

    public boolean available_select_my_card = false;
    public AnimationFlyCardsOutOfBoard fly_card_off_board = null;
    public int mode_toss2 = 0; // режим подкидывания после первого хода
    public boolean need_move_cards_on_board = false;
    //public boolean animated_off_board_card_in_board = false;
    //public boolean animated_off_board_card_in_board_reverse_move = false;
    public Paint paint_drawBitmap
            , paint_drawFilterBitmap
            , pain_drawTextCountDesc
            , paint_drawTextUserNikName
            , paint_drawTextUserNikName_2
            , paint_strokeDashed
            , paint_strokeDashed1
            , pain_drawTextInfo
            , pain_userSelected
            , paint_SelectedUser;
    // -- TOUCH
    public boolean _stop_draw = true;
    public float _scale_pixel;
    public ArrayList<AnimatedCard> my_animated_card = new ArrayList<>();
    public Object lock_my_animated_card_desk = new Object();
    public Bitmap joker = null;
    public List<AnimatedCardFly> card_fly_user_xod = new ArrayList<>();
    public boolean draw_trump_card_in_the_deck = false;
    public boolean draw_off_game_cards = false;
    public boolean draw_ready_status_users = true;
    public boolean needRebuildBackground = true;
    public List<AminatedTextRoundedRectangle> adraw_animated_texts = new ArrayList<>();
    public ArrayList<AnimationImgToUsers> list_animation_fly_img = new ArrayList<>();
    public Object lock_animation_fly_img = new Object();
    public boolean flag_animation_fly_img = false
                 , flag_list_animation_text_coints = false
                 , flag_list_animation_number = false
                 , flag_list_animation_image_smile = false
                 , flag_adraw_animated_texts = false
                 , flag_card_fly_user_xod = false
            ;
    public boolean stat_game = false;
    public List<AminatedImageSmile> list_animation_image_smile = new ArrayList<>();
    public Object lock_list_animation_image_smile = new Object();
    public int type_backcard, type_cardstyle, type_selectstyle;
    public int CARD_BLANK;
    public int size_avatar_x, size_avatar_y;
    //int bk_color;
    //int card_w, card_h, card_w2, card_h2, card_w4, card_h4;
    //int card2_w, card2_h, card2_w2, card2_h2, card2_w4, card2_h4;
    int __i, __k, __len;
    //Paint mPaint = null;
    Paint paint_timer_draw = null;
    List<Integer> lines = new ArrayList<>();       // количество карт на линиях
    List<Integer> map = new ArrayList<>();       // номер линии по индексу карты
    List<Integer> index_items = new ArrayList<>();  // номер карты на линии
    long /*counter_draw = 0,*/ room_id = 0;
    float R_MAIN, limit_angle, start, stop, min_step, step;
    double x, y, x0, y0, radius;
    //Matrix matrix;
    //private int PADDING_X_DRAW_CARD = 55;
    //private int PADDING_Y_DRAW_CARD = 70;
    private int PROCENTAGE_MAX_WIDTH_DRAW_CARD_IN_BOARD = 85;
    //private int PADDING_AREA_INSERT_CARD = 50;
    private DrawManagerRoom drawLoopThread = null;
    private SurfaceHolder holder;
    private boolean is_first_draw = true, initAllCards = true;
    //private float scale = 1;
    private float scale_my_cards_pr;
    private float scale_board_cards_pr;
    private float lastX;
    private float lastY; // координаты последнего события
    private float lastUpX, lastMoveX;
    private float lastUpY, lastMoveY;
    private int orietration_screen = -1;
    //private float _screen_offset_w = 0;
    //private float _screen_offset_h = 100;
    //private float _last_screen_offset_w = 0;
    //private float _last_screen_offset_h = 0;
    //private boolean firstMulti = true;   // Показывает, произошло ли multitouch событие при данном вызове функции или еще при предыдущем
    private boolean pressed = false;  // Показывает, а был ли пальчик
    //private boolean zoomTrans = false;  // Транзакция зуммирования (во избежание перемещения сразу после масштабирования, когда пальцы     // отрываешь)
    private boolean canMove = false;
    private float tmp_angle_timer = 0;
    private RoomInfo room_info = new RoomInfo();
    //private float padding_cards_footer = 6.0f;
    private float f = 0;
    //private RoomInfo_User room_user = null;
    private AnimatedCard current_my_animated_card = null;
    private Bitmap _bk = null;
    private Bitmap fade_bk = null;
    private Bitmap default_avatar = null;
    private Bitmap user_card_on_room = null;
    private Bitmap waiting_user = null;
    private Bitmap icon_new_card_in_board = null;
    private Bitmap icon_new_card_in_board2 = null;
    private Bitmap icon_new_tossed_card = null;
    private Bitmap status_ready_play_user = null;
    private Bitmap money_icon = null;
    private Bitmap background_big_img = null;
    private Bitmap window_user_info = null;
    private int pos_x_wondow_user_info, pos_y_wondow_user_info;
    public boolean show_wondow_user_info;
    private Matrix matrix_waiting_user = new Matrix();
    private long time_last_step = 0;
    //private AnimationTimer1 atimer1 = null;
    private Object card_fly_user_xod_lock = new Object();
    private Object lock_hovered = new Object();
    //private float animated_off_board_card_in_board_value = 0;
    private Matrix trump_card_in_the_deck_matrix = null;
    private Matrix deck_rotate_matrix = null; // матрица для колоды карт
    private boolean delete_all_cards_off_game = false;
    private boolean draw_deck = false;
    private boolean draw_my_cards = false;
    private boolean show_hints_cards;
    private RectF drawRectCardBoard = new RectF();
    private PointPP firstDrawCardInBoard, lastDrawCardInBoard;
    private MainActivity _ma;
    private LoadingWait loadingWait;
    private CardDrawInfo hovered = null;
    public Object lock_adraw_animated_texts = new Object();
    private List<AnimationTextAddCoints> list_animation_text_coints = new ArrayList<>();
    private Object lock_list_animation_text_coints = new Object();
    private List<AnimationNumber> list_animation_number = new ArrayList<>();
    private Object lock_list_animation_number = new Object();
    private String type_game_txt = "";
    private String stavka_txt = "";
    //private int color_start_timer_wait_user;//, color_end_timer_wait_user;
    private List<ColorBetwheen.ColorBetwheenStep> list_colors;
    private MediaPlayer cards_thrown;
    /*private int min_screen_length_of_side;
    private int max_screen_length_of_side;
    private int device_screen_size_side_max;
    private int device_screen_size_side_min;*/

    public ThreadInnerAnimationProcess th_in;
    private int cards_boards_size;

    private float cards_boards_draw_first_pos_x = 0;
    private float cards_boards_draw_first_pos_y = 0;
    private float cards_boards_draw_pos_x = 0;
    private float cards_boards_draw_pos_y = 0;

    int cards_boardstotal_width = 0, cards_boards_counter = 0, cards_boards_count_on_line = 0;

    public Object lockSurfaceChanged = new Object();
    public Object lockWorkThreadAnimation = new Object();

    private int my_userinfo_index, my_userinfo_cards_size;
    private double my_userinfo_x0
            , my_userinfo_y0
            , my_userinfo_x1
            , my_userinfo_y1
            , my_userinfo_x1_2
            , my_userinfo_y1_2
            , my_userinfo_l0
            , my_userinfo_l1
            , my_userinfo_l2
            , my_userinfo_end_x0
            , my_userinfo_end_y0
            , my_userinfo_px1
            , my_userinfo_py1
            , my_userinfo_px2
            , my_userinfo_py2
            , my_userinfo_px3
            , my_userinfo_py3
            , my_userinfo_px4
            , my_userinfo_py4
            , my_userinfo_B_alfa;

    //private Canvas TEMP_CANVAS = null;
    //private Bitmap TEMP_BITMAP = null;
    //public boolean drawBkImg = false;

    private Map<String, GButton> ui_buttons =new HashMap<String, GButton>();

    public RoomSurfaceView(
            Context context
            , long room_id
            , int rate
            , int count_users
            , int count_cards
            , int type_game
            , int toss
            , int have_password
            , int current_my_position
            , int trump
            , int count_cards_in_desck
            , List<Short> cards_users
            , boolean show_hints_cards
            , int type_backcard
            , int type_cardstyle
    )
    {
        super(context);

        this.room_id = room_id;
        room_info.rate = rate;
        room_info.count_users = count_users;
        room_info.count_cards = count_cards;
        room_info.type_game = type_game;
        room_info.toss = toss;
        room_info.have_password = have_password;
        room_info.current_my_position = current_my_position;
        room_info.trump_card_in_the_deck = trump;
        room_info.count_deck_of_cards = count_cards_in_desck;

        this.show_hints_cards = show_hints_cards;
        this.type_backcard = type_backcard;
        this.type_cardstyle = type_cardstyle;
        this.type_selectstyle = (type_cardstyle == 1) ? 1 : 2;

        //setZOrderOnTop(true);

        if (type_backcard == 1)
        {
            CARD_BLANK = Cards.BLANK;
        }
        else if (type_backcard == 2)
        {
            CARD_BLANK = Cards.BLANK2;
        }
        else if (type_backcard == 3)
        {
            CARD_BLANK = Cards.BLANK3;
        }
        else if (type_backcard == 4)
        {
            CARD_BLANK = Cards.BLANK4;
        }
        else
        {
            CARD_BLANK = Cards.BLANK5;
        }

        cards_thrown = MediaPlayer.create(context, R.raw.cards_thrown);
        cards_thrown.setVolume(0.18f, 0.18f);

        if (room_info.type_game == RoomInfo.TYPE_GAME__SIMPLE)
        {
            type_game_txt = getContext().getString(R.string.txt_21);
        }
        else
        {
            type_game_txt = getContext().getString(R.string.txt_22);
        }

        if (room_info.rate > 0)
        {
            stavka_txt =
                    getContext().getString(R.string.txt_94)
                  + ": "
                  + String.valueOf(room_info.rate)
                  + " "
                  + Utils.getCorrectSuffix(room_info.rate, getResources().getString(R.string.txt_68), getResources().getString(R.string.txt_69), getResources().getString(R.string.txt_70));
        }
        else
        {
            stavka_txt = "";
        }

        _ma = (MainActivity) context;

        //min_screen_length_of_side = Math.min(MainActivity.screen_height, MainActivity.screen_width);

        for (int i = 0; i < cards_users.size(); i++)
        {
            room_info.my_userinfo.cards_user.add(Cards.getInfoCard(cards_users.get(i)));
            room_info.my_userinfo.cards_points.add(new CardDraw_RectPoint());
        }

        int sotr_cards = _ma.getAppSettings().opt_sort_my_cards;

        if (sotr_cards == AppSettings.SORT_MY_CARDS_ABC)
        {
            room_info.my_userinfo.sortCardsABC();
        }
        else if (sotr_cards == AppSettings.SORT_MY_CARDS_DESC)
        {
            room_info.my_userinfo.sortCardsDESC();
        }

        //******************************************************************************

        int sort_trump_catds = _ma.getAppSettings().opt_sort_trump_cards;

        if (sort_trump_catds == AppSettings.SORT_TRUMP_CARDS_LEFT)
        {
            room_info.my_userinfo.sortTrumpCardsLeft(room_info.trump_card_in_the_deck);
        }
        else if (sort_trump_catds == AppSettings.SORT_TRUMP_CARDS_RIGHT)
        {
            room_info.my_userinfo.sortTrumpCardsOnRight(room_info.trump_card_in_the_deck);
        }

        //BkColor = Color.parseColor("#5b91b6");

        holder = getHolder();
        holder.addCallback(this);

        //bk_color = Color.parseColor("#0a6d00");
        //bk_color = Color.parseColor("#00a2d3");

        paint_drawBitmap = new Paint();
        paint_drawBitmap.setAntiAlias(true);
        //paint_drawBitmap.setFilterBitmap(true);

        paint_drawFilterBitmap = new Paint();
        paint_drawFilterBitmap.setAntiAlias(true);
        //paint_drawFilterBitmap.setFilterBitmap(true);

        //color_start_timer_wait_user = Color.parseColor("#3dc12f");
        //color_end_timer_wait_user = Color.parseColor("#d12f2f");

        _scale_pixel = ((MainActivity) context).get_scale_px();

        paint_timer_draw = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_timer_draw.setStyle(Paint.Style.STROKE);
        //paint_timer_draw.setAntiAlias(true);
        paint_timer_draw.setStrokeWidth(5 * _scale_pixel);
        //paint_timer_draw.setFilterBitmap(true);
        //paint_timer_draw.setDither(true);
        paint_timer_draw.setColor(Color.parseColor("#3dc12f"));
        paint_timer_draw.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        paint_timer_draw.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        paint_timer_draw.setPathEffect(new CornerPathEffect(10) );   // set the path effect when they join.

        list_colors = new ArrayList<>();
        list_colors.add(new ColorBetwheen.ColorBetwheenStep(0.0f, 0x00, 0xff, 0));
        list_colors.add(new ColorBetwheen.ColorBetwheenStep(0.5f, 0xff, 0xff, 0));
        list_colors.add(new ColorBetwheen.ColorBetwheenStep(1.0f, 0xff, 0x00, 0));

        float scale_pr;//  = 60.0f;
        Bitmap bmp;//      = null;

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);

        //device_screen_size_side_max = Math.max(MainActivity.screen_height, MainActivity.screen_width);
        //device_screen_size_side_min = Math.min(MainActivity.screen_height, MainActivity.screen_width);

        double _screenInches = _ma.screenInches;

        if (_screenInches < 5.5f)
        {
            _screenInches = 5.5f;
        }

        scale_pr = (float) (12.5f * _screenInches);

        default_avatar = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);

        size_avatar_x = default_avatar.getWidth();
        size_avatar_y = default_avatar.getHeight();

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromOtherCard(CARD_BLANK));

        scale_pr = (float) (5.7f * _screenInches);

        user_card_on_room = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);


        waiting_user = BitmapFactory.decodeResource(getResources(), R.drawable.waiting_user);
        icon_new_card_in_board = BitmapFactory.decodeResource(getResources(), R.drawable.icon_new_card_in_board);
        icon_new_card_in_board2 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_new_card_in_board2);
        icon_new_tossed_card = BitmapFactory.decodeResource(getResources(), R.drawable.is_tossed);
        status_ready_play_user = BitmapFactory.decodeResource(getResources(), R.drawable.status_ready_user);
        money_icon = BitmapFactory.decodeResource(getResources(), R.drawable.money);
        joker = BitmapFactory.decodeResource(getResources(), R.drawable.joker);

        loadingWait = new LoadingWait(getContext());

        /*atimer1 = new AnimationTimer1(getContext()
                , R.drawable.wait_timer1
                , R.drawable.wait_timer1_arrow);*/

        //matrix_waiting_user.postRotate(0, waiting_user.getWidth()/2, waiting_user.getHeight()/2);


        //bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(Cards.BLANK));


        //_fontApp = Typeface.createFromAsset(((MainActivity) context).getAssets(), "fonts/Roboto-Regular.ttf");
        _fontApp_bold = Typeface.createFromAsset(((MainActivity) context).getAssets(), "fonts/Monitorca-Bd.ttf");

        pain_drawTextCountDesc = new Paint();
        pain_drawTextCountDesc.setColor(Color.BLACK);
        pain_drawTextCountDesc.setTextSize(20 * _scale_pixel);
        pain_drawTextCountDesc.setTypeface(_fontApp_bold);

        if (type_backcard == 4)
        {
            pain_drawTextCountDesc.setColor(Color.parseColor("#2cb715"));
        }
        else if (type_cardstyle != 1)
        {
            pain_drawTextCountDesc.setColor(Color.RED);
        }

        pain_drawTextInfo = new Paint();
        pain_drawTextInfo.setColor(Color.WHITE);
        pain_drawTextInfo.setTextSize(15 * _scale_pixel);
        pain_drawTextInfo.setTypeface(_fontApp_bold);

        pain_userSelected = new Paint();
        pain_userSelected.setColor(Color.RED);
        pain_userSelected.setStyle(Paint.Style.STROKE);
        pain_userSelected.setStrokeWidth(2);

        paint_drawTextUserNikName = new Paint();
        //paint_drawTextUserNikName.setColor( Color.parseColor("#09329b") );
        paint_drawTextUserNikName.setColor(Color.parseColor("#ff0000"));
        paint_drawTextUserNikName.setTextSize(19 * _scale_pixel);
        paint_drawTextUserNikName.setTypeface(_fontApp_bold);

        if (type_backcard == 4)
        {
            paint_drawTextUserNikName.setColor(Color.GRAY);
        }

        paint_drawTextUserNikName_2 = new Paint();
        paint_drawTextUserNikName_2.setColor(Color.GREEN);
        paint_drawTextUserNikName_2.setTextSize(19 * _scale_pixel);
        paint_drawTextUserNikName_2.setTypeface(_fontApp_bold);


        paint_strokeDashed = new Paint();
        paint_strokeDashed.setARGB(255, 0, 0, 0);
        paint_strokeDashed.setStyle(Paint.Style.STROKE);
        paint_strokeDashed.setStrokeWidth(2);
        paint_strokeDashed.setColor(Color.parseColor("#ececec"));
        paint_strokeDashed.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));

        paint_strokeDashed1 = new Paint();
        paint_strokeDashed1.setARGB(255, 0, 0, 0);
        paint_strokeDashed1.setStyle(Paint.Style.STROKE);
        paint_strokeDashed1.setStrokeWidth(2);
        paint_strokeDashed1.setColor(Color.parseColor("#ececec"));
        paint_strokeDashed1.setPathEffect(new DashPathEffect(new float[]{10, 5, 10, 15}, 0));

        paint_SelectedUser = new Paint();
        paint_SelectedUser.setStyle(Paint.Style.STROKE);
        paint_SelectedUser.setStrokeWidth(6);
        paint_SelectedUser.setColor(Color.RED);
        paint_SelectedUser.setAntiAlias(true);

        // инициализация всех позиций для рендеринга
        int pos_tmp = current_my_position;
        for (int g = 0; g < count_users - 1; g++)
        {
            RoomInfo_User t = new RoomInfo_User();
            t.ready = false;

            t.offset_position_num = Utils.getNextPos(pos_tmp, count_users + 1, 1);
            //t.user_photo = getDefaultAvatar().copy( getDefaultAvatar().getConfig(), true);

            t.user_photo = default_avatar.copy(default_avatar.getConfig(), true);

            if (t.offset_position_num == 1)
            {
                pos_tmp = 1;
            }
            else
            {
                pos_tmp += 1;
            }

            room_info.users_in_room.add(t);
        }

        setBk();

        this.drawLoopThread = new DrawManagerRoom(this);
        this.drawLoopThread.setPriority(Thread.MAX_PRIORITY);

        th_in = new ThreadInnerAnimationProcess();
        th_in.start();

        /*long ts = System.currentTimeMillis();

        for( int i = 0; i < 100000; i++ )
        {
            Utils.Q_rsqrt( 10 * Math.random() );
        }

        Log.i("TAG_TS",  String.valueOf( System.currentTimeMillis() - ts ) );

        ts = System.currentTimeMillis();

        for( int i = 0; i < 100000; i++ )
        {
            Math.sqrt( 10 * Math.random() );
        }

        Log.i("TAG_TS",  String.valueOf( System.currentTimeMillis() - ts ) );

        ts = System.currentTimeMillis();

        for( int i = 0; i < 100000; i++ )
        {
            Math.sqrt( 10 * Math.random() );
        }

        Log.i("TAG_TS",  String.valueOf( System.currentTimeMillis() - ts ) );*/

        //------------------------------------------------------------------

        /*Bitmap btn_1 = GButton.generateButton(
                "Добавить в друзья"
                , ( (MainActivity) getContext() ).get_fontAppBold()
                , 60
                , 10 * _scale_pixel
                , 5 * _scale_pixel
                , 10 * _scale_pixel
                , 7 * _scale_pixel
                , Color.parseColor("#00a658")
                , Color.parseColor("#00a658")
                , Color.YELLOW
                , 3 * _scale_pixel
                , 2 * _scale_pixel
        );

        Bitmap btn_2 = GButton.generateButton(
                "Добавить в друзья"
                , ( (MainActivity) getContext() ).get_fontAppBold()
                , 60
                , 10 * _scale_pixel
                , 5 * _scale_pixel
                , 10 * _scale_pixel
                , 7 * _scale_pixel
                , Color.parseColor("#00d872")
                , Color.parseColor("#f4bf1a")
                , Color.YELLOW
                , 3 * _scale_pixel
                , 2 * _scale_pixel
        );

        GButton btn_test =new GButton( btn_1, btn_2 )
        {
            @Override
            public void callbackPressed()
            {
                show_wondow_user_info = false;
            }
        };

        btn_test.setMargin( 5 * _scale_pixel, 5 * _scale_pixel, 5 * _scale_pixel, 5 * _scale_pixel);

        ui_buttons.put("btn_test", btn_test);*/
    }

    public void setBk()
    {
        List<Integer> rand_bk = new ArrayList<>();

        rand_bk.add(R.drawable.bk1004);
        rand_bk.add(R.drawable.bk1005);
        rand_bk.add(R.drawable.bk1006);
        rand_bk.add(R.drawable.bk1007);
        rand_bk.add(R.drawable.bk1008);
        rand_bk.add(R.drawable.bk1009);
        rand_bk.add(R.drawable.bk1010);
        rand_bk.add(R.drawable.bk1011);
        rand_bk.add(R.drawable.bk1013);
        rand_bk.add(R.drawable.bk1015);
        rand_bk.add(R.drawable.bk1016);

        int rand_bk_index = rand_bk.get((new Random()).nextInt(rand_bk.size()));

        /*if (rand_bk_index == 3)
        {
            paint_drawTextUserNikName.setColor(Color.GREEN);
        }*/

        _bk = BitmapFactory.decodeResource(getResources(), rand_bk_index);
    }

    public static int randInt(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static double distance_point(double x0, double y0, double x1, double y1)
    {
        //return Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
        return JniApi.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
    }

    //Фо́рмула Герона позволяет вычислить площадь треугольника (S) по его сторонам a, b, c:
    public static double s_tr(double x1, double y1, double x2, double y2, double x3, double y3)
    {
        double da = RoomSurfaceView.distance_point(x1, y1, x2, y2);
        double db = RoomSurfaceView.distance_point(x2, y2, x3, y3);
        double dc = RoomSurfaceView.distance_point(x1, y1, x3, y3);

        double p = (da + db + dc) / 2;

        return Math.sqrt(p * (p - da) * (p - db) * (p - dc));
        //return Utils.Q_rsqrt(p * (p - da) * (p - db) * (p - dc));
    }

    public static boolean point_in_triangle(double x1, double y1, double x2, double y2, double x3, double y3, double px, double py)
    {
        double p_0 = s_tr(x1, y1, x2, y2, x3, y3);

        double p_1 = s_tr(px, py, x2, y2, x3, y3);
        double p_2 = s_tr(x1, y1, px, py, x3, y3);
        double p_3 = s_tr(x1, y1, x2, y2, px, py);

        //console.log("p: ", p_0, p_1, p_2, p_3, (p_1 + p_2 + p_3));

        return Math.abs(p_0 - (p_1 + p_2 + p_3)) < 1E-8;
    }

    private void initAllCards()
    {
        InfoOrientation info_port = new InfoOrientation();
        InfoOrientation info_land = new InfoOrientation();

        info_port.PADDING_X_DRAW_CARD = 30 * _scale_pixel;
        info_port.PADDING_Y_DRAW_CARD = 40 * _scale_pixel;

        info_land.PADDING_X_DRAW_CARD = 28.7f * _scale_pixel;
        info_land.PADDING_Y_DRAW_CARD = 30 * _scale_pixel;

        infoOrientation.put( ORIENTATION_PORTRAIT, info_port);
        infoOrientation.put( ORIENTATION_LANDSCAPE, info_land);

        double _screenInches = _ma.screenInches;

        /*if (_screenInches < 4.9f)
        {
            _screenInches = 4.9f;
        }*/

        Bitmap origin_img = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromOtherCard(CARD_BLANK));
        Bitmap bmp;
        float scale_pr = 100f;

        // ORIENTATION_PORTRAIT
        int _wlimit = (int) ((
                (( orietration_screen == ORIENTATION_PORTRAIT ) ? MainActivity.screen_width : MainActivity.screen_height)
                        * 85f) / 100.0f);

        info_port.width_draw_card_in_board = _wlimit;

        bmp = Bitmap.createScaledBitmap(origin_img, (int) (origin_img.getWidth() * scale_pr / 100f), (int) (origin_img.getHeight() * scale_pr / 100f), true);

        if( ( bmp.getWidth()  + info_port.PADDING_X_DRAW_CARD ) * 3f < _wlimit )
        {
            while (true)
            {
                info_port.PADDING_X_DRAW_CARD += 1 * _scale_pixel;
                info_port.PADDING_Y_DRAW_CARD += 0.5 * _scale_pixel;

                if( (bmp.getWidth()  + info_port.PADDING_X_DRAW_CARD ) * 3f >= _wlimit )
                {
                    break;
                }
            }
        }
        else
        {
            while (true)
            {

                if ((bmp.getWidth() + info_port.PADDING_X_DRAW_CARD) * 3f > _wlimit)
                {
                    scale_pr -= 0.4f;
                    bmp = Bitmap.createScaledBitmap(origin_img, (int) (origin_img.getWidth() * scale_pr / 100f), (int) (origin_img.getHeight() * scale_pr / 100f), true);
                }
                else
                {
                    break;
                }
            }
        }

        scale_board_cards_pr = scale_pr;

        //list_cards_draw.clear();

        ArrayList<Integer> res_tmp = Cards.get_all_cards_from_type(Cards.TC_CLUB);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));

            info_port.list_cards_draw.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_DIAMOND);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));
            info_port.list_cards_draw.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_HEART);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));
            info_port.list_cards_draw.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_SPADE);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));
            info_port.list_cards_draw.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromOtherCard(CARD_BLANK));
        info_port.list_cards_draw.put(Cards.BLANK
                , new CardDrawInfo(
                        Cards.BLANK
                        , Cards.getInfoCard(Cards.BLANK)
                        , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));


        bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromSelectCard(type_selectstyle, Cards.SELECT_IN_BOARD));
        info_port.list_cards_draw.put(Cards.SELECT_IN_BOARD
                , new CardDrawInfo(
                        Cards.SELECT_IN_BOARD
                        , Cards.getInfoCard(Cards.SELECT_IN_BOARD)
                        , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromOtherCard(Cards.TRANSFER));
        info_port.list_cards_draw.put(Cards.TRANSFER
                , new CardDrawInfo(
                        Cards.TRANSFER
                        , Cards.getInfoCard(Cards.TRANSFER)
                        , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));


        info_port.card_w = info_port.list_cards_draw.get(Cards.BLANK).img.getWidth();
        info_port.card_h = info_port.list_cards_draw.get(Cards.BLANK).img.getHeight();

        info_port.card_w2 = info_port.card_w / 2;
        info_port.card_h2 = info_port.card_h / 2;

        info_port.card_w4 = info_port.card_w / 4;
        info_port.card_h4 = info_port.card_h / 4;

        //==========================================================================================
        //ORIENTATION_LANDSCAPE
        scale_pr = 100f;

        _wlimit = (int) ((
                (( orietration_screen == ORIENTATION_PORTRAIT ) ? MainActivity.screen_height : MainActivity.screen_width)
                        * 96f) / 100f);

        info_land.width_draw_card_in_board = _wlimit;

        bmp = Bitmap.createScaledBitmap(origin_img, (int) (origin_img.getWidth() * scale_pr / 100f), (int) (origin_img.getHeight() * scale_pr / 100f), true);

        if( ( bmp.getWidth()  + info_land.PADDING_X_DRAW_CARD ) * 6f < _wlimit )
        {
            while (true)
            {
                info_land.PADDING_X_DRAW_CARD += 1 * _scale_pixel;
                info_land.PADDING_Y_DRAW_CARD += 0.5f * _scale_pixel;

                if( (bmp.getWidth()  + info_land.PADDING_X_DRAW_CARD ) * 6f >= _wlimit )
                {
                    break;
                }
            }
        }
        else
        {
            while (true)
            {
                if ((bmp.getWidth() + info_land.PADDING_X_DRAW_CARD) * 6f > _wlimit)
                {
                    scale_pr -= 0.4f;
                    bmp = Bitmap.createScaledBitmap(origin_img, (int) (origin_img.getWidth() * scale_pr / 100f), (int) (origin_img.getHeight() * scale_pr / 100f), true);
                }
                else
                {
                    break;
                }
            }
        }

        scale_board_cards_pr = scale_pr;

        //list_cards_draw.clear();

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_CLUB);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));

            info_land.list_cards_draw.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_DIAMOND);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));
            info_land.list_cards_draw.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_HEART);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));
            info_land.list_cards_draw.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_SPADE);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));
            info_land.list_cards_draw.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromOtherCard(CARD_BLANK));
        info_land.list_cards_draw.put(Cards.BLANK
                , new CardDrawInfo(
                        Cards.BLANK
                        , Cards.getInfoCard(Cards.BLANK)
                        , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));


        bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromSelectCard(type_selectstyle, Cards.SELECT_IN_BOARD));
        info_land.list_cards_draw.put(Cards.SELECT_IN_BOARD
                , new CardDrawInfo(
                        Cards.SELECT_IN_BOARD
                        , Cards.getInfoCard(Cards.SELECT_IN_BOARD)
                        , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromOtherCard(Cards.TRANSFER));
        info_land.list_cards_draw.put(Cards.TRANSFER
                , new CardDrawInfo(
                        Cards.TRANSFER
                        , Cards.getInfoCard(Cards.TRANSFER)
                        , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));


        info_land.card_w = info_land.list_cards_draw.get(Cards.BLANK).img.getWidth();
        info_land.card_h = info_land.list_cards_draw.get(Cards.BLANK).img.getHeight();

        info_land.card_w2 = info_land.card_w / 2;
        info_land.card_h2 = info_land.card_h / 2;

        info_land.card_w4 = info_land.card_w / 4;
        info_land.card_h4 = info_land.card_h / 4;

        //------------------------------------------------------------------------------------------
        _screenInches = _ma.screenInches;

        if (_screenInches < 5.5f)
        {
            _screenInches = 5.5f;
        }

        //scale_pr = ( 100.0f * max_screen_length_of_side / min_screen_length_of_side ) * 0.55f;
        //scale_pr = ( 100.0f * device_screen_size_side_min / device_screen_size_side_max ) * 1.35f;
        scale_pr = (float) (15.2f * _screenInches);

        scale_my_cards_pr = scale_pr;
        //list_cards_user.clear();

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_CLUB);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));

            list_cards_user.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_DIAMOND);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));
            list_cards_user.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_HEART);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));
            list_cards_user.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        res_tmp = Cards.get_all_cards_from_type(Cards.TC_SPADE);

        for (int i = 0; i < res_tmp.size(); i++)
        {
            bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromCard(type_cardstyle, res_tmp.get(i)));
            list_cards_user.put(res_tmp.get(i)
                    , new CardDrawInfo(
                            res_tmp.get(i)
                            , Cards.getInfoCard(res_tmp.get(i))
                            , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        }

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromOtherCard(CARD_BLANK));
        list_cards_user.put(Cards.BLANK
                , new CardDrawInfo(
                        Cards.BLANK
                        , Cards.getInfoCard(Cards.BLANK)
                        , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getDrawableResId_FromSelectCard(type_selectstyle, Cards.SELECT_MY_CARDS));
        list_cards_user.put(Cards.SELECT_MY_CARDS
                , new CardDrawInfo(
                        Cards.SELECT_MY_CARDS
                        , Cards.getInfoCard(Cards.SELECT_MY_CARDS)
                        , Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true)));

        info_port.card2_w = list_cards_user.get(Cards.BLANK).img.getWidth();
        info_port.card2_h = list_cards_user.get(Cards.BLANK).img.getHeight();

        info_port.card2_w2 = info_port.card2_w / 2;
        info_port.card2_h2 = info_port.card2_h / 2;

        info_port.card2_w4 = info_port.card2_w / 4;
        info_port.card2_h4 = info_port.card2_h / 4;

        info_land.card2_w = list_cards_user.get(Cards.BLANK).img.getWidth();
        info_land.card2_h = list_cards_user.get(Cards.BLANK).img.getHeight();

        info_land.card2_w2 = info_land.card2_w / 2;
        info_land.card2_h2 = info_land.card2_h / 2;

        info_land.card2_w4 = info_land.card2_w / 4;
        info_land.card2_h4 = info_land.card2_h / 4;
        //------------------------------------------------------------------------------------------

        /*PADDING_X_DRAW_CARD = (int) (card_w / 2.7f);
        PADDING_Y_DRAW_CARD = (int) (card_h / 2.5f);*/

        //scale_pr = 60.0f;
        bmp = BitmapFactory.decodeResource(getResources(), Cards.getIconResCard(Cards.TC_SPADE));
        list_cards_icons.put(Cards.TC_SPADE, Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true));

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getIconResCard(Cards.TC_DIAMOND));
        list_cards_icons.put(Cards.TC_DIAMOND, Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true));

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getIconResCard(Cards.TC_HEART));
        list_cards_icons.put(Cards.TC_HEART, Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true));

        bmp = BitmapFactory.decodeResource(getResources(), Cards.getIconResCard(Cards.TC_CLUB));
        list_cards_icons.put(Cards.TC_CLUB, Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true));
    }

    public Bitmap getMoneyIcon()
    {
        return money_icon;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        this._stop_thread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        this._run_thread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        synchronized (lockSurfaceChanged)
        {
            //Log.i("TAG78", "surfaceChanged: " + String.valueOf(canvas_w) + " " + String.valueOf(canvas_h) + " " + String.valueOf(width) + " " + String.valueOf(height));
            //Log.i("TAG78", "surfaceChanged: w = " + String.valueOf(width) + " h = " + String.valueOf(height));

            if (canvas_w != width || canvas_h != height)
            {
                _stop_draw = true;
                th_in.pause = false;

                canvas_w = width;
                canvas_h = height;
                canvas_w2 = canvas_w / 2;
                canvas_h2 = canvas_h / 2;

                if (canvas_w > canvas_h)
                {
                    orietration_screen = ORIENTATION_LANDSCAPE;
                }
                else
                {
                    orietration_screen = ORIENTATION_PORTRAIT;
                }

                if (this.initAllCards)
                {
                    initAllCards();
                    this.initAllCards = false;
                }

                synchronized (room_info.lock_draw)
                {
                    room_info.my_userinfo.cached_draw_my_cards = true;
                    room_info.my_userinfo.resetMatrixDrawCardsUser();
                }

                synchronized (card_fly_user_xod_lock)
                {
                    synchronized (room_info.lock_cards_boards)
                    {
                        for (int g = 0; g < room_info.cards_boards.size(); g++)
                        {
                            ItemBoardCard item = room_info.cards_boards.get(g);

                            if (item.c1 != null)
                            {
                                item.c1 = infoOrientation.get(orietration_screen).list_cards_draw.get(item.c1.card_info.card);

                            }

                            if (item.c2 != null)
                            {
                                item.c2 = infoOrientation.get(orietration_screen).list_cards_draw.get(item.c2.card_info.card);
                            }
                        }

                        room_info.cache_draw_cards_board = false;
                    }
                }

                synchronized (room_info.lock_list_matrix_users_cards)
                {
                    room_info.resetAllUserListMatrixCards();
                }

                initFidrstDraw();
                needRebuildBackground = true;

                th_in.pause = true;
                _stop_draw = false;

                //System.gc();
            }
        }
    }

    public int getOrietrationScreen()
    {
        return orietration_screen;
    }

    public void _stop_thread()
    {
        boolean retry = true;
        this._stop_draw = true;
        this.drawLoopThread.setRunning(false);
        while (retry)
        {
            try
            {
                this.drawLoopThread.join();
                retry = false;
            } catch (InterruptedException e)
            {
            }
        }
    }

    private void _run_thread()
    {
        this._stop_draw = false;
        this.drawLoopThread.setRunning(true);

        if (this.drawLoopThread.getState() == Thread.State.TERMINATED)
        {
            this.drawLoopThread = new DrawManagerRoom(this);
            this.drawLoopThread.setPriority(Thread.MAX_PRIORITY);
            this.drawLoopThread.setRunning(true);
            this.drawLoopThread.start();
        }
        else if (this.drawLoopThread.getState() != Thread.State.RUNNABLE)
        {
            this.drawLoopThread.start();
        }
    }

    //=========================================================================================================
    public void StartGame()
    {
        draw_deck = true;
        draw_trump_card_in_the_deck = getRoomInfo().count_deck_of_cards > 0;
        needRebuildBackground = true;

        draw_my_cards = false;

        runFlyCardsOfDescktoUsers();

        synchronized (room_info.lock_cards_boards)
        {
            room_info.cards_boards = new ArrayList<>();
        }

        Thread wait_draw_my_cards = new Thread()
        {
            @Override
            public void run()
            {
                while (animation_fly_desc_card)
                {
                    try
                    {
                        Thread.sleep(200);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                draw_my_cards = true;

                for (int i = 0; i < room_info.users_in_room.size(); i++)
                {
                    room_info.users_in_room.get(i).drawUserCards = true;
                }

                needRebuildBackground = true;
            }
        };
        wait_draw_my_cards.start();
    }

    public void OnDestroy()
    {
        this.drawLoopThread.setRunning(false);
        th_in.run = false;
    }

    public void resetAnimationOffsetMyCard()
    {
        for (Map.Entry<Integer, CardDrawInfo> entry : infoOrientation.get(orietration_screen).list_cards_draw.entrySet())
        {
            Integer key = entry.getKey();
            CardDrawInfo value = entry.getValue();

            value.animation_offset_x = 0;
            value.animation_offset_y = 0;
            value.animation_rotate_x = 0;
            value.animation_rotate_y = 0;
        }
    }

    public void resetAllUsersTimers()
    {
        for (int i = 0; i < room_info.users_in_room.size(); i++)
        {
            room_info.users_in_room.get(i).stopTimer();
        }
    }

    public void flyJoker(int start_x, int start_y, int end_x, int end_y)
    {
        synchronized (lock_animation_fly_img)
        {
            AnimationImgToUsers tm = new AnimationImgToUsers(
                    joker
                    , start_x
                    , start_y
                    , (int) end_x
                    , (int) end_y
                    , 100
                    , 30
                    , 3000
            );

            list_animation_fly_img.add(tm);

            flag_animation_fly_img = true;
        }
    }

    public void flyMoneyUsers(List<FlyMoneyInfo> list_uid)
    {
        synchronized (lock_animation_fly_img)
        {
            for (int i = 0; i < list_uid.size(); i++)
            {
                FlyMoneyInfo fm = list_uid.get(i);

                RoomInfo_User user = getInfoUser(fm.uid);

                float pos_x = user.pos_x;
                float pos_y = user.pos_y;

                if (user.user_photo != null)
                {
                    pos_y += user.user_photo.getHeight() + 10 * _scale_pixel;
                }

                if (room_info.my_userinfo.uid == fm.uid)
                {
                    pos_y -= 50 * _scale_pixel;
                }

                for (int j = 0; j < 5; j++)
                {
                    AnimationImgToUsers tm = new AnimationImgToUsers(
                            getMoneyIcon()
                            , 0
                            , canvas_h2
                            , (int) pos_x
                            , (int) pos_y
                            , j * 120 + i * 100
                            , 20
                            , 0
                    );

                    list_animation_fly_img.add(tm);
                }

                if (room_info.my_userinfo.uid == fm.uid)
                {
                    addCointsAddAnimation(fm.add_coints, canvas_w2, canvas_h2 + canvas_h2 / 4, "#00d500");
                }
                else
                {
                    addCointsAddAnimation(fm.add_coints, pos_x, pos_y, "#00d500");
                }
            }

            flag_animation_fly_img = true;
        }
    }

    public void resetAnimationOffsetCardBoards()
    {
        for (int i = 0; i < room_info.cards_boards.size(); i++)
        {
            if (room_info.cards_boards.get(i).c1 != null)
            {
                room_info.cards_boards.get(i).c1.animation_offset_x = 0;
                room_info.cards_boards.get(i).c1.animation_offset_y = 0;
                room_info.cards_boards.get(i).c1.animation_rotate_x = 0;
                room_info.cards_boards.get(i).c1.animation_rotate_y = 0;
            }

            if (room_info.cards_boards.get(i).c2 != null)
            {
                room_info.cards_boards.get(i).c2.animation_offset_x = 0;
                room_info.cards_boards.get(i).c2.animation_offset_y = 0;
                room_info.cards_boards.get(i).c2.animation_rotate_x = 0;
                room_info.cards_boards.get(i).c2.animation_rotate_y = 0;
            }
        }
    }

    public RoomInfo getRoomInfo()
    {
        return room_info;
    }

    public Bitmap getDefaultAvatar()
    {
        return default_avatar;
    }

    public RoomInfo_User getUserFromOffsetPosition(int num)
    {
        for (int i = 0; i < room_info.users_in_room.size(); i++)
        {
            if (room_info.users_in_room.get(i).offset_position_num == num)
            {
                return room_info.users_in_room.get(i);
            }
        }

        if (room_info.current_my_position == num)
        {
            return room_info.my_userinfo;
        }

        return null;
    }

    public RoomInfo_User getInfoUser(int uid)
    {
        for (int i = 0; i < room_info.users_in_room.size(); i++)
        {
            if (room_info.users_in_room.get(i).uid == uid)
            {
                return room_info.users_in_room.get(i);
            }
        }

        if (room_info.my_userinfo.uid == uid)
        {
            return room_info.my_userinfo;
        }

        return null;
    }

    public RoomInfo_User getInfoUserNoMe(int uid)
    {
        for (int i = 0; i < room_info.users_in_room.size(); i++)
        {
            if (room_info.users_in_room.get(i).uid == uid)
            {
                return room_info.users_in_room.get(i);
            }
        }

        return null;
    }

    public boolean waitFlyCardToBoard()
    {
        synchronized (card_fly_user_xod_lock)
        {
            return card_fly_user_xod.size() > 0;
        }
    }

    public void resetHoveredCardSelected()
    {
        synchronized (lock_hovered)
        {
            hovered = null;
        }
    }

    public void highLightTossedMyCards()
    {
        for (int i = 0, len = room_info.my_userinfo.cards_user.size(); i < len; i++)
        {
            CardInfo item = room_info.my_userinfo.cards_user.get(i);

            if (room_info.validateTossXodOnBoard(item.card))
            {
                item.draw_icon_tossed_card = true;
                item.timestamp_add2 = System.currentTimeMillis();
            }
        }
    }

    public void highLightBeadMyCards()
    {
        synchronized (room_info.lock_cards_boards)
        {
            boolean all_catds_not_beated = room_info.allCardsIsNotBeated();

            for (int j = 0; j < room_info.cards_boards.size(); j++)
            {
                ItemBoardCard item = room_info.cards_boards.get(j);

                for (int i = 0; i < room_info.my_userinfo.cards_user.size(); i++)
                {
                    CardInfo card_info = room_info.my_userinfo.cards_user.get(i);

                    if (
                            item.c2 == null
                                    && Cards.testBeat(room_info.trump_card_in_the_deck
                                    , card_info.card
                                    , item.c1.card_info.card))
                    {
                        card_info.draw_icon_tossed_card = true;
                        card_info.timestamp_add2 = System.currentTimeMillis();
                    }
                    else if (
                            all_catds_not_beated
                                    && room_info.type_game == RoomInfo.TYPE_GAME__TRANSFER_FOOL
                                    && Cards.getInfoCard(card_info.card).sort_num == item.c1.card_info.sort_num
                                    && room_info.count_bito_bery > 0
                            )
                    {
                        card_info.draw_icon_tossed_card = true;
                        card_info.timestamp_add2 = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    public void highLightResetAll()
    {
        synchronized (room_info.lock_cards_boards)
        {
            //for (int j = 0; j < room_info.cards_boards.size(); j++)
            {
                for (int i = 0; i < room_info.my_userinfo.cards_user.size(); i++)
                {
                    room_info.my_userinfo.cards_user.get(i).draw_icon_tossed_card = false;
                }
            }
        }
    }

    private void hideTransferCard()
    {
        synchronized (room_info.lock_cards_boards)
        {
            if (room_info.cards_boards.size() > 0
                    && room_info.cards_boards.get(room_info.cards_boards.size() - 1).c1.card_info.card == Cards.getInfoCard(Cards.TRANSFER).card)
            {
                room_info.cards_boards.remove(room_info.cards_boards.size() - 1);
                room_info.cache_draw_cards_board = false;
            }
        }
    }

    private void showTransferCard()
    {
        if (room_info.cards_boards.size() > 0
                && room_info.type_game == RoomInfo.TYPE_GAME__TRANSFER_FOOL
                && room_info.current_pos_user_beat == room_info.current_my_position
                && room_info.count_bito_bery > 0
                )
        {
            if (
                    current_my_animated_card != null
                            && room_info.type_game == RoomInfo.TYPE_GAME__TRANSFER_FOOL // подкидной дурак
                            && room_info.validateTossXodOnBoard(current_my_animated_card.card)
                            && room_info.allCardsIsNotBeated()
                            && !room_info.inBoardHaveTrumpCard()
                            && existNextUserTransferXod()
                    )
            {
                if (room_info.cards_boards.get(room_info.cards_boards.size() - 1).c1.card_info.card != Cards.getInfoCard(Cards.TRANSFER).card)
                {
                    ItemBoardCard item_add = new ItemBoardCard();
                    item_add.c1 = infoOrientation.get(orietration_screen).list_cards_draw.get(Cards.TRANSFER);
                    item_add.c2 = null;

                    item_add.need_draw1 = true;

                    synchronized (room_info.lock_cards_boards)
                    {
                        room_info.cards_boards.add(item_add);
                        room_info.cache_draw_cards_board = false;
                    }
                }
            }
            else
            {
                synchronized (room_info.lock_cards_boards)
                {
                    if (room_info.cards_boards.get(room_info.cards_boards.size() - 1).c1.card_info.card == Cards.getInfoCard(Cards.TRANSFER).card)
                    {
                        room_info.cards_boards.remove(room_info.cards_boards.size() - 1);
                        room_info.cache_draw_cards_board = false;
                    }
                }
            }
        }
    }

    //==============================================================================================
    private void onTouchEvent_ACTION_DOWN(final float x, final float y)
    {
        //Log.i("TAG", "MotionEvent.ACTION_DOWN");

        lastX = x;
        lastY = y;

        pressed = true;
        canMove = true;

        if (available_select_my_card)
        {
            synchronized (room_info.lock_draw)
            {
                for (int i = room_info.my_userinfo.cards_points.size() - 1; i >= 0; --i)
                {
                    if (
                            point_in_triangle(
                                    room_info.my_userinfo.cards_points.get(i).x1
                                    , room_info.my_userinfo.cards_points.get(i).y1
                                    , room_info.my_userinfo.cards_points.get(i).x2
                                    , room_info.my_userinfo.cards_points.get(i).y2
                                    , room_info.my_userinfo.cards_points.get(i).x3
                                    , room_info.my_userinfo.cards_points.get(i).y3
                                    , x
                                    , y
                            )
                                    || point_in_triangle(
                                    room_info.my_userinfo.cards_points.get(i).x3
                                    , room_info.my_userinfo.cards_points.get(i).y3
                                    , room_info.my_userinfo.cards_points.get(i).x4
                                    , room_info.my_userinfo.cards_points.get(i).y4
                                    , room_info.my_userinfo.cards_points.get(i).x1
                                    , room_info.my_userinfo.cards_points.get(i).y1
                                    , x
                                    , y
                            )
                            )
                    {

                        if (current_my_animated_card == null || current_my_animated_card.card != room_info.my_userinfo.cards_user.get(i).card)
                        {
                            for (int k = 0; k < my_animated_card.size(); k++)
                            {
                                if (my_animated_card.get(k) != null)
                                {
                                    my_animated_card.get(k).setUp(false);
                                }
                            }

                            AnimatedCard a = new AnimatedCard(
                                    getResources()
                                    , room_info.my_userinfo.cards_user.get(i).card
                                    , room_info.my_userinfo.cards_points.get(i).center_x
                                    , room_info.my_userinfo.cards_points.get(i).center_y
                                    , room_info.my_userinfo.cards_points.get(i).end_center_x
                                    , room_info.my_userinfo.cards_points.get(i).end_center_y
                                    , room_info.my_userinfo.cards_points.get(i).rotate_angle
                                    , room_info.my_userinfo.cards_points.get(i).rotate_angle_x
                                    , room_info.my_userinfo.cards_points.get(i).rotate_angle_y
                                    , true
                                    , Cards.getDrawableResId_FromCard(type_cardstyle, room_info.my_userinfo.cards_user.get(i).card)
                                    , scale_my_cards_pr
                                    , scale_my_cards_pr + 15.0f
                                    , 3
                                    , scale_my_cards_pr
                                    , 3
                            );

                            current_my_animated_card = a;
                            my_animated_card.add(a);

                            showTransferCard();
                        }
                        else
                        {
                            current_my_animated_card.need_delete = false;
                            current_my_animated_card.setUp(true);
                        }

                        animation_my_animated_card = true;
                        //Log.i("TAG", "IN card: " + room_info.my_userinfo.cards_user.get(i).litera + " " + room_info.my_userinfo.cards_user.get(i).litera2);
                        break;
                    }
                }

            } // --synchronized
        }


        for (int i = 0, s = room_info.users_in_room.size(); i < s; i++)
        {
            RoomInfo_User item = room_info.users_in_room.get(i);

            item.selected = false;

            if (
                    item.init_user
                            && item.user_photo != null
                            && distance_point((double) x, (double) y, (double) item.pos_x, (double) item.pos_y) < item.user_photo.getWidth()
                    )
            {
                item.selected = true;

                buildWindowUserInfo(item.uid);

                pos_x_wondow_user_info = (canvas_w - window_user_info.getWidth()) / 2;
                pos_y_wondow_user_info = (int) ((canvas_h - window_user_info.getHeight()) / 1.7f);

                GButton close_btn = ui_buttons.get("btn_close_window_user_info");
                close_btn.setPosition(
                        pos_x_wondow_user_info + window_user_info.getWidth() - close_btn.getW()
                        , pos_y_wondow_user_info
                );

                if(ui_buttons.containsKey("btn_test"))
                {
                    GButton btn_test = ui_buttons.get("btn_test");
                    btn_test.setPosition(
                            pos_x_wondow_user_info + window_user_info.getWidth() / 2 - btn_test.getW() / 2
                            , pos_y_wondow_user_info + window_user_info.getHeight() - btn_test.getH() - 3 * _scale_pixel
                    );
                }

                show_wondow_user_info = true;

                //callbackClickUserInRoom(item.uid);
            }
        }
    }

    //==============================================================================================
    private void onTouchEvent_ACTION_UP(final float ex, final float ey)
    {
        //Log.i("TAG", "MotionEvent.ACTION_UP");

        if (!pressed)
        {
            return;
        }

        pressed = false;
        canMove = false;
        //zoomTrans = false;

        boolean restore__available_select_my_card = available_select_my_card;

        synchronized (room_info.lock_draw)
        {
            if (current_my_animated_card != null)
            {
                CardInfo ci = Cards.getInfoCard(current_my_animated_card.card);

                //Log.i("TAG", "ACTION_UP:  " + String.valueOf(ey) + " " + String.valueOf(ex) + " " + ci.litera + " " + ci.litera2 + "  mode_toss2: " + String.valueOf(mode_toss2));

                lastUpX = ex;
                lastUpY = ey;

                synchronized (room_info.lock_cards_boards)
                {
                    if ((
                            room_info.current_pos_user_beat == room_info.current_my_position
                                    || (room_info.count_bito_bery == 0 && room_info.cards_boards.size() + 1 < 6)
                                    || (room_info.count_bito_bery > 0 && room_info.cards_boards.size() + 1 < 7)
                    )
                            && (
                            (room_info.cards_boards.size() == 0 && available_select_my_card)
                                    || (
                                    room_info.cards_boards.size() > 0
                                            && available_select_my_card
                                            && (
                                            (
                                                    room_info.current_pos_user_xod == room_info.current_my_position
                                                            && current_my_animated_card != null
                                                          /*&& hovered != null
                                                          && Cards.testBeat(
                                                                 room_info.trump_card_in_the_deck
                                                               , current_my_animated_card.card
                                                                //, room_info.cards_boards.get( room_info.cards_boards.size() - 1).c1.card_info.card
                                                               , hovered.card_info.card
                                                             )*/

                                                            && room_info.validateTossXodOnBoard(current_my_animated_card.card)
                                            )
                                                    || (
                                                    room_info.current_pos_user_beat == room_info.current_my_position
                                                            && current_my_animated_card != null
                                                            &&
                                                            (
                                                                    (
                                                                            hovered != null
                                                                                    && Cards.testBeat(
                                                                                    room_info.trump_card_in_the_deck
                                                                                    , current_my_animated_card.card
                                                                                    //, room_info.cards_boards.get( room_info.cards_boards.size() - 1).c1.card_info.card
                                                                                    , hovered.card_info.card
                                                                            )
                                                                    )
                                                                            || (
                                                                            hovered == null
                                                                                    && room_info.cards_boards.size() == 1
                                                                                    && Cards.testBeat(
                                                                                    room_info.trump_card_in_the_deck
                                                                                    , current_my_animated_card.card
                                                                                    //, room_info.cards_boards.get( room_info.cards_boards.size() - 1).c1.card_info.card
                                                                                    , room_info.cards_boards.get(0).c1.card_info.card
                                                                            )
                                                                    )
                                                                            || (
                                                                            hovered == null
                                                                                    && room_info.getOneCardNotBeated() != -1
                                                                                    && Cards.testBeat(
                                                                                    room_info.trump_card_in_the_deck
                                                                                    , current_my_animated_card.card
                                                                                    , room_info.cards_boards.get(room_info.getOneCardNotBeated()).c1.card_info.card
                                                                            )
                                                                    )
                                                            )
                                            )
                                                    || (
                                                    room_info.current_pos_user_beat == room_info.current_my_position
                                                            && current_my_animated_card != null
                                                            && room_info.type_game == RoomInfo.TYPE_GAME__TRANSFER_FOOL // подкидной дурак
                                                            && room_info.validateTossXodOnBoard(current_my_animated_card.card)
                                                            && !room_info.inBoardHaveTrumpCard()
                                            )
                                    )
                            )
                    )
                            &&
                            (
                                   /*(
                                           lines.size() <= 1
                                        && lastUpX >= canvas_w2 - canvas_w2 / 1.2f
                                        && lastUpY >= canvas_h2 - canvas_h2 / 4
                                        && lastUpX <= canvas_w2 + canvas_w2 / 1.2f
                                        && lastUpY <= canvas_h2 + canvas_h2 / 4
                                   )
                                ||*/ (
                                    drawRectCardBoard != null
                                            && drawRectCardBoard.contains(lastUpX, lastUpY)
                            )
                            )
                        //&& ( mode_toss2 != 0 || hovered != null )
                            )
                    {
                        available_select_my_card = false;

                        for (int i = 0, l0 = room_info.my_userinfo.cards_user.size(); i < l0; i++)
                        {
                            CardInfo item = room_info.my_userinfo.cards_user.get(i);

                            if (current_my_animated_card.card == item.card)
                            {
                                //Log.i("TAG", "fount card");

                                synchronized (lock_hovered)
                                {
                                    if (hovered == null && room_info.cards_boards.size() == 1)
                                    {
                                        hovered = room_info.cards_boards.get(0).c1;
                                    }
                                    else if (hovered == null && room_info.getOneCardNotBeated() != -1)
                                    {
                                        hovered = room_info.cards_boards.get(room_info.getOneCardNotBeated()).c1;
                                    }
                                }

                                if (
                                        mode_toss2 == 0
                                                && hovered != null
                                        )
                                {
                                    // ПЕРЕВОД ХОДА ?
                                    if (
                                            room_info.current_pos_user_beat == room_info.current_my_position
                                                    && current_my_animated_card != null
                                                    && room_info.type_game == RoomInfo.TYPE_GAME__TRANSFER_FOOL // подкидной дурак
                                                    && room_info.validateTossXodOnBoard(current_my_animated_card.card)
                                                    && room_info.allCardsIsNotBeated()
                                                    && hovered.card_info.card == Cards.TRANSFER
                                            )
                                    {
                                        hideTransferCard();

                                        // удалить карту из списка моих карт
                                        room_info.my_userinfo.resetMatrixDrawCardsUser();
                                        room_info.my_userinfo.cards_user.remove(i);
                                        room_info.my_userinfo.cards_points.remove(i);

                                        ItemBoardCard item_add = new ItemBoardCard();
                                        item_add.c1 = infoOrientation.get(orietration_screen).list_cards_draw.get(item.card);
                                        item_add.c2 = null;

                                        current_my_animated_card.setDestinationXYDownAnimation(
                                                lastDrawCardInBoard.getx() + infoOrientation.get(orietration_screen).card_w2
                                                , lastDrawCardInBoard.gety() + infoOrientation.get(orietration_screen).card_h
                                                , scale_board_cards_pr
                                        );

                                        room_info.cards_boards.add(item_add);
                                        room_info.cache_draw_cards_board = false;

                                        toss_xod_transfer_foll(item.card, i);

                                        for (int k = 0; k < my_animated_card.size(); k++)
                                        {
                                            if (my_animated_card.get(k) != null)
                                            {
                                                my_animated_card.get(k).setUp(false);
                                            }
                                        }

                                        return;
                                    }

                                    // определить карту на которую идет ход
                                    if (!Cards.testBeat(
                                            room_info.trump_card_in_the_deck
                                            , current_my_animated_card.card
                                            //, room_info.cards_boards.get( room_info.cards_boards.size() - 1).c1.card_info.card
                                            , hovered.card_info.card
                                    ))
                                    {

                                        //Log.i("TAG", "return");

                                        for (int k = 0; k < my_animated_card.size(); k++)
                                        {
                                            if (my_animated_card.get(k) != null)
                                            {
                                                my_animated_card.get(k).setUp(false);
                                            }
                                        }

                                        available_select_my_card = restore__available_select_my_card;

                                        return;
                                    }
                                }


                                boolean _flag = false;

                                if (mode_toss2 == 0)
                                {
                                    for (int h = 0, _len = room_info.cards_boards.size(); h < _len; h++)
                                    {
                                        if (hovered != null)
                                        {
                                            CardInfo ci2 = Cards.getInfoCard(room_info.cards_boards.get(h).c1.card_info.card);
                                            //Log.i("TAG", ci2.litera + " " + ci2.litera2);

                                            CardInfo ci3 = Cards.getInfoCard(hovered.card_info.card);
                                            //Log.i("TAG", ci3.litera + " " + ci3.litera2);
                                        }


                                        if (
                                                hovered != null && room_info.cards_boards.get(h).c1.card_info.card == hovered.card_info.card
                                                        || hovered == null && room_info.cards_boards.get(h).c2 == null
                                                )
                                        {
                                            _flag = true;
                                            //Log.i("TAG", "_flag = true");

                                            if (current_my_animated_card != null)
                                            {
                                                /*current_my_animated_card.setDestinationXYDownAnimation(
                                                        lastDrawCardInBoard.getx() + card_w2 + card_w2 / 4
                                                        , lastDrawCardInBoard.gety() + card_h + card_h2 / 2
                                                );*/

                                                current_my_animated_card.setDestinationXYDownAnimation(
                                                        room_info.cards_boards.get(h).c1.draw_pos_x + infoOrientation.get(orietration_screen).card_w2 + infoOrientation.get(orietration_screen).card_w2 / 4
                                                        , room_info.cards_boards.get(h).c1.draw_pos_y + infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).card_h2 / 2
                                                        , scale_board_cards_pr
                                                );


                                                beat_xod(item.card, i, hovered != null ? hovered.card_info.card : 0);

                                                // удалить карту из списка моих карт
                                                room_info.my_userinfo.resetMatrixDrawCardsUser();
                                                room_info.my_userinfo.cards_user.remove(i);
                                                room_info.my_userinfo.cards_points.remove(i);

                                                current_my_animated_card.play_sound_thrown = true;
                                            }

                                            room_info.cards_boards.get(h).c2 = infoOrientation.get(orietration_screen).list_cards_draw.get(item.card);
                                            break;
                                        }
                                    }
                                }

                                // новая карта
                                if (!_flag)
                                {
                                    if (
                                            current_my_animated_card != null
                                                    && (getUserFromOffsetPosition(room_info.current_pos_user_beat).tmp_count_cards - (room_info.countCardsNotBeatedBoard() + 1) >= 0)
                                            )
                                    {
                                        //Log.i("TAG", "Add card");
                                        ItemBoardCard item_add = new ItemBoardCard();
                                        item_add.c1 = infoOrientation.get(orietration_screen).list_cards_draw.get(item.card);
                                        item_add.c2 = null;

                                        PointPP t = getPosNextDrawCardsInBoard(true);

                                        if (
                                                room_info.cards_boards.size() == 0
                                                        || room_info.cards_boards.get(room_info.cards_boards.size() - 1).c2 == null
                                                )
                                        {
                                                /*if(    room_info.cards_boards.size() > 0
                                                    && room_info.cards_boards.get( room_info.cards_boards.size() - 1 ).c2 == null )
                                                {
                                                    current_my_animated_card.setDestinationXYDownAnimation(
                                                            lastDrawCardInBoard.getx() //+ card_w2 / 5
                                                            , lastDrawCardInBoard.gety() //+ card_h2 / 3
                                                    );
                                                }
                                                else
                                                {*/
                                            current_my_animated_card.setDestinationXYDownAnimation(
                                                    t.getx() + infoOrientation.get(orietration_screen).card_w2
                                                    , t.gety() + infoOrientation.get(orietration_screen).card_h
                                                    , scale_board_cards_pr
                                            );

                                            toss_xod(item.card, i);

                                            // удалить карту из списка моих карт
                                            room_info.my_userinfo.resetMatrixDrawCardsUser();
                                            room_info.my_userinfo.cards_user.remove(i);
                                            room_info.my_userinfo.cards_points.remove(i);

                                            current_my_animated_card.play_sound_thrown = true;
                                        }
                                        else
                                        {
                                            current_my_animated_card.setDestinationXYDownAnimation(
                                                    t.getx() + infoOrientation.get(orietration_screen).card_w2
                                                    , t.gety() + infoOrientation.get(orietration_screen).card_h
                                                    , scale_board_cards_pr
                                            );

                                            toss_xod(item.card, i);

                                            // удалить карту из списка моих карт
                                            room_info.my_userinfo.resetMatrixDrawCardsUser();
                                            room_info.my_userinfo.cards_user.remove(i);
                                            room_info.my_userinfo.cards_points.remove(i);

                                            current_my_animated_card.play_sound_thrown = true;
                                        }

                                        room_info.cards_boards.add(item_add);
                                        room_info.cache_draw_cards_board = false;
                                    }
                                    else
                                    {
                                        //Log.i("TAG", "UP = false");

                                        for (int k = 0; k < my_animated_card.size(); k++)
                                        {
                                            if (my_animated_card.get(k) != null)
                                            {
                                                my_animated_card.get(k).setUp(false);
                                            }
                                        }

                                        return;
                                    }

                                    //need_move_cards_on_board = true;
                                }

                                break;
                            }

                        } //break

                        //available_select_my_card = true;
                    }
                    else
                    {
                        hideTransferCard();
                    }

                }
            }
            else
            {
                //Log.i("TAG", "onTouchEvent: current_my_animated_card == null");
            }

            /*if (current_my_animated_card != null)
            {
                //current_my_animated_card.setDestinationXYDownAnimation( get_end_pos_doard_draw_cards() + card_w2, canvas_h2 + card_h2 );
                current_my_animated_card = null;
            }*/

            //Log.i("TAG", "UP = false 2");

            stopSelectAnimationCards();
        }
    }

    public void stopSelectAnimationCards()
    {
        for (int k = 0; k < my_animated_card.size(); k++)
        {
            if (my_animated_card.get(k) != null)
            {
                my_animated_card.get(k).setUp(false);
            }
        }
    }

    public void resetSelectUsers()
    {
        for (int i = 0, s = room_info.users_in_room.size(); i < s; i++)
        {
            room_info.users_in_room.get(i).selected = false;
        }
    }

    //==============================================================================================
    public boolean onTouchEvent(final MotionEvent event)
    {
        if (
                    animation_fly_card_off_board
                ||  animation_fly_desc_card
                ||  flag_animation_fly_img
                ||  flag_card_fly_user_xod
                ||  ! stat_game
                //||  show_wondow_user_info
            )
        {
            return true;
        }

        final float ex = event.getX();
        final float ey = event.getY();
        int action = event.getAction();

        if( ! ui_buttons.isEmpty() )
        {
            if( action == MotionEvent.ACTION_DOWN )
            {
                for (Map.Entry<String, GButton> entry : ui_buttons.entrySet())
                {
                    GButton btn = entry.getValue();

                    //entry.getKey();
                    if (btn.btn_rect.contains(ex, ey))
                    {
                        btn.setStatus(GUiStatus.STATUS_PRESSED);
                    }
                    else
                    {
                        btn.setStatus(GUiStatus.STATUS_DEFAULT);
                    }
                }
            }
            else if( action == MotionEvent.ACTION_UP )
            {
                for (Map.Entry<String, GButton> entry : ui_buttons.entrySet())
                {
                    GButton btn = entry.getValue();

                    //entry.getKey();
                    if (btn.btn_rect.contains(ex, ey) && btn.getStatus() == GUiStatus.STATUS_PRESSED)
                    {
                        btn.execPress();
                        break;
                    }
                    else
                    {
                        btn.setStatus(GUiStatus.STATUS_DEFAULT);
                    }
                }
            }
        }

        if( show_wondow_user_info )
        {
            return true;
        }

        switch (action)
        {
            case MotionEvent.ACTION_DOWN: // нажатие на экран

                onTouchEvent_ACTION_DOWN(event.getX(), event.getY());

                return true;

            case MotionEvent.ACTION_MOVE: //движение по экрану


                //Log.i("TAG", "MotionEvent.ACTION_MOVE");

                if (canMove)
                {
                    lastMoveX = ex;
                    lastMoveY = ey;

                    synchronized (room_info.lock_draw)
                    {
                        if (current_my_animated_card != null)
                        {
                            if (ey < canvas_h - infoOrientation.get(orietration_screen).card2_h4 - infoOrientation.get(orietration_screen).card2_h4 / 2)
                            {
                                current_my_animated_card.setOffsetMove((double) (lastMoveX - lastX), (double) (lastMoveY - lastY));
                            }
                        }
                    }

                    if (available_select_my_card)
                    {
                        boolean found = false;

                        // карты выделение ведением пальца нижняя полоска
                        if (ey > canvas_h - infoOrientation.get(orietration_screen).card2_h4 - infoOrientation.get(orietration_screen).card2_h4 / 2)
                        {
                            synchronized (room_info.lock_draw)
                            {
                                for (int i = room_info.my_userinfo.cards_points.size() - 1; i >= 0; --i)
                                {
                                    CardInfo card_info      = room_info.my_userinfo.cards_user.get(i);
                                    CardDraw_RectPoint item = room_info.my_userinfo.cards_points.get(i);

                                    if (
                                            point_in_triangle(
                                                    item.x1
                                                    , item.y1
                                                    , item.x2
                                                    , item.y2
                                                    , item.x3
                                                    , item.y3
                                                    , ex
                                                    , ey
                                            )
                                            || point_in_triangle(
                                                    item.x3
                                                    , item.y3
                                                    , item.x4
                                                    , item.y4
                                                    , item.x1
                                                    , item.y1
                                                    , ex
                                                    , ey
                                            )
                                            )
                                    {
                                        if (current_my_animated_card == null || current_my_animated_card.card != card_info.card)
                                        {
                                            if (current_my_animated_card == null)
                                            {
                                                //Log.i("TAG", "current_my_animated_card == null 0");
                                            }

                                            boolean ff = false;

                                            for (int k = 0; k < my_animated_card.size(); k++)
                                            {
                                                if (my_animated_card.get(k) != null)
                                                {
                                                    if (current_my_animated_card != null
                                                            && my_animated_card.get(k).card == card_info.card
                                                            && current_my_animated_card.card == card_info.card)
                                                    {
                                                        ff = true;
                                                        current_my_animated_card = my_animated_card.get(k);
                                                        current_my_animated_card.need_delete = false;
                                                        current_my_animated_card.setUp(true);

                                                        break;
                                                    }
                                                    else
                                                    {
                                                        //my_animated_card.get(k).up = false;
                                                    }
                                                }
                                            }

                                            if (!ff)
                                            {
                                                AnimatedCard a = new AnimatedCard(
                                                        getResources()
                                                        , card_info.card
                                                        , item.center_x
                                                        , item.center_y
                                                        , item.end_center_x
                                                        , item.end_center_y
                                                        , item.rotate_angle
                                                        , item.rotate_angle_x
                                                        , item.rotate_angle_y
                                                        , true
                                                        , Cards.getDrawableResId_FromCard(type_cardstyle, card_info.card)
                                                        , scale_my_cards_pr
                                                        , scale_my_cards_pr + 15.0f
                                                        , 3
                                                        , scale_my_cards_pr
                                                        , 3
                                                );

                                                if (current_my_animated_card != null)
                                                {
                                                    current_my_animated_card.need_delete = true;
                                                    current_my_animated_card.setUp(false);
                                                }

                                                current_my_animated_card = a;
                                                my_animated_card.add(a);

                                                showTransferCard();
                                            }
                                        }
                                        else
                                        {
                                            current_my_animated_card.need_delete = false;
                                            current_my_animated_card.setUp(true);
                                        }

                                        lastX = lastMoveX;
                                        lastY = lastMoveY;

                                        animation_my_animated_card = true;
                                        //Log.i("TAG", "IN card: " + room_info.my_userinfo.cards_user.get(i).litera + " " + room_info.my_userinfo.cards_user.get(i).litera2);
                                        break;
                                    }
                                }
                            }
                        }
                        else if (current_my_animated_card != null)
                        {
                            synchronized (room_info.lock_cards_boards)
                            {
                                // выделим первую карту на которую можно сделать ход
                                for (int g = 0; g < room_info.cards_boards.size(); g++)
                                {
                                    ItemBoardCard item = room_info.cards_boards.get(g);

                                    if (
                                            item.c1 != null
                                         && item.c2 == null

                                         && ex > item.c1.draw_pos_x - infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD / 2
                                         && ex < item.c1.draw_pos_x + item.c1.img.getWidth() + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD / 2

                                         && ey > item.c1.draw_pos_y - infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD / 2
                                         && ey < item.c1.draw_pos_y + item.c1.img.getHeight() + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD / 2
                                        )
                                    {
                                        found = true;
                                        synchronized (lock_hovered)
                                        {
                                            hovered = item.c1;
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        if (!found)
                        {
                            synchronized (lock_hovered)
                            {
                                hovered = null;
                            }
                        }
                    }
                }

                return true;

            case MotionEvent.ACTION_UP: // отжатие

                onTouchEvent_ACTION_UP(event.getX(), event.getY());

                break;

            case MotionEvent.ACTION_OUTSIDE:

                //pressed = false;
                //canMove = false;
                //zoomTrans = false;

                //Log.i("TAG", "MotionEvent.ACTION_OUTSIDE");
                break;

            case MotionEvent.ACTION_CANCEL:
                //pressed = false;
                //canMove = false;
                //zoomTrans = false;

                //Log.i("TAG", "MotionEvent.ACTION_CANCEL");
                break;

            default:

                //Log.i("TAG", "onTouchEvent: default");
                return false; // событие не обработано
        }

        return true; // событие обработано
    }

    public void releaseUp()
    {
        onTouchEvent_ACTION_UP(lastMoveX, lastMoveY);
    }

    public void addCointsAddAnimation(String text, float pos_x, float pos_y, String color)
    {
        AnimationTextAddCoints tmp = new AnimationTextAddCoints(
                _ma.get_fontApp()
                , text
                , pos_x
                , pos_y
                , 18 * _scale_pixel
                , 2500
                , 2f
                //, Color.parseColor("#00d500")
                , Color.parseColor(color)
                , 10
                , 7
                , 100
        );

        synchronized (lock_list_animation_text_coints)
        {
            list_animation_text_coints.add(tmp);
            flag_list_animation_text_coints = true;
        }
    }

    private PointPP getPosNextDrawCardsInBoard(boolean tossed_mode)
    {
        PointPP result_pp = new PointPP(0, 0);
        result_pp.setX((float) (canvas_w2 - infoOrientation.get(orietration_screen).card_w2));
        result_pp.setY((float) (canvas_h2 - infoOrientation.get(orietration_screen).card_h2));

        int card_size = 0;

        synchronized (room_info.lock_cards_boards)
        {
            card_size = room_info.cards_boards.size() + 1;

            if (
                    room_info.cards_boards.size() > 0
                            && !tossed_mode
                            && room_info.cards_boards.get(room_info.cards_boards.size() - 1).c2 == null
                    )
            {
                card_size -= 1;
            }
        }

        if (card_size <= 0)
        {
            return result_pp;
        }

        float draw_pos_x = 0;
        float draw_pos_y = 0;

        int total_width = 0, counter = 0, count_on_line = 0;
        List<Integer> lines = new ArrayList<>();
        List<Integer> map = new ArrayList<>();
        List<Integer> index_items = new ArrayList<>();

        lines.add(0);

        for (int g = 0; g < card_size; g++)
        {
            total_width += (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD);

            if (total_width > infoOrientation.get(orietration_screen).width_draw_card_in_board)
            {
                lines.add(1);
                total_width = (int) (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD);
                counter = 1;
            }
            else
            {
                lines.set(lines.size() - 1, lines.get(lines.size() - 1) + 1);
                counter += 1;
            }

            map.add(lines.size());
            index_items.add(counter);
        }

        /*drawRectCardBoard.left = 0;
        drawRectCardBoard.top = 0;
        drawRectCardBoard.right = 0;
        drawRectCardBoard.bottom = 0;*/

        for (int g = 0; g < card_size; g++)
        {
            if (lines.size() % 2 == 0)
            {
                draw_pos_y = canvas_h2
                        - (infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD) * ((lines.size()) / 2)
                        + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD / 2
                        + (infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD) * (map.get(g) - 1)
                ;
            }
            else
            {
                draw_pos_y = canvas_h2
                        - infoOrientation.get(orietration_screen).card_h2
                        - (infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD) * ((lines.size() - 1) / 2)
                        + (infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD) * (map.get(g) - 1)
                ;
            }

            count_on_line = lines.get(map.get(g) - 1);

            if (count_on_line % 2 == 0) // карт на линии четное число
            {
                draw_pos_x = canvas_w2
                        - (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD) * ((count_on_line) / 2)
                        + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD / 2
                        + (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD) * (index_items.get(g) - 1)
                ;
            }
            else
            {
                draw_pos_x = canvas_w2
                        - infoOrientation.get(orietration_screen).card_w2
                        - (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD) * ((count_on_line) / 2)
                        + (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD) * (index_items.get(g) - 1)
                ;
            }

            /*if (drawRectCardBoard.left == 0 || drawRectCardBoard.left > draw_pos_x)
            {
                drawRectCardBoard.left = draw_pos_x;
            }

            if (drawRectCardBoard.top == 0 || drawRectCardBoard.top > draw_pos_y)
            {
                drawRectCardBoard.top = draw_pos_y;
            }

            if (drawRectCardBoard.right == 0 || drawRectCardBoard.right < draw_pos_x)
            {
                drawRectCardBoard.right = draw_pos_x;
            }

            if (drawRectCardBoard.bottom == 0 || drawRectCardBoard.bottom < draw_pos_y)
            {
                drawRectCardBoard.bottom = draw_pos_y;
            }*/

            /*if( flag1 )
            {
                draw_pos_x += ( card_w + PADDING_X_DRAW_CARD );
            }*/
        }

        /*if( room_info.cards_boards.size() > 0 )
        {
            drawRectCardBoard.left   -= PADDING_AREA_INSERT_CARD;
            drawRectCardBoard.top    -= PADDING_AREA_INSERT_CARD;
            drawRectCardBoard.right  += PADDING_AREA_INSERT_CARD + card_w;
            drawRectCardBoard.bottom += PADDING_AREA_INSERT_CARD + card_h;
        }
        else
        {
            drawRectCardBoard.left   = canvas_w2 - canvas_w2 / 2 - canvas_w2 / 4;
            drawRectCardBoard.top    = canvas_h2 - canvas_h2 / 2;
            drawRectCardBoard.right  = canvas_w2 + canvas_w2 / 2 + canvas_w2 / 4;
            drawRectCardBoard.bottom = canvas_h2 + canvas_h2 / 2;
        }*/

        result_pp.setX(draw_pos_x);
        result_pp.setY(draw_pos_y);

        return result_pp;
    }

    /*private float[] getCardTrasferFollPos()
    {
        float draw_pos_x = 0;
        float draw_pos_y = 0;

        int total_width = 0, counter = 0, count_on_line = 0;

        List<Integer> lines = new ArrayList<>();       // количество карт на линиях
        List<Integer> map = new ArrayList<>();       // номер линии по индексу карты
        List<Integer> index_items = new ArrayList<>();  // номер карты на линии

        lines.add(0);

        for (int g = 0; g < room_info.cards_boards.size() + 1; g++)
        {
            total_width += (card_w + PADDING_X_DRAW_CARD);

            if (total_width > width_draw_card_in_board)
            {
                lines.add(1);
                total_width = (card_w + PADDING_X_DRAW_CARD);
                counter = 1;
            }
            else
            {
                lines.set(lines.size() - 1, lines.get(lines.size() - 1) + 1);
                counter += 1;
            }

            map.add(lines.size());
            index_items.add(counter);
        }

        for (int g = 0; g < room_info.cards_boards.size() + 1; g++)
        {
            if (lines.size() % 2 == 0)
            {
                draw_pos_y = canvas_h2
                        - (card_h + PADDING_Y_DRAW_CARD) * (lines.size() / 2)
                        + PADDING_Y_DRAW_CARD / 2
                        + (card_h + PADDING_Y_DRAW_CARD) * (map.get(g) - 1)
                ;
            }
            else
            {
                draw_pos_y = canvas_h2
                        - card_h2
                        - (card_h + PADDING_Y_DRAW_CARD) * ((lines.size() - 1) / 2)
                        + (card_h + PADDING_Y_DRAW_CARD) * (map.get(g) - 1)
                ;
            }

            draw_pos_y -= ( orietration_screen == ORIENTATION_LANDSCAPE ) ? ( 30 ) * _scale_pixel : 0;

            count_on_line = lines.get(map.get(g) - 1);

            if (count_on_line % 2 == 0) // карт на линии четное число
            {
                draw_pos_x = canvas_w2
                        - (card_w + PADDING_X_DRAW_CARD) * (count_on_line / 2)
                        + PADDING_X_DRAW_CARD / 2
                        + (card_w + PADDING_X_DRAW_CARD) * (index_items.get(g) - 1)
                ;
            }
            else
            {
                draw_pos_x = canvas_w2
                        - card_w2
                        - (card_w + PADDING_X_DRAW_CARD) * ((count_on_line - 1) / 2)
                        + (card_w + PADDING_X_DRAW_CARD) * (index_items.get(g) - 1)
                ;
            }

        }

        return new float[]{ draw_pos_x, draw_pos_y };
    }*/

    public void generate_off_boards_cards()
    {
        synchronized (room_info.card_off_game_lock)
        {
            room_info.cards_off_game.clear();

            for (int t = 0; t < 5 && t < room_info.real_count_cards_off_game; t++)
            {
                room_info.cards_off_game.add(
                        new OffGameCardItem(
                                (float) canvas_w - user_card_on_room.getWidth() / 3 - RoomSurfaceView.randInt(10, 70)
                                , (float) canvas_h2 - user_card_on_room.getHeight() / 2 + (((int) (Math.random() * 2) == 0) ? 1 : -1) * RoomSurfaceView.randInt(30, 150)
                                , RoomSurfaceView.randInt(0, 30))
                );
            }
        }
    }

    private void runFlyCardsOfDescktoUsers()
    {
        synchronized (lock_my_animated_card_desk)
        {
            if (_ma.getAppSettings().opt_sound_in_game && _ma.fly_card_to_users != null && ! _ma.call_phone)
            {
                _ma.fly_card_to_users.start();
            }

            animation_fly_desc_card = true;

            for (int tt = 0
                 , _len = room_info.count_users <= 3 ? 5 : 4
                 ; tt < _len
                    ; tt++)
            {
                for (int k = 0; k < room_info.count_users - 1; k++)
                {
                    RoomInfo_User ruinfo = room_info.users_in_room.get(k);

                    AnimatedFlyDeck a = new AnimatedFlyDeck(
                            getResources()
                            , Cards.getDrawableResId_FromOtherCard(CARD_BLANK)

                            , -5 * _scale_pixel
                            , canvas_h2

                            , ruinfo.pos_x
                            , ruinfo.pos_y

                            , 0.64f
                            , 0.45f
                            , System.currentTimeMillis() + (tt + 1) * (k + 1) * 100
                    );

                    animated_card_desk.add(a);
                }

                AnimatedFlyDeck a = new AnimatedFlyDeck(
                        getResources()
                        , Cards.getDrawableResId_FromOtherCard(CARD_BLANK)

                        , -5 * _scale_pixel
                        , canvas_h2

                        , canvas_w2
                        , canvas_h

                        , 0.64f
                        , 0.45f
                        , System.currentTimeMillis() + (tt + 1) * 200
                );

                animated_card_desk.add(a);
            }

        } // --s*/
    }

    public void run_tossed_xod(RoomInfo_User ruinfo, int card, boolean _animation_fly_card_new_pos)
    {
        synchronized (card_fly_user_xod_lock)
        {
            float start_pos_x = ruinfo.pos_x;
            float start_pos_y = ruinfo.pos_y;// + card_h;

            float end_pos_x;
            float end_pos_y;

            PointPP t = getPosNextDrawCardsInBoard(true);

            if (_animation_fly_card_new_pos)
            {
                end_pos_x = t.getx() + infoOrientation.get(orietration_screen).card_w2;
                end_pos_y = t.gety() + infoOrientation.get(orietration_screen).card_h;
                end_pos_y -= (orietration_screen == ORIENTATION_LANDSCAPE) ? (30) * _scale_pixel : 0;
            }
            else
            {
                end_pos_x = lastDrawCardInBoard.getx() + infoOrientation.get(orietration_screen).card_w2 + infoOrientation.get(orietration_screen).card_w2 / 4;
                end_pos_y = lastDrawCardInBoard.gety() + infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).card_h2 / 2;
            }

            float center_pos_x = (start_pos_x + end_pos_x) / 2;
            float center_pos_y = (start_pos_y + end_pos_y) / 2;

            AnimatedCardFly tmp = new AnimatedCardFly(
                    getResources()
                    , card
                    , Cards.getDrawableResId_FromCard(type_cardstyle, card)
                    , scale_my_cards_pr - 30.0f
                    , start_pos_x
                    , start_pos_y
            );

            tmp.setDestinationXYUpDownAnimation(
                    scale_my_cards_pr + 10.0f
                    , center_pos_x
                    , center_pos_y
                    , scale_board_cards_pr
                    , end_pos_x
                    , end_pos_y);

            synchronized (card_fly_user_xod_lock)
            {
                card_fly_user_xod.add(tmp);
                flag_card_fly_user_xod = true;
            }

            //animation_fly_card_other_user = true;
            animation_fly_card_new_pos = _animation_fly_card_new_pos;
        }
    }

    public boolean haveAnimationFlyUserCard()
    {
        synchronized (card_fly_user_xod_lock)
        {
            return card_fly_user_xod.size() > 0;
        }
    }

    public boolean haveAnimatedCardDesk()
    {
        synchronized (lock_my_animated_card_desk)
        {
            return animated_card_desk.size() > 0;
        }
    }

    public void run_tossed_xod2(RoomInfo_User ruinfo, int card, int card_hovered, boolean _animation_fly_card_new_pos)
    {
        synchronized (card_fly_user_xod_lock)
        {
            float start_pos_x = ruinfo.pos_x;
            float start_pos_y = ruinfo.pos_y;// + card_h;

            float end_pos_x;
            float end_pos_y;

            if (card_hovered != 0)
            {
                ItemBoardCard item = room_info.getItem(card_hovered);
                if (item == null)
                {
                    return;
                }

                end_pos_x = item.c1.draw_pos_x;
                end_pos_y = item.c1.draw_pos_y;
                end_pos_y -= (orietration_screen == ORIENTATION_LANDSCAPE) ? (30) * _scale_pixel : 0;

                if (!_animation_fly_card_new_pos)
                {
                    end_pos_x += infoOrientation.get(orietration_screen).card_w2 + infoOrientation.get(orietration_screen).card_w2 / 4;
                    end_pos_y += infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).card_h2 / 2;
                }
            }
            else
            {
                PointPP t = getPosNextDrawCardsInBoard(false);

                if (_animation_fly_card_new_pos)
                {
                    end_pos_x = t.getx() + infoOrientation.get(orietration_screen).card_w2;
                    end_pos_y = t.gety() + infoOrientation.get(orietration_screen).card_h;
                    end_pos_y -= (orietration_screen == ORIENTATION_LANDSCAPE) ? (30) * _scale_pixel : 0;
                }
                else
                {
                    end_pos_x = t.getx() + infoOrientation.get(orietration_screen).card_w2 + infoOrientation.get(orietration_screen).card_w2 / 4;
                    end_pos_y = t.gety() + infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).card_h2 / 2;
                }
            }

            float center_pos_x = (start_pos_x + end_pos_x) / 2;
            float center_pos_y = (start_pos_y + end_pos_y) / 2;

            AnimatedCardFly tmp = new AnimatedCardFly(
                    getResources()
                    , card
                    , Cards.getDrawableResId_FromCard(type_cardstyle, card)
                    , scale_my_cards_pr - 30.0f
                    , start_pos_x
                    , start_pos_y
            );

            tmp.card_hovered = card_hovered;

            tmp.setDestinationXYUpDownAnimation(
                    scale_my_cards_pr + 10.0f
                    , center_pos_x
                    , center_pos_y
                    , scale_board_cards_pr
                    , end_pos_x
                    , end_pos_y);

            //synchronized (card_fly_user_xod_lock)
            {
                card_fly_user_xod.add(tmp);
                flag_card_fly_user_xod = true;
            }

            //animation_fly_card_other_user = true;
            animation_fly_card_new_pos = _animation_fly_card_new_pos;
        }
    }

    public void hideWaitTime()
    {
        loadingWait = null;
    }

    public void startWaitTime()
    {
        loadingWait = new LoadingWait(getContext());
    }

    public boolean isInitDraw()
    {
        return this.is_first_draw;
    }

    // вычисление позиций для игроков
    public void calculatingPositionsForGambling()
    {
        float R0 = 2 * Math.max(canvas_w, canvas_h);
        float x0 = canvas_w2;
        float y0 = default_avatar.getHeight() + R0;

        float alfa, start_angle, x, y;

        if (canvas_w < canvas_h)
        {
            alfa = (float) Math.toDegrees(Math.asin(((canvas_w - 12 * _scale_pixel * 2) / 2) / R0));
        }
        else
        {
            y0 -= 20 * _scale_pixel;
            alfa = (float) Math.toDegrees(Math.asin(((canvas_h - 12 * _scale_pixel * 2) / 2) / R0));
            alfa *= 1.4f;
        }

        //alfa = 30;

        start_angle = 180 + alfa;

        alfa *= 2;
        float _step = alfa / ((float) (room_info.users_in_room.size()));
        float _step2 = _step / 2;
        float a = start_angle;

        for (int i = 0, l = room_info.users_in_room.size(); i < l; i++)
        {
            RoomInfo_User item = room_info.users_in_room.get(i);

            x = (float) (x0 + R0 * Math.sin(Math.toRadians(a - _step2)));
            y = (float) (y0 + R0 * Math.cos(Math.toRadians(a - _step2)));

            item.pos_x = x;
            item.pos_y = y;

            /*if (item.user_photo != null && y - item.user_photo.getHeight() < 0)
            {
                y = item.user_photo.getHeight() + 1; // +1 px
            }*/

            a -= _step;
        }
    }

    public void addAnimationNimber(AnimationNumber add)
    {
        synchronized (lock_list_animation_number)
        {
            list_animation_number.add(add);
            flag_list_animation_number = true;
        }
    }

    private void initFidrstDraw()
    {
        /*width_draw_card_in_board = (int) ((canvas_w * PROCENTAGE_MAX_WIDTH_DRAW_CARD_IN_BOARD) / 100.0f);

        if (orietration_screen == ORIENTATION_LANDSCAPE)
        {
            width_draw_card_in_board = (int) ((canvas_w * 95.0f) / 100.0f);
        }*/

        room_info.my_userinfo.pos_x = canvas_w2;
        room_info.my_userinfo.pos_y = canvas_h - 10 * _scale_pixel;

        lastDrawCardInBoard = new PointPP(canvas_w2 - infoOrientation.get(orietration_screen).card_w2, canvas_h2 - infoOrientation.get(orietration_screen).card_h2);

        generate_off_boards_cards();

        /*if (mPaint == null)
        {
            mPaint = new Paint();
            mPaint.setColor(bk_color);
            mPaint.setStrokeWidth(1);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setShader(new RadialGradient(
                    canvas_w2
                    , canvas_h2
                    , canvas_h / 1 // raduis
                    , Color.parseColor("#EEEEEE")
                    , bk_color
                    , Shader.TileMode.MIRROR)
            );
        }*/

        trump_card_in_the_deck_matrix = new Matrix();
        trump_card_in_the_deck_matrix.setRotate(97.0f, infoOrientation.get(orietration_screen).card_w / 2, 0);
        trump_card_in_the_deck_matrix.postTranslate(infoOrientation.get(orietration_screen).card_w - 35 * _scale_pixel, canvas_h2 - 17 * _scale_pixel);

        deck_rotate_matrix = new Matrix();
        deck_rotate_matrix.setRotate(7.0f, infoOrientation.get(orietration_screen).card_w / 2, infoOrientation.get(orietration_screen).card_h / 2);
        deck_rotate_matrix.postTranslate(-42 * _scale_pixel, canvas_h2 - infoOrientation.get(orietration_screen).card_h2 - 25 * _scale_pixel);

        calculatingPositionsForGambling();

        R_MAIN = Math.max(canvas_h, canvas_w) * 2.5f + (orietration_screen == ORIENTATION_LANDSCAPE ? 35 * _scale_pixel : 0);
        limit_angle = 180.0f - 2 * (float) Math.toDegrees(Math.acos((canvas_w - infoOrientation.get(orietration_screen).card2_w) / (R_MAIN * 2)));
        min_step = (float) (180.0f - 90.0f - Math.toDegrees(Math.atan(R_MAIN / infoOrientation.get(orietration_screen).card2_w)));

        x0 = canvas_w2;
        y0 = canvas_h + R_MAIN + infoOrientation.get(orietration_screen).card2_h4 + (orietration_screen == ORIENTATION_LANDSCAPE ? 10 * _scale_pixel : 0);

        drawRectCardBoard = new RectF();

        if (orietration_screen == ORIENTATION_LANDSCAPE)
        {
            drawRectCardBoard.left = canvas_w2 - canvas_w2 * 0.8f;
            drawRectCardBoard.top = canvas_h2 - canvas_h2 * 0.6f;
            drawRectCardBoard.right = canvas_w2 + canvas_w2 * 0.9f;
            drawRectCardBoard.bottom = canvas_h2 + canvas_h2 * 0.35f;
        }
        else
        {
            drawRectCardBoard.left = canvas_w2 - canvas_w2 * 0.8f;
            drawRectCardBoard.top = canvas_h2 - canvas_h2 * 0.5f;
            drawRectCardBoard.right = canvas_w2 + canvas_w2 * 0.9f;
            drawRectCardBoard.bottom = canvas_h2 + canvas_h2 * 0.5f;
        }

        //buildBackgroundImage();
        //callbackDrawBkImg(background_big_img);

        /*fade_bk = Bitmap.createBitmap(canvas_w, canvas_h, Bitmap.Config.ARGB_8888);
        Canvas _canvas = new Canvas(fade_bk);

        Paint _p = new Paint();
        _p.setColor(Color.BLACK);
        _p.setAlpha(130);

        _canvas.drawRect(0, 0, canvas_w, canvas_h, _p);*/
    }

    private void buildWindowUserInfo(int uid)
    {
        fade_bk = Bitmap.createBitmap(canvas_w, canvas_h, Bitmap.Config.ARGB_8888);
        Canvas _canvas = new Canvas(fade_bk);

        Paint _p = new Paint();
        _p.setColor(Color.BLACK);
        _p.setAlpha(130);

        _canvas.drawRect(0, 0, canvas_w, canvas_h, _p);

        //****************************************************************************************

        float offset_top = 0;

        RoomInfo_User user_info = getInfoUser(uid);

        int h = (int) (

                (( ! (user_info.exists_in_friends || user_info.is_bot) )
                        ? 360
                        : 320)
                        * _scale_pixel);

        if( orietration_screen == ORIENTATION_LANDSCAPE )
        {
            while( canvas_h < h )
            {
                h -= 2;
            }
        }

        window_user_info = Bitmap.createBitmap((int) (220 * _scale_pixel), h, Bitmap.Config.ARGB_8888);
        _canvas = new Canvas(window_user_info);

        _canvas.drawColor(Color.parseColor("#eeeeee"));

        Paint paint_rect = new Paint();
        paint_rect.setColor(Color.BLACK);
        paint_rect.setStyle(Paint.Style.STROKE);
        paint_rect.setStrokeWidth(3);

        _canvas.drawRect(0, 0, window_user_info.getWidth(), window_user_info.getHeight(), paint_rect);

        GButton close_btn = new GButton( getResources(), null, R.drawable.close0, R.drawable.close0_select )
        {
            @Override
            public void callbackPressed()
            {
                setStatus(GUiStatus.STATUS_DEFAULT);
                resetSelectUsers();
                show_wondow_user_info = false;
            }
        };
        close_btn.setMargin( 5 * _scale_pixel, 5 * _scale_pixel, 5 * _scale_pixel, 5 * _scale_pixel);

        ui_buttons.put("btn_close_window_user_info", close_btn);

        offset_top = 10 * _scale_pixel;

        _canvas.drawBitmap(
                      user_info.user_photo
                ,window_user_info.getWidth() /2 - user_info.user_photo.getWidth() / 2
                , offset_top
                , null
        );

        offset_top += user_info.user_photo.getHeight();

        float padding_left_right = 10 * _scale_pixel;
        float _font_size = 20 * _scale_pixel;


        Paint txt_paint = new Paint();
        txt_paint.setTypeface( _fontApp_bold );
        txt_paint.setTextSize( _font_size );
        txt_paint.setColor(Color.parseColor("#0017a0"));

        Rect bounds = new Rect();

        txt_paint.getTextBounds(user_info.first_name, 0, user_info.first_name.length(), bounds);

        while( bounds.width() > window_user_info.getWidth() - padding_left_right * 2 )
        {
            _font_size -= 0.5;
            txt_paint.getTextBounds(user_info.first_name, 0, user_info.first_name.length(), bounds);
        }

        offset_top += 5 * _scale_pixel + bounds.height();

        _canvas.drawText(
                     user_info.first_name
                , window_user_info.getWidth() / 2 - bounds.width() / 2
                , offset_top
                , txt_paint );



        _font_size = 22 * _scale_pixel;
        txt_paint.setTextSize( _font_size );

        Rect bounds2 = new Rect();
        txt_paint.getTextBounds(user_info.last_name, 0, user_info.last_name.length(), bounds2);

        while( bounds2.width() > window_user_info.getWidth() - padding_left_right * 2 )
        {
            _font_size -= 0.5;
            txt_paint.getTextBounds(user_info.last_name, 0, user_info.last_name.length(), bounds2);
        }

        offset_top += 3 * _scale_pixel + bounds2.height();

        _canvas.drawText(
                user_info.last_name
                , window_user_info.getWidth() / 2 - bounds2.width() / 2
                , offset_top
                , txt_paint );

        offset_top += 7 * _scale_pixel;
        txt_paint.setColor(Color.BLACK);

        // таблица статитики по игроку
        _font_size = 20 * _scale_pixel;
        txt_paint.setTextSize( _font_size );
        txt_paint.getTextBounds("A", 0, 1, bounds);

        Bitmap img;
        //------------------------------------------------------------------------------------------
        img = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        _canvas.drawBitmap(img, padding_left_right, offset_top, null);
        _canvas.drawText(
                getResources().getString(R.string.txt_53) + " " + String.valueOf(user_info.info_raiting)
                , padding_left_right + 44 * _scale_pixel
                , offset_top + img.getHeight() / 2 + bounds.height() / 2
                , txt_paint );

        offset_top += img.getHeight() + 5 * _scale_pixel;

        //------------------------------------------------------------------------------------------
        img = BitmapFactory.decodeResource(getResources(), R.drawable.stat);
        _canvas.drawBitmap(img, padding_left_right, offset_top, null);
        _canvas.drawText(
                  getResources().getString(R.string.txt_51) + " " + String.valueOf(user_info.info_count_games)
                , padding_left_right + 44 * _scale_pixel
                , offset_top + img.getHeight() / 2 + bounds.height() / 2
                , txt_paint );

        offset_top += img.getHeight() + 5 * _scale_pixel;

        //------------------------------------------------------------------------------------------
        img = BitmapFactory.decodeResource(getResources(), R.drawable.total_win);
        _canvas.drawBitmap(img, padding_left_right, offset_top, null);
        _canvas.drawText(
                getResources().getString(R.string.txt_52) + " " + String.valueOf(user_info.info_count_wins)
                , padding_left_right + 44 * _scale_pixel
                , offset_top + img.getHeight() / 2 + bounds.height() / 2
                , txt_paint );

        offset_top += img.getHeight() + 5 * _scale_pixel;

        //------------------------------------------------------------------------------------------
        img = BitmapFactory.decodeResource(getResources(), R.drawable.game_over);
        _canvas.drawBitmap(img, padding_left_right, offset_top, null);
        _canvas.drawText(
                getResources().getString(R.string.txt_54) + " " + String.valueOf(user_info.info_count_defeats)
                , padding_left_right + 44 * _scale_pixel
                , offset_top + img.getHeight() / 2 + bounds.height() / 2
                , txt_paint );

        offset_top += img.getHeight() + 5 * _scale_pixel;

        //------------------------------------------------------------------------------------------
        img = BitmapFactory.decodeResource(getResources(), R.drawable.draw);
        _canvas.drawBitmap(img, padding_left_right, offset_top, null);
        _canvas.drawText(
                getResources().getString(R.string.txt_55) + " " + String.valueOf(user_info.info_count_draw)
                , padding_left_right + 44 * _scale_pixel
                , offset_top + img.getHeight() / 2 + bounds.height() / 2
                , txt_paint );

        offset_top += img.getHeight() + 5 * _scale_pixel;

        //------------------------------------------------------------------

        if ( ! (user_info.exists_in_friends || user_info.is_bot) )
        {
            Bitmap btn_1 = GButton.generateButton(
                    "ДОБАВИТЬ В ДРУЗЬЯ"
                    , _fontApp_bold
                    , 20 * _scale_pixel
                    , 15 * _scale_pixel
                    , 6 * _scale_pixel
                    , 15 * _scale_pixel
                    , 8 * _scale_pixel
                    , Color.parseColor("#00a658")
                    , Color.parseColor("#00a658")
                    , Color.YELLOW
                    , 2 * _scale_pixel
                    , 1.8f * _scale_pixel
            );

            Bitmap btn_2 = GButton.generateButton(
                    "ДОБАВИТЬ В ДРУЗЬЯ"
                    , _fontApp_bold
                    , 20 * _scale_pixel
                    , 15 * _scale_pixel
                    , 6 * _scale_pixel
                    , 15 * _scale_pixel
                    , 8 * _scale_pixel
                    , Color.parseColor("#00d872")
                    , Color.parseColor("#f4bf1a")
                    , Color.YELLOW
                    , 2 * _scale_pixel
                    , 1.8f * _scale_pixel
            );

            GButton btn_test = new GButton(uid, btn_1, btn_2)
            {
                @Override
                public void callbackPressed()
                {
                    show_wondow_user_info = false;

                    callbackAddToFriend( (int) getData() );
                }
            };

            btn_test.setMargin(5 * _scale_pixel, 5 * _scale_pixel, 5 * _scale_pixel, 5 * _scale_pixel);

            ui_buttons.put("btn_test", btn_test);
        }

    }

    private void buildBackgroundImage()
    {
        //Log.i("TAG54", "buildBackgroundImage");

        if (_bk != null)
        {
            background_big_img = Bitmap.createBitmap(canvas_w, canvas_h, Bitmap.Config.ARGB_8888);
            Canvas _canvas = new Canvas(background_big_img);

            for (int y = 0, height = canvas_h; y < height; y += _bk.getHeight())
            {
                for (int x = 0, width = canvas_w; x < width; x += _bk.getWidth())
                {
                    _canvas.drawBitmap(_bk, x, y, null);
                }
            }

            _canvas.drawText(type_game_txt + "  " + stavka_txt
                    , 5 * _scale_pixel
                    , 15 * _scale_pixel
                    , pain_drawTextInfo);

            /// козырная карта
            if (draw_trump_card_in_the_deck && room_info.trump_card_in_the_deck != 0)
            {
                _canvas.drawBitmap(
                        infoOrientation.get(orietration_screen).list_cards_draw.get(room_info.trump_card_in_the_deck).img
                        , trump_card_in_the_deck_matrix
                        , paint_drawFilterBitmap
                );
            }

            /// рисуем колоду карт
            if (draw_deck && room_info.count_deck_of_cards > 0)
            {
                //canvas.drawBitmap( list_cards_draw.get(Cards.BLANK).img, 0, canvas_h2 - list_cards_draw.get(Cards.BLANK).img.getHeight()/2, paint_drawBitmap );

                //Matrix matrix = new Matrix();
                //matrix.setTranslate();
                //matrix.setRotate(7.0f, card_w/2, card_h/2);
                //matrix.postTranslate(-5 * _scale_pixel, canvas_h2);

                if( room_info.trump_card_in_the_deck > 1 )
                {
                    _canvas.drawBitmap(infoOrientation.get(orietration_screen).list_cards_draw.get(Cards.BLANK).img, deck_rotate_matrix, paint_drawFilterBitmap);
                }

                _canvas.drawText(String.valueOf(room_info.count_deck_of_cards), 7 * _scale_pixel, canvas_h2 - 10 * _scale_pixel, pain_drawTextCountDesc);
            }

            if (stat_game && room_info.trump_card_in_the_deck != 0)
            {
                _canvas.drawBitmap(list_cards_icons.get(infoOrientation.get(orietration_screen).list_cards_draw.get(room_info.trump_card_in_the_deck).card_info.type), 1 * _scale_pixel, canvas_h2 - 65 * _scale_pixel, pain_drawTextCountDesc);
            }

            if (draw_off_game_cards)
            {
                synchronized (room_info.card_off_game_lock)
                {
                    for (int d = 0, _len = room_info.cards_off_game.size(); d < _len; d++)
                    {
                        Matrix matrix = new Matrix();

                        matrix.setRotate(
                                room_info.cards_off_game.get(d).angle
                                , user_card_on_room.getWidth() / 2
                                , user_card_on_room.getHeight() / 2
                        );

                        matrix.postTranslate(room_info.cards_off_game.get(d).x, room_info.cards_off_game.get(d).y);

                        _canvas.drawBitmap(user_card_on_room, matrix, paint_drawFilterBitmap);
                    }

                    if (delete_all_cards_off_game && room_info.cards_off_game.size() > 0)
                    {
                        room_info.cards_off_game.remove(0);

                        if (room_info.cards_off_game.size() == 0)
                        {
                            delete_all_cards_off_game = false;
                        }
                    }
                }
            }

            // игроки за столом
            synchronized (room_info.lock_list_matrix_users_cards)
            {
                for (int i = 0, len = room_info.users_in_room.size(); i < len; i++)
                {
                    RoomInfo_User item = room_info.users_in_room.get(i);

                    if (item.init_user && item.drawUserCards)
                    {
                        int _N = room_info.users_in_room.get(i).tmp_count_cards;

                        if (_N > 0)
                        {
                            if (_N > 10)
                            {
                                _N = 10;
                            }

                            boolean _flag = (_N % 2 == 0);
                            float _limit_angle = (room_info.users_in_room.size() < 6) ? 45 : 35;
                            float _start = 0;
                            float _step = _limit_angle / _N;
                            float _R = user_card_on_room.getHeight() / 2;// - user_card_on_room.getHeight() / 5;
                            float _min_step = (float) (180.0f - 90.0f - Math.toDegrees(Math.atan(_R / (user_card_on_room.getWidth()))));

                            if (_N <= 4 || _step > _min_step - _min_step / 5)
                            {
                                _step = Math.min(_step, _min_step - _min_step / 5);
                            }

                            // четное кол-во
                            if (_flag)
                            {
                                _start = _step / 2 + (_N - 1) / 2 * _step;
                            }
                            else
                            {
                                _start = (_N - 1) / 2 * _step;
                            }

                            int index = 0;

                            double __x0 = item.pos_x;
                            double __y0 = item.pos_y - item.user_photo.getHeight() * 0.6f;

                            for (float a = _start; index < _N; a -= _step, index++)
                            {
                                double _x = __x0 + (_R + (_N - index) * 1.0f * _scale_pixel) * Math.sin(Math.toRadians(a));
                                double _y = __y0 + (_R + (_N - index) * 1.0f * _scale_pixel) * Math.cos(Math.toRadians(a));

                                Matrix matrix = new Matrix();
                                matrix.setRotate(180 - a, user_card_on_room.getWidth() / 2, user_card_on_room.getHeight());
                                matrix.postTranslate((float) _x - user_card_on_room.getWidth() / 2, (float) _y - user_card_on_room.getHeight());

                                _canvas.drawBitmap(user_card_on_room, matrix, paint_drawFilterBitmap);
                            }
                        }

                    }

                    if ( stat_game && item.init_user)
                    {
                        _canvas.drawBitmap(
                                item.user_photo
                                , item.pos_x - item.user_photo.getWidth() / 2
                                , item.pos_y - item.user_photo.getHeight() / 2
                                , paint_drawFilterBitmap
                        );

                        if( item.offset_position_num == room_info.current_pos_user_xod )
                        {
                            _canvas.drawText(item.first_name
                                    , item.pos_x - item.uname_draw_rect.width() / 2
                                    , item.pos_y + item.user_photo.getHeight() / 2 + item.uname_draw_rect.height() + 3 * _scale_pixel
                                    , paint_drawTextUserNikName_2);
                        }
                        else
                        {
                            _canvas.drawText(item.first_name
                                    , item.pos_x - item.uname_draw_rect.width() / 2
                                    , item.pos_y + item.user_photo.getHeight() / 2 + item.uname_draw_rect.height() + 3 * _scale_pixel
                                    , paint_drawTextUserNikName);
                        }

                    } // -- if
                }

                for (int i = 0, len = room_info.users_in_room.size(); i < len; i++)
                {
                    RoomInfo_User item = room_info.users_in_room.get(i);

                    if ( stat_game && item.init_user)
                    {
                        if( item.tmp_count_cards > 0 )
                        {
                            _canvas.drawText(String.valueOf(
                                    item.tmp_count_cards)
                                    , item.pos_x + item.user_photo.getHeight() / 2
                                    , item.pos_y + item.user_photo.getWidth() / 2 - 1 * _scale_pixel
                                    ,
                                    item.offset_position_num == room_info.current_pos_user_xod
                                            ? paint_drawTextUserNikName
                                            : pain_drawTextCountDesc
                            );
                        }
                    }
                }

            } // -- synch
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if( canvas == null ) { return; }

        if (this._stop_draw)
        {
            canvas.drawColor(Color.BLACK);
            return;
        }

        super.onDraw(canvas);

        if (this.is_first_draw)
        {
            canvas_w = getWidth();
            canvas_h = getHeight();

            //initAllCards();

            callbackInit();

            //initFidrstDraw();

            is_first_draw = false;

            System.gc();

        } /// - first draw

        //initFidrstDraw();

        if (needRebuildBackground)
        {
            buildBackgroundImage();
            needRebuildBackground = false;

            callbackDrawBkImg(background_big_img);
        }

        /*if( drawBkImg )
        {
            canvas.drawBitmap(background_big_img, 0, 0, null);
        }
        else*/
        //{
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //}

        /*Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(0, 0, canvas_w, canvas_h, clearPaint);*/

        synchronized (room_info.lock_list_matrix_users_cards)
        {
            for (__i = 0, __len = room_info.users_in_room.size(); __i < __len; __i++)
            {
                RoomInfo_User item = room_info.users_in_room.get(__i);

                if(  item.init_user )
                {
                    //нарисовать карты у игрока
                    /*if (item.drawUserCards)
                    {
                        if (item.list_matrix_user_cards == null)
                        {
                            int _N = room_info.users_in_room.get(i).tmp_count_cards;

                            if (_N > 0)
                            {
                                if( _N > 10 ) { _N = 10; }

                                boolean _flag = (_N % 2 == 0);
                                float _limit_angle = 45;
                                float _start = 0;
                                float _step = _limit_angle / _N;
                                float _R = user_card_on_room.getHeight() / 2;// - user_card_on_room.getHeight() / 5;
                                float _min_step = (float) (180.0f - 90.0f - Math.toDegrees(Math.atan(_R / (user_card_on_room.getWidth()))));

                                if (_N <= 4 || _step > _min_step - _min_step / 5) {
                                    _step = Math.min(_step, _min_step - _min_step / 5);
                                }

                                // четное кол-во
                                if (_flag) {
                                    _start = _step / 2 + (_N - 1) / 2 * _step;
                                } else {
                                    _start = (_N - 1) / 2 * _step;
                                }

                                int index = 0;

                                double __x0 = item.pos_x;
                                double __y0 = item.pos_y - item.user_photo.getHeight() * 0.6f;

                                item.list_matrix_user_cards = new ArrayList<>();

                                for (float a = _start; index < _N; a -= _step, index++)
                                {
                                    double _x = __x0 + (_R + (_N - index) * 1.0f * _scale_pixel) * Math.sin(Math.toRadians(a));
                                    double _y = __y0 + (_R + (_N - index) * 1.0f * _scale_pixel) * Math.cos(Math.toRadians(a));

                                    Matrix matrix = new Matrix();
                                    matrix.setRotate(180 - a, user_card_on_room.getWidth() / 2, user_card_on_room.getHeight());
                                    matrix.postTranslate((float) _x - user_card_on_room.getWidth() / 2, (float) _y - user_card_on_room.getHeight());

                                    item.list_matrix_user_cards.add(matrix);

                                    canvas.drawBitmap(user_card_on_room, matrix, paint_drawBitmap2);
                                }
                            }
                        }
                        else if( stat_game )
                        {
                            for (int kk = 0, _len0 = item.list_matrix_user_cards.size(); kk < _len0; kk++)
                            {
                                canvas.drawBitmap(user_card_on_room, item.list_matrix_user_cards.get(kk), paint_drawBitmap2);
                            }
                        }
                    }*/

                    if (item.timerWork)
                    {
                        int pr = item.checkTimer();

                        if (item.mOval == null)
                        {
                            float plus_r = 5 * _scale_pixel;

                            item.mOval = new RectF(
                                    item.pos_x - item.user_photo.getWidth() / 2 - plus_r
                                    , item.pos_y - item.user_photo.getHeight() / 2 - plus_r
                                    , item.pos_x + item.user_photo.getWidth() / 2 + plus_r
                                    , item.pos_y + item.user_photo.getHeight() / 2 + plus_r
                            );
                        }

                        if (pr % 5 == 0)
                        {
                            //paint_timer_draw.setColor( ColorUtils.getColor(color_start_timer_wait_user, color_end_timer_wait_user, pr * 100.0f / 360.0f ) );
                            paint_timer_draw.setColor(ColorBetwheen.getColor(list_colors, (pr * 1.0f / 360.0f)));
                        }

                        item.timer_path.reset();
                        item.timer_path.addArc(item.mOval, 180, pr);

                        canvas.drawPath(item.timer_path, paint_timer_draw);
                    }

                    if (item.selected)
                    {
                        canvas.drawCircle(
                                item.pos_x
                                , item.pos_y
                                , item.user_photo.getWidth() / 2
                                , paint_SelectedUser
                        );
                    }

                    if ( ! stat_game && item.user_photo != null)
                    {
                        canvas.drawBitmap(
                                item.user_photo
                                , item.pos_x - item.user_photo.getWidth() / 2
                                , item.pos_y - item.user_photo.getHeight() / 2
                                , paint_drawBitmap
                        );

                        if( item.uname_draw_rect != null )
                        {
                            canvas.drawText(item.first_name
                                    , item.pos_x - item.uname_draw_rect.width() / 2
                                    , item.pos_y + item.user_photo.getHeight() / 2 + item.uname_draw_rect.height() + 3 * _scale_pixel
                                    , paint_drawTextUserNikName);
                        }
                    }


                    if (draw_ready_status_users && item.ready)
                    {
                        canvas.drawBitmap(
                                status_ready_play_user
                                , item.pos_x - item.user_photo.getWidth() / 2 + 3 * _scale_pixel
                                , item.pos_y + item.user_photo.getHeight() / 3 - 7 * _scale_pixel
                                , paint_drawBitmap
                        );
                    }

                    if (stat_game && item.draw_bery_text != null)
                    {
                        if (orietration_screen == ORIENTATION_LANDSCAPE)
                        {
                            item.draw_bery_text.draw(
                                    item.pos_x
                                    , item.pos_y - item.user_photo.getHeight() / 2 + item.draw_bery_text.rect_h * 0.4f
                                    , canvas);
                        }
                        else
                        {
                            item.draw_bery_text.draw(
                                    item.pos_x
                                    , item.pos_y - item.user_photo.getHeight() / 2 - item.draw_bery_text.rect_h * 0.2f
                                    , canvas);
                        }
                    }
                }
                else
                {
                    if (tmp_angle_timer % 2 == 0)
                    {
                        canvas.drawCircle(
                                item.pos_x
                                , item.pos_y
                                , 25 * _scale_pixel
                                , paint_strokeDashed
                        );
                    }
                    else
                    {
                        canvas.drawCircle(
                                item.pos_x
                                , item.pos_y
                                , 25 * _scale_pixel
                                , paint_strokeDashed1
                        );
                    }

                    matrix_waiting_user.reset();
                    matrix_waiting_user.postRotate(tmp_angle_timer, waiting_user.getWidth() / 2, waiting_user.getHeight() / 2);
                    matrix_waiting_user.postTranslate(item.pos_x - waiting_user.getWidth() / 2
                            , item.pos_y - waiting_user.getHeight() / 2);

                    canvas.drawBitmap(
                            waiting_user
                            , matrix_waiting_user
                            , paint_drawBitmap
                    );
                }
            }
        }

        if (draw_my_cards)
        {
            synchronized (room_info.lock_draw)
            {
                synchronized (room_info.lock_cards_boards)
                {
                    cards_boards_size = room_info.cards_boards.size();

                    if (cards_boards_size == 0)
                    {
                        lastDrawCardInBoard = new PointPP(canvas_w2 - infoOrientation.get(orietration_screen).card_w2, canvas_h2 - infoOrientation.get(orietration_screen).card_h2);
                        firstDrawCardInBoard = new PointPP(canvas_w2 - infoOrientation.get(orietration_screen).card_w2, canvas_h2 - infoOrientation.get(orietration_screen).card_h2);
                    }
                    else
                    {
                        if (animation_fly_card_off_board || ! room_info.cache_draw_cards_board )
                        {
                            cards_boards_draw_first_pos_x = 0;
                            cards_boards_draw_first_pos_y = 0;
                            cards_boards_draw_pos_x = 0;
                            cards_boards_draw_pos_y = 0;

                            cards_boardstotal_width = 0;
                            cards_boards_counter = 0;

                            lines.clear();          // количество карт на линиях
                            map.clear();            // номер линии по индексу карты
                            index_items.clear();    // номер карты на линии

                            lines.add(0);

                            for (__i = 0/*, _len = room_info.cards_boards.size()*/; __i < cards_boards_size; __i++)
                            {
                                cards_boardstotal_width += (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD);

                                if (cards_boardstotal_width > infoOrientation.get(orietration_screen).width_draw_card_in_board)
                                {
                                    lines.add(1);
                                    cards_boardstotal_width = (int) (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD);
                                    cards_boards_counter = 1;
                                }
                                else
                                {
                                    lines.set(lines.size() - 1, lines.get(lines.size() - 1) + 1);
                                    cards_boards_counter += 1;
                                }

                                map.add(lines.size());
                                index_items.add(cards_boards_counter);
                            }

                            for (__i = 0/*, _len = room_info.cards_boards.size()*/; __i < cards_boards_size; __i++)
                            {
                                ItemBoardCard item = room_info.cards_boards.get(__i);

                                if ( ! item.need_draw1 )
                                {
                                    continue;
                                }

                                if (lines.size() % 2 == 0)
                                {
                                    cards_boards_draw_pos_y = canvas_h2
                                            - (infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD) * (lines.size() / 2)
                                            + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD / 2
                                            + (infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD) * (map.get(__i) - 1)
                                    ;
                                }
                                else
                                {
                                    cards_boards_draw_pos_y = canvas_h2
                                            - infoOrientation.get(orietration_screen).card_h2
                                            - (infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD) * ((lines.size() - 1) / 2)
                                            + (infoOrientation.get(orietration_screen).card_h + infoOrientation.get(orietration_screen).PADDING_Y_DRAW_CARD) * (map.get(__i) - 1)
                                    ;
                                }

                                cards_boards_draw_pos_y -= (orietration_screen == ORIENTATION_LANDSCAPE) ? (30) * _scale_pixel : 0;

                                cards_boards_count_on_line = lines.get(map.get(__i) - 1);

                                if (cards_boards_count_on_line % 2 == 0) // карт на линии четное число
                                {
                                    cards_boards_draw_pos_x = canvas_w2
                                            - (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD) * (cards_boards_count_on_line / 2)
                                            + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD / 2
                                            + (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD) * (index_items.get(__i) - 1)
                                    ;
                                }
                                else
                                {
                                    cards_boards_draw_pos_x = canvas_w2
                                            - infoOrientation.get(orietration_screen).card_w2
                                            - (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD) * ((cards_boards_count_on_line - 1) / 2)
                                            + (infoOrientation.get(orietration_screen).card_w + infoOrientation.get(orietration_screen).PADDING_X_DRAW_CARD) * (index_items.get(__i) - 1)
                                    ;
                                }

                                if (cards_boards_draw_first_pos_x == 0 && cards_boards_draw_first_pos_y == 0)
                                {
                                    cards_boards_draw_first_pos_x = cards_boards_draw_pos_x;
                                    cards_boards_draw_first_pos_y = cards_boards_draw_pos_y;
                                }

                                if (item.c1.img != null && item.need_draw1)
                                {
                                    item.c1.draw_pos_x = cards_boards_draw_pos_x;
                                    item.c1.draw_pos_y = cards_boards_draw_pos_y;

                                    /*canvas.drawBitmap(
                                            room_info.cards_boards.get(g).c1.img
                                            , room_info.cards_boards.get(g).c1.draw_pos_x + room_info.cards_boards.get(g).c1.animation_offset_x
                                            , room_info.cards_boards.get(g).c1.draw_pos_y + room_info.cards_boards.get(g).c1.animation_offset_y
                                            , paint_drawBitmap
                                    );*/
                                }

                                if (item.c2 != null && item.need_draw2)
                                {
                                    //float a = 12;
                                    item.matrix_draw.reset();
                                    //matrix.setRotate(180 - a - counter_draw, card_w2, 0);
                                    item.matrix_draw.setRotate(12, infoOrientation.get(orietration_screen).card_w2, infoOrientation.get(orietration_screen).card_h2);
                                    item.matrix_draw.postTranslate(
                                            cards_boards_draw_pos_x + infoOrientation.get(orietration_screen).card_w2 / 4 + item.c1.animation_offset_x
                                            , cards_boards_draw_pos_y + infoOrientation.get(orietration_screen).card_h2 / 2 + item.c1.animation_offset_y
                                    );

                                    item.c2.draw_pos_x = cards_boards_draw_pos_x + infoOrientation.get(orietration_screen).card_w2 / 4;
                                    item.c2.draw_pos_y = cards_boards_draw_pos_y + infoOrientation.get(orietration_screen).card_h2 / 2;

                                    //canvas.drawBitmap(room_info.cards_boards.get(g).c2.img, matrix, paint_drawBitmap);
                                }
                            }

                            if (cards_boards_draw_pos_x == 0 && cards_boards_draw_pos_y == 0)
                            {
                                lastDrawCardInBoard = new PointPP(canvas_w2 - infoOrientation.get(orietration_screen).card_w2, canvas_h2 - infoOrientation.get(orietration_screen).card_h2);
                                firstDrawCardInBoard = new PointPP(canvas_w2 - infoOrientation.get(orietration_screen).card_w2, canvas_h2 - infoOrientation.get(orietration_screen).card_h2);
                            }
                            else
                            {
                                if (lastDrawCardInBoard == null)
                                {
                                    lastDrawCardInBoard = new PointPP(cards_boards_draw_pos_x, cards_boards_draw_pos_y);
                                }
                                else
                                {
                                    lastDrawCardInBoard.setX(cards_boards_draw_pos_x);
                                    lastDrawCardInBoard.setY(cards_boards_draw_pos_y);
                                }

                                if (firstDrawCardInBoard == null)
                                {
                                    firstDrawCardInBoard = new PointPP(cards_boards_draw_first_pos_x, cards_boards_draw_first_pos_y);
                                }
                                else
                                {
                                    firstDrawCardInBoard.setX(cards_boards_draw_first_pos_x);
                                    firstDrawCardInBoard.setY(cards_boards_draw_first_pos_y);
                                }

                            }

                            room_info.cache_draw_cards_board = true;
                        }

                        synchronized (lock_hovered)
                        {

                            for (__i = 0/*, _len = room_info.cards_boards.size()*/; __i < cards_boards_size; __i++)
                            {
                                ItemBoardCard item = room_info.cards_boards.get(__i);

                                if (item.c1.img != null && item.need_draw1)
                                {
                                    canvas.drawBitmap(
                                            item.c1.img
                                            , item.c1.draw_pos_x + item.c1.animation_offset_x
                                            , item.c1.draw_pos_y + item.c1.animation_offset_y
                                            , paint_drawBitmap
                                    );

                                    if (!animation_fly_card_off_board)
                                    {
                                        //synchronized (lock_hovered)
                                        //{
                                        if (hovered != null && hovered.card_info.card == item.c1.card_info.card)
                                        {
                                            canvas.drawBitmap(
                                                    infoOrientation.get(orietration_screen).list_cards_draw.get(Cards.SELECT_IN_BOARD).img
                                                    , hovered.draw_pos_x
                                                    , hovered.draw_pos_y, paint_drawBitmap);
                                        }
                                        //}
                                    }
                                }

                                if (item.c2 != null && item.need_draw2)
                                {
                                    canvas.drawBitmap(item.c2.img, item.matrix_draw, paint_drawFilterBitmap);
                                }
                            }
                        }
                    }
                }

                if ( ! room_info.my_userinfo.cached_draw_my_cards)
                {
                    //R = Math.max(canvas_h, canvas_w) * 2.5f + ( orietration_screen == ORIENTATION_LANDSCAPE  ? 32 * _scale_pixel : 0 );
                    int my_userinfo_countDrawCards = room_info.my_userinfo.countDrawCards();

                    //boolean flag = (my_userinfo_countDrawCards % 2 == 0);
                    //limit_angle = 180.0f - 2 * (float) Math.toDegrees( Math.acos( (canvas_w - card2_w) / ( R * 2 ) ) );
                    step = limit_angle / my_userinfo_countDrawCards;

                    //min_step = (float) (180.0f - 90.0f - Math.toDegrees(Math.atan(R / card2_w)));

                    //x0 = canvas_w2;
                    //y0 = canvas_h + R + card2_h4 + ( orietration_screen == ORIENTATION_LANDSCAPE  ? 10 * _scale_pixel : 0 );

                    if (my_userinfo_countDrawCards <= 4 || step > min_step - min_step / 5)
                    {
                        step = Math.min(step, min_step - min_step / 5);
                    }

                    /// четное кол-во
                    if (my_userinfo_countDrawCards % 2 == 0)
                    {
                        start = 180 + step / 2 + (my_userinfo_countDrawCards - 1) / 2 * step;
                    }
                    else
                    {
                        start = 180 + (my_userinfo_countDrawCards - 1) / 2 * step;
                    }

                    //boolean skip;
                    my_userinfo_index = 0;
                    my_userinfo_cards_size = room_info.my_userinfo.cards_user.size();

                    for (float a = start; my_userinfo_index < my_userinfo_cards_size; a -= step, my_userinfo_index++)
                    {
                        /*if ( ! room_info.my_userinfo.cards_points.get(index).need_draw_card)
                        {
                            a += step;

                            continue;
                        }*/

                        /*skip = false;

                        for (int k = 0; k < my_animated_card.size(); k++)
                        {
                            if (my_animated_card.get(k) != null
                                    && my_animated_card.get(k).card == room_info.my_userinfo.cards_user.get(index).card
                                //&& animated_card.get(k).need_delete == false
                                    )
                            {
                                skip = true;
                                break;
                            }
                        }

                        //пропуск карты что сейчас анимируется
                        if (skip)
                        {
                            continue;
                        }*/

                        radius = (R_MAIN + (my_userinfo_cards_size - my_userinfo_index) * 2f * _scale_pixel);

                        if (room_info.my_userinfo.cards_user.get(my_userinfo_index).matrix_draw == null)
                        {
                            x = x0 + radius * Math.sin(Math.toRadians(a));
                            y = y0 + radius * Math.cos(Math.toRadians(a));

                            // вектор приямой по ходу движения
                            my_userinfo_x0 = x - x0;
                            my_userinfo_y0 = y - y0;

                            // нормаль к прямой
                            my_userinfo_x1 = -1 * (y - y0);
                            my_userinfo_y1 = x - x0;

                            // нормаль к прямой обратная
                            my_userinfo_x1_2 = -1 * my_userinfo_x1;
                            my_userinfo_y1_2 = -1 * my_userinfo_y1;

                            // нормализация векторов
                            my_userinfo_l0 = Math.sqrt(my_userinfo_x0 * my_userinfo_x0 + my_userinfo_y0 * my_userinfo_y0);
                            //my_userinfo_l0 = Utils.Q_rsqrt(my_userinfo_x0 * my_userinfo_x0 + my_userinfo_y0 * my_userinfo_y0);
                            my_userinfo_x0 /= my_userinfo_l0;
                            my_userinfo_y0 /= my_userinfo_l0;

                            my_userinfo_l1 = Math.sqrt(my_userinfo_x1 * my_userinfo_x1 + my_userinfo_y1 * my_userinfo_y1);
                            //my_userinfo_l1 = Utils.Q_rsqrt(my_userinfo_x1 * my_userinfo_x1 + my_userinfo_y1 * my_userinfo_y1);
                            my_userinfo_x1 /= my_userinfo_l1;
                            my_userinfo_y1 /= my_userinfo_l1;

                            my_userinfo_l2 = Math.sqrt(my_userinfo_x1_2 * my_userinfo_x1_2 + my_userinfo_y1_2 * my_userinfo_y1_2);
                            //my_userinfo_l2 = Utils.Q_rsqrt(my_userinfo_x1_2 * my_userinfo_x1_2 + my_userinfo_y1_2 * my_userinfo_y1_2);
                            my_userinfo_x1_2 /= my_userinfo_l2;
                            my_userinfo_y1_2 /= my_userinfo_l2;

                            my_userinfo_end_x0 = x + infoOrientation.get(orietration_screen).card2_h * my_userinfo_x0;
                            my_userinfo_end_y0 = y + infoOrientation.get(orietration_screen).card2_h * my_userinfo_y0;

                            my_userinfo_px1 = (float) (my_userinfo_end_x0 + infoOrientation.get(orietration_screen).card2_w2 * my_userinfo_x1);
                            my_userinfo_py1 = (float) (my_userinfo_end_y0 + infoOrientation.get(orietration_screen).card2_w2 * my_userinfo_y1);

                            my_userinfo_px2 = (float) (my_userinfo_end_x0 + infoOrientation.get(orietration_screen).card2_w2 * my_userinfo_x1_2);
                            my_userinfo_py2 = (float) (my_userinfo_end_y0 + infoOrientation.get(orietration_screen).card2_w2 * my_userinfo_y1_2);

                            my_userinfo_px3 = (float) (x + infoOrientation.get(orietration_screen).card2_w2 * my_userinfo_x1_2);
                            my_userinfo_py3 = (float) (y + infoOrientation.get(orietration_screen).card2_w2 * my_userinfo_y1_2);

                            my_userinfo_px4 = (float) (x + infoOrientation.get(orietration_screen).card2_w2 * my_userinfo_x1);
                            my_userinfo_py4 = (float) (y + infoOrientation.get(orietration_screen).card2_w2 * my_userinfo_y1);

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).center_x = x;
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).center_y = y;

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).end_center_x = my_userinfo_end_x0;
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).end_center_y = my_userinfo_end_y0;

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).x1 = my_userinfo_px1;
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).y1 = my_userinfo_py1;

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).x2 = my_userinfo_px2;
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).y2 = my_userinfo_py2;

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).x3 = my_userinfo_px3;
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).y3 = my_userinfo_py3;

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).x4 = my_userinfo_px4;
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).y4 = my_userinfo_py4;

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).rotate_angle = 180 - a;
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).rotate_angle_x = infoOrientation.get(orietration_screen).card2_w / 2;
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).rotate_angle_y = infoOrientation.get(orietration_screen).card2_h;

                            room_info.my_userinfo.cards_user.get(my_userinfo_index).matrix_draw = new Matrix();
                            room_info.my_userinfo.cards_user.get(my_userinfo_index).matrix_draw.setRotate(180 - a, infoOrientation.get(orietration_screen).card2_w / 2, infoOrientation.get(orietration_screen).card2_h);
                            room_info.my_userinfo.cards_user.get(my_userinfo_index).matrix_draw.postTranslate((float) x - infoOrientation.get(orietration_screen).card2_w / 2, (float) y - infoOrientation.get(orietration_screen).card2_h);


                            my_userinfo_B_alfa = Math.atan(infoOrientation.get(orietration_screen).card2_w2 / (radius + infoOrientation.get(orietration_screen).card2_h)) * 180.0f / Math.PI;

                            x = x0 + radius * Math.sin(Math.toRadians(a + my_userinfo_B_alfa));
                            y = y0 + radius * Math.cos(Math.toRadians(a + my_userinfo_B_alfa));

                            // вектор приямой по ходу движения
                            my_userinfo_x0 = x - x0;
                            my_userinfo_y0 = y - y0;

                            my_userinfo_l0 = Math.sqrt(my_userinfo_x0 * my_userinfo_x0 + my_userinfo_y0 * my_userinfo_y0);
                            //my_userinfo_l0 = Utils.Q_rsqrt(my_userinfo_x0 * my_userinfo_x0 + my_userinfo_y0 * my_userinfo_y0);
                            my_userinfo_x0 /= my_userinfo_l0;
                            my_userinfo_y0 /= my_userinfo_l0;

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).icon_new_card_in_board_x = (x + (infoOrientation.get(orietration_screen).card2_h + 12 * _scale_pixel) * my_userinfo_x0);
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).icon_new_card_in_board_y = (y + (infoOrientation.get(orietration_screen).card2_h + 12 * _scale_pixel) * my_userinfo_y0);

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).icon_new_card_in_board2_x = (x + (infoOrientation.get(orietration_screen).card2_h + 12 * _scale_pixel) * my_userinfo_x0);
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).icon_new_card_in_board2_y = (y + (infoOrientation.get(orietration_screen).card2_h + 12 * _scale_pixel) * my_userinfo_y0);

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).matrix_icon_new_tossed_card = new Matrix();
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).matrix_icon_new_tossed_card.setRotate(180 - a, icon_new_tossed_card.getWidth() / 2, icon_new_tossed_card.getHeight());
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).matrix_icon_new_tossed_card.postTranslate(
                                    (float) (x + (infoOrientation.get(orietration_screen).card2_h + 30) * my_userinfo_x0)
                                    , (float) (y + (infoOrientation.get(orietration_screen).card2_h + 30) * my_userinfo_y0));

                            room_info.my_userinfo.cards_points.get(my_userinfo_index).matrix_icon_new_tossed_card2 = new Matrix();
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).matrix_icon_new_tossed_card2.setRotate(180 - a, icon_new_tossed_card.getWidth() / 2, icon_new_tossed_card.getHeight());
                            room_info.my_userinfo.cards_points.get(my_userinfo_index).matrix_icon_new_tossed_card2.postTranslate(
                                    (float) (x + (infoOrientation.get(orietration_screen).card2_h + 30) * my_userinfo_x0 + (32) * my_userinfo_x0)
                                    , (float) (y + (infoOrientation.get(orietration_screen).card2_h + 30) * my_userinfo_y0 + (32) * my_userinfo_y0));
                        }


                    } // --for

                    room_info.my_userinfo.cached_draw_my_cards = true;
                }

                boolean skip;
                int /*k, */ my_animated_animated_card_size = my_animated_card.size();

                for (__i = 0, __len = room_info.my_userinfo.cards_user.size(); __i < __len; __i++)
                {
                    CardInfo item                   = room_info.my_userinfo.cards_user.get(__i);
                    CardDraw_RectPoint item_points  = room_info.my_userinfo.cards_points.get(__i);

                    skip = false;

                    for (__k = 0; __k < my_animated_animated_card_size; __k++)
                    {
                        if (my_animated_card.get(__k) != null && my_animated_card.get(__k).card == item.card
                            //&& animated_card.get(k).need_delete == false
                                )
                        {
                            skip = true;
                            break;
                        }
                    }

                    //пропуск карты что сейчас анимируется и еще
                    if (       skip
                            || ( ! item_points.need_draw_card || item.matrix_draw == null )
                      )
                    {
                        continue;
                    }

                    /*if (_len > 8)
                    {
                        canvas.drawBitmap(
                                list_cards_user.get(item.card).img
                                , item.matrix_draw
                                , paint_drawBitmap2
                        );

                        if (show_hints_cards && item.draw_icon_tossed_card)
                        {
                            canvas.drawBitmap(
                                    list_cards_user.get(Cards.SELECT_MY_CARDS).img
                                    , item.matrix_draw
                                    , paint_drawBitmap2
                            );
                        }
                    }
                    else
                    {*/
                        // понижаем качетсво отрисовки если карт больше
                        //if (__len > 10)
                        //{
                            canvas.drawBitmap(
                                    list_cards_user.get(item.card).img
                                    , item.matrix_draw
                                    , paint_drawBitmap
                            );
                        //}
                        /*else
                        {
                            canvas.drawBitmap(
                                    list_cards_user.get(item.card).img
                                    , item.matrix_draw
                                    , paint_drawFilterBitmap
                            );
                        }*/

                        // подсказка
                        /*if ( show_hints_cards && item.draw_icon_tossed_card )
                        {
                            canvas.drawBitmap(
                                    list_cards_user.get(Cards.SELECT_MY_CARDS).img
                                    , item.matrix_draw
                                    , paint_drawBitmap
                            );
                        }*/
                    //}

                    if (show_hints_cards)
                    {
                        if (item.draw_icon_new_card)
                        {
                            if (item.timestamp_add == 0)
                            {
                                item.timestamp_add = System.currentTimeMillis();
                            }
                            else if (System.currentTimeMillis() - item.timestamp_add > RoomSurfaceView.TIME_DRAW_NEW_CARD_ICON_IN_MS)
                            {
                                item.draw_icon_new_card = false;
                            }

                            canvas.drawBitmap(
                                       icon_new_card_in_board
                                    , (float) item_points.icon_new_card_in_board_x
                                    , (float) item_points.icon_new_card_in_board_y
                                    , null);
                        }
                        else if (item.draw_icon_new_card2)
                        {
                            if (item.timestamp_add == 0)
                            {
                                item.timestamp_add = System.currentTimeMillis();
                            }
                            else if (System.currentTimeMillis() - item.timestamp_add > RoomSurfaceView.TIME_DRAW_NEW_CARD_ICON_IN_MS)
                            {
                                item.draw_icon_new_card2 = false;
                            }

                            canvas.drawBitmap(
                                    icon_new_card_in_board2
                                    , (float) item_points.icon_new_card_in_board2_x
                                    , (float) item_points.icon_new_card_in_board2_y
                                    , null);
                        }

                        if (item.draw_icon_tossed_card)
                        {
                            canvas.drawBitmap(
                                    list_cards_user.get(Cards.SELECT_MY_CARDS).img
                                    , item.matrix_draw
                                    , paint_drawBitmap
                            );

                            if (item.timestamp_add2 == 0)
                            {
                                item.timestamp_add2 = System.currentTimeMillis();
                            }
                            else if (System.currentTimeMillis() - item.timestamp_add2 > RoomSurfaceView.TIME_DRAW_HINT_TOSS_ICON_IN_MS)
                            {
                                item.draw_icon_tossed_card = false;
                            }

                            if (item.draw_icon_new_card || item.draw_icon_new_card2)
                            {
                                canvas.drawBitmap(
                                        icon_new_tossed_card
                                        , item_points.matrix_icon_new_tossed_card2
                                        , null);
                            }
                            else
                            {
                                canvas.drawBitmap(
                                        icon_new_tossed_card
                                        , item_points.matrix_icon_new_tossed_card
                                        , null);
                            }

                        }
                    }
                }

                if( my_animated_animated_card_size > 0 )
                {
                    for (__i = 0, __len = my_animated_animated_card_size; __i < __len; __i++)
                    {
                        AnimatedCard item = my_animated_card.get(__i);

                        if (item != null)
                        {
                            if (item.need_delete)
                            {
                                if (current_my_animated_card != null && current_my_animated_card.card == item.card)
                                {
                                    if (current_my_animated_card.play_sound_thrown)
                                    {
                                        if (_ma.getAppSettings().opt_sound_in_game && cards_thrown != null && !_ma.call_phone)
                                        {
                                            cards_thrown.start();
                                        }

                                        if (room_info.allCardsIsBeated())
                                        {
                                            highLightResetAll();
                                        }
                                    }

                                    //Log.i("TAG", "DRAW: current_my_animated_card = null" );
                                    current_my_animated_card = null;
                                }

                                item.draw(canvas);

                                my_animated_card.set(__i, null);
                                continue;
                            }

                            if (item.up)
                            {
                                item.stepUp();
                            }
                            else
                            {
                                item.stepDown();
                            }

                            item.draw(canvas);
                        }

                    } // --for

                    my_animated_card.removeAll(Collections.singleton(null));

                    if (my_animated_card.size() == 0)
                    {
                        animation_my_animated_card = false;
                        hideTransferCard();

                        synchronized (room_info.lock_cards_boards)
                        {
                            for (__i = 0, __len = room_info.cards_boards.size(); __i < __len; __i++)
                            {
                                ItemBoardCard item = room_info.cards_boards.get(__i);

                                if (item.c1 != null)
                                {
                                    item.need_draw1 = true;
                                }

                                if (item.c2 != null)
                                {
                                    item.need_draw2 = true;
                                }
                            }

                            room_info.cache_draw_cards_board = false;
                        }
                    }

                }

            } // --synchronized
        }

        if ( ! stat_game)
        {
            if (System.currentTimeMillis() - time_last_step > 200)
            {
                time_last_step = System.currentTimeMillis();

                tmp_angle_timer += 5;

                if (tmp_angle_timer >= 360)
                {
                    tmp_angle_timer = 0;
                }
            }
        }

        //_ma.my_avatar_icon.setAngle( tmp_angle_timer );
        //_ma.my_avatar_icon_redraw();


        //canvas.drawLine(0, canvas_h2, canvas_w, canvas_h2, paint_drawBitmap);
        //canvas.drawLine(canvas_w2, 0, canvas_w2, canvas_h, paint_drawBitmap);

        if (loadingWait != null)
        {
            loadingWait.step(canvas, canvas_w2, canvas_h2);
        }

        if(
                flag_card_fly_user_xod
             || animation_fly_desc_card
             || (animation_fly_card_off_board && fly_card_off_board != null)
             || flag_adraw_animated_texts
             || flag_animation_fly_img
             || flag_list_animation_text_coints
             || flag_list_animation_number
             || flag_list_animation_image_smile
        )
        {
            synchronized (lockWorkThreadAnimation)
            {
                drawAnimationProcess(canvas);
            }
        }

        if( show_wondow_user_info )
        {
            canvas.drawBitmap(fade_bk, 0, 0, null);
            canvas.drawBitmap(window_user_info, pos_x_wondow_user_info, pos_y_wondow_user_info, null );

            ui_buttons.get("btn_close_window_user_info").draw(canvas);

            if( ui_buttons.containsKey("btn_test") )
            {
                ui_buttons.get("btn_test").draw(canvas);
            }
        }

        //ui_buttons.get("btn_test").draw(canvas);

        /*PointPP t = getPosNextDrawCardsInBoard(true);

        canvas.drawCircle(
                  t.getx()
                , t.gety()
                , 2 * _scale_pixel
                , paint_drawBitmap
        );*/

        /*float[] pp1 = getCardTrasferFollPos();

        if( pp1 != null  && firstDrawCardInBoard != null)
        {
            canvas.drawBitmap(
                    list_cards_draw.get(Cards.SELECT_IN_BOARD).img
                    , firstDrawCardInBoard.getx() + pp1[0]
                    , firstDrawCardInBoard.gety() + pp1[1]
                    , paint_drawBitmap
            );
        }*/


        /*canvas.drawRect(drawRectCardBoard, pain_userSelected);
        canvas.drawRect(
                   canvas_w2 - canvas_w2 / 1.2f
                , canvas_h2 - canvas_h2 / 4
                , canvas_w2 + canvas_w2 / 1.2f
                , canvas_h2 + canvas_h2 / 4
                , paint_strokeDashed
        );*/
    }

    private void drawAnimationProcess(Canvas canvas)
    {
        //полет карты от игрока (не сам)
        if( flag_card_fly_user_xod )
        {
            synchronized (card_fly_user_xod_lock)
            {
                for (int __i = 0, __len = card_fly_user_xod.size(); __i < __len; __i++)
                {
                    AnimatedCardFly item = card_fly_user_xod.get(__i);

                    if (item != null)
                    {
                        if ( ! item.stop_up)
                        {
                            item.stepUp();

                            if( canvas != null )
                            { item.draw(canvas); }
                        }
                        else if ( ! item.stop_down)
                        {
                            item.stepDown();

                            if( canvas != null )
                            { item.draw(canvas); }
                        }
                        else
                        {
                            if( canvas != null )
                            { item.draw(canvas); }

                            boolean _flag = false;

                            if ( ! animation_fly_card_new_pos)
                            {
                                synchronized (room_info.lock_cards_boards)
                                {
                                    for (int h = 0, _l = room_info.cards_boards.size(); h < _l; h++)
                                    {
                                        ItemBoardCard item2 = room_info.cards_boards.get(h);

                                        if (
                                                (
                                                        item.card_hovered != 0
                                                                && item2.c1.card_info.card == item.card_hovered
                                                                && item2.c2 == null
                                                )
                                                        || (item.card_hovered == 0 && item2.c2 == null)
                                                )
                                        {
                                            _flag = true;
                                            item2.c2 = infoOrientation.get(orietration_screen).list_cards_draw.get(item.getCard());
                                            item2.need_draw2 = true;
                                            room_info.cache_draw_cards_board = false;
                                            break;
                                        }
                                    }
                                }
                            }

                            if (!_flag)
                            {
                                ItemBoardCard item_add = new ItemBoardCard();
                                item_add.c1 = infoOrientation.get(orietration_screen).list_cards_draw.get(item.getCard());
                                item_add.c2 = null;

                                item_add.need_draw1 = true;

                                synchronized (room_info.lock_cards_boards)
                                {
                                    room_info.cards_boards.add(item_add);
                                    room_info.cache_draw_cards_board = false;
                                }

                                //need_move_cards_on_board = true;
                            }

                            card_fly_user_xod.set(__i, null);
                            //animation_fly_card_other_user = false;

                            if (_ma.getAppSettings().opt_sound_in_game && cards_thrown != null && ! _ma.call_phone)
                            {
                                cards_thrown.start();
                            }
                        }
                    }
                }

                card_fly_user_xod.removeAll(Collections.singleton(null));

                if (card_fly_user_xod.size() == 0)
                {
                    flag_card_fly_user_xod = false;
                    callbackEndAnimationFlyCardsFromUserToBoard();
                }
            }
        }

        if (animation_fly_desc_card)
        {
            synchronized (lock_my_animated_card_desk)
            {
                for(int __k = 0, __len = animated_card_desk.size(); __k < __len; __k++)
                {
                    AnimatedFlyDeck item = animated_card_desk.get(__k);

                    if (item != null && System.currentTimeMillis() > item.timestampStart)
                    {
                        if ( ! item.step() )
                        {
                            if( canvas != null )
                            { item.draw(canvas); }
                        }
                        else
                        {
                            //item.draw(canvas);
                            animated_card_desk.set(__k, null);
                        }
                    }
                }

                animated_card_desk.removeAll(Collections.singleton(null));

                if (animated_card_desk.size() == 0)
                {
                    animation_fly_desc_card = false;

                    synchronized (room_info.lock_draw)
                    {
                        for (int __i = 0, __len = room_info.my_userinfo.cards_points.size(); __i < __len; __i++)
                        {
                            if ( ! room_info.my_userinfo.cards_points.get(__i).need_draw_card)
                            {
                                room_info.my_userinfo.resetMatrixDrawCardsUser();
                                room_info.my_userinfo.cards_points.get(__i).need_draw_card = true;
                            }
                        }
                    }

                    callbackEndAnimationFryFromDesckToUsers();
                }
            }
        }

        //canvas.drawText(String.valueOf(room_info.current_my_position), 16, 150, pain_drawTextCountDesc);

        if (animation_fly_card_off_board && fly_card_off_board != null)
        {
            if ( ! fly_card_off_board.run_step() )
            {
                animation_fly_card_off_board = false;
                fly_card_off_board = null;

                //room_info.cache_draw_cards_board = false;

                synchronized (room_info.lock_cards_boards)
                {
                    room_info.cards_boards.clear();
                }

                callbackEndAnimationFlyCardsOffBoard();
            }
        }

        //counter_draw += 1;

        if( flag_adraw_animated_texts )
        {
            synchronized (lock_adraw_animated_texts)
            {
                for (int __k = 0, __len = adraw_animated_texts.size(); __k < __len; __k++)
                {
                    AminatedTextRoundedRectangle item = adraw_animated_texts.get(__k);

                    if (item != null)
                    {
                        if(canvas != null)
                        { item.draw(canvas); }

                        if (item.step())
                        {
                            adraw_animated_texts.set(__k, null);
                            break;
                        }
                    }
                }

                adraw_animated_texts.removeAll(Collections.singleton(null));

                if (adraw_animated_texts.size() == 0)
                {
                    flag_adraw_animated_texts = false;
                }
            }
        }

        if( flag_animation_fly_img )
        {
            synchronized (lock_animation_fly_img)
            {
                //if (list_animation_fly_img.size() > 0)
                //{
                    for (int __i = 0, __len = list_animation_fly_img.size(); __i < __len; __i++)
                    {
                        AnimationImgToUsers item = list_animation_fly_img.get(__i);

                        if (item == null)
                        {
                            continue;
                        }

                        if ( item.step() )
                        {
                            if( canvas != null )
                            {
                                item.draw(canvas);
                            }
                        }
                        else
                        {
                            list_animation_fly_img.set(__i, null);
                        }
                    }

                    list_animation_fly_img.removeAll(Collections.singleton(null));

                    if (list_animation_fly_img.size() == 0)
                    {
                        flag_animation_fly_img = false;
                    }
                //}
            }
        }

        if( flag_list_animation_text_coints )
        {
            synchronized (lock_list_animation_text_coints)
            {
                //if (list_animation_text_coints.size() > 0)
                //{
                    for (int __i = 0, __len = list_animation_text_coints.size(); __i < __len; __i++)
                    {
                        AnimationTextAddCoints item = list_animation_text_coints.get(__i);

                        if (item == null)
                        {
                            continue;
                        }
                        else if (item != null && System.currentTimeMillis() > item.timestampStart)
                        {
                            if ( ! item.step())
                            {
                                if( canvas != null )
                                {
                                    item.draw(canvas);
                                }
                            }
                            else
                            {
                                list_animation_text_coints.set(__i, null);
                            }
                        }
                    }

                    list_animation_text_coints.removeAll(Collections.singleton(null));

                    if (list_animation_text_coints.size() == 0)
                    {
                        flag_list_animation_text_coints = false;
                    }
                //}
            }
        }

        if(flag_list_animation_number)
        {
            synchronized (lock_list_animation_number)
            {
                //if (list_animation_number.size() > 0)
                //{
                    for (int __i = 0, __len = list_animation_number.size(); __i < __len; __i++)
                    {
                        AnimationNumber item = list_animation_number.get(__i);

                        if (item == null)
                        {
                            continue;
                        }
                        else if (item != null && System.currentTimeMillis() > item.timestampStart)
                        {
                            if ( ! item.step() )
                            {
                                if( canvas != null )
                                {
                                    item.draw(canvas);
                                }
                            }
                            else
                            {
                                list_animation_number.set(__i, null);
                            }
                        }
                    }

                    list_animation_number.removeAll(Collections.singleton(null));

                    if (list_animation_number.size() == 0)
                    {
                        flag_list_animation_number = false;
                    }
                //}
            }
        }

        if(flag_list_animation_image_smile)
        {
            synchronized (lock_list_animation_image_smile)
            {
                //if (list_animation_image_smile.size() > 0)
                //{
                    for (int __i = 0, __len = list_animation_image_smile.size(); __i < __len; __i++)
                    {
                        AminatedImageSmile item = list_animation_image_smile.get(__i);

                        if (item == null)
                        {
                            continue;
                        }
                        else
                        {
                            if ( ! item.step() )
                            {
                                if( canvas != null )
                                {
                                    item.draw(canvas);
                                }
                            }
                            else
                            {
                                list_animation_image_smile.set(__i, null);
                            }
                        }
                    }

                    list_animation_image_smile.removeAll(Collections.singleton(null));

                    if (list_animation_image_smile.size() == 0)
                    {
                        flag_list_animation_image_smile = false;
                    }
                //}
            }
        }
    }

    private class ThreadInnerAnimationProcess extends Thread
    {
        public boolean pause = true;
        public boolean run = true;

        @Override
        public void run()
        {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 1);

            while (run)
            {
                if( pause )
                {
                    try
                    {
                        Thread.sleep(300);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                synchronized (lockWorkThreadAnimation)
                {
                    drawAnimationProcess(null);
                }
            }
        }
    }



    public boolean existNextUserTransferXod()
    {
        int current_pos = room_info.current_my_position; // текущий это тот кто отбиваеться

        while (true)
        {
            current_pos = room_info.getNextPos(current_pos);
            RoomInfo_User found_user_next = getUserFromOffsetPosition(current_pos);

            current_pos = found_user_next.offset_position_num;

            if (found_user_next.uid == room_info.my_userinfo.uid)
            {
                break;
            }

            //добавим карту новую которую кладут для передачи хода
            // если кол-во карт у пользователя достаточно чтобы отбить ход, то передаем ход ему
            if (found_user_next.tmp_count_cards >= room_info.cards_boards.size() + 1)
            {
                return true;
            }
        }

        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////
    /// CALLBACKS ///

    public void toss_xod(int card, int place_the_cards_in_my_hand)
    {
    }

    public void toss_xod_transfer_foll(int card, int place_the_cards_in_my_hand)
    {
    }

    public void beat_xod(int card, int place_the_cards_in_my_hand, int card_hovered)
    {
    }

    public void callbackEndAnimationFryFromDesckToUsers()
    {
    }

    public void callbackEndAnimationFlyCardsOffBoard()
    {
    }

    public void callbackEndAnimationFlyCardsFromUserToBoard()
    {
    }

    public void callbackClickUserInRoom(int uid)
    {
    }

    public void callbackInit()
    {
    }

    public void callbackDrawBkImg(Bitmap img)
    {
    }

    public void callbackAddToFriend(int uid) {}

    public void resetAll()
    {
        //Log.i("TAG", "ResetAll");

        resetAllUsersTimers();

        draw_ready_status_users = true;
        draw_off_game_cards = false;
        draw_trump_card_in_the_deck = false;

        draw_my_cards = false;


        room_info.count_deck_of_cards = 0;

        room_info.resetAll();

        /*atimer1 = new AnimationTimer1(getContext()
                , R.drawable.wait_timer1
                , R.drawable.wait_timer1_arrow);*/

        initAllCards();
    }

    public void onResume()
    {
        th_in.pause = true;
    }

    public void onPause()
    {
        th_in.pause = false;
    }
}
