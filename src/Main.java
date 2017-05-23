import gui.Gui;
import javafx.application.Application;
import metier.hibernate.DataBase;
import metier.hibernate.DataPatient;
import models.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

import static metier.hibernate.DataBase.getSession;

public class Main {

    public static void main(final String[] args) throws Exception {

        Application.launch(Gui.class);

    }
}