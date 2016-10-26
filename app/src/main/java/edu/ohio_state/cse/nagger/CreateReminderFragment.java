package edu.ohio_state.cse.nagger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Sayam Ganguly on 10/21/2016.
 */
public class CreateReminderFragment extends Fragment {

    private Button mSendReminder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_reminder_fragment,container,false);
//        return super.onCreateView(inflater, container, savedInstanceState);
        mSendReminder = (Button) v.findViewById(R.id.button_send_reminder);

        mSendReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Reminder Sent",Toast.LENGTH_LONG);
            }
        });

        return v;
    }
}
