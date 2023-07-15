package com.example.aiadda.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aiadda.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WebsiteListFragment extends Fragment {
    private DatabaseReference mDatabase;
    private List<String> websiteList;
    private RecyclerView recyclerView;
    private WebsiteListAdapter websiteListAdapter;
    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString("category");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mDatabase = FirebaseDatabase.getInstance("https://ai-adda-86d35-default-rtdb.firebaseio.com/").getReference().child("category");
        Log.d("TAG", "Web: " + mDatabase);
        websiteList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        websiteListAdapter = new WebsiteListAdapter(websiteList);
        recyclerView.setAdapter(websiteListAdapter);
        Log.d("TAG", "Web: A" );

        mDatabase.child("websites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "Web ID: ");
                for (DataSnapshot websiteSnapshot : dataSnapshot.getChildren()) {
                    String websiteItem = websiteSnapshot.getValue(String.class);
                    Log.d("TAG", "Web ID: " + websiteItem);
                    websiteList.add(websiteItem);
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
        private List<String> websiteList;

        public WebsiteListAdapter(List<String> websiteList) {
            this.websiteList = websiteList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.websites_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String websiteItem = websiteList.get(position);
            holder.websiteTextView.setText(websiteItem);
        }

        @Override
        public int getItemCount() {
            return websiteList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView websiteTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                websiteTextView = itemView.findViewById(R.id.textViewWebsite);
            }
        }
    }
}
