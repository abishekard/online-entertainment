package com.abishek534.entertainment.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.abishek534.entertainment.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bottomCardSetting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bottomCardSetting extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private CheckBox oneCheckBox,allCheckBox;
    private SharedPreferences sets;
    private TextView detailsView;
    private String videoPath;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public bottomCardSetting(String videoPath) {
        this.videoPath = videoPath;
    }

    public bottomCardSetting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment bottomCardSetting.
     */
    // TODO: Rename and change types and number of parameters
    public static bottomCardSetting newInstance(String param1, String param2) {
        bottomCardSetting fragment = new bottomCardSetting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view =inflater.inflate(R.layout.setting_bottom_card_setting, container, false);
         oneCheckBox = view.findViewById(R.id.one_checkBox);
         allCheckBox = view.findViewById(R.id.all_checkBox);
         detailsView = view.findViewById(R.id.video_details);

         detailsView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
              /*   hmvProperties displayDetails = new hmvProperties(videoPath);
                 displayDetails.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.setting_dialog);
                 displayDetails.show(getFragmentManager(),"videoDetails");
                 dismiss();*/
             }
         });

        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(true);
      //  volume();
      //  brightness();
        repeat();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        }
        catch(ClassCastException e)
        {
            throw  new ClassCastException(context.toString() +"must implement BottomSheetListener");
        }
    }
    public interface BottomSheetListener{
        void loopStateChanged(String text);


    }

    public void repeat()
    {
      //  sets = getActivity().getPreferences(Context.MODE_PRIVATE);
        sets= PreferenceManager.getDefaultSharedPreferences(getContext());
        String videoRepeat = sets.getString("loopSetting","one");
        final SharedPreferences.Editor editor = sets.edit();
        if(videoRepeat.equals("one"))
        {
            oneCheckBox.setChecked(true);
            allCheckBox.setChecked(false);


        }
        else
        {
            oneCheckBox.setChecked(false);
            allCheckBox.setChecked(true);
        }
        oneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    allCheckBox.setChecked(false);
                    oneCheckBox.setChecked(true);
                    editor.putString("loopSetting","one").apply();
                    mListener.loopStateChanged("one");
                    dismiss();

                }
            }
        });
        allCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                   oneCheckBox.setChecked(false);
                   allCheckBox.setChecked(true);
                   editor.putString("loopSetting","all").apply();
                    mListener.loopStateChanged("all");
                   dismiss();

                }
            }
        });



    }
}
