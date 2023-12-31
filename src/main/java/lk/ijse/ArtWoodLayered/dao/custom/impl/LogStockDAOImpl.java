package lk.ijse.ArtWoodLayered.dao.custom.impl;

import lk.ijse.ArtWoodLayered.dao.SqlUtil;
import lk.ijse.ArtWoodLayered.dao.custom.LogStockDAO;
import lk.ijse.ArtWoodLayered.dto.LogsDto;
import lk.ijse.ArtWoodLayered.entity.Logs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogStockDAOImpl implements LogStockDAO {
    @Override
    public String generateNextId() throws SQLException{
        ResultSet resultSet = SqlUtil.execute("SELECT logs_id FROM log_stock ORDER BY logs_id DESC LIMIT 1");

        int max = 0;
        while (resultSet.next()){
            String x = resultSet.getString(1);
            String[] y = x.split("L");
            int id = Integer.parseInt(y[1]);

            if (max < id){
                max = id;
            }

        }

        return "L" + ++max;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SqlUtil.execute("DELETE FROM log_stock WHERE logs_id = ?", id);
    }

    @Override
    public List<Logs> getAll() throws SQLException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM log_stock");

        List<Logs> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            Logs entity = new Logs(
                    resultSet.getString("logs_id"),
                    resultSet.getString("wood_type"),
                    resultSet.getInt("logs_amount")
            );
            dtoList.add(entity);
        }
        return dtoList;
    }

    @Override
    public boolean save(Logs entity) throws SQLException {
        return SqlUtil.execute("INSERT INTO log_stock VALUES(?, ?, ?)", entity.getLogs_id(), entity.getWood_type(), entity.getLog_amount());
    }

    @Override
    public boolean update(Logs entity) throws SQLException {
        return SqlUtil.execute("UPDATE log_stock SET wood_type = ?, logs_amount = ? WHERE logs_id = ?", entity.getWood_type(), entity.getLog_amount(), entity.getLogs_id());
    }

    @Override
    public Logs search(String id) throws SQLException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM log_stock WHERE logs_id = ?", id);

        Logs dto = null;

        if(resultSet.next()) {
            String log_id = resultSet.getString(1);
            String log_type = resultSet.getString(2);
            int log_amount = Integer.parseInt(resultSet.getString(3));

            dto = new Logs(log_id, log_type, log_amount);
        }

        return dto;
    }

    public int getLogCount() throws SQLException {
        ResultSet resultSet = SqlUtil.execute("SELECT SUM(logs_amount) FROM log_stock");

        int amount = 0;

        if (resultSet.next()){
            amount = resultSet.getInt(1);
        }
        return amount;
    }

    @Override
    public List<LogsDto> getAllLogsForCombo() throws SQLException {
        ResultSet resultSet = SqlUtil.execute("SELECT * FROM log_stock WHERE logs_amount = 1");

        List<LogsDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            String log_id = resultSet.getString(1);
            String wood_type = resultSet.getString(2);
            int log_amount = Integer.parseInt(resultSet.getString(3));

            var dto = new LogsDto(log_id, wood_type, log_amount);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public boolean reduce(String log_id) throws SQLException {
        return SqlUtil.execute("update log_stock set logs_amount = 0 where logs_id = ?", log_id);
    }

}
