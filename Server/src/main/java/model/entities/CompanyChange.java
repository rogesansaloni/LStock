package model.entities;


public class CompanyChange {

    private int companyId;
    private String name;
    private float currentShare;
    private float change;
    private float changePer;

    public CompanyChange() {
    }

    /**
     * It will create a companyChange
     *
     * @param companyId      company id
     * @param name      company name
     * @param currentShare     current company's share price
     * @param change    difference between current price and the price the company had 5 minutes ago
     * @param changePer      the change value but in % format
     */

    public CompanyChange(int companyId, String name, float currentShare, float change, float changePer) {
        this.companyId = companyId;
        this.name = name;
        this.currentShare = currentShare;
        this.change = change;
        this.changePer = changePer;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCurrentShare() {
        return currentShare;
    }

    public void setCurrentShare(float currentShare) {
        this.currentShare = currentShare;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public float getChangePer() {
        return changePer;
    }

    public void setChangePer(float changePer) {
        this.changePer = changePer;
    }
}


