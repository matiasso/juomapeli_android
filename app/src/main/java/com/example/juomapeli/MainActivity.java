package com.example.juomapeli;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> players = new ArrayList<String>();
    private Button buttonAddPlayer;
    private Button buttonStartGame;
    private Button buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText nameInput = (EditText) findViewById(R.id.editText);
        buttonAddPlayer = (Button) findViewById(R.id.buttonAdd);
        buttonStartGame = (Button) findViewById(R.id.buttonBegin);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameLayout();
            }
        });
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    buttonAddPlayer.setEnabled(false);
                } else {
                    buttonAddPlayer.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onClick(View view) {
        EditText nameInput = (EditText) findViewById(R.id.editText);
        TextView playersList = (TextView) findViewById(R.id.textViewPlayers);
        String newPlayer = nameInput.getText().toString();
        nameInput.setText("");
        players.add(newPlayer);
        String newPlayerList = String.format("Valitut pelaajat: (%1$d kpl)\n%2$s", this.players.size(), String.join("\n", players));
        playersList.setText(newPlayerList);
        if (players.size() > 1) {
            Button startGame = (Button) findViewById(R.id.buttonBegin);
            startGame.setEnabled(true);
        }
    }

    public void openGameLayout() {
        Intent intent = new Intent(this, Juomapeli2.class);
        startActivity(intent);
    }

    private void openSettings() {
        Intent intent = new Intent(this, CardRules.class);
        startActivity(intent);
    }
}