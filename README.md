# COP4520-Assignment3

# Part 1
Presents are added to the bag then shuffled to simulate randomness. Then as long as there are thank you notes left to write or the list of sorted presents isn't empty, generate a task for one of the 4 workers (threads). Either add a present from the bag to the sorted list, remove a present from the list and write a thank you note, or search the list to see if the present is there. Once the bag has all of its presents added to the list, I limited the tasks that could be given to a worker to exclude the first one. I also placed a limit on the number of searches, as this seemed secondary to the primary objective of writing thank you notes. There is an output to the console once: all presents have been added to the list, the search limit has been reached, or the thank you cards have all been done.

The search limit can be adjusted as well as the present count. I have my present count set at 250,000 and it takes about 5 minutes for all presents to be added to the bag. I have also set my search limit to 50,000 and that can also be adjusted.

# Part 2

Temperature readings are taken every second and reports generated every minute, to simulate temps every min and reports every hour. Every minute, the report with its number shows the 5 lowest and 5 highest temperatures recorded for that minute. Additionally, the 10 second interval with the largest temperature difference is also reported. The program will continue to record temperatures until manually terminated.

Temp readings are simulated by generating a random number from within the temperature range. Each sensor is a thread. An ExecutorService is used to help maintain the threads and ensure correctness. 
