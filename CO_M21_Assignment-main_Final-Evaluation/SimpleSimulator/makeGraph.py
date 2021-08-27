import sys
import os
import matplotlib.pyplot as plt
import matplotlib.ticker as MaxNLocator
workingdirectory = os.getcwd()
def printGraph():
    x = []
    y = []
    str = sys.argv[1].split()
    for i in range(0,len(str)):
        y.append(int(str[i]))
        x.append(i)
    plt.scatter(x,y) 
    plt.xlabel("Cycle Number")
    plt.ylabel("Memory Address")
    plt.title("Memory address v/s Cycle Number plot")
    plt.savefig("Plot.png", bbox = "tight")

printGraph()
