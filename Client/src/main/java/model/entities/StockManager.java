package model.entities;

import java.util.ArrayList;

public class StockManager {
    private User user;
    private Company company;
    private ArrayList<Company> companies;
    private ArrayList<CompanyDetail> companyDetails;
    private ArrayList<CompanyChange> companiesChange;


    public StockManager(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public ArrayList<CompanyDetail> getCompanyDetails() {
        return companyDetails;
    }

    public float getCurrentShareValue() {
        return companyDetails.get(0).getValueClose();
    }

    public float getMaxDetailShareValue() {
        float maxValue = companyDetails.get(0).getValueClose();
        for(int i=1; i<companyDetails.size(); i++){
            if(companyDetails.get(i).getValueClose() > maxValue){
                maxValue = companyDetails.get(i).getValueClose();
            }
        }
        return maxValue;
    }

    public float getMinDetailShareValue() {
        float minValue = companyDetails.get(0).getValueClose();
        for(int i=1; i<companyDetails.size(); i++){
            if(companyDetails.get(i).getValueClose() < minValue){
                minValue = companyDetails.get(i).getValueClose();
            }
        }
        return minValue;
    }

    public float getMinShareValue() {
        return companyDetails.get(0).getValueClose();
    }

    public int getCurrentShareId() {
        return companyDetails.get(0).getShareIdClose();
    }

    public String getCompanyDetailName() {
        return companyDetails.get(0).getCompanyName();
    }

    public int getCompanyDetailId() {
        return companyDetails.get(0).getCompanyId();
    }

    public ArrayList<CompanyChange> getCompaniesChange() {
        return companiesChange;
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public void setCompanyDetails(ArrayList<CompanyDetail> companyDetails) {
        this.companyDetails = companyDetails;
    }

    public void setCompaniesChange(ArrayList<CompanyChange> companiesChange) {
        this.companiesChange = companiesChange;
    }

    public float checkUserBalance (int quantityShares){
        float totalPurchase = quantityShares * getCurrentShareValue();
        float userBalance = user.getTotalBalance() - totalPurchase;
        return userBalance;
    }

    public void updateUserBalance (float newBalance){
        this.user.setTotalBalance(newBalance);
    }
}
