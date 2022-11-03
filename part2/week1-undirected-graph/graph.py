# Standard graph implementation using adjacency lists
class Graph:
    def __init__(self, n: int):
        self.n = n
        self.adj = [[] for _ in range(n)]

    def add_edge(self, v: int, w: int):
        self.adj[v].append(w)
        self.adj[w].append(v)
