import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Kamran Awan
 * This class contains variables and methods for handling the checkout of the program.
 */
public class Checkout
{
    private Queue<Customer> customers;
    private int numSuper;
    private int numExp;
    private int numStandLines;
    private double arrivalRate;
    private int maxItems;
    private double maxSimTime;
    private int averageWaitingTime;
    private int totalCustomers;
    private int totalItemsProcessed;
    private int totalOccupiedTime;

    /**
     * This constructor assigns the variables in the class.
     * @param numSuper
     * @param numExp
     * @param numStandLines
     * @param arrivalRate
     * @param maxItems
     * @param maxSimTime
     */
    public Checkout(int numSuper, int numExp, int numStandLines, double arrivalRate, int maxItems, double maxSimTime)
    {
        customers = new LinkedList<>();
        this.numSuper = numSuper;
        this.numExp = numExp;
        this.numStandLines = numStandLines;
        this.arrivalRate = arrivalRate;
        this.maxItems = maxItems;
        this.maxSimTime = maxSimTime;
    }

    /**
     * This method adds a customer to the counter.
     * @param customer
     */
    public void addCustomer(Customer customer)
    {
        totalCustomers++;
        totalItemsProcessed += customer.getItemsBought();
        customers.offer(customer);
    }

    /**
     * This method returns the number of customers in the counter.
     * @return customers.size()
     */
    public int getSize()
    {
        return customers.size();
    }

    /**
     * This method returns the total number of customers that entered the counter.
     * @return totalCustomers
     */
    public int getTotalCustomers()
    {
        return totalCustomers;
    }

    /**
     * This method returns the total number of items that were processed by the counter.
     * @return totalItemsProcessed
     */
    public int getTotalItemsProcessed()
    {
        return totalItemsProcessed;
    }

    /**
     * This method returns the total amount of time that the counter was occupied.
     * @return totalOccupiedTime
     */
    public int getTotalOccupiedTime()
    {
        return totalOccupiedTime;
    }

    /**
     * This method is used for serving customers in the counter.
     * @param clock
     * @param counter
     */
    public void serveCustomer(int clock, String counter)
    {
        if (customers.size() > 0)
        {
            totalOccupiedTime++;
            Queue<Customer> temp = new LinkedList<>();
            while (customers.size() > 1)
            {
                temp.offer(customers.poll());
            }

            if (clock >= (customers.peek().getServeTime() + customers.peek().getItemsBought() * 5))
            {
                System.out.println("Time stamp: " + clock + ": " + " Served customer from checkout: " + counter + " counter");
                customers.poll();
            }
            else
            {
                temp.offer(customers.poll());
            }
            customers = temp;

        }
    }

    /**
     * This method is used to get the amount of time the customer entering will have to wait for the counter to end.
     * @return totalTime
     */
    public int getCustomerWaitingTime()
    {
        int totalTime = 0;
        Queue<Customer> temp = new LinkedList<>();
        while (customers.size() > 0)
        {
            temp.offer(customers.peek());
            totalTime += customers.poll().getItemsBought() * 5;
        }
        customers = temp;
        return totalTime;
    }

    /**
     * This method is used to get the average waiting time in the counter.
     * @return averageWaitingTime or 0
     */
    public double getAverageWaitingTime()
    {
        Queue<Customer> temp = new LinkedList<>();
        int totalWaitingTime = 0;

        while (customers.size() > 0) {
            temp.offer(customers.peek());
            totalWaitingTime += customers.poll().getWaitingTime();
        }
        customers = temp;
        if (customers.size() > 0) {
            averageWaitingTime = totalWaitingTime / customers.size();
            return averageWaitingTime;
        }
        return 0;
    }
}