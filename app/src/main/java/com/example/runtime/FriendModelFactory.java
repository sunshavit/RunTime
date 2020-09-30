package com.example.runtime;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FriendModelFactory implements ViewModelProvider.Factory {

    private String friendId;

    public FriendModelFactory(String friendId) {
        this.friendId = friendId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create( Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendDialogVM.class)) {
            return (T) new FriendDialogVM(friendId);
        }
        //noinspection unchecked
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
