/*
 * Copyright (C) 2016  Luke Melaia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lmelaia.iseries.fx.exit_dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import org.lmelaia.iseries.common.fx.FXController;

/**
 * Quit confirmation dialog controller.
 */
public class ExitDialogController extends FXController {

    @FXML
    private Button btnQuit;

    @FXML
    private Button btnTray;

    @FXML
    private Button btnCancel;

    @FXML
    private CheckBox checkBoxRemember;

    /**
     * Currently selected button.
     */
    protected ExitDialog.ResultOption result = ExitDialog.ResultOption.CANCEL;

    /**
     * @return true if the remember choice check box is
     * checked.
     */
    public boolean remember() {
        return checkBoxRemember.isSelected();
    }

    /**
     * Sets up the window logic.
     */
    @Override
    public void init() {
        btnQuit.setOnAction(a -> {
            result = ExitDialog.ResultOption.QUIT;
            this.getWindow().close();
        });

        btnTray.setOnAction(a -> {
            result = ExitDialog.ResultOption.TRAY;
            this.getWindow().close();
        });

        btnCancel.setOnAction(a -> {
            result = ExitDialog.ResultOption.CANCEL;
            this.getWindow().close();
        });

        this.getWindow().focusedProperty().addListener(l -> {
            if (!this.getWindow().isFocused()) {
                this.getWindow().hide();
            }
        });
    }
}
