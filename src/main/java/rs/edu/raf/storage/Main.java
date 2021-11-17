package rs.edu.raf.storage;

import rs.edu.raf.storage.storage_management.FileStorage;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) {

        String impl = args.length>0 ? args[0] : "local";
        System.out.println(impl);
        String className = impl.equals("drive") ? "rs.edu.raf.storage.GoogleDriveAPI" : "rs.edu.raf.storage.LocalFileStorageImplementation";
        System.out.println(className);
        FileStorage fileStorage;
        try {
            fileStorage = (FileStorage) Class.forName(className).getDeclaredConstructor().newInstance();
        }catch (Exception e) {
            e.printStackTrace();
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
