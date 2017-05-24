package app.controller;

import app.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class RootLayoutCtrl {
	
	private Main mainApp;
	
	//Tab
    //-----------------------------------
	@FXML
    private Tab TabHome;
	@FXML
    private Tab TabPlanning;
    @FXML
    private Tab TabPaiement;
    @FXML
    private Tab TabTP;
    @FXML
    private Tab TabCompta;
	
    //Main m�thods
    //-----------------------------------
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

	//Set content for all Tab
	public void setView(){
		TabHome.setContent(mainApp.homeOverview());
		TabPaiement.setContent(mainApp.paiementOverview());
		TabCompta.setContent(mainApp.comptaOverview());
		TabPlanning.setContent(mainApp.planningContainer());
		TabTP.setContent(mainApp.tiersPayantOverview());
	}
}
