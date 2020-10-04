package com.example.runtime;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ManagedTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ManagedTabAdapter.ManagedListener {

    private ArrayList<Event> managed = new ArrayList<>();
    private ManagedTabVM viewModel;
    private ManagedTabAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ManagedTabVM.class);
        //viewModel= new ViewModelProvider(getActivity()).get(ManagedTabVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("managed", "inside onCreateView");
        return inflater.inflate(R.layout.managed_tab_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView managedRecycler = view.findViewById(R.id.managedRecycler);
        managedRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext());
        managedRecycler.setLayoutManager(manager);
        adapter = new ManagedTabAdapter(managed, this.getContext());
        adapter.setManagedCallback(this);
        managedRecycler.setAdapter(adapter);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshManagedTab);
        swipeRefreshLayout.setOnRefreshListener(this);

       /* swipeRefreshLayout.post(new Runnable() {

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

        viewModel.getManagedEventsLiveData().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                managed.clear();
                managed.addAll(events);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onRefresh() {
        //swipeRefreshLayout.setRefreshing(true);
        viewModel.getManagedEventsIds();
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
    public void onCancelButtonClicked(final Event event) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        AlertDialog eventDeleteDialog = builder.setMessage(R.string.is_sure_cancel_event)
                .setPositiveButton(R.string.cancel_event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.onEventCancel(event);
                        Snackbar.make(swipeRefreshLayout, R.string.event_canceled_successfuly, Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.do_not_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false).show();
        eventDeleteDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#f56a45"));
        eventDeleteDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#4292ac"));

    }


}
