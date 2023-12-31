package lk.ijse.ArtWoodLayered.dao;

import lk.ijse.ArtWoodLayered.dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;
    private DAOFactory(){

    }

    public static DAOFactory getDaoFactory(){
        return (daoFactory==null)? daoFactory = new DAOFactory(): daoFactory;
    }

    public enum DaoTypes{
        CUSTOMER, SUPPLIER, EMPLOYEE, PRODUCT_TYPE, LOGS, SUP_ORDER, SUP_ORDER_DETAIL, FINANCE, WOOD_PIECES, PENDING_STOCK, FINISHED_STOCK, SALARY, EDIT_PROFILE, LOGIN, OTHER_SALARY, ORDER, ORDER_DETAIL
    }

    public SuperDAO getDao(DaoTypes daoTypes){
        switch (daoTypes){
            case CUSTOMER:
                return new CustomerDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            case EMPLOYEE:
                return new EmployeeDAOImpl();
            case PRODUCT_TYPE:
                return new ProductTypeDAOImpl();
            case LOGS:
                return new LogStockDAOImpl();
            case SUP_ORDER:
                return new SupOrderDAOImpl();
            case SUP_ORDER_DETAIL:
                return new SupOrderDetailDAOImpl();
            case FINANCE:
                return new FinanceDAOImpl();
            case WOOD_PIECES:
                return new WoodPiecesStockDAOImpl();
            case PENDING_STOCK:
                return new PendingStockDAOImpl();
            case FINISHED_STOCK:
                return new FinishedStockDAOImpl();
            case SALARY:
                return new SalaryDAOImpl();
            case EDIT_PROFILE:
                return new EditProfileDAOImpl();
            case LOGIN:
                return new LoginDAOImpl();
            case OTHER_SALARY:
                return new OtherEmployeeSalaryDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDER_DETAIL:
                return new OrderDetailDAOImpl();
            default:
                return null;
        }

    }
}
