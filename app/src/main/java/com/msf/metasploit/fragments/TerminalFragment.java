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
import com.msf.metasploit.adapter.TerminalPresenter;
import com.msf.metasploit.model.RpcServer;
import com.msf.metasploit.model.Terminal;

public class TerminalFragment extends Fragment implements TerminalPresenter.UpdateListener {

    private static final String ID = "id";

    public static TerminalFragment newInstance(String id, RpcServer rpcServer) {
        TerminalFragment terminalFragment = new TerminalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MsfServerList.RPC_SERVER_ID, rpcServer.uid);
        bundle.putString(ID, id);
        terminalFragment.setArguments(bundle);
        return terminalFragment;
    }

    private ScrollView scrollviewConsole;
    private TextView textviewConsole;
    private TextView textviewPrompt;
    private EditText edittextInput;

    private TerminalPresenter terminalPresenter;

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
        String consoleId = bundle.getString(ID);
        RpcServer rpcServer = Msf.get().msfServerList.fromIntent(getActivity().getIntent());

        terminalPresenter = new TerminalPresenter();
        terminalPresenter.setConsole(rpcServer.getRpc(), consoleId);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        terminalPresenter.addListener(this);
    }

    @Override
    public void onStop() {
        terminalPresenter.removeListener(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    private void updateView() {
        updateView(terminalPresenter.getTerminal());
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
        terminalPresenter.update();
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

    private void updateView(Terminal terminal) {
        textviewPrompt.setText(terminal.prompt);
        textviewConsole.setText(terminal.text);
        updateScroll();
    }

    private void writeCommand(String command) {
        terminalPresenter.sendCommand(command);
        terminalPresenter.update();
    }

}
