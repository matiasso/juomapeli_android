package com.example.juomapeli;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Random;

public class Juomapeli2 extends AppCompatActivity {


    private ArrayList<String> deck = new ArrayList<String>();
    private int playerIndex = -1;
    private String snake = "";
    private String questionMan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDeck();
        setContentView(R.layout.activity_juomapeli2);
        ImageView imgClick = (ImageView) findViewById(R.id.imageView);
        imgClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePlayer();
            }
        });
    }


    private void createDeck() {
        String va;
        for (int i = 1; i < 14; i++) {
            va = String.valueOf(i);
            deck.add("h" + va);
            deck.add("d" + va);
            deck.add("s" + va);
            deck.add("c" + va);
        }
    }

    private String drawCard() {
        if (deck.size() == 0)
            createDeck();
        Random rnd = new Random();
        int index = rnd.nextInt(deck.size());
        String item = deck.get(index);
        deck.remove(index);
        return item;
    }


    private void updateTurnIndex() {
        playerIndex++;
        if (playerIndex >= MainActivity.players.size()) {
            playerIndex = 0;
        }
    }

    private Drawable GetImage(Context c, String name) {
        return ContextCompat.getDrawable(c, c.getResources().getIdentifier(name, "drawable", c.getPackageName()));
    }

    private String getRule(String card) {
        SharedPreferences prefs = getSharedPreferences("juomapeli_info", 0);
        int number = Integer.parseInt(card.substring(1));
        String prefName = card.substring(0, 1);
        boolean red = (prefName.equals("d") || prefName.equals("h")) ? true : false;
        prefName = (red ? "r" : "b") + number;
        String rule = prefs.getString(prefName, "NULL");
        if (!rule.equals("NULL")) {
            if (rule.toLowerCase().equals("katsekäärme"))
                snake = MainActivity.players.get(playerIndex);
            else if (rule.toLowerCase().equals("kysymysmestari"))
                questionMan = MainActivity.players.get(playerIndex);
            return rule;
        }
        if (number == 1)
            return "vesiputous";
        else if (number > 1 && number < 7)
            return String.format("%1$s %2$d huikkaa", red ? "jaa" : "juo", number);
        else if (number == 7) {
            snake = MainActivity.players.get(playerIndex);
            return "katsekäärme";
        } else if (number == 8) {
            questionMan = MainActivity.players.get(playerIndex);
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
            return "JOKU ERROR ILMENI :(";
    }


    private void updateCardRule(String card) {
        //Recode this to allow user customization
        String text = "Kortti: ";
        text += getRule(card);
        TextView rules = (TextView) findViewById(R.id.textViewCardInfo);
        rules.setText(text);

        //Then the special rules (Katsekäärme & kysymysmestari)
        TextView specialText = (TextView) findViewById(R.id.textViewSpecials);
        String special = "";
        if (!snake.equals(""))
            special += "Katsekäärme: " + snake;
        if (!questionMan.equals((""))) {
            if (!snake.equals(""))
                special += "\n"; //Add new line if kysymysmestari is above this!
            special += "Kysymysmestari: " + questionMan;
        }
        specialText.setText(special);
    }

    private void changePlayer() {
        //this runs onClick
        ImageView img = (ImageView) findViewById(R.id.imageView);
        ImageView backImg = (ImageView) findViewById(R.id.imageViewCardBack);
        if (deck.size() == 0) {
            Toast toast = Toast.makeText(this, "Pakka loppui! Sekoitetaan...", Toast.LENGTH_LONG);
            toast.show();
            createDeck();
            backImg.setImageDrawable(GetImage(this, "back_red"));
            img.setImageDrawable(GetImage(this, "back_red"));
            img.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shuffle));
        } else {
            updateTurnIndex();
            TextView playerTurn = (TextView) findViewById(R.id.textViewTurn);
            String title = String.format("Pelaajan %s vuoro", MainActivity.players.get(playerIndex));
            SpannableStringBuilder str = new SpannableStringBuilder(title);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 9, title.length() - 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            playerTurn.setText(str);
            //Draw a mew card.
            String card = drawCard();
            updateCardRule(card);
            Drawable cardImg = GetImage(this, card);
            backImg.setImageDrawable(img.getDrawable());
            img.setImageDrawable(cardImg);
            img.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left));
        }
    }
}


