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

ax = plt.gca()
ax.figure.set_size_inches(16,12)

plt.scatter(x,up,label="up")
plt.plot(x,up,label="up")
plt.scatter(x,down,label="down")
plt.plot(x,down,label="down")
plt.scatter(x,left,label="left")
plt.plot(x,left,label="left")
plt.scatter(x,right,label="right")
plt.plot(x,right,label="right")
plt.scatter(x,absorbed,label="absorbed")
plt.plot(x,absorbed,label="absorbed")

ax.legend(fontsize=20)

f.close()
plt.show()