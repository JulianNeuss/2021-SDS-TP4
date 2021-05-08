import matplotlib.pyplot as plt

class Result:
    def __init__(self,dt = 0, ecm = 0):
        self.dt = dt
        self.ecm = ecm
    def __repr__(self):
        return f"dt:{self.dt} ecm:{self.ecm}"

class AlgorithmResults:
    def __init__(self,name = "", results = None):
        self.name = name
        self.results = [] if results == None else results
    def __repr__(self):
        return f"Name:{self.name}\nResults:{self.results}"

f = open("../data/oscillatory/varyingTimeStep.txt")

al_results = []
line = f.readline().strip()

while line:
    r = AlgorithmResults(line,[])
    line = f.readline().strip()
    while line and line != "":
        line = list(map(float,line.split(":")))
        r.results.append(Result(line[0],line[1]))
        line = f.readline().strip()
    al_results.append(r)
    line = f.readline()

ax = plt.gca()
ax.set_yscale('log')
ax.figure.set_size_inches(15,12)
plt.xlabel('dt', fontsize=18)
plt.ylabel('log(ecm)', fontsize=16)

for ar in al_results:
    x = []
    y = []
    for r in ar.results:
        x.append(r.dt)
        y.append(r.ecm)
    plt.plot(x,y,label=ar.name)

ax.legend(fontsize=20)
plt.show()
