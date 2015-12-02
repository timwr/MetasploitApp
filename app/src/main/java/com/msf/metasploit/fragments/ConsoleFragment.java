package com.msf.metasploit.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.msf.metasploit.R;
import com.msf.metasploit.rpc.MsfController;
import com.msf.metasploit.rpc.MsgRpcImpl;
import com.msf.metasploit.rpc.RpcConstants;

import java.util.HashMap;

public class ConsoleFragment extends Fragment {

    private static final String ID = "id";

    private String consoleId;

    public static ConsoleFragment newInstance(String id) {
        ConsoleFragment consoleFragment = new ConsoleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ID, id);
        consoleFragment.setArguments(bundle);
        return consoleFragment;
    }

    private ScrollView scrollviewConsole;
    private TextView textviewConsole;
    private TextView textviewPrompt;
    private EditText edittextInput;

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
                if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))){
                    String text = edittextInput.getText().toString() + "\n";
                    writeCommand(text);
                    edittextInput.setText("");
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private void updateContent() {
        AsyncTask<Void, Void, HashMap<String, String>> updateTask = new AsyncTask<Void, Void, HashMap<String, String>>() {

            @Override
            protected HashMap<String, String> doInBackground(Void... params) {
                try {
                    MsfController msfController = MsfController.getInstance();
                    MsgRpcImpl msgRpcImpl = msfController.getMsgRpcImpl();

                    if (consoleId == null) {
                        Object console = msgRpcImpl.execute(RpcConstants.CONSOLE_CREATE);
                        HashMap<String, String> consoleInfo = (HashMap<String, String>)console;
                        consoleId = consoleInfo.get("id");
                    }

                    HashMap<String, String> consoleMap;
                    consoleMap = (HashMap<String, String>) msgRpcImpl.execute(RpcConstants.CONSOLE_READ, new Object[] { consoleId });
                    return consoleMap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String,String> result) {
                Log.e("e", "onPostExecute" + result);
                if (result != null) {
                    updateView(result);
                }
            };

        };
        updateTask.execute();
    }

    private void updateView(HashMap<String, String> consoleObject) {
        textviewPrompt.setText(consoleObject.get("prompt"));
        String data = consoleObject.get("data");
        if (data != null) {
            textviewConsole.append(data);
//            scrollviewConsole.post(new Runnable() {
//                public void run() {
//                    scrollviewConsole.scrollTo(0, scrollviewConsole.getBottom());
//                }
//            });
            scrollviewConsole.post(new Runnable() {
                @Override
                public void run() {
                    scrollviewConsole.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    private void writeCommand(final String command) {
        AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MsfController msfController = MsfController.getInstance();
                    MsgRpcImpl msgRpcImpl = msfController.getMsgRpcImpl();

                    if (consoleId != null) {
                        Object result = msgRpcImpl.execute(RpcConstants.CONSOLE_WRITE, new Object[] { consoleId, command });
                        updateContent();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        updateTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateContent();
    }

}
