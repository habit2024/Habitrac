package app.habitrac.com.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;

import app.habitrac.com.HabitMapper;
import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.activities.RoutineStartActivity;
import app.habitrac.com.models.RoutineModel;
import app.habitrac.com.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineVH> implements Filterable {
    Context context;
    Activity activity;
    ArrayList<RoutineModel> list;
    ArrayList<RoutineModel> listAll;
    private HabitMapper habitMapper;

    public RoutineAdapter(Context context, Activity activity, ArrayList<RoutineModel> list,HabitMapper habitMapper) {
        this.context = context;
        this.activity = activity;
        this.list = list;
        listAll = new ArrayList<>(list);
        this.habitMapper = habitMapper;
    }

    @NonNull
    @Override
    public RoutineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutineVH(LayoutInflater.from(context).inflate(R.layout.routine_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineVH holder, int position) {
        RoutineModel model = list.get(holder.getAdapterPosition());
        String s = model.getSteps().size() > 1 ? " steps" : " step";
        holder.totalStep.setText(model.getSteps().size() + s);
        holder.totalStep.setTextColor(Stash.getInt(Constants.COLOR, context.getResources().getColor(R.color.light)));
        String habitMessage = habitMapper.getHabitMessage(model.getName());
        Log.d("HabitAdapter", "Item name: " + model.getName() + ", Habit message: " + habitMessage);
        holder.name.setText(habitMessage);

       // holder.name.setText(habitMapper.getHabitMessage(model.getName()));
       // holder.name.setText(model.getName());
        int min = 0;
        List<Integer> timeValues = Constants.extractTimeValues(model.getSteps());
        for (int value : timeValues) {
            min += value;
        }
        String formattedTime = Constants.convertMinutesToHHMM(min) + "h";
        holder.totalTime.setText(formattedTime);
        holder.totalTime.setTextColor(Stash.getInt(Constants.COLOR, context.getResources().getColor(R.color.light)));
        String steps = "";
        for (int i = 0; i < model.getSteps().size(); i++) {
            String st = model.getSteps().get(i).getName();
            steps += (i+1) + ". " + st + "\n";
            if (i == 2){
                steps += "...";
                break;
            }
        }

        if (model.getDaysCompleted().isMonday()){
            holder.view1.setBackgroundResource(R.drawable.active_bg);
        }
        if (model.getDaysCompleted().isTuesday()){
            holder.view2.setBackgroundResource(R.drawable.active_bg);
        }
        if (model.getDaysCompleted().isWednesday()){
            holder.view3.setBackgroundResource(R.drawable.active_bg);
        }
        if (model.getDaysCompleted().isThursday()){
            holder.view4.setBackgroundResource(R.drawable.active_bg);
        }
        if (model.getDaysCompleted().isFriday()){
            holder.view5.setBackgroundResource(R.drawable.active_bg);
        }
        if (model.getDaysCompleted().isSaturday()){
            holder.view6.setBackgroundResource(R.drawable.active_bg);
        }
        if (model.getDaysCompleted().isSunday()){
            holder.view7.setBackgroundResource(R.drawable.active_bg);
        }

        if (model.getDaysCompleted().isMonday() && model.getDaysCompleted().isTuesday()){
            holder.path1.setBackgroundResource(R.drawable.path_active);
        }

        if (model.getDaysCompleted().isTuesday() && model.getDaysCompleted().isWednesday()){
            holder.path2.setBackgroundResource(R.drawable.path_active);
        }

        if (model.getDaysCompleted().isWednesday() && model.getDaysCompleted().isThursday()){
            holder.path3.setBackgroundResource(R.drawable.path_active);
        }
        if (model.getDaysCompleted().isThursday() && model.getDaysCompleted().isFriday()){
            holder.path4.setBackgroundResource(R.drawable.path_active);
        }
        if (model.getDaysCompleted().isFriday() && model.getDaysCompleted().isSaturday()){
            holder.path5.setBackgroundResource(R.drawable.path_active);
        }

        if (model.getDaysCompleted().isSaturday() && model.getDaysCompleted().isSunday()){
            holder.path6.setBackgroundResource(R.drawable.path_active);
        }

        holder.steps.setText(steps);


        holder.itemView.setOnClickListener(v -> {
            Stash.put(Constants.MODEL, model);
            context.startActivity(new Intent(context, RoutineStartActivity.class));
            activity.finish();
        });

        holder.routine_card.setStrokeColor(Stash.getInt(Constants.COLOR_TEXT, context.getResources().getColor(R.color.text)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<RoutineModel> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterList.addAll(listAll);
            } else {
                for (RoutineModel listModel : listAll) {
                    for (String days : listModel.getDays()){
                        if (
                                days.equalsIgnoreCase(charSequence.toString())
                        ) {
                            filterList.add(listModel);
                        }
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends RoutineModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class RoutineVH extends RecyclerView.ViewHolder {
        TextView totalTime, totalStep, name, steps;
        View view1, view2, view3, view4, view5, view6, view7;
        View path1, path2, path3, path4, path5, path6;
        MaterialCardView routine_card;

        public RoutineVH(@NonNull View itemView) {
            super(itemView);

            totalTime = itemView.findViewById(R.id.totalTime);
            totalStep = itemView.findViewById(R.id.totalStep);
            name = itemView.findViewById(R.id.name);
            steps = itemView.findViewById(R.id.steps);
            routine_card = itemView.findViewById(R.id.routine_card);

            view1 = itemView.findViewById(R.id.view1);
            view2 = itemView.findViewById(R.id.view2);
            view3 = itemView.findViewById(R.id.view3);
            view4 = itemView.findViewById(R.id.view4);
            view5 = itemView.findViewById(R.id.view5);
            view6 = itemView.findViewById(R.id.view6);
            view7 = itemView.findViewById(R.id.view7);

            path1 = itemView.findViewById(R.id.path1);
            path2 = itemView.findViewById(R.id.path2);
            path3 = itemView.findViewById(R.id.path3);
            path4 = itemView.findViewById(R.id.path4);
            path5 = itemView.findViewById(R.id.path5);
            path6 = itemView.findViewById(R.id.path6);

        }
    }

}
