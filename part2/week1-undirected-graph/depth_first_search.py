class Graph:
    def __init__(self, v: int):
        self.v = v
        self.adj = [[] for _ in range(v)]

    def add_edge(self, v: int, w: int):
        self.adj[v].append(w)
        self.adj[w].append(v)


class DepthFirstSearch:
    def __init__(self, graph: Graph, s: int):
        self.marked = set()
        self.graph = graph
        self._dfs(s)

    def _dfs(self, v: int):
        self.marked.add(v)
        for w in graph.adj[v]:
            if w not in self.marked:
                self._dfs(w)

    def connected(self):
        return self.marked


if __name__ == "__main__":
    graph = Graph(5)
    graph.add_edge(0, 1)
    graph.add_edge(0, 2)
    graph.add_edge(3, 4)

    dfs = DepthFirstSearch(graph, 2)
    assert dfs.connected() == set([0, 1, 2])
