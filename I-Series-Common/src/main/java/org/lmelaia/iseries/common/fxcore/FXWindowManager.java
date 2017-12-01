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

package org.lmelaia.iseries.common.fxcore;

import com.google.common.reflect.ClassPath;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.lmelaia.iseries.common.system.AppBase;
import org.lmelaia.iseries.common.system.AppLogger;
import org.lmelaia.iseries.common.system.ExitCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all declared {@link FXWindow}s.
 * <p>
 * <p>
 * Provides a way to initialize
 * a window by annotating it with
 * {@link LoadFXWindow}, along
 * with a few other utilities
 * to manipulate and use the declared
 * windows.
 * </p>
 */
public class FXWindowManager extends Application {

    /**
     * Logging framework instance.
     */
    private static final Logger LOG = AppLogger.getLogger();

    /**
     * FX window manager instance.
     */
    private static FXWindowManager INSTANCE;

    /**
     * The name of the fx thread.
     */
    private static String name;

    /**
     * Package path to where
     * the windows are declared.
     */
    private static String windowsPath;

    /**
     * The application instance the fx thread is going
     * to be run on.
     */
    private static AppBase app;

    /**
     * List of all declared windows.
     */
    private List<FXWindow> windows = new ArrayList<>();

    /**
     * Starts the FX application thread in a nonblocking way
     * and initializes all declared windows.
     *
     * @param threadName  the name of the FX thread.
     * @param args        the arguments passed to the program.
     * @param windowsPath the package path to where the
     *                    windows are declared.
     * @param app         the application instance the FX thread
     *                    will be run on.
     */
    public static void startFX(String threadName, String[] args, String windowsPath, AppBase app) {
        if (INSTANCE != null)
            throw new IllegalStateException("FX thread already started");

        LOG.info("Starting FX thread...");
        name = threadName;
        FXWindowManager.windowsPath = windowsPath;
        FXWindowManager.app = app;

        new Thread("FX starter") {
            @Override
            public void run() {
                Application.launch(FXWindowManager.class, args);
            }
        }.start();

        Platform.setImplicitExit(false);
    }

    /**
     * @return the FX window manager instance if
     * initialized by {@link #startFX(String, String[], String, AppBase)}.
     * @throws IllegalStateException if the instance
     *                               has not been initialized by {@link #startFX(String, String[], String, AppBase)}.
     */
    public static FXWindowManager getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("Instance not initialized");

        return INSTANCE;
    }

    /**
     * Initializes all declared windows and
     * further sets up the FX thread.
     *
     * @param primaryStage the primary stage given. <b>Unused.</b>
     */
    @Override
    public void start(Stage primaryStage) {
        if (INSTANCE != null)
            throw new IllegalStateException("FX thread already started");

        INSTANCE = this;

        try {
            Thread.currentThread().setName(name + " FX Thread");
            app.manageThread(Thread.currentThread());
            LOG.info("FX thread started: " + Thread.currentThread().toString());

            initWindows();
            postInitWindows();
        } catch (Exception e) {
            LOG.fatal("Uncaught exception propagated to FX start()", e);
            app.exit(ExitCode.UNHANDLED_EXCEPTION);
        }
    }

    /**
     * Constructs a new instance of each
     * declared window and calls the
     * {@link FXWindow#initialize()} method
     * on it.
     */
    private void initWindows() {
        try {
            for (Class c : getWindowClasses()) {
                LOG.info("Initializing window: " + c.getCanonicalName());
                FXWindow window = (FXWindow) c.newInstance();
                windows.add(window);
                window.initialize();
            }
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            //Should not happen as each class is assured
            //to exist.
            throw new IllegalStateException(e);
        }

    }

    /**
     * Calls the {@link FXWindow#postInitialize()}
     * method of each declared window.
     */
    private void postInitWindows() {
        for (FXWindow window : windows)
            window.postInitialize();
    }

    /**
     * @return a list of all found, declared window classes.
     * @throws IOException if the attempt to read class path resources
     *                     (jar files or directories) failed.
     */
    private List<Class> getWindowClasses() throws IOException {
        List<Class> classList = new ArrayList<>();
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
            if (info.getName().startsWith(windowsPath)) {
                final Class<?> clazz = info.load();
                if (clazz.getSuperclass().getCanonicalName().equals(FXWindow.class.getCanonicalName())) {
                    if (clazz.getDeclaredAnnotation(LoadFXWindow.class) != null) {
                        classList.add(clazz);
                    }
                }
            }
        }

        return classList;
    }
}