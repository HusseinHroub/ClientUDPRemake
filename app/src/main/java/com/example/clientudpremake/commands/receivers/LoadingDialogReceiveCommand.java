package com.example.clientudpremake.commands.receivers;

import android.app.Activity;
import android.view.View;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoadingDialogReceiveCommand implements Command {
    private final Command command;

    @Override
    public void apply(Activity activity) {
        activity.findViewById(R.id.loading_dialog).setVisibility(View.GONE);
        command.apply(activity);
    }

}
