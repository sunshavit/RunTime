package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class InvitationsTabFragment extends Fragment implements InvitationsTabAdapter.OnInvitationResponse, SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Event> invitations = new ArrayList<>();
    private InvitationsTabVM viewModel;
    private InvitationsTabAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel= new ViewModelProvider(getActivity()).get(InvitationsTabVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invitations_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView invitationsRecycler = view.findViewById(R.id.invitationsRecycler);
        invitationsRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext());
        invitationsRecycler.setLayoutManager(manager);
        adapter = new InvitationsTabAdapter(invitations, this.getContext());
        adapter.setInvitationCallback(this);
        invitationsRecycler.setAdapter(adapter);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshInvitationsTab);
        swipeRefreshLayout.setOnRefreshListener(this);

        /*swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);

            }
        });*/

        viewModel.getSwipeLayoutBool().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                swipeRefreshLayout.setRefreshing(true);
            }
        });


        viewModel.getInvitationsLiveData().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                invitations.clear();
                invitations.addAll(events);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }


    @Override
    public void onJoinToEvent(String userId, String eventId, int position) {

        viewModel.onJoinToEvent(userId, eventId);
    }

    @Override
    public void onRemoveEvent(String userId, String eventId, int position) {

        viewModel.onRemoveEvent(userId, eventId);
    }

    @Override
    public void onRunnersClicked(String eventId) {
        //openDialog
        FragmentManager fm = getFragmentManager();
        RunnersDialog editNameDialogFragment = RunnersDialog.newInstance(eventId);
        // SETS the target fragment for use later when sending results
        //editNameDialogFragment.setTargetFragment(MyParentFragment.this, 300);
        assert fm != null;
        editNameDialogFragment.show(fm, "fragment_runners");
    }

    @Override
    public void onRefresh() {
        //swipeRefreshLayout.setRefreshing(true);
        viewModel.getInvitationsIds();
    }
}
