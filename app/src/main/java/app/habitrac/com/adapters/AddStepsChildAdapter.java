package app.habitrac.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.habitrac.com.R;
import app.habitrac.com.listners.StepClickListner;
import app.habitrac.com.models.AddStepsChildModel;

import java.util.ArrayList;

public class AddStepsChildAdapter extends RecyclerView.Adapter<AddStepsChildAdapter.ChildVH> {
    Context context;
    ArrayList<AddStepsChildModel> list;
    StepClickListner listner;
    private int selectedPosition = -1;
    public AddStepsChildAdapter(Context context, ArrayList<AddStepsChildModel> list, StepClickListner listner) {
        this.context = context;
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ChildVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildVH(LayoutInflater.from(context).inflate(R.layout.add_steps_child, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildVH holder, int position) {
        AddStepsChildModel model = list.get(holder.getAdapterPosition());
        holder.time.setText(model.getTime());
        holder.title.setText(model.getName());
        holder.radioButton.setChecked(selectedPosition == holder.getAdapterPosition());
        holder.itemView.setOnClickListener(v -> {



            if (listner != null){
                listner.onClick(list.get(holder.getAdapterPosition()));
            }
        });



        // Find RadioButton and EditText (ensure IDs match your layout)
        RadioButton radioButton = holder.itemView.findViewById(R.id.radio_button);
        EditText editText = holder.itemView.findViewById(R.id.otherq);

        // Handle "Other" option selection
        if (model.getName().equals(context.getString(R.string.other))) {
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editText.setVisibility(isChecked ? View.VISIBLE : View.GONE); // Concise visibility toggle
                    editText.setEnabled(isChecked);
                    if (isChecked) {
                        // Pre-fill EditText with current time (optional)
                        editText.setText(model.getTime()); // Adjust based on your data structure
                    } else {
                        editText.setText("");
                    }
                }
            });
        } else { // For other options
            editText.setVisibility(View.GONE);
            editText.setEnabled(false);
            editText.setText("");
        }
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //  selectedPosition = position;
                    //   notifyItemRangeChanged(0, list.size());
                    listner.onClick(list.get(holder.getAdapterPosition()));// Efficiently update all items
                    if (model.getName().equals(context.getString(R.string.other))) {
                        editText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        // Update selected position for click listener (optional, if needed)
        holder.radioButton.setChecked(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChildVH extends RecyclerView.ViewHolder{
        TextView title, time;
        RadioButton radioButton;
        public ChildVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            radioButton = itemView.findViewById(R.id.radio_button);
            EditText editText = itemView.findViewById(R.id.otherq);
        }
    }

}
