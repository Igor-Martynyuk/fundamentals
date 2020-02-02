package com.itea.practice.fundamentals.start.menu;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.itea.practice.fundamentals.task.components.ui.dashboard.PingDashboardActivity;
import com.itea.practice.fundamentals.task.data.source.SourcesActivity;

public final class MenuListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Context context = view.getContext();

        switch (((ViewGroup) view.getParent()).indexOfChild(view)) {
            case 0:
                context.startActivity(new Intent(context, PingDashboardActivity.class));
                break;
            case 1:
                context.startActivity(new Intent(context, SourcesActivity.class));
                break;
        }
    }

}
