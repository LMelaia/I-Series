package org.lmelaia.iseries.fx.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.lmelaia.iseries.common.fx.FXController;
import org.lmelaia.iseries.fx.components.TextProgressBar;
import org.lmelaia.iseries.ilibrary.ITableEntry;

/**
 * The controller class for the main window.
 */
public class MainWindowController extends FXController {

    //***************
    //* FX Controls *
    //***************

    //Control Bar

    @FXML
    private Button controlButtonAdd;

    @FXML
    private Button controlButtonEdit;

    @FXML
    private Button controlButtonDelete;

    @FXML
    private Button controlButtonUnindex;

    @FXML
    private TextField controlInputField;

    @FXML
    private Button controlBtnClearSearch;

    //Media Player

    @FXML
    private Slider mediaSliderSeek;

    @FXML
    private Slider mediaSliderVolume;

    @FXML
    private Button mediaButtonPrevious;

    @FXML
    private Button mediaButtonBack;

    @FXML
    private Button mediaButtonPlay;

    @FXML
    private Button mediaButtonForward;

    @FXML
    private Button mediaButtonNext;

    @FXML
    private Button mediaButtonEnlarge;

    //Navigator

    @FXML
    private Button navButtonNavigator;

    @FXML
    private Button navButtonInformation;

    @FXML
    private Button navButtonEpisodes;

    @FXML
    private Button navButtonStatistics;

    @FXML
    private AnchorPane navDisplayPane;

    //Window controls and table

    @FXML
    protected SplitPane splitPaneVertical;

    @FXML
    protected SplitPane splitPaneHorizontal;

    @FXML
    private TableView<ITableEntry> entryTable;

    //Menu Bar

    @FXML
    private MenuItem menuItemQuit;

    @FXML
    private MenuItem menuItemSettings;

    @FXML
    private MenuItem menuItemRestart;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private MenuItem menuItemTray;

    @FXML
    private MenuItem menuItemMinimize;

    @FXML
    private MenuItem menuItemQuitDialog;

    @FXML
    private MenuItem menuItemChangeLibrary;

    @FXML
    private MenuItem menuItemAddEntry;

    @FXML
    private MenuItem menuItemEditEntry;

    @FXML
    private MenuItem menuItemDeleteEntry;

    @FXML
    private MenuItem menuItemUnindexEntry;

    // OTHER

    @FXML
    private HBox hBoxProgress;

    //*******
    //* END *
    //*******

    /**
     * Handles the Toolbar part of the main
     * window.
     */
    protected ActionBarControl controlBar;

    /**
     * Handles the media player part of the main
     * window.
     */
    protected MediaPlayerControl mediaPlayer;

    /**
     * Handles the navigator part of the main
     * window.
     */
    protected NavigatorControl navigator;

    /**
     * Handles the menu bar part of the main
     * window.
     */
    protected MenuBarControl menuBar;

    /**
     * Handles the table in the main window.
     */
    protected TableController tableController;

    /**
     * Handles the progress bar.
     */
    protected ProgressControl progressControl;

    /**
     * Initializes sub components.
     */
    @Override
    public void init() {
        //Consider initialing from a list
        controlBar = new ActionBarControl(this, new Object[]{
                controlButtonAdd, controlButtonEdit, controlButtonDelete, controlInputField, controlButtonUnindex,
                controlBtnClearSearch
        });

        this.mediaPlayer = new MediaPlayerControl(this, new Object[]{
                mediaButtonPrevious, mediaButtonBack, mediaButtonPlay, mediaButtonForward,
                mediaButtonNext, mediaButtonEnlarge, mediaSliderVolume
        });

        navigator = new NavigatorControl(this, new Object[]{
                navDisplayPane, navButtonNavigator, navButtonInformation, navButtonEpisodes, navButtonStatistics
        });

        this.menuBar = new MenuBarControl(this, new Object[]{
                menuItemQuit, menuItemSettings, menuItemRestart, menuItemAbout, menuItemTray, menuItemMinimize,
                menuItemQuitDialog, menuItemChangeLibrary, menuItemAddEntry, menuItemEditEntry, menuItemDeleteEntry,
                menuItemUnindexEntry
        });

        this.tableController = new TableController(this, entryTable);

        this.progressControl = new ProgressControl(hBoxProgress);

        controlBar.init();
        mediaPlayer.init();
        navigator.init();
        menuBar.init();
        tableController.init();
        progressControl.init();
    }

    /**
     * Saves the state (user changed)
     * of each sub-controller.
     */
    protected void saveState() {
        controlBar.saveState();
        mediaPlayer.saveState();
        navigator.saveState();
        menuBar.saveState();
        tableController.saveState();
        progressControl.saveState();
    }

    /**
     * Loads the state (user changed)
     * of each sub-controller.
     */
    protected void loadState() {
        controlBar.loadState();
        mediaPlayer.loadState();
        navigator.loadState();
        menuBar.loadState();
        tableController.loadState();
        progressControl.loadState();
    }

    // **********
    // PUBLIC API
    // **********

    /**
     * Clears the users search input
     * and clears the table filter.
     */
    public void clearSearch() {
        controlBar.clearSearch();
    }

    /**
     * @return the {@link TextProgressBar} on the main window.
     */
    public TextProgressBar getProgressBar() {
        return progressControl.getProgressBar();
    }
}
