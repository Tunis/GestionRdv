package app;

import app.controller.RootLayoutCtrl;
import app.controller.TabHomeCtrl;
import app.controller.TabPlanningContainerCtrl;
import app.controller.dialog.CreateMedecinDialogCtrl;
import app.controller.dialog.CreatePatientDialogCtrl;
import app.controller.dialog.ProfilPatientDialogCtrl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import metier.action.MMedecin;
import metier.action.MPatient;
import metier.hibernate.DataBase;

import java.io.IOException;


public class Main extends Application {
	
	private Stage primaryStage;
	private TabPane rootLayout;
	private BorderPane planningContainer;

	private MPatient mPatient = new MPatient();
    private MMedecin mMedecin = new MMedecin();
	
	//Construct
	public Main(){
		
	}
	
	//Getter
	public Stage getPrimaryStage(){
		return this.primaryStage;
	}
	
	public BorderPane getPlanningContainer(){
		return this.planningContainer;
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gestion RdV");

        this.primaryStage.setOnCloseRequest(event -> DataBase.close());
        
        initRootLayout();
	}
	
	//Initializes the root layout.
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (TabPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            // Give the controller access to the main app.
            RootLayoutCtrl controller = loader.getController();
            controller.setMainApp(this);
            controller.setView();
            
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
  
    
    //Overview for Tab
    //--------------------------------------------
    public BorderPane homeOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TabHomeOverview.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            
            //Give the controller access to the main app.
            TabHomeCtrl controller = loader.getController();
            controller.setMainApp(this, mPatient, mMedecin);
         
            return borderPane;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public BorderPane paiementOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TabPaiementOverview.fxml"));
            return(BorderPane) loader.load();
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public BorderPane comptaOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TabComptaOverview.fxml"));
            return(BorderPane) loader.load();
         
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public BorderPane tiersPayantOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TabTiersPayantsOverview.fxml"));
            return(BorderPane) loader.load();
         
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public BorderPane planningContainer() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TabPlanningContainer.fxml"));
            planningContainer = (BorderPane) loader.load();
            
            //Give the controller access to the main app.
            TabPlanningContainerCtrl controller = loader.getController();
            controller.setMainApp(this);
            
            
            return planningContainer;
         
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    //--------------------------------------------
    //Show "Create" Dialog
    public void showCreatePatientDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dialog/CreatePatientDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cr�er un Patient");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CreatePatientDialogCtrl controller = loader.getController();
            controller.setDialogStage(dialogStage, mPatient);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showCreateMedecinDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dialog/CreateMedecinDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cr�er un Medecin");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CreateMedecinDialogCtrl controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Show "Profil" Dialog
    public void showProfilPatientDialog(){
    	try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dialog/ProfilPatientDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Profil");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ProfilPatientDialogCtrl controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Main
    //--------------------------------------------
	public static void main(String[] args) {
		launch(args);
	}
}
