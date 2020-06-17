    package com.example.finaltodoapp;
    import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Query;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.finaltodoapp.model.ETodo;
import com.example.finaltodoapp.viewmodel.TodoViewModel;
import java.text.SimpleDateFormat;
import java.util.List;




    public class TodoListFragment extends Fragment {
    View rootView;
    RecyclerView todoRecyclerView;
    TodoViewModel mTodoViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_todo_list, container, false);
        mTodoViewModel= ViewModelProviders.of(this).get(TodoViewModel.class);
        todoRecyclerView=rootView.findViewById(R.id.todo_recycler_view);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateRV();
        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        List<ETodo> todoList= mTodoViewModel.getAllTodos().getValue();
                        TodoAdaptor adaptor= new TodoAdaptor(todoList);
                        ETodo todo = adaptor.getTododAt(viewHolder.getAdapterPosition());
                        mTodoViewModel.deleteById(todo);
                    }
                }).attachToRecyclerView(todoRecyclerView);
        return rootView;
    }
    void updateRV()
    {
        mTodoViewModel.getAllTodos().observe(this, new Observer<List<ETodo>>() {
            @Override
            public void onChanged(List<ETodo> todoList) {
                TodoAdaptor adaptor= new TodoAdaptor(todoList);
                todoRecyclerView.setAdapter(adaptor);
            }
        });
    }
    private class TodoHolder extends RecyclerView.ViewHolder
    {
        TextView mTitle,mDate;
        public TodoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_todo,parent,false));
            mTitle=itemView.findViewById(R.id.list_item_tv_Name);
            mDate=itemView.findViewById(R.id.list_item_tv_Date);
            mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TodoAdaptor adaptor= new TodoAdaptor(mTodoViewModel.getAllTodos().getValue());
                    int position = getAdapterPosition();
                    ETodo eTodo=adaptor.getTododAt(position);
                    Intent intent= new Intent(getActivity(),EditTodoActivity.class);
                    intent.putExtra("TodoId",eTodo.getId());
                    startActivity(intent);
                }
            });
        }
        public void bind(ETodo todo)
        {
            SimpleDateFormat dateFormater= new SimpleDateFormat("yyyy-MM-dd");
            mTitle.setText(todo.getTitle());
            mDate.setText(dateFormater.format(todo.getTodo_date()));
        }
    }
    private class TodoAdaptor extends RecyclerView.Adapter<TodoHolder>
    {
        List<ETodo> mETodoList;
        public TodoAdaptor(List<ETodo> todoList)
        {
            mETodoList=todoList;
        }
        @NonNull
        @Override
        public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater= LayoutInflater.from(getActivity());
            return new TodoHolder(layoutInflater,parent);
        }
        @Override
        public void onBindViewHolder(@NonNull TodoHolder holder, int position) {
            ETodo todo = mETodoList.get(position);
            LinearLayout layout = (LinearLayout) ((ViewGroup) holder.mTitle.getParent());
            switch (todo.getPriority()) {
                case 1:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_high_priority));
                    break;
                case 2:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_medium_priority));
                    break;
                case 3:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_low_priority));
                    break;
            }

            holder.bind(todo);
        }

        @Query("DELETE FROM todo_table WHERE is_completed='1'")

    @Override
    public int getItemCount()
        {
            return mETodoList.size();
        }

     public ETodo getTododAt(int index)
     {
         return mETodoList.get(index);
     }

        }
    }












