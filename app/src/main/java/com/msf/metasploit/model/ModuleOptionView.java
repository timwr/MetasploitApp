
package com.msf.metasploit.model;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ModuleOptionView extends LinearLayout {

    private final ModuleOption option;
    private LinearLayout layoutContainer;
    private TextView textviewName;
    private TextView textviewDescription;

    public ModuleOptionView(Context context, ModuleOption option) {
        super(context);
        this.option = option;
//        View view = View.inflate(context, R.layout.view_module_option, this);
        initialise();
        updateView();
    }

    private void initialise() {
//        layoutContainer = (LinearLayout) findViewById(R.id.layout_container);
//        textviewName = (TextView) findViewById(R.id.textview_name);
//        textviewDescription = (TextView) findViewById(R.id.textview_description);
    }

    private void updateView() {
        textviewName.setText(option.name);
        textviewDescription.setText(option.desc);
        layoutContainer.setVisibility(View.VISIBLE);
    }
}
