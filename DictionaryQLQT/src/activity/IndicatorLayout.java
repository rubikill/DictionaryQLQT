package activity;

import com.hcmus.dictionaryqlqt.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author Minh Khanh
 * custom view cho Indicator tab
 *
 */
public class IndicatorLayout extends LinearLayout{

	public IndicatorLayout(Context context, String name, int icon) {
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.tab_indicator_layout, this);
	    ((TextView)view.findViewById(R.id.tab_title)).setText(name);
	    view.findViewById(R.id.tab_icon).setBackgroundResource(icon);
	}
}
