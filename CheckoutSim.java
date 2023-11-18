import java.util.Scanner;

/**
 * @author Kamran Awan
 * This class contains variables and methods for handling the checkout simulation in the program.
 */
public class CheckoutSim
{
    private SuperExpressCounter superExpressCounter;
    private final ExpressCounter[] expressCounters = new ExpressCounter[2];
    private StandardCounter[] standardCounters;
    private int numSuper, numExp, numStandLines, maxItems, clock;
    private double arrivalRate, maxSimTime;

    /**
     * This method is used for entering the data from the user.
     */
    private void enterData()
    {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter the number of items allowed in the super-express line (numSuper): ");
        numSuper = input.nextInt();

        System.out.print("Enter the number of items allowed in the express line (numExp): ");
        numExp = input.nextInt();

        System.out.print("Enter the number of regular lines (numStandLines): ");
        numStandLines = input.nextInt();

        System.out.print("Enter the arrival rate of customers per minutes (arrivalRate): ");
        arrivalRate = input.nextDouble()/60;

        System.out.print("Enter the maximum number of items (maxItems): ");
        maxItems = input.nextInt();

        System.out.print("Enter the simulation time in minutes (maxSimTime): ");
        maxSimTime = input.nextDouble();
    }

    /**
     * This method is used for creating the counters for the simulation.
     */
    private void createCounters()
    {
        standardCounters = new StandardCounter[numStandLines];
        superExpressCounter = new SuperExpressCounter(numSuper, numExp, numStandLines, arrivalRate, maxItems, maxSimTime);
        for (int i = 0; i < 2; i++)
        {
            expressCounters[i] = new ExpressCounter(numSuper, numExp, numStandLines, arrivalRate, maxItems, maxSimTime);
        }
        for (int i = 0; i < numStandLines; i++)
        {
            standardCounters[i] = new StandardCounter(numSuper, numExp, numStandLines, arrivalRate, maxItems, maxSimTime);
        }
    }

    /**
     * This method runs the simulation.
     */
    private void runSimulation()
    {
        ExpressCounter smallestExpressCounter = expressCounters[0];
        StandardCounter smallestStandardCounter = standardCounters[0];

        for (clock = 0; clock < maxSimTime; clock++)
        {
            if (Math.random() < arrivalRate)
            {
                Customer newCustomer = new Customer(maxItems, clock);
                int itemsBought = newCustomer.getItemsBought();
                String counter = "Standard";

                for (ExpressCounter expressCounter : expressCounters)
                {
                    if (expressCounter.getSize() <= smallestExpressCounter.getSize())
                    {
                        smallestExpressCounter = expressCounter;
                    }
                }
                for (StandardCounter standardCounter : standardCounters)
                {
                    if (standardCounter.getSize() <= smallestStandardCounter.getSize())
                    {
                        smallestStandardCounter = standardCounter;
                    }
                }

                if (smallestStandardCounter.getSize() == 0)
                {
                    newCustomer.setWaitingTime(smallestStandardCounter.getCustomerWaitingTime());
                    smallestStandardCounter.addCustomer(newCustomer);
                }
                else if (itemsBought <= numSuper)
                {
                    if (smallestExpressCounter.getSize() == 0)
                    {
                        newCustomer.setWaitingTime(smallestExpressCounter.getCustomerWaitingTime());
                        smallestExpressCounter.addCustomer(newCustomer);
                        counter = "Express";
                    }
                    else
                    {
                        newCustomer.setWaitingTime(superExpressCounter.getCustomerWaitingTime());
                        superExpressCounter.addCustomer(newCustomer);
                        counter = "Super Express";
                    }
                }
                else if (itemsBought <= numExp)
                {
                    newCustomer.setWaitingTime(smallestExpressCounter.getCustomerWaitingTime());
                    smallestExpressCounter.addCustomer(newCustomer);
                    counter = "Express";
                }
                else
                {
                    newCustomer.setWaitingTime(smallestStandardCounter.getCustomerWaitingTime());
                    smallestStandardCounter.addCustomer(newCustomer);
                }

                System.out.println("Time stamp: " + clock + ": " + " customer arrived. Items bought: " + itemsBought + ". Moved to " + counter + " Counter");
            }

            superExpressCounter.serveCustomer(clock, "Super");
            for (ExpressCounter expressCounter : expressCounters)
            {
                expressCounter.serveCustomer(clock, "Express");
            }
            for (StandardCounter standardCounter : standardCounters)
            {
                standardCounter.serveCustomer(clock, "Standard");
            }
        }
    }

    /**
     * This method displays the data retrieved from the simulation.
     */
    private void displayData()
    {
        double totalAverageWaitingTime = superExpressCounter.getAverageWaitingTime();
        int totalCustomers = 0;
        int totalItemsProcessed = 0;
        int totalFreeTime = 0;

        System.out.println("\nSuper express counter data: ");
        System.out.println("Average waiting time (seconds): " + superExpressCounter.getAverageWaitingTime());
        System.out.println("Maximum length: " + superExpressCounter.getCustomerWaitingTime());
        System.out.println("Number of customers per hour: " + superExpressCounter.getTotalCustomers()/(maxSimTime/60));
        System.out.println("Number of items processed per hour: " + superExpressCounter.getTotalItemsProcessed()/(maxSimTime/60));
        System.out.println("Average free time per hour: " + (maxSimTime - superExpressCounter.getTotalOccupiedTime())/(maxSimTime/60));
        totalFreeTime += maxSimTime - superExpressCounter.getTotalOccupiedTime();
        totalCustomers += superExpressCounter.getTotalCustomers();
        totalItemsProcessed += superExpressCounter.getTotalItemsProcessed();

        for (int i = 1; i < 3; i++)
        {
            System.out.println("\nExpress counter #" + i + "'s data: ");
            System.out.println("Average waiting time (seconds): " + expressCounters[i - 1].getAverageWaitingTime());
            System.out.println("Maximum length: " + expressCounters[i - 1].getCustomerWaitingTime());
            System.out.println("Number of customers per hour: " + expressCounters[i - 1].getTotalCustomers()/(maxSimTime/60));
            System.out.println("Number of items processed per hour: " + expressCounters[i - 1].getTotalItemsProcessed()/(maxSimTime/60));
            System.out.println("Average free time per hour: " + (maxSimTime - expressCounters[i - 1].getTotalOccupiedTime())/(maxSimTime/60));
            totalFreeTime += maxSimTime - expressCounters[i - 1].getTotalOccupiedTime();
            totalCustomers += expressCounters[i - 1].getTotalCustomers();
            totalAverageWaitingTime += expressCounters[i - 1].getAverageWaitingTime();
            totalItemsProcessed += expressCounters[i - 1].getTotalItemsProcessed();
        }
        for (int i = 1; i < numStandLines + 1; i++)
        {
            System.out.println("\nSuper express counter #" + i + "'s data: ");
            System.out.println("Average waiting time (seconds): " + standardCounters[i - 1].getAverageWaitingTime());
            System.out.println("Maximum length: " + standardCounters[i - 1].getCustomerWaitingTime());
            System.out.println("Number of customers per hour: " + standardCounters[i - 1].getTotalCustomers()/(maxSimTime/60));
            System.out.println("Number of items processed per hour: " + standardCounters[i - 1].getTotalItemsProcessed()/(maxSimTime/60));
            System.out.println("Average free time per hour: " + (maxSimTime - standardCounters[i - 1].getTotalOccupiedTime())/(maxSimTime/60));
            totalFreeTime += maxSimTime - standardCounters[i - 1].getTotalOccupiedTime();
            totalCustomers += standardCounters[i - 1].getTotalCustomers();
            totalAverageWaitingTime += standardCounters[i - 1].getAverageWaitingTime();
            totalItemsProcessed += standardCounters[i - 1].getTotalItemsProcessed();
        }

        System.out.println("Total average waiting time: " + totalAverageWaitingTime/(3 + numStandLines));
        System.out.println("Overall number of customers: " +  totalCustomers);
        System.out.println("Overall number of items processed: " +  totalItemsProcessed);
        System.out.println("Overall free time: " +  totalFreeTime);
    }

    /**
     * This is the main method.
     * @param args
     */
    public static void main(String[] args)
    {
        CheckoutSim checkoutSim = new CheckoutSim();
        checkoutSim.enterData();
        checkoutSim.createCounters();
        checkoutSim.runSimulation();
        checkoutSim.displayData();
    }
}