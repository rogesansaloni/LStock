package database;


import model.entities.Bot;
import model.entities.Company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class BotDao {

    private DBConnector dbConnector;

    public BotDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }


    /**
     * Creates a bot associated to a company
     *
     * @param bot     bot to be created
     * @param company company to which the bot belongs to
     * @return id of the newly created bot. It will return -1 in case there is an error.
     */
    public int createBot(Bot bot, Company company) {
        // Create the new bot for the specified company
        dbConnector.insertQuery("INSERT INTO Bot (company_id, active_time, probability) " +
                "VALUES ('" + company.getCompanyId() + "','" + bot.getActiveTime() + "','" + bot.getProbability() + "')");

        // Get the latest bot created for the specified company
        ResultSet verify = dbConnector.selectQuery("SELECT * FROM Bot WHERE company_id =" + company.getCompanyId() + "" +
                "ORDER BY created_at DESC LIMIT 1;");
        try {
            int botId = Integer.parseInt(verify.getObject("bot_id").toString());
            System.out.println("New bot with id " + botId + "has been created.");
            return botId;
        } catch (SQLException e) {
            System.out.println(String.format("Error in creating bot for %s", company.getName()));
        }
        return -1;
    }

    /**
     * Retrieves bot information by its id
     * @param botId id of the bot
     * @return a Bot object that contains all the information of the bot
     */
    public Bot getBotById(int botId) {
        final String selectQuery = "SELECT * FROM Bots WHERE bot_id = %d;";
        final String errorMessage = "Error in getting bot information for bot with %d";

        ResultSet retrievedBot = dbConnector.selectQuery(String.format(selectQuery, botId));
        try {
            while (retrievedBot.next()) {
                return toBot(retrievedBot);
            }
        } catch (SQLException e) {
            System.out.println(String.format(errorMessage, botId));
        }
        return null;
    }

    /**
     * It will get all the bots in the LStock
     */
    public ArrayList<Bot> getAllBots() {
        ResultSet retrievedBots = dbConnector.selectQuery("SELECT * FROM Bots;");
        ArrayList<Bot> bots = null;
        try {
            bots = new ArrayList<Bot>();
            while (retrievedBots.next()) {
                bots.add(toBot(retrievedBots));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all bots");
        }
        return bots;
    }

    /**
     * Converts retrieved information into a Bot
     *
     * @param resultSet result set from database
     * @return a Bot object containing the information retrieved from the database
     * @throws SQLException
     */
    private Bot toBot(ResultSet resultSet) throws SQLException {
        Bot bot = new Bot();
        bot.setBotId(Integer.parseInt(resultSet.getObject("id").toString()));
        bot.setActiveTime(Float.parseFloat(resultSet.getObject("active_time").toString()));
        bot.setProbability(Float.parseFloat(resultSet.getObject("probability").toString()));
        bot.setCompany(new Company(Integer.parseInt(resultSet.getObject("company_id").toString())));
        return bot;
    }

    /**
     * Erases a bot
     *
     * @param botId id of bot to erase
     */
    public boolean deleteBot(int botId) {
        final String selectQuery = "SELECT * FROM Bots WHERE bot_id = %d;";
        final String deleteQuery = "DELETE FROM Bots WHERE bot_id = %d;";
        final String successMessage = "Bot %d deleted";
        final String errorMessage = "Error deleting the bot with id %d";

        // Consult the database for the bot to be deleted
        ResultSet verify = dbConnector.selectQuery(String.format(selectQuery, botId));
        try {
            while (verify.next()) {
                // If the bot exists, delete it
                if (verify.getInt("bot_id") == botId) {
                    dbConnector.deleteQuery(String.format(deleteQuery, botId));
                    System.out.println(String.format(successMessage, botId));
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println(String.format(errorMessage, botId));
        }
        return false;
    }

    public boolean updateBot(Bot bot) {
        final String updateQuery = "UPDATE Bots SET company_id = %d, active_time = %f, probability = %f WHERE " +
                "bot_id = %d";
        final String selectQuery = "SELECT * FROM Bots WHERE bot_id = %d;";

        // Consult the database to get information on the bot to be updated
        ResultSet result = dbConnector.selectQuery(String.format(selectQuery, bot.getBotId()));
        try {
            while (result.next()) {
                // If the bot exists, update the information
                if (result.getInt("bot_id") == bot.getBotId()) {
                    dbConnector.updateQuery(String.format(updateQuery, bot.getCompany().getCompanyId(),
                            bot.getActiveTime(), bot.getProbability(), bot.getBotId()));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating bot with id " + bot.getBotId());
        }
        return false;
    }
}