package com.example.aiadda.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aiadda.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WebsiteListFragment extends Fragment {
    private RecyclerView recyclerView, recyclerView1;
    private ConstraintLayout constraintLayout;
    private DatabaseReference db;
    private List<String> websiteListArray;
    private WebsiteListAdapter websiteListAdapter;
    private List<String> websiteList;

    public WebsiteListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.website_list, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String selectedChatItem = arguments.getString("selectedChatItem");
            Toast.makeText(getActivity(), "Your message hello" + selectedChatItem, Toast.LENGTH_SHORT).show();
       }
        db = FirebaseDatabase.getInstance("https://ai-adda-86d35-default-rtdb.firebaseio.com/").getReference().child("category");
        websiteListArray = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerViewWebsites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        websiteListAdapter = new WebsiteListAdapter(websiteListArray);
        recyclerView.setAdapter(websiteListAdapter);


        db.child("websites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "Web ID: ");
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    String chatItem = chatSnapshot.getValue(String.class);
                    Log.d("TAG", "Web ID: " + chatItem);
                    websiteListArray.add(chatItem);
                }
                websiteListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        return view;
    }

    public class WebsiteListAdapter extends RecyclerView.Adapter<WebsiteListAdapter.ViewHolder> {
        private List<String> chatList;

        public WebsiteListAdapter(List<String> chatList) {
            this.chatList = chatList;
        }

        @NonNull
        @Override
        public WebsiteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
            return new WebsiteListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WebsiteListAdapter.ViewHolder holder, int position) {
            String chatItem = chatList.get(position);
            holder.chatTextView.setText(chatItem);
        }

        @Override
        public int getItemCount() {
            return chatList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView chatTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                chatTextView = itemView.findViewById(R.id.textViewUsername);
            }
        }
    }
}
