package front.components;

import front.Interface_controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class TabbedPanelSubnets extends TabbedPanel {

    final TabbedPanelHosts panel_hosts;
    private final Interface_controller i_control;
    private final int panelwidth_label = 145;
    private final int panelwidth_text = 635;
    private final int panelheight_network = 40;
    private final int panelheight_netclass = 40;
    private final int panelheight_subnets = 430;

    // JPanels
    private final JPanel panel_network = new JPanel();
    private final JPanel panel_network_label = new JPanel();
    private final JPanel panel_network_text = new JPanel();

    private final JPanel panel_buttons = new JPanel();

    private final JPanel panel_netclass = new JPanel();
    private final JPanel panel_netclass_label = new JPanel();
    private final JPanel panel_netclass_text = new JPanel();

    private final JPanel panel_subnets = new JPanel();
    private final JPanel panel_subnets_label = new JPanel();
    private final JPanel panel_subnets_text = new JPanel();

    // JLists
    private final JList list_network = new JList();
    private final JList list_netclass = new JList();
    private final JList list_subnets = new JList();
    private final JScrollPane scroll_subnets = new JScrollPane();
    private final DefaultListModel modell_subnets = new DefaultListModel();
    private final DefaultListModel modell_network = new DefaultListModel();
    private final DefaultListModel modell_netclass = new DefaultListModel();

    //JLabel
    private final JLabel label_network = new JLabel("Network");
    private final JLabel label_netclass = new JLabel("Netclass");
    private final JLabel label_subnets = new JLabel("Subnets");

    // Btn
    private final Btn button_new_subnet = new Btn();
    private final Btn button_delete_subnet = new Btn();


    public TabbedPanelSubnets(int w, int h, Interface_controller interface_controller, final TabbedPanelHosts panel_hosts) {
        super(w, h);
        this.setLayout(new FlowLayout());
        i_control = interface_controller;
        this.panel_hosts = panel_hosts;
        list_subnets.setModel(modell_subnets);
        list_network.setModel(modell_network);
        list_netclass.setModel(modell_netclass);
        init(w, h);
        initListener();
    }

    private void init(int w, int h) {
        // create panels for subnets
        panel_network.setPreferredSize(new Dimension(w - 10, panelheight_network));
        panel_network.setVisible(true);
        this.add(panel_network);
        panel_netclass.setPreferredSize(new Dimension(w - 10, panelheight_netclass));
        panel_netclass.setVisible(true);
        this.add(panel_netclass);
        panel_subnets.setPreferredSize(new Dimension(w - 10, panelheight_subnets));
        panel_subnets.setVisible(true);
        this.add(panel_subnets);
        panel_buttons.setPreferredSize(new Dimension(w - 10, 40));
        panel_buttons.setLayout(new GridLayout(1, 3));
        panel_buttons.setVisible(true);
        this.add(panel_buttons);

        panel_network_label.setPreferredSize(new Dimension(panelwidth_label, panelheight_network));
        panel_network_label.setVisible(true);
        panel_network.add(panel_network_label);

        panel_network_text.setPreferredSize(new Dimension(panelwidth_text, panelheight_network));
        panel_network_text.setVisible(true);
        panel_network.add(panel_network_text);

        panel_netclass_label.setPreferredSize(new Dimension(panelwidth_label, panelheight_netclass));
        panel_netclass_label.setVisible(true);
        panel_netclass.add(panel_netclass_label);

        panel_netclass_text.setPreferredSize(new Dimension(panelwidth_text, panelheight_netclass));
        panel_netclass_text.setVisible(true);
        panel_netclass.add(panel_netclass_text);

        panel_subnets_label.setPreferredSize(new Dimension(panelwidth_label, panelheight_subnets));
        panel_subnets_label.setVisible(true);
        panel_subnets.add(panel_subnets_label);

        panel_subnets_text.setPreferredSize(new Dimension(panelwidth_text, panelheight_subnets));
        panel_subnets_text.setVisible(true);
        panel_subnets.add(panel_subnets_text);

        // JLists erstellen für Network, NetClass, Subnets
        list_network.setPreferredSize(new Dimension(panelwidth_text, panelheight_network));
        list_network.setVisible(true);
        list_network.setEnabled(false);
        panel_network_text.add(list_network);
        list_netclass.setPreferredSize(new Dimension(panelwidth_text, panelheight_netclass));
        list_netclass.setVisible(true);
        list_netclass.setEnabled(false);
        panel_netclass_text.add(list_netclass);
        scroll_subnets.setPreferredSize(new Dimension(panelwidth_text, panelheight_subnets - 10));
        scroll_subnets.setViewportView(list_subnets);
        scroll_subnets.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_subnets.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ToolTip
//        list_subnets
        list_subnets.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Point p = new Point(e.getX(), e.getY());
                if (list_subnets.getModel().getSize() > 0) {
                    String hover = list_subnets.getModel().getElementAt(list_subnets.locationToIndex(p)).toString();
                    if (hover != null && !hover.isEmpty()) {
                        String[] tmp = hover.split("/");
                        String[] binaryIpArray = tmp[0].split("\\.");
                        String binaryIp = String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[0]))).replace(" ", "0")
                                + "." + String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[1]))).replace(" ", "0")
                                + "." + String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[2]))).replace(" ", "0")
                                + "." + String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[3]))).replace(" ", "0");

                        list_subnets.setToolTipText(binaryIp);
                    }
                }
            }
        });

        list_subnets.setAutoscrolls(true);
        list_subnets.setVisible(true);
        panel_subnets_text.add(scroll_subnets);

        // test!
//		panel_network.setBackground(Color.red);
//		panel_netclass.setBackground(Color.yellow);
//		panel_subnets.setBackground(Color.cyan);
//		panel_buttons.setBackground(Color.blue);


        // Drop Down Liste für Subnet erstellen

        // Buttons erstellen für Subnet
        button_new_subnet.setText("Add Subnet");
        button_delete_subnet.setText("Delete Subnet");
        button_new_subnet.setVisible(true);
        button_delete_subnet.setVisible(true);
        panel_buttons.add(button_new_subnet);
        panel_buttons.add(button_delete_subnet);

        //JLabels für Panels
        label_network.setVisible(true);
        label_netclass.setVisible(true);
        label_subnets.setVisible(true);
        panel_network_label.add(label_network);
        panel_netclass_label.add(label_netclass);
        panel_subnets_label.add(label_subnets);
    }

    private void initListener() {

        button_new_subnet.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                addSubnet();
            }
        });
        button_delete_subnet.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                deleteSubnet();

            }
        });
        list_subnets.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent i) {
                if (!i.getValueIsAdjusting() && !list_subnets.isSelectionEmpty()) {
                    i_control.setCurrentSubnet(list_subnets.getSelectedValue().toString());
                }
            }
        });
    }

    private void addSubnet() {
        if (i_control.isNetworkSet() && !i_control.isSubnetSet()) {
            String networkIp = i_control.getNetworkIp();
            JTextField prefix = new JTextField(2);
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Prefix:"));
            myPanel.add(prefix);
            int result = JOptionPane.showConfirmDialog(null, myPanel,
                    "Please Enter Your New Subnet Prefix", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION && i_control.verifyPrefix(prefix.getText())) {
                i_control.addNewSubnet(networkIp, prefix.getText());
                refreshIndex();
            }
        } else if (!i_control.isSubnetSet() && i_control.isNetworkSet()) {
            if (i_control.generateNextSubnet() != null) {
                i_control.addNewSubnet(i_control.generateNextSubnet(), i_control.getSubnetPrefix());
                refreshIndex();
            } else {
                i_control.errorMessage("There are no further subnets possible for this network");
            }
        }
    }

    private void deleteSubnet() {
        if (!list_subnets.isSelectionEmpty()) {
            String[] tmp = list_subnets.getSelectedValue().toString().split(" - ");
            tmp = tmp[0].toString().split("/");
            i_control.deleteSubnet(tmp[0], tmp[1]);
            i_control.changeSubnetsTabIp("");
            panel_hosts.clean();
            refreshIndex();
        }
    }

    public void refreshIndex() {
        modell_subnets.removeAllElements();
        Set<String> subnets = i_control.loadSubnetsIntoDialog();
        for (String s : subnets) {
            modell_subnets.addElement(s);
        }
        modell_network.removeAllElements();
        modell_network.addElement(i_control.getNetworkInfo());
        modell_netclass.removeAllElements();
        setNetClass();
    }

    private boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private void setNetClass() {
        if (i_control.getNetworkIp().contains(".")) {
            int firstOkt = Integer.parseInt(i_control.getNetworkIp().split("\\.")[0]);
            if (isBetween(firstOkt, 10, 128)) {
                modell_netclass.addElement("Class A");
            } else if (isBetween(firstOkt, 129, 191)) {
                modell_netclass.addElement("Class B");
            } else if (isBetween(firstOkt, 192, 223)) {
                modell_netclass.addElement("Class C");
            } else if (isBetween(firstOkt, 224, 239)) {
                modell_netclass.addElement("Class E");
            } else {
                modell_netclass.addElement("Class D");
            }
        }
    }

    public void clean() {
        list_subnets.clearSelection();
        modell_subnets.removeAllElements();
        modell_netclass.removeAllElements();
        modell_network.removeAllElements();
    }
}