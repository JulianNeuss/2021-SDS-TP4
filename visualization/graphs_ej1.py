import os
import numpy as np
import matplotlib.pyplot as plt

BEEMAN_PATH = os.path.join("../data/oscillatory", "beeman.txt")
GEAR_PATH = os.path.join("../data/oscillatory", "gear.txt")
VERLET_PATH = os.path.join("../data/oscillatory", "verlet.txt")

methods = ["beeman", "gear", "verlet"]


def parse(path):
    file = open(path, "r")
    times = []
    positions = []
    for line in file.readlines():
        record = line.strip().split(' ')
        times.append(float(record[0]))
        positions.append([float(record[1]), float(record[2])])
    return times, positions


def error(list1, list2):
    return sum(list(map(lambda x, y: (x - y) * (x - y), list1, list2)))/len(list1)


def oscillatory_solution(k, gamma, m, times):
    return list(map(lambda t: np.exp(-gamma/(2*m)*t) * np.cos(np.sqrt(k/m - gamma*gamma/(4*m*m)) * t), times))


k = 10000
gamma = 100
m = 70
times_beeman, beeman2D = parse(BEEMAN_PATH)
beeman_x = list(map(lambda position: position[0], beeman2D))
times_gear, gear2D = parse(GEAR_PATH)
gear_x = list(map(lambda position: position[0], gear2D))
times_verlet, verlet2D = parse(VERLET_PATH)
verlet_x = list(map(lambda position: position[0], verlet2D))
approximations = [beeman_x, gear_x, verlet_x]
times = [times_beeman, times_gear, times_verlet]

error_beeman = error(oscillatory_solution(k, gamma, m, times_beeman), beeman_x)
error_gear = error(oscillatory_solution(k, gamma, m, times_gear), gear_x)
error_verlet = error(oscillatory_solution(k, gamma, m, times_verlet), verlet_x)
errors = [error_beeman, error_gear, error_verlet]

for (i, error), time in zip(enumerate(errors), times):
    print("Error " + methods[i] + ": " + str(error))

for (i, approximation), time in zip(enumerate(approximations), times):
    plt.plot(time, approximation, label=methods[i])

plt.title("Posicion")
plt.plot(times[0], oscillatory_solution(k, gamma, m, times[0]), label="analytical")
plt.legend()
plt.show()
