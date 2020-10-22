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

/**
 * This action displays a dialog where the user can enter a latitude and
 * longitude, and when ok is pressed, a new node is created at the specified
 * position.
 */
public final class UpdatetagAction_AddProject extends JosmAction {

    // remember input from last time
    private String textLatLon, textEastNorth;

    /**
     * Constructs a new {@code AddNodeAction}.
     */
    public UpdatetagAction_AddProject() {
        super(tr("project"), "project", tr(""),
                Shortcut.registerShortcut("project:cccmc_sustainable_rubber", tr("Edit: {0}", tr("Add tag project: cccmc_sustainable_rubber ..")),
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
        Collection<Way> selection = set == null ? Collections.<Way>emptySet() : set.getSelectedWays();
        if (selection.isEmpty()) {
            return;
        }

        //Set commands for updating
        Collection<Command> commands = new ArrayList<>();

        commands.add(new ChangePropertyCommand(selection, "project", "cccmc_sustainable_rubber"));
        commands.add(new ChangePropertyCommand(selection, "natural", "rubber"));

        SequenceCommand sequenceCommand = new SequenceCommand("change values", commands);
        sequenceCommand.executeCommand();
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
