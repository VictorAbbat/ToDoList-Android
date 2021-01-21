package ca.victor.mytodolist.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import ca.victor.mytodolist.R;
import ca.victor.mytodolist.models.Tasks;

public class RowAdapter extends ArrayAdapter<Tasks> {

    public RowAdapter(Context context, List<Tasks> tasksList) {
        super(context, 0, tasksList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_list,parent, false);
        }

        TaskViewHolder viewHolder = (TaskViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TaskViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.taskName);
            viewHolder.description = (TextView) convertView.findViewById(R.id.taskDesc);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        Tasks tasks = getItem(position);

        if(tasks.getImportant().equals("y")) {
            viewHolder.description.setTextColor(Color.parseColor("#FF0000"));
        }

        if(tasks.getImportant().equals("n")) {
            viewHolder.description.setTextColor(Color.parseColor("#000000"));
        }
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        viewHolder.title.setText(tasks.getName());
        viewHolder.description.setText(tasks.getDescription());
        //viewHolder.avatar.setImageDrawable(new ColorDrawable(Color.parseColor("#3455FF")));
        viewHolder.avatar.setImageDrawable(new ColorDrawable(color));

        return convertView;
    }

    private class TaskViewHolder {
        public TextView title;
        public TextView description;
        public ImageView avatar;
    }

}

