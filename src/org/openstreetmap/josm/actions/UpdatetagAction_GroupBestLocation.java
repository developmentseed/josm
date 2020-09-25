// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.actions;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import org.openstreetmap.josm.command.ChangePropertyCommand;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.tools.Shortcut;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.Notification;

/**
 * This action displays a dialog where the user can enter a latitude and
 * longitude, and when ok is pressed, a new node is created at the specified
 * position.
 */
public final class UpdatetagAction_GroupBestLocation extends JosmAction {

    // remember input from last time
    private String textLatLon, textEastNorth;

    /**
     * Constructs a new {@code AddNodeAction}.
     */
    public UpdatetagAction_GroupBestLocation() {
        super(tr("_modified_g:_best_locat"), "_modified_g:_best_locat", tr(""),
                Shortcut.registerShortcut("_modified_g:_best_locat", tr("Edit: {0}", tr("update group _modified_g if exist _best_locat ..")),
                        KeyEvent.VK_2, Shortcut.SHIFT), true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isEnabled()) {
            return;
        }

        // Rub21
        //=========================================================== start
        //Get source layer
        DataSet set = getLayerManager().getActiveDataSet();
        Collection<Node> selection = set == null ? Collections.<Node>emptySet() : set.getSelectedNodes();
        if (selection.isEmpty()) {
            return;
        }
        // set best location
        String group_id = "";
        List<Object> valuesTrue = new ArrayList<Object>( Arrays.asList("t", "T", 1,"1",true,"TRUE","true"));
        for (Node node : selection) {
            boolean answ = valuesTrue.contains(node.getKeys().get("_best_loca"));
            if (node.getKeys().get("_group_id") !=null && answ) {
                group_id = node.getKeys().get("_group_id");
            }
        }

        //Set commands for updating
        Collection<Command> commands = new ArrayList<>();
        if (!"".equals(group_id)) {
            commands.add(new ChangePropertyCommand(selection, "_modified_", group_id));
            commands.add(new ChangePropertyCommand(selection, "_needs_sme", "FALSE"));
            commands.add(new ChangePropertyCommand(selection, "_visited", "TRUE"));
            SequenceCommand sequenceCommand = new SequenceCommand("change values", commands);
            sequenceCommand.executeCommand();
            new Notification(tr("New _modified_g : " + group_id)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        } else {
            new Notification(tr("Need a node with best_locat:TRUE or _group_id  ")).setIcon(JOptionPane.WARNING_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        }
        // commands.add(new ChangePropertyCommand(selection, "_modified_g", group_id));
        // SequenceCommand sequenceCommand = new SequenceCommand("change values", commands);
        // sequenceCommand.executeCommand();

        //=========================================================== start
    }

    @Override
    protected boolean listenToSelectionChange() {
        return false;
    }

    @Override
    protected void updateEnabledState() {
        setEnabled(getLayerManager().getEditLayer() != null);
    }
}
