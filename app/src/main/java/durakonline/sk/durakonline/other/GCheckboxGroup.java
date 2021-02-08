package durakonline.sk.durakonline.other;


import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class GCheckboxGroup
{

    List<CheckBox> radios = new ArrayList<CheckBox>();
    /**
     * This occurs everytime when one of RadioButtons is clicked,
     * and deselects all others in the group.
     */
    OnClickListener onClick = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {

            // let's deselect all radios in group
            for (CheckBox rb : radios)
            {

                ViewParent p = rb.getParent();
                /*if (p.getClass().equals(RadioGroup.class))
                {
                    // if RadioButton belongs to RadioGroup,
                    // then deselect all radios in it
                    RadioGroup rg = (RadioGroup) p;
                    rg.clearCheck();
                }
                else*/
                {
                    // if RadioButton DOES NOT belong to RadioGroup,
                    // just deselect it
                    rb.setChecked(false);
                }
            }

            // now let's select currently clicked RadioButton
            if (v.getClass().equals(CheckBox.class))
            {
                CheckBox ch = (CheckBox) v;
                ch.setChecked(true);

                callback_checkboxClicked(ch);
            }

        }
    };

    public GCheckboxGroup(List<CheckBox> radios)
    {
        super();

        for (int i = 0; i < radios.size(); i++)
        {
            radios.get(i).setOnClickListener(onClick);
            this.radios.add(radios.get(i));
        }
    }

    public void callback_checkboxClicked(CheckBox ch) {}

}
