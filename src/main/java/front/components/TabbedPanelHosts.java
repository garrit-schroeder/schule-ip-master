package front.components;

import back.componentes.IPv4Address;
import front.Interface_controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.regex.Pattern;

public class TabbedPanelHosts extends TabbedPanel {

    private final Interface_controller i_control;

    private final int panelwidth_label = 145;
    private final int panelwidth_text = 635;
    private final int panelheight_subnetaddress = 40;
    private final int panelheight_broadcastaddress = 40;
    private final int panelheight_hosts = 430;

    // JPanel
    private final JPanel panel_buttons = new JPanel();

    private final JPanel panel_subnetaddress = new JPanel();
    private final JPanel panel_subnetaddress_label = new JPanel();
    private final JPanel panel_subnetaddress_text = new JPanel();

    private final JPanel panel_broadcastaddress = new JPanel();
    private final JPanel panel_broadcastaddress_label = new JPanel();
    private final JPanel panel_broadcastaddress_text = new JPanel();

    private final JPanel panel_hosts = new JPanel();
    private final JPanel panel_hosts_label = new JPanel();
    private final JPanel panel_hosts_text = new JPanel();

    // JLabel
    private final JLabel label_subnetaddress = new JLabel("Subnetaddress");
    private final JLabel label_broadcastaddress = new JLabel("Broadcastaddress");
    private final JLabel label_hosts = new JLabel("Hosts");

    // JList
    private final JList list_subnetaddress = new JList();
    private final JList list_broadcastaddress = new JList();
    private final JList list_hosts = new JList();
    private final JScrollPane scroll_hosts = new JScrollPane();

    // JButton
    private final Btn button_new_host = new Btn();
    private final Btn button_delete_host = new Btn();

    // JList Modells
    private final DefaultListModel modell_subnetaddress = new DefaultListModel();
    private final DefaultListModel modell_broadcastaddress = new DefaultListModel();
    private final DefaultListModel modell_hosts = new DefaultListModel();

    // Binär Felder

    public TabbedPanelHosts(int w, int h,
                            Interface_controller interface_controller) {
        super(w, h);
        init(w, h);
        initListener();
        this.i_control = interface_controller;
        list_subnetaddress.setModel(modell_subnetaddress);
        list_broadcastaddress.setModel(modell_broadcastaddress);
        list_hosts.setModel(modell_hosts);
    }

    private void init(int w, int h) {
        // create Panels
        panel_subnetaddress.setPreferredSize(new Dimension(w - 10,
                panelheight_subnetaddress));
        panel_subnetaddress.setVisible(true);
        this.add(panel_subnetaddress);

        panel_broadcastaddress.setPreferredSize(new Dimension(w - 10,
                panelheight_broadcastaddress));
        panel_broadcastaddress.setVisible(true);
        this.add(panel_broadcastaddress);

        panel_hosts.setPreferredSize(new Dimension(w - 10, panelheight_hosts));
        panel_hosts.setVisible(true);
        this.add(panel_hosts);

        panel_buttons.setPreferredSize(new Dimension(w - 10, 40));
        panel_buttons.setLayout(new GridLayout(1, 2));
        this.add(panel_buttons);

        panel_subnetaddress_label.setPreferredSize(new Dimension(
                panelwidth_label, panelheight_subnetaddress));
        panel_subnetaddress_label.setVisible(true);
        panel_subnetaddress.add(panel_subnetaddress_label);
        panel_subnetaddress_text.setPreferredSize(new Dimension(
                panelwidth_text, panelheight_subnetaddress));
        panel_subnetaddress_text.setVisible(true);
        panel_subnetaddress.add(panel_subnetaddress_text);

        panel_broadcastaddress_label.setPreferredSize(new Dimension(
                panelwidth_label, panelheight_broadcastaddress));
        panel_broadcastaddress_label.setVisible(true);
        panel_broadcastaddress.add(panel_broadcastaddress_label);
        panel_broadcastaddress_text.setPreferredSize(new Dimension(
                panelwidth_text, panelheight_broadcastaddress));
        panel_broadcastaddress_text.setVisible(true);
        panel_broadcastaddress.add(panel_broadcastaddress_text);

        panel_hosts_label.setPreferredSize(new Dimension(panelwidth_label,
                panelheight_hosts));
        panel_hosts_label.setVisible(true);
        panel_hosts.add(panel_hosts_label);
        panel_hosts_text.setPreferredSize(new Dimension(panelwidth_text,
                panelheight_hosts));
        panel_hosts_text.setVisible(true);
        panel_hosts.add(panel_hosts_text);

        // JLabel
        label_subnetaddress.setVisible(true);
        label_broadcastaddress.setVisible(true);
        label_hosts.setVisible(true);
        panel_subnetaddress_label.add(label_subnetaddress);
        panel_broadcastaddress_label.add(label_broadcastaddress);
        panel_hosts_label.add(label_hosts);

        // JList
        list_subnetaddress.setPreferredSize(new Dimension(panelwidth_text,
                panelheight_subnetaddress));
        list_broadcastaddress.setPreferredSize(new Dimension(panelwidth_text,
                panelheight_broadcastaddress));
        list_subnetaddress.setVisible(true);
        list_subnetaddress.setEnabled(false);
        list_broadcastaddress.setVisible(true);
        list_broadcastaddress.setEnabled(false);
        list_hosts.setVisible(true);

        // ToolTip
        list_hosts.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Point p = new Point(e.getX(), e.getY());
                if (list_hosts.getModel().getSize() > 0) {
                    String hover = list_hosts.getModel().getElementAt(list_hosts.locationToIndex(p)).toString();
                    if (hover != null && !hover.isEmpty()) {
                        String[] binaryIpArray = hover.split("\\.");
                        String binaryIp = String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[0]))).replace(" ", "0")
                                + "." + String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[1]))).replace(" ", "0")
                                + "." + String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[2]))).replace(" ", "0")
                                + "." + String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[3]))).replace(" ", "0");

                        list_hosts.setToolTipText(binaryIp);
                    }
                }
            }
        });

        panel_subnetaddress_text.add(list_subnetaddress);
        panel_broadcastaddress_text.add(list_broadcastaddress);
        scroll_hosts.setPreferredSize(new Dimension(panelwidth_text,
                panelheight_hosts - 10));
        scroll_hosts.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_hosts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_hosts.setViewportView(list_hosts);
        panel_hosts_text.add(scroll_hosts);

        // Btn
        button_new_host.setText("New Host");
        button_delete_host.setText("Delete Host");

        panel_buttons.add(button_new_host);
        panel_buttons.add(button_delete_host);
    }

    private void initListener() {
        button_new_host.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addHost();
            }
        });

        button_delete_host.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHost();

            }
        });
    }

    private void deleteHost() {
        if (!list_hosts.isSelectionEmpty()) {
            i_control.deleteHost(list_hosts.getSelectedValue().toString());
            refreshIndex();
        }
    }

    private void addHost() {
        if (i_control.isNetworkSet() && i_control.isSubnetSet()) {
            JTextField ip = new JTextField(15);
            JPanel myPanel = new JPanel(new GridLayout(2, 1));
            JPanel iPPanel = new JPanel();
            JPanel binaryPanel = new JPanel();
            JTextField firstOctet = new JTextField(8);
            JTextField secondOctet = new JTextField(8);
            JTextField thirdOctet = new JTextField(8);
            JTextField fourthOctet = new JTextField(8);
            firstOctet.setPreferredSize(new Dimension(15, 20));
            secondOctet.setPreferredSize(new Dimension(15, 20));
            thirdOctet.setPreferredSize(new Dimension(15, 20));
            fourthOctet.setPreferredSize(new Dimension(15, 20));
            myPanel.add(iPPanel);
            myPanel.add(binaryPanel);
            iPPanel.add(new JLabel("IP:"));
            iPPanel.add(ip);
            binaryPanel.add(new JLabel("Binary:"));
            binaryPanel.add(firstOctet);
            binaryPanel.add(secondOctet);
            binaryPanel.add(thirdOctet);
            binaryPanel.add(fourthOctet);

            int result = JOptionPane.showConfirmDialog(null, myPanel,
                    "Please Enter Your New Host", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                if (ip.getText() != null && !ip.getText().isEmpty() &&
                        new IPv4Address().verifyIP(ip.getText())) {
                    i_control.addNewHost(ip.getText());
                } else if (verifyBinaryString(firstOctet.getText(), secondOctet.getText(),
                        thirdOctet.getText(), fourthOctet.getText())) {
                    String decimal = Integer.parseInt(firstOctet.getText(), 2)
                            + "." + Integer.parseInt(secondOctet.getText(), 2)
                            + "." + Integer.parseInt(thirdOctet.getText(), 2)
                            + "." + Integer.parseInt(fourthOctet.getText(), 2);
                    i_control.addNewHost(decimal);
                } else {
                    i_control.errorMessage("Please Only Enter Values Zero and One for Binary Representation");
                }
                refreshIndex();
            }
        }
    }

    private boolean verifyBinaryString(String octet0, String octet1, String octet2, String octet3) {
        Pattern pattern = Pattern.compile("[01]+");
        return pattern.matcher(octet0).matches() && pattern.matcher(octet1).matches() &&
                pattern.matcher(octet2).matches() && pattern.matcher(octet3).matches();
    }

    public void refreshIndex() {
        list_hosts.clearSelection();
        modell_hosts.removeAllElements();
        Set<String> hosts = i_control.loadHostsIntoDialog();
        for (String s : hosts) {
            modell_hosts.addElement(s);
        }
        String[] tmpInfo = i_control.getSubnetInfo();
        if (tmpInfo != null && tmpInfo.length > 0) {
            modell_subnetaddress.removeAllElements();
            modell_subnetaddress.addElement(tmpInfo[0]);
            modell_broadcastaddress.removeAllElements();
            modell_broadcastaddress.addElement(tmpInfo[1]);
        }
    }

    public void clean() {
        list_hosts.clearSelection();
        modell_hosts.removeAllElements();
        modell_subnetaddress.removeAllElements();
        modell_broadcastaddress.removeAllElements();
    }
}
