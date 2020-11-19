// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.actions;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.openstreetmap.josm.command.ChangePropertyCommand;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.tools.Shortcut;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.Notification;
/**
 * This action displays a dialog where the user can enter a latitude and
 * longitude, and when ok is pressed, a new node is created at the specified
 * position.
 */
public final class UpdatetagAction_unrecognized extends JosmAction {

    // remember input from last time
    private String textLatLon, textEastNorth;

    /**
     * Constructs a new {@code AddNodeAction}.
     */
    public UpdatetagAction_unrecognized() {
        super(tr("ml_school"), "ml_school", tr(""),
                Shortcut.registerShortcut("ml_school=unrecognized", tr("Edit: {0}", tr("Add tag ml_school as unrecognized..")),
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
        Collection<Way> selection = set == null ? Collections.<Way>emptySet() : set.getSelectedWays();
        if (selection.isEmpty()) {
            return;
        }

        //Set commands for updating
        Collection<Command> commands = new ArrayList<>();

        commands.add(new ChangePropertyCommand(selection, "ml_school", "unrecognized"));
        commands.add(new ChangePropertyCommand(selection, "ml_reviewed", "yes"));

        SequenceCommand sequenceCommand = new SequenceCommand("change values", commands);
        sequenceCommand.executeCommand();
        
        new Notification(tr("ml_school : unrecognized")).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();

        
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
