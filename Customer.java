import java.util.Random;

/**
 * @author Kamran Awan
 * This class contains variables and methods for handling the customers in the program.
 */
public class Customer
{
    private int waitingTime;
    private int itemsBought;
    private Random random = new Random();
    private int serveTime;

    /**
     * This constructor inputs the data for the customer.
     * @param maxItems
     * @param serveTime
     */
    public Customer(int maxItems, int serveTime)
    {
        itemsBought = random.nextInt(maxItems) + 1;
        this.serveTime = serveTime;
    }

    /**
     * This method returns the number of items bought by the customer.
     * @return itemsBought
     */
    public int getItemsBought()
    {
        return itemsBought;
    }

    /**
     * This method sets the waitingTime to the one in the parameters.
     * @param waitingTime
     */
    public void setWaitingTime(int waitingTime)
    {
        this.waitingTime = waitingTime;
    }

    /**
     * This method returns the amount of time the customer will have to wait before processing.
     * @return waitingTime
     */
    public int getWaitingTime()
    {
        return waitingTime;
    }

    /**
     * This method returns the amount of time it would take to serve.
     * @return serveTime
     */
    public int getServeTime()
    {
        return serveTime;
    }
}