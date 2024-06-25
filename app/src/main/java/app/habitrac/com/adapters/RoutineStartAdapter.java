package app.habitrac.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.checkbox.MaterialCheckBox;
import app.habitrac.com.R;
import app.habitrac.com.models.StepsLocalModel;

import java.util.ArrayList;

public class RoutineStartAdapter extends RecyclerView.Adapter<RoutineStartAdapter.RoutineVH> {

    Context context;
    ArrayList<StepsLocalModel> list;

    public RoutineStartAdapter(Context context, ArrayList<StepsLocalModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RoutineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutineVH(LayoutInflater.from(context).inflate(R.layout.steps_done, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineVH holder, int position) {
        StepsLocalModel model = list.get(holder.getAdapterPosition());
        holder.time.setText(model.getTime());
        holder.title.setText(model.getName());

        holder.switchDone.setChecked(model.isCompleted());

        holder.switchDone.setOnCheckedChangeListener((compoundButton, b) -> {
            list.get(holder.getAdapterPosition()).setCompleted(b);
            Stash.put(model.getID(), list);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RoutineVH extends RecyclerView.ViewHolder{
        TextView title, time;
        MaterialCheckBox switchDone;
        public RoutineVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            switchDone = itemView.findViewById(R.id.switchDone);
        }
    }

}
