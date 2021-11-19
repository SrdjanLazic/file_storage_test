package rs.edu.raf.storage;

import rs.edu.raf.storage.enums.Operations;
import rs.edu.raf.storage.enums.Privileges;
import rs.edu.raf.storage.exceptions.*;
import rs.edu.raf.storage.storage_management.FileStorage;
import rs.edu.raf.storage.storage_management.StorageManager;

import java.util.Collection;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        String impl = args.length>0 ? args[0] : "local";
        String className = impl.equals("drive") ? "rs.edu.raf.storage.GoogleDriveAPI" : "rs.edu.raf.storage.LocalFileStorageImplementation";

        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        FileStorage fileStorage = StorageManager.getFileStorage();
        while(true) {
            System.out.println("\nUnesite korisnicko ime:");
            String username = scanner.nextLine();
            System.out.println("\nUnesite lozinku:");
            String password = scanner.nextLine();
            System.out.println("\nUnesite putanju korenskog direktorijuma skladista:");

            String storage = scanner.nextLine();

            try {
                fileStorage.initializeStorage(storage, username, password);
                break;
            } catch (UserNotFoundException e) {
                System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");

            }
        }

        String username;
        String password = null;
        String choice;
        while(true){
            System.out.println("\nIzaberite jednu od opcija upisivanjem zeljenog broja:");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Rad sa korisnicima");
            System.out.println("2. Konfiguracija skladista (admin only)");
            System.out.println("3. Operacije nad skladistem");
            System.out.println("4. Izlaz iz programa");

            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    while (true) {
                        System.out.println("\nIzaberite jednu od opcija upisivanjem zeljenog broja:");
                        System.out.println("-----------------------------------------------------");
                        System.out.println("1. Kreiranje novog korisnika (admin only)");
                        System.out.println("2. Uklanjanje korisnika (admin only)");
                        System.out.println("3. Odjavljivanje korisnika - logout");
                        System.out.println("4. Prijavljivanje korisnika - login");
                        System.out.println("5. Postavljanje privilegija nad datim direktorijumom (admin only)");
                        System.out.println("6. Nazad");

                        choice = scanner.nextLine();

                        switch (choice) {
                            case "1":
                                System.out.println("\nUnesite korisnicko ime novog korisnika:");
                                username = scanner.nextLine();
                                System.out.println("\nUnesite lozinku novog korisnika:");
                                password = scanner.nextLine();
                                // U petlji trazi od korisnika unosenje privilegija
                                while (true) {
                                    System.out.println("\nUnesite najvisu privilegiju koje zelite da novi korisnik ima. \nPrivilegije su (od najvise ka najnizoj): DELETE, CREATE, DOWNLOAD, VIEW");
                                    String privilege = scanner.nextLine();
                                    if (privilege.equalsIgnoreCase("DELETE")) {
                                        try {
                                            fileStorage.addNewUser(username, password, Set.of(Privileges.DELETE));
                                            System.out.println("\n\nUspesno dodat novi korisnik.\n\n");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("CREATE")) {
                                        try {
                                            fileStorage.addNewUser(username, password, Set.of(Privileges.CREATE));
                                            System.out.println("\n\nUspesno dodat novi korisnik.\n\n");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("DOWNLOAD")) {
                                        try {
                                            fileStorage.addNewUser(username, password, Set.of(Privileges.DOWNLOAD));
                                            System.out.println("\n\nUspesno dodat novi korisnik.\n\n");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("VIEW")) {
                                        try {
                                            fileStorage.addNewUser(username, password, Set.of(Privileges.VIEW));
                                            System.out.println("\n\nUspesno dodat novi korisnik.\n\n");
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

                                System.out.println("\nKorisnicko ime korisnika kojeg zelite da uklonite:");
                                username = scanner.nextLine();
                                try {
                                    fileStorage.removeUser(username);
                                    System.out.println("\n\nUspesno uklonjen korisnik.\n\n");
                                    break;
                                } catch (UserNotFoundException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;

                            case "3":
                                try {
                                    fileStorage.logout();
                                    System.out.println("\n\nUspesno izlogovan trenutni korisnik.\n\n");
                                }catch (CurrentUserIsNullException e){
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }
                                break;

                            case "4":
                                System.out.println("\nUnesite vase korisnicko ime:");
                                username = scanner.nextLine();
                                System.out.println("\nUnesite vasu lozinku:");
                                password = scanner.nextLine();
                                try {
                                    fileStorage.login(username, password);
                                    System.out.println("\n\nKorisnik uspesno prijavljen.\n\n");
                                    break;
                                } catch (UserAlreadyLoggedInException | UserNotFoundException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;

                            case "5":
                                System.out.println("\nUnesite korisnicko ime korisnika kom zelite postaviti privilegiju na nekom direktorijumu:");
                                username = scanner.nextLine();
                                if(impl.equals("drive")) {
                                    System.out.println("\nUnesite naziv direktorijuma gde zelite postaviti privilegiju:");
                                } else {
                                    System.out.println("\nUnesite relativnu putanju u skladistu:");
                                }
                                String path = scanner.nextLine();
                                // U petlji trazi od korisnika unosenje privilegija
                                while (true) {
                                    System.out.println("\nUnesite najvisu privilegiju koju zelite da novi korisnik ima unutar tog direktorijuma. \n\nPrivilegije su (od najvise ka najnizoj): DELETE, CREATE, DOWNLOAD, VIEW");
                                    String privilege = scanner.nextLine();
                                    if (privilege.equalsIgnoreCase("DELETE")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, path, Set.of(Privileges.DELETE));
                                            System.out.println("\n\nUspesno dodata privilegija unetom korisniku na odabranom direktorijumu.\n\n");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("CREATE")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, path, Set.of(Privileges.CREATE));
                                            System.out.println("\n\nUspesno dodata privilegija unetom korisniku na odabranom direktorijumu.\n\n");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("DOWNLOAD")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, path, Set.of(Privileges.DOWNLOAD));
                                            System.out.println("\n\nUspesno dodata privilegija unetom korisniku na odabranom direktorijumu.\n\n");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                            break;
                                        }
                                        break;
                                    } else if (privilege.equalsIgnoreCase("VIEW")) {
                                        try {
                                            fileStorage.setFolderPrivileges(username, path, Set.of(Privileges.VIEW));
                                            System.out.println("\n\nUspesno dodata privilegija unetom korisniku na odabranom direktorijumu.\n\n");
                                        } catch (UserAlreadyExistsException | InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
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
                                    System.out.println("\n\nZabrana ekstenzije uspesno dodata.\n\n");
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
                                    System.out.println("\n\nVelicina skladista uspesno ogranicena.\n\n");
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
                                    System.out.println("\n\nBroj fajlova uspesno ogranicen\n\n");
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
                        System.out.println("5. Brisanje fajla ili direktorijuma iz skladista");
                        System.out.println("6. Preuzimanje fajla iz skladista na lokalni download folder za skladiste");
                        System.out.println("7. Premestanje fajla u okviru skladista");
                        System.out.println("8. Smestanje fajla u skladiste");
                        System.out.println("9. Pregled svih fajlova iz odredjenog direktorijuma ");
                        System.out.println("10. Napredni pregled svih fajlova iz odredjenog direktorijuma ");
                        System.out.println("11. Nazad");

                        choice = scanner.nextLine();

                        switch (choice) {
                            case "1":
                                System.out.println("\nUnesite naziv direktorijuma za kreiranje:");
                                String folderName = scanner.nextLine();
                                try {
                                    fileStorage.createFolder(folderName);
                                    System.out.println("\n\nUspesno kreiran direktorijum.\n\n");
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
                                    System.out.println("\nUnesite relativnu putanju destinacionog direktorijuma:");
                                    destinationFolderName = scanner.nextLine();
                                }
                                System.out.println("\nUnesite naziv jednog ili vise direktorijuma koje zeliteda kreirate na odabranoj lokaciji koristeci zareze da ih razdvajate:");
                                String naziviFoldera = scanner.nextLine();
                                String[] stringovi = naziviFoldera.split(",");
                                for(int i = 0; i < stringovi.length; i++) {
                                    stringovi[i] = stringovi[i].replace(" ", "");
                                }
                                try {
                                    fileStorage.createFolder(destinationFolderName, stringovi);
                                    System.out.println("\n\nUspesno kreirani direktorijumi na zadatoj putanji.\n\n");
                                    break;
                                } catch (UserNotFoundException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;

                            case "3":
                                System.out.println("\nUnesite naziv fajla:");
                                String fileName = scanner.nextLine();
                                try {
                                    fileStorage.createFile(fileName);
                                    System.out.println("\n\nUspesno kreiran fajl.\n\n");
                                    break;
                                } catch (InsufficientPrivilegesException | InvalidExtensionException | FileLimitExceededException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }
                                break;

                            case "4":
                                String targetFolderName;
                                if(impl.equals("drive")) {
                                    System.out.println("\nUnesite naziv destinacionog direktorijuma:");
                                    targetFolderName = scanner.nextLine();
                                } else {
                                    System.out.println("\nUnesite relativnu putanju destinacionog direktorijuma:");
                                    targetFolderName = scanner.nextLine();
                                }
                                System.out.println("\nUnesite naziv jednog ili vise fajla koje zelite da kreirate na odabranoj lokaciji koristeci zareze da ih razdvajate:");
                                String naziviFajlova = scanner.nextLine();
                                String[] strings = naziviFajlova.split(",");
                                for(int i = 0; i < strings.length; i++) {
                                    strings[i] = strings[i].replace(" ", "");
                                }
                                try {
                                    fileStorage.createFile(targetFolderName, strings);
                                    System.out.println("\n\nUspesno kreirani fajlovi na zadatoj putanji.\n\n");
                                    break;
                                } catch (InsufficientPrivilegesException | InvalidExtensionException | FileNotFoundException | FileLimitExceededException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;

                            case "5":
                                if(impl.equals("drive")){
                                    System.out.println("\nUnesite ime fajla ili direktorijuma koji zelite da obrisete:");
                                }else {
                                    System.out.println("\nUnesite relativne putanje do direktorijuma ili fajlove koje zelite da obrisete (jedan ili vise), odvojene zarezom:");
                                }
                                String fileNames = scanner.nextLine();
                                String[] paths = fileNames.split(",");
                                for(int i = 0; i < paths.length; i++) {
                                    paths[i] = paths[i].replace(" ", "");
                                }
                                try {
                                    if(fileNames.contains(","))
                                        fileStorage.delete(paths);
                                    else
                                        fileStorage.delete(fileNames);
                                    System.out.println("\n\nBrisanje uspesno obavljeno.\n\n");
                                    break;
                                } catch (FileNotFoundException | InsufficientPrivilegesException | FileDeleteFailedException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }
                                break;

                            case "6":
                                if(impl.equals("drive")){
                                    System.out.println("\nUnesite ime fajla ili direktorijuma (jedan ili vise) koji zelide da preuzmete sa skladista, odvojite imena zarezima ukoliko ih ima vise:");
                                } else {
                                    System.out.println("\nUnesite relativne putanje do direktorijuma ili fajla (jedan ili vise) koji zelite da preuzmete sa skladista, odvojite putanje zarezima ukoliko ih ima vise:");
                                }
                                String targetFileNames = scanner.nextLine();
                                String[] input = targetFileNames.split(",");
                                for (int i = 0; i < input.length; i++)
                                    input[i] = input[i].replace(" ", "");
                                try {
                                    fileStorage.get(input);
                                    System.out.println("\n\nPreuzimanje uspesno obavljeno.\n\n");
                                    break;
                                } catch (FileNotFoundException | InsufficientPrivilegesException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }
                                break;

                            case "7":
                                String endFolderName;
                                if(impl.equals("drive")) {
                                    System.out.println("\nUnesite naziv destinacionog direktorijuma:");
                                    endFolderName = scanner.nextLine();
                                } else {
                                    System.out.println("\nUnesite putanju destinacionog direktorijuma od korenskog direktorijuma (bez njega):");
                                    endFolderName = scanner.nextLine();
                                }
                                System.out.println("\nUnesite naziv jednog ili vise fajla da premestite na odabranu lokaciju koristeci zareze da ih razdvajate:");
                                String naziviFajlovaPomeranja = scanner.nextLine();
                                String[] ispisi = naziviFajlovaPomeranja.split(",");
                                for(int i = 0; i < ispisi.length; i++) {
                                    ispisi[i] = ispisi[i].replace(" ", "");
                                }
                                try {
                                    fileStorage.move(endFolderName, ispisi);
                                    System.out.println("\n\nUspesno premesteni fajlovi na zadatoj putanji.\n\n");
                                    break;
                                } catch (InsufficientPrivilegesException | OperationFailedException | FileLimitExceededException | FileNotFoundException | StorageSizeExceededException | InvalidExtensionException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;

                            case "8":
                                String uploadFolderName;
                                if(impl.equals("drive")) {
                                    System.out.println("\nUnesite naziv destinacionog direktorijuma:");
                                } else {
                                    System.out.println("\nUnesite putanju destinacionog direktorijuma od korenskog direktorijuma (bez njega):");
                                }
                                uploadFolderName = scanner.nextLine();
                                System.out.println("\nUnesite putanju jednog ili vise fajla da smestite na odabranu lokaciju koristeci zareze da ih razdvajate:");
                                String uploadFajlovi = scanner.nextLine();
                                String[] parsirano = uploadFajlovi.split(",");
                                for(int i = 0; i < parsirano.length; i++) {
                                    parsirano[i] = parsirano[i].replace(" ", "");
                                }
                                try {
                                    fileStorage.put(uploadFolderName, parsirano);
                                    System.out.println("\n\nUspesno smesteni fajlovi na zadatoj putanji.\n\n");
                                    break;
                                } catch (FileAlreadyInStorageException | OperationFailedException | FileNotFoundException | FileLimitExceededException | InsufficientPrivilegesException | InvalidExtensionException | StorageSizeExceededException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }

                                break;
                            // TODO nesto se ubaguje kod NE
                            case "9":
                                if(impl.equals("drive")) {
                                    System.out.println("\nUnesite naziv direktorijuma cije fajlove zelite da vidite:");
                                } else {
                                    System.out.println("\nUnesite putanju do foldera ciji sadrzaj zelite da pregledate:");
                                }
                                String listFolder = scanner.nextLine();
                                boolean subCheck;
                                while(true){
                                    System.out.println("\nDa li zelite da vidite i fajlove iz poddirektorijuma? (DA/NE)");
                                    if(scanner.nextLine().equalsIgnoreCase("DA")){
                                        subCheck = true;
                                        break;
                                    }else if(scanner.nextLine().equalsIgnoreCase("NE")) {
                                        subCheck = false;
                                        break;
                                    }else {
                                        System.out.println("\nPogresan unos. Napisite DA ukoliko zelite da vidite i poddirektorijume ili napisite NE ukoliko zelite.\n");
                                    }
                                }
                                try {
                                    Collection<String> lista = fileStorage.list(listFolder, subCheck);
                                    for(String s : lista)
                                        System.out.println(s);
                                    break;
                                } catch (InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                    System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                }
                                break;

                            case "10":
                                Collection<String> result;
                                if(impl.equals("drive")) {
                                    System.out.println("\nUnesite naziv direktorijuma cije fajlove zelite da vidite:");
                                } else {
                                    System.out.println("\nUnesite putanju do foldera ciji sadrzaj zelite da pregledate:");
                                }
                                String advancedListFolder = scanner.nextLine();
                                boolean subSearch;
                                while(true){
                                    System.out.println("\nDa li zelite da vidite i fajlove iz poddirektorijuma? (DA/NE)");
                                    if(scanner.nextLine().equalsIgnoreCase("DA")){
                                        subSearch = true;
                                        break;
                                    }else if(scanner.nextLine().equalsIgnoreCase("NE")) {
                                        subSearch = false;
                                        break;
                                    }else {
                                        System.out.println("\nPogresan unos. Napisite DA ukoliko zelite da vidite i podfajlove ili napisite NE ukoliko zelite.\n");
                                    }
                                }

                                while(true){
                                    System.out.println("\nUnesite jednu od ponudjenih operacija:\n1. Filtriranje ekstenzije\n2. Filtriranje naziva fajla\n3. Sortiranje po nazivu fajla" +
                                    " rastuce\n4. Sortiranje po nazivu fajla opadajuce\n5. Filtriranje po datumu poslednje izmene rastuce\n" +
                                    "6. Filtriranje po datumu poslednje izmene opadajuce\n7. Nazad");
                                    if(impl.equals("drive")){
                                        System.out.println("8. Sortiranje po datumu kreiranja fajla");
                                    }
                                    choice = scanner.nextLine();
                                    String argument;
                                    if(choice.equals("1")){
                                        System.out.println("\nUnesite ekstenziju fajlova koje zelite da pregledate:");
                                        argument = scanner.nextLine();
                                        try {
                                            result = fileStorage.list(advancedListFolder, argument, Operations.FILTER_EXTENSION, subSearch);
                                            for(String s : result)
                                                System.out.println(s);
                                        } catch (InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                        }
                                        break;
                                    } else if(choice.equals("2")){
                                        System.out.println("\nUnesite tekst koji nazivi fajlova treba da sadrze:");
                                        argument = scanner.nextLine();
                                        try {
                                            result = fileStorage.list(advancedListFolder, argument, Operations.FILTER_FILENAME, subSearch);
                                            for(String s : result)
                                                System.out.println(s);
                                        } catch (InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                        }
                                        break;
                                    } else if(choice.equals("3")){
                                        try {
                                            result = fileStorage.list(advancedListFolder, null, Operations.SORT_BY_NAME_ASC, subSearch);
                                            for(String s : result)
                                                System.out.println(s);
                                        } catch (InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                        }
                                        break;
                                    } else if(choice.equals("4")){
                                        try {
                                            result = fileStorage.list(advancedListFolder, null, Operations.SORT_BY_NAME_DESC, subSearch);
                                            for(String s : result)
                                                System.out.println(s);
                                        } catch (InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                        }
                                        break;
                                    } else if(choice.equals("5")){
                                        try {
                                            result = fileStorage.list(advancedListFolder, null, Operations.SORT_BY_DATE_MODIFIED_ASC, subSearch);
                                            for(String s : result)
                                                System.out.println(s);
                                        } catch (InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                        }
                                        break;
                                    } else if(choice.equals("6")){
                                        try {
                                            result = fileStorage.list(advancedListFolder, null, Operations.SORT_BY_DATE_MODIFIED_DESC, subSearch);
                                            for(String s : result)
                                                System.out.println(s);
                                        } catch (InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                        }
                                        break;
                                    } else if(choice.equals("7")){
                                        break;
                                    } else if(choice.equals("8")){
                                        try {
                                            result = fileStorage.list(advancedListFolder, null, Operations.SORT_BY_DATE_CREATED, subSearch);
                                            for(String s : result)
                                                System.out.println(s);
                                        } catch (InsufficientPrivilegesException | FileNotFoundException | CurrentUserIsNullException e) {
                                            System.out.println("\n-----------------------------------\n" + e.getMessage() + "\n-----------------------------------\n");
                                        }
                                    } else {
                                        System.out.println("\nPogresan unos.\n");
                                    }
                                }
                                break;

                            case "11":
                                break;

                            default:
                                System.out.println("\nNiste uneli validan broj. Pokusajte ponovo.\n");
                        }
                        break;
                    }
                    break;

                case "4":
                    System.out.println("\nIzlazenje iz programa...\nDovidjenja!");
                    System.exit(0);
                default:
                    System.out.println("\nNiste uneli validan broj. Pokusajte ponovo.\n");
                    break;
            }
        }

    }

}
