package durakonline.sk.durakonline.other;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomSeekBar {

    int maxCount, textColor, min_value;
    Context mContext;
    LinearLayout mSeekLin;
    SeekBar mSeekBar;
    public List<TextView> list_tv = new ArrayList<>();

    public CustomSeekBar(Context context, int maxCount, int textColor, int min_value) {
        this.mContext = context;
        this.maxCount = maxCount;
        this.textColor = textColor;
        this.min_value = min_value;
    }

    public SeekBar getSeekBar() { return mSeekBar; }

    public void addSeekBar(LinearLayout parent)
    {
        if (parent instanceof LinearLayout)
        {
            parent.setOrientation(LinearLayout.VERTICAL);
            mSeekBar = new SeekBar(mContext);
            mSeekBar.setMax(maxCount - 1);

            // Add LinearLayout for labels below SeekBar
            mSeekLin = new LinearLayout(mContext);
            mSeekLin.setOrientation(LinearLayout.HORIZONTAL);
            mSeekLin.setPadding(10, 5, 10, 5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(35, 10, 35, 0);
            mSeekLin.setLayoutParams(params);

            addLabelsBelowSeekBar();
            parent.addView(mSeekBar);
            parent.addView(mSeekLin);

        }
        else
        {
            Log.e("CustomSeekBar", " Parent is not a LinearLayout");
        }

    }

    private void addLabelsBelowSeekBar()
    {
        for (int index = 0; index < maxCount; index++ )
        {
            TextView textView = new TextView(mContext);
            textView.setText(String.valueOf(index + min_value));
            textView.setTextColor(textColor);
            textView.setTextSize(18);
            textView.setGravity(Gravity.LEFT);

            list_tv.add( textView );

            mSeekLin.addView(textView);

            textView.setLayoutParams((index == maxCount - 1) ? getLayoutParams(0.0f) : getLayoutParams(1.0f));
            textView.setTag( index );
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    mSeekBar.setProgress( (Integer)view.getTag() );
                }
            });
        }
    }

    LinearLayout.LayoutParams getLayoutParams(float weight) {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight);
    }

}
