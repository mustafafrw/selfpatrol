package com.example.mustafa.patrolguard;

import android.app.DialogFragment;
import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class NFCReadFragment extends DialogFragment {

    public static final String TAG = NFCReadFragment.class.getSimpleName();

    public static NFCReadFragment newInstance() {

        return new NFCReadFragment();
    }

    private TextView mTvMessage;
    private Listener mListener;
    private Button vr;
    private Button yk;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read,container,false);
        initViews(view);
        vr.setVisibility(View.INVISIBLE);
        yk.setVisibility(View.INVISIBLE);
        return view;
    }
    public void varClick(View view)
    {
        mTvMessage.setText("vara tıkladın");
    }
    public void yokClick(View view)
    {
        mTvMessage.setText("yoka tıkladın");
    }
    public void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
        vr = (Button) view.findViewById(R.id.var);
        vr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvMessage.setText("Vara tıkladın");
            }
        });
        yk= (Button) view.findViewById(R.id.yok);
        yk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvMessage.setText("Yoka tıkladın");

            }
        });
        vr.setVisibility(View.INVISIBLE);
        yk.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Main2Activity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef){

        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            int checkPointId = Integer.parseInt(message);
            if(checkPointId == Main2Activity.currentPoint.getId()){
                mTvMessage.setText("Bir sorun var mı?");
                vr.setVisibility(View.VISIBLE);
                yk.setVisibility(View.VISIBLE);
            }
            else
                mTvMessage.setText("Yanlış Kontrol Noktası!");
            Log.d(TAG, "readFromNFC: "+message+", checkPointId: " + checkPointId);

            ndef.close();

        } catch (IOException | FormatException e) {
            System.out.println("ndefx = [" + e.getMessage() + "]");

        }
    }
}
