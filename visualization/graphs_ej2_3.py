import matplotlib.pyplot as plt

f = open("../data/electric/trajectoryVaryingSpeed.txt")

line = f.readline().strip()

vels = []
trajectories = []

while line and line != "":
    line = list(map(float,line.split(":")))
    
    vels.append(line[0])
    trajectories.append(line[1])

    line = f.readline().strip()

plt.gcf().set_size_inches(16, 12)
plt.xlabel("velocidades",fontsize=15)
plt.ylabel("trayectorias",fontsize=15)

plt.scatter(vels,trajectories)
plt.plot(vels,trajectories)

plt.show()