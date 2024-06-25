package app.habitrac.com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import app.habitrac.com.R;
import app.habitrac.com.databinding.ActivityTimerBinding;
import app.habitrac.com.models.RoutineModel;
import app.habitrac.com.models.StepsLocalModel;
import app.habitrac.com.notification.NotificationHelper;
import app.habitrac.com.utils.Constants;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity {
    ActivityTimerBinding binding;
    private CountDownTimer countDownTimer;
    private long remainingTimeInMillis, initialTimeInMillis;
    private boolean isPaused = true;
    int count = 0;
    ArrayList<StepsLocalModel> stepsList;
    StepsLocalModel stepsModel;
    RoutineModel model;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, RoutineStartActivity.class));
        finish();
    }
/*
    private void showAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                binding.placeholder.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);
          //  showAd();
        }

        model = (RoutineModel) Stash.getObject(Constants.ROUTINE_LIST, RoutineModel.class);

        binding.toolbar.tittle.setText(getString(R.string.start_routine));
        Constants.initDialog(this);

        ArrayList<StepsLocalModel> modelList = Stash.getArrayList(model.getID(), StepsLocalModel.class);
        stepsList = new ArrayList<>();
        for (StepsLocalModel model : modelList) {
            if (!model.isCompleted()) {
                stepsList.add(model);
            }
        }

        stepsModel = stepsList.get(count);

        initialTimeInMillis = (long) Constants.extractSingleTimeValue(stepsModel.getTime()) * 60 * 1000;
        remainingTimeInMillis = initialTimeInMillis;
        setValue(stepsModel, count);

        binding.startPause.setOnClickListener(v -> {
            if (isPaused) {
                startTimer();
            } else {
                pauseTimer();
            }
        });

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.resetTime.setOnClickListener(v -> {
            resetTimer();
        });

        binding.skip.setOnClickListener(v -> {
            nextTask();
        });

        binding.completed.setOnClickListener(v -> {
            taskComplete();
        });

    }

    private void nextTask(){
        count = count + 1;
        stepsModel = stepsList.get(count);
        setValue(stepsModel, count);
    }

    private void taskComplete() {
        ArrayList<StepsLocalModel> mainList = Stash.getArrayList(model.getID(), StepsLocalModel.class);
        for (StepsLocalModel list : mainList){
            if (list.getName().equals(stepsList.get(count).getName())){
                mainList.get(count).setCompleted(true);
            }
        }
        Stash.put(model.getID(), mainList);

        if (count < stepsList.size()-1){
            nextTask();
        } else {
            finishTask();
        }

    }

    private void finishTask() {
        Stash.put("CONGRATS", model);
        startActivity(new Intent(this, CompletedActivity.class));
        finish();
    }

    private void setValue(StepsLocalModel stepsModel, int count) {
        binding.minutesSchedule.setText(Constants.extractSingleTimeValue(stepsModel.getTime()) + " " + getString(R.string.mint_scheduled) );
        binding.stepName.setText(stepsModel.getName());
        String startNow = Constants.finishTime(Constants.getCurrentTime(), Constants.extractSingleTimeValue(stepsModel.getTime()));
        binding.finishTime.setText(startNow);
        binding.remainingSteps.setText(""+stepsList.size());

        if (count < stepsList.size()-1){
            binding.nextStep.setText(stepsList.get(count + 1).getName());
        } else {
            binding.skip.setVisibility(View.INVISIBLE);
            binding.nextStep.setText(R.string.you_re_done_with_everything);
        }
        binding.clock.setText(Constants.extractSingleTimeValue(stepsModel.getTime()) + getString(R.string._00_min));
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        } else {
            NotificationHelper helper = new NotificationHelper(this);
            String body = stepsModel.getName() + " " + stepsModel.getTime() + " " + getString(R.string.left);
            helper.sendHighPriorityNotification(getString(R.string.start_routine), body, TimerActivity.class);
        }

        countDownTimer = new CountDownTimer(remainingTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) {
                    remainingTimeInMillis = millisUntilFinished;
                    updateCountdownText();
                    updateProgressBar();
                }
            }

            @Override
            public void onFinish() {
                binding.clock.setText("0:00");
                binding.progressBar.setProgress(100);
                taskComplete();
            }
        };

        countDownTimer.start();
        isPaused = false;
        binding.startPauseText.setText(getString(R.string.pause));
        binding.startPauseIcon.setImageResource(R.drawable.pause);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    private void updateViews() {
        binding.mainCard.setCardBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.skip.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.completed.setBackgroundTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));
        binding.completed.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.skipIco.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.finishTime.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.remainingSteps.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.skipText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isPaused = true;
            binding.startPauseText.setText(getString(R.string.resume));
            binding.startPauseIcon.setImageResource(R.drawable.play);
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        remainingTimeInMillis = initialTimeInMillis;
        updateCountdownText();
        updateProgressBar();
        isPaused = true;
        binding.startPauseText.setText(getString(R.string.start));
        binding.startPauseIcon.setImageResource(R.drawable.play);
    }

    private void updateCountdownText() {
        long minutes = (remainingTimeInMillis / 1000) / 60;
        long seconds = (remainingTimeInMillis / 1000) % 60;
        String formattedTime = String.format("%d:%02d", minutes, seconds);
        binding.clock.setText(formattedTime + " " + getString(R.string.mint));
    }

    private void updateProgressBar() {
        int progress = (int) (((float) (initialTimeInMillis - remainingTimeInMillis) / initialTimeInMillis) * 100);
        binding.progressBar.setProgress(progress);
    }

}