package com.bgomes.owner.tictactoe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class TicTacToeActivity extends Activity
            implements OnClickListener {

    private TextView statusMessage;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;

    private int[] buttonArray = new int[9];
    private String player,
                   player0,
                   player1,
                   statusString,
                   name0 = "X",
                   name1 = "O";
    private boolean gameOver;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        statusMessage = (TextView) findViewById(R.id.statusMessageTextView);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);

        newGame();

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onPause() {
        // save the instance variables
        Editor editor = prefs.edit();
        editor.putInt("bA0", buttonArray[0]);
        editor.putInt("bA1", buttonArray[1]);
        editor.putInt("bA2", buttonArray[2]);
        editor.putInt("bA3", buttonArray[3]);
        editor.putInt("bA4", buttonArray[4]);
        editor.putInt("bA5", buttonArray[5]);
        editor.putInt("bA6", buttonArray[6]);
        editor.putInt("bA7", buttonArray[7]);
        editor.putInt("bA8", buttonArray[8]);
        editor.putString("player", player);
        editor.putBoolean("gameOver", gameOver);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // get the instance variables
        gameOver = prefs.getBoolean("gameOver", false);
        for (int i = 0; i < 9; i++) {
            buttonArray[i] = prefs.getInt("bA" + i, 0);
        }
        name0 = prefs.getString("x_preference", "X");
        name1 = prefs.getString("o_preference", "O");
        player = prefs.getString("player", player0);
        loadGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tic_tac_toe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
           startActivity(new Intent(getApplicationContext(),
                   SettingsActivity.class));
           return true;
        } else if (id == R.id.menu_newGame) {
            newGame();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            evaluateSquare(1);
        } else if (v.getId() == R.id.button2) {
            evaluateSquare(2);
        } else if (v.getId() == R.id.button3) {
            evaluateSquare(3);
        } else if (v.getId() == R.id.button4) {
            evaluateSquare(4);
        } else if (v.getId() == R.id.button5) {
            evaluateSquare(5);
        } else if (v.getId() == R.id.button6) {
            evaluateSquare(6);
        } else if (v.getId() == R.id.button7) {
            evaluateSquare(7);
        } else if (v.getId() == R.id.button8) {
            evaluateSquare(8);
        } else if (v.getId() == R.id.button9) {
            evaluateSquare(9);
        }
    }

    private void newGame() {
        if (!name0.equalsIgnoreCase("X")) {
            player0 = name0;
        } else { player0 = "X"; }
        if (!name1.equalsIgnoreCase("O")) {
            player1 = name1;
        } else { player1 = "O"; }

        player = player0;
        statusString = "Player " + player + "'s turn";
        statusMessage.setText(statusString);
        gameOver = false;
        for (int i = 0; i < 9; i++) {
            buttonArray[i] = 0;
            getSquare(i+1);
        }
    }

    private void evaluateSquare (int sqrNum) {

        if (gameOver) {
            return;
        }
        if(buttonArray[sqrNum-1] != 0) {
            /*
                Taking sqrNum-1 as square 1 is array's 0th element
                and testing if it has a value (X or O)
             */
            Toast t = Toast.makeText(this, "That square is already taken", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        /*
            the value of buttonArray's elements: one = X, negative one = O
         */
        if (player.equalsIgnoreCase(player0)) {
            buttonArray[sqrNum - 1] = 1;
        } else {
            buttonArray[sqrNum - 1] = -1;
        }
        getSquare(sqrNum);
        checkForWinner();
        /*
            The following code rotates player value between turns
         */
        if (!gameOver) {
            if (player.equalsIgnoreCase(player0)) {
                player = player1;
            } else {
                player = player0;
            }
            statusString = "Player " + player + "'s turn";
            statusMessage.setText(statusString);
        }

        return;
    }

    private void checkForWinner() {
        boolean winner = false,
                draw = true;

        if (Math.abs(buttonArray[0] + buttonArray[1] + buttonArray[2]) == 3) { winner = true; }
        if (Math.abs(buttonArray[3] + buttonArray[4] + buttonArray[5]) == 3) { winner = true; }
        if (Math.abs(buttonArray[6] + buttonArray[7] + buttonArray[8]) == 3) { winner = true; }

        if (Math.abs(buttonArray[0] + buttonArray[3] + buttonArray[6]) == 3) { winner = true; }
        if (Math.abs(buttonArray[1] + buttonArray[4] + buttonArray[7]) == 3) { winner = true; }
        if (Math.abs(buttonArray[2] + buttonArray[5] + buttonArray[8]) == 3) { winner = true; }

        if (Math.abs(buttonArray[0] + buttonArray[4] + buttonArray[8]) == 3) { winner = true; }
        if (Math.abs(buttonArray[2] + buttonArray[4] + buttonArray[6]) == 3) { winner = true; }

        if (winner) {
            draw = false;
            statusString = player + " wins!";
        } else {
            for (int i = 0; i <= 8; i++) {
                if (buttonArray[i] == 0) {
                    draw = false;
                }
            }
            if (draw) {
                statusString = "Game is a draw";
            }
        }
        if (winner || draw) {
            gameOver = true;
            statusMessage.setText(statusString);
        } else {
            gameOver = false;
        }
    }

    private void getSquare(int sqrNum) {
        String squareValue;
        switch (buttonArray[sqrNum-1]) {
            case -1:
                squareValue = "O";
                break;
            case 1:
                squareValue = "X";
                break;
            default:
                squareValue = "";
        }
        if (sqrNum == 1) {
            button1.setText(squareValue);
        } else if (sqrNum == 2) {
            button2.setText(squareValue);
        } else if (sqrNum == 3) {
            button3.setText(squareValue);
        } else if (sqrNum == 4) {
            button4.setText(squareValue);
        } else if (sqrNum == 5) {
            button5.setText(squareValue);
        } else if (sqrNum == 6) {
            button6.setText(squareValue);
        } else if (sqrNum == 7) {
            button7.setText(squareValue);
        } else if (sqrNum == 8) {
            button8.setText(squareValue);
        } else if (sqrNum == 9) {
            button9.setText(squareValue);
        }
    }

    private void loadGame() {
        int xCounter = 0,
            yCounter = 0;

        /*
            The following if statements verify that the user didn't enter in O for player X's name
            and vise versa. If it was, the program forces the names back to default (X & O).
         */
        if (name0.equalsIgnoreCase("O")) {
            name0 = "X";
            Toast t = Toast.makeText(this, "Player Xs name can't be O", Toast.LENGTH_SHORT);
            t.show();
        }
        if (name1.equalsIgnoreCase("X")) {
            name1 = "O";
            Toast t = Toast.makeText(this, "Player Os name can't be X", Toast.LENGTH_SHORT);
            t.show();
        }

        if (!name0.equalsIgnoreCase("X")) {
            player0 = name0;
        } else { player0 = "X"; }
        if (!name1.equalsIgnoreCase("O")) {
            player1 = name1;
        } else { player1 = "O"; }

        if (!gameOver) {
            /*
                The x and y counters allow a player to change the name during a turn, and
                the program will display the correct values for X and O immediately.
             */
            for (int j = 0; j < 9; j++) {
                if (buttonArray[j] == 1) {
                    xCounter ++;
                } else if (buttonArray[j] == -1) {
                    yCounter ++;
                }
            }
            if (xCounter == yCounter) {
                player = player0;
            } else {
                player = player1;
            }

            statusString = "Player " + player + "'s turn";
            statusMessage.setText(statusString);
        }
        for (int i = 1; i < 10; i++) {
            getSquare(i);
        }
        checkForWinner();
    }
}
