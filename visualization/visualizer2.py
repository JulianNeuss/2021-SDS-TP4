import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from matplotlib.collections import PatchCollection

D = pow(10,-8)
L = 16
SAVE_VIDEO = True
SHOW_PLOT = True

algorithm_used = 1

if algorithm_used == 1:
    f = open("../data/electric_beeman.txt") 
else:
    f = open("../data/electric_verlet.txt")

class Snap:
    def __init__(self,t = 0,x = 0,y = 0,vx = 0,vy = 0):
        self.t = t 
        self.x = x 
        self.y = y 
        self.vx = vx 
        self.vy = vy 
    def __repr__(self):
        return f"t:{self.t}, x:{self.x}, y:{self.y}, vx:{self.vx}, vy:{self.vy}"
                
snaps = []

line = f.readline().strip()
while line:
    line = list(map(float,line.split(" ")))
    snaps.append(Snap(line[0],line[1],line[2],line[3],line[4]))
    line = f.readline().strip()

def update_snap(i):
    global snap_scatter
    plt.title(f"Time:  {snaps[i].t} seg")
    snap_scatter.set_offsets([[snaps[i].x,snaps[i].y]])


def progress_callback(curr,total):
    if curr % 10 == 0:
        print(f"Saving frame {curr} of total {total}")


stationary_x = []
stationary_y = []
stationary_colors = []
for x in range(16):
    for y in range(16):
        stationary_x.append((x+1) * D)
        stationary_y.append(y * D)
        if (x+y) % 2:
            stationary_colors.append("black")
        else:
            stationary_colors.append("red")

plt.xlim([0, (L+1) * D])
plt.ylim([-1 * D, L * D])



stationary = plt.scatter(stationary_x,stationary_y,facecolors=stationary_colors,s=150)
snap_scatter = plt.scatter(snaps[0].x,snaps[0].y,facecolor="red",s=150)

ani = FuncAnimation(
    plt.gcf(),update_snap,
    frames=len(snaps)
)

plt.gcf().set_size_inches(12,12)

if SAVE_VIDEO == True:
    ani.save("animation/visualizer2_anime.avi",progress_callback=progress_callback)
    ani.save("animation/visualizer2_anime.gif",progress_callback=progress_callback)
if SHOW_PLOT == True:
    plt.show()