package nl.rukish.mageknights;

import java.util.List;

import java.util.List;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HighscoreListAdapter extends ArrayAdapter<Highscore> {
	private int resource;
	private LayoutInflater inflater;
	private Context context;

	public HighscoreListAdapter(Context ctx, int resourceId, List<Highscore> objects) {
		super(ctx, resourceId, objects);
		resource = resourceId;
		inflater = LayoutInflater.from(ctx);
		context = ctx;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = (LinearLayout) inflater.inflate(resource, null);
		Highscore Legend = getItem(position);
		
		TextView hsIndex = (TextView) convertView.findViewById(R.id.tv_index);
		hsIndex.setText(Legend.getIndex()+".");

		TextView hsName = (TextView) convertView.findViewById(R.id.tv_name);
		hsName.setText(Legend.getName());

		TextView hsScore = (TextView) convertView.findViewById(R.id.tv_score);
		hsScore.setText(Integer.toString(Legend.getScore()));

		return convertView;
	}

}
