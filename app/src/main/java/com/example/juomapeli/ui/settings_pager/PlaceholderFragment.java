package com.example.juomapeli.ui.settings_pager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.juomapeli.R;

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "1";

    private PageViewModel pageViewModel;
    private Button buttonSave;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    private Drawable GetImage(Context c, String name) {
        return ContextCompat.getDrawable(c, c.getResources().getIdentifier(name, "drawable", c.getPackageName()));
    }


    private String getRule(String card) {
        SharedPreferences prefs = getActivity().getSharedPreferences("juomapeli_info", 0);
        if (prefs.contains(card))
            return prefs.getString(card, "");
        boolean isRed = card.substring(0, 1).equals("r");
        int number = Integer.parseInt(card.substring(1));
        if (number == 1)
            return "vesiputous";
        else if (number > 1 && number < 7)
            return String.format("%1$s %2$d huikkaa", isRed ? "jaa" : "juo", number);
        else if (number == 7) {
            return "katsekäärme";
        } else if (number == 8) {
            return "kysymysmestari";
        } else if (number == 9)
            return "kategoria";
        else if (number == 10)
            return "tarina";
        else if (number == 11)
            return "taukokortti";
        else if (number == 12)
            return "huorakortti";
        else if (number == 13)
            return "oma sääntö";
        else
            return "ERROR getRule()";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_card_rules, container, false);

        final TextView textViewRed = root.findViewById(R.id.textViewRed);
        final TextView textViewBlack = root.findViewById(R.id.textViewBlack);
        final ImageView imgview = root.findViewById(R.id.imageViewMixedCard);
        final EditText editRed = root.findViewById(R.id.editTextRed);
        final EditText editBlack = root.findViewById(R.id.editTextBlack);
        buttonSave = root.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getArguments().getInt(ARG_SECTION_NUMBER);
                EditText rText = (EditText) root.findViewById(R.id.editTextRed);
                EditText bText = (EditText) root.findViewById(R.id.editTextBlack);
                String red = rText.getText().toString();
                String black = bText.getText().toString();
                if (!red.isEmpty() && !black.isEmpty()) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("juomapeli_info", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    //Maybe add a toggle to set both to be the same thing?
                    editor.putString("r" + index, red);
                    editor.putString("b" + index, black);
                    editor.commit();
                    Toast toast = Toast.makeText(getContext(), "Asetukset tallennettu kortille numero: " + index, Toast.LENGTH_LONG);
                    toast.show(); //This will show the msg that the saving was successful
                    System.out.println(String.format("Saving player preferences: R%1$d: %2$s,\t B%1$d: %3$s", index, red, black));
                } else {
                    Toast toast = Toast.makeText(getContext(), "ERROR: Täytä molemmat kentät", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                int index = getArguments().getInt(ARG_SECTION_NUMBER);
                textViewRed.setText("Punainen " + index);
                textViewBlack.setText("Musta " + index);
                editBlack.setHint(getRule("b" + index));
                editBlack.setText("");
                editRed.setHint(getRule("r" + index));
                editRed.setText("");
                Drawable cardImg = GetImage(getContext(), "br" + index);
                imgview.setImageDrawable(cardImg);

            }
        });
        return root;
    }
}