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
import java.util.HashSet;  
import org.openstreetmap.josm.command.ChangePropertyCommand;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.tools.Shortcut;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.Notification;

/**
 * This action displays a dialog where the user can enter a latitude and
 * longitude, and when ok is pressed, a new node is created at the specified
 * position.
 */
public final class UpdatetagAction_yes extends JosmAction {

    // remember input from last time
    private String textLatLon, textEastNorth;

    /**
     * Constructs a new {@code AddNodeAction}.
     */
    public UpdatetagAction_yes() {
        super(tr("ml_school"), "ml_school", tr(""),
                Shortcut.registerShortcut("ml_school=yes", tr("Edit: {0}", tr("Add tag ml_school as yes. ..")),
                        KeyEvent.VK_1, Shortcut.SHIFT), true);

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
        DataSet setNode = getLayerManager().getActiveDataSet();
        
        Collection<Way> selectionWay = set == null ? Collections.<Way>emptySet() : set.getSelectedWays();
        Collection<Node> selectionNode = setNode == null ? Collections.<Node>emptySet() : setNode.getSelectedNodes();
        Collection<Node> nodeNew = new ArrayList<>();

        //  filter nodes
        for (Node node : selectionNode) {
            if (node.isNew()) {
                nodeNew.add(node);
            }
        }

        if (!selectionWay.isEmpty()) {
            Collection<Command> commands = new ArrayList<>();
            commands.add(new ChangePropertyCommand(selectionWay, "ml_school", "yes"));
            commands.add(new ChangePropertyCommand(selectionWay, "ml_reviewed", "yes"));

            SequenceCommand sequenceCommand = new SequenceCommand("change values", commands);
            sequenceCommand.executeCommand();

            new Notification(tr("ml_school : yes --> way")).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        }
        if (!nodeNew.isEmpty()) {
            Collection<Command> commandsNode = new ArrayList<>();

            commandsNode.add(new ChangePropertyCommand(nodeNew, "ml_school", "yes"));
            commandsNode.add(new ChangePropertyCommand(nodeNew, "ml_reviewed", "yes"));
            commandsNode.add(new ChangePropertyCommand(nodeNew, "project", "project_connect_p3_ml_output"));

            SequenceCommand sequenceCommandNode = new SequenceCommand("change values", commandsNode);
            sequenceCommandNode.executeCommand();

            new Notification(tr("ml_school : yes --> node")).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        }

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
