package app.habitrac.com.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import app.habitrac.com.R;
import app.habitrac.com.databinding.FragmentInsightsBinding;
import app.habitrac.com.models.HistoryModel;
import app.habitrac.com.utils.Constants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class InsightsFragment extends Fragment implements OnChartValueSelectedListener {
    FragmentInsightsBinding binding;
    int active = 0;
    private BarChart barChart;
    ArrayList<BarEntry> barArraylist;
    ArrayList<HistoryModel> historyList;
    ArrayList<HistoryModel> last7DaysData = new ArrayList<>();
    ArrayList<HistoryModel> last30DaysData = new ArrayList<>();
    ArrayList<HistoryModel> last365DaysData = new ArrayList<>();

    public InsightsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInsightsBinding.inflate(getLayoutInflater(), container, false);

        if (Stash.getBoolean(Constants.LANGUAGE, true)) {
            Constants.setLocale(requireContext(), Constants.EN);
        } else {
            Constants.setLocale(requireContext(), Constants.ES);
        }

        Constants.initDialog(requireContext());

        barChart = binding.chart;
        barChart.setOnChartValueSelectedListener(this);

        barArraylist = new ArrayList<>();
        historyList = new ArrayList<>();

        getData();

        binding.week.setOnClickListener(v -> {
            active = 0;
            binding.week.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
            binding.month.setTextColor(getResources().getColor(R.color.white));
            binding.year.setTextColor(getResources().getColor(R.color.white));

            binding.week.setBackground(getResources().getDrawable(R.drawable.btn_bg_solid));
            binding.month.setBackground(getResources().getDrawable(R.drawable.btn_bg_border));
            binding.year.setBackground(getResources().getDrawable(R.drawable.btn_bg_border));
            addData();
        });
        binding.month.setOnClickListener(v -> {
            active = 1;
            binding.month.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
            binding.week.setTextColor(getResources().getColor(R.color.white));
            binding.year.setTextColor(getResources().getColor(R.color.white));

            binding.month.setBackground(getResources().getDrawable(R.drawable.btn_bg_solid));
            binding.week.setBackground(getResources().getDrawable(R.drawable.btn_bg_border));
            binding.year.setBackground(getResources().getDrawable(R.drawable.btn_bg_border));
            addData();
        });
        binding.year.setOnClickListener(v -> {
            active = 2;
            binding.year.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
            binding.month.setTextColor(getResources().getColor(R.color.white));
            binding.week.setTextColor(getResources().getColor(R.color.white));

            binding.year.setBackground(getResources().getDrawable(R.drawable.btn_bg_solid));
            binding.month.setBackground(getResources().getDrawable(R.drawable.btn_bg_border));
            binding.week.setBackground(getResources().getDrawable(R.drawable.btn_bg_border));
            addData();
        });

        return binding.getRoot();
    }

    private void getData() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.HISTORY).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(dataSnapshot -> {
                    historyList.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HistoryModel model = snapshot.getValue(HistoryModel.class);
                            historyList.add(model);
                        }
                        
                        historyList.sort(Comparator.comparingLong(HistoryModel::getTimestamp));

                        Log.d("CHECKING12", "Size  " + historyList.size());

                        long thirtyDaysAgoMillis = new Date().getTime() - (30L * 24L * 60L * 60L * 1000L);
                        long sevenDaysAgoMillis = new Date().getTime() - (7L * 24L * 60L * 60L * 1000L);
                        long threeHundredSixtyFiveDaysAgoMillis = new Date().getTime() - (365L * 24L * 60L * 60L * 1000L);

                        for (HistoryModel dataObject : historyList) {
                            if (dataObject.getTimestamp() >= threeHundredSixtyFiveDaysAgoMillis) {
                                last365DaysData.add(dataObject);
                            }
                        }
                        for (HistoryModel dataObject : historyList) {
                            if (dataObject.getTimestamp() >= sevenDaysAgoMillis) {
                                last7DaysData.add(dataObject);
                            }
                        }
                        for (HistoryModel dataObject : historyList) {
                            if (dataObject.getTimestamp() >= thirtyDaysAgoMillis) {
                                last30DaysData.add(dataObject);
                            }
                        }

                        Log.d("CHECKING12", "last7DaysData Size  " + last7DaysData.size());
                        Log.d("CHECKING12", "last30DaysData Size  " + last30DaysData.size());
                        Log.d("CHECKING12", "last365DaysData Size  " + last365DaysData.size());

                        addData();
                    } else {
                        Log.d("CHECKING12", "No Data Found");
                    }
                    Constants.dismissDialog();
                });
    }

    private void addData() {
        barArraylist.clear();
        if (active == 0) {
            for (HistoryModel week : last7DaysData) {
                Log.d("CHECKING12", Constants.getFormattedDate(week.getTimestamp()));
                Log.d("CHECKING12", "CHECK  " + Constants.historyDay(week.getTimestamp()) + "  count  " + week.getCount());
                barArraylist.add(new BarEntry(Constants.historyDay(week.getTimestamp()), week.getCount()));
            }
            Log.d("CHECKING12", "barArraylist Size  " + barArraylist.size());
        }
        if (active == 1) {
            for (HistoryModel month : last30DaysData) {
                barArraylist.add(new BarEntry(Constants.historyDay(month.getTimestamp()), month.getCount()));
            }

//            for (int i = 1; i<=35; i++){
//                barArraylist.add(new BarEntry(i, (new Random().nextInt(30) + 1)));
//            }
        }
        if (active == 2) {
            for (HistoryModel year : last365DaysData) {
                barArraylist.add(new BarEntry(Constants.historyDay(year.getTimestamp()), year.getCount()));
            }
//
//            for (int i = 1; i<=365; i++){
//                barArraylist.add(new BarEntry(i, (new Random().nextInt(30) + 1)));
//            }
        }
        barChart.clear();
        initChart();
    }

    private void initChart() {
        BarDataSet barDataSet = new BarDataSet(barArraylist, requireContext().getResources().getString(R.string.routines_followed));
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        //color bar data set
        barDataSet.setColor(Stash.getInt(Constants.COLOR_TEXT, R.color.text));
        barDataSet.setFormLineWidth(5f);
        //text color
        barDataSet.setValueTextColor(Color.WHITE);
        barData.setValueTypeface(Typeface.createFromAsset(requireContext().getApplicationContext().getAssets(), "poppins.ttf"));
        //settting text size
        barDataSet.setValueTextSize(13f);
        barChart.getDescription().setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mainCard.setCardBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        if (active == 0) {
            binding.week.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (active == 1) {
            binding.month.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (active == 2) {
            binding.year.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}