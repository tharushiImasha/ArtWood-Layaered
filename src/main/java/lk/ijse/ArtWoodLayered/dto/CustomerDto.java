package lk.ijse.ArtWoodLayered.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDto {
    private String id;
    private String name;
    private String address;
    private int tel;
}
