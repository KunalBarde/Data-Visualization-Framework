package edu.cmu.cs.cs214.hw5.framework.gui;

import edu.cmu.cs.cs214.hw5.framework.core.Framework;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;

public class FrameworkGui extends JFrame {
    private static final String TITLE = "County Framework";

    //GUI Components
    private DataPanel dataPanel;
    private DispPanel dispPanel;
    private JTextArea msgBoard;
    private JButton dispRun;

    //Proprietary Fields
    private Framework fmWork;

    public FrameworkGui() {
        super(TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Set up edu.cmu.cs.cs214.hw5.framework.core.DataPlugin
        this.fmWork = new Framework();

        //Set up GUI comp
        JPanel container = new JPanel();
        this.dataPanel = new DataPanel();
        this.dispPanel = new DispPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(this.dataPanel);
        container.add(this.dispPanel);

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.msgBoard = new JTextArea();
        this.msgBoard.setEditable(false);
        this.add(container);
        this.add(this.msgBoard);
        this.pack();
        this.setResizable(false);
    }

    /**
     * Contains drop-down of data plugins and
     * the 'run' button to run that data plugin
     */
    private class DataPanel extends JPanel{
        JComboBox dataCombo;
        JButton dataRun;
        int selectedIndex = 0;

        public DataPanel() {
            String title = "Data Plugins";
            Border border = BorderFactory.createTitledBorder(title);
            this.setBorder(border);

            List<String> d = fmWork.getDataPlugins();
            if(d == null){
                return;
            }
            String[] strings = d.toArray(new String[d.size()]);
            dataCombo = new JComboBox<>(strings);
            dataCombo.addActionListener(
                    (event) -> {
                        JComboBox<String> cb = (JComboBox) event.getSource();
                        selectedIndex = cb.getSelectedIndex();
                    }
            );

            dataRun = new JButton("Run");
            dataRun.addActionListener(
                    (event) -> {
                        if(selectedIndex != -1) {
                            JFrame inputDialog = new JFrame();
                            String source = JOptionPane.showInputDialog(inputDialog, "Enter Source Path:", null);
                            if (fmWork.runDataPlugin(selectedIndex, source)) {
                                msgBoard.setText("Data Load Successful");
                                dispRun.setEnabled(true);
                            } else {
                                msgBoard.setText("Data Load Failed");
                                dispRun.setEnabled(false);
                            }
                        }
                    }
            );

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(dataCombo);
            this.add(dataRun);
        }
    }

    /**
     * Contains drop-down of display plugins and
     * the 'run' button to run that display plugin
     */
    private class DispPanel extends JPanel{
        JComboBox dispCombo;
        int selectedIndex = -1;

        public DispPanel() {
            String title = "Display Plugins";
            Border border = BorderFactory.createTitledBorder(title);
            this.setBorder(border);
            List<String> d = fmWork.getDisplayPlugins();
            if(d == null){
                return;
            }
            String[] strings = d.toArray(new String[d.size()]);
            dispCombo = new JComboBox<>(strings);
            dispCombo.addActionListener(
                    (event) -> {
                        JComboBox<String> cb = (JComboBox) event.getSource();
                        selectedIndex = cb.getSelectedIndex();
                    }
            );
            dispRun = new JButton("Run");
            dispRun.setEnabled(false);
            dispRun.addActionListener(
                    (event) -> {
                        if(selectedIndex != -1){
                            JPanel panel = fmWork.runDisplayPlugin(selectedIndex);
                            JFrame frame = new JFrame();
                            frame.add(panel);
                            frame.pack();
                            frame.setVisible(true);
                        }
                    }
            );

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(dispCombo);
            this.add(dispRun);
        }
    }
}
