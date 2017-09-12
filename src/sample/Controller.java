package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    public TextField sourceTextField;
    public TextField destTextField;
    @FXML
    private Label lbResult;

    public void getStart(ActionEvent actionEvent) {

        String wayToSource = sourceTextField.getText().replace("\\", "\\\\");
        String wayToDest = destTextField.getText().replace("\\", "\\\\");
        SyncService myService = new SyncService();
        myService.setDest(wayToDest);
        SourceDir srsDr = SourceDir.getInstance();
        srsDr.setPath(wayToSource);
        myService.startService();

        Thread firstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                myService.startService();
            }
        });

        Thread secondThread = new Thread(new Runnable() {
            @Override
            public void run() {
                myService.startService();

            }
        });
        firstThread.start();
        secondThread.start();

    }

    public void getStop(ActionEvent actionEvent) {
    }
}
