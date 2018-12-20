package usi.memotion2personal.UI.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import usi.memotion2personal.R;

/**
 * Created by usi on 08/02/17.
 */

public class PamSurveyView extends LinearLayout {

    public final static int EARLY_MORNING = 0;
    public final static int MORNING = 1;
    public final static int AFTERNOON = 2;
    public final static int LECTURE = 3;



    OnPAMSelectionListener listener;

    public interface OnPAMSelectionListener {
        public void onPAMSelection(int position);
    }

    public void setOnPAMSelectionListener(OnPAMSelectionListener l) {
        listener = l;
    }

    static int[][] pamImages = {
            {R.drawable.afraid_1_1, R.drawable.afraid_1_2, R.drawable.afraid_1_3},
            {R.drawable.tense_2_1, R.drawable.tense_2_2, R.drawable.tense_2_1},
            {R.drawable.excited_3_1, R.drawable.excited_3_2, R.drawable.excited_3_3},
            {R.drawable.delighted_4_1, R.drawable.delighted_4_2, R.drawable.delighted_4_3},
            {R.drawable.frustrated_5_1, R.drawable.frustrated_5_2, R.drawable.frustrated_5_3},
            {R.drawable.angry_6_1, R.drawable.angry_6_2, R.drawable.angry_6_3},
            {R.drawable.happy_7_1, R.drawable.happy_7_2, R.drawable.happy_7_3},
            {R.drawable.glad_8_1, R.drawable.glad_8_2, R.drawable.glad_8_3},
            {R.drawable.miserable_9_1, R.drawable.miserable_9_2, R.drawable.miserable_9_3},
            {R.drawable.sad_10_1, R.drawable.sad_10_2, R.drawable.sad_10_3},
            {R.drawable.calm_11_1, R.drawable.calm_11_2, R.drawable.calm_11_3},
            {R.drawable.satisfied_12_1, R.drawable.satisfied_12_2, R.drawable.satisfied_12_3},
            {R.drawable.gloomy_13_1, R.drawable.gloomy_13_2, R.drawable.gloomy_13_3},
            {R.drawable.tired_14_1, R.drawable.tired_14_2, R.drawable.tired_14_3},
            {R.drawable.sleepy_15_1, R.drawable.sleepy_15_2, R.drawable.sleepy_15_3},
            {R.drawable.serene_16_1, R.drawable.serene_16_2, R.drawable.serene_16_3}
    };

    private Context context;
    private GridView imageGrid;
    private TextView textView;
    private int chosen = -1;

    public PamSurveyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    private void init() {
//        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = li.inflate(R.layout.surveys_pam_layout, this, true);
        imageGrid = (GridView) root.findViewById(R.id.pamImageGrid);
        textView = (TextView) root.findViewById(R.id.pam_explanation);
        imageGrid.setVerticalScrollBarEnabled(false);
        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listener != null)
                    listener.onPAMSelection(i);

                //((ImageView)view).setColorFilter(Color.argb(50, 0, 0, 0), PorterDuff.Mode.OVERLAY);
                //chosen = i;
            }
        });
        initSurvey();
    }

    public void setPAMExplanation(int type){
        switch (type){
            case MORNING:
                textView.setText(context.getString(R.string.pam_image_explanation_morning));
                break;
            case AFTERNOON:
                textView.setText(context.getString(R.string.pam_image_explanation_afternoon));
                break;
            case LECTURE:
                textView.setText(context.getString(R.string.pam_image_explanation_lecture));
                break;
            default:
                textView.setText(context.getString(R.string.pam_image_explanation));
                break;
        }
    }


    public void initSurvey() {
        Random r = new Random();
        int[] images = new int[16];

        for(int i = 0; i < images.length; i++) {
            images[i] = pamImages[i][r.nextInt(3)];
        }

        imageGrid.setAdapter(new PamImageAdapter(images, context));
    }

}