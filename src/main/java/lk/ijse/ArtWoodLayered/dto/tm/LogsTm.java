package lk.ijse.ArtWoodLayered.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LogsTm {
    private String logs_id;
    private String wood_type;
    private int log_amount;
    private Button btn;
}
