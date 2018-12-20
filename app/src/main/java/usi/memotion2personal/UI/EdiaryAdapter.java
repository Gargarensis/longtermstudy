package usi.memotion2personal.UI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import usi.memotion2personal.R;
import usi.memotion2personal.UI.fragments.EdiaryFragment;

public class EdiaryAdapter extends ArrayAdapter<EdiaryFragment.EdiaryActivity> {

    public EdiaryAdapter(Context context, ArrayList<EdiaryFragment.EdiaryActivity> entries) {
        super(context, 0, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EdiaryFragment.EdiaryActivity entry = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entry_layout, parent, false);
        }
        TextView entry_time = (TextView) convertView.findViewById(R.id.entry_time);
        ImageView entry_emotion = (ImageView) convertView.findViewById(R.id.entry_emotion);
        ImageView entry_activity = (ImageView) convertView.findViewById(R.id.entry_activity);
        entry_time.setText(entry.getStart_time() + " - " + entry.getEnd_time());
        switch (entry.getEmotion()) {
            case "very_sad":
                entry_emotion.setImageResource(R.drawable.activity_very_sad);
                break;
            case "happy":
                entry_emotion.setImageResource(R.drawable.activity_happy);
                break;
            case "neutral":
                entry_emotion.setImageResource(R.drawable.activity_neutral);
                break;
            case "sad":
                entry_emotion.setImageResource(R.drawable.activity_sad);
                break;
            case "very_happy":
                entry_emotion.setImageResource(R.drawable.activity_very_happy);
                break;
        }
        switch (entry.getActivity()) {
            case "Sleep":
                entry_activity.setImageResource(R.drawable.sleep);
                break;
            case "Physical exercise":
                entry_activity.setImageResource(R.drawable.physical_exercise);
                break;
            case "Study":
                entry_activity.setImageResource(R.drawable.study);
                break;
            case "Attend lecture":
                entry_activity.setImageResource(R.drawable.attend_lecture);
                break;
            case "Commute":
                entry_activity.setImageResource(R.drawable.commute);
                break;
            case "Socialize":
                entry_activity.setImageResource(R.drawable.socialize);
                break;
            case "Eat":
                entry_activity.setImageResource(R.drawable.eat);
                break;
            //case "Chores":
            // entry_emotion.setImageResource(R.drawable.chores);
            //  break;
            case "Work":
                entry_activity.setImageResource(R.drawable.work);
                break;
            case "Relaxation":
                entry_activity.setImageResource(R.drawable.relaxation);
                break;
            //case "Other":
            //  entry_emotion.setImageResource(R.drawable.other);
            // break;
        }
        entry_activity.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        return convertView;
    }
}
