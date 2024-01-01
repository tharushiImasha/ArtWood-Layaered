package lk.ijse.ArtWoodLayered.bo;


import lk.ijse.ArtWoodLayered.bo.custom.impl.*;
import lk.ijse.ArtWoodLayered.dao.custom.impl.SupplierDAOImpl;

public class BOFactory {
    private static BOFactory boFactory ;
    private BOFactory(){

    }

    public static BOFactory getBoFactory(){
        return (boFactory==null)? boFactory = new BOFactory():boFactory ;
    }

    public enum BoTypes{
        CUSTOMER, SUPPLIER, EMPLOYEE, PRODUCT_TYPE, LOGS, SUP_ORDER, PLACE_SUP_ORDER
    }

    public SuperBO getBoo(BoTypes boTypes){
        switch (boTypes){
            case CUSTOMER:
                return new CustomerBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            case EMPLOYEE:
                return new EmployeeBOImpl();
            case PRODUCT_TYPE:
                return new ProductTypeBOImpl();
            case LOGS:
                return new LogStockBOImpl();
            case SUP_ORDER:
                return new SupOrderBOImpl();
            case PLACE_SUP_ORDER:
                return new PlaceSupOrderBOImpl();
            default:
                return null;
        }

    }
}
