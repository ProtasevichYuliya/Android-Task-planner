package com.example.taskplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.taskplanner.databinding.TaskListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskListViewAdapter extends BaseAdapter {
    private List<TaskListItemViewModel> tasks;

    public TaskListViewAdapter(List<TaskListItemViewModel> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getTask().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TaskListItemViewModel taskViewModel = tasks.get(position);
        TaskListItemBinding binding;
        View resultView = convertView;
        if (resultView == null) {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            binding = TaskListItemBinding.inflate(inflater, parent, false);
            resultView = binding.getRoot();
            resultView.setTag(binding);
        } else {
            binding = (TaskListItemBinding)resultView.getTag();
        }

        resultView.setClickable(true);
        resultView.setOnClickListener((v) -> taskViewModel.openTask());

        binding.setViewModel(taskViewModel);
        return resultView;
    }
}
