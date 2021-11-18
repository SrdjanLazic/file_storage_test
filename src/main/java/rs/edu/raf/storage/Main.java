package rs.edu.raf.storage;

import rs.edu.raf.storage.enums.Privileges;
import rs.edu.raf.storage.exceptions.*;
import rs.edu.raf.storage.storage_management.FileStorage;
import rs.edu.raf.storage.storage_management.StorageManager;

import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        String impl = args.length>0 ? args[0] : "local";
//        System.out.println(impl);
//        String className = impl.equals("drive") ? "rs.edu.raf.storage.GoogleDriveAPI" : "rs.edu.raf.storage.LocalFileStorageImplementation";
//        System.out.println(className);
//        FileStorage fileStorage;
//        try {
//            fileStorage = (FileStorage) Class.forName(className).getDeclaredConstructor().newInstance();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
        String className = impl.equals("drive") ? "rs.edu.raf.storage.GoogleDriveAPI" : "rs.edu.raf.storage.LocalFileStorageImplementation";

        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // TODO: ispis da li je to postojece skladiste ili pravimo novo

        FileStorage fileStorage = StorageManager.getFileStorage();

        System.out.println("Unesite korisnicko ime:");
        String username = scanner.nextLine();
        System.out.println("Unesite lozinku:");
        String password = scanner.nextLine();

//        if(impl.equals("drive"))
//            System.out.println("Unesite putanju za skladiste:");
//        else
            System.out.println("Unesite putanju korenskog direktorijuma skladista:");

        String storage = scanner.nextLine();

        try {
            fileStorage.initializeStorage(storage, username, password);
        } catch (UserNotFoundException e){
            e.printStackTrace();
        }

        String choice;
        while(true){
            System.out.println("\nIzaberite jednu od opcija upisivanjem zeljenog broja:");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Rad sa korisnicima (admin only)");
            System.out.println("2. Konfiguracija skladista (admin only)");
            System.out.println("3. Operacije nad skladistem");
            System.out.println("4. Izlaz iz programa");

            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    while (true) {
                        System.out.println("\nIzaberite jednu od opcija upisivanjem zeljenog broja:");
                        System.out.println("-----------------------------------------------------");
                        System.out.println("1. Kreiranje novog korisnika");
                        System.out.println("2. Uklanjanje korisnika");
                        System.out.println("3. Odjavljivanje korisnika (logout)");
                        System.out.println("4. Prijavljivanje korisnika (login)");
                        System.out.println("5. Postavljanje privilegija nad datim direktorijumom");
                        System.out.println("6. Nazad");

                        choice = scanner.nextLine();

                        switch (choice) {
                            case "1":
                                System.out.println("Unesite korisnicko ime novog korisnika:");
                                username = scanner.nextLine();
                                System.out.println("Unesite lozinku novog korisnika:");
                                password = scanner.nextLine();
                                // U petlji trazi od korisnika unosenje privilegija
                                while (true) {
                                    System.out.println("Unesite najvisu privilegiju koje zelite da novi korisnik ima. \nPrivilegije su (od najvise ka najnizoj): DELETE, CREATE, DOWNLOAD, VIEW");
                                    String privilege = scanner.nextLine();
                                    if (privilege.equalsIgnoreCase("DELETE")) {
                                        try {
                                            fileStorage.addNewUser(username, password, Set.of(Privileges.DELETE));
                                            System.out.println("Uspesno dodat novi korisnik.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                    } else if (privilege.equalsIgnoreCase("CREATE")) {
                                        try {
                                            fileStorage.addNewUser(username, password, Set.of(Privileges.CREATE));
                                            System.out.println("Uspesno dodat novi korisnik.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("DOWNLOAD")) {
                                        try {
                                            fileStorage.addNewUser(username, password, Set.of(Privileges.DOWNLOAD));
                                            System.out.println("Uspesno dodat novi korisnik.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("VIEW")) {
                                        try {
                                            fileStorage.addNewUser(username, password, Set.of(Privileges.VIEW));
                                            System.out.println("Uspesno dodat novi korisnik.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else {
                                        System.out.println("\nNiste uneli validnu privilegiju. Pokusajte ponovo.\n");
                                    }
                                }

                                break;

                            case "2":

                                System.out.println("Unesite korisnicko ime korisnika kojeg zelite da uklonite:");
                                username = scanner.nextLine();
                                try {
                                    fileStorage.removeUser(username);
                                    System.out.println("Uspesno uklonjen korisnik.");
                                    break;
                                } catch (UserNotFoundException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;

                            case "3":
                                try {
                                    fileStorage.logout();
                                    System.out.println("Uspesno izlogovan trenutni korisnik.");
                                }catch (CurrentUserIsNullException e){
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }
                                break;

                            case "4":
                                System.out.println("Unesite vase korisnicko ime:");
                                username = scanner.nextLine();
                                System.out.println("Unesite vasu lozinku:");
                                password = scanner.nextLine();
                                try {
                                    fileStorage.login(username, password);
                                    System.out.println("\nKorisnik uspesno prijavljen.");
                                    break;
                                } catch (UserAlreadyLoggedInException | UserNotFoundException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;

                            case "5":
                                System.out.println("Unesite korisnicko ime korisnika kom zelite postaviti privilegiju na nekom direktorijumu:");
                                username = scanner.nextLine();
                                if(impl.equals("drive")) {
                                    System.out.println("Unesite naziv direktorijuma gde zelite postaviti privilegiju:");
                                } else {
                                    System.out.println("Unesite putanju korenskog direktorijuma skladista:");
                                }
                                // U petlji trazi od korisnika unosenje privilegija
                                while (true) {
                                    System.out.println("Unesite najvisu privilegiju koje zelite da novi korisnik ima unutar tog direktorijuma. \nPrivilegije su (od najvise ka najnizoj): DELETE, CREATE, DOWNLOAD, VIEW");
                                    String privilege = scanner.nextLine();
                                    if (privilege.equalsIgnoreCase("DELETE")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, password, Set.of(Privileges.DELETE));
                                            System.out.println("Uspesno dodata privilegija unetom korisniku na odabranom direktorijumu.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                    } else if (privilege.equalsIgnoreCase("CREATE")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, password, Set.of(Privileges.CREATE));
                                            System.out.println("Uspesno dodata privilegija unetom korisniku na odabranom direktorijumu.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("DOWNLOAD")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, password, Set.of(Privileges.DOWNLOAD));
                                            System.out.println("Uspesno dodata privilegija unetom korisniku na odabranom direktorijumu.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("VIEW")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, password, Set.of(Privileges.VIEW));
                                            System.out.println("Uspesno dodata privilegija unetom korisniku na odabranom direktorijumu.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else {
                                        System.out.println("\nNiste uneli validnu privilegiju. Pokusajte ponovo.\n");
                                    }
                                }
                                break;

                            case "6":
                                break;

                            default:
                                System.out.println("\nNiste uneli validan broj. Pokusajte ponovo.");
                        }
                        break;
                    }
                    break;

                case "2":
                    while (true) {
                        System.out.println("\nIzaberite jednu od opcija upisivanjem zeljenog broja:");
                        System.out.println("-----------------------------------------------------");
                        System.out.println("1. Ogranicavanje ekstenzije na nivou skladista");
                        System.out.println("2. Ogranicavanje velicine skladista");
                        System.out.println("3. Ogranicavanje broja fajlova u folderu");
                        System.out.println("4. Nazad");

                        choice = scanner.nextLine();
                        switch (choice) {
                            case "1":
                                System.out.println("\nUnesite ekstenziju koju zelite da zabranite u skladistu:");
                                String extension = scanner.nextLine();
                                try {
                                    fileStorage.restrictExtension(extension);
                                    System.out.println("\nZabrana ekstenzije uspesno dodata.");
                                } catch (InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n"+e.getMessage()+"\n-----------------------------------\n");
                                    break;
                                }
                                break;
                            case "2":
                                System.out.println("\nUnesite velicinu u bajtovima na koju zelite da ogranicite velicinu skladista:");
                                String size = scanner.nextLine();
                                try {
                                    fileStorage.limitStorageSize(Long.parseLong(size));
                                    System.out.println("\nVelicina skladista uspesno ogranicena.");
                                } catch (InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n"+e.getMessage()+"\n-----------------------------------\n");
                                    break;
                                }
                                break;
                            case "3":
                                if(impl.equalsIgnoreCase("drive"))
                                    System.out.println("\nUnesite naziv foldera koji zelite da ogranicite:");
                                else
                                    System.out.println("\nUnesite relativnu putanju do foldera koji zelite da ogranicite:");
                                String path = scanner.nextLine();
                                System.out.println("\nUnesite broj fajlova na koji zelite da ogranicite folder:");
                                String numberOfFiles = scanner.nextLine();
                                try {
                                    fileStorage.limitNumberOfFiles(Integer.parseInt(numberOfFiles), path);
                                    System.out.println("\nBroj fajlova uspesno ogranicen");
                                } catch (InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n"+e.getMessage()+"\n-----------------------------------\n");
                                    break;
                                }
                                break;
                            case "4":
                                break;
                        }
                        break;
                    }
                    break;

                case "3":
                    while (true) {
                        System.out.println("\nIzaberite jednu od opcija upisivanjem zeljenog broja:");
                        System.out.println("-----------------------------------------------------");
                        System.out.println("1. Kreiranje novog direktorijuma direktno na skladistu");
                        System.out.println("2. Kreiranje novog direktorijuma na zeljenoj putanji");
                        System.out.println("3. Kreiranje novog fajla direktno na skladistu");
                        System.out.println("4. Kreiranje novog fajla na zeljenoj putanji");
                        System.out.println("5. Brisanje fajla iz skladista");
                        System.out.println("6. Preuzimanje fajla iz skladista na lokalni download folder za skladiste");
                        System.out.println("7. Premestanje fajla iz skladista sa jednog mesta na drugo");
                        System.out.println("8. Smestanje fajla u skladiste");
                        System.out.println("9. Pregled svih fajlova iz odredjenog direktorijuma ");
                        System.out.println("10. Napredni pregled svih fajlova iz odredjenog direktorijuma ");

                        choice = scanner.nextLine();

                        switch (choice) {
                            case "1":
                                System.out.println("\nUnesite naziv direktorijuma za kreiranje:");
                                String folderName = scanner.nextLine();
                                try {
                                    fileStorage.createFolder(folderName);
                                    System.out.println("\nUspesno kreiran direktorijum.");
                                    break;
                                } catch (InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }
                                break;

                            case "2":
                                String destinationFolderName;
                                if(impl.equals("drive")) {
                                    System.out.println("\nUnesite naziv destinacionog direktorijuma:");
                                    destinationFolderName = scanner.nextLine();
                                } else {
                                    System.out.println("Unesite putanju destinacionog direktorijuma:");
                                    destinationFolderName = scanner.nextLine();
                                }
                                System.out.println("\nUnesite naziv jednog ili vise direktorijuma da kreirate na odabranoj lokaciji:");
                                String naziviFoldera = scanner.nextLine();
                                String[] stringovi = naziviFoldera.split(",");
                                for(int i = 0; i < stringovi.length; i++) {
                                    stringovi[i] = stringovi[i].replace(" ", "");
                                }
                                try {
                                    fileStorage.createFolder(destinationFolderName, stringovi);
                                    System.out.println("\nUspesno kreirani direktorijumi na zadatoj putanji.");
                                    break;
                                } catch (UserNotFoundException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;

                            case "3":
                                try {
                                    fileStorage.logout();
                                    System.out.println("Uspesno izlogovan trenutni korisnik.");
                                }catch (CurrentUserIsNullException e){
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }
                                break;

                            case "4":
                                String targetFolderName;
                                if(impl.equals("drive")) {
                                    System.out.println("\nUnesite naziv destinacionog direktorijuma:");
                                    targetFolderName = scanner.nextLine();
                                } else {
                                    System.out.println("Unesite putanju destinacionog direktorijuma:");
                                    targetFolderName = scanner.nextLine();
                                }
                                System.out.println("\nUnesite naziv jednog ili vise fajla da kreirate na odabranoj lokaciji:");
                                String naziviFajlova = scanner.nextLine();
                                String[] strings = naziviFajlova.split(",");
                                for(int i = 0; i < strings.length; i++) {
                                    strings[i] = strings[i].replace(" ", "");
                                }
                                try {
                                    fileStorage.createFolder(targetFolderName, strings);
                                    System.out.println("\nUspesno kreirani fajlovi na zadatoj putanji.");
                                    break;
                                } catch (UserNotFoundException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;

                            case "5":
                                System.out.println("Unesite korisnicko ime korisnika kom zelite postaviti privilegiju na nekom direktorijumu:");
                                username = scanner.nextLine();
                                if(impl.equals("drive")) {
                                    System.out.println("Unesite naziv direktorijuma gde zelite postaviti privilegiju:");
                                } else {
                                    System.out.println("Unesite putanju korenskog direktorijuma skladista:");
                                }
                                // U petlji trazi od korisnika unosenje privilegija
                                while (true) {
                                    System.out.println("Unesite najvisu privilegiju koje zelite da novi korisnik ima unutar tog direktorijuma. \nPrivilegije su (od najvise ka najnizoj): DELETE, CREATE, DOWNLOAD, VIEW");
                                    String privilege = scanner.nextLine();
                                    if (privilege.equalsIgnoreCase("DELETE")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, password, Set.of(Privileges.DELETE));
                                            System.out.println("Uspesno dodata privilegija unetom korisniku na odabranom direktorijumu.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                    } else if (privilege.equalsIgnoreCase("CREATE")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, password, Set.of(Privileges.CREATE));
                                            System.out.println("Uspesno dodata privilegija unetom korisniku na odabranom direktorijumu.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("DOWNLOAD")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, password, Set.of(Privileges.DOWNLOAD));
                                            System.out.println("Uspesno dodata privilegija unetom korisniku na odabranom direktorijumu.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("VIEW")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, password, Set.of(Privileges.VIEW));
                                            System.out.println("Uspesno dodata privilegija unetom korisniku na odabranom direktorijumu.");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else {
                                        System.out.println("\nNiste uneli validnu privilegiju. Pokusajte ponovo.\n");
                                    }
                                }
                                break;

                            case "6":
                                break;

                            default:
                                System.out.println("\nNiste uneli validan broj. Pokusajte ponovo.");
                        }
                        break;
                    }
                    break;

                case "4":
                    System.out.println("Izlazenje iz programa...\n Dovidjenja!");
                    System.exit(0);
                default:
                    System.out.println("\nNiste uneli validan broj. Pokusajte ponovo.");
                    break;
            }
        }




//        fileStorage.createFile();
        //fileStorage.createFolder("D:","sasfagg{1..20}");
//        User user = new User("blabla", "123");
//        StorageModel storageModel = new StorageModel(user, "D:/storage1");
//
//        FileStorage fileStorage = new LocalFileStorageImplementation(storageModel);
//
//        storageModel.getUnsupportedExtensions().add(".txt");


//        fileStorage.createFolder("download");
//        fileStorage.createFile("/download", "fajl.txt");
        //fileStorage.createFile("tekstneki.txt");
        //Scanner input =  new Scanner(System.in);
        //String command = input.nextLine();

        //fileStorage.list();

        //fileStorage.move("D:/skladiste/hello.txt", "D:/");

        //fileStorage.list(".txt", Operations.FILTER_EXTENSION);

        //fileStorage.list("fajl", Operations.FILTER_FILENAME);
//        fileStorage.list(null, Operations.SORT_BY_DATE_MODIFIED_ASC);
//        fileStorage.list(null, Operations.SORT_BY_DATE_MODIFIED_DESC);
//        fileStorage.list(null, Operations.SORT_BY_NAME_ASC);
//        fileStorage.list(null, Operations.SORT_BY_NAME_DESC);

        // FolderModel folderModel = ...
        // folderModel.getFile().getParentFile();




//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            objectMapper.writeValueAsString(fileStorage);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }


//        while (true){
//            System.out.println("Izaberite jednu od opcija:\n");
//            System.out.println("1. Kreiranje direktorijuma i praznih fajlova");
//            System.out.println("2. Smestanje jednog ili vise fajlova");
//            System.out.println("3. Brisanje direktorijuma i fajlova iz skladista");
//            System.out.println("4. Pregled sadrzaja skladista");
//            System.out.println("5. Premestanje fajlova iz jednog direktorijuma u drugi");
//            System.out.println("6. Preuzimanje fajlova iz skladista\n");
//            System.out.println("Unesi broj opcije:\n");
//
//            String option = input.nextLine();
//            switch (option){
//                case "4":
//                    System.out.println("Izaberite jednu od opcija:\n");
//                    System.out.println("1. Vrati sve nazive fajlova u direktorijumu");
//
//
//            }
//
//        }
    }

}
