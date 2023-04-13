# COP4520-Assignment3

# Part 1

# Part 2

Temperature readings are taken every second and reports generated every minute, to simulate temps every min and reports every hour. Every minute, the report with its number shows the 5 lowest and 5 highest temperatures recorded for that minute. Additionally, the 10 second interval with the largest temperature difference is also reported. The program will continue to record temperatures until manually terminated.

Temp readings are simulateds by generating a random number from within the temperature range. Each sensor is a thread. An ExecutorService is used to help maintain the threads. 
