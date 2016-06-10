package front.components;

import back.componentes.IPv4Address;
import front.Interface_controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Set;

public class TabbedPanelNetworks extends TabbedPanel {

    final TabbedPanelSubnets panel_subnets;
    final TabbedPanelHosts panel_hosts;
    private final int btn_height = 120;
    private final Interface_controller i_control;
    private final FileNameExtensionFilter jsonfilter = new FileNameExtensionFilter(
            "JSON files (*.json)", "json");
    private final JPanel panel_txt = new JPanel();
    private final JPanel panel_btn = new JPanel();
    private final JPanel panel_txt_label = new JPanel();
    private final JPanel panel_txt_show = new JPanel();
    private final JLabel label_networks = new JLabel();
    private final DefaultListModel modell = new DefaultListModel();
    private final JList list_networks = new JList();
    private final JScrollPane scroll_networks = new JScrollPane();
    private final Btn btn_new_network = new Btn();
    private final Btn btn_delete_network = new Btn();
    private final Btn btn_load = new Btn();
    private final Btn btn_save = new Btn();

    public TabbedPanelNetworks(int w, int h,
                               Interface_controller interface_controller,
                               final TabbedPanelSubnets panel_subnets,
                               final TabbedPanelHosts panel_hosts) {
        super(w, h);
        i_control = interface_controller;
        list_networks.setModel(modell);
        this.panel_subnets = panel_subnets;
        this.panel_hosts = panel_hosts;
        init(w, h);
        initListener();
    }

    private void initListener() {
        // ActionListener for Buttons
        btn_new_network.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNetwork();
            }
        });
        btn_delete_network.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteNetwork();

            }
        });

        btn_load.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                importNetworks();

            }
        });
        btn_save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exportNetworks();
            }
        });
        list_networks.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent i) {
                if (!i.getValueIsAdjusting()
                        && !list_networks.isSelectionEmpty()) {
                    i_control.changeSubnetsTabIp(list_networks
                            .getSelectedValue().toString());
                    i_control.setCurrentNetwork(list_networks
                            .getSelectedValue().toString());
                }
            }
        });
    }

    public boolean isSelectionEmpty() {
        return list_networks.isSelectionEmpty();
    }

    private void init(int w, int h) {
        // create two sections, upper section for showing networks, bottom
        // sections for buttons
        this.setLayout(new FlowLayout());
        panel_txt.setPreferredSize(new Dimension(w - 10, h - btn_height));
        panel_txt.setVisible(true);
        this.add(panel_txt);
        panel_btn.setPreferredSize(new Dimension(w - 10, 80));
        panel_btn.setVisible(true);
        this.add(panel_btn);
        // seperate upper panel into two sections, left for JLabel, right for
        // showing some shit
        panel_txt.setLayout(new FlowLayout());
        // section 1
        panel_txt_label.setPreferredSize(new Dimension(145, h - btn_height));
        panel_txt_label.setLayout(new FlowLayout());
        panel_txt_label.setVisible(true);
        panel_txt.add(panel_txt_label);
        // section2
        panel_txt_show.setPreferredSize(new Dimension(635, h - btn_height));
        panel_txt_show.setLayout(new FlowLayout());
        panel_txt_show.setVisible(true);
        panel_txt.add(panel_txt_show);
        // JLabel
        label_networks.setText("Networks");
        label_networks.setVisible(true);
        panel_txt_label.add(label_networks);
        // JList
        scroll_networks.setPreferredSize(new Dimension(635, h - btn_height - 10));
        scroll_networks.setViewportView(list_networks);
        scroll_networks.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_networks.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ToolTip
        list_networks.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (list_networks.getModel().getSize() > 0) {
                    String hover = list_networks.getModel().getElementAt(list_networks.locationToIndex(
                            new Point(e.getX(), e.getY()))).toString();
                    if (hover != null && !hover.isEmpty()) {
                        String[] binaryIpArray = hover.split("/");
                        binaryIpArray = binaryIpArray[0].split("\\.");
                        list_networks.setToolTipText(String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[0]))).replace(" ", "0")
                                + "." + String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[1]))).replace(" ", "0")
                                + "." + String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[2]))).replace(" ", "0")
                                + "." + String.format("%8s", Integer.toBinaryString(Integer.parseInt(binaryIpArray[3]))).replace(" ", "0"));
                    }
                }
            }
        });

        list_networks.setVisible(true);
        panel_txt_show.add(scroll_networks);
        // bottom section, buttons
        panel_btn.setLayout(new GridLayout(2, 2));
        btn_new_network.setText("Add Network");
        btn_delete_network.setText("Delete Network");
        btn_load.setText("Import");
        btn_save.setText("Export");
        panel_btn.add(btn_new_network);
        panel_btn.add(btn_delete_network);
        panel_btn.add(btn_load);
        panel_btn.add(btn_save);
    }

    protected void exportNetworks() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Specify a file to export");
        fc.setFileFilter(jsonfilter);
        int entry = fc.showSaveDialog(this);
        File selectedFile = fc.getSelectedFile();
        if (selectedFile != null && !selectedFile.getName().endsWith(".json")) {
            selectedFile = new File(selectedFile.getAbsoluteFile() + ".json");
        }
        if (selectedFile != null || entry == JFileChooser.APPROVE_OPTION) {
            i_control.exportData(selectedFile.getAbsolutePath());
            JFrame frame = new JFrame("Export Successful");
            // show a joptionpane dialog using showMessageDialog
            JOptionPane.showMessageDialog(frame,
                    "Successfully written to export directory: '"
                            + selectedFile.getAbsolutePath() + "'."
            );
        }
    }

    protected void importNetworks() {
        JFileChooser fc = new JFileChooser();
        fc.setApproveButtonText("Import");
        fc.setDialogTitle("Import Data");
        fc.setFileFilter(jsonfilter);
        int entry = fc.showOpenDialog(btn_load);
        File selectedFile = fc.getSelectedFile();
        if (selectedFile != null || entry == JFileChooser.APPROVE_OPTION) {
            i_control.importData(selectedFile.getAbsolutePath());
            refreshIndex();
        }
    }

    protected void deleteNetwork() {
        if (!list_networks.isSelectionEmpty()) {
            String[] tmp = list_networks.getSelectedValue().toString()
                    .split("/");
            i_control.deleteNetwork(tmp[0], tmp[1]);
            i_control.changeSubnetsTabIp("");
            refreshIndex();
            panel_subnets.clean();
        }
    }

    protected void addNetwork() {
        JTextField ip = new JTextField(15);
        JTextField prefix = new JTextField(2);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("IP:"));
        myPanel.add(ip);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Prefix:"));
        myPanel.add(prefix);
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Enter Your New Network", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION
                && new IPv4Address().verifyIP(ip.getText())) {
            i_control.addNewNetwork(ip.getText(), prefix.getText());
            refreshIndex();
        }
    }

    public void refreshIndex() {
        list_networks.clearSelection();
        modell.removeAllElements();
        Set<String> networks = i_control.loadNetworksIntoDialog();
        for (String s : networks) {
            modell.addElement(s);
        }
    }
}
