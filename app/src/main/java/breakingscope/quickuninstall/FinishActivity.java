package breakingscope.quickuninstall;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import breakingscope.quickuninstall.databinding.ActivityFinishBinding;

public class FinishActivity extends AppCompatActivity {

    ActivityFinishBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finish);
        binding.setActivity(this);
    }

    // Finishes the Activity
    public void onReturnClicked() {
        finish();
    }

}
