package app.habitrac.com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.models.UserModel;
import app.habitrac.com.utils.Constants;

public class FisrtWelcomeQ extends AppCompatActivity {
    private int currentQuestion = 1;
    private RadioButton q1b1,q1b2,q1b3,q1b4,q1b5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisrt_welcome_q);
        RadioButton otherRadioButton = findViewById(R.id.question2_radio_button5);
        EditText otherText = findViewById(R.id.question2_other_text);
        RadioButton otherRadioButton1 = findViewById(R.id.question5_radio_button4);
        EditText otherText1 = findViewById(R.id.question3_other_text);
        q1b1 = findViewById(R.id.question1_radio_button1);
        q1b2 = findViewById(R.id.question2_radio_button1);
        q1b3 = findViewById(R.id.question3_radio_button1);
        q1b4 = findViewById(R.id.question4_radio_button1);
        q1b5 = findViewById(R.id.question5_radio_button1);



        LinearLayout[] linearLayouts = new LinearLayout[]{
                findViewById(R.id.ss),
                findViewById(R.id.s2),
                findViewById(R.id.s3),
                findViewById(R.id.s4),
                findViewById(R.id.s5),
                findViewById(R.id.s6)
        };

        RadioGroup[] radioGroups = new RadioGroup[]{
                findViewById(R.id.question1_radio_group),
                findViewById(R.id.question2_radio_group),
                findViewById(R.id.question3_radio_group),
                findViewById(R.id.question4_radio_group),
                findViewById(R.id.question5_radio_group),
                findViewById(R.id.question6_radio_group)
        };

        Button nextButton = findViewById(R.id.next_button);
        otherRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    otherText.setVisibility(View.VISIBLE);
                    otherText.setEnabled(true);
                } else {
                    otherText.setVisibility(View.GONE);
                    otherText.setEnabled(false);
                    otherText.setText(""); // Clear the text when unchecked
                }
            }
        });
        otherRadioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    otherText1.setVisibility(View.VISIBLE);
                    otherText1.setEnabled(true);
                } else {
                    otherText1.setVisibility(View.GONE);
                    otherText1.setEnabled(false);
                    otherText1.setText(""); // Clear the text when unchecked
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                // Create a reference to the user's node
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference userRef = database.child("General").child(userId).child("questions");
                DatabaseReference userRef2 = database.child("Habitrac").child("users").child(userId).child("questions");

                // Handle each question and update Firebase accordingly

                    String questionId = "question1";

                    // Get the selected RadioButton group
                    RadioGroup radioButtonGroup = findViewById(R.id.question1_radio_group); // Replace with actual RadioGroup ID
                    int selectedRadioButtonId = radioButtonGroup.getCheckedRadioButtonId();

                    // Check if a RadioButton is selected

                        RadioButton radioButton = findViewById(selectedRadioButtonId);
                        String selectedValue = radioButton.getText().toString();

                        // Update Firebase with the selected value
                        userRef.child(questionId).setValue(selectedValue);
                userRef2.child(questionId).setValue(selectedValue);



                if (currentQuestion < 1 || currentQuestion > 6) {
                    // Handle invalid currentQuestion value
                    return;
                }

                if (currentQuestion == 1) {
                    // Check if a radio button is selected in question 1 (optional)
                    if (radioGroups[0].getCheckedRadioButtonId() == -1) {
                        // Show a message to select an option
                        Toast.makeText(getApplicationContext(), "Selecciona una opción", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                switch (currentQuestion) {
                    case 1:
                        radioGroups[0].setVisibility(View.GONE);
                        currentQuestion = 2;
                        break;
                    case 2:


                        // Handle each question and update Firebase accordingly

                        String questionId2 = "question2";

                        // Get the selected RadioButton group
                        RadioGroup radioButtonGroup2 = findViewById(R.id.question2_radio_group); // Replace with actual RadioGroup ID
                        int selectedRadioButtonId2 = radioButtonGroup2.getCheckedRadioButtonId();

                        // Check if a RadioButton is selected

                        RadioButton radioButton2 = findViewById(selectedRadioButtonId2);
                        String selectedValue2 = radioButton2.getText().toString();

                        // Update Firebase with the selected value
                        userRef.child(questionId2).setValue(selectedValue2);
                        userRef2.child(questionId2).setValue(selectedValue2);
                        radioGroups[0].setVisibility(View.GONE);
                        currentQuestion = 3;
                        break;
                    case 3:
                        // Handle each question and update Firebase accordingly

                        String questionId3 = "question3";

                        // Get the selected RadioButton group
                        RadioGroup radioButtonGroup3 = findViewById(R.id.question3_radio_group); // Replace with actual RadioGroup ID
                        int selectedRadioButtonId3 = radioButtonGroup3.getCheckedRadioButtonId();

                        // Check if a RadioButton is selected

                        RadioButton radioButton3 = findViewById(selectedRadioButtonId3);
                        String selectedValue3 = radioButton3.getText().toString();

                        // Update Firebase with the selected value
                        userRef.child(questionId3).setValue(selectedValue3);
                        userRef2.child(questionId3).setValue(selectedValue3);
                        radioGroups[0].setVisibility(View.GONE);
                        currentQuestion = 4;
                        break;
                    case 4:

                        // Handle each question and update Firebase accordingly

                        String questionId4 = "question4";

                        // Get the selected RadioButton group
                        RadioGroup radioButtonGroup4 = findViewById(R.id.question4_radio_group); // Replace with actual RadioGroup ID
                        int selectedRadioButtonId4 = radioButtonGroup4.getCheckedRadioButtonId();

                        // Check if a RadioButton is selected

                        RadioButton radioButton4 = findViewById(selectedRadioButtonId4);
                        String selectedValue4 = radioButton4.getText().toString();

                        // Update Firebase with the selected value
                        userRef.child(questionId4).setValue(selectedValue4);
                        userRef2.child(questionId4).setValue(selectedValue4);

                        radioGroups[0].setVisibility(View.GONE);
                        currentQuestion = 5;
                        break;
                    case 5:

                        // Handle each question and update Firebase accordingly

                        String questionId5 = "question5";

                        // Get the selected RadioButton group
                        RadioGroup radioButtonGroup5 = findViewById(R.id.question5_radio_group); // Replace with actual RadioGroup ID
                        int selectedRadioButtonId5 = radioButtonGroup5.getCheckedRadioButtonId();

                        // Check if a RadioButton is selected

                        RadioButton radioButton5 = findViewById(selectedRadioButtonId5);
                        String selectedValue5 = radioButton5.getText().toString();

                        // Update Firebase with the selected value
                        userRef.child(questionId5).setValue(selectedValue5);
                        userRef2.child(questionId5).setValue(selectedValue5);
                        radioGroups[0].setVisibility(View.GONE);
                        currentQuestion = 6;
                        break;
                    case 6:
                        // Handle each question and update Firebase accordingly

                        String questionId6 = "question6";

                        // Get the selected RadioButton group
                        RadioGroup radioButtonGroup6 = findViewById(R.id.question6_radio_group); // Replace with actual RadioGroup ID
                        int selectedRadioButtonId6 = radioButtonGroup6.getCheckedRadioButtonId();

                        // Check if a RadioButton is selected

                        RadioButton radioButton6 = findViewById(selectedRadioButtonId6);
                        String selectedValue6 = radioButton6.getText().toString();

                        // Update Firebase with the selected value
                        userRef.child(questionId6).setValue(selectedValue6);
                        userRef2.child(questionId6).setValue(selectedValue6);


                        radioGroups[0].setVisibility(View.GONE);

                        startActivity(new Intent(FisrtWelcomeQ.this, MainActivity.class));
                        finish();// Loop back to question 1
                        break;
                }

                updateLayoutVisibility(linearLayouts, radioGroups, currentQuestion);
            }
        });
    }

    private void updateLayoutVisibility(LinearLayout[] linearLayouts, RadioGroup[] radioGroups, int currentQuestion) {
        for (int i = 0; i < linearLayouts.length; i++) {
            linearLayouts[i].setVisibility(i == (currentQuestion - 1) ? View.VISIBLE : View.GONE);
        }

        for (int i = 0; i < radioGroups.length; i++) {
            radioGroups[i].setVisibility(i == (currentQuestion - 1) ? View.VISIBLE : View.GONE);
        }
    }
}