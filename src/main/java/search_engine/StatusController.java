package search_engine;

import javafx.application.Platform;
import javafx.scene.control.Label;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

class StatusController {

    @Getter
    private static List<String> statusMsg = new ArrayList<>();

    static void ResetStatus(Label status) {
        Platform.runLater(() -> {
            if (statusMsg.isEmpty()) {
                status.setText(Config.getSTATUS_MSG() + Config.getREADY_MSG());
            } else {
                StringBuilder tempMSG = new StringBuilder(Config.getSTATUS_MSG());
                statusMsg.forEach(msg -> tempMSG.append(msg).append("\n"));
                System.out.println(tempMSG);
                status.setText(tempMSG.toString());
            }
        });
    }
}
