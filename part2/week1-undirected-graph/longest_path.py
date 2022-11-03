from graph import Graph


def print_path_len_map(m):
    for i in range(len(m)):
        print("{}: {}".format(i, m[i]))
        print("===")


# Find the longest simple path in a connected undirected acyclic graph
def find_longest_path(graph: Graph):
    beg = 0  # beginning node

    # Map of edge to path length going from that edge
    path_len_map = [{} for _ in range(graph.n)]
    for i in range(graph.n):
        for v in graph.adj[i]:
            path_len_map[i][v] = 0

    visited_outgoing = set()

    # Updates the outgoing path lengths by dfs
    def _dfs_outward(v: int):
        visited_outgoing.add(v)
        for w in graph.adj[v]:
            if w not in visited_outgoing:
                _dfs_outward(w)
                # print("v: {}, w: {}".format(v, w))
                # print_path_len_map(path_len_map)
                # print("")
                path_len_from_w = 1 + max(path_len_map[w].values())
                path_len_map[v][w] = path_len_from_w

    _dfs_outward(beg)

    visited_backward = set()

    # Updates the backward path lengths by dfs
    def _dfs_backward(v: int):
        visited_backward.add(v)
        for w in graph.adj[v]:
            if w not in visited_backward:
                max_len = max([val for k, val in path_len_map[v].items()
                               if k != w], default=0)
                path_len_map[w][v] = 1 + max_len
                _dfs_backward(w)

    _dfs_backward(beg)

    # Computes total length of the max path that each node is on
    total_len_map = [None for _ in range(graph.n)]
    for w in range(graph.n):
        if len(graph.adj[w]) == 1:
            assert len(path_len_map[w].values()) == 1
            total_len_map[w] = max(path_len_map[w].values())
        else:
            largest = float('-inf')
            second_largest = float('-inf')
            for val in path_len_map[w].values():
                if val > largest:
                    second_largest = largest
                    largest = val
                elif val > second_largest:
                    second_largest = val
            total_len_map[w] = largest + second_largest

    # Find a node in a max path
    longest = float('-inf')
    node = None
    for v in range(graph.n):
        if total_len_map[v] > longest:
            longest = total_len_map[v]
            node = v

    # Construct the longest path containing node
    path = [node]

    def _get_max_path(v: int, v_from: int):
        path.append(v)
        next_node = None
        for w in graph.adj[v]:
            if w != v_from:
                if total_len_map[w] == longest:
                    next_node = w
                    _get_max_path(next_node, v)
                    break

    if len(graph.adj[node]) == 1:
        _get_max_path(graph.adj[node][0], node)
    else:
        count = 0
        for w in graph.adj[node]:
            if total_len_map[w] == longest:
                _get_max_path(w, node)
                count += 1
                if count == 2:
                    break

    return path


if __name__ == "__main__":
    graph = Graph(6)
    graph.add_edge(0, 1)
    graph.add_edge(0, 2)
    graph.add_edge(2, 5)
    graph.add_edge(3, 4)
    graph.add_edge(2, 3)

    path = find_longest_path(graph)
    assert set(path) == {0, 1, 2, 3, 4}

    graph2 = Graph(15)
    graph2.add_edge(0, 1)
    graph2.add_edge(1, 2)
    graph2.add_edge(2, 3)
    graph2.add_edge(2, 4)
    graph2.add_edge(4, 5)
    graph2.add_edge(4, 8)
    graph2.add_edge(8, 6)
    graph2.add_edge(6, 7)
    graph2.add_edge(8, 10)
    graph2.add_edge(10, 11)
    graph2.add_edge(8, 9)
    graph2.add_edge(8, 12)
    graph2.add_edge(12, 13)
    graph2.add_edge(13, 14)

    path = find_longest_path(graph2)
    assert set(path) == {0, 1, 2, 4, 8, 12, 13, 14}
