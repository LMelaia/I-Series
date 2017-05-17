/*   Copyright (C) 2016  Luke Melaia
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lmelaia.iseries.build.izpack;

import java.util.Objects;

/**
 *
 * @author Luke Melaia
 */
public class TargetPanel {

    private final TargetPanelType type;

    public TargetPanel(TargetPanelType type) {
        this.type = Objects.requireNonNull(type);
    }

    public TargetPanel() {
        this(TargetPanelType.TARGET_PANEL);
    }

    public TargetPanelType getType() {
        return type;
    }

    public enum TargetPanelType {
        TARGET_PANEL,
        DEFAULT_TARGET_PANEL;
    }
}
