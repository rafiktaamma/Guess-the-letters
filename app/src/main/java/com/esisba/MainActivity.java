package com.esisba;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder builder ;
    AlertDialog dialog ;
    MaBaseManager mDatabase ;
    ArrayList<String> wordList;
    TextView tv1;
    EditText tv2;
    EditText tv3;
    TextView tv4;
    TextView tv5;
    TextView tv8;
    String prgress;
    String user;
    int userID;
    int userScore;
    int nb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiateAttrs();
        intiateDB();
        setDialog();

        Cursor cursor = mDatabase.getAllWords();
        if (cursor.moveToFirst()) {
            do {
                wordList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor = mDatabase.getAllUsers();
        cursor.moveToFirst();
        user = cursor.getString(2);
        userID = cursor.getInt(0);
        userScore = cursor.getInt(1);
        prgress = " / "+ wordList.size() ;
        nb = userScore < 5 ? userScore : 0;
        initGame(nb);

    }

    private void setDialog() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.dialogue);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @SuppressLint("SetTextI18n")
    private void initGame(int i) {
        String w = wordList.get(i);

        tv1.setText(String.valueOf(w.charAt(0)));
        tv2.setText("");
        tv3.setText("");
        tv4.setText(String.valueOf(w.charAt(3)));
        tv5.setText(nb+1 + prgress);
        tv8.setText("user : " + user + " Score :" + userScore);

    }

    private void initiateAttrs() {
        mDatabase = new MaBaseManager(this);
        wordList = new ArrayList<>();
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv8 = findViewById(R.id.tv8);

        tv2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1){
                    tv3.requestFocus();
                }
            }
        });
        tv3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1){
                    hideKeyboard(MainActivity.this);
                }
            }
        });
    }

    private void intiateDB() {
        mDatabase.addWord("MOON");
        mDatabase.addWord("SOON");
        mDatabase.addWord("ZOOM");
        mDatabase.addWord("ROOM");
        mDatabase.addWord("MOOD");
        mDatabase.addUser("user1", 0);
    }

    public void next(View view) {
        String t11 = String.valueOf(tv2.getText());
        String t12 = String.valueOf(tv3.getText());
        String t21 = String.valueOf(wordList.get(nb).charAt(1));
        String t22 = String.valueOf(wordList.get(nb).charAt(2));
        if (t11.equals(t21) && t12.equals(t22)) {
            if (userScore < 5){
                userScore ++;
                mDatabase.updateUser(userID, userScore);
                tv5.setText(nb + prgress);
            }
            if (nb < wordList.size() - 1)
                nb ++;
            else {
                nb = 0;
                dialog.show();
            }
            initGame(nb);
        }
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
