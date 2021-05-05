import os
import numpy as np
import matplotlib.pyplot as plt

OUTPUT_PATH = os.path.join("../data", "output.txt")

file = open(OUTPUT_PATH, "r")
times = []
positions = []
for line in file.readlines():
    record = line.strip().split(' ')
    times.append(float(record[0]))
    positions.append([float(record[1]), float(record[2])])

k = 10000
gamma = 100
m = 70

plt.plot(times, list(map(lambda position: position[0], positions)), label="approximation")
plt.plot(times, list(map(lambda t: np.exp(-gamma/(2*m)*t) * np.cos(np.sqrt(k/m - gamma*gamma/(4*m*m)) * t), times)), label="analitical")
plt.legend()
plt.show()

