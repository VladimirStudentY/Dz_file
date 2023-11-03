import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    GameProgress gameProgress;
    String[] dir = {"src", "res", "savegames", "temp"};
    String[] arrayFile = {"Main.java", "Utils.java", "temp.txt"};
    String[] subcatalogs = {"main", "test", "drawables", "vectors", "icons"};
    int index = 0;
    StringBuilder textLog = new StringBuilder(new Date() + "\n");
    ArrayList<String> filesInDir = new ArrayList<>();

    public Main() throws IOException {
        initGames();
        gamerecording();
        fileDelete("D:/Games/savegames/", "dat");
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

    // ---------------------
    private void initGames() {
        createDir(dir, subcatalogs);
        fileCreate(arrayFile);
        textLog.append("Инициализация завершена .");
        System.out.println(textLog);
        logSave(textLog.toString());
    }

    // ---------------------
    private void gamerecording() throws IOException {
        saveGame("D:/Games/savegames/", 350, 100, 5, 300.25);
        saveGame("D:/Games/savegames/", 400, 85, 6, 350.25);
        saveGame("D:/Games/savegames/", 250, 65, 6, 150.5);
        logSave(textLog.toString());
        // allFilesInDir("D:/Games/savegames/");
        zipFilesAll("D:/Games/savegames/");
    }

    // ---------------------
    private void createDir(String[] array, String[] subcatalogs) {

        for (String tempArray : array) {
            createKatalog("D:/Games" + "/" + tempArray);
        }
        for (int i = 0; i < subcatalogs.length; i++) {
            String pathDir = (i < 2) ? "D:/Games/src/" + subcatalogs[i] : "D:/Games/res/" + subcatalogs[i];
            createKatalog(pathDir);
        }
    }

    private boolean createKatalog(String dirPath) {
        File dir = new File(dirPath);
        if (dir.mkdir()) {
            textLog.append("\tКаталог " + dirPath + " \tсоздан\n");
            return true;
        }
        return false;
    }

    // ---------------------
    private void fileCreate(String[] arrayFile) {
        for (int i = 0; i < arrayFile.length - 1; i++) {
            String pathFile = (i < arrayFile.length - 1 ? "D:/Games/src/main/" + arrayFile[i] : "D:/Games/temp/" + arrayFile[i]);
            fileCreate(pathFile);
        }
    }

    private void fileCreate(String name) {
        File newFile = new File(name);
        try {
            if (newFile.createNewFile()) {
                textLog.append("\tФайл " + name + "\t создан успешно\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    // ---------------------
    private void logSave(String logText) {
        try (FileWriter writer = new FileWriter("D:/Games/temp/temp.txt", true)) {
            writer.write(logText);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    // ********************
    private void saveGame(String path, int health, int weapons, int lvl, double distance) {

        String fileName = (index != 0 ? "save" + index + ".dat" : "save.dat");
        index++;
        gameProgress = new GameProgress(health, weapons, lvl, distance);
        //создаем 2 потока для сериализации объекта и сохранения его в файл
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("D:\\Games\\savegames\\" + fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            // сохраняем игру в файл
            objectOutputStream.writeObject(gameProgress);

            //закрываем поток и освобождаем ресурсы
            objectOutputStream.close();

            File file = new File("D:\\Games\\savegames\\" + fileName);
            if (file.exists()) {
                logSave("\nФайл " + fileName + " успешно записан .");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // *********************
    private void zipFilesAll(String dirPath) throws IOException {
        File fDir = new File(dirPath);
        for (File item : fDir.listFiles()) {
            if (item.isFile())
                filesInDir.add(item.getAbsolutePath());
        }

        FileOutputStream fos = new FileOutputStream(dirPath + "allSaveDat.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (Object item : filesInDir) {
            File fileToZip = new File((String) item);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.
                    putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
    }

    // ********************
    private void fileDelete(String dirPath, String ext) {
        File file = new File(dirPath);
        if (!file.exists()) System.out.println(dirPath + " папка не существует");
        File[] listFiles = file.listFiles(new MyFileNameFilter(ext));
        if (listFiles.length == 0) {
            System.out.println(dirPath + " не содержит файлов с расширением " + ext);
        } else {
            for (File f : listFiles)
                System.out.println("Файл: " + dirPath + File.separator + f.getName() + " " + f.getAbsolutePath());

//            if(del.delete())
//            {
//                System.out.println("File deleted successfully");
//            }
//            else
//            {
//                System.out.println("Failed to delete the file");
//            }

        }
    }

    // ********************

}

