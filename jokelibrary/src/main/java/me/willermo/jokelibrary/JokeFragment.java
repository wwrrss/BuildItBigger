package me.willermo.jokelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by william on 9/27/15.
 */
public class JokeFragment extends Fragment {
    public JokeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.joke_lib_fragment,container,false);
        TextView textView = (TextView) root.findViewById(R.id.textView);
        Intent intent = getActivity().getIntent();
        String joke = intent.getStringExtra("joke");
        if(joke!=null&&joke.length()!=0){
            textView.setText(joke);
        }
        return root;
    }
}
