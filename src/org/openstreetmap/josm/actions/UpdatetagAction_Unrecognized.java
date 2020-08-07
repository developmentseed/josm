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
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.tools.Shortcut;

/**
 * This action displays a dialog where the user can enter a latitude and
 * longitude, and when ok is pressed, a new node is created at the specified
 * position.
 */
public final class UpdatetagAction_Unrecognized extends JosmAction {

    // remember input from last time
    private String textLatLon, textEastNorth;

    /**
     * Constructs a new {@code AddNodeAction}.
     */
    public UpdatetagAction_Unrecognized() {
        super(tr("dc_has_pattern_school=unrecognized"), "updatetag_unrecognized", tr(""),
                Shortcut.registerShortcut("updatetag_unrecognized", tr("Edit: {0}", tr("update tag unrecognized ..")),
                        KeyEvent.VK_U, Shortcut.SHIFT), true);

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
        Collection<OsmPrimitive> selection = set == null ? Collections.<OsmPrimitive>emptySet() : set.getSelected();
        if (selection.isEmpty()) {
            return;
        }

        //Set commands for updating
        Collection<Command> commands = new ArrayList<>();
        commands.add(new ChangePropertyCommand(selection, "dc_has_pattern_school", "unrecognized"));
        SequenceCommand sequenceCommand = new SequenceCommand("change values", commands);
        sequenceCommand.executeCommand();

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
