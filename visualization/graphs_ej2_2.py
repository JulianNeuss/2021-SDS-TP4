import matplotlib.pyplot as plt


f = open("../data/electric/energyVaryingTimeStep.txt")
lines = f.readlines()
lines = list(filter(lambda x: x.strip() != '', lines))

dts = []
times = []
energies = []

for i in range(int(len(lines)/2)):
    dt = float(lines[2*i].strip())
    dts.append(dt)
    e = list(map(float,lines[2*i + 1].strip().split(" ")))
    energies.append(e)
    aux_times = []
    for i in range(len(e)):
        aux_times.append(i * dt)
    times.append(aux_times)
    plt.plot(aux_times,e,label=dt)

ax = plt.gca()
ax.figure.set_size_inches(16,12)
ax.set_xlabel("Tiempo (s)",fontsize=15)
ax.set_ylabel("Energía mecánica (J)",fontsize=15)
ax.legend(fontsize=12)

plt.show()
f.close()
