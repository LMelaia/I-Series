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

package org.lmelaia.iseries.common.fx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class to be initialized by
 * {@link FXWindowsManager}. The class
 * must extend {@link FXWindow} in order
 * to be initialized.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterFXWindow {

    /**
     * @return the name of the fxml file for the window.
     */
    String fxmlFileName() default "";

    /**
     * @return the name of the css file for the window.
     */
    String cssFileName() default "";

    /**
     * @return the controller class for the window. This must
     * be specified if an fxml file is specified.
     */
    Class<? extends FXController> controllerClass() default FXController.class;
}
