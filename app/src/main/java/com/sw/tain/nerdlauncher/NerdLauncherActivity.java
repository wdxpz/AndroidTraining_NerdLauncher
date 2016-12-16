package com.sw.tain.nerdlauncher;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment CreateFragment() {
        Fragment f = NerdLauncherFragment.newInstance();
        return f;
    }

}
