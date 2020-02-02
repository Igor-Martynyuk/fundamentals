package com.itea.practice.fundamentals.task.data.source;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.data.source.asssets.HandlerThreadAssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.ThreadAssetsFragment;
import com.itea.practice.fundamentals.task.data.source.prefs.PrefsFragment;

public class SourcesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sources);
        navigate(new SourcesFragment(), false);
    }

    private void navigate(Fragment fragment, boolean shouldAddToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (shouldAddToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        transaction.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                navigate(new PrefsFragment(), true);
                break;
            case 1:
                navigate(new ThreadAssetsFragment(), true);
                break;
            case 2:
                navigate(new HandlerThreadAssetsFragment(), true);
                break;

            case 3:
                break;
        }
    }

}
