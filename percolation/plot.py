import matplotlib.pyplot as plt
import numpy as np

# Plot T vs time
x = [100, 200, 300, 400, 600, 800, 1200, 1600, 3200, 6400, 12800]
y = [0.107, 0.155, 0.181, 0.218, 0.3, 0.383, 0.535, 0.688, 1.302, 2.567, 4.896]

plt.scatter(x, y)
plt.xlabel("T - number of trials")
plt.ylabel("running time (seconds)")
plt.title("timing graph")

m, b = np.polyfit(x, y, 1)
print("linear regression fit: y = {} * x + {}".format(m, b))
x = np.linspace(100, 12800, 100)
y = m * x + b
plt.plot(x, y, color='green')
plt.show()

# Plot n vs time
x = [100, 200, 400, 800, 1000, 1200, 1600]
y = [0.266, 0.9, 3.673, 15.021, 25.844, 39.15, 100.48]

plt.scatter(x, y)
plt.xlabel("n - number of rows/cols")
plt.ylabel("running time (seconds)")
plt.title("timing graph")

m1, m2, b = np.polyfit(x, y, 2)
print("poly regression fit: y = {} * x^2 + {} * x + {}".format(m1, m2, b))
x = np.linspace(100, 1600, 100)
y = m1 * x ** 2 + m2 * x + b
plt.plot(x, y, color='green')
plt.show()
