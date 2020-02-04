package com.itea.practice.fundamentals.task.data.source;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.data.source.asssets.tread.HandlerThreadAssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.loader.LoaderAssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.tread.ThreadAssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.task.AsyncTaskAssetsFragment;
import com.itea.practice.fundamentals.task.data.source.prefs.PrefsFragment;
import com.itea.practice.fundamentals.task.data.source.sqlite.SQLiteFragment;
import com.itea.practice.fundamentals.task.data.source.web.WebImageFragment;

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
                navigate(new SQLiteFragment(), true);
                break;
            case 2:
                navigate(new ThreadAssetsFragment(), true);
                break;
            case 3:
                navigate(new HandlerThreadAssetsFragment(), true);
                break;
            case 4:
                navigate(new AsyncTaskAssetsFragment(), true);
                break;
            case 5:
                navigate(new LoaderAssetsFragment(), true);
            case 6:
                navigate(new WebImageFragment(), true);
        }
    }

}
