package sample;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class SourceDir {

    //Singleton
    private static SourceDir instance;

    public static SourceDir getInstance() {
        if (instance == null) {
            instance = new SourceDir();
        }
        return instance;
    }
    // end of Singleton
    String path = "";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void initFromComandLine() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Put path to source dir ");
        path = sc.nextLine();
    }
}
