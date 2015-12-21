package com.msf.metasploit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.msf.metasploit.Msf;
import com.msf.metasploit.MsfServerList;
import com.msf.metasploit.R;
import com.msf.metasploit.adapter.ConsolePresenter;
import com.msf.metasploit.model.Console;
import com.msf.metasploit.model.RpcServer;

public class ConsoleFragment extends Fragment implements ConsolePresenter.UpdateListener {

    private static final String ID = "id";

    public static ConsoleFragment newInstance(String id, RpcServer rpcServer) {
        ConsoleFragment consoleFragment = new ConsoleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MsfServerList.RPC_SERVER_ID, rpcServer.uid);
        bundle.putString(ID, id);
        consoleFragment.setArguments(bundle);
        return consoleFragment;
    }

    private ScrollView scrollviewConsole;
    private TextView textviewConsole;
    private TextView textviewPrompt;
    private EditText edittextInput;

    private String consoleId;
    private RpcServer rpcServer;
    private ConsolePresenter consolePresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_console, container, false);
        scrollviewConsole = (ScrollView)view.findViewById(R.id.scrollview_console);
        textviewConsole = (TextView) view.findViewById(R.id.textview_console);
        textviewPrompt = (TextView) view.findViewById(R.id.textview_prompt);
        textviewPrompt.setText("Loading...");
        edittextInput = (EditText) view.findViewById(R.id.edittext_input);
        edittextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    String text = edittextInput.getText().toString() + "\n";
                    writeCommand(text);
                    edittextInput.setText("");
                    return true;
                }
                return false;
            }
        });

        Bundle bundle = getArguments();
        consoleId = bundle.getString(ID);
        rpcServer = Msf.get().msfServerList.fromIntent(getActivity().getIntent());

        consolePresenter = new ConsolePresenter();
        consolePresenter.rpcConnection = rpcServer.getRpc();
        consolePresenter.console = new Console();
        consolePresenter.console.id = consoleId;

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        consolePresenter.addListener(this);
    }

    @Override
    public void onStop() {
        consolePresenter.removeListener(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    private void updateView() {
        updateView(consolePresenter.console);
    }

    @Override
    public void onUpdated() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    private void updateContent() {
        consolePresenter.update();
    }

    private void updateScroll() {
         scrollviewConsole.post(new Runnable() {
            @Override
            public void run() {
                scrollviewConsole.fullScroll(ScrollView.FOCUS_DOWN);
                edittextInput.requestFocus();
            }
        });
    }

    private void updateView(Console console) {
        textviewPrompt.setText(console.prompt);
        textviewConsole.setText(console.text);
        updateScroll();
    }

    private void writeCommand(String command) {
        consolePresenter.sendCommand(command);
        consolePresenter.update();
    }

}
