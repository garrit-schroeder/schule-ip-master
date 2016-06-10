package front.components;

import front.Interface_controller;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class MainFrame extends JFrame {

    /**
     * created by luca on 04/12/14
     */

    private final String title = "Network-Address Calculator";
    private final int width = 800;
    private final int height = 600;

    private final JTabbedPane tabbed_pane;

    public MainFrame(Interface_controller interface_controller) {
        // initialize Frame
        this.setBounds(100, 30, width, height);
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setVisible(true);

        // create main panel
        JPanel panel_main = new JPanel();
        panel_main.setSize(width, height);
        panel_main.setVisible(true);
        this.add(panel_main);

        // GridLayout for main panel
        panel_main.setLayout(new GridLayout(1, 1));

        // create panels for networks, subnets and hosts
        final TabbedPanelHosts panel_hosts = new TabbedPanelHosts(width, height, interface_controller);
        final TabbedPanelSubnets panel_subnets = new TabbedPanelSubnets(width, height, interface_controller, panel_hosts);
        final TabbedPanelNetworks panel_networks = new TabbedPanelNetworks(width, height, interface_controller,
                panel_subnets, panel_hosts);

        // create tabbed pane
        tabbed_pane = new JTabbedPane(JTabbedPane.TOP);
        tabbed_pane.setSize(width, height);
        tabbed_pane.setVisible(true);
        panel_main.add(tabbed_pane);

        // add panels to tabbed pane
        tabbed_pane.addTab("Networks", panel_networks);
        tabbed_pane.addTab("Subnets", panel_subnets);
        tabbed_pane.addTab("Hosts", panel_hosts);

        // LISTENER for changed tab
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                switch (index) {
                    //case 0:panel_networks.refreshIndex();
                    case 1:
                        if (!panel_networks.isSelectionEmpty())
                            panel_subnets.refreshIndex();
                        break;
                    case 2:
                        if (!panel_networks.isSelectionEmpty())
                            panel_hosts.refreshIndex();
                        break;
                }
            }
        };
        tabbed_pane.addChangeListener(changeListener);
    }

    public void changeSubnetsTabIp(String ip) {
        tabbed_pane.setTitleAt(1, "Subnets " + ip);
    }


}
