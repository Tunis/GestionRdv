package app;

import app.controller.*;
import app.controller.dialog.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import metier.action.*;
import metier.hibernate.DataBase;
import models.Medecin;
import models.Patient;
import models.Rdv;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Main extends Application {
	// TODO: 04/06/2017 faire les cell Factory pour les liste / gridPane
	// TODO: 04/06/2017 faire les css
	// TODO: 04/06/2017 faire la view tier payant
	// TODO: 04/06/2017 faire la view compta
	// TODO: 04/06/2017 faire l'impression des compta (/jour et /mois)
	// TODO: 04/06/2017 faire l'impresseion des rdv des patient (imprime liste des rdv d'un patient)
	// TODO: 04/06/2017 faire l'impression du planning de la journée pour le medecin (imprime la view day du medecin)

	private Stage primaryStage;
	private TabPane rootLayout;
	private BorderPane planningContainer;

	private TabPaiementOverviewCtrl tabPaiementOverviewCtrl;
	public TabPaiementOverviewCtrl getTabPaiementOverviewCtrl() {
		return tabPaiementOverviewCtrl;
	}

	private TabHomeCtrl tabHomeCtrl;
	public TabHomeCtrl getTabHomeCtrl() {
		return tabHomeCtrl;
	}

    private TabTpCtrl tabTpCtrl;

    public TabTpCtrl getTabTpCtrl() {
        return tabTpCtrl;
    }

    private TabPlanningContainerCtrl tabPlanningCtrl;

    public TabPlanningContainerCtrl getTabPlanningCtrl() {
        return tabPlanningCtrl;
    }

	private MPatient mPatient = new MPatient();
    private MMedecin mMedecin = new MMedecin();
    private MPaiement mPaiement = new MPaiement();
    private MCompta mCompta = new MCompta();
    private MRdv mRdv = new MRdv();
    private LocalDate date;

    public LocalDate getDate(){ return date;}
    public void changeDate(LocalDate date){ this.date = date;}


    private ObjectProperty<Medecin> medecin = new SimpleObjectProperty<>();

    public Medecin getMedecin() {
        return medecin.get();
    }

    public ObjectProperty<Medecin> medecinProperty() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin.set(medecin);
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
		this.primaryStage.setMinWidth(1000);
		this.primaryStage.setMinHeight(600);
		this.primaryStage.setTitle("Gestion RdV");
        this.primaryStage.setOnCloseRequest(event -> DataBase.close());

        medecin.addListener((observable, oldValue, newValue) -> {
            primaryStage.setTitle("Gestion de Rdv " + medecin.get().showName());
        });
        // bug don't take all screen ...
        //this.primaryStage.setMaximized(true);
        this.primaryStage.show();

        //showCreateRdvDialog(LocalDateTime.now(), (Medecin)mMedecin.getList().get(0));
        
        initRootLayout();
	}
	
	//Initializes the root layout.
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/RootLayout.fxml"));
            rootLayout = loader.load();

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
            loader.setLocation(Main.class.getResource("/views/TabHomeOverview.fxml"));
            BorderPane borderPane = loader.load();
            
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
            loader.setLocation(Main.class.getResource("/views/TabPaiementOverview.fxml"));
            BorderPane borderPane = loader.load();
            tabPaiementOverviewCtrl = loader.getController();
	        tabPaiementOverviewCtrl.initController(this, mPaiement, mRdv);
	        return borderPane;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public BorderPane comptaOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/TabComptaOverview.fxml"));

            BorderPane comptaOverview = loader.load();
            TabComptaCtrl controller = loader.getController();
            controller.initCompta(this, mMedecin, mCompta);

            return comptaOverview;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public BorderPane tiersPayantOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/TabTiersPayantsOverview.fxml"));
            BorderPane tp = loader.load();
            tabTpCtrl = loader.getController();
            tabTpCtrl.initController(this, mMedecin);
            return tp;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public BorderPane planningContainer() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/TabPlanningContainer.fxml"));
            planningContainer = loader.load();
            
            //Give the controller access to the main app.
            tabPlanningCtrl = loader.getController();
            tabPlanningCtrl.setMainApp(this, mMedecin);
            
            
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
            loader.setLocation(Main.class.getResource("/views/dialog/CreatePatientDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cr�er un Patient");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
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
            loader.setLocation(Main.class.getResource("/views/dialog/CreateMedecinDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Créer un Medecin");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CreateMedecinDialogCtrl controller = loader.getController();
            controller.setDialogStage(dialogStage, mMedecin);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showCreateRdvDialog(LocalDateTime dateRdv, Medecin medecin) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/dialog/CreateRdvDialog.fxml"));
            Pane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Créer un RdV");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CreateRdvDialogCtrl controller = loader.getController();
            controller.setDialogStage(dialogStage, mPatient, mRdv, mMedecin, dateRdv, medecin);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Show "Profil" Dialog
    public void showProfilPatientDialog(Patient p){
    	try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/dialog/ProfilPatientDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Profil");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the patient into the controller.
            ProfilPatientDialogCtrl controller = loader.getController();
            controller.setDialogStage(dialogStage, this, mPatient, p);
            
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showProfilMedecinDialog(Medecin m){
    	try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/dialog/ProfilMedecinDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Profil");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the medecin into the controller.
            ProfilMedecinDialogCtrl controller = loader.getController();
            controller.setDialogStage(dialogStage, mMedecin, m);
            
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showEditRdvDialog(Rdv rdv, ProfilPatientDialogCtrl patientCtrl){
    	try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/dialog/EditRdvDialog.fxml"));
            Pane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("RdV");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the RdV into the controller.
            EditRdvDialogCtrl controller = loader.getController();
            controller.setDialogStage(dialogStage, rdv, mMedecin, mRdv, this, patientCtrl);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Show "Payment" Dialog
    public void showPaiementDialog(Rdv rdv){
    	try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/dialog/PaiementDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Paiement");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the RdV into the controller.
            PaiementDialogCtrl controller = loader.getController();
		    controller.setDialogStage(dialogStage, rdv);

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
