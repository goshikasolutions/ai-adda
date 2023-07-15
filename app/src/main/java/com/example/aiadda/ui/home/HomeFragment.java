package com.example.aiadda.ui.home;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aiadda.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private DatabaseReference mDatabase;
    private List<String> chatList;
    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;
    private ImageView iconImageView;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mDatabase = FirebaseDatabase.getInstance("https://ai-adda-86d35-default-rtdb.firebaseio.com/").getReference().child("category");
        Log.d("TAG", "Storage IDed: " + mDatabase);
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("chats");
        String chatItem = "Hello, World!";
        mDatabase1.push().setValue(chatItem);
        Log.d("TAG", "Storagesd IDed: ");

        chatList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatListAdapter = new ChatListAdapter(chatList);
        recyclerView.setAdapter(chatListAdapter);
        Log.d("TAG", "Storagesdsdf IDed: ");

        mDatabase.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "Document ID: ");
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    String chatItem = chatSnapshot.getValue(String.class);
                    Log.d("TAG", "Document ID: " + chatItem);
                    chatList.add(chatItem);
                }
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        return view;
    }
    public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
        private List<String> chatList;

        public ChatListAdapter(List<String> chatList) {
            this.chatList = chatList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String chatItem = chatList.get(position);
            holder.chatTextView.setText(chatItem);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment websiteListFragment = new WebsiteListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("category", chatItem);
                    websiteListFragment.setArguments(bundle);

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_home, websiteListFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });



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
