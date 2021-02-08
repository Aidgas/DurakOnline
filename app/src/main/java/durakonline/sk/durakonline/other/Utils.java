package durakonline.sk.durakonline.other;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by sk on 20.01.18.
 */

public class Utils
{
    /**
     * @example getCorrectSuffix(10, ' день ', ' дня ', ' дней ')
     */
    /*public static String getCorrectSuffix(int num, String str1, String str2, String str3)
    {
        int val = num % 100;

        if (val > 10 && val < 20)
        {
            return str3;
        }
        else
        {
            val = num % 10;
            if (val == 1)
            {
                return str1;
            }
            else if (val > 1 && val < 5)
            {
                return str2;
            }
            else
            {
                return str3;
            }
        }
    }*/

    public static String getCorrectSuffix(long num, String str1, String str2, String str3)
    {
        long val = num % 100;

        if (val > 10 && val < 20)
        {
            return str3;
        }
        else
        {
            val = num % 10;
            if (val == 1)
            {
                return str1;
            }
            else if (val > 1 && val < 5)
            {
                return str2;
            }
            else
            {
                return str3;
            }
        }
    }

    public static String formatDateRUS(long date, String format)
    {
        //Locale locale = new Locale("fr");
        Locale locale = new Locale("ru");
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);
        String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        String[] shortMonths = {
                "янв", "фев", "мар", "апр", "май", "июн",
                "июл", "авг", "сен", "окт", "ноя", "дек"};
        dfs.setMonths(months);
        dfs.setShortMonths(shortMonths);
        String[] weekdays = {"", "Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
        String[] shortWeekdays = {"", "вс", "пн", "вт", "ср", "чт", "пт", "сб"};
        dfs.setWeekdays(weekdays);
        dfs.setShortWeekdays(shortWeekdays);

        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        sdf.setDateFormatSymbols(dfs);
        return sdf.format(date); // пт, 09 декабря 2016
    }

    public static String formatDateEN(long date, String format)
    {
        Locale locale = new Locale("en");
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);
        String[] months = {"january", "february", "march", "april", "may", "june",
                "july", "august", "september", "october", "november", "december"};
        String[] shortMonths = {
                "jan", "feb", "mar", "apr", "may", "jun",
                "jul", "aug", "sep", "oct", "nov", "dec"};
        dfs.setMonths(months);
        dfs.setShortMonths(shortMonths);
        String[] weekdays = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] shortWeekdays = {"", "su", "mo", "tu", "we", "th", "fr", "sa"};
        dfs.setWeekdays(weekdays);
        dfs.setShortWeekdays(shortWeekdays);

        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        sdf.setDateFormatSymbols(dfs);
        return sdf.format(date);
    }

    public static String getApplicationName(Context context)
    {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    public static Bitmap getBitmapFromURL(String src)
    {
        try
        {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(2000);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e)
        {
            e.printStackTrace();
            // Log exception
            return null;
        }
    }

    public static float dipToPixels(Context context, float dipValue)
    {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap)
    {
        if (bitmap == null)
        {
            return null;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static int getNextPos(int current, int count, int min)
    {
        if (current + 1 < count)
        {
            return current + 1;
        }
        else
        {
            return min;
        }
    }

    public static float pixelsToSp(Context context, Float px)
    {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public static void hideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null)
        {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean doubleEquals(double left, double right, double epsilon)
    {
        return (Math.abs(left - right) < epsilon);
    }

    public static boolean doubleLess(double left, double right, double epsilon, boolean orequal)
    {
        if (Math.abs(left - right) < epsilon)
        {
            return (orequal);
        }
        return (left < right);
    }

    public static boolean doubleGreater(double left, double right, double epsilon, boolean orequal)
    {
        if (Math.abs(left - right) < epsilon)
        {
            return (orequal);
        }
        return (left > right);
    }

    public static boolean doubleEquals(float left, float right, float epsilon)
    {
        return (Math.abs(left - right) < epsilon);
    }

    public static boolean doubleLess(float left, float right, float epsilon, boolean orequal)
    {
        if (Math.abs(left - right) < epsilon)
        {
            return (orequal);
        }
        return (left < right);
    }

    public static boolean doubleGreater(float left, float right, float epsilon, boolean orequal)
    {
        if (Math.abs(left - right) < epsilon)
        {
            return (orequal);
        }
        return (left > right);
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static void hideSoftKeyboard(Context context, View view)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void showSoftKeyboard(Context context, View view)
    {
        if (view.requestFocus())
        {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static boolean checkPermission_1(Context c)
    {
        if (Build.VERSION.SDK_INT < 23)
        {
            return true;
        }

        if (ContextCompat.checkSelfPermission(c, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean checkPermission_2(Context c)
    {
        if (Build.VERSION.SDK_INT < 23)
        {
            return true;
        }

        if (
                (ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        && (ContextCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static String transliterate(String message)
    {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < message.length(); i++)
        {
            for (int x = 0; x < abcCyr.length; x++)
            {
                if (message.charAt(i) == abcCyr[x])
                {
                    builder.append(abcLat[x]);
                }
            }
        }

        return builder.toString();
    }

    public static double Q_rsqrt(double number)
    {
        double x = number;
        double xhalf = 0.5d * x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6ec85e7de30daL - (i >> 1);
        x = Double.longBitsToDouble(i);
        /*for (int it = 0; it < 4; it++)
        {
            x = x * (1.5d - xhalf * x * x);
        }*/

        x = x * (1.5d - xhalf * x * x);
        x = x * (1.5d - xhalf * x * x);
        x = x * (1.5d - xhalf * x * x);
        x = x * (1.5d - xhalf * x * x);

        x *= number;
        return x;
    }

    public static void adjustFontScale(Context context, Configuration configuration)
    {
        if (configuration.fontScale != 1)
        {
            configuration.fontScale = 1;
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            context.getResources().updateConfiguration(configuration, metrics);
        }
    }



}
