package com.example.tablesono;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout plates;
    private TextView totalAccount, totalPerGuest;
    private float lastAmount, totalAmount, totalPlates, totalGuests, tip, guests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plates = findViewById(R.id.plates);
        EditText tipPercent = findViewById(R.id.tip_amount);
        EditText numberOfGuests = findViewById(R.id.guests_amount);
        totalAccount = findViewById(R.id.total_account_amount);
        totalPerGuest = findViewById(R.id.total_guests_amount);

        lastAmount = 0f;
        totalAmount = 0f;
        totalPlates = 0f;
        totalGuests = 0f;
        tip = 0f;
        guests = 1f;

        tipPercent.addTextChangedListener(new PercentWatcher());
        numberOfGuests.addTextChangedListener(new GuestsWatcher());
    }

    public void addPlates(View view) {
        Context context = plates.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.plate, null);
        plates.addView(layout);
        EditText price = (EditText) layout.getChildAt(2);

        price.addTextChangedListener(new PlatesWatcher());
    }

    public void removePlates(View view) {
        RelativeLayout layout = (RelativeLayout) view.getParent();
        EditText lastCost = (EditText) layout.getChildAt(2);
        totalPlates -= Float.parseFloat(lastCost.getText().toString());
        plates.removeView(layout);
        updateTotals();
    }

    @SuppressLint("DefaultLocale")
    private void updateTotals() {
        totalAmount = totalPlates + (totalPlates * tip);
        totalAccount.setText(String.format("%.2f", totalAmount));

        Log.i("MainActivity.java > updateTotals", "" + guests);
        totalGuests = totalAmount/guests;
        totalPerGuest.setText(String.format("%.2f", totalGuests));
    }

    private void updateLastAmount(CharSequence valueToChange) {
        String newText = valueToChange.toString(),
                newValue = newText.startsWith(".")? "0"+newText:newText;
        lastAmount = Float.parseFloat(newValue.isEmpty()?"0.00":newValue);
    }

    @SuppressLint("DefaultLocale")
    private void updateTotalPlates(float toChangeAmount) {
        totalPlates += toChangeAmount - lastAmount;
        updateTotals();
    }

    @SuppressLint("DefaultLocale")
    private void updateTotalPercent(float toChangePercent) {
        tip = toChangePercent/100;
        updateTotals();
    }

    @SuppressLint("DefaultLocale")
    private void updateGuests(float toChangeGuests) {
        guests = toChangeGuests < 1? 1:toChangeGuests;
        updateTotals();
    }

    private class PlatesWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateLastAmount(charSequence);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String textCost = editable.toString();
            textCost = textCost.startsWith(".")? "0"+textCost:textCost;
            float cost = Float.parseFloat(textCost.isEmpty()?"0.00":textCost);
            updateTotalPlates(cost);
        }
    }

    private class PercentWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateLastAmount(charSequence);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String textPercent = editable.toString();
            textPercent = textPercent.startsWith(".")? "0"+textPercent:textPercent;
            float tip = Float.parseFloat(textPercent.isEmpty()?"0.00":textPercent);
            updateTotalPercent(tip);
        }
    }

    private class GuestsWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateLastAmount(charSequence);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String textGuests = editable.toString();
            textGuests = textGuests.startsWith(".")? "0"+textGuests:textGuests;
            float numberOFGuests = Float.parseFloat(textGuests.isEmpty()?"0.00":textGuests);
            updateGuests(numberOFGuests);
        }
    }
}