package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import model.entities.*;
import model.managers.StockManager;
import utils.CompanyMapperImpl;
import utils.ShareMapperImpl;
import utils.UserMapperImpl;

public class DedicatedServer extends Thread {
    private static final String BUY_ACTION = "BUY";
    private static final String SELL_ACTION = "SELL";
    private boolean isOn;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket sClient;
    private StockManager stockModel;
    private UserMapperImpl mapper;
    private CompanyMapperImpl companyMapper;
    private ShareMapperImpl shareMapper;

    /**
     * DedicatedServer constructor
     *
     * @param sClient client socket
     */
    public DedicatedServer(Socket sClient) {
        this.sClient = sClient;
        this.stockModel = new StockManager();
        this.mapper = new UserMapperImpl();
        this.companyMapper = new CompanyMapperImpl();
        this.shareMapper = new ShareMapperImpl();
    }

    /**
     * Stops the connection to the server
     */
    public void stopServerConnection() {
        this.isOn = false;
        this.interrupt();
    }

    /**
     * Starts the connection to the server
     */
    public void startServerConnection() {
        this.isOn = true;
        this.start();
    }

    /**
     * Main dedicated server thread that sends and receives information to and from the client side. It handles
     * each object received.
     */
    public void run() {
        try {
            // Create the communication channels
            ois = new ObjectInputStream(sClient.getInputStream());
            oos = new ObjectOutputStream(sClient.getOutputStream());

            while (isOn) {
                TunnelObject tunnelObject = (TunnelObject) ois.readObject();

                if (tunnelObject instanceof AuthenticationInfo) {
                    AuthenticationInfo info = ((AuthenticationInfo) tunnelObject);
                    User user = mapper.authenticationInfoToUser((AuthenticationInfo) tunnelObject);
                    // Check if the object is for registering users
                    if (info.getAction().equals("register")) {
                        AuthenticationInfo authInfoRegister = stockModel.registerUser(user);
                        oos.writeObject(authInfoRegister);
                    } else {
                        // Check if we need user validation for login
                        if (info.getAction().equals("login")) {
                            AuthenticationInfo authInfoLogin = stockModel.validateUser(user);
                            oos.writeObject(authInfoLogin);
                        }
                    }
                }
                // Check if the object is for updating the users balance or description
                if (tunnelObject instanceof UserProfileInfo) {
                    UserProfileInfo userInfo = ((UserProfileInfo) tunnelObject);
                    User user = mapper.userProfileInfoToUser((UserProfileInfo) tunnelObject);
                    if (userInfo.getAction().equals("balance")) {
                        UserProfileInfo userProfileInfo = stockModel.updateUserBalance(user);
                        oos.writeObject(userProfileInfo);
                    } else {
                        if (userInfo.getAction().equals("information")) {
                            UserProfileInfo userProfileInfo = stockModel.updateUserInformation(user);
                            oos.writeObject(userProfileInfo);
                        }
                    }
                }

                if (tunnelObject instanceof ShareTrade) {
                    ShareTrade shareTrade = (ShareTrade) tunnelObject;
                    User user = shareMapper.shareTradeToUser(shareTrade);
                    Company company = shareMapper.shareTradeToCompany(shareTrade);
                    if (shareTrade.getActionToDo().equals(BUY_ACTION)) {
                        ShareTrade share = stockModel.createUserCompanyShare(user, company);
                        oos.writeObject(share);
                    } else {
                        if (shareTrade.getActionToDo().equals(SELL_ACTION)) {
                             //stockModel.updateCompanyValue(shareTrade.getCompanyId(), "sell");
                        }
                    }
                }

                if (tunnelObject instanceof CompanyList) {
                    ArrayList<Company> companies = stockModel.getCompanies();
                    CompanyList companyList = companyMapper.convertToCompanyList(companies);
                    oos.writeObject(companyList);
                }

                if (tunnelObject instanceof CompanyChangeList) {
                    ArrayList<CompanyChange> companies = stockModel.getCompaniesChange();
                    CompanyChangeList companyChangeList = companyMapper.convertToCompanyChangeList(companies);
                    oos.writeObject(companyChangeList);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            stopServerConnection();
            System.out.println("Stopped client connection to the server...");
            e.printStackTrace();
        }
    }
}
