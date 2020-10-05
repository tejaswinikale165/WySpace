# WySpace
WY Space has a fleet of satellites, and a ground station to communicate with them. Each satellite has a
downlink rate measured in units per 30 minutes. The ground station has a maximum bandwidth that it
can handle at any one time; it can handle multiple satellites in parallel.
A satellite can only downlink data when the ground station can see it, this window is called a pass.
When the pass begins, the connection and downlink to the ground station is immediate, you may
assume there are no delays. Similarly, the downlink will immediately stop when the pass ends. All
passes are a minimum of 30 minutes.
WY Space would like to take a text based schedule (detailed below) and use a program that can find the
30 minute period where the total downlink (all satellite passes) will be at its maximum. Furthermore,
they would like the program to determine if the ground station has the bandwidth to support this.
Since WY Space want to use your solution for multiple ground stations, the bandwidth of the ground
station should be provided as an argument to the program.
Attached File: pass-schedule.txt
This is a text based schedule WY Space expect to use with your program.
The format is as follows:
Each line represents a single pass. A pass contains the satellite name, it’s bandwith per 30 minute
period, the start time of the pass, and the end time of the pass. The four elements of a pass are comma
separated.
