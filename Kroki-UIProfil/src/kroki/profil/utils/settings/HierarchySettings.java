/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.settings;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.uml_core_basic.UmlProperty;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class HierarchySettings extends VisibleAssociationEndSettings {

    JTextField levelTf;
    JLabel levelLb;
    JLabel viaAssociationEndLb;
    JLabel appliedToPanelLb;
    JLabel hierarchyParentLb;
    JTextField hierarchyParentTf;
    JTextField viaAssociationEndTf;
    JTextField appliedToPanelTf;
    JButton hierarchyParentBtn;
    JButton viaAssociationEndBtn;
    JButton appliedToPanelBtn;

    public HierarchySettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {
        levelLb = new JLabel(Intl.getValue("hierarchy.level"));
        viaAssociationEndLb = new JLabel(Intl.getValue("hierarchy.viaAssociationEnd"));
        appliedToPanelLb = new JLabel(Intl.getValue("hierarchy.appliedToPanel"));
        hierarchyParentLb = new JLabel(Intl.getValue("hierarchy.hierarchyParent"));
        levelTf = new JTextField();
        levelTf.setEditable(false);
        hierarchyParentTf = new JTextField();
        hierarchyParentTf.setEditable(false);
        viaAssociationEndTf = new JTextField();
        viaAssociationEndTf.setEditable(false);
        appliedToPanelTf = new JTextField();
        appliedToPanelTf.setEditable(false);
        hierarchyParentBtn = new JButton("...");
        viaAssociationEndBtn = new JButton("...");
        appliedToPanelBtn = new JButton("...");
        hierarchyParentBtn.setPreferredSize(new Dimension(30, 20));
        hierarchyParentBtn.setMinimumSize(sortByBtn.getPreferredSize());
        viaAssociationEndBtn.setPreferredSize(new Dimension(30, 20));
        viaAssociationEndBtn.setMinimumSize(sortByBtn.getPreferredSize());
        appliedToPanelBtn.setPreferredSize(new Dimension(30, 20));
        appliedToPanelBtn.setMinimumSize(sortByBtn.getPreferredSize());
    }

    private void layoutComponents() {
        JPanel intermediate = null;
        if (panelMap.containsKey("group.INTERMEDIATE")) {
            intermediate = panelMap.get("group.INTERMEDIATE");
        } else {
            intermediate = new JPanel();
            intermediate.setLayout(new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]"));
            panelMap.put("group.INTERMEDIATE", intermediate);
            this.addTab(Intl.getValue("group.INTERMEDIATE"), intermediate);
        }
        intermediate.add(levelLb);
        intermediate.add(levelTf);

        intermediate.add(hierarchyParentLb);
        intermediate.add(hierarchyParentTf, "split 2, grow");
        intermediate.add(hierarchyParentBtn, "shrink");

        intermediate.add(viaAssociationEndLb);
        intermediate.add(viaAssociationEndTf, "split 2, grow");
        intermediate.add(viaAssociationEndBtn, "shrink");

        intermediate.add(appliedToPanelLb);
        intermediate.add(appliedToPanelTf, "split 2, grow");
        intermediate.add(appliedToPanelBtn, "shrink");
    }

    @Override
    public void updateComponents() {
        super.updateComponents();

        Hierarchy hierarchy = (Hierarchy) visibleElement;

        //NEW:
        ParentChild panel = (ParentChild) ((UmlProperty) visibleElement).umlClass();


        levelTf.setText("" + hierarchy.getLevel());
        String hierarchyParentValue = "";
        if (hierarchy.getHierarchyParent() != null) {
            hierarchyParentValue = hierarchy.getHierarchyParent().toString();
        }
        hierarchyParentTf.setText(hierarchyParentValue);

        String viaAssociationEndValue = "";
        if (hierarchy.getViaAssociationEnd() != null) {
            viaAssociationEndValue = hierarchy.getViaAssociationEnd().toString();
        }
        viaAssociationEndTf.setText(viaAssociationEndValue);

        String appliedToPanelValue = "";
        if (hierarchy.getAppliedToPanel() != null) {
            appliedToPanelValue = hierarchy.getAppliedToPanel().toString();
        }
        appliedToPanelTf.setText(appliedToPanelValue);

        //potrebna disejblovanja vezana za ogranicenja koja se odnose na prosirivanje prava nad formom hijerarhije!
//        if(hierarchy.getTargetPanel()!=null){
//            if(hierarchy.getTargetPanel() instanceof StandardPanel){
//                StandardPanel targetSp = (StandardPanel) hierarchy.getTargetPanel();
//                if(!targetSp.isAdd()){
//                    hierarchy.setAdd(false);
//                    addCb.setSelected(false);
//                    addCb.setEnabled(false);
//                }
//                if(!targetSp.isChangeMode()){
//                    hierarchy.setChangeMode(false);
//                    changeModeCb.setSelected(false);
//                    changeModeCb.setEnabled(false);
//                }
//                if(!targetSp.isCopy()){
//                    hierarchy.setCopy(false);
//                    copyCb.setSelected(false);
//                    copyCb.setEnabled(false);
//                }
//                if(!targetSp.isDataNavigation()){
//                    hierarchy.setDataNavigation(false);
//                    dataNavigationCb.setSelected(false);
//                    dataNavigationCb.setEnabled(false);
//                }
//                //...
//            }
//
//        }


        if (panel.getHierarchyCount() == 0 || panel.getHierarchyCount() == 1) {
            hierarchyParentBtn.setEnabled(false);
            viaAssociationEndBtn.setEnabled(false);
            appliedToPanelBtn.setEnabled(false);
        } else if (panel.getHierarchyCount() == 2) {
            viaAssociationEndBtn.setEnabled(true);
            appliedToPanelBtn.setEnabled(false);
        } else {
            hierarchyParentBtn.setEnabled(true);
            viaAssociationEndBtn.setEnabled(true);
            appliedToPanelBtn.setEnabled(false);
        }

        if (hierarchy.getTargetPanel() != null) {
            if (hierarchy.getTargetPanel() instanceof ParentChild) {
                appliedToPanelBtn.setEnabled(true);
            }
        }


    }

    private void addActions() {
        hierarchyParentBtn.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                //NEW
                ParentChild panel = (ParentChild) ((UmlProperty) visibleElement).umlClass();
                List<Hierarchy> hierarcyList = panel.allContainedHierarchies();
                Hierarchy parentHierarchy = (Hierarchy) ListDialog.showDialog(hierarcyList.toArray(), "Choose parent hierarchy");
                ((Hierarchy) visibleElement).setHierarchyParent(parentHierarchy);
                if (parentHierarchy != null) {
                    hierarchyParentTf.setText(parentHierarchy.toString());
                }
            }
        });
        viaAssociationEndBtn.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                Hierarchy hierarchy = (Hierarchy) visibleElement;
                List<VisibleAssociationEnd> viaAssociationEndList = new ArrayList<VisibleAssociationEnd>();
                if (hierarchy.getHierarchyParent() != null) {
                    if (hierarchy.getTargetPanel() != null) {
                        VisibleClass parentPanel = hierarchy.getHierarchyParent().getTargetPanel();
                        VisibleClass targetPanel = hierarchy.getTargetPanel();
                        List<Zoom> zooms = targetPanel.containedZooms();
                        for (int i = 0; i < zooms.size(); i++) {
                            Zoom zoom = zooms.get(i);
                            if (zoom.getTargetPanel() == parentPanel) {
                                viaAssociationEndList.add(zoom);
                            }
                        }
                    }
                }
                VisibleAssociationEnd viaAssociationEnd = (VisibleAssociationEnd) ListDialog.showDialog(viaAssociationEndList.toArray(), "Choose via association end:");

                if (viaAssociationEnd != null) {
                    viaAssociationEndTf.setText(viaAssociationEnd.toString());
                    hierarchy.setViaAssociationEnd(viaAssociationEnd);
                }

            }
        });
        appliedToPanelBtn.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                Hierarchy hierarchy = (Hierarchy) visibleElement;
                List<VisibleClass> targetPanelList = new ArrayList<VisibleClass>();
                if (hierarchy.getTargetPanel() != null) {
                    VisibleClass targetPanel = hierarchy.getTargetPanel();
                    if (targetPanel instanceof ParentChild) {
                        List<Hierarchy> containedHierarchies = ((ParentChild) targetPanel).allContainedHierarchies();
                        for (int i = 0; i < containedHierarchies.size(); i++) {
                            VisibleClass tp = containedHierarchies.get(i).getTargetPanel();
                            if (tp != null) {
                                targetPanelList.add(tp);
                            }
                        }
                    }
                }
                VisibleClass targetPanel = (VisibleClass) ListDialog.showDialog(targetPanelList.toArray(), "Choose applied to panel");

                if (targetPanel != null) {
                    hierarchy.setAppliedToPanel(targetPanel);
                    appliedToPanelTf.setText(targetPanel.toString());
                }
            }
        });

    }

    @Override
    public void updateSettings(VisibleElement visibleElement) {
        super.updateSettings(visibleElement);
    }

    @Override
    public void updatePreformed() {
        super.updatePreformed();
    }
}
