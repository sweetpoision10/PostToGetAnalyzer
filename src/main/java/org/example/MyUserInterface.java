package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyUserInterface {
    private JPanel ui;
    private JTextField hashFieldTxt;
    private JLabel hashLabel;
    private JLabel testLabel;
    private JButton saveHashBtn;
    private MyFIrstHTTPHandler handler;

    public MyUserInterface() {
        saveHashBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.setHash(hashFieldTxt.getText());
            }
        });
    }

    public JPanel getUI() {
        return this.ui;
    }

    public void setHashFieldTxt(String hash) {
        this.hashFieldTxt.setText(hash);
    }

    public void setHTTPHandler(MyFIrstHTTPHandler handler) {
        this.handler = handler;
    }
}
