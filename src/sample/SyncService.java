package sample;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyncService {
    private long interval = 1000;
    private String dest ;
    private String[] dirDestNames = new String[]{"Video", "TXT", "IMG", "Music", "Other"};
    private boolean btn = true;

    public void setDest(String dest) {
        this.dest = dest;
    }

    /*public void setBtn(boolean btn) {
        this.btn = btn;
    }*/

    public void startService() {
        //initSettings();
        //initDestination();//server
        if (createDirs()) {
            start();
        }
    }

    private void initSettings() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Put sync interval (in seconds): ");
        interval = sc.nextLong() * 1000;
    }

    private void initDestination() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Put path to destination dir ");
        dest = sc.nextLine();

    }

    public void start() {
        while (1 == 1) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
                Logger.getLogger(SyncService.class.getName()).log(Level.SEVERE, null, ex);
            }
            sync(true);
        }
    }

    public void sync(boolean btn) {
        SourceDir sd = SourceDir.getInstance();
        System.out.println("\nBegin " + timeOfFunction() + "\nSource - " + sd.getPath() + " Destination - " + dest);
        File sourceDir = new File(sd.getPath());
        File destDir = new File(dest);
        List<File> filesFromDestDirs = getFilesFromDestDirs(getAllDirs());
        System.out.println("\nSize necessary destenation  - " + filesFromDestDirs.size());
        File[] listSorceFiles = sourceDir.listFiles();
        File[] listAllDestFiles = transferArreyListToList((ArrayList) filesFromDestDirs);
        String[] listAllDestFilesNames = new String[]{};
        HashSet<String> sourceDirHashSet = createHashSet(listSorceFiles);
        HashSet<String> destDirHashSet = createHashSet(listAllDestFiles);
        //System.out.println("\nhashset source = " + sourceDirHashSet);
        //System.out.println("\nhashset dest = " + destDirHashSet);
        sourceDirHashSet.removeAll(destDirHashSet);
        Iterator<String> mySourceDirIterator = sourceDirHashSet.iterator();
        while (mySourceDirIterator.hasNext()) {
            System.out.println("iterator source --- " + mySourceDirIterator.next());
        }
        List<String> list = new ArrayList<>(sourceDirHashSet);//+
        for (String copingFile : list) {
            //list.stream().forEach((String copingFile) -> {
            try {
                String nameOfDirForPut = dirDestNames[findTypeOfSource(getFileExtencion(copingFile)) - 1];
                copy(new File(sd.getPath() + "\\" + copingFile), new File(dest + "\\" + nameOfDirForPut + "\\" + copingFile));
                if (btn = true) {
                    delete(new File(sd.getPath() + "\\" + copingFile));
                }
            } catch (IOException ex) {
                Logger.getLogger(SyncService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ;
    }

    private String timeOfFunction() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public boolean createDirs() {
        System.out.println(dest);
        if (!Files.exists(Paths.get(dest))) {
            System.out.println("Please write correct destination!");
            return false;
        }
        boolean isSuccess = true;
        for (String drNm : dirDestNames) {
            Path path = Paths.get(dest + "\\" + drNm);
            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    isSuccess = false;
                }
            }
        }
        return isSuccess;
    }

    public void copy(File src, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    private File[] getAllDirs() {
        File[] listDestDirSort = new File[dirDestNames.length];
        for (int i = 0; i < dirDestNames.length; i++) {
            listDestDirSort[i] = new File(dest + "\\" + dirDestNames[i]);
        }
        return listDestDirSort;
    }

    private ArrayList<File> getFilesFromDestDirs(File[] listDestDirSort) {
        ArrayList<File> arryListOfAllFiles = new ArrayList<>();
        for (File fl : listDestDirSort) {//перебираєм папку DEST
            for (File flsNms : fl.listFiles()) {//перебираємо кожну папку
                System.out.println("listDestDirSort = " + flsNms.getName());
                arryListOfAllFiles.add(flsNms);
            }
        }
        return arryListOfAllFiles;
    }

    private HashSet<String> createHashSet(File[] listFilesFromAlreadyCreatedDir) {
        HashSet<String> dirHashSet = new HashSet<>();
        for (File newListOfFilisForHashSet : listFilesFromAlreadyCreatedDir) {
            dirHashSet.add(newListOfFilisForHashSet.getName());
        }
        return dirHashSet;
    }

    private File[] transferArreyListToList(ArrayList<File> arrListForTransf) {
        File[] fSorted = arrListForTransf.toArray(new File[arrListForTransf.size()]);
        return fSorted;
    }

    public String getFileExtencion(String name) {
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public int findTypeOfSource(String extencion) {
        int res = 0;
        if (null == extencion) {
            return 0;
        }
        switch (extencion) {
            case "mp4":
            case "avi":
            case "mkv":
                res = 1;
                break;
            case "txt":
            case "docx":
                res = 2;
                break;
            case "jpeg":
            case "img":
            case "bmp":
            case "tiff":
            case "gif":
                res = 3;
                break;
            case "mp3":
            case "AAC":
            case "WMA":
            case "WAV":
                res = 4;
                break;
            default:
                res = 5;
                break;
        }
        return res;
    }

    public void delete(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                delete(subFile);
            }
        }
        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("Could not delete {}" + file);
            }
        }
    }
}
