package durakonline.sk.durakonline;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import durakonline.sk.durakonline.fragments.SearchRooms;
import durakonline.sk.durakonline.other.AccountInfo;
import durakonline.sk.durakonline.other.AnimationFlyCardsOutOfBoard;
import durakonline.sk.durakonline.other.AppSettings;
import durakonline.sk.durakonline.other.FlyMoneyInfo;
import durakonline.sk.durakonline.other.FriendItem;
import durakonline.sk.durakonline.other.Log;
import durakonline.sk.durakonline.other.RequestToFriendInfo;
import durakonline.sk.durakonline.other.RoomInfo;
import durakonline.sk.durakonline.other.RoomInfo_User;
import durakonline.sk.durakonline.other.SearchItem;
import durakonline.sk.durakonline.other.SessionInfo;
import durakonline.sk.durakonline.other.Shop;
import durakonline.sk.durakonline.other.Utils;


public class MainActivity extends AppCompatActivity
{
    public static final int screen_width;
    public static final int screen_height;
    public static final int REQUEST_PERMISSION_2 = 5;
    //public static final String LANG_RU = "RU";
    //public static final String LANG_EN = "EN";
    private static final String TAG = "TAG";
    private static final int RC_SIGN_IN = 9001;
    static boolean active = false;
    private static String VERSION_APP = "";

    static
    {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
    }

    public final int SV_PAGE_MENU = 0x01;
    public final int SV_PAGE_ROOM = 0x02;
    public final int SV_PAGE_SETTINGS = 0x03;
    public final int SV_PAGE_CONNECTION_SERVER = 0x04;
    public final int SV_PAGE_RULES = 0x05;
    public final int SV_PAGE_CREATE_NEW_GAME = 0x06;
    public final int SV_PAGE_PROFILE = 0x07;
    public final int SV_PAGE_SEARCH = 0x08;
    public final int SV_PAGE_FRIENDS = 0x09;
    public final int SV_PAGE_AUTH = 0x10;
    public final int SV_PAGE_PRIVACY_POLICY = 0x11;
    public final int SV_PAGE_MAIN2 = 0x12;
    public final int SV_PAGE_ADD_BALANS = 0x13;
    public final int SV_PAGE_TABLE_RATING = 0x14;
    public final int SV_PAGE_RULET = 0x15;
    public final int SV_PAGE_SEARCH_USERS = 0x16;
    public final int SV_PAGE_FIRST_PAGE = 0x17;
    public final int SV_PAGE_SHOP = 0x18;

    public final int TYPE_BTN_BITO = 0x01;
    public final int TYPE_BTN_BERY = 0x02;
    public final int TYPE_BTN_THATS_ALL = 0x03;
    public final int TYPE_BTN_BITO_2 = 0x04;
    public final int TYPE_BTN_BERY_2 = 0x05;
    public final int TYPE_BTN_THATS_ALL_2 = 0x06;
    public int sv_page = 0;
    public Button btn_ready_play_game;
    public double screenInches;
    public MediaPlayer click_sound, deal_gin, fly_card_to_users, fly_card_to_users_2, sound_game_over, sound_coins, cachbox, myxod;
    String app_name = "";
    SessionInfo session_info = new SessionInfo();

    /*public final int ACTION_END_ANIMATION_FLY_NONE              = 0x00;
    public final int ACTION_END_ANIMATION_FLY_SHOW_BTN_BITO       = 0x01;
    public final int ACTION_END_ANIMATION_FLY_SHOW_BTN_BERY       = 0x02;
    public final int ACTION_END_ANIMATION_FLY_SHOW_BTN_THAT_ALL   = 0x03;*/
    private RoomSurfaceView _draw_task = null;
    private RuletSurfaceView _draw_rulet = null;
    private HashMap<Integer, UiItemFriends> map_ui_friends = new HashMap<>();

    //private int action_end_animation_fly = ACTION_END_ANIMATION_FLY_NONE;
    HashMap<Integer, UiItemFriends> map_ui_search_friends = new HashMap<>();
    List<UiItemRating> list_rating = new ArrayList<>();
    List<CheckBox> list_checkbox = new ArrayList<>();
    TimerXod timer_xod = null;
    TimerReady timer_ready = null;
    private String HOST = "";
    private int TCP_PORT_SERVER_1 = 0;
    private int TCP_PORT_SERVER_2 = 0;
    private GoogleApiClient mGoogleApiClient;
    private Boolean _is_first_run = true;
    private Typeface _fontApp;
    private Typeface _fontApp_bold;
    private Typeface _fontApp_bold2;
    private Typeface _fontApp_bold3;
    private int WAIT_SECOND = 10;
    private boolean auto_connection_room = false;
    private float _scale_px;
    private int create_new_game_count_users;
    private int room_page_oppened_user_info = 0;
    private AccountInfo accout_info = new AccountInfo();
    private ClassNetWork network = null;
    //public MyAvatarIcon my_avatar_icon = null;
    private String idToken = "";
    private Bitmap cache_personal_photo = null;
    private int create_game_current_type = 1;
    private AppSettings app_setting;
    private Object lock_drawTask = new Object();
    private ProgressBar pb_wait_readyPlay = null;
    private long time_press_exit_app = 0;
    private List<RequestToFriendInfo> page_room_request_to_friends = null;
    private List<FriendItem> friend_list = new ArrayList<>();
    private List<SearchItem> search_friends_list = new ArrayList<>();
    private boolean showed_salute = false;
    private boolean need_show_alert_update_app = false;

    private int uid_page_friends_selected_send_money = 0;
    private AlertDialog alert_dialog_out_of_room = null;

    private RewardedVideoAd mRewardedVideoAd;
    private boolean onRewardedVideoAd = false;
    private FireworksSurfaceView fireworks_sv = null;
    private Object lock_fireworks_sv = new Object();
    private boolean pressed_ready_game = false;
    private int last_count_online = 0;

    private View current_buy_goods_view;
    public boolean call_phone;
    private DB db = null;
    private String token;
    //private boolean inPause = false;

    public static Bitmap resizeAvatarImg(int w, int h, Bitmap bmp)
    {
        if (bmp == null)
        {
            return null;
        }

        return Bitmap.createScaledBitmap(
                bmp
                , w
                , h
                , true);
    }

    /**
     * Запрос разрешений. ГАЛЕРЕЯ
     */
    /*public void requestPermission_2()
    {
        if (
                (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle( getString(R.string.txt_108) )
                    .setMessage( getString(R.string.txt_109) )
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(appSettingsIntent, 1024);
                        }
                    });
            builder.create().show();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.txt_110))
                    .setMessage( getString(R.string.txt_111) )
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                    {
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE
                                    }, REQUEST_PERMISSION_2);
                        }
                    });
            builder.create().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode
            , @NonNull String[] permissions
            , @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_PERMISSION_2 && grantResults.length == 1)
        {
            if(
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    )
            {
                init_app();
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        // In some cases modifying newConfig leads to unexpected behavior,
        // so it's better to edit new instance.
        Configuration configuration = new Configuration(newConfig);
        Utils.adjustFontScale(getApplicationContext(), configuration);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        db = new DB(this);
        db._db_cteate_tables();


        _fontApp_bold = Typeface.createFromAsset(getAssets(), "fonts/Monitorca-Bd.ttf");

        show_page_first_page();

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        //getConfiguration().orientation

        try
        {
            this.VERSION_APP = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        /*String languageToLoad  = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());*/

        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);*/

        HOST = JniApi.f1();

        //HOST = "10.42.1.1";

        TCP_PORT_SERVER_1 = JniApi.port1();
        TCP_PORT_SERVER_2 = JniApi.port2();

        //MobileAds.initialize(this, "ca-app-pub-9352011444644710~4773408865");
        //UnityAds.initialize(this, "2950505", this);

        /*Vungle.init("5c080453d7989c0019cb2d26", getApplicationContext(), new InitCallback() {
            @Override
            public void onSuccess() {
                // Initialization has succeeded and SDK is ready to load an ad or play one if there
                // is one pre-cached already

                Toast.makeText(MainActivity.this, "Initialization has succeeded and SDK",Toast.LENGTH_SHORT).show();

                if (Vungle.isInitialized())
                {
                    Vungle.loadAd("DEFAULT-3981593", new LoadAdCallback()
                    {
                        @Override
                        public void onAdLoad(String placementReferenceId) { }

                        @Override
                        public void onError(String placementReferenceId, Throwable throwable) {
                            // Load ad error occurred - throwable.getLocalizedMessage() contains error message
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable throwable) {
                // Initialization error occurred - throwable.getLocalizedMessage() contains error message
            }

            @Override
            public void onAutoCacheAdAvailable(String placementId) {
                // Callback to notify when an ad becomes available for the auto-cached placement
                // NOTE: This callback works only for the auto-cached placement. Otherwise, please use
                // LoadAdCallback with loadAd API for loading placements.
            }
        });*/

        this._scale_px = this.getResources().getDisplayMetrics().density;


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);
        screenInches = (double) Math.round(screenInches * 10) / 10;
        //Log.d("TAG10", "Screen inches : " + screenInches);


        //Log.i("TAG10", "densityDpi: " + String.valueOf(this.getResources().getDisplayMetrics().densityDpi));
        //Log.i("TAG10", "dipToPixels 10: " + String.valueOf(Utils.dipToPixels(this, 10)));
        //Log.i("TAG10", "pixelsToSp 10: " + String.valueOf(Utils.pixelsToSp(this, 10f)));

        app_name = Utils.getApplicationName(MainActivity.this);

        app_setting = new AppSettings();



        //this._is_first_run = app_setting.db.getKeyValue("is_first_run", "1").equals("1") ? true : false;

        //_fontApp              = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        //_fontApp_bold         = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

        _fontApp = Typeface.createFromAsset(getAssets(), "fonts/Russo_One/RussoOne-Regular.ttf");
        _fontApp_bold2 = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        _fontApp_bold3 = Typeface.createFromAsset(getAssets(), "fonts/fira-mono.bold.ttf");

        //_fontApp = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        //_fontApp_bold = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(10955);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        //setContentView(R.layout.activity_main);

        //show_page_add_money();
        //show_page_menu();
        //close_app();
        //show_page_room();


        // Views
        //mStatusTextView = (TextView) findViewById(R.id.status);

        // Button listeners
        /*findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);*/

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("457473160264-9uq5akg8o89cbuu8n33i88qnco0ui04r.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener()
                {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
                    {
                        Log.d(TAG, "onConnectionFailed:" + connectionResult);
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_clientt

        /*if( ! Utils.checkPermission_2(getApplicationContext()) )
        {
            requestPermission_2();
        }
        else
        {
            init_app();
        }*/

        init_app();
    }

    public Typeface get_fontApp()
    {
        return _fontApp;
    }

    public Typeface get_fontAppBold()
    {
        return _fontApp_bold;
    }

    /*public Typeface get_fontAppBold3()
    {
        return _fontApp_bold3;
    }*/

    public AccountInfo getAccoutInfo()
    {
        return accout_info;
    }

    public SessionInfo getSessionInfo()
    {
        return session_info;
    }

    public float get_scale_px()
    {
        return this._scale_px;
    }

    private void authGoogle()
    {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone())
        {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

            show_page_connection_server();
        }
        else
        {
            //show_page_auth();

            // Если пользователь ранее не входил в систему на этом устройстве или срок действия входа истек,
            // эта асинхронная ветвь попытается выполнить вход в систему пользователя в автоматическом режиме.  Кросс-устройство
            // единый вход будет происходить в этой ветке.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>()
            {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult)
                {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if( db.getKeyValue(getString(R.string.cmd_1), "0").equals("0") )
        {
            if (network == null)
            {
                //signOut();
                authGoogle();
            }
        }
        else
        {
            if (network == null)
            {
                show_page_connection_server();
                connection_server();
            }
        }
    }

    @Override
    protected void onResume()
    {
        mRewardedVideoAd.resume(this);
        super.onResume();

        //inPause = false;
        //startAppAd.onResume();
        if (_draw_task != null)
        {
            _draw_task.onResume();
        }
    }

    private void init_telephonyManager()
    {
        if( ! Utils.checkPermission_2(this) )
        {
            return;
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager != null)
        {
            this.registerReceiver(new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    if (intent != null && intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED))
                    {
                        String state = intent.hasExtra(TelephonyManager.EXTRA_STATE) ? intent.getStringExtra(TelephonyManager.EXTRA_STATE) : null;

                        if (state == null)
                        {
                            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                            Log.e("Phone", "Outgoing number : " + number);
                        }
                        else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))
                        {
                            // Incoming call: Pause music
                            Log.i("Phone", "Ringing 1010");
                            call_phone = true;
                        }
                        else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
                        {
                            Log.i("Phone", "Idle ");
                            call_phone = false;
                        }
                        else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
                        {
                            // A call is dialing, active or on hold
                            Log.i("Phone", "offhook");
                            call_phone = true;
                        }
                        else
                        {
                            Log.i("Phone", String.valueOf(state));
                        }
                    }
                    //super.onCallStateChanged(state, incomingNumber);
                }
            }, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));
        }
    }

    @Override
    public void onPause()
    {
        mRewardedVideoAd.pause(this);
        super.onPause();

        //inPause = true;
        //startAppAd.onPause();
        if (_draw_task != null)
        {
            _draw_task.onPause();
        }
    }

    @Override
    public void onDestroy()
    {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result)
    {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result != null && result.isSuccess())
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    FrameLayout fl = findViewById(R.id.fl_page_auth_wait);

                    if( fl != null )
                    {
                        fl.setVisibility(View.VISIBLE);
                    }
                }
            });

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            accout_info.personName = acct.getDisplayName();
            accout_info.personEmail = acct.getEmail();
            accout_info.personId = acct.getId();

            accout_info.fisrtName = acct.getGivenName();
            accout_info.lastName = acct.getFamilyName();

            idToken = acct.getIdToken();

            if (acct.getPhotoUrl() == null)
            {
                accout_info.personPhoto = "";//BitmapFactory.decodeResource( getResources(), R.drawable.default_avatar );
                //show_page_menu();
            }
            else
            {
                String url = acct.getPhotoUrl().toString();
                accout_info.personPhoto = url;
            }

            db.setKeyValue("accout_info.personId", accout_info.personId);
            db.setKeyValue("accout_info.personName", accout_info.personName);
            db.setKeyValue("accout_info.personEmail", accout_info.personEmail);
            db.setKeyValue("accout_info.fisrtName", accout_info.fisrtName);

            if( accout_info.lastName != null )
            {
                db.setKeyValue("accout_info.lastName", accout_info.lastName);
            }
            else
            {
                db.setKeyValue("accout_info.lastName", "");
            }

            db.setKeyValue("accout_info.personPhoto", accout_info.personPhoto);

            //app_setting.saveAccountInfo(accout_info);

            connection_server();
            //show_page_connection_server();
        }
        else
        {
            if (result.getStatus().getStatusCode() == CommonStatusCodes.NETWORK_ERROR)
            {
                if (sv_page == SV_PAGE_CONNECTION_SERVER)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            /*TextView tv = (TextView) findViewById(R.id.tv_page_connection_server_status);

                            if (tv != null)
                            {
                                tv.setText("Ошибка сети");
                                tv.setTextColor(Color.RED);
                                tv.setTextSize(25);

                                FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_connection_server_root);
                                fl.setBackgroundResource(R.drawable.style_bk_img_21);
                            }*/
                        }
                    });
                }
                else if (sv_page == SV_PAGE_AUTH)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            TextView tv = findViewById(R.id.tv_page_auth_status_txt);

                            tv.setTextColor(Color.RED);
                            tv.setText(getString(R.string.txt_92));
                        }
                    });
                }
            }
            else if (result.getStatus().getStatusCode() == 12501)
            {
                close_app();
            }
            else if (result.getStatus().getStatusCode() == CommonStatusCodes.SIGN_IN_REQUIRED)
            {
                //signIn();
                //show_page_auth();

                //show_page_auth();

                if (result.getSignInAccount() == null)
                {
                    show_page_auth();
                }
            }
            else
            {
                Log.d(TAG, "result.getStatus():" + result.getStatus());

                show_page_auth();
            }
            /*else
            {
                //authGoogle();
                signIn();
            }*/

            //CommonStatusCodes.DEVELOPER_ERROR
            // Signed out, show unauthenticated UI.
        }
    }

    // [START signIn]
    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        mGoogleApiClient.connect();
    }

    private void signInReset()
    {
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mGoogleApiClient.clearDefaultAccountAndReconnect();

        startActivityForResult(signInIntent, RC_SIGN_IN);

        mGoogleApiClient.connect();
    }


    // [START signOut]
    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>()
                {
                    @Override
                    public void onResult(Status status)
                    {
                        // [START_EXCLUDE]

                        // [END_EXCLUDE]

                        close_app();
                    }
                });
    }
    // [END onActivityResult]

    // [START revokeAccess]
    private void revokeAccess()
    {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>()
                {
                    @Override
                    public void onResult(Status status)
                    {
                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
    // [END handleSignInResult]

    private void init_app()
    {
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        token = db.getKeyValue("accout_info.token", "");

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }

        /*if (app_setting.db.getKeyValue("init_ok", "0").equals("1"))
        {
            accout_info = app_setting.loadAccountInfo();

            show_page_connection_server();
        }
        else
        {
            show_page_auth();
        }*/


        deal_gin = MediaPlayer.create(this, R.raw.deal_gin);
        deal_gin.setVolume(0.25f, 0.25f);

        fly_card_to_users = MediaPlayer.create(this, R.raw.cards_shuffle);
        fly_card_to_users.setVolume(0.25f, 0.25f);

        fly_card_to_users_2 = MediaPlayer.create(this, R.raw.cards_shuffle_2);
        fly_card_to_users_2.setVolume(0.25f, 0.25f);

        sound_game_over = MediaPlayer.create(this, R.raw.game_over);
        sound_game_over.setVolume(0.33f, 0.33f);

        sound_coins = MediaPlayer.create(this, R.raw.coins);
        sound_coins.setVolume(0.3f, 0.3f);

        cachbox = MediaPlayer.create(this, R.raw.cash_box);

        myxod = MediaPlayer.create(this, R.raw.myxod);
        myxod.setVolume(0.05f, 0.05f);

        init_telephonyManager();

        //signIn();

        /*if (_is_first_run)
        {
            app_setting.db.setKeyValue("is_first_run", "0");
        }*/

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener()
        {
            @Override
            public void onRewarded(RewardItem reward)
            {
                onRewardedVideoAd = true;

                ArrayList<String> params = new ArrayList<String>();

                params.add(String.valueOf(accout_info.uid));
                params.add(String.valueOf(session_info.session_socket_id));
                params.add(String.valueOf(10 + 100 + 90));

                network.queue_network.add(JniApi.k1()
                        , params
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__LONG);

                WaitThread wt = new WaitThread(800)
                {
                    @Override
                    public void callback()
                    {
                        getMyBalans();
                    }
                };
                wt.start();

                //Toast.makeText(MainActivity.this, "onRewarded! currency: " + reward.getType() + " amount: " +
                //        reward.getAmount(), Toast.LENGTH_SHORT).show();
                // Reward the user.

                mRewardedVideoAd.loadAd("ca-app-pub-9352011444644710/4746199141", new AdRequest.Builder().build());
                //mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                //Toast.makeText(MainActivity.this, "onRewardedVideoAdLeftApplication",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                //Toast.makeText(MainActivity.this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();

                if( onRewardedVideoAd )
                {
                    runAnimationAfterVideoAd();
                }
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                //Toast.makeText(MainActivity.this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                //Toast.makeText(MainActivity.this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                //Toast.makeText(MainActivity.this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                //Toast.makeText(MainActivity.this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {
                //Toast.makeText(MainActivity.this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
            }
        });

        WaitThread wt = new WaitThread(3000)
        {
            @Override
            public void callback()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mRewardedVideoAd.loadAd("ca-app-pub-9352011444644710/4746199141", new AdRequest.Builder().build());
                        //mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());

                        /*Ap360Banner airPush360Banner = new Ap360Banner(MainActivity.this, 0);
                        airPush360Banner.setEventsListener(new ApEventsListener() {
                            @Override
                            public void onLoaded(ApPreparedAd iAirPushPreparedAd) {
                                iAirPushPreparedAd.show();

                                Toast.makeText(MainActivity.this, "airPush360Banner onLoaded", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(String s) {
                                Toast.makeText(MainActivity.this, "airPush360Banner onFailed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onClicked() {
                                Toast.makeText(MainActivity.this, "airPush360Banner onClicked", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onOpened() {
                                Toast.makeText(MainActivity.this, "airPush360Banner onOpened", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onClosed() {
                                Toast.makeText(MainActivity.this, "airPush360Banner onClosed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLeaveApplication() {
                                Toast.makeText(MainActivity.this, "airPush360Banner onLeaveApplication", Toast.LENGTH_SHORT).show();
                            }


                        });
                        airPush360Banner.load();*/


                        /*ApSmartWall airPushSmartWall = new ApSmartWall(MainActivity.this);
                        airPushSmartWall.setEventsListener(new ApEventsListener() {
                            @Override
                            public void onLoaded(ApPreparedAd iAirPushPreparedAd) {
                                iAirPushPreparedAd.show();
                                Toast.makeText(MainActivity.this, "airPushSmartWall onLoaded", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(String s) {
                                Toast.makeText(MainActivity.this, "airPushSmartWall onFailed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onClicked() {
                                Toast.makeText(MainActivity.this, "airPushSmartWall onClicked", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onOpened() {
                                Toast.makeText(MainActivity.this, "airPushSmartWall onOpened", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onClosed() {
                                Toast.makeText(MainActivity.this, "airPushSmartWall onClosed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLeaveApplication() {
                                Toast.makeText(MainActivity.this, "airPushSmartWall onLeaveApplication", Toast.LENGTH_SHORT).show();
                            }


                        });
                        airPushSmartWall.load();*//*


                        StartAppSDK.init(MainActivity.this, "211202863", true);

                        StartAppAd.disableSplash();
                        StartAppAd.disableAutoInterstitial();


                        /*startAppAd.setVideoListener(new VideoListener() {
                            @Override
                            public void onVideoCompleted()
                            {
                                // Grant user with the reward
                                Toast.makeText(MainActivity.this, "setVideoListener", Toast.LENGTH_SHORT).show();
                            }
                        });*/
                    }
                });

            }
        };
        wt.start();

    }
    // [END signIn]

    private void setFont(ViewGroup group, Typeface font)
    {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++)
        {
            v = group.getChildAt(i);
            if (v instanceof TextView)
            {
                ((TextView) v).setTypeface(font);
            }
            else if (v instanceof Button)
            {
                ((Button) v).setTypeface(font);
            }
            else if (v instanceof ViewGroup)
            {
                setFont((ViewGroup) v, font);
            }
            else if (v instanceof GridView)
            {
                setFont((ViewGroup) v, font);
            }
            else if (v instanceof ViewSwitcher)
            {
                setFont((ViewGroup) v, font);
            }
            else if (v instanceof RelativeLayout)
            {
                setFont((ViewGroup) v, font);
            }
        }
    }
    // [END signOut]

    private void show_page_profile()
    {
        sv_page = SV_PAGE_PROFILE;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_profile);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv_title = findViewById(R.id.tv_page_profile_title);
        tv_title.setTypeface(_fontApp_bold2);

        ImageView iv_logo = (ImageView) findViewById(R.id.iv_page_profile_image_logo);
        TextView tv_user_name = (TextView) findViewById(R.id.tv_page_profile_user_name);
        TextView tv_last_login = (TextView) findViewById(R.id.tv_page_profile_user_last_login);
        TextView tv_reiting = (TextView) findViewById(R.id.tv_page_profile_reiting);
        TextView tv_count_games = (TextView) findViewById(R.id.tv_page_profile_count_games);
        TextView tv_count_wins = (TextView) findViewById(R.id.tv_page_profile_count_wins);
        TextView tv_count_draw = (TextView) findViewById(R.id.tv_page_profile_count_draw);
        TextView tv_page_profile_users = (TextView) findViewById(R.id.tv_page_profile_users);
        TextView tv_page_profile_game_over = (TextView) findViewById(R.id.tv_page_profile_game_over);


        if (cache_personal_photo != null)
        {
            iv_logo.setImageBitmap(cache_personal_photo);
        }
        else
        {
            iv_logo.setImageResource(R.drawable.default_avatar);
        }

        tv_user_name.setText(accout_info.personName);

        if (Locale.getDefault().getLanguage().equalsIgnoreCase("RU"))
        {
            tv_last_login.setText(getString(R.string.txt_17) + ": " + Utils.formatDateRUS(accout_info.time_last_login * 1000, "dd MMMM yyyy HH:mm"));
        }
        else
        {
            tv_last_login.setText(getString(R.string.txt_17) + ": " + Utils.formatDateEN(accout_info.time_last_login * 1000, "dd MMMM yyyy HH:mm"));
        }

        tv_reiting.setText(String.valueOf(accout_info.raiting));
        tv_count_games.setText(String.valueOf(accout_info.count_games));
        tv_count_wins.setText(String.valueOf(accout_info.count_wins));
        tv_count_draw.setText(String.valueOf(accout_info.count_draw));
        tv_page_profile_game_over.setText(String.valueOf(accout_info.count_defeats));
        tv_page_profile_users.setText(String.valueOf(friend_list.size()));

        TextView tv = (TextView) findViewById(R.id.tv_page_profile_balans);
        tv.setText(String.valueOf(accout_info.balans) + " " + Utils.getCorrectSuffix(accout_info.balans, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));
    }
    // [END revokeAccess]

    private void show_page_main2()
    {
        sv_page = SV_PAGE_MAIN2;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_main2);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        final ImageView iv_logo = (ImageView) findViewById(R.id.iv_page_main2_image_logo);

        if (cache_personal_photo != null)
        {
            iv_logo.setImageBitmap(cache_personal_photo);
        }
        else
        {
            iv_logo.setImageResource(R.drawable.default_avatar);
        }

        if (cache_personal_photo == null)
        {
            if (accout_info.personPhoto.trim().length() > 0)
            {
                WaitThread wt = new WaitThread(500)
                {
                    @Override
                    public void callback()
                    {
                        String url = accout_info.personPhoto;

                        url = url.replace("s96-c", "s240-c");

                        final Bitmap b = Utils.getCroppedBitmap(Utils.getBitmapFromURL(url));
                        cache_personal_photo = b;

                        if (b != null)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    iv_logo.setImageBitmap(b);
                                }
                            });
                        }
                    }
                };
                wt.start();
            }
        }

        TextView tv_page_main2_uname = (TextView) findViewById(R.id.tv_page_main2_uname);

        tv_page_main2_uname.setText(accout_info.fisrtName + " " + accout_info.lastName);


    }

    private void connection_2(long room_id)
    {
        String password = "";

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try
        {
            buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl((int) getAccoutInfo().uid)).array());
            buffer.write(ByteBuffer.allocate(8).putLong(ProtokolUtils.htonl2(room_id)).array());

            buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(getSessionInfo().session_socket_id)).array());

            buffer.write(ByteBuffer.allocate(1).put((byte) password.length()).array());
            buffer.write(password.getBytes());

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        network.queue_network.add("CONNECTION_ROOM_2"
                , null
                , buffer.toByteArray()
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        Bundle b = intent.getExtras();

        if (b != null && !auto_connection_room && b.containsKey("open_room_id"))
        {
            long room_id = b.getLong("open_room_id");

            if (room_id > 0)
            {
                auto_connection_room = true;

                connection_2(room_id);

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(10955);
            }
        }
    }

    private void show_page_menu()
    {
        if (timer_ready != null)
        {
            timer_ready.running = false;
        }

        if (timer_xod != null)
        {
            timer_xod.running = false;
        }

        if (_draw_task != null)
        {
            _draw_task._stop_thread();
        }

        if( alert_dialog_out_of_room != null )
        {
            alert_dialog_out_of_room.dismiss();
            alert_dialog_out_of_room = null;
        }

        synchronized (lock_drawTask)
        {
            if (_draw_task != null)
            {
                _draw_task = null;
                //Log.i("TAG54", "_draw_task = null");
                System.gc();
            }


            pressed_ready_game = false;
        }

        synchronized (lock_fireworks_sv)
        {
            if (fireworks_sv != null)
            {
                fireworks_sv._stop_draw = true;
                fireworks_sv = null;
            }
        }

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        click_sound = null;

        sv_page = SV_PAGE_MENU;
        removeAllOldFragmentsThisApp();

        /*Configuration configuration = getResources().getConfiguration();
        configuration.fontScale=(float) 0.85; //0.85 small size, 1 normal size, 1,15 big etc

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);*/


        setContentView(R.layout.page_menu);


        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv_title = findViewById(R.id.tv_page_menu_top_title);
        tv_title.setTypeface(_fontApp_bold2);

        page_room_request_to_friends = null;

        //accout_info.personPhoto
        final ImageView iv = (ImageView) findViewById(R.id.iv_page_menu_avatar_photo);
        TextView tv_1 = (TextView) findViewById(R.id.tv_page_menu_statistic_balans);
        TextView tv_2 = (TextView) findViewById(R.id.tv_page_menu_statistic_raiting);

        tv_1.setText(String.valueOf(accout_info.balans) + " " + Utils.getCorrectSuffix(accout_info.balans, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));
        tv_2.setText(String.valueOf( getString(R.string.txt_113) + ": " + accout_info.raiting));

        if (iv != null)
        {
            iv.setImageResource(R.drawable.default_avatar);

            if (cache_personal_photo == null)
            {
                if (accout_info.personPhoto.trim().length() > 0)
                {
                    WaitThread wt = new WaitThread(50)
                    {
                        @Override
                        public void callback()
                        {
                            String url = accout_info.personPhoto;

                            url = url.replace("s96-c", "s240-c");

                            final Bitmap b = Utils.getCroppedBitmap(Utils.getBitmapFromURL(url));

                            cache_personal_photo = b;

                            if (b != null)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        iv.setImageBitmap(b);
                                    }
                                });
                            }
                        }
                    };
                    wt.start();
                }
            }
            else
            {
                iv.setImageBitmap(cache_personal_photo);
            }
        }

        TableRow tr = (TableRow) findViewById(R.id.tr_page_main_open_settings);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_settings();
            }
        });

        tr = (TableRow) findViewById(R.id.tr_page_main_open_rules);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_rules();
            }
        });

        tr = (TableRow) findViewById(R.id.tr_page_main_open_list_friends);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_friends();
            }
        });

        tr = (TableRow) findViewById(R.id.tr_page_main_open_list_add_money);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_add_balans();
            }
        });

        tr = (TableRow) findViewById(R.id.tr_page_main_open_list_rating);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_table_rating();
            }
        });

        tr = (TableRow) findViewById(R.id.tr_page_main_open_shop);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_shop();
            }
        });


        tr = (TableRow) findViewById(R.id.tr_page_main_open_share);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.txt_91) + " https://play.google.com/store/apps/details?id=durakonline.sk.durakonline";

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Online Radio");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.txt_8)));
            }
        });

        tr = (TableRow) findViewById(R.id.tr_page_main_create_new_game);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*if (accout_info.balans < 10)
                {

                }
                else*/
                {
                    show_page_create_new_game();
                }
            }
        });

        tr = (TableRow) findViewById(R.id.tr_page_main_open_search);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_search();
            }
        });

        /*tr = (TableRow) findViewById(R.id.tr_page_main_open_profile);
        tr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_profile();
            }
        });*/

        LinearLayout ll_photo = findViewById(R.id.ll_page_menu_avatar_photo);
        ll_photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_profile();
            }
        });

        //accout_info.balans

        /*android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        StatisticTop fragment = (StatisticTop) fm.findFragmentById(R.id.page_main_top_info);

        if (fragment != null)
        {
            fragment.setMoneyText(String.valueOf(accout_info.balans));
        }*/

        Bundle b = getIntent().getExtras();

        if (b != null && !auto_connection_room && b.containsKey("open_room_id"))
        {
            long room_id = b.getLong("open_room_id");

            if (room_id > 0)
            {
                auto_connection_room = true;

                connection_2(room_id);
            }
        }

        TextView tv = findViewById(R.id.tv_page_menu_alert_update_app_cancel);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                FrameLayout fl = findViewById(R.id.fl_page_menu_alert_new_version_app);

                if (fl != null)
                {
                    fl.setVisibility(View.GONE);
                }
            }
        });

        tv = findViewById(R.id.tv_page_menu_alert_update_app_update);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FrameLayout fl = findViewById(R.id.fl_page_menu_alert_new_version_app);

                if (fl != null)
                {
                    fl.setVisibility(View.GONE);
                }
            }
        });

        setUiCountOnline(last_count_online);

        System.gc();
    }

    private void setUiCountOnline(int v)
    {
        TextView tv = MainActivity.this.findViewById(R.id.tv_page_menu_online_count);
        if (tv != null)
        {
            if( last_count_online < 10 )
            {
                tv.setVisibility(View.GONE);
            }
            else
            {
                tv.setVisibility(View.VISIBLE);
                tv.setText("ONLINE: " + String.valueOf(last_count_online));
            }
        }
    }

    public void UiPageSearchShowHideWait(boolean show)
    {
        /*LinearLayout fl = (LinearLayout) findViewById(R.id.ll_page_search_loading);

        if (fl != null)
        {
            fl.setVisibility(show ? View.VISIBLE : View.GONE);
        }*/

        FrameLayout fl = findViewById(R.id.fl_page_search_wait);

        if (show)
        {
            fl.setVisibility(View.VISIBLE);
        }
        else
        {
            fl.setVisibility(View.GONE);
        }
    }

    private void ui_stop_timer_waiting_xod()
    {
        //Log.i("TAG36", "ui_stop_timer_waiting_xod");
        //Log.d("TAG36", android.util.Log.getStackTraceString(new Exception()));

        if (timer_xod != null)
        {
            timer_xod.running = false;
            timer_xod = null;
        }

        TextView tv = (TextView) findViewById(R.id.tv_page_room_waiting_time);

        if (tv == null)
        {
            return;
        }

        tv.setText("");

        //TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);
        //tv_0.setText("");

        ProgressBar pb = (ProgressBar) findViewById(R.id.pb_page_room_waiting);
        pb.setVisibility(View.GONE);

        //Log.i("TAG36", "pb.setVisibility(View.GONE)");
    }

    private void ui_page_table_rating_draw_info_user(
            int ui_c
            , int ui_id
            , int count_games
            , int count_defeats
            , int count_draw
            , int count_wins
            , String fio
            , Bitmap img
    )
    {
        LinearLayout ll = findViewById(ui_id);
        if (ll == null)
        {
            return;
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        View child = getLayoutInflater().inflate(R.layout.template_info_user, null);

        setFont((ViewGroup) child, _fontApp_bold);

        ll.removeAllViews();
        ll.addView(child, lp);

        ImageView iv = findViewById(R.id.iv_template_info_user_avatar);

        iv.setImageBitmap(img);

        TextView tv1 = findViewById(R.id.tv_template_info_user_popup_total_game);
        TextView tv2 = findViewById(R.id.tv_template_info_user_popup_defeats);
        TextView tv3 = findViewById(R.id.tv_template_info_user_popup_draw);
        TextView tv4 = findViewById(R.id.tv_template_info_user_popup_wins);
        TextView tv5 = findViewById(R.id.tv_template_info_user_popup_user_name);

        tv1.setText(String.valueOf(count_games));
        tv2.setText(String.valueOf(count_defeats));
        tv3.setText(String.valueOf(count_draw));
        tv4.setText(String.valueOf(count_wins));
        tv5.setText(fio);

        FrameLayout fl = findViewById(ui_c);
        fl.setVisibility(View.VISIBLE);
    }

    private void ui_draw_list_rating(List<UiItemRating> list)
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_page_table_rating_list);

        if (ll == null)
        {
            return;
        }

        ll.setPadding(
                  (int) (_scale_px * 1)
                , (int) (_scale_px * 3)
                , (int) (_scale_px * 1)
                , (int) (_scale_px * 3)
        );

        ll.removeAllViews();

        TableLayout tl = new TableLayout(MainActivity.this);

        ll.addView(tl);

        for (int i = 0; i < list.size(); i++)
        {
            TableRow TR = new TableRow(MainActivity.this);

            TR.setTag(list.get(i));
            TR.setClickable(true);
            TR.setBackgroundResource(R.drawable.style_select_menu_1);
            TR.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    UiItemRating data = (UiItemRating) v.getTag();

                    ui_page_table_rating_draw_info_user(
                            R.id.fl_table_rating_user_info
                            , R.id.fl_page_table_rating_user_info_data
                            , data.count_games
                            , data.count_defeats
                            , data.count_draw
                            , data.count_wins
                            , data.first_name + " " + data.last_name
                            , data.img
                    );
                }
            });

            TableRow.LayoutParams lp_1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            lp_1.gravity = Gravity.CENTER;

            TextView tv_posnum = new TextView(MainActivity.this);
            tv_posnum.setText(String.valueOf(i + 1));
            tv_posnum.setTextColor(Color.YELLOW);
            tv_posnum.setTextSize(20);
            tv_posnum.setPadding(
                    (int) (_scale_px * 10)
                    , (int) (_scale_px * 10)
                    , (int) (_scale_px * 10)
                    , (int) (_scale_px * 10)
            );
            tv_posnum.setLayoutParams(lp_1);
            tv_posnum.setTypeface(_fontApp_bold);

            TR.addView(tv_posnum);

            ImageView iv = new ImageView(MainActivity.this);

            TableRow.LayoutParams lp_2 = new TableRow.LayoutParams((int) (40 * _scale_px), (int) (40 * _scale_px));
            lp_2.gravity = Gravity.CENTER;
            iv.setLayoutParams(lp_2);

            iv.setImageResource(R.drawable.default_avatar);
            iv.setAdjustViewBounds(true);

            list.get(i).iv = iv;

            if (list.get(i).img == null)
            {
                iv.setImageResource(R.drawable.default_avatar);

                final int index = i;

                WaitThread wt = new WaitThread(0)
                {
                    @Override
                    public void callback()
                    {
                        final Bitmap b = Utils.getCroppedBitmap(Utils.getBitmapFromURL(list_rating.get(index).puctire));

                        if (b != null)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    list_rating.get(index).img = b;
                                    list_rating.get(index).iv.setImageBitmap(b);
                                }
                            });
                        }
                    }
                };
                wt.start();
            }
            else
            {
                iv.setImageBitmap(list_rating.get(i).img);
            }

            TR.addView(iv);

            TableRow.LayoutParams lp_3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            lp_3.gravity = Gravity.CENTER;

            TextView tv_uname = new TextView(MainActivity.this);
            tv_uname.setText(list.get(i).first_name + " " + list.get(i).last_name);
            tv_uname.setTypeface(_fontApp_bold);
            tv_uname.setTextColor(Color.YELLOW);
            tv_uname.setTextSize(20);
            tv_uname.setPadding(
                    (int) (_scale_px * 10)
                    , (int) (_scale_px * 10)
                    , (int) (_scale_px * 10)
                    , (int) (_scale_px * 10)
            );
            //tv_uname.setLayoutParams(lp_3);

            TR.addView(tv_uname);

            tl.addView(TR);

            //-------------------

            TableRow tr2 = new TableRow(MainActivity.this);
            tr2.setBackgroundColor(Color.parseColor("#444444"));
            tr2.setPadding(0, 0, 0, 1);

            tl.addView(tr2);
        }

        FrameLayout fl = findViewById(R.id.fl_page_table_rating_wait);
        fl.setVisibility(View.GONE);
    }

    private void ui_draw_list_my_search_friends(final List<SearchItem> _list)
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_page_search_friends_list);

        if (ll == null)
        {
            return;
        }

        ll.removeAllViews();

        if (_list.size() == 0)
        {
            TextView tv_raiting = new TextView(MainActivity.this);
            tv_raiting.setTextColor(Color.WHITE);
            tv_raiting.setTypeface(_fontApp_bold);
            tv_raiting.setTextSize(18);
            tv_raiting.setText(getString(R.string.txt_35));
            tv_raiting.setPadding((int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px));

            ll.addView(tv_raiting);

            FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_search_friends_wait_loading);
            fl.setVisibility(View.GONE);

            return;
        }

        for (int i = 0; i < _list.size(); i++)
        {
            ImageView iv_add_frind = null;

            LinearLayout ll_item = new LinearLayout(MainActivity.this);
            ll_item.setOrientation(LinearLayout.HORIZONTAL);
            //ll_item.setBackgroundColor(Color.GRAY);
            ll_item.setPadding((int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px));

            LinearLayout.LayoutParams lp_iv_logo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) (50 * _scale_px));

            final ImageView iv_logo = new ImageView(MainActivity.this);
            //final String url = _friends.get(i).picture;

            iv_logo.setLayoutParams(lp_iv_logo);
            iv_logo.setImageResource(R.drawable.default_avatar);
            iv_logo.setAdjustViewBounds(true);

            if (_list.get(i).img == null)
            //if (url != null && url.length() > 0)
            {
                final int index = i;

                //_friends.get(i).iv = iv_logo;

                WaitThread wt = new WaitThread(0)
                {
                    @Override
                    public void callback()
                    {
                        final Bitmap b = Utils.getCroppedBitmap(Utils.getBitmapFromURL(_list.get(index).picture));

                        if (b != null)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    _list.get(index).img = b;
                                    iv_logo.setImageBitmap(b);
                                }
                            });
                        }
                    }
                };
                wt.start();
            }
            else
            {
                iv_logo.setImageBitmap(_list.get(i).img);
            }


            ll_item.addView(iv_logo);

            LinearLayout.LayoutParams lp_ll1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            lp_ll1.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_ll1.leftMargin = (int) (10 * _scale_px);

            LinearLayout ll1 = new LinearLayout(MainActivity.this);
            ll1.setOrientation(LinearLayout.VERTICAL);
            ll1.setLayoutParams(lp_ll1);


            LinearLayout.LayoutParams lp_ll2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_ll2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_ll2.leftMargin = (int) (5 * _scale_px);

            LinearLayout ll2 = new LinearLayout(MainActivity.this);
            ll2.setOrientation(LinearLayout.HORIZONTAL);
            ll2.setLayoutParams(lp_ll2);

            LinearLayout.LayoutParams lp_tv1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_tv1.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

            LinearLayout.LayoutParams lp_tv2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_tv2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_tv2.leftMargin = (int) (7 * _scale_px);

            TextView tv1 = new TextView(MainActivity.this);
            tv1.setTextColor(Color.WHITE);
            tv1.setLayoutParams(lp_tv1);
            tv1.setTextSize(16);
            tv1.setTypeface(_fontApp_bold);
            tv1.setText(_list.get(i).first_name);

            ll2.addView(tv1);

            TextView tv2 = new TextView(MainActivity.this);
            tv2.setTextColor(Color.WHITE);
            tv2.setLayoutParams(lp_tv2);
            tv2.setTextSize(16);
            tv2.setTypeface(_fontApp_bold);
            tv2.setText(_list.get(i).last_name);

            ll2.addView(tv2);


            ll1.addView(ll2);

            /*LinearLayout.LayoutParams lp_tv_raiting = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_tv_raiting.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_tv_raiting.leftMargin = (int) ( 5 * _scale_px );

            TextView tv_raiting = new TextView(MainActivity.this);
            tv_raiting.setTextColor(Color.WHITE);
            tv_raiting.setLayoutParams(lp_tv_raiting);
            tv_raiting.setTextSize(16);
            tv_raiting.setText("Рейтинг: " + String.valueOf(_list.get(i).raiting) );

            ll1.addView(tv_raiting);*/

            ll_item.addView(ll1);

            final String uname = _list.get(i).first_name + " " + _list.get(i).last_name;

            //-----------------------------------------------------------------------------------------

            LinearLayout.LayoutParams lp_iv_add_frineds = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_iv_add_frineds.gravity = Gravity.CENTER;
            lp_iv_add_frineds.rightMargin = (int) (5 * _scale_px);

            iv_add_frind = new ImageView(MainActivity.this);
            iv_add_frind.setImageResource(R.drawable.style_btn_plus);
            iv_add_frind.setLayoutParams(lp_iv_add_frineds);
            iv_add_frind.setClickable(true);
            iv_add_frind.setPadding((int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px));

            iv_add_frind.setTag(_list.get(i).uid);
            iv_add_frind.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View view)
                {
                    final int _uid = (Integer) view.getTag();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Добавить в друзья \n" + uname + "?");

                    builder.setPositiveButton("Да", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ArrayList<String> params = new ArrayList<String>();

                            params.add(String.valueOf(accout_info.uid));
                            params.add(String.valueOf(session_info.session_socket_id));
                            params.add(String.valueOf(_uid));

                            network.queue_network.add(getString(R.string.cmd_10)
                                    , params
                                    , null
                                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                    , QueueNetwork.TYPE_SEND__LONG);

                            dialog.dismiss();

                            View v = (View) view.getParent();

                            ((ViewManager) v.getParent()).removeView(v);
                        }
                    });

                    builder.setNegativeButton("Нет", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.setOnShowListener(new DialogInterface.OnShowListener()
                    {
                        @Override
                        public void onShow(DialogInterface arg0)
                        {
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green_1));
                            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                        }
                    });

                    alert.show();
                }
            });

            ll_item.addView(iv_add_frind);


            //-----------------------------------------------------------------------------------------


            ll.addView(ll_item);

            TableRow tr = new TableRow(MainActivity.this);
            tr.setBackgroundColor(Color.GRAY);
            tr.setPadding(0, 0, 0, 1);

            ll.addView(tr);

            UiItemFriends uf = new UiItemFriends();
            uf.ll = ll_item;

            uf.iv = iv_add_frind;

            //map_ui_search_friends.put( _list.get(i).uid, uf );
        }

        FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_search_friends_wait_loading);
        fl.setVisibility(View.GONE);
    }

    private void ui_draw_list_my_friends(final List<FriendItem> _friends)
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_page_friends_list);

        if (ll == null)
        {
            return;
        }

        ll.removeAllViews();

        if (_friends.size() == 0)
        {
            TextView tv_raiting = new TextView(MainActivity.this);
            tv_raiting.setTextColor(Color.WHITE);
            tv_raiting.setTextSize(18);
            tv_raiting.setTypeface(_fontApp_bold);
            tv_raiting.setText("Нет данных");
            tv_raiting.setPadding((int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px));

            ll.addView(tv_raiting);

            FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_friends_wait_loading);
            fl.setVisibility(View.GONE);

            return;
        }

        for (int i = 0; i < _friends.size(); i++)
        {
            ImageView iv_add_frind = null;

            LinearLayout ll_item = new LinearLayout(MainActivity.this);
            ll_item.setOrientation(LinearLayout.HORIZONTAL);
            //ll_item.setBackgroundColor(Color.GRAY);
            ll_item.setPadding((int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px));

            ll_item.setTag(_friends.get(i));
            ll_item.setClickable(true);
            ll_item.setBackgroundResource(R.drawable.style_select_menu_1);
            ll_item.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FriendItem data = (FriendItem) v.getTag();

                    ui_page_table_rating_draw_info_user(
                            R.id.fl_page_friends_user_info
                            , R.id.fl_page_friends_user_info_data
                            , data.count_games
                            , data.count_defeats
                            , data.count_draw
                            , data.count_wins
                            , data.first_name + " " + data.last_name
                            , data.img
                    );
                }
            });

            LinearLayout.LayoutParams lp_iv_logo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) (50 * _scale_px));

            final ImageView iv_logo = new ImageView(MainActivity.this);
            //final String url = _friends.get(i).picture;

            iv_logo.setLayoutParams(lp_iv_logo);
            iv_logo.setImageResource(R.drawable.default_avatar);
            iv_logo.setAdjustViewBounds(true);

            if (_friends.get(i).img == null)
            //if (url != null && url.length() > 0)
            {
                final int index = i;

                //_friends.get(i).iv = iv_logo;

                WaitThread wt = new WaitThread(0)
                {
                    @Override
                    public void callback()
                    {
                        final Bitmap b = Utils.getCroppedBitmap(Utils.getBitmapFromURL(_friends.get(index).picture));

                        if (b != null)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    _friends.get(index).img = b;
                                    iv_logo.setImageBitmap(b);
                                }
                            });
                        }
                    }
                };
                wt.start();
            }
            else
            {
                iv_logo.setImageBitmap(_friends.get(i).img);
            }


            ll_item.addView(iv_logo);

            LinearLayout.LayoutParams lp_ll1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            lp_ll1.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_ll1.leftMargin = (int) (10 * _scale_px);

            LinearLayout ll1 = new LinearLayout(MainActivity.this);
            ll1.setOrientation(LinearLayout.VERTICAL);
            ll1.setLayoutParams(lp_ll1);


            LinearLayout.LayoutParams lp_ll2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_ll2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_ll2.leftMargin = (int) (5 * _scale_px);

            LinearLayout ll2 = new LinearLayout(MainActivity.this);
            ll2.setOrientation(LinearLayout.HORIZONTAL);
            ll2.setLayoutParams(lp_ll2);

            LinearLayout.LayoutParams lp_tv1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_tv1.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

            LinearLayout.LayoutParams lp_tv2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_tv2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_tv2.leftMargin = (int) (7 * _scale_px);

            TextView tv1 = new TextView(MainActivity.this);
            tv1.setTextColor(Color.WHITE);
            tv1.setLayoutParams(lp_tv1);
            tv1.setTextSize(16);
            tv1.setTypeface(_fontApp_bold);
            tv1.setText(_friends.get(i).first_name);

            ll2.addView(tv1);

            TextView tv2 = new TextView(MainActivity.this);
            tv2.setTextColor(Color.WHITE);
            tv2.setLayoutParams(lp_tv2);
            tv2.setTextSize(16);
            tv2.setTypeface(_fontApp_bold);
            tv2.setText(_friends.get(i).last_name);

            ll2.addView(tv2);


            ll1.addView(ll2);

            LinearLayout.LayoutParams lp_tv_raiting = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_tv_raiting.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_tv_raiting.leftMargin = (int) (5 * _scale_px);

            TextView tv_raiting = new TextView(MainActivity.this);
            tv_raiting.setTextColor(Color.WHITE);
            tv_raiting.setLayoutParams(lp_tv_raiting);
            tv_raiting.setTextSize(16);
            tv_raiting.setTypeface(_fontApp_bold);
            tv_raiting.setText("Рейтинг: " + String.valueOf(_friends.get(i).raiting));

            ll1.addView(tv_raiting);

            ll_item.addView(ll1);

            final String uname = _friends.get(i).first_name + " " + _friends.get(i).last_name;

            //-----------------------------------------------------------------------------------------

            if (_friends.get(i).status_fiends == 0) // отправлен запрос на добавления в друзья
            {
                ll_item.setBackgroundColor(Color.parseColor("#5a5941"));

                LinearLayout.LayoutParams lp_iv_txt4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp_iv_txt4.gravity = Gravity.CENTER;
                lp_iv_txt4.rightMargin = (int) (3 * _scale_px);

                TextView tv_txt4 = new TextView(MainActivity.this);
                tv_txt4.setTextColor(Color.WHITE);
                tv_txt4.setText("Отправлен запрос");
                tv_txt4.setLayoutParams(lp_iv_txt4);
                tv_txt4.setTypeface(_fontApp_bold);

                ll1.addView(tv_txt4);
            }
            else if (_friends.get(i).status_fiends == 3) // удален из друзей
            {
                ll_item.setBackgroundColor(Color.parseColor("#704f4f"));

                LinearLayout.LayoutParams lp_iv_txt4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp_iv_txt4.gravity = Gravity.CENTER;
                lp_iv_txt4.rightMargin = (int) (3 * _scale_px);

                TextView tv_txt4 = new TextView(MainActivity.this);
                tv_txt4.setTextColor(Color.WHITE);
                tv_txt4.setText("Удален из друзей");
                tv_txt4.setLayoutParams(lp_iv_txt4);
                tv_txt4.setTypeface(_fontApp_bold);

                ll1.addView(tv_txt4);
            }
            else if (_friends.get(i).status_fiends == 2) // запрос в друзья
            {
                LinearLayout.LayoutParams lp_iv_add_frineds = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp_iv_add_frineds.gravity = Gravity.CENTER;
                lp_iv_add_frineds.rightMargin = (int) (5 * _scale_px);

                iv_add_frind = new ImageView(MainActivity.this);
                iv_add_frind.setImageResource(R.drawable.style_btn_plus);
                iv_add_frind.setLayoutParams(lp_iv_add_frineds);
                iv_add_frind.setClickable(true);
                iv_add_frind.setPadding((int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px));

                iv_add_frind.setTag(_friends.get(i).uid);
                iv_add_frind.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        final int _uid = (Integer) view.getTag();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setTitle(getString(R.string.txt_57) + " \n" + uname + "?");
                        builder.setPositiveButton(getString(R.string.txt_71), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // надо проверить ход на сервере если все хорошо, передать ход следующему кто подкидывает
                                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                                try
                                {
                                    buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(_uid)).array());
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }

                                // подкинем карту
                                network.queue_network.add("ADD_TO_FRIEND_CONNECT"
                                        , null
                                        , buffer.toByteArray()
                                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                        , QueueNetwork.TYPE_SEND__FORCE);

                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton(getString(R.string.txt_72), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });

                        final AlertDialog alert = builder.create();

                        alert.setOnShowListener(new DialogInterface.OnShowListener()
                        {
                            @Override
                            public void onShow(DialogInterface arg0)
                            {
                                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green_1));
                                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                            }
                        });

                        alert.show();
                    }
                });

                ll_item.addView(iv_add_frind);

            }
            //-----------------------------------------------------------------------------------------
            LinearLayout.LayoutParams lp_iv_send_money = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_iv_send_money.gravity = Gravity.CENTER;
            lp_iv_send_money.leftMargin = (int) (10 * _scale_px);

            ImageView iv_send_money = new ImageView(MainActivity.this);
            iv_send_money.setImageResource(R.drawable.style_btn_send_money);
            iv_send_money.setLayoutParams(lp_iv_send_money);
            iv_send_money.setClickable(true);
            iv_send_money.setPadding((int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px));
            iv_send_money.setTag(_friends.get(i));

            iv_send_money.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FrameLayout fl = findViewById(R.id.fl_page_friends_send_money);
                    fl.setVisibility(View.VISIBLE);

                    FriendItem fi = (FriendItem) (v.getTag());

                    uid_page_friends_selected_send_money = fi.uid;

                    TextView tv = findViewById(R.id.tv_page_friends_alert_dialog_fname);
                    tv.setText(fi.first_name + " " + fi.last_name);
                }
            });

            ll_item.addView(iv_send_money);
            //-----------------------------------------------------------------------------------------

            LinearLayout.LayoutParams lp_iv_delete = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_iv_delete.gravity = Gravity.CENTER;
            lp_iv_delete.leftMargin = (int) (10 * _scale_px);

            ImageView iv_delete = new ImageView(MainActivity.this);
            iv_delete.setImageResource(R.drawable.style_btn_close1);
            iv_delete.setLayoutParams(lp_iv_delete);
            iv_delete.setClickable(true);
            iv_delete.setPadding((int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px));

            iv_delete.setTag(_friends.get(i).uid);
            iv_delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    final int _uid = (Integer) view.getTag();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle(getString(R.string.txt_73) + " \n" + uname + "?");

                    builder.setPositiveButton(getString(R.string.txt_71), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // надо проверить ход на сервере если все хорошо, передать ход следующему кто подкидывает
                            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                            try
                            {
                                buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(_uid)).array());
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }

                            // подкинем карту
                            network.queue_network.add("DELETE_FRIEND_CONNECT"
                                    , null
                                    , buffer.toByteArray()
                                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                    , QueueNetwork.TYPE_SEND__FORCE);

                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton(getString(R.string.txt_72), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.setOnShowListener(new DialogInterface.OnShowListener()
                    {
                        @Override
                        public void onShow(DialogInterface arg0)
                        {
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green_1));
                            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                        }
                    });

                    alert.show();
                }
            });

            ll_item.addView(iv_delete);

            ll.addView(ll_item);

            TableRow tr = new TableRow(MainActivity.this);
            tr.setBackgroundColor(Color.GRAY);
            tr.setPadding(0, 0, 0, 1);

            ll.addView(tr);

            UiItemFriends uf = new UiItemFriends();
            uf.ll = ll_item;

            uf.iv = iv_add_frind;

            map_ui_friends.put(_friends.get(i).uid, uf);
        }

        FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_friends_wait_loading);
        fl.setVisibility(View.GONE);
    }

    /*private void ui_update_controls()
    {
        if( _draw_task == null )
        {
            return;
        }

        ui_page_room_hide_all_btns();

        if( _draw_task.getRoomInfo().current_pos_user_xod == _draw_task.getRoomInfo().current_my_position )
        {

        }
        //else
    }*/

    private void ui_draw_list_my_friends_create_game(List<FriendItem> _friends)
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_page_create_new_game_friends_list);

        if (ll == null)
        {
            return;
        }

        ll.removeAllViews();
        list_checkbox.clear();

        if (_friends.size() == 0)
        {
            TextView tv_raiting = new TextView(MainActivity.this);
            tv_raiting.setTypeface(_fontApp_bold);
            tv_raiting.setTextColor(Color.WHITE);
            tv_raiting.setTextSize(18);
            tv_raiting.setText(getString(R.string.txt_35));
            tv_raiting.setPadding((int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px));

            ll.addView(tv_raiting);

            return;
        }

        for (int i = 0; i < _friends.size(); i++)
        {
            if (_friends.get(i).status_fiends != 1)
            {
                continue;
            }

            ImageView iv_add_frind = null;

            LinearLayout.LayoutParams lp_ll_item = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            LinearLayout ll_item = new LinearLayout(MainActivity.this);
            ll_item.setOrientation(LinearLayout.HORIZONTAL);
            //ll_item.setBackgroundColor(Color.GRAY);
            ll_item.setLayoutParams(lp_ll_item);
            ll_item.setPadding((int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px), (int) (10 * _scale_px));

            LinearLayout.LayoutParams lp_iv_logo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) (50 * _scale_px));

            final ImageView iv_logo = new ImageView(MainActivity.this);
            final String url = _friends.get(i).picture;

            iv_logo.setLayoutParams(lp_iv_logo);
            iv_logo.setImageResource(R.drawable.default_avatar);
            iv_logo.setAdjustViewBounds(true);

            if (url != null && url.length() > 0)
            {
                WaitThread wt = new WaitThread(0)
                {
                    @Override
                    public void callback()
                    {
                        final Bitmap b = Utils.getCroppedBitmap(Utils.getBitmapFromURL(url));

                        if (b != null)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    iv_logo.setImageBitmap(b);
                                }
                            });
                        }
                    }
                };
                wt.start();
            }

            ll_item.addView(iv_logo);

            LinearLayout.LayoutParams lp_ll1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            lp_ll1.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_ll1.leftMargin = (int) (10 * _scale_px);

            LinearLayout ll1 = new LinearLayout(MainActivity.this);
            ll1.setOrientation(LinearLayout.VERTICAL);
            ll1.setLayoutParams(lp_ll1);


            LinearLayout.LayoutParams lp_ll2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_ll2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_ll2.leftMargin = (int) (5 * _scale_px);

            LinearLayout ll2 = new LinearLayout(MainActivity.this);
            ll2.setOrientation(LinearLayout.HORIZONTAL);
            ll2.setLayoutParams(lp_ll2);

            LinearLayout.LayoutParams lp_tv1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_tv1.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

            LinearLayout.LayoutParams lp_tv2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_tv2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_tv2.leftMargin = (int) (7 * _scale_px);

            TextView tv1 = new TextView(MainActivity.this);
            tv1.setTextColor(Color.WHITE);
            tv1.setLayoutParams(lp_tv1);
            tv1.setTextSize(16);
            tv1.setTypeface(_fontApp_bold);
            tv1.setText(_friends.get(i).first_name);

            ll2.addView(tv1);

            TextView tv2 = new TextView(MainActivity.this);
            tv2.setTextColor(Color.WHITE);
            tv2.setLayoutParams(lp_tv2);
            tv2.setTextSize(16);
            tv2.setTypeface(_fontApp_bold);
            tv2.setText(_friends.get(i).last_name);

            ll2.addView(tv2);


            ll1.addView(ll2);

            LinearLayout.LayoutParams lp_tv_raiting = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_tv_raiting.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp_tv_raiting.leftMargin = (int) (5 * _scale_px);

            TextView tv_raiting = new TextView(MainActivity.this);
            tv_raiting.setTextColor(Color.WHITE);
            tv_raiting.setLayoutParams(lp_tv_raiting);
            tv_raiting.setTextSize(16);
            tv_raiting.setTypeface(_fontApp_bold);
            tv_raiting.setText(getString(R.string.txt_53) + ": " + String.valueOf(_friends.get(i).raiting));

            ll1.addView(tv_raiting);

            ll_item.addView(ll1);

            //-----------------------------------------------------------------------------------------

            LinearLayout.LayoutParams lp_iv_delete = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp_iv_delete.gravity = Gravity.CENTER;
            lp_iv_delete.leftMargin = (int) (10 * _scale_px);

            CheckBox ch_1 = new CheckBox(new ContextThemeWrapper(MainActivity.this, R.style.MyCheckbox), null, R.style.MyCheckbox);
            ch_1.setLayoutParams(lp_iv_delete);
            ch_1.setClickable(true);
            ch_1.setPadding((int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px), (int) (3 * _scale_px));

            ch_1.setTag(_friends.get(i).uid);

            list_checkbox.add(ch_1);

            ll_item.addView(ch_1);

            ll.addView(ll_item);

            TableRow tr = new TableRow(MainActivity.this);
            tr.setBackgroundColor(Color.GRAY);
            tr.setPadding(0, 0, 0, 1);

            ll.addView(tr);

            UiItemFriends uf = new UiItemFriends();
            uf.ll = ll_item;

            uf.iv = iv_add_frind;

            map_ui_friends.put(_friends.get(i).uid, uf);
        }
    }

    private void ui_page_shop_set_active_menu(int k)
    {
        LinearLayout ll1 = findViewById(R.id.ll_page_shop_top_menu_1);
        LinearLayout ll2 = findViewById(R.id.ll_page_shop_top_menu_2);

        TextView tv1 = findViewById(R.id.tv_page_shop_top_menu_label_1);
        TextView tv2 = findViewById(R.id.tv_page_shop_top_menu_label_2);

        FrameLayout fl1 = findViewById(R.id.fl_page_shop_page_1);
        FrameLayout fl2 = findViewById(R.id.fl_page_shop_page_2);

        if (k == 1)
        {
            ll1.setBackgroundColor(Color.parseColor("#cccccc"));
            tv1.setTextColor(Color.parseColor("#990000"));

            ll2.setBackgroundResource(0);
            tv2.setTextColor(Color.parseColor("#ffffff"));

            fl1.setVisibility(View.VISIBLE);
            fl2.setVisibility(View.GONE);
        }
        else
        {
            ll2.setBackgroundColor(Color.parseColor("#cccccc"));
            tv2.setTextColor(Color.parseColor("#990000"));

            ll1.setBackgroundResource(0);
            tv1.setTextColor(Color.parseColor("#ffffff"));

            fl1.setVisibility(View.GONE);
            fl2.setVisibility(View.VISIBLE);
        }
    }

    private void ui_add_list_request_user_in_page_room(RequestToFriendInfo uinfo)
    {
        LinearLayout ll = new LinearLayout(MainActivity.this);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ll.setPadding(
                (int) (5 * _scale_px)
                , (int) (5 * _scale_px)
                , (int) (5 * _scale_px)
                , (int) (5 * _scale_px)
        );

        LinearLayout.LayoutParams iv_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iv_lp.rightMargin = (int) (8 * _scale_px);

        uinfo.iv.setLayoutParams(iv_lp);

        ll.addView(uinfo.iv);

        LinearLayout ll2 = new LinearLayout(MainActivity.this);
        ll2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp_ll2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        ll2.setLayoutParams(lp_ll2);


        LinearLayout.LayoutParams lp_ll3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp_ll3.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

        LinearLayout ll3 = new LinearLayout(MainActivity.this);
        ll3.setOrientation(LinearLayout.HORIZONTAL);
        ll3.setLayoutParams(lp_ll3);

        TextView tv_uname = new TextView(MainActivity.this);
        tv_uname.setTypeface(_fontApp_bold);
        tv_uname.setTextSize(16);
        tv_uname.setText(uinfo.first_name + " " + uinfo.last_name);
        tv_uname.setTextColor(Color.parseColor("#555555"));

        ll3.addView(tv_uname);

                    /*TextView tv_uname2 = new TextView(MainActivity.this);
                    tv_uname2.setTextSize( 16 );
                    tv_uname2.setText(" " + uinfo.last_name);
                    tv_uname2.setTextColor(Color.parseColor("#555555"));

                    ll3.addView(tv_uname2);*/


        ll2.addView(ll3);

        LinearLayout ll4 = new LinearLayout(MainActivity.this);
        ll4.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout.LayoutParams lp_iv_star = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) (18 * _scale_px));
        lp_iv_star.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

        ImageView iv_star = new ImageView(MainActivity.this);
        iv_star.setImageResource(R.drawable.star);
        iv_star.setLayoutParams(lp_iv_star);

        ll4.addView(iv_star);

        LinearLayout.LayoutParams lp_tv_reiting = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp_tv_reiting.gravity = Gravity.CENTER_VERTICAL;
        lp_tv_reiting.leftMargin = (int) (5 * _scale_px);

        TextView tv_reiting = new TextView(MainActivity.this);
        tv_reiting.setTypeface(_fontApp_bold);
        tv_reiting.setText(String.valueOf(uinfo.reiting));
        tv_reiting.setTextSize(16);
        tv_reiting.setLayoutParams(lp_tv_reiting);

        ll4.addView(tv_reiting);

        ll2.addView(ll4);


        ll.addView(ll2);

        LinearLayout.LayoutParams lp_btn_add_to_friend = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp_btn_add_to_friend.gravity = Gravity.CENTER;
        lp_btn_add_to_friend.leftMargin = (int) (10 * _scale_px);

        FrameLayout fl = new FrameLayout(MainActivity.this);
        fl.setLayoutParams(lp_btn_add_to_friend);

        FrameLayout.LayoutParams lp_fl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp_fl.gravity = Gravity.CENTER;

        LinearLayout ll_btn = new LinearLayout(MainActivity.this);
        ll_btn.setBackgroundResource(R.drawable.style_btn2);
        ll_btn.setClickable(true);
        ll_btn.setLayoutParams(lp_fl);
        ll_btn.setPadding(
                (int) (5 * _scale_px)
                , (int) (5 * _scale_px)
                , (int) (5 * _scale_px)
                , (int) (5 * _scale_px)
        );

        ll_btn.setTag(uinfo);

        TextView tv_txt1 = new TextView(MainActivity.this);
        tv_txt1.setText(getString(R.string.txt_74));
        tv_txt1.setTextColor(Color.WHITE);
        tv_txt1.setGravity(Gravity.CENTER);
        tv_txt1.setTypeface(_fontApp_bold);

        ll_btn.addView(tv_txt1);

        ll_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RequestToFriendInfo _uinfo = (RequestToFriendInfo) view.getTag();

                ArrayList<String> params = new ArrayList<String>();

                params.add(String.valueOf(accout_info.uid));
                params.add(String.valueOf(session_info.session_socket_id));
                params.add(String.valueOf(_uinfo.uid));

                network.queue_network.add("ADD_TO_FRIENDS_2"
                        , params
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__LONG);

                        /*LinearLayout wait_icon = (LinearLayout) findViewById(R.id.ll_page_room_wait_add_friend);
                        wait_icon.setVisibility(View.VISIBLE);*/

                view.setVisibility(View.GONE);
            }
        });

        fl.addView(ll_btn);

        ll.addView(fl);

        LinearLayout ll_page_room_messages_list = (LinearLayout) findViewById(R.id.ll_page_room_messages_list);
        ll_page_room_messages_list.addView(ll);
    }

    private void ui_start_timer_waiting_xod(int second_wait)
    {
        if (_draw_task == null)
        {
            return;
        }

        if (timer_xod != null)
        {
            timer_xod.running = false;
        }

        timer_xod = new TimerXod(second_wait)
        {
            @Override
            public void callback_BeforeStart(final int total_count)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ProgressBar pb = findViewById(R.id.pb_page_room_waiting);

                        if (pb != null)
                        {
                            //Log.i("TAG36", "TimerXod: pb.setVisibility(View.VISIBLE);");

                            pb.setVisibility(View.VISIBLE);
                            pb.setMax(total_count);
                        }
                    }
                });
            }

            @Override
            public void callback_changes(final String v, final long diff)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ProgressBar pb = findViewById(R.id.pb_page_room_waiting);

                        if (pb != null)
                        {
                            pb.setProgress((int) diff);
                        }

                        //Log.i("TAG36", "TimerXod: " + String.valueOf((int)diff) );

                        TextView tv = (TextView) findViewById(R.id.tv_page_room_waiting_time);

                        if (tv == null)
                        {
                            running = false;
                            return;
                        }

                        tv.setText(v);
                    }
                });
            }

            @Override
            public void callback_end()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // Ато нажатие на кнопки если они видны

                        Button btn1 = (Button) findViewById(R.id.btn_page_room_bito);
                        Button btn2 = (Button) findViewById(R.id.btn_page_room_bery);
                        Button btn3 = (Button) findViewById(R.id.btn_page_room_that_s_all);
                        Button btn4 = (Button) findViewById(R.id.btn_page_room_bito_2);
                        Button btn5 = (Button) findViewById(R.id.btn_page_room_bery_2);
                        Button btn6 = (Button) findViewById(R.id.btn_page_room_that_s_all_2);

                        if (btn1 != null && btn1.getVisibility() == View.VISIBLE)
                        {
                            btn1.performClick();
                            btn1.setPressed(true);
                            btn1.invalidate();
                            btn1.setPressed(false);
                            btn1.invalidate();
                        }
                        else if (btn2 != null && btn2.getVisibility() == View.VISIBLE)
                        {
                            btn2.performClick();
                            btn2.setPressed(true);
                            btn2.invalidate();
                            btn2.setPressed(false);
                            btn2.invalidate();
                        }
                        else if (btn3 != null && btn3.getVisibility() == View.VISIBLE)
                        {
                            btn3.performClick();
                            btn3.setPressed(true);
                            btn3.invalidate();
                            btn3.setPressed(false);
                            btn3.invalidate();
                        }
                        else if (btn4 != null && btn4.getVisibility() == View.VISIBLE)
                        {
                            btn4.performClick();
                            btn4.setPressed(true);
                            btn4.invalidate();
                            btn4.setPressed(false);
                            btn4.invalidate();
                        }
                        else if (btn5 != null && btn5.getVisibility() == View.VISIBLE)
                        {
                            btn5.performClick();
                            btn5.setPressed(true);
                            btn5.invalidate();
                            btn5.setPressed(false);
                            btn5.invalidate();
                        }
                        else if (btn6 != null && btn6.getVisibility() == View.VISIBLE)
                        {
                            btn6.performClick();
                            btn6.setPressed(true);
                            btn6.invalidate();
                            btn6.setPressed(false);
                            btn6.invalidate();
                        }
                        else
                        {
                            network.queue_network.add("OUT_OF_ROOM"
                                    , null
                                    , null
                                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                    , QueueNetwork.TYPE_SEND__FORCE);

                            show_page_menu();
                        }
                    }
                });

            }
        };
        timer_xod.start();
    }

    public void hideKeyboard(View view)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void show_page_first_page()
    {
        sv_page = SV_PAGE_FIRST_PAGE;
        setContentView(R.layout.page_first_page);
        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);
    }

    private void show_page_connection_server()
    {
        /*Log.d("TAG22", android.util.Log.getStackTraceString(new Exception()));

        if(sv_page == SV_PAGE_CONNECTION_SERVER)
        {
            return;
        }*/

        sv_page = SV_PAGE_CONNECTION_SERVER;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_connection_server);

        /*FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_connection_server_root);
        TextView txt1 = (TextView) findViewById(R.id.tv_page_connection_txt1);
        TextView txt2 = (TextView) findViewById(R.id.tv_page_connection_txt2);

        if( app_setting.getTypeSkin() == AppSettings.TYPE_SKIN_DAY )
        {
            fl.setBackgroundColor(Color.parseColor("#dedede"));
            txt1.setTextColor(Color.BLACK);
            txt2.setTextColor(Color.BLACK);
        }
        else
        {
            fl.setBackgroundColor(Color.parseColor("#353535"));
            txt1.setTextColor(Color.WHITE);
            txt2.setTextColor(Color.WHITE);
        }*/

           /*Thread th = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            BackgroundAnimation1 b = new BackgroundAnimation1(MainActivity.this)
                            {

                            };

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

                            FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_connection_server_bk);

                            if( fl != null )
                            {
                                fl.addView(b, params);
                            }
                        }
                    });


                }
            });
            th.start();*/


        /*BackgroundAnimation1 b = new BackgroundAnimation1(MainActivity.this)
        {

        };

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_connection_server_bk);

        if( fl != null )
        {
            fl.addView(b, params);
        }*/

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv = findViewById(R.id.tv_page_connection_server_version_name);

        tv.setText(getString(R.string.txt_75) + " " + BuildConfig.VERSION_NAME);

        //authGoogle();
    }

    private void show_page_create_new_game()
    {
        sv_page = SV_PAGE_CREATE_NEW_GAME;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_create_new_game);

        create_game_current_type = 1;
        create_new_game_count_users = 3;

        SeekBar sb = (SeekBar) findViewById(R.id.sb_page_create_new_game);
        TextView tv = (TextView) findViewById(R.id.tv_page_create_new_game_rate);
        //TextView tv2 = (TextView) findViewById(R.id.tv_page_create_new_game_users_count);

        if (accout_info.balans < 5)
        {
            FrameLayout fl25 = findViewById(R.id.fl_page_create_new_game_no_money);

            fl25.setVisibility(View.VISIBLE);
        }

        if (sb.getMax() + 5 > accout_info.balans)
        {
            sb.setMax((int) accout_info.balans - 5);
        }

        sb.setProgress(15);

        int num = 5 + sb.getProgress();
        tv.setText(String.valueOf(num) + " " + Utils.getCorrectSuffix(num, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                TextView tv = (TextView) findViewById(R.id.tv_page_create_new_game_rate);

                int num = 5 + progress;

                tv.setText(String.valueOf(num) + " " + Utils.getCorrectSuffix(num, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        final TextView tv_page_create_new_game_count_user_2 = (TextView) findViewById(R.id.tv_page_create_new_game_count_user_2);
        final TextView tv_page_create_new_game_count_user_3 = (TextView) findViewById(R.id.tv_page_create_new_game_count_user_3);
        final TextView tv_page_create_new_game_count_user_4 = (TextView) findViewById(R.id.tv_page_create_new_game_count_user_4);
        final TextView tv_page_create_new_game_count_user_5 = (TextView) findViewById(R.id.tv_page_create_new_game_count_user_5);
        final TextView tv_page_create_new_game_count_user_6 = (TextView) findViewById(R.id.tv_page_create_new_game_count_user_6);

        tv_page_create_new_game_count_user_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                create_new_game_count_users = 2;

                tv_page_create_new_game_count_user_2.setBackgroundResource(R.drawable.style_border2);
                tv_page_create_new_game_count_user_3.setBackgroundResource(0);
                tv_page_create_new_game_count_user_4.setBackgroundResource(0);
                tv_page_create_new_game_count_user_5.setBackgroundResource(0);
                tv_page_create_new_game_count_user_6.setBackgroundResource(0);

                tv_page_create_new_game_count_user_2.setTextColor(Color.parseColor("#00ff00"));
                tv_page_create_new_game_count_user_3.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_4.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_5.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_6.setTextColor(Color.parseColor("#cccccc"));

                RadioButton rb_36 = (RadioButton) findViewById(R.id.rb_page_create_new_game_throw_all);
                rb_36.setChecked(true);
            }
        });

        tv_page_create_new_game_count_user_3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                create_new_game_count_users = 3;

                tv_page_create_new_game_count_user_3.setBackgroundResource(R.drawable.style_border2);
                tv_page_create_new_game_count_user_2.setBackgroundResource(0);
                tv_page_create_new_game_count_user_4.setBackgroundResource(0);
                tv_page_create_new_game_count_user_5.setBackgroundResource(0);
                tv_page_create_new_game_count_user_6.setBackgroundResource(0);

                tv_page_create_new_game_count_user_2.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_3.setTextColor(Color.parseColor("#00ff00"));
                tv_page_create_new_game_count_user_4.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_5.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_6.setTextColor(Color.parseColor("#cccccc"));

                RadioButton rb_36 = (RadioButton) findViewById(R.id.rb_page_create_new_game_throw_all);
                rb_36.setChecked(true);
            }
        });

        tv_page_create_new_game_count_user_4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                create_new_game_count_users = 4;

                tv_page_create_new_game_count_user_4.setBackgroundResource(R.drawable.style_border2);
                tv_page_create_new_game_count_user_2.setBackgroundResource(0);
                tv_page_create_new_game_count_user_3.setBackgroundResource(0);
                tv_page_create_new_game_count_user_5.setBackgroundResource(0);
                tv_page_create_new_game_count_user_6.setBackgroundResource(0);

                tv_page_create_new_game_count_user_2.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_3.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_4.setTextColor(Color.parseColor("#00ff00"));
                tv_page_create_new_game_count_user_5.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_6.setTextColor(Color.parseColor("#cccccc"));
            }
        });

        tv_page_create_new_game_count_user_5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                create_new_game_count_users = 5;

                tv_page_create_new_game_count_user_5.setBackgroundResource(R.drawable.style_border2);
                tv_page_create_new_game_count_user_2.setBackgroundResource(0);
                tv_page_create_new_game_count_user_4.setBackgroundResource(0);
                tv_page_create_new_game_count_user_3.setBackgroundResource(0);
                tv_page_create_new_game_count_user_6.setBackgroundResource(0);

                tv_page_create_new_game_count_user_2.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_3.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_4.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_5.setTextColor(Color.parseColor("#00ff00"));
                tv_page_create_new_game_count_user_6.setTextColor(Color.parseColor("#cccccc"));
            }
        });

        tv_page_create_new_game_count_user_6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                create_new_game_count_users = 6;

                tv_page_create_new_game_count_user_6.setBackgroundResource(R.drawable.style_border2);
                tv_page_create_new_game_count_user_2.setBackgroundResource(0);
                tv_page_create_new_game_count_user_4.setBackgroundResource(0);
                tv_page_create_new_game_count_user_5.setBackgroundResource(0);
                tv_page_create_new_game_count_user_3.setBackgroundResource(0);

                tv_page_create_new_game_count_user_2.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_3.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_4.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_5.setTextColor(Color.parseColor("#cccccc"));
                tv_page_create_new_game_count_user_6.setTextColor(Color.parseColor("#00ff00"));
            }
        });

        ui_draw_list_my_friends_create_game(friend_list);


        /*LinearLayout mSeekLin = (LinearLayout) findViewById(R.id.ll_page_create_new_game_seekbar1);
        final CustomSeekBar customSeekBar = new CustomSeekBar(new ContextThemeWrapper(this, R.style.SeekBarColor), 5, Color.RED, 2);
        customSeekBar.addSeekBar(mSeekLin);

        customSeekBar.getSeekBar().setProgress(3);
        ((TextView) customSeekBar.list_tv.get(3)).setTextColor(Color.parseColor("#00ff00"));

        num = 2 + customSeekBar.getSeekBar().getProgress();
        tv2.setText(String.valueOf(num));

        //customSeekBar.getSeekBar().setProgressDrawable( getResources().getDrawable(R.drawable.tprogress_drawable) );
        customSeekBar.getSeekBar().setThumb(getResources().getDrawable(R.drawable.thumb_image));

        customSeekBar.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                TextView tv = (TextView) findViewById(R.id.tv_page_create_new_game_users_count);

                int num = 2 + progress;

                tv.setText(String.valueOf(num) /*+ " " + Utils.getCorrectSuffix(num, "монета", "монеты", "монет") *//*);

                for (int i = 0; i < customSeekBar.list_tv.size(); i++)
                {
                    ((TextView) customSeekBar.list_tv.get(i)).setTextColor(Color.RED);
                }

                ((TextView) customSeekBar.list_tv.get(progress)).setTextColor(Color.parseColor("#00ff00"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });*/

        final LinearLayout t1 = (LinearLayout) findViewById(R.id.ll_page_create_new_game_type_game_1);
        final LinearLayout t2 = (LinearLayout) findViewById(R.id.ll_page_create_new_game_type_game_2);

        final TextView tv_type1 = findViewById(R.id.tv_create_new_game_type_game_1);
        final TextView tv_type2 = findViewById(R.id.tv_create_new_game_type_game_2);

        create_game_current_type = RoomInfo.TYPE_GAME__SIMPLE;

        t1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                t1.setBackgroundResource(R.drawable.style_border);
                t2.setBackgroundResource(0);

                tv_type1.setTextColor(Color.parseColor("#333333"));
                tv_type2.setTextColor(Color.WHITE);

                create_game_current_type = RoomInfo.TYPE_GAME__SIMPLE;
            }
        });

        t2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                t2.setBackgroundResource(R.drawable.style_border);
                t1.setBackgroundResource(0);

                tv_type1.setTextColor(Color.WHITE);
                tv_type2.setTextColor(Color.parseColor("#333333"));

                create_game_current_type = RoomInfo.TYPE_GAME__TRANSFER_FOOL;
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_page_create_new_game_count_cards);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                RadioButton rb_36 = (RadioButton) findViewById(R.id.rb_page_create_new_game_count_cards_36);
                RadioButton rb_52 = (RadioButton) findViewById(R.id.rb_page_create_new_game_count_cards_52);

                rb_36.setTextColor(Color.parseColor("#ffffff"));
                rb_52.setTextColor(Color.parseColor("#ffffff"));

                RadioButton rb_i = (RadioButton) findViewById(i);
                rb_i.setTextColor(Color.GREEN);
            }
        });

        rg = (RadioGroup) findViewById(R.id.rb_page_create_new_game_throw);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                RadioButton rb_36 = (RadioButton) findViewById(R.id.rb_page_create_new_game_throw_all);
                RadioButton rb_52 = (RadioButton) findViewById(R.id.rb_page_create_new_game_throw_s);

                rb_36.setTextColor(Color.parseColor("#ffffff"));
                rb_52.setTextColor(Color.parseColor("#ffffff"));

                if (create_new_game_count_users >= 4)
                {
                    RadioButton rb_i = (RadioButton) findViewById(i);
                    rb_i.setTextColor(Color.GREEN);
                }
                else
                {
                    rb_36.setChecked(true);
                }
            }
        });

        CheckBox cb_friends_in_game = (CheckBox) findViewById(R.id.cb_page_create_new_game_friends_in_game);
        cb_friends_in_game.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                LinearLayout ll = (LinearLayout) findViewById(R.id.ll_page_create_new_game_friends_list);

                if (ll == null)
                {
                    return;
                }

                SeekBar sb = (SeekBar) findViewById(R.id.sb_page_create_new_game);
                TextView tv = (TextView) findViewById(R.id.tv_page_create_new_game_rate);

                sb.setEnabled(!b);

                if (b)
                {
                    CheckBox cb_game_with_bots = (CheckBox) findViewById(R.id.cb_page_create_new_game_with_bots);
                    cb_game_with_bots.setChecked(false);

                    int num = 0;

                    tv.setText(String.valueOf(num) + " " + Utils.getCorrectSuffix(num, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));
                    ll.setVisibility(View.VISIBLE);
                }
                else
                {
                    int num = 5 + sb.getProgress();

                    tv.setText(String.valueOf(num) + " " + Utils.getCorrectSuffix(num, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));

                    ll.setVisibility(View.GONE);
                }

                final ScrollView scroll = (ScrollView) findViewById(R.id.sv_page_create_new_game);

                scroll.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        scroll.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });

        CheckBox cb_have_password = (CheckBox) findViewById(R.id.cb_page_create_new_game_have_password);
        cb_have_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                LinearLayout ll = (LinearLayout) findViewById(R.id.ll_page_create_new_game_password_field);
                final EditText et = (EditText) findViewById(R.id.et_page_create_new_game_password_values);
                final ScrollView sv = (ScrollView) findViewById(R.id.sv_page_create_new_game);

                if (isChecked)
                {
                    CheckBox cb_game_with_bots = (CheckBox) findViewById(R.id.cb_page_create_new_game_with_bots);
                    cb_game_with_bots.setChecked(false);

                    ll.setVisibility(View.VISIBLE);
                    et.setEnabled(true);

                    sv.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            et.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);

                            sv.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                else
                {
                    ll.setVisibility(View.GONE);
                    et.setEnabled(false);
                    et.setText("");
                }
            }
        });

        CheckBox cb_game_with_bots = (CheckBox) findViewById(R.id.cb_page_create_new_game_with_bots);
        cb_game_with_bots.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox cb_friends_in_game = (CheckBox) findViewById(R.id.cb_page_create_new_game_friends_in_game);
                CheckBox cb_have_password = (CheckBox) findViewById(R.id.cb_page_create_new_game_have_password);

                if ((cb_friends_in_game.isChecked() || cb_have_password.isChecked()) && isChecked)
                {
                    cb_friends_in_game.setChecked(false);
                    cb_have_password.setChecked(false);
                }
            }
        });

        Button btn1 = (Button) findViewById(R.id.btn_page_create_new_game_run);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_create_new_game_wait);
                TextView txt_error = (TextView) findViewById(R.id.tv_page_create_new_game_error_form);

                if (fl == null)
                {
                    return;
                }

                fl.setVisibility(View.VISIBLE);
                txt_error.setVisibility(View.GONE);

                SeekBar sb = (SeekBar) findViewById(R.id.sb_page_create_new_game);

                //LinearLayout mSeekLin = (LinearLayout) findViewById(R.id.ll_page_create_new_game_seekbar1);
                //SeekBar customSeekBar = (SeekBar) mSeekLin.getChildAt(0);

                RadioGroup rg = (RadioGroup) findViewById(R.id.rg_page_create_new_game_count_cards);
                int checked_rb_id = rg.getCheckedRadioButtonId();
                int count_cards_in_desc = 0;

                if (checked_rb_id == R.id.rb_page_create_new_game_count_cards_36)
                {
                    count_cards_in_desc = 36;
                }
                else if (checked_rb_id == R.id.rb_page_create_new_game_count_cards_52)
                {
                    count_cards_in_desc = 52;
                }

                rg = (RadioGroup) findViewById(R.id.rb_page_create_new_game_throw);
                checked_rb_id = rg.getCheckedRadioButtonId();
                int toss_type = RoomInfo.TOSS_ALL;

                if (checked_rb_id == R.id.rb_page_create_new_game_throw_all)
                {
                    toss_type = RoomInfo.TOSS_ALL;
                }
                else if (checked_rb_id == R.id.rb_page_create_new_game_throw_s)
                {
                    toss_type = RoomInfo.TOSS_NEAREST;
                }

                CheckBox ch_have_password = (CheckBox) findViewById(R.id.cb_page_create_new_game_have_password);
                CheckBox ch_friends_in_game = (CheckBox) findViewById(R.id.cb_page_create_new_game_friends_in_game);
                CheckBox ch_with_bots = (CheckBox) findViewById(R.id.cb_page_create_new_game_with_bots);
                String password = "";

                if (ch_have_password.isChecked())
                {
                    EditText et = (EditText) findViewById(R.id.et_page_create_new_game_password_values);
                    password = et.getText().toString().trim();
                }

                int have_password = (ch_have_password.isChecked() && password.length() > 0) ? 1 : 0;
                int with_bots = (ch_with_bots.isChecked()) ? 1 : 0;
                int friends_in_game = (ch_friends_in_game.isChecked()) ? 1 : 0;

                Utils.hideKeyboard(MainActivity.this);

                List<Integer> list_uids_friends = new ArrayList<>();

                if (ch_friends_in_game.isChecked())
                {
                    for (int i = 0; i < list_checkbox.size(); i++)
                    {
                        CheckBox ch_item = list_checkbox.get(i);

                        if (ch_item.isChecked())
                        {
                            list_uids_friends.add((Integer) ch_item.getTag());
                        }
                    }

                    if (list_uids_friends.size() == 0)
                    {
                        txt_error.setVisibility(View.VISIBLE);
                        txt_error.setText(getString(R.string.txt_76));
                        fl.setVisibility(View.GONE);

                        return;
                    }

                    if (list_uids_friends.size() < create_new_game_count_users - 1)
                    {
                        txt_error.setVisibility(View.VISIBLE);
                        txt_error.setText(getString(R.string.txt_77));
                        fl.setVisibility(View.GONE);

                        return;
                    }
                    else if (list_uids_friends.size() > create_new_game_count_users - 1)
                    {
                        txt_error.setVisibility(View.VISIBLE);
                        txt_error.setText(getString(R.string.txt_78));
                        fl.setVisibility(View.GONE);

                        return;
                    }

                }
                else if (accout_info.balans < 5)
                {
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText(getString(R.string.txt_79));
                    fl.setVisibility(View.GONE);

                    return;
                }

                if (have_password == 1 && password.length() < 3)
                {
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText(getString(R.string.txt_90));
                    fl.setVisibility(View.GONE);

                    return;
                }

                //fl.setVisibility(View.VISIBLE);
                ArrayList<String> params = new ArrayList<String>();

                params.add(String.valueOf(accout_info.uid));
                params.add(String.valueOf(session_info.session_socket_id));
                params.add(String.valueOf(sb.getProgress() + 5));            // ставка
                params.add(String.valueOf(create_new_game_count_users));     // сколько игроков
                params.add(String.valueOf(count_cards_in_desc));             // 36 52
                params.add(String.valueOf(create_game_current_type));        // тип игры
                params.add(String.valueOf(toss_type));                          // подкидывают
                params.add(String.valueOf(have_password));                      //
                params.add(String.valueOf(with_bots));                          //
                params.add(String.valueOf(password));                           //
                params.add(String.valueOf(friends_in_game));                    //
                params.add(TextUtils.join("|", list_uids_friends));  //

                network.queue_network.add(getString(R.string.cmd_3)
                        , params
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__LONG);

            }
        });

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv_title = findViewById(R.id.tv_page_create_new_game_title);
        tv_title.setTypeface(_fontApp_bold2);
    }

    private void show_page_privacy_policy()
    {
        sv_page = SV_PAGE_PRIVACY_POLICY;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_privacy_policy);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        WebView view = findViewById(R.id.wv_page_privacy_policy_data);

        view.setBackgroundColor(Color.TRANSPARENT);
        view.clearCache(true);
        view.setWebViewClient(new WebViewClient()
        {

            public void onPageFinished(WebView view, String url)
            {

                FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_rules_wait);

                if (fl != null)
                {
                    fl.setVisibility(View.GONE);
                }
            }
        });

        view.getSettings().setSupportZoom(false);

        String HTML = "";

        if (Locale.getDefault().getLanguage().equalsIgnoreCase("RU"))
        {
            HTML = readTextFromResource(R.raw.privacy_policy);
        }
        else
        {
            HTML = readTextFromResource(R.raw.privacy_policy_en);
        }

        view.loadDataWithBaseURL("file:///android_res/drawable/", HTML, "text/html", "UTF-8", null);
    }

    private void show_add_money_rulet(int count_money)
    {
        TextView tv = findViewById(R.id.tv_page_rulet_result_v);
        Button btn = findViewById(R.id.page_rulet_btn_again_run_rulet);

        if (tv != null)
        {
            if (count_money > 0)
            {
                int money = count_money;

                tv.setText(String.valueOf(money) + " " + Utils.getCorrectSuffix(money, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));

                accout_info.balans += money;

                ArrayList<String> params = new ArrayList<String>();

                params.add(String.valueOf(accout_info.uid));
                params.add(String.valueOf(session_info.session_socket_id));
                params.add(String.valueOf(money));

                network.queue_network.add(JniApi.k1()
                        , params
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__LONG);
            }
            else
            {
                //tv.setText(getString(R.string.txt_44));
                tv.setText("");
            }

            btn.setVisibility(View.VISIBLE);
        }
    }

    private void show_page_rulet()
    {
        sv_page = SV_PAGE_RULET;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_rulet);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv = findViewById(R.id.tv_page_rulet_title);
        tv.setTypeface(_fontApp_bold2);

        /*TextView tv0 = findViewById(R.id.tv_page_rulet_result_v);

        if( tv0 != null )
        {
            tv0.setText("--");
        }*/

        FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_rulet_canvas);
        fl.removeAllViews();

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        _draw_rulet = new RuletSurfaceView(MainActivity.this)
        {
            @Override
            public void callback_money_rulet(final int count_money)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        show_add_money_rulet(count_money);
                    }
                });
            }

            @Override
            public void callback_off()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        show_page_add_balans();
                    }
                });
            }

            @Override
            public void callback_end_rotate()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        FrameLayout fl = MainActivity.this.findViewById(R.id.fl_page_rulet_bk_main);
                        if( fl != null )
                        {
                            fl.setBackgroundColor(Color.parseColor("#cd0a00"));
                        }
                    }
                });
            }
        };

        _draw_rulet.eneble_sound = app_setting.opt_sound_in_game;
        _draw_rulet.eneble_vibrate = app_setting.opt_vibration;
        _draw_rulet.setZOrderOnTop(true);
        SurfaceHolder sfhTrack = _draw_rulet.getHolder();
        sfhTrack.setFormat(PixelFormat.TRANSPARENT);

        fl.addView(_draw_rulet, lp);


        Button btn = findViewById(R.id.page_rulet_btn_again_run_rulet);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (_draw_rulet != null)
                {
                    view.setVisibility(View.GONE);

                    FrameLayout fl = findViewById(R.id.fl_page_rulet_bk_main);
                    if( fl != null )
                    {
                        fl.setBackgroundResource(R.drawable.tile_background);
                    }

                    TextView tv = findViewById(R.id.tv_page_rulet_result_v);
                    tv.setText("");

                    _draw_rulet.startRuletAnain();
                }
            }
        });
    }

    private void show_page_shop()
    {
        sv_page = SV_PAGE_SHOP;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_shop);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv = findViewById(R.id.tv_page_table_shop_title);
        tv.setTypeface(_fontApp_bold2);

        ImageView iv_loading = findViewById(R.id.iv_page_shop_loading);
        iv_loading.setAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_indefinitely));

        LinearLayout ll1 = findViewById(R.id.ll_page_shop_top_menu_1);
        LinearLayout ll2 = findViewById(R.id.ll_page_shop_top_menu_2);

        ll1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_page_shop_set_active_menu(1);
            }
        });

        ll2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_page_shop_set_active_menu(2);
            }
        });

        FrameLayout fl1 = findViewById(R.id.fl_page_shop_page_1);
        FrameLayout fl2 = findViewById(R.id.fl_page_shop_page_2);

        Shop shop = new Shop(this);

        fl1.addView(shop.drawUiPage1(app_setting.mylist_purchased_goods));
        fl2.addView(shop.drawUiPage2(app_setting.mylist_purchased_goods));

        tv = findViewById(R.id.tv_page_shop_my_balans);
        if (tv != null)
        {
            tv.setText(String.valueOf(accout_info.balans) + " " + Utils.getCorrectSuffix(accout_info.balans, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));
        }

        if (shop.page1_draw_empty)
        {
            ui_page_shop_set_active_menu(2);
        }
    }

    private void show_page_table_rating()
    {
        sv_page = SV_PAGE_TABLE_RATING;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_table_rating);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv = findViewById(R.id.tv_page_table_rating_title);
        tv.setTypeface(_fontApp_bold2);

        ui_draw_list_rating(list_rating);

        FrameLayout fl = findViewById(R.id.fl_page_table_rating_user_info_close);
        fl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FrameLayout fl = findViewById(R.id.fl_table_rating_user_info);
                fl.setVisibility(View.GONE);
            }
        });
    }

    private void show_page_auth()
    {
        sv_page = SV_PAGE_AUTH;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_auth);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        //click_sound = MediaPlayer.create(this, R.raw.click);

        TextView tv = (TextView) findViewById(R.id.tv_page_auth_footer_text);
        tv.setText(Html.fromHtml(getString(R.string.txt_80)));
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_privacy_policy();
            }
        });

        Button btn = (Button) findViewById(R.id.btn_page_auth_run_input);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*if(click_sound != null)
                {
                    click_sound.start();
                }*/

                signIn();
            }
        });
    }

    private void show_page_settings()
    {
        sv_page = SV_PAGE_SETTINGS;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_settings);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv_title = findViewById(R.id.tv_page_settings_title);
        tv_title.setTypeface(_fontApp_bold2);

        CheckBox sound_in_game = findViewById(R.id.cb_page_settings_sound_in_game);
        CheckBox sound_ready_play = findViewById(R.id.cb_page_settings_sound_ready_play);
        CheckBox vibration = findViewById(R.id.cb_page_settings_vibration);
        CheckBox hints_toss_beat = findViewById(R.id.cb_page_settings_show_hints_toss_beat);

        sound_in_game.setChecked(app_setting.opt_sound_in_game);
        sound_in_game.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                app_setting.opt_sound_in_game = b;

                sendSaveOption(AppSettings.TYPE_OPT__SOUND_IN_GAME, String.valueOf(b ? 1 : 0));
            }
        });

        sound_ready_play.setChecked(app_setting.opt_sound_ready_stroke);
        sound_ready_play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                app_setting.opt_sound_ready_stroke = b;

                sendSaveOption(AppSettings.TYPE_OPT__SOUND_READY_STROKE, String.valueOf(b ? 1 : 0));
            }
        });

        vibration.setChecked(app_setting.opt_vibration);
        vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                app_setting.opt_vibration = b;

                sendSaveOption(AppSettings.TYPE_OPT__VIBRATION, String.valueOf(b ? 1 : 0));
            }
        });

        hints_toss_beat.setChecked(app_setting.opt_hints_cards);
        hints_toss_beat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                app_setting.opt_hints_cards = b;

                sendSaveOption(AppSettings.TYPE_OPT__HINTS_CARDS, String.valueOf(b ? 1 : 0));
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.page_settings_sort_my_carts);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                //Log.d("chk", "id" + checkedId);
                int value = 0;

                if (checkedId == R.id.page_settings_sort_my_carts_off)
                {
                    value = AppSettings.SORT_MY_CARDS_OFF;
                }
                else if (checkedId == R.id.page_settings_sort_my_carts_abc)
                {
                    value = AppSettings.SORT_MY_CARDS_ABC;
                }
                else if (checkedId == R.id.page_settings_sort_my_carts_desc)
                {
                    value = AppSettings.SORT_MY_CARDS_DESC;
                }

                app_setting.opt_sort_my_cards = value;
                sendSaveOption(AppSettings.TYPE_OPT__SORT_MY_CARDS, String.valueOf(value));
            }
        });

        RadioGroup rg2 = (RadioGroup) findViewById(R.id.page_settings_sort_trump_cards);

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                //Log.d("chk", "id" + checkedId);
                int value = 0;

                if (checkedId == R.id.page_settings_sort_trump_cards_left)
                {
                    value = AppSettings.SORT_TRUMP_CARDS_LEFT;
                }
                else if (checkedId == R.id.page_settings_sort_trump_cards_right)
                {
                    value = AppSettings.SORT_TRUMP_CARDS_RIGHT;
                }
                else if (checkedId == R.id.page_settings_sort_trump_cards_off)
                {
                    value = AppSettings.SORT_TRUMP_CARDS_OFF;
                }

                app_setting.opt_sort_trump_cards = value;
                sendSaveOption(AppSettings.TYPE_OPT__SORT_TRUMP_CARDS, String.valueOf(value));
            }
        });

        /*int typeSkin = app_setting.getTypeSkin();

        if( typeSkin == AppSettings.TYPE_SKIN_DAY )
        {
            rb_day.setChecked(true);
        }
        else
        {
            rb_night.setChecked(true);
        }*/

        int typeSortCard = app_setting.opt_sort_my_cards;

        if (typeSortCard == AppSettings.SORT_MY_CARDS_OFF)
        {
            RadioButton rb = (RadioButton) findViewById(R.id.page_settings_sort_my_carts_off);
            rb.setChecked(true);
        }
        else if (typeSortCard == AppSettings.SORT_MY_CARDS_ABC)
        {
            RadioButton rb = (RadioButton) findViewById(R.id.page_settings_sort_my_carts_abc);
            rb.setChecked(true);
        }
        else
        {
            RadioButton rb = (RadioButton) findViewById(R.id.page_settings_sort_my_carts_desc);
            rb.setChecked(true);
        }

        int sort_trump_cards = app_setting.opt_sort_trump_cards;

        if (sort_trump_cards == AppSettings.SORT_TRUMP_CARDS_LEFT)
        {
            RadioButton rb = (RadioButton) findViewById(R.id.page_settings_sort_trump_cards_left);
            rb.setChecked(true);
        }
        else if (sort_trump_cards == AppSettings.SORT_TRUMP_CARDS_RIGHT)
        {
            RadioButton rb = (RadioButton) findViewById(R.id.page_settings_sort_trump_cards_right);
            rb.setChecked(true);
        }
        else
        {
            RadioButton rb = (RadioButton) findViewById(R.id.page_settings_sort_trump_cards_off);
            rb.setChecked(true);
        }

        /*LinearLayout ll_page_settings_backcard_type_1 = findViewById(R.id.ll_page_settings_backcard_type_1);
        LinearLayout ll_page_settings_backcard_type_2 = findViewById(R.id.ll_page_settings_backcard_type_2);
        LinearLayout ll_page_settings_backcard_type_3 = findViewById(R.id.ll_page_settings_backcard_type_3);
        LinearLayout ll_page_settings_backcard_type_4 = findViewById(R.id.ll_page_settings_backcard_type_4);
        LinearLayout ll_page_settings_backcard_type_5 = findViewById(R.id.ll_page_settings_backcard_type_5);

        ll_page_settings_backcard_type_1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_settongs_set_backcardtype(1);
            }
        });

        ll_page_settings_backcard_type_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_settongs_set_backcardtype(2);
            }
        });

        ll_page_settings_backcard_type_3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_settongs_set_backcardtype(3);
            }
        });

        ll_page_settings_backcard_type_4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_settongs_set_backcardtype(4);
            }
        });

        ll_page_settings_backcard_type_5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_settongs_set_backcardtype(5);
            }
        });

        ui_settongs_set_backcardtype(app_setting.type_backcard);

        LinearLayout ll_page_settings_cardtype_type_1 = findViewById(R.id.ll_page_settings_cardtype_type_1);
        LinearLayout ll_page_settings_cardtype_type_2 = findViewById(R.id.ll_page_settings_cardtype_type_2);
        LinearLayout ll_page_settings_cardtype_type_3 = findViewById(R.id.ll_page_settings_cardtype_type_3);

        ll_page_settings_cardtype_type_1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_settongs_set_cardtype(1);
            }
        });

        ll_page_settings_cardtype_type_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_settongs_set_cardtype(2);
            }
        });

        ll_page_settings_cardtype_type_3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ui_settongs_set_cardtype(3);
            }
        });

        ui_settongs_set_cardtype(app_setting.type_cardsstyle);*/
    }

    private void show_page_search()
    {
        sv_page = SV_PAGE_SEARCH;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_search);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv_title = findViewById(R.id.tv_page_search_title);
        tv_title.setTypeface(_fontApp_bold2);

        UiPageSearchShowHideWait(true);

        Thread th = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    SearchRooms fragment = (SearchRooms) fm.findFragmentById(R.id.page_search_main);

                    if (fragment != null)
                    {
                        fragment.runThreadSearch();
                    }
                    else
                    {
                        return;
                    }

                    try
                    {
                        Thread.sleep(3000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        th.start();

    }

    private void show_page_search_users()
    {
        sv_page = SV_PAGE_SEARCH_USERS;
        setContentView(R.layout.page_search_friends);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv_title = findViewById(R.id.tv_page_search_friends_top_title);
        tv_title.setTypeface(_fontApp_bold2);

        Button btn = findViewById(R.id.btn_page_search_frineds_run_search);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText et = findViewById(R.id.et_page_search_friends_search_txt);
                String v = et.getText().toString().trim();

                if (v.length() < 3)
                {
                    et.setBackgroundResource(R.drawable.style_edittext_3);
                    return;
                }

                et.setBackgroundResource(R.drawable.style_edittext_2);

                ArrayList<String> params = new ArrayList<String>();

                params.add(String.valueOf(accout_info.uid));
                params.add(String.valueOf(session_info.session_socket_id));
                params.add(v);

                network.queue_network.add("SEARCH_FRIENDS"
                        , params
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__LONG);

                Utils.hideKeyboard(MainActivity.this);

                FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_search_friends_wait_loading);
                fl.setVisibility(View.VISIBLE);
            }
        });
    }

    private void show_page_friends()
    {
        sv_page = SV_PAGE_FRIENDS;
        setContentView(R.layout.page_friends);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv_title = findViewById(R.id.tv_page_friends_top_title);
        tv_title.setTypeface(_fontApp_bold2);

        ui_draw_list_my_friends(friend_list);

        LinearLayout ll = findViewById(R.id.ll_page_friends_search_users);

        ll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_search_users();
            }
        });

        TextView tv_0 = (TextView) findViewById(R.id.tv_page_friends_alert_dialog_mybalans);

        tv_0.setText(getString(R.string.txt_41) + ": " + String.valueOf(accout_info.balans) + " " + Utils.getCorrectSuffix(accout_info.balans, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));

        TextView tv1 = findViewById(R.id.tv_page_friends_alert_dialog_cancel);
        tv1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FrameLayout fl = findViewById(R.id.fl_page_friends_send_money);
                fl.setVisibility(View.GONE);

                EditText et = findViewById(R.id.et_page_friends_alert_dialog_password);
                hideKeyboard(et);
            }
        });

        TextView tv2 = findViewById(R.id.tv_page_friends_alert_dialog_ok);
        tv2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FrameLayout fl = findViewById(R.id.fl_page_friends_send_money);
                fl.setVisibility(View.GONE);

                EditText et = findViewById(R.id.et_page_friends_alert_dialog_password);
                hideKeyboard(et);

                Integer sum = Integer.parseInt(et.getText().toString());

                if (sum > 0)
                {
                    ArrayList<String> params = new ArrayList<String>();

                    params.add(String.valueOf(accout_info.uid));
                    params.add(String.valueOf(session_info.session_socket_id));
                    params.add(String.valueOf(uid_page_friends_selected_send_money));
                    params.add(String.valueOf(sum));

                    network.queue_network.add(getString(R.string.cmd_4)
                            , params
                            , null
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__LONG);
                }
            }
        });

        FrameLayout fl = findViewById(R.id.fl_page_friends_user_info_close);
        fl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FrameLayout fl = findViewById(R.id.fl_page_friends_user_info);
                fl.setVisibility(View.GONE);
            }
        });
    }

    private void show_page_add_balans()
    {
        sv_page = SV_PAGE_ADD_BALANS;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_add_banals);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        if (_draw_rulet != null)
        {
            _draw_rulet._stop_thread();
            _draw_rulet = null;
        }

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv = findViewById(R.id.tv_page_add_balans_money);

        tv.setText(String.valueOf(accout_info.balans) + " " + Utils.getCorrectSuffix(accout_info.balans, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));

        tv = findViewById(R.id.tv_page_add_balans_title);
        tv.setTypeface(_fontApp_bold2);

        LinearLayout ll = findViewById(R.id.ll_page_add_balans_btn_rulet);

        ll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                show_page_rulet();
            }
        });

        ll = findViewById(R.id.ll_page_add_balans_btn_video);

        ll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mRewardedVideoAd.isLoaded())
                {
                    onRewardedVideoAd = false;
                    mRewardedVideoAd.show();
                }

                /*if(UnityAds.isReady("rewardedVideo")){ //Make sure a video is available & the placement is valid.
                    UnityAds.show(MainActivity.this, "rewardedVideo");
                }*/

                /*if (Vungle.canPlayAd("DEFAULT-3981593"))
                {
                    Vungle.playAd("DEFAULT-3981593", null, new PlayAdCallback() {
                        @Override
                        public void onAdStart(String placementReferenceId)
                        {
                            Toast.makeText(MainActivity.this, "onAdStart", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAdEnd(String placementReferenceId, boolean completed, boolean isCTAClicked)
                        {
                            Toast.makeText(MainActivity.this, "onAdEnd", Toast.LENGTH_LONG).show();

                            ArrayList<String> params = new ArrayList<String>();

                            params.add(String.valueOf(accout_info.uid));
                            params.add(String.valueOf(session_info.session_socket_id));
                            params.add(String.valueOf(10 + 100 + 90));

                            network.queue_network.add(JniApi.k1()
                                    , params
                                    , null
                                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                    , QueueNetwork.TYPE_SEND__LONG);

                            WaitThread wt = new WaitThread(800)
                            {
                                @Override
                                public void callback()
                                {
                                    getMyBalans();
                                }
                            };
                            wt.start();

                            if( ! showed_salute )
                            {
                                showed_salute = true;
                                runAnimationAfterVideoAd();
                            }
                            else
                            {
                                if (app_setting.opt_sound_in_game && cachbox != null) {
                                    cachbox.start();
                                }
                            }
                        }


                        @Override
                        public void onError(String placementReferenceId, Throwable throwable) {
                            // Play ad error occurred - throwable.getLocalizedMessage() contains error message

                            Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_LONG).show();
                        }
                    });
                };*/

                /**
                 * Load rewarded by specifying AdMode.REWARDED
                 * We are using AdEventListener to trigger ad show
                 */
                /*startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {

                    @Override
                    public void onReceiveAd(Ad arg0)
                    {
                        startAppAd.showAd();
                    }

                    @Override
                    public void onFailedToReceiveAd(Ad arg0) {

                         // Failed to load rewarded video:
                         // 1. Check that FullScreenActivity is declared in AndroidManifest.xml:
                         // See https://github.com/StartApp-SDK/Documentation/wiki/Android-InApp-Documentation#activities
                         // 2. Is android API level above 16?
                         //
                        Log.e("MainActivity", "Failed to load rewarded video with reason: " + arg0.getErrorMessage());
                    }
                });*/

                //final StartAppAd rewardedVideo = new StartAppAd(MainActivity.this);

                /**
                 * This is very important: set the video listener to be triggered after video
                 * has finished playing completely
                 */
                /*rewardedVideo.setVideoListener(new VideoListener()
                {

                    @Override
                    public void onVideoCompleted()
                    {
                        //Toast.makeText(MainActivity.this, "Rewarded video has completed - grant the user his reward", Toast.LENGTH_LONG).show();

                        ArrayList<String> params = new ArrayList<String>();

                        params.add(String.valueOf(accout_info.uid));
                        params.add(String.valueOf(session_info.session_socket_id));
                        params.add(String.valueOf(10 + 100 + 90));

                        network.queue_network.add(JniApi.k1()
                                , params
                                , null
                                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                , QueueNetwork.TYPE_SEND__LONG);

                        WaitThread wt = new WaitThread(800)
                        {
                            @Override
                            public void callback()
                            {
                                getMyBalans();
                            }
                        };
                        wt.start();

                        if (!showed_salute)
                        {
                            showed_salute = true;
                            runAnimationAfterVideoAd();
                        }
                        else if (app_setting.opt_sound_in_game && cachbox != null && ! call_phone)
                        {
                            cachbox.start();
                        }
                    }
                });*/

                /**
                 * Load rewarded by specifying AdMode.REWARDED
                 * We are using AdEventListener to trigger ad show
                 */
                /*rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener()
                {
                    @Override
                    public void onReceiveAd(Ad arg0)
                    {
                        rewardedVideo.showAd();
                    }

                    @Override
                    public void onFailedToReceiveAd(Ad arg0)
                    {
                        /**
                         * Failed to load rewarded video:
                         * 1. Check that FullScreenActivity is declared in AndroidManifest.xml:
                         * See https://github.com/StartApp-SDK/Documentation/wiki/Android-InApp-Documentation#activities
                         * 2. Is android API level above 16?
                         *//*
                        //Log.e("MainActivity", "Failed to load rewarded video with reason: " + arg0.getErrorMessage());
                    }
                });*/
            }
        });

    }

    private void show_page_rules()
    {
        sv_page = SV_PAGE_RULES;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_rules);




        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        TextView tv_title = findViewById(R.id.tv_page_rules_title);
        tv_title.setTypeface(_fontApp_bold2);

        WebView view = (WebView) findViewById(R.id.wv_page_rules_content);
        view.setBackgroundColor(Color.TRANSPARENT);
        view.clearCache(true);
        view.setWebViewClient(new WebViewClient()
        {

            public void onPageFinished(WebView view, String url)
            {
                FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_rules_wait);

                if (fl != null)
                {
                    fl.setVisibility(View.GONE);
                }
            }
        });

        view.getSettings().setSupportZoom(false);

        String HTML = readTextFromResource(R.raw.info);

        view.loadDataWithBaseURL("file:///android_res/drawable/", HTML, "text/html", "UTF-8", null);
    }

    private void show_page_room(
            long room_id
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
            , int current_pos_user_xod
            , int current_pos_user_beat
            , int current_pos_user_main_xod
    )
    {
        sv_page = SV_PAGE_ROOM;
        removeAllOldFragmentsThisApp();
        setContentView(R.layout.page_room);

        page_room_request_to_friends = new ArrayList<>();

        FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_room_canvas);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        /*Button b1 = (Button) findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //animation_fly_card_off_board = false

                if( _draw_task == null ) { return; }

                List<CardDrawInfo> l = new ArrayList<>();
                CardDrawInfo cdi = _draw_task.list_cards_draw.get(Cards.BLANK);

                for (int g = 0; g < _draw_task.getRoomInfo().cards_boards.size(); g++)
                {
                    if ( ! _draw_task.getRoomInfo().cards_boards.get(g).need_draw1)
                    {
                        continue;
                    }

                    if( _draw_task.getRoomInfo().cards_boards.get(g).c1 != null )
                    {
                        _draw_task.getRoomInfo().cards_boards.get(g).c1.img = cdi.img;
                        l.add( _draw_task.getRoomInfo().cards_boards.get(g).c1 );
                    }

                    if( _draw_task.getRoomInfo().cards_boards.get(g).c2 != null )
                    {
                        _draw_task.getRoomInfo().cards_boards.get(g).c2.img = cdi.img;
                        l.add( _draw_task.getRoomInfo().cards_boards.get(g).c2 );
                    }
                }

                _draw_task.fly_card_off_board = new AnimationFlyCardsOutOfBoard(20, 0, 0, l);

                _draw_task.animation_fly_card_off_board = true;
            }
        });*/

        FrameLayout fl0 = (FrameLayout) findViewById(R.id.fl_page_room_user_info_close);

        if (fl0 != null)
        {
            fl0.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_room_user_info);

                    if (fl != null)
                    {
                        fl.setVisibility(View.GONE);
                    }

                    if (_draw_task != null)
                    {
                        _draw_task.resetSelectUsers();
                    }

                    _draw_task.setZOrderOnTop(true);
                    //_draw_task.drawBkImg = false;

                    _draw_task.needRebuildBackground = true;
                }
            });
        }

        LinearLayout btn_add_friends = (LinearLayout) findViewById(R.id.btn_page_room_add_friend);
        /*btn_add_friends.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (room_page_oppened_user_info == 0)
                {
                    return;
                }

                ArrayList<String> params = new ArrayList<String>();

                params.add(String.valueOf(accout_info.uid));
                params.add(String.valueOf(session_info.session_socket_id));
                params.add(String.valueOf(room_page_oppened_user_info));

                network.queue_network.add(getString(R.string.cmd_10)
                        , params
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__LONG);

                LinearLayout wait_icon = (LinearLayout) findViewById(R.id.ll_page_room_wait_add_friend);
                wait_icon.setVisibility(View.VISIBLE);

                view.setVisibility(View.GONE);

            }
        });*/

        //ImageView wait_icon = (ImageView) findViewById(R.id.iv_page_room_wait_add_friends);
        //wait_icon.setVisibility(View.VISIBLE);

        System.gc();

        _draw_task = new RoomSurfaceView(
                MainActivity.this
                , room_id
                , rate
                , count_users
                , count_cards
                , type_game
                , toss
                , have_password
                , current_my_position
                , trump
                , count_cards_in_desck
                , cards_users
                , app_setting.opt_hints_cards
                , app_setting.opt_type_backcard
                , app_setting.opt_type_cardsstyle
        )
        {
            @Override
            public void callbackInit()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TextView iv = MainActivity.this.findViewById(R.id.tv_page_room_loading);

                        if (iv != null)
                        {
                            iv.setVisibility(GONE);
                        }
                    }
                });
            }

            @Override
            public void toss_xod_transfer_foll(int card, int place_the_cards_in_my_hand)
            {
                _draw_task.available_select_my_card = false;

                // надо проверить ход на сервере если все хорошо, передать ход следующему кто подкидывает
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try
                {
                    buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(card)).array());
                    buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(place_the_cards_in_my_hand)).array());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                // подкинем карту с переводом хода!
                network.queue_network.add(getString(R.string.cmd_5)
                        , null
                        , buffer.toByteArray()
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__FORCE);

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_hide_all_btns();
                        ui_stop_timer_waiting_xod();
                    }
                });
            }

            @Override
            public void toss_xod(int card, int place_the_cards_in_my_hand)
            {
                _draw_task.available_select_my_card = false;
                _draw_task.resetHoveredCardSelected();

                // надо проверить ход на сервере если все хорошо, передать ход следующему кто подкидывает
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try
                {
                    buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(card)).array());
                    buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(place_the_cards_in_my_hand)).array());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                if (_draw_task.mode_toss2 == 1)
                {
                    // подкинем карту
                    network.queue_network.add(getString(R.string.cmd_6)
                            , null
                            , buffer.toByteArray()
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__FORCE);
                }
                else if (_draw_task.mode_toss2 == 2)
                {
                    // подкинем карту
                    network.queue_network.add(getString(R.string.cmd_7)
                            , null
                            , buffer.toByteArray()
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__FORCE);
                }
                else
                {
                    // ход
                    network.queue_network.add(getString(R.string.cmd_8)
                            , null
                            , buffer.toByteArray()
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__FORCE);

                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_hide_all_btns();
                        ui_stop_timer_waiting_xod();
                    }
                });
            }

            @Override
            public void beat_xod(int card, int place_the_cards_in_my_hand, int card_hovered)
            {
                _draw_task.available_select_my_card = false;
                _draw_task.resetHoveredCardSelected();

                // надо проверить ход на сервере если все хорошо, передать ход следующему кто подкидывает
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try
                {
                    buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(card)).array());
                    buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(place_the_cards_in_my_hand)).array());
                    buffer.write(ByteBuffer.allocate(4).putInt(ProtokolUtils.htonl(card_hovered)).array());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                // подкинем карту
                network.queue_network.add(getString(R.string.cmd_9)
                        , null
                        , buffer.toByteArray()
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__FORCE);

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_hide_all_btns();
                        ui_stop_timer_waiting_xod();
                    }
                });
            }

            @Override
            public void callbackEndAnimationFryFromDesckToUsers()
            {
                int sotr_cards = app_setting.opt_sort_my_cards;

                if (sotr_cards == AppSettings.SORT_MY_CARDS_ABC)
                {
                    _draw_task.getRoomInfo().my_userinfo.sortCardsABC();
                }
                else if (sotr_cards == AppSettings.SORT_MY_CARDS_DESC)
                {
                    _draw_task.getRoomInfo().my_userinfo.sortCardsDESC();
                }

                //******************************************************************************

                int sort_trump_catds = app_setting.opt_sort_trump_cards;

                if (sort_trump_catds == AppSettings.SORT_TRUMP_CARDS_LEFT)
                {
                    _draw_task.getRoomInfo().my_userinfo.sortTrumpCardsLeft(_draw_task.getRoomInfo().trump_card_in_the_deck);
                }
                else if (sort_trump_catds == AppSettings.SORT_TRUMP_CARDS_RIGHT)
                {
                    _draw_task.getRoomInfo().my_userinfo.sortTrumpCardsOnRight(_draw_task.getRoomInfo().trump_card_in_the_deck);
                }

                _draw_task.getRoomInfo().my_userinfo.resetMatrixDrawCardsUser();
                _draw_task.getRoomInfo().my_userinfo.cached_draw_my_cards = false;
            }

            @Override
            public void callbackClickUserInRoom(final int uid)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showUserInfo(uid);
                    }
                });
            }

            @Override
            public void callbackEndAnimationFlyCardsFromUserToBoard()
            {
                /*runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if( action_end_animation_fly == ACTION_END_ANIMATION_FLY_SHOW_BTN_BITO )
                        {
                            Button btn_bery = (Button) findViewById(R.id.btn_page_room_bito);

                            if( btn_bery == null ) { return; }

                            btn_bery.setVisibility(View.VISIBLE);
                        }
                        else if( action_end_animation_fly == ACTION_END_ANIMATION_FLY_SHOW_BTN_BERY )
                        {
                            Button btn_bery = (Button) findViewById(R.id.btn_page_room_bery);

                            if( btn_bery == null ) { return; }

                            btn_bery.setVisibility(View.VISIBLE);
                        }
                        else if( action_end_animation_fly == ACTION_END_ANIMATION_FLY_SHOW_BTN_THAT_ALL )
                        {
                            ui_show_btn_only__that_s_all();
                        }
                        //


                    }
                });

                action_end_animation_fly = ACTION_END_ANIMATION_FLY_NONE;*/

                if( _draw_task == null )
                {
                    return;
                }

                _draw_task.highLightResetAll();

                if (_draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_xod)
                {
                    _draw_task.highLightTossedMyCards();
                }
                else if (_draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_beat)
                {
                    _draw_task.highLightBeadMyCards();
                }
            }

            @Override
            public void callbackEndAnimationFlyCardsOffBoard()
            {
                if (_draw_task != null)
                {
                    //_draw_task.resetAnimationOffsetCardBoards();
                    _draw_task.resetAnimationOffsetMyCard();

                    //if (_draw_task.last_cmd_server.equals("RUN_BITO"))
                    {
                        _draw_task.draw_off_game_cards = true;
                        _draw_task.needRebuildBackground = true;
                        //_draw_task.getRoomInfo().my_userinfo.cached_draw_my_cards = false;
                    }
                }
            }

            @Override
            public void callbackDrawBkImg(final Bitmap img)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        FrameLayout fl = (FrameLayout) MainActivity.this.findViewById(R.id.fl_page_room_canvas);

                        if( fl != null )
                        {
                            fl.setBackground(new BitmapDrawable(getResources(), img));
                        }
                    }
                });

            }

            @Override
            public void callbackAddToFriend(int uid)
            {
                ArrayList<String> params = new ArrayList<String>();

                params.add(String.valueOf(accout_info.uid));
                params.add(String.valueOf(session_info.session_socket_id));
                params.add(String.valueOf(uid));

                network.queue_network.add(getString(R.string.cmd_10)
                        , params
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__LONG);
            }
        };

        _draw_task.getRoomInfo().current_pos_user_xod = current_pos_user_xod;
        _draw_task.getRoomInfo().current_pos_user_beat = current_pos_user_beat;
        _draw_task.getRoomInfo().current_pos_user_main_xod = current_pos_user_main_xod;
        _draw_task.setZOrderOnTop(true);
        //_draw_task.setZOrderMediaOverlay(true);
        SurfaceHolder sfhTrack = _draw_task.getHolder();
        sfhTrack.setFormat(PixelFormat.TRANSPARENT);

        /*FrameLayout _fl = (FrameLayout) findViewById(R.id.fl_page_room_user_info);

        if (_fl == null)
        {
            _fl.bringToFront();
        }*/

        fl.addView(_draw_task, lp);

        FrameLayout.LayoutParams fl_ll_br = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        fl_ll_br.gravity = Gravity.LEFT | Gravity.BOTTOM;
        fl_ll_br.bottomMargin = (int) (10 * _scale_px);
        fl_ll_br.leftMargin = (int) (10 * _scale_px);

        LinearLayout ll_br = new LinearLayout(MainActivity.this);
        ll_br.setOrientation(LinearLayout.VERTICAL);

        ll_br.setLayoutParams(fl_ll_br);

        btn_ready_play_game = new Button(MainActivity.this);
        FrameLayout.LayoutParams fl_ch = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        btn_ready_play_game.setBackgroundResource(R.drawable.style_btn1);
        btn_ready_play_game.setLayoutParams(fl_ch);
        btn_ready_play_game.setTypeface(_fontApp_bold);

                /*btn_ready_play_game.setMargin(
                           (int) ( 6 * _scale_px)
                        ,  (int) ( 6 * _scale_px)
                        ,  (int) ( 10 * _scale_px)
                        ,  (int) ( 6 * _scale_px)
                );*/

        btn_ready_play_game.setText(getString(R.string.txt_81));
        btn_ready_play_game.setTextSize(22);
        btn_ready_play_game.setTextColor(Color.WHITE);
        btn_ready_play_game.setVisibility(View.GONE);
        btn_ready_play_game.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pressed_ready_game = true;

                timer_ready.running = false;
                pb_wait_readyPlay.setVisibility(View.GONE);
                btn_ready_play_game.setVisibility(View.GONE);

                network.queue_network.add(getString(R.string.cmd_11)
                        , null
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__FORCE);
            }
        });/*
                btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                    {
                        if(network != null)
                        {
                            if (b)
                            {
                                timer_ready.running = false;
                                pb_wait_readyPlay.setVisibility(View.GONE);
                                ch_ready_play.setVisibility(View.GONE);

                                network.queue_network.add(getString(R.string.cmd_11)
                                        , null
                                        , null
                                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                        , QueueNetwork.TYPE_SEND__FORCE);
                            }
                            else
                            {
                                /*pb_wait_readyPlay.setVisibility(View.VISIBLE);
                                ch_ready_play.setVisibility(View.VISIBLE);
                                pb_wait_readyPlay.setMax(100);
                                pb_wait_readyPlay.setProgress(100);

                                timer_ready = new TimerReady(pb_wait_readyPlay, 10);
                                timer_ready.start();*//*

                                network.queue_network.add("NOT_READY_PLAY"
                                        , null
                                        , null
                                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                        , QueueNetwork.TYPE_SEND__FORCE);
                            }
                        }
                    }
                });*/

        //btn_ready_play_game = btn;

        ll_br.addView(btn_ready_play_game);

        fl.addView(ll_br);

        pb_wait_readyPlay = (ProgressBar) findViewById(R.id.pb_page_room_waiting_ready_play);
        /*pb_wait_readyPlay.setVisibility(View.VISIBLE);
        pb_wait_readyPlay.setMax(100);
        pb_wait_readyPlay.setProgress(100);

        timer_ready = new TimerReady(pb_wait_readyPlay, 10);
        timer_ready.start();*/

        //my_avatar_icon = (MyAvatarIcon) findViewById(R.id.imageView27);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        setFont(root, this._fontApp_bold);

        Button btn_page_room_bito_2 = (Button) findViewById(R.id.btn_page_room_bito_2);
        btn_page_room_bito_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _draw_task.available_select_my_card = false;

                ui_page_room_hide_all_btns();
                ui_stop_timer_waiting_xod();

                if (!_draw_task.haveAnimatedCardDesk())
                {
                    page_room_sendCmd(TYPE_BTN_BITO_2);
                }

                TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                if (tv_0 != null)
                {
                    tv_0.setText("");
                }
                /*network.queue_network.add(getString(R.string.cmd_15)
                        , null
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__FORCE);*/

                //
            }
        });

        Button btn_page_room_bery_2 = (Button) findViewById(R.id.btn_page_room_bery_2);
        btn_page_room_bery_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _draw_task.available_select_my_card = false;

                ui_page_room_hide_all_btns();
                ui_stop_timer_waiting_xod();

                if (!_draw_task.haveAnimatedCardDesk())
                {
                    page_room_sendCmd(TYPE_BTN_BERY_2);
                }

                TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                if (tv_0 != null)
                {
                    tv_0.setTextColor(Color.WHITE);
                    tv_0.setText(getString(R.string.txt_49));
                }
                /*network.queue_network.add(getString(R.string.cmd_16)
                        , null
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__FORCE);*/
            }
        });

        Button btn_page_room_that_s_all_2 = (Button) findViewById(R.id.btn_page_room_that_s_all_2);
        btn_page_room_that_s_all_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _draw_task.available_select_my_card = false;

                ui_page_room_hide_all_btns();
                ui_stop_timer_waiting_xod();

                if (!_draw_task.haveAnimatedCardDesk())
                {
                    page_room_sendCmd(TYPE_BTN_THATS_ALL_2);
                }

                TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                if (tv_0 != null)
                {
                    tv_0.setText("");
                }
                /*network.queue_network.add(getString(R.string.cmd_17)
                        , null
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__FORCE);*/
            }
        });

        Button btn = (Button) findViewById(R.id.btn_page_room_bito);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _draw_task.available_select_my_card = false;

                ui_page_room_hide_all_btns();
                ui_stop_timer_waiting_xod();

                if (!_draw_task.haveAnimatedCardDesk())
                {
                    page_room_sendCmd(TYPE_BTN_BITO);
                }

                TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                if (tv_0 != null)
                {
                    tv_0.setText("");
                }
                /*network.queue_network.add(getString(R.string.cmd_13)
                        , null
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__FORCE);*/
            }
        });

        Button btn_bery = (Button) findViewById(R.id.btn_page_room_bery);
        btn_bery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (_draw_task != null)
                {
                    _draw_task.available_select_my_card = false;

                    ui_page_room_hide_all_btns();
                    ui_stop_timer_waiting_xod();

                    if (!_draw_task.haveAnimatedCardDesk())
                    {
                        page_room_sendCmd(TYPE_BTN_BERY);
                    }

                    TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                    if (tv_0 != null)
                    {
                        tv_0.setTextColor(Color.WHITE);
                        tv_0.setText(getString(R.string.txt_49));
                    }
                    /*network.queue_network.add(getString(R.string.cmd_12)
                            , null
                            , null
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__FORCE);*/

                    //ui_page_room_hide_all_btns();

                    //ui_stop_timer_waiting_xod();
                }
            }
        });

        Button btn_page_room_that_s_all = (Button) findViewById(R.id.btn_page_room_that_s_all);
        btn_page_room_that_s_all.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (_draw_task != null)
                {
                    _draw_task.available_select_my_card = false;

                    ui_page_room_hide_all_btns();
                    ui_stop_timer_waiting_xod();

                    if (!_draw_task.haveAnimatedCardDesk())
                    {
                        page_room_sendCmd(TYPE_BTN_THATS_ALL);
                    }

                    TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                    if (tv_0 != null)
                    {
                        tv_0.setText("");
                    }
                    /*network.queue_network.add(getString(R.string.cmd_14)
                            , null
                            , null
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__FORCE);*/

                    //ui_page_room_hide_all_btns();

                    //ui_stop_timer_waiting_xod();
                }
            }
        });

        fl = (FrameLayout) findViewById(R.id.fl_page_room_messages_close);
        fl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_room_messages_c);
                fl.setVisibility(View.GONE);
            }
        });

        ImageView iv = findViewById(R.id.iv_page_room_new_message_icon);
        iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ImageView iv = findViewById(R.id.iv_page_room_new_message_icon);
                iv.setVisibility(View.GONE);

                FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_room_messages_c);
                fl.setVisibility(View.VISIBLE);
            }
        });


        ImageView iv_btn = findViewById(R.id.iv_page_room_list_icons_close);
        iv_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FrameLayout fl = findViewById(R.id.fl_page_room_list_icons_box);
                fl.setVisibility(View.GONE);
            }
        });

        iv_btn = findViewById(R.id.iv_page_room_list_icons_open);
        iv_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FrameLayout fl = findViewById(R.id.fl_page_room_list_icons_box);
                if (fl != null && fl.getVisibility() == View.VISIBLE)
                {
                    fl.setVisibility(View.GONE);
                }
                else if (fl != null && fl.getVisibility() == View.GONE)
                {
                    fl.setVisibility(View.VISIBLE);
                }


            }
        });

        TableLayout table_icons = findViewById(R.id.tl_page_room_table_icons);

        if (table_icons != null)
        {
            TableRow tr1 = new TableRow(MainActivity.this);

            ui_table_icons_row_add_icon(tr1, 1, R.drawable.icon_emotzi_1);
            ui_table_icons_row_add_icon(tr1, 2, R.drawable.icon_emotzi_2);
            ui_table_icons_row_add_icon(tr1, 3, R.drawable.icon_emotzi_3);

            table_icons.addView(tr1);

            tr1 = new TableRow(MainActivity.this);

            ui_table_icons_row_add_icon(tr1, 4, R.drawable.icon_emotzi_4);
            ui_table_icons_row_add_icon(tr1, 5, R.drawable.icon_emotzi_5);
            ui_table_icons_row_add_icon(tr1, 6, R.drawable.icon_emotzi_6);

            table_icons.addView(tr1);

            tr1 = new TableRow(MainActivity.this);

            ui_table_icons_row_add_icon(tr1, 7, R.drawable.icon_emotzi_7);
            ui_table_icons_row_add_icon(tr1, 8, R.drawable.icon_emotzi_8);
            ui_table_icons_row_add_icon(tr1, 9, R.drawable.icon_emotzi_9);

            table_icons.addView(tr1);
        }

        /*MyAvatarIcon ma = findViewById(R.id.ma_page_room_myavatar);
        if(ma != null)
        {
            ma.setImg( cache_personal_photo );
        }*/

        ImageView iv2 = findViewById(R.id.iv_page_room_bkchange);
        iv2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( _draw_task != null)
                {
                    synchronized (_draw_task.lockWorkThreadAnimation)
                    {
                        _draw_task.setBk();
                        _draw_task.needRebuildBackground = true;
                    }
                }
            }
        });
    }

    /*private void ui_settongs_set_cardtype(int t)
    {
        RadioButton rb1 = findViewById(R.id.rb_page_settings_cardtype_type_1);
        RadioButton rb2 = findViewById(R.id.rb_page_settings_cardtype_type_2);
        RadioButton rb3 = findViewById(R.id.rb_page_settings_cardtype_type_3);

        rb1.setChecked(t == 1);
        rb2.setChecked(t == 2);
        rb3.setChecked(t == 3);

        app_setting.type_cardsstyle = t;
        app_setting.db.setKeyValue("mgz_type_cardsstyle", String.valueOf(t));
    }

    private void ui_settongs_set_backcardtype(int t)
    {
        RadioButton rb1 = findViewById(R.id.rb_page_settings_backcard_type_1);
        RadioButton rb2 = findViewById(R.id.rb_page_settings_backcard_type_2);
        RadioButton rb3 = findViewById(R.id.rb_page_settings_backcard_type_3);
        RadioButton rb4 = findViewById(R.id.rb_page_settings_backcard_type_4);
        RadioButton rb5 = findViewById(R.id.rb_page_settings_backcard_type_5);

        rb1.setChecked(t == 1);
        rb2.setChecked(t == 2);
        rb3.setChecked(t == 3);
        rb4.setChecked(t == 4);
        rb5.setChecked(t == 5);

        app_setting.type_backcard = t;
        app_setting.db.setKeyValue("mgz_type_backcard", String.valueOf(t));
    }*/

    private void show_alert_update_app()
    {
        if (sv_page != SV_PAGE_AUTH)
        {
            return;
        }

        FrameLayout fl = findViewById(R.id.fl_page_menu_alert_new_version_app);

        if (fl != null)
        {
            fl.setVisibility(View.VISIBLE);
        }
    }

    private void runAnimationAfterVideoAd()
    {
        FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_add_balans_animation_after_video);

        if (fl == null)
        {
            return;
        }

        fl.setVisibility(View.VISIBLE);

        synchronized (lock_fireworks_sv)
        {
            fireworks_sv = new FireworksSurfaceView(MainActivity.this)
            {
                @Override
                public void callback_end()
                {
                    //this._ma.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            FrameLayout fl = (FrameLayout) MainActivity.this.findViewById(R.id.fl_page_add_balans_animation_after_video);

                            if (fl != null)
                            {
                                fl.removeAllViews();
                                fl.setVisibility(GONE);
                            }

                            TextView tv = MainActivity.this.findViewById(R.id.tv_page_add_balans_money);

                            if (tv != null)
                            {
                                tv.setTextColor(Color.GREEN);
                            }
                        }
                    });

                    /*WaitThread wt = new WaitThread(200) {
                        @Override
                        public void callback() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView tv = MainActivity.this.findViewById(R.id.tv_page_add_balans_money);

                                    if (tv != null) {
                                        tv.setTextColor(Color.GREEN);
                                    }

                                    try {
                                        Thread.sleep(5500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    tv = MainActivity.this.findViewById(R.id.tv_page_add_balans_money);

                                    if (tv != null) {
                                        tv.setTextColor(Color.WHITE);
                                    }
                                }
                            });

                        }
                    };

                    wt.start();*/

                    if (app_setting.opt_sound_in_game && cachbox != null && ! call_phone)
                    {
                        cachbox.start();
                    }
                }

                @Override
                public void callback_init()
                {
                    int curOrientation = this._ma.getWindowManager().getDefaultDisplay().getRotation();

                    switch (curOrientation)
                    {
                        case 0:
                            //. SCREEN_ORIENTATION_PORTRAIT
                            this._ma.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        //----------------------------------------
                        case 2:
                            //. SCREEN_ORIENTATION_REVERSE_PORTRAIT
                            this._ma.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                            break;
                        //----------------------------------------
                        case 1:
                            //. SCREEN_ORIENTATION_LANDSCAPE
                            this._ma.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                        //----------------------------------------
                        case 3:
                            //. SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                            this._ma.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                            break;
                    }

                    AnimationFireworks af;

                    af = new AnimationFireworks(getApplicationContext()
                            , this.canvas_w / 4
                            , this.canvas_h
                            , 80
                            , this.canvas_w / 2
                            , this.canvas_h / 4
                            , 18
                            , 0.1f
                            , 1.7f
                            , 100
                            , 200
                    );

                    addNewFireWork(af);

                    af = new AnimationFireworks(getApplicationContext()
                            , this.canvas_w / 2 + this.canvas_w / 4
                            , this.canvas_h
                            , 80
                            , this.canvas_w / 2
                            , this.canvas_h / 4
                            , 21
                            , 0.1f
                            , 1.9f
                            , 800
                            , 200
                    );

                    addNewFireWork(af);

                    af = new AnimationFireworks(getApplicationContext()
                            , this.canvas_w / 4
                            , this.canvas_h
                            , 80
                            , this.canvas_w / 2
                            , this.canvas_h / 4
                            , 19
                            , 0.1f
                            , 1.9f
                            , 900
                            , 200
                    );

                    addNewFireWork(af);

                    af = new AnimationFireworks(getApplicationContext()
                            , this.canvas_w / 4
                            , this.canvas_h
                            , 80
                            , this.canvas_w / 2
                            , this.canvas_h / 4
                            , 19
                            , 0.1f
                            , 1.9f
                            , 1200
                            , 200
                    );

                    addNewFireWork(af);

                    af = new AnimationFireworks(getApplicationContext()
                            , this.canvas_w / 4
                            , this.canvas_h
                            , 80
                            , this.canvas_w / 2
                            , this.canvas_h / 4
                            , 15
                            , 0.1f
                            , 2.1f
                            , 1300
                            , 200
                    );

                    addNewFireWork(af);

                    af = new AnimationFireworks(getApplicationContext()
                            , this.canvas_w / 4
                            , this.canvas_h
                            , 80
                            , this.canvas_w / 2
                            , this.canvas_h / 4
                            , 17
                            , 0.1f
                            , 1.9f
                            , 1400
                            , 200
                    );

                    addNewFireWork(af);


                    AnimationTextAddCoints tmp = new AnimationTextAddCoints(
                            this._ma.get_fontApp()
                            , "+200"
                            , this.canvas_w / 2
                            , this.canvas_h - this.canvas_h / 4
                            , 18 * this._ma._scale_px
                            , 2500
                            , 2.5f
                            , Color.parseColor("#00d500")
                            , 10
                            , 80
                            , 800
                    );

                    synchronized (this.lock_list_anumation_text_coints)
                    {
                        this.list_anumation_text_coints.add(tmp);
                    }
                }
            };

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

            if (fl != null)
            {
                fl.removeAllViews();
                fl.addView(fireworks_sv, params);
            }
        }
    }

    private void ui_table_icons_row_add_icon(TableRow tr, int code_icon, int res_img)
    {
        int[] l = new int[]{code_icon, res_img};

        TableRow.LayoutParams tr_lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tr_lp.gravity = Gravity.CENTER;
        tr_lp.leftMargin = (int) (4 * _scale_px);
        tr_lp.rightMargin = (int) (4 * _scale_px);
        tr_lp.topMargin = (int) (3 * _scale_px);
        tr_lp.bottomMargin = (int) (3 * _scale_px);

        ImageView iv = new ImageView(MainActivity.this);
        iv.setLayoutParams(tr_lp);

        iv.setPadding(2, 2, 2, 2);
        iv.setImageResource(res_img);
        iv.setTag(l);

        iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int[] _l = (int[]) (v.getTag());

                if (_draw_task != null)
                {
                    addSmileToSlot(_draw_task.canvas_w2, _draw_task.canvas_h2, _l[1], 1.8f);
                    ui_draw_close_page_room_list_icon();
                    sendAllUsersMyIconSmile(_l[0]);
                }
            }
        });

        tr.addView(iv);
    }

    private void ui_draw_close_page_room_list_icon()
    {
        FrameLayout fl = findViewById(R.id.fl_page_room_list_icons_box);
        if (fl != null)
        {
            fl.setVisibility(View.GONE);
        }
    }

    private boolean supportES2()
    {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

    public void sendSaveOption(int opt_type, String value)
    {
        ArrayList<String> params = new ArrayList<String>();

        params.add(String.valueOf(accout_info.uid));
        params.add(String.valueOf(session_info.session_socket_id));
        params.add(String.valueOf(opt_type));
        params.add(String.valueOf(value));

        network.queue_network.add("SAVE_MY_OPTIONS"
                , params
                , null
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);
    }

    public void sendBuyGoods(View view, int type)
    {
        current_buy_goods_view = view;

        ArrayList<String> params = new ArrayList<String>();

        params.add(String.valueOf(accout_info.uid));
        params.add(String.valueOf(session_info.session_socket_id));
        params.add(String.valueOf(type));

        network.queue_network.add("BUY_GOODS"
                , params
                , null
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);

        FrameLayout fl_wait = findViewById(R.id.fl_page_shop_wait);
        if (fl_wait != null)
        {
            fl_wait.setVisibility(View.VISIBLE);
        }
    }

    private void sendAllUsersMyIconSmile(int id_num_icon)
    {
        ArrayList<String> params = new ArrayList<String>();

        params.add(String.valueOf(accout_info.uid));
        params.add(String.valueOf(session_info.session_socket_id));
        params.add(String.valueOf(id_num_icon));

        network.queue_network.add("SEND_ALL_IN_ROOM_MYICON"
                , params
                , null
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);
    }

    private void addSmileToSlot(final int pos_x, final int pox_y, final int res_img, final float end_scale)
    {
        if (_draw_task != null)
        {
            synchronized (_draw_task.lock_list_animation_image_smile)
            {
                AminatedImageSmile f = new AminatedImageSmile(
                        getResources()
                        , pos_x
                        , pox_y
                        , 1600
                        , end_scale
                        , res_img
                );

                _draw_task.list_animation_image_smile.add(f);
                _draw_task.flag_list_animation_image_smile = true;
            }
        }
    }

    private void amination_new_message_icon()
    {
        ImageView iv_page_room_new_message_icon = (ImageView) findViewById(R.id.iv_page_room_new_message_icon);

        if (iv_page_room_new_message_icon == null || iv_page_room_new_message_icon.getVisibility() != View.VISIBLE)
        {
            return;
        }

        int fadeInDuration = 1000;
        int fadeOutDuration = 1000;
        int timeBetween = 500;

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        iv_page_room_new_message_icon.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                amination_new_message_icon();
            }

            public void onAnimationRepeat(Animation animation)
            {

            }

            public void onAnimationStart(Animation animation)
            {

            }
        });
    }

    /*private void ui_show_btn_only__that_s_all()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Button btn = null;

                btn = (Button) findViewById(R.id.btn_page_room_bito);

                if( btn == null )
                {
                    return;
                }

                btn.setVisibility(View.GONE);

                btn = (Button) findViewById(R.id.btn_page_room_bery);
                btn.setVisibility(View.GONE);

                btn = (Button) findViewById(R.id.btn_page_room_that_s_all);
                btn.setVisibility(View.VISIBLE);
            }
        });
    }*/

    private void page_room_sendCmd(int t)
    {
        if (t == TYPE_BTN_BITO)
        {
            network.queue_network.add(getString(R.string.cmd_13)
                    , null
                    , null
                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                    , QueueNetwork.TYPE_SEND__FORCE);
        }
        else if (t == TYPE_BTN_BERY)
        {
            network.queue_network.add(getString(R.string.cmd_12)
                    , null
                    , null
                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                    , QueueNetwork.TYPE_SEND__FORCE);
        }
        else if (t == TYPE_BTN_THATS_ALL)
        {
            network.queue_network.add(getString(R.string.cmd_14)
                    , null
                    , null
                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                    , QueueNetwork.TYPE_SEND__FORCE);
        }
        else if (t == TYPE_BTN_BITO_2)
        {
            network.queue_network.add(getString(R.string.cmd_15)
                    , null
                    , null
                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                    , QueueNetwork.TYPE_SEND__FORCE);
        }
        else if (t == TYPE_BTN_BERY_2)
        {
            network.queue_network.add(getString(R.string.cmd_16)
                    , null
                    , null
                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                    , QueueNetwork.TYPE_SEND__FORCE);
        }
        else if (t == TYPE_BTN_THATS_ALL_2)
        {
            network.queue_network.add(getString(R.string.cmd_17)
                    , null
                    , null
                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                    , QueueNetwork.TYPE_SEND__FORCE);
        }
    }

    private void ui_page_room_show_btn(int t)
    {
        ui_page_room_hide_all_btns();

        Button btn = null;

        btn = (Button) findViewById(R.id.btn_page_room_bito);
        if (btn != null && t == TYPE_BTN_BITO) btn.setVisibility(View.VISIBLE);

        btn = (Button) findViewById(R.id.btn_page_room_bery);
        if (btn != null && t == TYPE_BTN_BERY) btn.setVisibility(View.VISIBLE);

        btn = (Button) findViewById(R.id.btn_page_room_that_s_all);
        if (btn != null && t == TYPE_BTN_THATS_ALL) btn.setVisibility(View.VISIBLE);

        btn = (Button) findViewById(R.id.btn_page_room_bito_2);
        if (btn != null && t == TYPE_BTN_BITO_2) btn.setVisibility(View.VISIBLE);

        btn = (Button) findViewById(R.id.btn_page_room_bery_2);
        if (btn != null && t == TYPE_BTN_BERY_2) btn.setVisibility(View.VISIBLE);

        btn = (Button) findViewById(R.id.btn_page_room_that_s_all_2);
        if (btn != null && t == TYPE_BTN_THATS_ALL_2) btn.setVisibility(View.VISIBLE);
    }

    /*private void ui_page_room_enable_all_btns(boolean v)
    {
        Button btn = null;

        btn = (Button) findViewById(R.id.btn_page_room_bito);
        if (btn != null) btn.setEnabled(v);

        btn = (Button) findViewById(R.id.btn_page_room_bery);
        if (btn != null) btn.setEnabled(v);

        btn = (Button) findViewById(R.id.btn_page_room_that_s_all);
        if (btn != null) btn.setEnabled(v);

        btn = (Button) findViewById(R.id.btn_page_room_bito_2);
        if (btn != null) btn.setEnabled(v);

        btn = (Button) findViewById(R.id.btn_page_room_bery_2);
        if (btn != null) btn.setEnabled(v);

        btn = (Button) findViewById(R.id.btn_page_room_that_s_all_2);
        if (btn != null) btn.setEnabled(v);
    }*/

    private void ui_page_room_hide_all_btns()
    {
        Button btn = null;

        btn = (Button) findViewById(R.id.btn_page_room_bito);
        if (btn != null) btn.setVisibility(View.GONE);

        btn = (Button) findViewById(R.id.btn_page_room_bery);
        if (btn != null) btn.setVisibility(View.GONE);

        btn = (Button) findViewById(R.id.btn_page_room_that_s_all);
        if (btn != null) btn.setVisibility(View.GONE);

        btn = (Button) findViewById(R.id.btn_page_room_bito_2);
        if (btn != null) btn.setVisibility(View.GONE);

        btn = (Button) findViewById(R.id.btn_page_room_bery_2);
        if (btn != null) btn.setVisibility(View.GONE);

        btn = (Button) findViewById(R.id.btn_page_room_that_s_all_2);
        if (btn != null) btn.setVisibility(View.GONE);
    }

    private void showUserInfo(int uid)
    {
        FrameLayout fl = (FrameLayout) findViewById(R.id.fl_page_room_user_info);

        if (fl == null)
        {
            return;
        }
        if (_draw_task == null)
        {
            return;
        }

        /*_draw_task.setZOrderOnTop(false);
        _draw_task.setZOrderMediaOverlay(true);*/
        //_draw_task.drawBkImg = true;
        //_draw_task.setVisibility(View.GONE);

        room_page_oppened_user_info = uid;

        TableRow tr_page_room_popup_user_info_raiting = (TableRow) findViewById(R.id.tr_page_room_popup_user_info_raiting);
        ImageView iv = (ImageView) findViewById(R.id.iv_page_room_popup_avatar);
        TextView tv_user_name = (TextView) findViewById(R.id.tv_page_room_popup_user_name);
        TextView tv_raiting = (TextView) findViewById(R.id.tv_page_room_popup_raiting);
        TextView tv_total_game = (TextView) findViewById(R.id.tv_page_room_popup_total_game);
        TextView tv_wins = (TextView) findViewById(R.id.tv_page_room_popup_wins);
        TextView tv_defeats = (TextView) findViewById(R.id.tv_page_room_popup_defeats);
        TextView tv_draw = (TextView) findViewById(R.id.tv_page_room_popup_draw);

        RoomInfo_User user_info = _draw_task.getInfoUser(uid);

        iv.setImageBitmap(user_info.user_photo);

        if (Locale.getDefault().getLanguage().equalsIgnoreCase("RU"))
        {
            tv_user_name.setText(user_info.first_name + " " + user_info.last_name);
        }
        else
        {
            tv_user_name.setText(Utils.transliterate(user_info.first_name + " " + user_info.last_name));
        }

        tv_raiting.setText(String.valueOf(user_info.info_raiting));
        tv_total_game.setText(String.valueOf(user_info.info_count_games));
        tv_wins.setText(String.valueOf(user_info.info_count_wins));
        tv_defeats.setText(String.valueOf(user_info.info_count_defeats));
        tv_draw.setText(String.valueOf(user_info.info_count_draw));

        fl.setVisibility(View.VISIBLE);

        if (user_info.info_raiting == 0)
        {
            tr_page_room_popup_user_info_raiting.setVisibility(View.GONE);
        }
        else
        {
            tr_page_room_popup_user_info_raiting.setVisibility(View.VISIBLE);
        }

        RoomInfo_User uinfo = _draw_task.getInfoUser(uid);

        LinearLayout btn = (LinearLayout) findViewById(R.id.btn_page_room_add_friend);

        if (uinfo.exists_in_friends || uinfo.is_bot)
        {
            btn.setVisibility(View.GONE);
        }
        else
        {
            btn.setVisibility(View.VISIBLE);
        }
    }

    private String readTextFromResource(int resourceID)
    {
        InputStream raw = getResources().openRawResource(resourceID);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int i;
        try
        {
            i = raw.read();
            while (i != -1)
            {
                stream.write(i);
                i = raw.read();
            }
            raw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return stream.toString();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    private void close_app()
    {
        //Log.i("TAG", "close_app");

        if (network != null)
        {
            network.OnDestroy();
            network = null;
        }

        active = false;

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /*private void setStatusTextPageRoom(String text)
    {
        TextView tv = (TextView) findViewById(R.id.tv_page_room_status_txt);

        if (tv != null)
        {
            tv.setText(text);
        }
    }*/

    public AppSettings getAppSettings()
    {
        return app_setting;
    }

    private void getMyBalans()
    {
        ArrayList<String> params = new ArrayList<String>();

        params.add(String.valueOf(accout_info.uid));
        params.add(String.valueOf(session_info.session_socket_id));

        network.queue_network.add("GET_MY_BALANS"
                , params
                , null
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);
    }

    private void offDrawGame(boolean need_balanse)
    {
        if (_draw_task != null)
        {
            _draw_task.OnDestroy();

            if (need_balanse)
            {
                getMyBalans();
            }

            network.queue_network.add("OUT_OF_ROOM"
                    , null
                    , null
                    , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                    , QueueNetwork.TYPE_SEND__FORCE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            FrameLayout fl = null;

            fl = (FrameLayout) findViewById(R.id.fl_page_menu_alert_new_version_app);
            if (fl != null && fl.getVisibility() == View.VISIBLE)
            {
                fl.setVisibility(View.GONE);
                return false;
            }

            fl = (FrameLayout) findViewById(R.id.fl_page_room_list_icons_box);
            if (fl != null && fl.getVisibility() == View.VISIBLE)
            {
                fl.setVisibility(View.GONE);
                return false;
            }

            fl = (FrameLayout) findViewById(R.id.fl_page_friends_user_info);
            if (fl != null && fl.getVisibility() == View.VISIBLE)
            {
                fl.setVisibility(View.GONE);
                return false;
            }

            fl = (FrameLayout) findViewById(R.id.fl_table_rating_user_info);
            if (fl != null && fl.getVisibility() == View.VISIBLE)
            {
                fl.setVisibility(View.GONE);
                return false;
            }


            if (sv_page == SV_PAGE_CONNECTION_SERVER)
            {
                close_app();
                return false;
            }

            if (sv_page == SV_PAGE_SEARCH_USERS)
            {
                show_page_friends();
                return false;
            }

            if (sv_page == SV_PAGE_ROOM && _draw_task != null)
            {
                if (_draw_task.stat_game == false) // если игра не начата, то просто выход
                {
                    _draw_task._stop_draw = true;

                    offDrawGame(true);
                    show_page_menu();
                    return false;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(getString(R.string.txt_82));

                builder.setPositiveButton(getString(R.string.txt_71), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        offDrawGame(true);

                        show_page_menu();

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(getString(R.string.txt_72), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                alert_dialog_out_of_room = builder.create();

                alert_dialog_out_of_room.setCancelable(false);
                alert_dialog_out_of_room.setOnShowListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface arg0)
                    {
                        alert_dialog_out_of_room.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green_1));
                        alert_dialog_out_of_room.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                    }
                });

                alert_dialog_out_of_room.show();
                return false;
            }

            if (
                    sv_page == SV_PAGE_SETTINGS
                            || sv_page == SV_PAGE_RULES
                            || sv_page == SV_PAGE_CREATE_NEW_GAME
                            || sv_page == SV_PAGE_PROFILE
                            || sv_page == SV_PAGE_SEARCH
                            || sv_page == SV_PAGE_FRIENDS
                            || sv_page == SV_PAGE_ADD_BALANS
                            || sv_page == SV_PAGE_TABLE_RATING
                            || sv_page == SV_PAGE_SHOP
                    )
            {
                show_page_menu();
                return false;
            }
            else if (sv_page == SV_PAGE_PRIVACY_POLICY)
            {
                show_page_auth();
                return false;
            }
            else if (sv_page == SV_PAGE_RULET)
            {


                show_page_add_balans();
                return false;
            }

            if (System.currentTimeMillis() - time_press_exit_app < 4100)
            {
                close_app();
            }
            else
            {
                time_press_exit_app = System.currentTimeMillis();

                String txt = getString(R.string.txt_83);

                Toast.makeText(MainActivity.this, txt, Toast.LENGTH_LONG).show();

                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onClick_logout(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.txt_84));

        builder.setPositiveButton(getString(R.string.txt_71), new DialogInterface.OnClickListener()
        {

            public void onClick(DialogInterface dialog, int which)
            {
                //app_setting.db.setKeyValue("init_ok", "0");
                signInReset();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.txt_72), new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                // Do nothing
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();

        //2. now setup to change color of the button
        alert.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface arg0)
            {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green_1));
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
            }
        });

        alert.show();

    }

    public void onClick_show_page_add_balans(View v)
    {
        show_page_add_balans();
    }

    public ClassNetWork getNetwork()
    {
        return network;
    }

    ///=============================================================================================
    private void connection_server()
    {
        if (network != null)
        {
            return;
        }

        network = new ClassNetWork(MainActivity.this, HOST, TCP_PORT_SERVER_1, session_info)
        {
            @Override
            public void callback_close_connection()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        show_page_connection_server();
                        //show_page_add_money();

                        /*TextView tv = (TextView) findViewById(R.id.textView_version);

                        if(tv != null)
                        {
                            tv.setText("v" + VERSION_APP+" beta");
                        }*/
                    }
                });
            }

            @Override
            public void callback_open_connection(int count_open_connection)
            {
            }

            @Override
            public void callback_open_connection2(int index_c, String v)
            {
                //Log.i("TAG", "_callback_open_connection2: " + String.valueOf(index_c) + " " + v);

                if (index_c == 1)
                {
                    if (network != null && network.queue_network != null)
                    {
                        network.clearBuffersInOut();
                        network.queue_network.clearAllQueue();
                    }
                    /*else
                    {
                        //Log.i("TAG", "callback_open_connection2 error 1");
                    }*/

                    if (network != null && network.queue_network != null)
                    {
                        //Log.i("TAG", "network.queue_network.add  FIRST_CMD ");

                        //8083network.queue_network.clearAllQueue();

                        session_info = new SessionInfo();

                        network.queue_network.add("FIRST_CMD"
                                , null
                                , null
                                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                , QueueNetwork.TYPE_SEND__FORCE);
                    }
                    /*else
                    {
                        //Log.i("TAG", "callback_open_connection2 error 2");
                    }*/

                }
                /*else
                {
                    //Log.i("TAG", "index_c == 1 error");
                }*/
            }

            @Override
            public int callback_on_read(ReadCommand r, InputStream input)
            {
                synchronized (lock_drawTask)
                {
                    _exec(r, input);
                }

                return 0;
            }

            @Override
            public boolean callback_exec2(final DataInputStream in_soket, final ReadCommand r, Socket sock, final Object param)
            {
                synchronized (lock_drawTask)
                {
                    return _exec2(in_soket, r, sock, param);
                }
            }

            @Override
            public void callback_on_send_echo()
            {
                //Log.i("TAG", "callback_on_send_echo");

                //if(_current_user_id > 0 )
                {
                    //ArrayList<String> params = new ArrayList<String>();

                    network.queue_network.add("ECHO"
                            , null
                            , null
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__FORCE);
                }
            }
        };
    }

    private void removeAllOldFragmentsThisApp()
    {
        for (Fragment fragment : getSupportFragmentManager().getFragments())
        {
            /*if (fragment instanceof StatisticTop && fragment != null)
            {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            else*/
            if (fragment instanceof SearchRooms && fragment != null)
            {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
    }

    public boolean check_version(String curr_version, String load_version)
    {
        List<Integer> c_ver_app = new ArrayList<Integer>();
        List<Integer> c_ver_app_in_server = new ArrayList<Integer>();

        String[] l = curr_version.split("\\.");
        for (int p = 0; p < l.length; p++)
        {
            c_ver_app.add(Integer.parseInt(l[p]));
        }

        l = load_version.trim().split("\\.");
        for (int p = 0; p < l.length; p++)
        {
            c_ver_app_in_server.add(Integer.parseInt(l[p]));
        }

        for (int p = 0; p < c_ver_app.size(); p++)
        {
            if (c_ver_app.get(p) < c_ver_app_in_server.get(p))
            {
                return true;
            }

            if (c_ver_app.get(p) > c_ver_app_in_server.get(p))
            {
                break;
            }
        }

        return false;
    }

    void _exec(final ReadCommand r, InputStream input)
    {
        Log.i("TAG54", "< cmd : " + r.cmd + " " + String.valueOf(r.timestamp));

        if (r.cmd.equalsIgnoreCase("OK_SEND"))  /// подтверждение
        {
            network.queue_network.okSend(r.timestamp);

            return;
        }

        //Log.i("TAG", "cmd: " + r.cmd);

        //----------------------------------------------------------
        String cmd = "OK_SEND";

        network.addOutBuffer(cmd, null, null, ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_HIGH, r.timestamp);
        //----------------------------------------------------------

        if (network.queue_network.foundInput(r.timestamp))
        {
            return;
        }
        else
        {
            network.queue_network.addInput(r.timestamp);
        }

        if (r.cmd.equalsIgnoreCase("FIRST_CMD"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] byff = ByteBuffer.allocate(4).put(r.data, offset, 4).array();
                offset += 4;

                session_info.session_socket_id = ProtokolUtils.fromByteArray(byff);

                //session_info.aes_key = new byte[16];
                session_info.xor_key = new byte[16];

                session_info.type_key = 1;
                //System.arraycopy(r.data, offset, session_info.aes_key, 0, 16);
                //offset += 16;
                System.arraycopy(r.data, offset, session_info.xor_key, 0, 16);
                offset += 16;

                network.setSessionInfo(session_info);

                /*runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        show_page_menu();
                    }
                });*/

                if( db.getKeyValue(getString(R.string.cmd_1), "0").equals("0") )
                {
                    ArrayList<String> params = new ArrayList<String>();

                    params.add(accout_info.personId);
                    params.add(idToken);
                    params.add(String.valueOf(session_info.session_socket_id));
                    params.add(Locale.getDefault().getLanguage());

                    network.queue_network.add(getString(R.string.cmd_1)
                            , params
                            , null
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__LONG);
                }
                else
                {
                    accout_info.personId = db.getKeyValue("accout_info.personId", "");
                    accout_info.personName = db.getKeyValue("accout_info.personName", "");
                    accout_info.personEmail = db.getKeyValue("accout_info.personEmail", "");
                    accout_info.fisrtName = db.getKeyValue("accout_info.fisrtName", "");
                    accout_info.lastName = db.getKeyValue("accout_info.lastName", "");
                    accout_info.personPhoto = db.getKeyValue("accout_info.personPhoto", "");

                    if( accout_info.personId == null )
                    {
                        close_app();
                    }

                    ArrayList<String> params = new ArrayList<String>();

                    params.add( accout_info.personId );
                    params.add(String.valueOf(session_info.session_socket_id));
                    params.add( token );

                    network.queue_network.add(getString(R.string.cmd_2)
                            , params
                            , null
                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                            , QueueNetwork.TYPE_SEND__LONG);
                }
            }

            /*int _socket_server = Integer.parseInt(new String(r.params.get(0)));
            String _xcode_session = (new String(r.params.get(1)));

            socket_s = _socket_server;
            xcode_session = _xcode_session;


            if (_current_user_id == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //show_page_input_or_reg();
                        show_page_auth();
                    }
                });
            } else {
                send_command_init_session();

                List<DB.StructContactInfo_FromLoadServer> list_users = db._db_u2u_get_all_from_send_server();

                ArrayList<String> params = new ArrayList<String>();

                String list_contacts = "";

                for (int i = 0; i < list_users.size(); i++) {
                    if (list_users.get(i).status == 3) {
                        list_contacts += String.valueOf(list_users.get(i).user_id) + "|";
                    }
                }

                params.add(list_contacts);

                network.queue_network.add("ECHO"
                        , params
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__FORCE);
            }*/
        }
        else if (r.cmd.equalsIgnoreCase("ECHO"))
        {
            if (r.data != null)
            {
                int offset = 0, count_read = 0;
                byte[] _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int count_online = buffer.getInt();

                last_count_online = count_online;

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setUiCountOnline(last_count_online);
                    }
                });
            }
        }
        else if (r.cmd.equalsIgnoreCase("ADD_MONEY_FROM_FRIEND"))
        {
            if (r.data != null)
            {
                getMyBalans();

                if (app_setting.opt_sound_in_game && cachbox != null && ! call_phone)
                {
                    cachbox.start();
                }

                int offset = 0, count_read = 0;
                byte[] _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int uid = buffer.getInt();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int sum = buffer.getInt();

                for (int i = 0; i < friend_list.size(); i++)
                {
                    if (friend_list.get(i).uid == uid)
                    {
                        final FriendItem fi = friend_list.get(i);

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(MainActivity.this
                                        , getString(R.string.txt_104)
                                                + " "
                                                + fi.first_name
                                                + " "
                                                + fi.last_name
                                                + " "
                                                + String.valueOf(sum)
                                                + " "
                                                + Utils.getCorrectSuffix(sum, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70))
                                        , Toast.LENGTH_LONG
                                ).show();
                            }
                        });

                        getMyBalans();

                        break;
                    }
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("STOP_APP"))
        {
            close_app();
        }
        else if (r.cmd.equalsIgnoreCase("NEED_UPDATE_LIST_FRIENDS"))
        {
            load_my_friends();
        }
        else if (r.cmd.equalsIgnoreCase("CHECK_VERSION"))
        {
            if (r.data != null)
            {
                int offset = 0, count_read = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                count_read = (int) _len[0] & 0xff;
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                String server_version = new String(_len);

                if ((server_version.trim().length() > 0) && check_version(VERSION_APP, server_version))
                {
                    need_show_alert_update_app = true;

                    if (sv_page == SV_PAGE_AUTH)
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                show_alert_update_app();
                            }
                        });
                    }
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("ADD_TO_FRIEND_CONNECT"))
        {
            if (r.data != null)
            {
                int offset = 0, count_read = 0;
                byte[] _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int uid = buffer.getInt();

                if (map_ui_friends.get(uid) != null)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            View t = map_ui_friends.get(uid).iv;

                            ViewGroup parent = (ViewGroup) t.getParent();
                            if (parent != null)
                            {
                                parent.removeView(t);
                            }
                        }
                    });
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("ADDED_TO_FRIENDS"))
        {
            if (r.data != null)
            {
                int offset = 0, count_read = 0;
                byte[] _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int uid = buffer.getInt();

                RoomInfo_User uinfo = _draw_task.getInfoUser(uid);

                uinfo.exists_in_friends = true;


                load_my_friends();
            }
        }
        else if (r.cmd.equalsIgnoreCase("DELETE_FRIEND_CONNECT"))
        {
            if (r.data != null)
            {
                int offset = 0, count_read = 0;
                byte[] _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int uid = buffer.getInt();

                if (map_ui_friends.get(uid) != null)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            View t = map_ui_friends.get(uid).ll;

                            ViewGroup parent = (ViewGroup) t.getParent();
                            if (parent != null)
                            {
                                parent.removeView(t);
                            }
                        }
                    });
                }

                load_my_friends();
            }
        }
        else if (r.cmd.equalsIgnoreCase("INFO_USERS_IN_ROOM"))
        {
            if (r.data != null)
            {
                WaitThread wt = new WaitThread(200)
                {
                    @Override
                    public void callback()
                    {
                        do
                        {
                            try
                            {
                                Thread.sleep(200);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        while (_draw_task == null || _draw_task.isInitDraw());

                        synchronized (lock_drawTask)
                        {
                            if (_draw_task == null)
                            {
                                return;
                            }

                            int offset = 0, count_read = 0;
                            byte[] _len;
                            ByteBuffer buffer;

                            _len = ByteBuffer.allocate(8).array();
                            System.arraycopy(r.data, offset, _len, 0, 8);
                            offset += _len.length;

                            long room_id = ProtokolUtils.htonl2(ByteBuffer.wrap(_len).getLong());

                            if (_draw_task.room_id != room_id)
                            {
                                //Log.i("TAG54", "ERROR ROOM ID");
                                return;
                            }

                            _len = ByteBuffer.allocate(1).array();
                            System.arraycopy(r.data, offset, _len, 0, _len.length);
                            offset += _len.length;
                            int count_users = _len[0];

                            int count_ready = 0;
                            int count_init = 0;
                            boolean im_ready = false;

                            for (int k = 0; k < count_users; k++)
                            {
                                _len = ByteBuffer.allocate(1).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                byte is_init = _len[0];

                                _len = ByteBuffer.allocate(4).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                buffer = ByteBuffer.wrap(_len);
                                buffer.order(ByteOrder.BIG_ENDIAN);

                                int uid = buffer.getInt();

                                if (is_init == 0 && _draw_task != null)
                                {
                                    RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(k + 1);

                                    if (ri != null)
                                    {
                                        ri.init_user = false;
                                        ri.ready = false;
                                        ri.selected = false;
                                        ri.exists_in_friends = false;
                                        ri.loaded_user_photo_from_url = false;
                                        ri.user_photo = _draw_task.getDefaultAvatar().copy(_draw_task.getDefaultAvatar().getConfig(), true);

                                        /*ri.user_photo = Bitmap.createScaledBitmap(
                                                  _draw_task.getDefaultAvatar()
                                                , (int)(60 * _scale_px)
                                                , (int)(60 * _scale_px)
                                                , true);*/

                                        _draw_task.needRebuildBackground = true;
                                    }

                                    continue;
                                }

                                count_init += 1;

                                //--------------------------------------------------------------------------

                                _len = ByteBuffer.allocate(1).array();

                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                count_read = (int) _len[0] & 0xff;
                                _len = ByteBuffer.allocate(count_read).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                String first_name = new String(_len);

                                //--------------------------------------------------------------------------

                                _len = ByteBuffer.allocate(1).array();

                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                count_read = (int) _len[0] & 0xff;
                                _len = ByteBuffer.allocate(count_read).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                String last_name = new String(_len);

                                //--------------------------------------------------------------------------

                                _len = ByteBuffer.allocate(1).array();

                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                count_read = (int) _len[0] & 0xff;
                                _len = ByteBuffer.allocate(count_read).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                String user_photo = new String(_len);

                                //--------------------------------------------------------------------------

                                _len = ByteBuffer.allocate(1).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                byte ready = _len[0];

                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                byte is_bot = _len[0];

                                //----------------------------------------------------------------------
                                // USER INFO STATISTIC
                                _len = ByteBuffer.allocate(4).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                buffer = ByteBuffer.wrap(_len);
                                buffer.order(ByteOrder.BIG_ENDIAN);

                                int info_raiting = buffer.getInt();

                                _len = ByteBuffer.allocate(4).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                buffer = ByteBuffer.wrap(_len);
                                buffer.order(ByteOrder.BIG_ENDIAN);

                                int info_count_games = buffer.getInt();

                                _len = ByteBuffer.allocate(4).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                buffer = ByteBuffer.wrap(_len);
                                buffer.order(ByteOrder.BIG_ENDIAN);

                                int info_count_wins = buffer.getInt();

                                _len = ByteBuffer.allocate(4).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                buffer = ByteBuffer.wrap(_len);
                                buffer.order(ByteOrder.BIG_ENDIAN);

                                int info_count_defeats = buffer.getInt();

                                _len = ByteBuffer.allocate(4).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                buffer = ByteBuffer.wrap(_len);
                                buffer.order(ByteOrder.BIG_ENDIAN);

                                int info_count_draw = buffer.getInt();

                                /*_len = ByteBuffer.allocate(1).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                byte exists_in_friends = _len[0];*/
                                //----------------------------------------------------------------------

                                count_ready += ready;

                                if (_draw_task == null)
                                {
                                    continue;
                                }

                                if (uid == accout_info.uid)
                                {
                                    _draw_task.getRoomInfo().my_userinfo.uid = uid;
                                    //_draw_task.getRoomInfo().my_userinfo.user_name = user_name;
                                    //_draw_task.getRoomInfo().my_userinfo.user_photo = _draw_task.getDefaultAvatar().copy( _draw_task.getDefaultAvatar().getConfig(), true );

                                    im_ready = (ready == 1) ? true : false;
                                }
                                else
                                {
                                    final RoomInfo_User uinfo = _draw_task.getUserFromOffsetPosition(k + 1);

                                    /*Log.i("TAG54", "INFO_USERS_IN_ROOM " +
                                            String.valueOf(uid) +
                                            " " +
                                            String.valueOf(_draw_task.getRoomInfo().users_in_room.size()) + " " + String.valueOf(k + 1));*/

                                    uinfo.ready = (ready == 1) ? true : false;
                                    uinfo.is_bot = (is_bot == 1) ? true : false;
                                    //count_ready += ready;
                                    uinfo.init_user = true;

                                    uinfo.uid = uid;
                                    uinfo.first_name = first_name;
                                    uinfo.last_name = last_name;

                                    if (!Locale.getDefault().getLanguage().equalsIgnoreCase("RU"))
                                    {
                                        uinfo.first_name = Utils.transliterate(uinfo.first_name);
                                        uinfo.last_name = Utils.transliterate(uinfo.last_name);
                                    }

                                    uinfo.uname_draw_rect = new Rect();

                                    _draw_task.paint_drawTextUserNikName.getTextBounds(
                                            first_name
                                            , 0
                                            , first_name.length()
                                            , uinfo.uname_draw_rect
                                    );

                                    uinfo.info_raiting          = info_raiting;
                                    uinfo.info_count_defeats    = info_count_defeats;
                                    uinfo.info_count_draw       = info_count_draw;
                                    uinfo.info_count_games      = info_count_games;
                                    uinfo.info_count_wins       = info_count_wins;

                                    if (uinfo.user_photo == null)
                                    {
                                        uinfo.user_photo = _draw_task.getDefaultAvatar().copy(_draw_task.getDefaultAvatar().getConfig(), true);

                                        /*uinfo.user_photo = Bitmap.createScaledBitmap(
                                                  _draw_task.getDefaultAvatar()
                                                , (int)(60 * _scale_px)
                                                , (int)(60 * _scale_px)
                                                , true);*/
                                    }

                                    if ( ! uinfo.loaded_user_photo_from_url)
                                    {
                                        //uinfo.exists_in_friends    = ( exists_in_friends == 1 ? true : false );

                                        String _url = user_photo;

                                        if (_url.startsWith("/"))
                                        {
                                            _url = "http://" + HOST + ":8083" + _url;
                                        }

                                        final String url = _url;

                                        WaitThread wt = new WaitThread(0)
                                        {
                                            @Override
                                            public void callback()
                                            {
                                                Bitmap b = Utils.getBitmapFromURL(url);

                                                if(b == null)
                                                {
                                                    b = Utils.getBitmapFromURL(url);
                                                }

                                                if (_draw_task != null && uinfo.user_photo != null && b != null)
                                                {
                                                    uinfo.user_photo = Utils.getCroppedBitmap(MainActivity.resizeAvatarImg(_draw_task.size_avatar_x, _draw_task.size_avatar_y, b));
                                                    uinfo.loaded_user_photo_from_url = true;
                                                }

                                                if (uinfo.user_photo == null)
                                                {
                                                    uinfo.user_photo = _draw_task.getDefaultAvatar().copy(_draw_task.getDefaultAvatar().getConfig(), true);
                                                }
                                            }
                                        };

                                        wt.start();
                                    }
                                }
                            }

                            for (int k = 0; k < count_users; k++)
                            {
                                _len = ByteBuffer.allocate(1).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                byte is_init = _len[0];

                                if (is_init == 1)
                                {
                                    _len = ByteBuffer.allocate(4).array();
                                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                                    offset += _len.length;

                                    buffer = ByteBuffer.wrap(_len);
                                    buffer.order(ByteOrder.BIG_ENDIAN);

                                    int uid = buffer.getInt();

                                    _len = ByteBuffer.allocate(1).array();
                                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                                    offset += _len.length;

                                    byte exists_in_friends = _len[0];

                                    RoomInfo_User uinfo = _draw_task.getInfoUser(uid);

                                    if (uinfo != null)
                                    {
                                        uinfo.exists_in_friends = (exists_in_friends == 1 ? true : false);
                                    }
                                }
                            }

                            _len = ByteBuffer.allocate(1).array();

                            System.arraycopy(r.data, offset, _len, 0, _len.length);
                            offset += _len.length;
                            int status_room = _len[0];

                            if (status_room == RoomSurfaceView.STATUS_ROOM_WAIT_START)
                            {
                                /*Log.i("TAG54"
                                        , String.valueOf(count_init)
                                        + " "
                                        + String.valueOf(count_users)
                                        + " "
                                        + String.valueOf(count_ready)
                                        + " "
                                        + String.valueOf( (timer_ready == null || !timer_ready.running) )
                                        + " "
                                        + String.valueOf( ! im_ready )
                                );*/

                                if (count_init == count_users && count_ready == count_users)
                                {
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {

                                            pb_wait_readyPlay.setVisibility(View.GONE);
                                            btn_ready_play_game.setVisibility(View.GONE);

                                            if (timer_ready != null)
                                            {
                                                timer_ready.running = false;
                                            }
                                        }
                                    });
                                }
                                else if (
                                        count_init == count_users
                                                && !pressed_ready_game
                                                && !im_ready
                                                && (timer_ready == null || !timer_ready.running))
                                {
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            pb_wait_readyPlay.setVisibility(View.VISIBLE);
                                            btn_ready_play_game.setVisibility(View.VISIBLE);
                                            pb_wait_readyPlay.setMax(1000);
                                            pb_wait_readyPlay.setProgress(1000);

                                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                            v.vibrate(30);

                                            if (timer_ready == null || ! timer_ready.running)
                                            {
                                                timer_ready = new TimerReady(pb_wait_readyPlay, 10);
                                                timer_ready.start();
                                            }
                                        }
                                    });
                                }
                                else if (count_init != count_users)
                                {
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            if (timer_ready != null)
                                            {
                                                timer_ready.running = false;
                                            }

                                            pb_wait_readyPlay.setVisibility(View.GONE);
                                            btn_ready_play_game.setVisibility(View.GONE);
                                            //ch_ready_play.setChecked(false);

                                            TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                                            if (tv_0 != null)
                                            {
                                                tv_0.setTextColor(Color.WHITE);
                                                tv_0.setText(getString(R.string.txt_47));
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                };
                wt.start();
            }
        }
        else if (r.cmd.equalsIgnoreCase("START_GAME_PRE_INFO"))
        {
            if (r.data != null && _draw_task != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int trump = buffer.getInt();

                //----------------------------------------------------

                final List<Short> cards_users = new ArrayList<>();

                for (int i = 0; i < 6; i++)
                {
                    _len = ByteBuffer.allocate(2).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    cards_users.add(buffer.getShort());
                }

                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                final byte count_cards_in_desck = _len[0];

                _draw_task.getRoomInfo().count_deck_of_cards = count_cards_in_desck;

                _draw_task.getRoomInfo().trump_card_in_the_deck = trump;

                _draw_task.getRoomInfo().my_userinfo.resetAll();

                for (int i = 0; i < cards_users.size(); i++)
                {
                    _draw_task.getRoomInfo().my_userinfo.cards_user.add(Cards.getInfoCard(cards_users.get(i)));
                    _draw_task.getRoomInfo().my_userinfo.cards_points.add(new CardDraw_RectPoint());
                }

                _draw_task.needRebuildBackground = true;
            }
        }
        else if (r.cmd.equalsIgnoreCase("START_GAME"))
        {
            if (r.data != null)
            {
                WaitThread wt = new WaitThread(0)
                {
                    @Override
                    public void callback()
                    {
                        int offset = 0;
                        byte[] _len = ByteBuffer.allocate(1).array();

                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        int current_pos_user_main_xod = _len[0];

                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        int current_pos_user_xod = _len[0];

                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        int current_pos_user_beat = _len[0];

                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        int count_cards_in_desck = _len[0];

                        while (_draw_task == null || _draw_task.isInitDraw())
                        {
                            try
                            {
                                Thread.sleep(100);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }

                            continue;
                        }

                        synchronized (lock_drawTask)
                        {
                            if (_draw_task == null)
                            {
                                return;
                            }

                            synchronized (_draw_task.getRoomInfo().lock_draw)
                            {
                                _draw_task.getRoomInfo().my_userinfo.cached_draw_my_cards = false;
                                _draw_task.getRoomInfo().my_userinfo.resetMatrixDrawCardsUser();
                            }

                            synchronized (_draw_task.getRoomInfo().lock_cards_boards)
                            {
                                _draw_task.getRoomInfo().cache_draw_cards_board = false;
                            }

                            _draw_task.draw_ready_status_users = false;

                            _draw_task.getRoomInfo().count_deck_of_cards = count_cards_in_desck;
                            _draw_task.draw_trump_card_in_the_deck = count_cards_in_desck > 0;

                                /*synchronized (_draw_task.lock_animation_fly_money)
                                {
                                    for(int k = 0; k < _draw_task.getRoomInfo().users_in_room.size(); k++)
                                    {
                                        RoomInfo_User user = _draw_task.getRoomInfo().users_in_room.get(k);

                                        for(int j = 0; j < 5; j++)
                                        {
                                            AnimationImgToUsers tm = new AnimationImgToUsers(
                                                    _draw_task.getMoneyIcon()
                                                    , 0
                                                    , _draw_task.canvas_h2
                                                    , (int) user.pos_x
                                                    , (int) user.pos_y
                                                    , j * 300 + k * 100
                                            );

                                            _draw_task.list_animation_fly_money.add(tm);
                                        }


                                    }

                                    _draw_task.flag_animation_fly_money = true;
                                }*/


                            _draw_task.hideWaitTime();

                            _draw_task.last_cmd_server = "START_GAME";
                            _draw_task.getRoomInfo().current_pos_user_main_xod = current_pos_user_main_xod;
                            _draw_task.getRoomInfo().current_pos_user_xod = current_pos_user_xod;
                            _draw_task.getRoomInfo().current_pos_user_beat = current_pos_user_beat;
                            _draw_task.stat_game = true;
                            _draw_task.getRoomInfo().count_bito_bery = 0;

                            synchronized (_draw_task.getRoomInfo().lock_list_matrix_users_cards)
                            {
                                for (int k = 0; k < _draw_task.getRoomInfo().users_in_room.size(); k++)
                                {
                                    _draw_task.getRoomInfo().users_in_room.get(k).tmp_count_cards = 6;
                                }
                            }

                            _draw_task.needRebuildBackground = true;

                            //Log.i("TAG", String.valueOf(_draw_task.getRoomInfo().current_my_position) + " - " + String.valueOf(_draw_task.getRoomInfo().current_pos_user_xod));

                            if (_draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_xod)
                            {
                                _draw_task.available_select_my_card = true;

                                ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.cards_user.size());

                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                                        if (tv_0 != null)
                                        {
                                            tv_0.setTextColor(Color.GREEN);
                                            tv_0.setText(getString(R.string.txt_85));
                                        }
                                    }
                                });

                                if (app_setting.opt_vibration)
                                {
                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(100);
                                }

                                if (app_setting.opt_sound_ready_stroke && myxod != null && ! call_phone)
                                {
                                    myxod.start();
                                }
                            }
                            else
                            {
                                _draw_task.resetAllUsersTimers();
                                RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_xod);

                                if (ri != null)
                                {
                                    ri.startTimer(WAIT_SECOND + 6);
                                }

                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                        TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                                        if (tv_0 != null)
                                        {
                                            tv_0.setText("");
                                        }
                                    }
                                });
                            }

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    /*ImageView iv = findViewById(R.id.iv_page_room_list_icons_open);
                                    if (iv != null)
                                    {
                                        iv.setVisibility(View.VISIBLE);
                                    }*/

                                    if (_draw_task != null)
                                    {
                                        _draw_task.StartGame();
                                    }

                                    if (btn_ready_play_game != null)
                                    {
                                        btn_ready_play_game.setVisibility(View.GONE);
                                        //ch_ready_play = null;
                                    }
                                }
                            });


                        }
                    }
                };
                wt.start();
            }
        }
        else if (r.cmd.equalsIgnoreCase(getString(R.string.cmd_5)))
        {
            if (r.data != null)
            {
                _draw_task.last_cmd_server = getString(R.string.cmd_5);

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_hide_all_btns();
                        ui_stop_timer_waiting_xod();
                    }
                });


                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_read = _len[0];
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                String result_cmd = new String(_len);

                if (result_cmd.equals("ERROR"))
                {
                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int card = buffer.getInt();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int place_the_cards_in_my_hand = buffer.getInt();

                    _draw_task.resetHoveredCardSelected();

                    CardInfo ci = Cards.getInfoCard(card);

                    synchronized (_draw_task.getRoomInfo().lock_draw)
                    {
                        _draw_task.getRoomInfo().my_userinfo.resetMatrixDrawCardsUser();
                        _draw_task.getRoomInfo().my_userinfo.cards_user.add(place_the_cards_in_my_hand, ci);
                        _draw_task.getRoomInfo().my_userinfo.cards_points.add(place_the_cards_in_my_hand, new CardDraw_RectPoint());

                        for (int k = 0; k < _draw_task.getRoomInfo().cards_boards.size(); k++)
                        {
                            ItemBoardCard tmp = _draw_task.getRoomInfo().cards_boards.get(k);

                            if (tmp.c1 != null
                                    && tmp.c2 == null
                                    && tmp.c1.card_info.card == card)
                            {
                                tmp.c1 = null;
                                _draw_task.getRoomInfo().cache_draw_cards_board = false;
                                _draw_task.getRoomInfo().cards_boards.remove(k);

                                break;
                            }
                            else if (tmp.c1 != null
                                    && tmp.c2 != null
                                    && tmp.c2.card_info.card == card)
                            {
                                tmp.c2 = null;
                                break;
                            }

                        }

                        _draw_task.getRoomInfo().cache_draw_cards_board = false;
                    }

                    if (_draw_task != null)
                    {
                        _draw_task.available_select_my_card = true;
                    }

                    /*for (int k = 0; k < _draw_task.animated_card.size(); k++)
                    {
                        if (_draw_task.animated_card.get(k) != null)
                        {
                            _draw_task.animated_card.get(k).up = false;
                        }
                    }*/
                }
                else
                {
                    if (_draw_task != null)
                    {
                        _draw_task.available_select_my_card = false;
                    }


                }

            }
        }
        else if (r.cmd.equalsIgnoreCase(getString(R.string.cmd_8)))
        {
            if (r.data != null)
            {
                _draw_task.last_cmd_server = getString(R.string.cmd_8);

                /*runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_enable_all_btns(true);
                    }
                });*/

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_read = _len[0];
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                String result_cmd = new String(_len);

                if (result_cmd.equals("ERROR"))
                {
                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int card = buffer.getInt();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int place_the_cards_in_my_hand = buffer.getInt();

                    CardInfo ci = Cards.getInfoCard(card);

                    synchronized (_draw_task.getRoomInfo().lock_draw)
                    {
                        _draw_task.getRoomInfo().my_userinfo.resetMatrixDrawCardsUser();
                        _draw_task.getRoomInfo().my_userinfo.cards_user.add(place_the_cards_in_my_hand, ci);
                        _draw_task.getRoomInfo().my_userinfo.cards_points.add(place_the_cards_in_my_hand, new CardDraw_RectPoint());

                        synchronized (_draw_task.getRoomInfo().lock_cards_boards)
                        {
                            for (int k = 0; k < _draw_task.getRoomInfo().cards_boards.size(); k++)
                            {
                                ItemBoardCard tmp = _draw_task.getRoomInfo().cards_boards.get(k);

                                if (tmp.c1 != null
                                        && tmp.c2 == null
                                        && tmp.c1.card_info.card == card)
                                {
                                    tmp.c1 = null;
                                    _draw_task.getRoomInfo().cache_draw_cards_board = false;
                                    _draw_task.getRoomInfo().cards_boards.remove(k);

                                    break;
                                }
                                else if (tmp.c1 != null
                                        && tmp.c2 != null
                                        && tmp.c2.card_info.card == card)
                                {
                                    tmp.c2 = null;
                                    break;
                                }

                            }
                            _draw_task.getRoomInfo().cache_draw_cards_board = false;
                        }
                    }

                    if (_draw_task != null)
                    {
                        _draw_task.available_select_my_card = true;

                        _draw_task.resetAllUsersTimers();
                        RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_beat);
                        ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);
                    }
                }
                else
                {
                    offset += 4;
                    offset += 4;

                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int can_toss = _len[0];

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int current_pos_user_main_xod = _len[0];

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int current_pos_user_xod = _len[0];

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int current_pos_user_beat = _len[0];

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int count_users_in_room = _len[0];

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ui_page_room_hide_all_btns();
                            ui_stop_timer_waiting_xod();
                        }
                    });

                    if (_draw_task != null)
                    {
                        synchronized (_draw_task.getRoomInfo().lock_list_matrix_users_cards)
                        {
                            for (int k = 0; k < count_users_in_room; k++)
                            {
                                _len = ByteBuffer.allocate(4).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                ByteBuffer buffer = ByteBuffer.wrap(_len);
                                buffer.order(ByteOrder.BIG_ENDIAN);

                                int __uid = buffer.getInt();

                                _len = ByteBuffer.allocate(1).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                int __count_cards = _len[0];

                                _draw_task.getRoomInfo().setCountCardsInUser(__uid, __count_cards);
                            }

                            _draw_task.needRebuildBackground = true;
                        }

                        _draw_task.getRoomInfo().current_pos_user_xod = current_pos_user_xod;
                        _draw_task.getRoomInfo().current_pos_user_beat = current_pos_user_beat;
                        _draw_task.getRoomInfo().current_pos_user_main_xod = current_pos_user_main_xod;

                        _draw_task.resetAllUsersTimers();
                        RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_beat);
                        ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);

                        //_draw_task.needRebuildBackground = true;

                        if (can_toss == 1)
                        {
                            _draw_task.available_select_my_card = true;

                            _draw_task.mode_toss2 = 1;

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                                    if (tv_0 != null)
                                    {
                                        tv_0.setTextColor(Color.GREEN);
                                        tv_0.setText(getString(R.string.txt_85));
                                    }
                                }
                            });
                        }
                        else
                        {
                            _draw_task.available_select_my_card = false;
                        }
                    }


                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("TOSSED_USER_XOD"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();


                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_main_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_beat = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int toss_user = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                //int current_pos_user_beat_press_bery = _len[0];
                int beat_now_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int allCardsIsBeat = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int pressed_bery = _len[0];


                _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int card = buffer.getInt();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int uid = buffer.getInt();

                _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int uid_count_cards = _len[0];

                /*int uid_to_transfer_foll = 0;

                if( _draw_task.getRoomInfo().type_game == RoomInfo.TYPE_GAME__TRANSFER_FOOL )
                {
                    System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    uid_to_transfer_foll = buffer.getInt();
                }*/

                if (_draw_task != null)
                {
                    _draw_task.getRoomInfo().current_pos_user_xod = current_pos_user_xod;
                    _draw_task.getRoomInfo().current_pos_user_beat = current_pos_user_beat;
                    _draw_task.getRoomInfo().current_pos_user_main_xod = current_pos_user_main_xod;

                    synchronized (_draw_task.getRoomInfo().lock_list_matrix_users_cards)
                    {
                        _draw_task.getRoomInfo().setCountCardsInUser(uid, uid_count_cards);
                    }

                    _draw_task.resetAllUsersTimers();
                    _draw_task.needRebuildBackground = true;

                    if (pressed_bery == 0 && (beat_now_xod == 1 || allCardsIsBeat == 0))
                    {
                        RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_beat);
                        ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);
                    }
                    else
                    {
                        RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_xod);
                        ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);
                    }

                    _draw_task.last_cmd_server = "TOSSED_USER_XOD";

                    RoomInfo_User ruinfo = _draw_task.getUserFromOffsetPosition(toss_user);

                    /*if (
                             _draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_beat
                         &&  current_pos_user_beat_press_bery == 0
                       )
                    {
                        //action_end_animation_fly = ACTION_END_ANIMATION_FLY_SHOW_BTN_BERY;

                        Thread th = new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                do
                                {
                                    try
                                    {
                                        Thread.sleep(200);
                                    }
                                    catch (InterruptedException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                while( _draw_task.waitFlyCardToBoard() );

                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Button btn_bery = (Button) findViewById(R.id.btn_page_room_bery);

                                        if( btn_bery == null ) { return; }

                                        btn_bery.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                    }*/

                    _draw_task.run_tossed_xod(ruinfo, card, true);
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("RUN_BEATS_XOD"))
        {
            //if (r.data != null)
            {
                if (_draw_task != null)
                {
                    _draw_task.last_cmd_server = "RUN_BEATS_XOD";

                    _draw_task.available_select_my_card = true;

                    _draw_task.mode_toss2 = 0;
                }

                if( _draw_task != null )
                {
                    ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.cards_user.size());

                    _draw_task.highLightBeadMyCards();
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                        if (tv_0 != null)
                        {
                            tv_0.setTextColor(Color.GREEN);
                            tv_0.setText(getString(R.string.txt_85));
                        }

                        Button btn = (Button) findViewById(R.id.btn_page_room_bery);
                        btn.setVisibility(View.VISIBLE);
                    }
                });

                if (app_setting.opt_vibration)
                {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);
                }

                if (app_setting.opt_sound_ready_stroke && myxod != null && ! call_phone)
                {
                    myxod.start();
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase(getString(R.string.cmd_9)))
        {
            if (r.data != null)
            {
                _draw_task.resetAllUsersTimers();
                _draw_task.last_cmd_server = getString(R.string.cmd_9);
                _draw_task.resetHoveredCardSelected();

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //ui_page_room_enable_all_btns(true);
                        ui_stop_timer_waiting_xod();
                    }
                });

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_read = _len[0];
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                String result_cmd = new String(_len);

                if (result_cmd.equals("ERROR"))
                {
                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int card = buffer.getInt();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int place_the_cards_in_my_hand = buffer.getInt();

                    CardInfo ci = Cards.getInfoCard(card);

                    synchronized (_draw_task.getRoomInfo().lock_draw)
                    {
                        synchronized (_draw_task.getRoomInfo().lock_cards_boards)
                        {
                            _draw_task.getRoomInfo().my_userinfo.resetMatrixDrawCardsUser();
                            _draw_task.getRoomInfo().my_userinfo.cards_user.add(place_the_cards_in_my_hand, ci);
                            _draw_task.getRoomInfo().my_userinfo.cards_points.add(place_the_cards_in_my_hand, new CardDraw_RectPoint());

                            for (int k = 0; k < _draw_task.getRoomInfo().cards_boards.size(); k++)
                            {
                                ItemBoardCard tmp = _draw_task.getRoomInfo().cards_boards.get(k);

                                if (tmp.c1 != null
                                        && tmp.c2 == null
                                        && tmp.c1.card_info.card == card)
                                {
                                    tmp.c1 = null;
                                    _draw_task.getRoomInfo().cache_draw_cards_board = false;
                                    _draw_task.getRoomInfo().cards_boards.remove(k);

                                    break;
                                }
                                else if (tmp.c1 != null
                                        && tmp.c2 != null
                                        && tmp.c2.card_info.card == card)
                                {
                                    tmp.c2 = null;
                                    break;
                                }

                            }

                            /// есои ошибка то надо ходить снова другой картой
                            if (_draw_task != null)
                            {
                                _draw_task.available_select_my_card = true;
                            }

                            _draw_task.getRoomInfo().cache_draw_cards_board = false;
                        }
                    }
                }
                else
                {
                    offset += 4; // skip
                    offset += 4; // skip

                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int can_toss = _len[0];

                    //Log.i("TAG36", String.valueOf(can_toss));

                    if (can_toss == 1 && _draw_task != null)
                    {
                        _draw_task.available_select_my_card = true;

                        ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.cards_user.size());

                        if (app_setting.opt_vibration)
                        {
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(20);
                        }

                        if (app_setting.opt_sound_ready_stroke && myxod != null && ! call_phone)
                        {
                            myxod.start();
                        }

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Button btn_bery = (Button) findViewById(R.id.btn_page_room_bery);

                                if (btn_bery == null)
                                {
                                    return;
                                }

                                btn_bery.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    else if (can_toss == 0 && _draw_task != null)
                    {
                        _draw_task.available_select_my_card = false;

                        RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_xod);
                        ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ui_page_room_hide_all_btns();
                            }
                        });

                    }
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("BEATED_USER_XOD"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();


                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_main_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_beat = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int beat_now_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int allCardsIsBeat = _len[0];


                _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int card = buffer.getInt();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int card_hovered = buffer.getInt();

                _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int uid = buffer.getInt(); // кто ходил


                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int uid_count_cards = _len[0];


                if (_draw_task != null)
                {
                    _draw_task.resetAllUsersTimers();

                    _draw_task.getRoomInfo().current_pos_user_xod = current_pos_user_xod;
                    _draw_task.getRoomInfo().current_pos_user_beat = current_pos_user_beat;
                    _draw_task.getRoomInfo().current_pos_user_main_xod = current_pos_user_main_xod;

                    synchronized (_draw_task.getRoomInfo().lock_list_matrix_users_cards)
                    {
                        _draw_task.getRoomInfo().setCountCardsInUser(uid, uid_count_cards);
                    }

                    _draw_task.needRebuildBackground = true;

                    _draw_task.last_cmd_server = "BEATED_USER_XOD";
                    //_draw_task.releaseUp();

                    if (beat_now_xod == 1 || allCardsIsBeat == 0)
                    {
                        RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_beat);
                        ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);
                    }
                    else
                    {
                        RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_xod);
                        ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);
                    }

                    RoomInfo_User ruinfo = _draw_task.getUserFromOffsetPosition(current_pos_user_beat);

                    if (beat_now_xod == 0 && _draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_xod)
                    {
                        if (_draw_task.getRoomInfo().allCardsIsBeated())
                        {
                            if (_draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_main_xod)
                            {
                                //action_end_animation_fly = ACTION_END_ANIMATION_FLY_SHOW_BTN_BITO;

                                Thread th = new Thread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        do
                                        {
                                            try
                                            {
                                                Thread.sleep(200);
                                            } catch (InterruptedException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                        while (_draw_task != null && _draw_task.waitFlyCardToBoard());

                                        if (_draw_task == null)
                                        {
                                            return;
                                        }

                                        runOnUiThread(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                Button btn_bery = (Button) findViewById(R.id.btn_page_room_bito);

                                                if (btn_bery == null)
                                                {
                                                    return;
                                                }

                                                btn_bery.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                });
                            }
                            else
                            {
                                //action_end_animation_fly = ACTION_END_ANIMATION_FLY_SHOW_BTN_THAT_ALL;

                                /*Thread th = new Thread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        do
                                        {
                                            try
                                            {
                                                Thread.sleep(200);
                                            }
                                            catch (InterruptedException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                        while( _draw_task != null && _draw_task.waitFlyCardToBoard() );

                                        ui_show_btn_only__that_s_all();
                                    }
                                });*/
                            }
                        }
                    }

                    _draw_task.run_tossed_xod2(ruinfo, card, card_hovered, false);
                }
            }
        }
        //
        else if (r.cmd.equalsIgnoreCase("RUN_NEXT_XOD"))
        {
            if (_draw_task != null && r.data != null)
            {
                _draw_task.last_cmd_server = "RUN_NEXT_XOD";

                _draw_task.available_select_my_card = true;


                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_main_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_beat = _len[0];

                _draw_task.getRoomInfo().current_pos_user_xod = current_pos_user_xod;
                _draw_task.getRoomInfo().current_pos_user_beat = current_pos_user_beat;
                _draw_task.getRoomInfo().current_pos_user_main_xod = current_pos_user_main_xod;

                Thread th = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        do
                        {
                            try
                            {
                                Thread.sleep(200);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        while (_draw_task != null && _draw_task.haveAnimationFlyUserCard());

                        synchronized (lock_drawTask)
                        {

                            if (_draw_task == null)
                            {
                                return;
                            }

                            ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.cards_user.size());

                            _draw_task.resetAllUsersTimers();
                            RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_xod);
                            ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);

                            _draw_task.needRebuildBackground = true;

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    TextView tv_0 = findViewById(R.id.tv_page_room_status_txt2);

                                    if (tv_0 != null)
                                    {
                                        tv_0.setTextColor(Color.YELLOW);
                                        tv_0.setText(getString(R.string.txt_101));
                                    }

                                    //Log.i("TAG", "_draw_task.getRoomInfo().allCardsIsBeated(): " + String.valueOf(_draw_task.getRoomInfo().allCardsIsBeated()));

                                    if (_draw_task.getRoomInfo().allCardsIsBeated())
                                    {
                                         ui_page_room_show_btn(TYPE_BTN_BITO);
                                    }
                                }
                            });

                            if (app_setting.opt_vibration)
                            {
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(20);
                            }

                            if (app_setting.opt_sound_ready_stroke && myxod != null && ! call_phone)
                            {
                                myxod.start();
                            }
                        }
                    }
                });

                th.start();
            }
        }
        else if (r.cmd.equalsIgnoreCase("STATUS_ROOM"))
        {
            if (_draw_task != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int status_room = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_main_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_beat = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_cards_in_desck = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_cards_in_board = _len[0];

                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_bito_bery = _len[0];

                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                final int beat_now_xod = _len[0];

                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_users_in_room = _len[0];

                synchronized (_draw_task.getRoomInfo().lock_list_matrix_users_cards)
                {
                    for (int k = 0; k < count_users_in_room; k++)
                    {
                        _len = ByteBuffer.allocate(4).array();
                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        ByteBuffer buffer = ByteBuffer.wrap(_len);
                        buffer.order(ByteOrder.BIG_ENDIAN);

                        int __uid = buffer.getInt();

                        _len = ByteBuffer.allocate(1).array();
                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        int __count_cards = _len[0];

                        if (_draw_task.getRoomInfo().my_userinfo.uid == __uid)
                        {
                            _draw_task.getRoomInfo().my_userinfo.tmp_count_cards = __count_cards;
                            //_draw_task.needRebuildBackground = true;
                        }
                        else
                        {
                            _draw_task.getRoomInfo().setCountCardsInUser(__uid, __count_cards);
                        }
                    }

                    _draw_task.getRoomInfo().resetUsersInRoomDrawTextBery();
                }

                _draw_task.getRoomInfo().status_room = status_room;
                _draw_task.resetHoveredCardSelected();
                //_draw_task.needRebuildBackground = true;
                //_draw_task.getRoomInfo().my_userinfo.cached_draw_my_cards = false;

                _draw_task.getRoomInfo().count_bito_bery = count_bito_bery;
                _draw_task.getRoomInfo().count_deck_of_cards = count_cards_in_desck;
                _draw_task.getRoomInfo().current_pos_user_xod = current_pos_user_xod;
                _draw_task.getRoomInfo().current_pos_user_beat = current_pos_user_beat;
                _draw_task.getRoomInfo().current_pos_user_main_xod = current_pos_user_main_xod;

                if (_draw_task.getRoomInfo().count_deck_of_cards == 0)
                {
                    _draw_task.draw_trump_card_in_the_deck = false;
                    _draw_task.needRebuildBackground = true;
                }

                _draw_task.available_select_my_card = false;

                _draw_task.stopSelectAnimationCards();

                _draw_task.needRebuildBackground = true;

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_hide_all_btns();
                    }
                });

                _draw_task.resetAllUsersTimers();

                //Log.i("TAG36", "STATUS_ROOM: " + String.valueOf(beat_now_xod) + " " + String.valueOf(_draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_xod));

                if (
                        beat_now_xod == 0
                                && (
                                _draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_xod
                                //|| _draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_beat
                        )
                        )
                {
                    //Log.i("TAG36", "a)");

                    _draw_task.available_select_my_card = true;

                    _draw_task.mode_toss2 = 0;

                    synchronized (_draw_task.getRoomInfo().lock_draw)
                    {
                        _draw_task.highLightResetAll();
                    }

                    //if( count_cards_in_board == 0 )
                    //{
                    ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.tmp_count_cards);
                    //}

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                            if (tv_0 != null)
                            {
                                tv_0.setTextColor(Color.GREEN);
                                tv_0.setText(getString(R.string.txt_85));
                            }
                        }
                    });

                    if (app_setting.opt_vibration)
                    {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                    }

                    if (app_setting.opt_sound_ready_stroke && myxod != null && ! call_phone)
                    {
                        myxod.start();
                    }
                }
                else
                {
                    //Log.i("TAG36", "b)");

                    if (beat_now_xod == 1 && _draw_task.getRoomInfo().current_my_position == _draw_task.getRoomInfo().current_pos_user_beat)
                    {
                        //Log.i("TAG36", "c)");

                        _draw_task.available_select_my_card = true;

                        _draw_task.mode_toss2 = 0;

                        ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.tmp_count_cards);
                    }
                    else
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ui_stop_timer_waiting_xod();

                                TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                                if (tv_0 != null)
                                {
                                    tv_0.setText("");
                                }
                            }
                        });

                        if (beat_now_xod == 1)
                        {
                            RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_beat);
                            ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);
                        }
                        else
                        {
                            RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_xod);
                            ri.startTimer(WAIT_SECOND + ri.tmp_count_cards);
                        }

                        if (count_cards_in_board == 0)
                        {
                            synchronized (_draw_task.getRoomInfo().lock_draw)
                            {
                                _draw_task.highLightResetAll();
                            }
                        }
                    }

                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("RUN_BERY"))
        {
            if (_draw_task != null)
            {
                _draw_task.resetAllUsersTimers();
                _draw_task.last_cmd_server = "RUN_BERY";

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_users1 = _len[0];

                for (int k = 0; k < count_users1; k++)
                {
                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int uid = buffer.getInt();

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int count_need_card_of_deck = _len[0];

                    RoomInfo_User ruinfo = _draw_task.getInfoUser(uid);

                    for (int f = 0; f < count_need_card_of_deck; f++)
                    {
                        AnimatedFlyDeck a = new AnimatedFlyDeck(
                                getResources()
                                , Cards.getDrawableResId_FromOtherCard(_draw_task.CARD_BLANK)

                                , -5 * _scale_px
                                , _draw_task.canvas_h2

                                , ruinfo.pos_x
                                , ruinfo.pos_y

                                , 0.64f
                                , 0.45f
                                , System.currentTimeMillis() + ((f + 1) + (k + 1)) * 200
                        );

                        synchronized (_draw_task.lock_my_animated_card_desk)
                        {
                            _draw_task.animated_card_desk.add(a);
                        }
                    }

                }

                //*********
                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_add_card = _len[0];

                List<Short> cards_users_add = new ArrayList<>();

                synchronized (_draw_task.getRoomInfo().lock_draw)
                {
                    for (int k = 0; k < count_add_card; k++)
                    {
                        _len = ByteBuffer.allocate(1).array();
                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        int type_card = _len[0];

                        _len = ByteBuffer.allocate(4).array();
                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        ByteBuffer buffer = ByteBuffer.wrap(_len);
                        buffer.order(ByteOrder.BIG_ENDIAN);

                        short card = (short) buffer.getInt();

                        cards_users_add.add(card);

                        CardInfo ci = Cards.getInfoCard(card);
                        CardDraw_RectPoint cp = new CardDraw_RectPoint();
                        cp.need_draw_card = false;

                        if (type_card == 0)
                        {
                            ci.draw_icon_new_card = true;
                        }
                        else
                        {
                            ci.draw_icon_new_card2 = true;
                        }
                        ci.timestamp_add = 0;

                        /*if( _draw_task.getRoomInfo().validateTossXodOnBoard( ci.card ) )
                        {
                            ci.draw_icon_tossed_card = true;
                        }*/

                        _draw_task.getRoomInfo().my_userinfo.resetMatrixDrawCardsUser();
                        _draw_task.getRoomInfo().my_userinfo.cards_user.add(ci);
                        _draw_task.getRoomInfo().my_userinfo.cards_points.add(cp);
                    }

                    /**/
                }

                /*_len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);  offset += _len.length;

                int current_pos_user_xod = _len[0];*/

                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ///кто берет карты
                final int current_pos_user_beat = _len[0];

                /**/

                //---------------------

                _draw_task.available_select_my_card = false;

                _draw_task.mode_toss2 = 0;

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_hide_all_btns();
                    }
                });


                Thread th = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        do
                        {
                            try
                            {
                                Thread.sleep(200);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        while (_draw_task != null && _draw_task.haveAnimationFlyUserCard());

                        try
                        {
                            Thread.sleep(200);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        synchronized (lock_drawTask)
                        {

                            if (_draw_task == null)
                            {
                                return;
                            }

                            // убрать все карты с поля к пользователю

                            List<CardDrawInfo> l = new ArrayList<>();

                            synchronized (_draw_task.getRoomInfo().lock_cards_boards)
                            {
                                for (int g = 0; g < _draw_task.getRoomInfo().cards_boards.size(); g++)
                                {
                                    if (!_draw_task.getRoomInfo().cards_boards.get(g).need_draw1)
                                    {
                                        continue;
                                    }

                                    if (_draw_task.getRoomInfo().cards_boards.get(g).c1 != null)
                                    {
                                        l.add(_draw_task.getRoomInfo().cards_boards.get(g).c1);
                                    }

                                    if (_draw_task.getRoomInfo().cards_boards.get(g).c2 != null)
                                    {
                                        l.add(_draw_task.getRoomInfo().cards_boards.get(g).c2);
                                    }
                                }
                            }

                            RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(current_pos_user_beat);

                            if (ri.uid != _draw_task.getRoomInfo().my_userinfo.uid)
                            {
                                synchronized (_draw_task.lock_adraw_animated_texts)
                                {
                                    _draw_task.adraw_animated_texts.add(new AminatedTextRoundedRectangle(_fontApp, getString(R.string.txt_49), ri.pos_x, ri.pos_y, 16.0f * _scale_px, 900, 1.7f));
                                    _draw_task.flag_adraw_animated_texts = true;
                                }
                            }

                            _draw_task.fly_card_off_board = new AnimationFlyCardsOutOfBoard(
                                    14
                                    , ri.pos_x
                                    , ri.pos_y
                                    , l
                            );

                            _draw_task.animation_fly_card_off_board = true;

                            if (app_setting.opt_sound_in_game && deal_gin != null && ! call_phone)
                            {
                                deal_gin.start();
                            }

                            // анимация раздачи карт пользователям

                            _draw_task.animation_fly_desc_card = true;

                            if (_draw_task.animated_card_desk.size() > 4)
                            {
                                if (app_setting.opt_sound_in_game && fly_card_to_users != null && ! call_phone)
                                {
                                    fly_card_to_users.start();
                                }
                            }
                            else
                            {
                                if (app_setting.opt_sound_in_game && fly_card_to_users_2 != null && ! call_phone)
                                {
                                    fly_card_to_users_2.start();
                                }
                            }
                        }
                    }
                });

                th.start();
                //--------------
            }
        }
        else if (r.cmd.equalsIgnoreCase("RUN_BITO"))
        {
            if (_draw_task != null && r.data != null)
            {
                _draw_task.resetAllUsersTimers();
                _draw_task.last_cmd_server = "RUN_BITO";
                _draw_task.available_select_my_card = false;

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_hide_all_btns();
                        ui_stop_timer_waiting_xod();
                    }
                });

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_users1 = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int user_pressed_bito = _len[0]; ///кто нажал "бито"

                RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(user_pressed_bito);

                if (ri.uid != _draw_task.getRoomInfo().my_userinfo.uid)
                {
                    synchronized (_draw_task.lock_adraw_animated_texts)
                    {
                        _draw_task.adraw_animated_texts.add(new AminatedTextRoundedRectangle(_fontApp, getString(R.string.txt_48), ri.pos_x, ri.pos_y, 16.0f * _scale_px, 900, 1.7f));
                        _draw_task.flag_adraw_animated_texts = true;
                    }
                }

                _draw_task.animated_card_desk.clear();

                // полет карт к пользователям
                for (int k = 0; k < count_users1; k++)
                {
                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int uid = buffer.getInt();

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int count_need_card_of_deck = _len[0];

                    //Log.i("TAG", ">> " + String.valueOf(uid) + " " + String.valueOf( count_need_card_of_deck ) );

                    RoomInfo_User ruinfo = _draw_task.getInfoUser(uid);

                    for (int f = 0; f < count_need_card_of_deck; f++)
                    {
                        AnimatedFlyDeck a = new AnimatedFlyDeck(
                                getResources()
                                , Cards.getDrawableResId_FromOtherCard(_draw_task.CARD_BLANK)

                                , -5 * _scale_px
                                , _draw_task.canvas_h2

                                , ruinfo.pos_x
                                , ruinfo.pos_y

                                , 0.64f
                                , 0.45f
                                , System.currentTimeMillis() + ((f + 1) + (k + 1)) * 200
                        );

                        synchronized (_draw_task.lock_my_animated_card_desk)
                        {
                            _draw_task.animated_card_desk.add(a);
                        }
                    }

                }

                // добавим карты
                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_add_card = _len[0];

                List<Short> cards_users_add = new ArrayList<>();

                synchronized (_draw_task.getRoomInfo().lock_draw)
                {
                    for (int k = 0; k < count_add_card; k++)
                    {
                        _len = ByteBuffer.allocate(4).array();
                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        ByteBuffer buffer = ByteBuffer.wrap(_len);
                        buffer.order(ByteOrder.BIG_ENDIAN);

                        short card = (short) buffer.getInt();

                        cards_users_add.add(card);

                        CardInfo ci = Cards.getInfoCard(card);
                        CardDraw_RectPoint cp = new CardDraw_RectPoint();
                        cp.need_draw_card = false;
                        ci.draw_icon_new_card = true;
                        ci.timestamp_add = 0;

                        /*if( _draw_task.getRoomInfo().validateTossXodOnBoard( ci.card ) )
                        {
                            ci.draw_icon_tossed_card = true;
                        }*/


                        _draw_task.getRoomInfo().my_userinfo.cards_user.add(ci);
                        _draw_task.getRoomInfo().my_userinfo.cards_points.add(cp);
                        _draw_task.getRoomInfo().my_userinfo.resetMatrixDrawCardsUser();
                    }

                    /*
                    // TODO: NOT USED
                    int sotr_cards = app_setting.opt_sort_my_cards;


                    if( sotr_cards == AppSettings.SORT_MY_CARDS_ABC)
                    {
                        _draw_task.getRoomInfo().my_userinfo.sortCardsABC();
                    }
                    else if( sotr_cards == AppSettings.SORT_MY_CARDS_DESC)
                    {
                        _draw_task.getRoomInfo().my_userinfo.sortCardsDESC();
                    }

                    //******************************************************************************

                    int sort_trump_catds = app_setting.opt_sort_trump_cards;

                    if( sort_trump_catds == AppSettings.SORT_TRUMP_CARDS_LEFT )
                    {
                        _draw_task.getRoomInfo().my_userinfo.sortTrumpCardsLeft( _draw_task.getRoomInfo().trump_card_in_the_deck );
                    }
                    else if( sort_trump_catds == AppSettings.SORT_TRUMP_CARDS_RIGHT )
                    {
                        _draw_task.getRoomInfo().my_userinfo.sortTrumpCardsOnRight( _draw_task.getRoomInfo().trump_card_in_the_deck );
                    }

                    _draw_task.needRebuildBackground = true;*/
                }

                /*_len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);  offset += _len.length;

                int current_pos_user_xod = _len[0];

                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);  offset += _len.length;

                int current_pos_user_beat = _len[0];*/

                //---------------------

                // убрать все карты с поля - в отбой
                Thread th = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        do
                        {
                            try
                            {
                                Thread.sleep(200);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        while (_draw_task != null && _draw_task.waitFlyCardToBoard());

                        synchronized (lock_drawTask)
                        {
                            if (_draw_task == null)
                            {
                                return;
                            }

                            try
                            {
                                Thread.sleep(300);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }

                            List<CardDrawInfo> l = new ArrayList<>();
                            CardDrawInfo _cdi = _draw_task.infoOrientation.get(_draw_task.getOrietrationScreen()).list_cards_draw.get(Cards.BLANK);

                            synchronized (_draw_task.getRoomInfo().lock_cards_boards)
                            {
                                for (int g = 0; g < _draw_task.getRoomInfo().cards_boards.size(); g++)
                                {
                                    if (!_draw_task.getRoomInfo().cards_boards.get(g).need_draw1)
                                    {
                                        continue;
                                    }

                                    if (_draw_task.getRoomInfo().cards_boards.get(g).c1 != null)
                                    {
                                        _draw_task.getRoomInfo().cards_boards.get(g).c1.img = _cdi.img;
                                        l.add(_draw_task.getRoomInfo().cards_boards.get(g).c1);
                                    }

                                    if (_draw_task.getRoomInfo().cards_boards.get(g).c2 != null)
                                    {
                                        _draw_task.getRoomInfo().cards_boards.get(g).c2.img = _cdi.img;
                                        l.add(_draw_task.getRoomInfo().cards_boards.get(g).c2);
                                    }
                                }

                                _draw_task.getRoomInfo().cache_draw_cards_board = false;
                            }

                            _draw_task.getRoomInfo().real_count_cards_off_game += _draw_task.getRoomInfo().cards_boards.size() * 2;
                            _draw_task.generate_off_boards_cards();

                            _draw_task.fly_card_off_board = new AnimationFlyCardsOutOfBoard(
                                    7
                                    , _draw_task.canvas_w
                                    , _draw_task.canvas_h2
                                    , l
                            );

                            //_draw_task.getRoomInfo().cache_draw_cards_board = false;
                            _draw_task.animation_fly_card_off_board = true;

                            if (app_setting.opt_sound_in_game && deal_gin != null && ! call_phone)
                            {
                                deal_gin.start();
                            }

                            // анимация раздачи карт пользователям

                            _draw_task.animation_fly_desc_card = true;

                            if (_draw_task.animated_card_desk.size() > 4)
                            {
                                if (app_setting.opt_sound_in_game && fly_card_to_users != null && ! call_phone)
                                {
                                    fly_card_to_users.start();
                                }
                            }
                            else
                            {
                                if (app_setting.opt_sound_in_game && fly_card_to_users_2 != null && ! call_phone)
                                {
                                    fly_card_to_users_2.start();
                                }
                            }

                            _draw_task.mode_toss2 = 0;

                            //ui_page_room_hide_all_btns();

                            _draw_task.needRebuildBackground = true;
                            _draw_task.getRoomInfo().my_userinfo.cached_draw_my_cards = false;

                            /*runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    ui_stop_timer_waiting_xod();
                                }
                            });*/
                        }
                    }
                });

                th.start();


                //_draw_task.resetAnimationOffsetCardBoards();

                //--------------
            }
        }
        else if (
                (r.cmd.equalsIgnoreCase("RUN_TOSS_2_XOD"))
                        || (r.cmd.equalsIgnoreCase("RUN_TOSS_3_XOD"))
                )
        {
            if (_draw_task != null && r.data != null)
            {
                _draw_task.last_cmd_server = r.cmd;

                //_draw_task.getRoomInfo().current_pos_user_xod = _draw_task.getRoomInfo().current_my_position;

                _draw_task.releaseUp();
                _draw_task.resetAllUsersTimers();
                _draw_task.available_select_my_card = false;
                _draw_task.mode_toss2 = 1;

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_main_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_beat = _len[0];

                _draw_task.getRoomInfo().current_pos_user_xod = current_pos_user_xod;
                _draw_task.getRoomInfo().current_pos_user_beat = current_pos_user_beat;
                _draw_task.getRoomInfo().current_pos_user_main_xod = current_pos_user_main_xod;

                synchronized (_draw_task.getRoomInfo().lock_draw)
                {
                    _draw_task.highLightResetAll();
                    _draw_task.highLightTossedMyCards();
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_show_btn(TYPE_BTN_THATS_ALL);
                        ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.cards_user.size());

                        TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                        if (tv_0 != null)
                        {
                            tv_0.setTextColor(Color.YELLOW);
                            tv_0.setText(getString(R.string.txt_101));
                        }
                    }
                });

                if (!(_draw_task.getRoomInfo().cards_boards.size() == 5 && _draw_task.getRoomInfo().count_bito_bery == 0))
                {
                    _draw_task.available_select_my_card = true;
                }

                if (app_setting.opt_vibration)
                {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);
                }

                if (app_setting.opt_sound_ready_stroke && myxod != null && ! call_phone)
                {
                    myxod.start();
                }
            }
        }
        /*else if (r.cmd.equalsIgnoreCase("RUN_TOSS_3_XOD"))
        {
            if( _draw_task != null )
            {
                _draw_task.last_cmd_server = "RUN_TOSS_3_XOD";

                _draw_task.available_select_my_card   = false;
                _draw_task.mode_toss2                 = 2;
                _draw_task.getRoomInfo().current_pos_user_xod = _draw_task.getRoomInfo().current_my_position;

                _draw_task.releaseUp();
                _draw_task.resetAllUsersTimers();

                _draw_task.highLightResetAll();
                _draw_task.highLightTossedMyCards();

                Log.i("TAG", "start ____");

                ui_show_btn_only__that_s_all();

                //Log.i("TAG", "start ___+");

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //Log.i("TAG", "start ++++");

                        TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                        if(tv_0 != null)
                        {
                            tv_0.setText( getString(R.string.txt_85) );
                        }

                        //Log.i("TAG", "start 00000");
                    }
                });

                ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.cards_user.size());

                _draw_task.available_select_my_card   = true;
            }
        }*/
        else if (r.cmd.equalsIgnoreCase("SET_ICON_FROM_USER"))
        {
            if (_draw_task != null && r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(4).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int uid = buffer.getInt();

                _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int icon_num = (int) _len[0] & 0xff;

                RoomInfo_User uinfo = _draw_task.getInfoUserNoMe(uid);

                if (uinfo != null)
                {
                    int res_icon = 0;

                    switch (icon_num)
                    {
                        case 1:
                            res_icon = R.drawable.icon_emotzi_1;
                            break;
                        case 2:
                            res_icon = R.drawable.icon_emotzi_2;
                            break;
                        case 3:
                            res_icon = R.drawable.icon_emotzi_3;
                            break;
                        case 4:
                            res_icon = R.drawable.icon_emotzi_4;
                            break;
                        case 5:
                            res_icon = R.drawable.icon_emotzi_5;
                            break;
                        case 6:
                            res_icon = R.drawable.icon_emotzi_6;
                            break;
                        case 7:
                            res_icon = R.drawable.icon_emotzi_7;
                            break;
                        case 8:
                            res_icon = R.drawable.icon_emotzi_8;
                            break;
                        case 9:
                            res_icon = R.drawable.icon_emotzi_9;
                            break;
                    }

                    if (res_icon != 0)
                    {
                        addSmileToSlot(
                                (int) uinfo.pos_x
                                , (int) uinfo.pos_y
                                , res_icon
                                , 1.4f
                        );
                    }
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("BERY_INFO"))
        {
            if (_draw_task != null && r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(4).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int uid = buffer.getInt();

                RoomInfo_User ru = _draw_task.getInfoUser(uid);
                ru.draw_bery_text = new DrawTextRect(
                        _fontApp_bold2
                        , getString(R.string.txt_49)
                        , 19 * _scale_px
                        , "#000000"
                        , "#000000"
                        , "#000000"
                        , 2
                        , Utils.dipToPixels(MainActivity.this, 20)
                        , Utils.dipToPixels(MainActivity.this, 20)
                );
            }
        }
        else if (r.cmd.equalsIgnoreCase("SHOW_BTN_BERY"))
        {
            if (_draw_task != null && r.data != null)
            {
                _draw_task.last_cmd_server = "SHOW_BTN_BERY";

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos = _len[0];

                _draw_task.resetAllUsersTimers();
                RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(current_pos);
                ri.startTimer(WAIT_SECOND + 6);

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        ui_page_room_show_btn(TYPE_BTN_BERY_2);
                        ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.cards_user.size());
                    }
                });

                if (app_setting.opt_vibration)
                {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);
                }

                if (app_setting.opt_sound_ready_stroke && myxod != null && ! call_phone)
                {
                    myxod.start();
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("SHOW_BTN_BITO"))
        {
            if (_draw_task != null && r.data != null)
            {
                _draw_task.last_cmd_server = "SHOW_BTN_BITO";

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos = _len[0];

                _draw_task.resetAllUsersTimers();
                RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(current_pos);
                ri.startTimer(WAIT_SECOND + 6);

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        ui_page_room_show_btn(TYPE_BTN_BITO_2);
                        ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.cards_user.size());
                    }
                });

                if (app_setting.opt_vibration)
                {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);
                }

                if (app_setting.opt_sound_ready_stroke && myxod != null && ! call_phone)
                {
                    myxod.start();
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("SHOW_BTN_THATS_ALL"))
        {
            if (_draw_task != null && r.data != null)
            {
                _draw_task.last_cmd_server = "SHOW_BTN_THATS_ALL";

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos = _len[0];

                _draw_task.resetAllUsersTimers();
                RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(current_pos);
                ri.startTimer(WAIT_SECOND + 6);

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        ui_page_room_show_btn(TYPE_BTN_THATS_ALL_2);
                        ui_start_timer_waiting_xod(WAIT_SECOND + _draw_task.getRoomInfo().my_userinfo.cards_user.size());
                    }
                });

                if (app_setting.opt_vibration)
                {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);
                }

                if (app_setting.opt_sound_ready_stroke && myxod != null && ! call_phone)
                {
                    myxod.start();
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("OFF_XOD_2"))
        {
            if (_draw_task != null && r.data != null)
            {
                _draw_task.last_cmd_server = "OFF_XOD_2";

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos = _len[0];

                _draw_task.resetAllUsersTimers();
                RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(current_pos);
                ri.startTimer(WAIT_SECOND + 6);

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_hide_all_btns();

                        Log.d("TAG67", android.util.Log.getStackTraceString(new Exception()));
                        TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                        if (tv_0 != null)
                        {
                            tv_0.setText("");
                        }
                    }
                });

            }
        }
        else if (r.cmd.equalsIgnoreCase("OFF_XOD"))
        {
            if (_draw_task != null && r.data != null)
            {
                _draw_task.last_cmd_server = "OFF_XOD";

                _draw_task.available_select_my_card = false;

                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_main_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int current_pos_user_beat = _len[0];

                _draw_task.getRoomInfo().current_pos_user_xod = current_pos_user_xod;
                _draw_task.getRoomInfo().current_pos_user_beat = current_pos_user_beat;
                _draw_task.getRoomInfo().current_pos_user_main_xod = current_pos_user_main_xod;

                /*for (int k = 0; k < _draw_task.animated_card.size(); k++)
                {
                    if (_draw_task.animated_card.get(k) != null)
                    {
                        _draw_task.animated_card.get(k).up = false;
                    }
                }*/

                _draw_task.resetAllUsersTimers();
                RoomInfo_User ri = _draw_task.getUserFromOffsetPosition(_draw_task.getRoomInfo().current_pos_user_xod);
                ri.startTimer(WAIT_SECOND + 6);

                _draw_task.needRebuildBackground = true;

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_page_room_hide_all_btns();

                        /*Log.d("TAG67", android.util.Log.getStackTraceString(new Exception()));
                        TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                        if(tv_0 != null)
                        {
                            tv_0.setText("");
                        }*/
                    }
                });
            }
        }
        else if (r.cmd.equalsIgnoreCase("ADD_MONEY_USER"))
        {
            if (r.data != null && sv_page == SV_PAGE_ROOM)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(4).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int uid = buffer.getInt();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int add_money = buffer.getInt();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int balans = buffer.getInt();

                if (uid == accout_info.uid)
                {
                    int old_balans = (int) accout_info.balans;
                    accout_info.balans = balans;

                    runAnimationNumber((int) accout_info.balans, old_balans);
                }

                List<FlyMoneyInfo> fm = new ArrayList<>();
                fm.add(new FlyMoneyInfo(uid
                        , "+"
                        + (new DecimalFormat("#.#").format(add_money))
                        //+ " "
                        //+ Utils.getCorrectSuffix( (int) add_money, getResources().getString(R.string.txt_68), getResources().getString(R.string.txt_69), getResources().getString(R.string.txt_70))
                ));

                if (_draw_task != null)
                {
                    _draw_task.flyMoneyUsers(fm);

                    if (app_setting.opt_sound_in_game && sound_coins != null && uid == accout_info.uid && ! call_phone)
                    {
                        sound_coins.start();
                    }
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("REQUEST_ADD_FRIEND"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(4).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int uid = buffer.getInt();

                _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_read = (int) _len[0] & 0xff;
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                final String first_name = new String(_len);

                //--------------------------------------------------------------------------

                _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                count_read = (int) _len[0] & 0xff;
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                final String last_name = new String(_len);

                //--------------------------------------------------------------------------

                _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                count_read = (int) _len[0] & 0xff;
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                final String user_photo = new String(_len);

                _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int info_raiting = buffer.getInt();

                boolean found = false;

                if (page_room_request_to_friends != null)
                {
                    for (int i = 0; i < page_room_request_to_friends.size(); i++)
                    {
                        if (page_room_request_to_friends.get(i).uid == uid)
                        {
                            found = true;
                            break;
                        }
                    }
                }

                if (!found)
                {
                    final RequestToFriendInfo uinfo = new RequestToFriendInfo();
                    uinfo.uid = uid;
                    uinfo.first_name = first_name;
                    uinfo.last_name = last_name;
                    uinfo.avatar = _draw_task.getDefaultAvatar().copy(_draw_task.getDefaultAvatar().getConfig(), true);

                    /*uinfo.avatar = Bitmap.createScaledBitmap(
                              _draw_task.getDefaultAvatar()
                            , (int)(60 * _scale_px)
                            , (int)(60 * _scale_px)
                            , true);*/

                    uinfo.photo_url = user_photo;
                    uinfo.iv = new ImageView(MainActivity.this);
                    uinfo.reiting = info_raiting;

                    WaitThread wt = new WaitThread(0)
                    {
                        @Override
                        public void callback()
                        {
                            final Bitmap b = Utils.getCroppedBitmap(Utils.getBitmapFromURL(uinfo.photo_url));

                            if (uinfo == null)
                            {
                                return;
                            }

                            uinfo.avatar = b;
                            uinfo.iv.setImageBitmap(b);
                        }
                    };
                    wt.start();

                    page_room_request_to_friends.add(uinfo);

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ImageView iv = findViewById(R.id.iv_page_room_new_message_icon);
                            iv.setVisibility(View.VISIBLE);
                            amination_new_message_icon();

                            ui_add_list_request_user_in_page_room(uinfo);
                        }
                    });
                }

                load_my_friends();
            }
        }
        else if (r.cmd.equalsIgnoreCase("STOP_GAME"))
        {
            pressed_ready_game = false;

            Thread th = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    do
                    {
                        try
                        {
                            Thread.sleep(200);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    while (_draw_task != null && _draw_task.waitFlyCardToBoard());

                    synchronized (lock_drawTask)
                    {

                        if (_draw_task == null)
                        {
                            return;
                        }

                        _draw_task.resetHoveredCardSelected();

                        synchronized (_draw_task.getRoomInfo().lock_list_matrix_users_cards)
                        {
                            _draw_task.getRoomInfo().resetAllusersCountCards();
                            _draw_task.getRoomInfo().resetAllUsersDrawBeryText();
                        }

                        if (r.data != null && sv_page == SV_PAGE_ROOM)
                        {
                            int offset = 0;
                            byte[] _len = ByteBuffer.allocate(1).array();

                            System.arraycopy(r.data, offset, _len, 0, _len.length);
                            offset += _len.length;

                            final int out_of_room = _len[0];

                            System.arraycopy(r.data, offset, _len, 0, _len.length);
                            offset += _len.length;
                            int count_users = _len[0];

                            System.arraycopy(r.data, offset, _len, 0, _len.length);
                            offset += _len.length;
                            int defers_pos_in_room = _len[0];

                            _len = ByteBuffer.allocate(4).array();
                            System.arraycopy(r.data, offset, _len, 0, _len.length);
                            offset += _len.length;

                            ByteBuffer buffer = ByteBuffer.wrap(_len);
                            buffer.order(ByteOrder.BIG_ENDIAN);

                            int old_balans = (int) accout_info.balans;
                            accout_info.balans = buffer.getInt();

                            List<FlyMoneyInfo> fm = new ArrayList<>();

                            for (int k = 0; k < count_users; k++)
                            {
                                _len = ByteBuffer.allocate(4).array();
                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                buffer = ByteBuffer.wrap(_len);
                                buffer.order(ByteOrder.BIG_ENDIAN);

                                int uid = buffer.getInt();

                                System.arraycopy(r.data, offset, _len, 0, _len.length);
                                offset += _len.length;

                                buffer = ByteBuffer.wrap(_len);
                                buffer.order(ByteOrder.LITTLE_ENDIAN);

                                float add_money = buffer.getFloat();

                                if (!Utils.doubleEquals(add_money, 0f, 0.1f))
                                {
                                    fm.add(new FlyMoneyInfo(uid
                                            , "+"
                                            + (new DecimalFormat("#.#").format(add_money))
                                            //+ " "
                                            //+ Utils.getCorrectSuffix( (int) add_money, getResources().getString(R.string.txt_68), getResources().getString(R.string.txt_69), getResources().getString(R.string.txt_70))
                                    ));

                                    if (uid == accout_info.uid)
                                    {
                                        runAnimationNumber((int) accout_info.balans, old_balans);
                                    }

                                    if (app_setting.opt_sound_in_game && sound_coins != null && uid == accout_info.uid && ! call_phone)
                                    {
                                        sound_coins.start();
                                    }
                                }

                                if (app_setting.opt_sound_in_game && sound_coins != null && uid == accout_info.uid && ! call_phone)
                                {
                                    sound_coins.start();
                                }
                            }

                            if (_draw_task != null)
                            {
                                _draw_task.last_cmd_server = "STOP_GAME";

                                if (defers_pos_in_room > 0)
                                {
                                    RoomInfo_User ru_defer = _draw_task.getUserFromOffsetPosition(defers_pos_in_room);

                                    if (ru_defer != null)
                                    {
                                        if (ru_defer.offset_position_num == _draw_task.getRoomInfo().my_userinfo.offset_position_num)
                                        {
                                            _draw_task.flyJoker(
                                                    _draw_task.canvas_w2
                                                    , _draw_task.canvas_h2
                                                    , (int) ru_defer.pos_x
                                                    , (int) (ru_defer.pos_y - _draw_task.joker.getHeight() - 5 * _draw_task._scale_pixel)
                                            );

                                            if (app_setting.opt_sound_in_game && sound_game_over != null && ! call_phone)
                                            {
                                                sound_game_over.start();
                                            }

                                            _draw_task.addCointsAddAnimation(
                                                    "-" + _draw_task.getRoomInfo().rate
                                                    , _draw_task.canvas_w2
                                                    , _draw_task.canvas_h2 + _draw_task.canvas_h2 / 4
                                                    , "#d50000");


                                            runAnimationNumber((int) accout_info.balans, old_balans);

                                        }
                                        else
                                        {
                                            _draw_task.flyJoker(_draw_task.canvas_w2, _draw_task.canvas_h2, (int) ru_defer.pos_x, (int) (ru_defer.pos_y + 10 * _draw_task._scale_pixel));
                                        }

                                    }
                                }

                                _draw_task.flyMoneyUsers(fm);
                                _draw_task.stat_game = false;
                                _draw_task.resetAll();
                                //_draw_task.draw_ready_status_users = false;
                                _draw_task.resetHoveredCardSelected();

                                _draw_task.needRebuildBackground = true;

                                synchronized (_draw_task.getRoomInfo().lock_list_matrix_users_cards)
                                {
                                    _draw_task.getRoomInfo().resetAllusersCountCards();
                                    _draw_task.getRoomInfo().resetAllUsersReady();
                                }

                                //if( out_of_room == 0 )
                                {


                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            ui_page_room_hide_all_btns();
                                            ui_stop_timer_waiting_xod();

                                            if (       out_of_room == 0
                                                    && _draw_task != null
                                                    && _draw_task.getRoomInfo().allUsersInit())
                                            {
                                                TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                                                if (tv_0 != null)
                                                {
                                                    tv_0.setTextColor(Color.WHITE);
                                                    tv_0.setText(getString(R.string.txt_47));
                                                }

                                                pb_wait_readyPlay.setVisibility(View.VISIBLE);
                                                btn_ready_play_game.setVisibility(View.VISIBLE);
                                                pb_wait_readyPlay.setMax(1000);
                                                pb_wait_readyPlay.setProgress(1000);

                                                if (timer_ready == null || !timer_ready.running)
                                                {
                                                    timer_ready = new TimerReady(pb_wait_readyPlay, 10);
                                                    timer_ready.start();
                                                }
                                            }
                                            else if (_draw_task != null && !_draw_task.getRoomInfo().allUsersInit())
                                            {
                                                TextView tv_0 = (TextView) findViewById(R.id.tv_page_room_status_txt2);

                                                if (tv_0 != null)
                                                {
                                                    tv_0.setTextColor(Color.WHITE);
                                                    tv_0.setText(getString(R.string.txt_47));
                                                }
                                            }

                                        }
                                    });
                                }
                            }

                            if (_draw_task != null && (accout_info.balans == 0 || accout_info.balans < _draw_task.getRoomInfo().rate))
                            {
                                WaitThread wt = new WaitThread(250)
                                {
                                    @Override
                                    public void callback()
                                    {
                                        offDrawGame(false);

                                        runOnUiThread(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                show_page_menu();
                                            }
                                        });
                                    }
                                };
                                wt.start();
                            }

                        }

                    }
                }
            });

            th.start();

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    ImageView iv = findViewById(R.id.iv_page_room_list_icons_open);
                    if (iv != null)
                    {
                        iv.setVisibility(View.GONE);
                    }

                    if (alert_dialog_out_of_room != null && alert_dialog_out_of_room.isShowing())
                    {
                        alert_dialog_out_of_room.dismiss();
                    }

                    /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Конец игры");

                    builder.setPositiveButton("Да", new DialogInterface.OnClickListener()
                    {

                        public void onClick(DialogInterface dialog, int which)
                        {
                            signOut();
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();*/
                }
            });

            getMyBalans();
        }
        //**********

    }


    /*public void my_avatar_icon_redraw()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (my_avatar_icon != null)
                {
                    my_avatar_icon.invalidate();
                }
            }
        });
    }*/

    private void runAnimationNumber(int new_balans, int old_balans)
    {
        //Log.i("TAG455",  String.valueOf(new_balans) + " " + String.valueOf(old_balans) );

        if (old_balans == new_balans)
        {
            return;
        }

        int diff = Math.abs(new_balans - old_balans);
        int start_pos = 0;
        if (new_balans > old_balans)
        {
            start_pos = (diff > 200 ? new_balans - 200 : old_balans);
        }
        else
        {
            start_pos = (diff > 200 ? new_balans + 200 : old_balans);
        }

        AnimationNumber an = new AnimationNumber(
                _fontApp_bold3
                , start_pos
                , new_balans
                , (new_balans > old_balans) ? 1 : -1
                , _draw_task.canvas_w2
                , _draw_task.canvas_h2
                , 40
                , 2000
                , 2.0f
                , Color.parseColor((new_balans > old_balans) ? "#00d600" : "#d50000")
                , 100
                , (diff > 200 ? 0 : 10)
                , 10
                , 200
                , getString(R.string.txt_41) + " "
                , ""
        );

        _draw_task.addAnimationNimber(an);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }

        return false;
    }

    void load_my_friends()
    {
        ArrayList<String> params = new ArrayList<String>();

        params.add(String.valueOf(accout_info.uid));
        params.add(String.valueOf(session_info.session_socket_id));

        network.queue_network.add(getString(R.string.cmd_18)
                , params
                , null
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);
    }

    void load_table_rating()
    {
        ArrayList<String> params = new ArrayList<String>();

        params.add(String.valueOf(accout_info.uid));
        params.add(String.valueOf(session_info.session_socket_id));

        network.queue_network.add("LOAD_TABLE_RATING"
                , params
                , null
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);
    }

    void load_purchased_goods()
    {
        ArrayList<String> params = new ArrayList<String>();

        params.add(String.valueOf(accout_info.uid));
        params.add(String.valueOf(session_info.session_socket_id));

        network.queue_network.add("LOAD_PURCHASED_GOODS"
                , params
                , null
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);
    }

    ///----------------------------------------------------------------------------------------------------
    boolean _exec2(final DataInputStream in_soket, final ReadCommand r, Socket sock, final Object param)
    {
        Log.i("TAG54", "<< cmd2 : " + r.cmd + " " + String.valueOf(r.timestamp));

        //----------------------------------------------------------
        if (r.cmd.equalsIgnoreCase("OK_SEND"))  /// подтверждение
        {
            if (network != null)
            {
                // удаление из очереди отправленого запроса
                network.queue_network.okSend(r.timestamp);
            }

            return false; // читаем дальше что есть
        }
        //----------------------------------------------------------

        if (network != null)
        {
            try
            {
                //отправко подтверждаения в потоке текушем
                byte[] b_cmd = ProtokolUtils.get_buffer_command_encrypt(
                        "OK_SEND"
                        , null
                        , null
                        , session_info
                        , r.timestamp);

                DataOutputStream output = new DataOutputStream(sock.getOutputStream());
                output.write(b_cmd);
                output.flush();
            } 
            catch (IOException e)
            {
                e.printStackTrace();
            }

            // исключить дублирование приема
            if (network.queue_network.foundInput(r.timestamp))
            {
                return true;
            }
            else
            {
                network.queue_network.addInput(r.timestamp);
            }
        }

        if (r.cmd.equalsIgnoreCase(getString(R.string.cmd_1)))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_read = _len[0];
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                String ss = new String(_len);

                if (ss.equals("INIT_SESSION_OK"))
                {
                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int uid = buffer.getInt();

                    //Log.i("TAG", String.valueOf(uid));

                    accout_info.uid = uid;
                    app_setting.opt_uid = uid;

                    db.setKeyValue(getString(R.string.cmd_1), "1");

                    //app_setting.setAccountInfoUid(accout_info.uid);

                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int time_last_login = buffer.getInt();

                    //Log.i("TAG", String.valueOf(time_last_login));

                    accout_info.time_last_login = time_last_login;


                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int reiting = buffer.getInt();

                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int balans = buffer.getInt();

                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int count_games = buffer.getInt();

                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int count_wins = buffer.getInt();

                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int count_draw = buffer.getInt();

                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int count_defeats = buffer.getInt();

                    accout_info.balans = balans;
                    accout_info.raiting = reiting;
                    accout_info.count_games = count_games;
                    accout_info.count_wins = count_wins;
                    accout_info.count_draw = count_draw;
                    accout_info.count_defeats = count_defeats;
                    //******************************************************************************
                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    WAIT_SECOND = _len[0];

                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int count_online = buffer.getInt();

                    //------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    count_read = _len[0];
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String _token = new String(_len);

                    if( _token.trim().length() > 0 )
                    {
                        token = _token;

                        db.setKeyValue("accout_info.token", token);
                    }
                    //------------------------------

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int count_options = (int) _len[0] & 0xff;

                    for (int i = 0; i < count_options; i++)
                    {
                        _len = ByteBuffer.allocate(2).array();

                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        buffer = ByteBuffer.wrap(_len);
                        buffer.order(ByteOrder.BIG_ENDIAN);

                        int type = buffer.getShort();

                        //----------------------------------------------------
                        _len = ByteBuffer.allocate(1).array();

                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        count_read = (int) _len[0] & 0xff;
                        _len = ByteBuffer.allocate(count_read).array();
                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        String value = new String(_len);
                        //----------------------------------------------------

                        if (type == AppSettings.TYPE_OPT__SORT_MY_CARDS)
                        {
                            app_setting.opt_sort_my_cards = Integer.valueOf(value);
                        }
                        else if (type == AppSettings.TYPE_OPT__SORT_TRUMP_CARDS)
                        {
                            app_setting.opt_sort_trump_cards = Integer.valueOf(value);
                        }
                        else if (type == AppSettings.TYPE_OPT__SOUND_IN_GAME)
                        {
                            app_setting.opt_sound_in_game = Integer.valueOf(value) == 1;
                        }
                        else if (type == AppSettings.TYPE_OPT__VIBRATION)
                        {
                            app_setting.opt_vibration = Integer.valueOf(value) == 1;
                        }
                        else if (type == AppSettings.TYPE_OPT__HINTS_CARDS)
                        {
                            app_setting.opt_hints_cards = Integer.valueOf(value) == 1;
                        }
                        else if (type == AppSettings.TYPE_OPT__TYPE_BACKCARDS)
                        {
                            app_setting.opt_type_backcard = Integer.valueOf(value);
                        }
                        else if (type == AppSettings.TYPE_OPT__TYPE_CARDSTYLE)
                        {
                            app_setting.opt_type_cardsstyle = Integer.valueOf(value);
                        }
                        else if (type == AppSettings.TYPE_OPT__SOUND_READY_STROKE)
                        {
                            app_setting.opt_sound_ready_stroke = Integer.valueOf(value) == 1;
                        }
                    }

                    //******************************************************************************

                    last_count_online = count_online;

                    //app_setting.db.setKeyValue("init_ok", "1");

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            show_page_menu();
                            //setUiCountOnline(last_count_online);
                        }
                    });

                    load_my_friends();
                    load_table_rating();
                    load_purchased_goods();

                    if (!isMyServiceRunning(MyService.class))
                    {
                        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                        {
                            MainActivity.this.startForegroundService(new Intent( MainActivity.this, MyService.class));
                        }
                        else*/
                        //{
                            startService(new Intent( MainActivity.this, MyService.class));
                        //}
                    }
                }
                else
                {
                    //app_setting.db.setKeyValue("init_ok", "0");
                    //connection_server();
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            db.setKeyValue(getString(R.string.cmd_1), "0");

                            if( network != null )
                            {
                                network.OnDestroy();
                                network = null;
                            }

                            signInReset();
                        }
                    });
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("LOAD_PURCHASED_GOODS"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(2).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int count_read = buffer.getShort();

                app_setting.MyListPurchasedReset();

                for (int i = 0; i < count_read; i++)
                {
                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    app_setting.mylist_purchased_goods.add(buffer.getInt());
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("BUY_GOODS"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_read = _len[0];
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                String ss = new String(_len);

                _len = ByteBuffer.allocate(4).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                int gid = buffer.getInt();

                if (ss.equalsIgnoreCase("OK"))
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            FrameLayout fl_wait = findViewById(R.id.fl_page_shop_wait);
                            if (fl_wait != null)
                            {
                                fl_wait.setVisibility(View.GONE);
                            }

                            if (sv_page == SV_PAGE_SHOP)
                            {
                                ((ViewManager) current_buy_goods_view.getParent()).removeView(current_buy_goods_view);

                                FrameLayout fl = findViewById(R.id.fl_page_shop_wait);
                                fl.setVisibility(View.GONE);

                                FrameLayout fl1 = findViewById(R.id.fl_page_shop_page_1);
                                FrameLayout fl2 = findViewById(R.id.fl_page_shop_page_2);

                                Shop shop = new Shop(MainActivity.this);

                                fl1.removeAllViews();
                                fl1.addView(shop.drawUiPage1(app_setting.mylist_purchased_goods));

                                fl2.removeAllViews();
                                fl2.addView(shop.drawUiPage2(app_setting.mylist_purchased_goods));
                            }

                            ui_page_shop_set_active_menu(2);
                        }
                    });

                    app_setting.mylist_purchased_goods.add(gid);
                    getMyBalans();
                }
                else
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            FrameLayout fl_wait = findViewById(R.id.fl_page_shop_wait);
                            if (fl_wait != null)
                            {
                                fl_wait.setVisibility(View.GONE);
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle(getString(R.string.txt_114))
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.create().show();
                        }
                    });
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("SEARCH_FRIENDS"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_users = _len[0];

                map_ui_friends.clear();
                search_friends_list.clear();

                for (int i = 0; i < count_users; i++)
                {
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int uid = buffer.getInt();

                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int count_read = (int) _len[0] & 0xff;
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String picture = new String(_len);
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    count_read = (int) _len[0] & 0xff;
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String first_name = new String(_len);
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    count_read = (int) _len[0] & 0xff;
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String last_name = new String(_len);
                    //----------------------------------------------------
                    /*_len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int raiting = buffer.getInt();
                    //----------------------------------------------------*/

                    SearchItem item = new SearchItem();
                    item.uid = uid;
                    item.first_name = first_name;
                    item.last_name = last_name;
                    item.picture = picture;

                    search_friends_list.add(item);
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_draw_list_my_search_friends(search_friends_list);
                    }
                });
            }
        }
        else if (r.cmd.equalsIgnoreCase("LOAD_TABLE_RATING"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_users = _len[0];

                list_rating.clear();

                for (int i = 0; i < count_users; i++)
                {
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int uid = buffer.getInt();

                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int count_read = (int) _len[0] & 0xff;
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String picture = new String(_len);
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    count_read = (int) _len[0] & 0xff;
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String first_name = new String(_len);
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    count_read = (int) _len[0] & 0xff;
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String last_name = new String(_len);

                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int count_games = buffer.getInt();

                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int count_wins = buffer.getInt();

                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int count_defeats = buffer.getInt();

                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    int count_draw = buffer.getInt();
                    //----------------------------------------------------

                    UiItemRating item = new UiItemRating();
                    item.id = uid;
                    item.first_name = first_name;
                    item.last_name = last_name;
                    item.puctire = picture;
                    item.count_wins = count_wins;
                    item.count_defeats = count_defeats;
                    item.count_draw = count_draw;
                    item.count_games = count_games;

                    list_rating.add(item);
                }

                network.queue_network.add("CHECK_VERSION"
                        , null
                        , null
                        , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                        , QueueNetwork.TYPE_SEND__FORCE);

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ui_draw_list_rating(list_rating);
                    }
                });
            }
        }
        else if (r.cmd.equalsIgnoreCase("GET_MY_BALANS"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(4).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                accout_info.balans = buffer.getInt();

                //--------------------------------------------
                System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                accout_info.count_games = buffer.getInt();
                //--------------------------------------------
                System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                accout_info.count_draw = buffer.getInt();
                //--------------------------------------------
                System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                accout_info.count_wins = buffer.getInt();
                //--------------------------------------------
                System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                accout_info.raiting = buffer.getInt();
                //--------------------------------------------
                //--------------------------------------------

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TextView tv = findViewById(R.id.tv_page_friends_alert_dialog_mybalans);
                        if (tv != null)
                        {
                            tv.setText(getString(R.string.txt_41) + ": " + String.valueOf(accout_info.balans) + " " + Utils.getCorrectSuffix(accout_info.balans, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));
                        }

                        tv = findViewById(R.id.tv_page_menu_statistic_balans);
                        if (tv != null)
                        {
                            tv.setText(String.valueOf(accout_info.balans) + " " + Utils.getCorrectSuffix(accout_info.balans, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));
                        }

                        tv = findViewById(R.id.tv_page_add_balans_money);
                        if (tv != null)
                        {
                            tv.setText(String.valueOf(accout_info.balans) + " " + Utils.getCorrectSuffix(accout_info.balans, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));
                        }

                        tv = findViewById(R.id.tv_page_shop_my_balans);
                        if (tv != null)
                        {
                            tv.setText(String.valueOf(accout_info.balans) + " " + Utils.getCorrectSuffix(accout_info.balans, getString(R.string.txt_68), getString(R.string.txt_69), getString(R.string.txt_70)));
                        }

                    }
                });
            }
        }
        else if (r.cmd.equalsIgnoreCase(getString(R.string.cmd_18)))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_users = _len[0];

                //final List<FriendItem> friend_list = new ArrayList<>();

                map_ui_friends.clear();
                friend_list.clear();

                for (int i = 0; i < count_users; i++)
                {
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int uid = buffer.getInt();

                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int count_read = (int) _len[0] & 0xff;
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String picture = new String(_len);
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    count_read = (int) _len[0] & 0xff;
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String first_name = new String(_len);
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    count_read = (int) _len[0] & 0xff;
                    _len = ByteBuffer.allocate(count_read).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    String last_name = new String(_len);
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int raiting = buffer.getInt();
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(1).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    int status_fiends = (int) _len[0] & 0xff;
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int count_games = buffer.getInt();
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int count_wins = buffer.getInt();
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int count_defeats = buffer.getInt();
                    //----------------------------------------------------
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int count_draw = buffer.getInt();
                    //----------------------------------------------------

                    FriendItem item = new FriendItem();
                    item.uid = uid;
                    item.first_name = first_name;
                    item.last_name = last_name;
                    item.raiting = raiting;
                    item.status_fiends = status_fiends;
                    item.picture = picture;

                    item.count_games = count_games;
                    item.count_wins = count_wins;
                    item.count_defeats = count_defeats;
                    item.count_draw = count_draw;

                    friend_list.add(item);
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (int i = 0; i < friend_list.size(); i++)
                        {
                            if (friend_list.get(i).status_fiends == 2)
                            {
                                ImageView iv = findViewById(R.id.iv_page_menu_alarm_new_friends);

                                if (iv != null)
                                {
                                    iv.setVisibility(View.VISIBLE);
                                }

                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(200);

                                break;
                            }
                        }

                        ui_draw_list_my_friends(friend_list);
                    }
                });
            }
        }
        else if (r.cmd.equalsIgnoreCase("ADD_TO_FRIENDS_2"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_read = _len[0];
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                String ss = new String(_len);

                if (ss.equals("OK"))
                {
                    _len = ByteBuffer.allocate(4).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int uid = buffer.getInt();

                    RoomInfo_User uinfo = _draw_task.getInfoUser(uid);
                    uinfo.exists_in_friends = true;
                }

                load_my_friends();
            }
        }
        else if (r.cmd.equalsIgnoreCase(getString(R.string.cmd_10)))
        {
            load_my_friends();

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    LinearLayout wait_icon = (LinearLayout) findViewById(R.id.ll_page_room_wait_add_friend);

                    if (wait_icon != null)
                    {
                        wait_icon.setVisibility(View.INVISIBLE);
                    }

                    Toast.makeText(MainActivity.this, getString(R.string.txt_86), Toast.LENGTH_LONG).show();

                    /*if (r.data != null)
                    {
                        int offset = 0;
                        byte[] _len = ByteBuffer.allocate(4).array();

                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        ByteBuffer buffer = ByteBuffer.wrap(_len);
                        buffer.order(ByteOrder.BIG_ENDIAN);

                        final int rate = buffer.getInt();
                    }*/
                }
            });

        }
        else if (r.cmd.equalsIgnoreCase(getString(R.string.cmd_3)))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_read = _len[0];
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                String ss = new String(_len);


                _len = ByteBuffer.allocate(8).array();
                System.arraycopy(r.data, offset, _len, 0, 8);
                offset += _len.length;

                final long room_id = ProtokolUtils.htonl2(ByteBuffer.wrap(_len).getLong());

                _len = ByteBuffer.allocate(4).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                ByteBuffer buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int rate = buffer.getInt();

                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;
                buffer = ByteBuffer.wrap(_len);

                final int count_users = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;
                buffer = ByteBuffer.wrap(_len);

                final int count_cards = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;
                buffer = ByteBuffer.wrap(_len);

                final int type_game = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;
                buffer = ByteBuffer.wrap(_len);

                final int toss = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;
                buffer = ByteBuffer.wrap(_len);

                final int have_password = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;
                buffer = ByteBuffer.wrap(_len);

                final int current_my_position = _len[0];

                _len = ByteBuffer.allocate(4).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                buffer = ByteBuffer.wrap(_len);
                buffer.order(ByteOrder.BIG_ENDIAN);

                final int trump = buffer.getInt();

                //-------------------------------------------

                final List<Short> cards_users = new ArrayList<>();

                for (int i = 0; i < 6; i++)
                {
                    _len = ByteBuffer.allocate(2).array();

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    cards_users.add(buffer.getShort());
                }

                _len = ByteBuffer.allocate(1).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                final byte count_cards_in_desck = _len[0];

                //---------
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                final int current_pos_user_main_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                final int current_pos_user_xod = _len[0];

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                final int current_pos_user_beat = _len[0];

                //---------

                if (ss.equals("CREATE_ROOM_OK"))
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            show_page_room(
                                    room_id
                                    , rate
                                    , count_users
                                    , count_cards
                                    , type_game
                                    , toss
                                    , have_password
                                    , current_my_position
                                    , trump
                                    , count_cards_in_desck
                                    , cards_users
                                    , current_pos_user_xod
                                    , current_pos_user_beat
                                    , current_pos_user_main_xod
                            );
                        }
                    });
                }
            }
        }
        else if (r.cmd.equalsIgnoreCase("SEARCH_ROOMS"))
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    ImageView iv = (ImageView) findViewById(R.id.iv_page_search_tick);

                    if (iv != null)
                    {
                        iv.setVisibility(View.GONE);
                    }

                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    SearchRooms fragment = (SearchRooms) fm.findFragmentById(R.id.page_search_main);

                    if (fragment != null)
                    {
                        if (fragment.UiUpdateAll(r) > 0)
                        {
                            UiPageSearchShowHideWait(false);
                        }
                        else
                        {
                            UiPageSearchShowHideWait(true);
                        }
                    }
                }
            });
        }
        else if (r.cmd.equalsIgnoreCase("CONNECTION_ROOM"))
        {
            if (r.data != null)
            {
                int offset = 0;
                byte[] _len = ByteBuffer.allocate(1).array();

                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                int count_read = _len[0];
                _len = ByteBuffer.allocate(count_read).array();
                System.arraycopy(r.data, offset, _len, 0, _len.length);
                offset += _len.length;

                String ss = new String(_len);

                if (ss.equals("OK"))
                {
                    /*_len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length); offset += _len.length;

                    final int position_in_room = _len[0];*/

                    _len = ByteBuffer.allocate(8).array();
                    System.arraycopy(r.data, offset, _len, 0, 8);
                    offset += _len.length;

                    final long room_id = ProtokolUtils.htonl2(ByteBuffer.wrap(_len).getLong());


                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final int have_password = _len[0];

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final int type_game = _len[0];

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final int toss = _len[0];

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final int count_users_in_room = _len[0];

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final int real_count_users = _len[0];

                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    ByteBuffer buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int rate = buffer.getInt();

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final byte count_cards = _len[0];

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final int current_my_position = (int) _len[0] & 0xff;

                    _len = ByteBuffer.allocate(4).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    buffer = ByteBuffer.wrap(_len);
                    buffer.order(ByteOrder.BIG_ENDIAN);

                    final int trump = buffer.getInt();

                    //----------------------------------------------------

                    final List<Short> cards_users = new ArrayList<>();

                    for (int i = 0; i < 6; i++)
                    {
                        _len = ByteBuffer.allocate(2).array();

                        System.arraycopy(r.data, offset, _len, 0, _len.length);
                        offset += _len.length;

                        buffer = ByteBuffer.wrap(_len);
                        buffer.order(ByteOrder.BIG_ENDIAN);

                        cards_users.add(buffer.getShort());
                    }

                    _len = ByteBuffer.allocate(1).array();
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final byte count_cards_in_desck = _len[0];

                    //---------
                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final int current_pos_user_main_xod = _len[0];

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final int current_pos_user_xod = _len[0];

                    System.arraycopy(r.data, offset, _len, 0, _len.length);
                    offset += _len.length;

                    final int current_pos_user_beat = _len[0];

                    //---------

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            show_page_room(
                                    room_id
                                    , rate
                                    , count_users_in_room
                                    , count_cards
                                    , type_game
                                    , toss
                                    , have_password
                                    , current_my_position
                                    , trump
                                    , count_cards_in_desck
                                    , cards_users
                                    , current_pos_user_xod
                                    , current_pos_user_beat
                                    , current_pos_user_main_xod
                            );
                        }
                    });


                }
                else if (ss.equals("ERROR_PASS"))
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            UiPageSearchShowHideWait(false);
                            Toast.makeText(MainActivity.this, getString(R.string.txt_87), Toast.LENGTH_LONG).show();

                            FrameLayout fl_wait = (FrameLayout) findViewById(R.id.fl_page_search_wait);
                            if (fl_wait != null)
                            {
                                fl_wait.setVisibility(View.GONE);
                            }
                        }
                    });

                }
                else if (ss.equals("NOT_PLACE"))
                {

                }
            }
        }

        return true;
    }

    class UiItemFriends
    {
        LinearLayout ll;
        ImageView iv;
    }

    class UiItemRating
    {
        public int id;
        public String first_name, last_name, puctire;
        public Bitmap img = null;
        public ImageView iv;
        public int count_games, count_wins, count_draw, count_defeats;
    }

    ///*********************************************************************************************
    /*public class AnimatedBkCards extends View
    {
        private float STEP_SIZE;

        Bitmap card_bk_SPADE    = null;
        Bitmap card_bk_CLUB     = null;
        Bitmap card_bk_HEART    = null;
        Bitmap card_bk_DIAMOND  = null;

        Paint paint_drawBitmap  = null;
        int bk_color;

        boolean init = false;
        int canvas_w, canvas_h;

        int offset_1 = 0;

        private Bitmap getIconByIndex(int index)
        {
            switch (index)
            {
                case 0:  return card_bk_CLUB;
                case 1:  return card_bk_SPADE;
                case 2:  return card_bk_DIAMOND;
                default: return card_bk_HEART;
            }
        }

        public AnimatedBkCards(Context context)
        {
            super(context);

            float scale_pr = 90f;

            STEP_SIZE = 50 * ((MainActivity)context).get_scale_px();

            bk_color = Color.parseColor("#00a2d3");

            paint_drawBitmap = new Paint();
            paint_drawBitmap.setAntiAlias(true);
            paint_drawBitmap.setFilterBitmap(true);
            paint_drawBitmap.setDither(true);

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bk_spade);
            card_bk_SPADE = bmp.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bk_club);
            card_bk_CLUB = bmp.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bk_heart);
            card_bk_HEART = bmp.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bk_diamond);
            card_bk_DIAMOND = bmp.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            if( ! init )
            {
                canvas_w = getWidth();
                canvas_h = getHeight();
                init = true;
            }

            canvas.drawColor(bk_color);

            for(int i = offset_1, k = 0; i < canvas_w; i += STEP_SIZE)
            {
                canvas.drawBitmap( getIconByIndex(k), i, 20, paint_drawBitmap);
                k += 1;

                if( k > 3) { k = 0; }
            }

            for(int i = offset_1, k = 3; i > 0; i -= STEP_SIZE)
            {
                canvas.drawBitmap( getIconByIndex(k), i, 20, paint_drawBitmap);
                k -= 1;

                if( k < 0) { k = 3; }
            }

            offset_1 += 1;
        }
    }*/
    ///*********************************************************************************************
    class TimerReady extends Thread
    {
        public boolean running = true;

        ProgressBar pb;
        long wait_time_sec = 0;
        long startTimeWork = 0;
        long diff = 0;

        public TimerReady(ProgressBar pb, long second_wait)
        {
            this.pb = pb;
            this.wait_time_sec = second_wait * 1000;
        }

        public void run()
        {
            startTimeWork = System.currentTimeMillis();
            long old = 0;

            while (running && pb != null)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                if( _draw_task == null )
                {
                    return;
                }

                synchronized (_draw_task.lockSurfaceChanged)
                {
                    diff = System.currentTimeMillis() - startTimeWork;

                    if (diff > wait_time_sec)
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (pb != null)
                                {
                                    network.queue_network.add("OUT_OF_ROOM"
                                            , null
                                            , null
                                            , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                                            , QueueNetwork.TYPE_SEND__FORCE);

                                    if(_draw_task != null)
                                    {
                                        _draw_task._stop_draw = true;
                                    }

                                    pb.setProgress(0);
                                    show_page_menu();
                                    running = false;
                                }
                            }
                        });

                        break;
                    }

                    if (old != diff)
                    {
                        old = diff;

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (pb != null)
                                {
                                    pb.setProgress((int) ((wait_time_sec - diff) * 1000 / wait_time_sec));
                                }
                            }
                        });
                    }
                }

            }
        }
    }

    class TimerXod extends Thread
    {
        volatile boolean running = true;
        long time_end = 0;
        int total_count_second = 0;

        public TimerXod(int second_wait)
        {
            total_count_second = second_wait;

            time_end = System.currentTimeMillis() + second_wait * 1000;
        }

        public void run()
        {
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            synchronized (_draw_task.lockSurfaceChanged)
            {
                callback_BeforeStart(total_count_second);
            }

            String sm, ss;
            long old = 0;

            while (running)
            {
                if(_draw_task == null)
                {
                    return;
                }

                long diff = time_end - System.currentTimeMillis();
                if (diff <= 0)
                {
                    break;
                }

                diff /= 1000; // в секундах

                long m = diff / (60);
                long s = (diff - m * 60);

                sm = ((m < 10) ? "0" + String.valueOf(m) : String.valueOf(m)) + ":";
                ss = String.valueOf(s);

                if (m == 0)
                {
                    sm = "";
                }

                //Log.i("TAG", String.valueOf(diff));

                if (old != diff)
                {
                    if (diff < 8 && app_setting.opt_sound_in_game)
                    {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(40);
                    }

                    old = diff;

                    synchronized (_draw_task.lockSurfaceChanged)
                    {
                        callback_changes(sm + ss, diff);
                    }
                }

                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }

        public void callback_BeforeStart(int total_count)
        {
        }

        public void callback_changes(String v, long diff)
        {
        }

        public void callback_end()
        {
        }
    }

/*
    @Override
    public void onUnityAdsReady(String s)
    {
        //Toast.makeText(MainActivity.this, "onUnityAdsReady: " + s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnityAdsStart(String s)
    {
        //Toast.makeText(MainActivity.this, "onUnityAdsStart: " + s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnityAdsFinish(String s, UnityAds.FinishState finishState)
    {
        //Toast.makeText(MainActivity.this, "onUnityAdsFinish: " + s,Toast.LENGTH_SHORT).show();

        ArrayList<String> params = new ArrayList<String>();

        params.add(String.valueOf(accout_info.uid));
        params.add(String.valueOf(session_info.session_socket_id));
        params.add(String.valueOf(10 + 100 + 90));

        network.queue_network.add(JniApi.k1()
                , params
                , null
                , ClassNetWork.GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM
                , QueueNetwork.TYPE_SEND__LONG);

        WaitThread wt = new WaitThread(800)
        {
            @Override
            public void callback()
            {
                getMyBalans();
            }
        };
        wt.start();

        if( ! showed_salute )
        {
            showed_salute = true;
            runAnimationAfterVideoAd();
        }
        else
        {
            if (app_setting.opt_sound_in_game && cachbox != null) {
                cachbox.start();
            }
        }
    }

    @Override
    public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s)
    {
        //Toast.makeText(MainActivity.this, "onUnityAdsError: " + s,Toast.LENGTH_SHORT).show();
    }*/
}


