package lk.ijse.ArtWoodLayered.bo.custom;

import lk.ijse.ArtWoodLayered.bo.SuperBO;
import lk.ijse.ArtWoodLayered.dto.PendingStockDto;

import java.sql.SQLException;
import java.util.List;

public interface PendingStockBO extends SuperBO {
    String generateNextPendingId() throws SQLException, ClassNotFoundException;

    List<PendingStockDto> getAllPendings() throws SQLException;

    boolean deletePending(String id) throws SQLException;

    int getPendingCount() throws SQLException;

    boolean savePending(PendingStockDto dto) throws SQLException;

    boolean updatePending(PendingStockDto dto) throws SQLException;
}
