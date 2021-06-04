import matplotlib.pyplot as plt

class Result:
    def __init__(self, vel = 0, up = 0, down = 0, left = 0, right = 0, absorbed = 0):
        self.vel = vel 
        self.up = up 
        self.down = down 
        self.left = left 
        self.right = right 
        self.absorbed = absorbed 
    
    def __repr__(self):
        return f"vel:{self.vel}, up:{self.up}, down:{self.down}, left:{self.left}, right:{self.right}, absorbed:{self.absorbed}"


f = open("../data/electric/directionVaryingSpeed.txt")
lines = f.readlines()
lines = list(filter(lambda x: x.strip() != '', lines))

results = []

max_y = 0
for i in range(int(len(lines)/2)):
    v = float(lines[2*i])
    line = list(map(lambda x: x.split(":"),lines[2*i + 1].strip().split(" ")))
    d = {}
    for field in line:
        d[field[0]] = float(field[1])
    results.append(Result(v,d["UP"], d["DOWN"], d["LEFT"], d["RIGHT"], d["ABSORBED"]))

x = []
up = []
down = []
left = []
right = []
absorbed = []

for r in results:
    x.append(r.vel)
    up.append(r.up)
    down.append(r.down)
    left.append(r.left)
    right.append(r.right)
    absorbed.append(r.absorbed)

class Result:
    def __init__(self, vel = 0, up = 0, down = 0, left = 0, right = 0, absorbed = 0):
        self.vel = vel
        self.up = up
        self.down = down
        self.left = left
        self.right = right
        self.absorbed = absorbed

    def __repr__(self):
        return f"vel:{self.vel}, up:{self.up}, down:{self.down}, left:{self.left}, right:{self.right}, absorbed:{self.absorbed}"


f = open("../data/electric/directionVaryingSpeed.txt")
lines = f.readlines()
lines = list(filter(lambda x: x.strip() != '', lines))

results = []

max_y = 0
for i in range(int(len(lines)/2)):
    v = float(lines[2*i])
    line = list(map(lambda x: x.split(":"),lines[2*i + 1].strip().split(" ")))
    d = {}
    for field in line:
        d[field[0]] = float(field[1])
    results.append(Result(v,d["UP"], d["DOWN"], d["LEFT"], d["RIGHT"], d["ABSORBED"]))

x = []
up = []
down = []
left = []
right = []
absorbed = []

for r in results:
    x.append(r.vel)
    up.append(r.up)
    down.append(r.down)
    left.append(r.left)
    right.append(r.right)
    absorbed.append(r.absorbed)

ax = plt.gca()
plt.scatter(x, absorbed, color='orangered')
plt.scatter(x, down, color='gold')
plt.scatter(x, left, color='palegreen')
plt.scatter(x, up, color='darkturquoise')
plt.scatter(x, right, color='royalblue')

plt.plot(x, absorbed, label='Absorbido', color='orangered')
plt.plot(x, down, label='Abajo', color='gold')
plt.plot(x, left, label='Izquierda', color='palegreen')
plt.plot(x, up, label='Arriba', color='darkturquoise')
plt.plot(x, right, label='Derecha', color='royalblue')


box = ax.get_position()
ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5), fontsize=15)
plt.xlabel("Velocidad inicial (m/s)",fontsize=15)
plt.ylabel("Proporción de particulas",fontsize=15)

f.close()
plt.show()

box = ax.get_position()
ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5), fontsize=15)
plt.xlabel("Velocidad inicial (m/s)",fontsize=15)
plt.ylabel("Proporción de particulas",fontsize=15)

f.close()
plt.show()