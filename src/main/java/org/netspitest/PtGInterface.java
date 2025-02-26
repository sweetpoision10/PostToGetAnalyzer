package org.netspitest;

import org.netspitest.utils.PtGUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PtGInterface {
    private JPanel ui;
//    private JTextField hashFieldTxt;
//    private JButton saveHashBtn;
    private JLabel configDuplicateLabel;
    private JCheckBox configDuplicateCheckBox;
    private JLabel configInScopeLabel;
    private JCheckBox configInScopeCheckBox;
    private JButton buttonOnOff;


    private PostToGetHttpHandler handler;

    public PtGInterface() {
        this.configDuplicateCheckBox.setSelected(true);
        this.configInScopeCheckBox.setSelected(true);


        this.buttonOnOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               //toggle button Start and Stop.
                //if tool is running, then clicking should result in Stopping
                if(PtGUtils.isRunning()){
                    PtGUtils.setRunning(false);
                    buttonOnOff.setText("Stopped");
                }
                else {
                    //if tool is not running, then clicking should result in Running
                    PtGUtils.setRunning(true);
                    buttonOnOff.setText("Running");
                }
        }
        ;
//        saveHashBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                handler.setHash(hashFieldTxt.getText());
//            }
        });

        this.configInScopeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (configInScopeCheckBox.isSelected()){
                    PtGUtils.setOnlyInScopeCheckBox(true);
                }
                else{
                    PtGUtils.setOnlyInScopeCheckBox(false);
                }
            }
        });

        this.configDuplicateCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (configDuplicateCheckBox.isSelected()){
                    PtGUtils.setDuplicateURLCheckBox(true);
                }
                else{
                    PtGUtils.setDuplicateURLCheckBox(false);
                }
            }
        });
    }

    public JPanel getUI() {
        return this.ui;
    }

//    public void setHashFieldTxt(String hash) {
//        this.hashFieldTxt.setText(hash);
//    }

    public void setHTTPHandler(PostToGetHttpHandler handler) {
        this.handler = handler;
    }
}
