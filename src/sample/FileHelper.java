package sample;

import java.io.File;

/**
 *
 * @author User
 */
public class FileHelper extends File {

    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_IMAGE = 3;
    public static final int TYPE_MUSIC = 4;

    private String name;//name of file that we will work with
    protected String fileName = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void delete(File file) {
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

    public FileHelper(String pathname, String name) {
        super(pathname, name);
        this.name = name;
        fileName = pathname + "\\" + name;
        findTypeOfSource(getFileExtencion());
    }

    public String getFileExtencion() {
        String name = this.getName();
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
                break;
        }
        return res;
    }

    public boolean isFileImage() {
        String extencion = getFileExtencion();
        return extencion.equals("jpg") || extencion.equals("bmp") || extencion.equals("tiff") || extencion.equals("gif");
    }

    public boolean isFileText() {
        String extencion = getFileExtencion();
        return extencion.equals("txt") || extencion.equals("docx");
    }

    public boolean isFileVideo() {
        String extencion = getFileExtencion();
        return extencion.equals("mp4") || extencion.equals("avi") || extencion.equals("mkv");
    }

    public boolean isFileMusic() {
        String extencion = getFileExtencion();
        return extencion.equals("mp3") || extencion.equals("AAC") || extencion.equals("WMA") || extencion.equals("WAV");
    }
}
