from graph import Graph


# Depth first search using recursion
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


# Depth first search using stack
class DepthFirstSearchStack:
    def __init__(self, graph: Graph, s: int):
        self.marked = set()
        self.graph = graph

        # performs search
        self.stack = []
        self.stack.append(s)
        while len(self.stack) > 0:
            cur = self.stack.pop()
            self.marked.add(cur)
            for w in graph.adj[cur]:
                if w not in self.marked:
                    self.stack.append(w)

    def connected(self):
        return self.marked


if __name__ == "__main__":
    graph = Graph(6)
    graph.add_edge(0, 1)
    graph.add_edge(0, 2)
    graph.add_edge(2, 5)
    graph.add_edge(3, 4)

    dfs = DepthFirstSearch(graph, 2)
    assert dfs.connected() == {0, 1, 2, 5}

    dfs_stack = DepthFirstSearchStack(graph, 2)
    assert dfs_stack.connected() == {0, 1, 2, 5}
