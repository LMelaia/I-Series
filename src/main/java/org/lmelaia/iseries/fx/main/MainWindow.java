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
package org.lmelaia.iseries.fx.main;

import javafx.scene.image.Image;
import org.lmelaia.iseries.App;
import org.lmelaia.iseries.Settings;
import org.lmelaia.iseries.common.fx.FXWindow;
import org.lmelaia.iseries.common.fx.RegisterFXWindow;
import org.lmelaia.iseries.common.system.ExitCode;
import org.lmelaia.iseries.fx.exit.ExitWindow;

/**
 * The main display window for I-Series.
 */
@RegisterFXWindow(
        fxmlFileName = "windows/main_window.fxml",
        cssFileName = "windows/css/main_window.css",
        controllerClass = MainWindowController.class
)
public class MainWindow extends FXWindow<MainWindowController> {

    /**
     * Object reference to setting for quick access.
     */
    private static final Settings quitPreference = Settings.WINDOW_CLOSE_PREFERENCE;

    /**
     * Constructs a new window instance.
     */
    public MainWindow() {
        this.setTitle("I-Series");
    }

    /**
     * Set's the application up to close when this window closes.
     */
    @Override
    protected void onInitialization() {
        this.getIcons().add(new Image("/images/iseries-32.png"));

        this.setOnCloseRequest(e -> {
            if (quitPreference.getValueAsInt() == 0) {
                ExitWindow.Result result = ExitWindow.present();
                if (result.remember())
                    quitPreference.changeValue(result.getOption().value);

                if (result.getOption() == ExitWindow.ResultOption.QUIT)
                    App.getInstance().exit(ExitCode.NORMAL);

                if (result.getOption() == ExitWindow.ResultOption.TRAY)
                    App.getInstance().exit(ExitCode.TRAY);

                e.consume();
            } else if (quitPreference.getValueAsInt() == 1) {
                App.getInstance().exit(ExitCode.NORMAL);
            } else if (quitPreference.getValueAsInt() == 2) {
                App.getInstance().exit(ExitCode.TRAY);
            }

            e.consume();
        });

        App.getInstance().addShutdownListener(code -> {
            saveState();
            return true;
        });

        this.setMinWidth(870);
        this.setMinHeight(600);
    }

    /**
     * Loads the previous window configuration set by the user.
     */
    @Override
    protected void onPostInitialization() {
        loadLastState();
    }

    /**
     * Saves the users window preferences (e.g. window size and position).
     */
    protected void saveState() {
        Settings.WINDOW_X.changeValue(this.getX());
        Settings.WINDOW_Y.changeValue(this.getY());
        Settings.WINDOW_WIDTH.changeValue(this.getWidth());
        Settings.WINDOW_HEIGHT.changeValue(this.getHeight());

        this.controller.saveState();
    }

    /**
     * Loads the users window preferences from file.
     */
    protected void loadLastState() {
        double x = Settings.WINDOW_X.getValueAsDouble();
        double y = Settings.WINDOW_Y.getValueAsDouble();

        if (x == 0 && y == 0)
            this.setMaximized(true);

        this.setX(x);
        this.setY(y);

        this.setWidth(Settings.WINDOW_WIDTH.getValueAsDouble());
        this.setHeight(Settings.WINDOW_HEIGHT.getValueAsDouble());

        this.controller.loadState();
    }
}
