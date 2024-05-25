package com.example.todo_application;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_application.Adapter.ToDoAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter toDoAdapter;

    public TouchHelper(ToDoAdapter toDoAdapter) {
        super(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        this.toDoAdapter = toDoAdapter;

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction==ItemTouchHelper.RIGHT){
            AlertDialog.Builder builder = new AlertDialog.Builder(toDoAdapter.getContext());
            builder.setMessage("Are You Sure Want to Delete ?")
                    .setTitle("Delete Task")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toDoAdapter.deleteTask(position);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toDoAdapter.notifyItemChanged(position);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
        }else {
            toDoAdapter.editTask(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightActionIcon(R.drawable.baseline_delete_24)
                .addSwipeRightBackgroundColor(Color.LTGRAY)
                .addSwipeLeftActionIcon(R.drawable.edit)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(toDoAdapter.getContext(),R.color.lightgray))
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
