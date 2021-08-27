# Week 5 - Balanced Search Tree - Interview Question 2
# Find the shortest interval in which query (of m words) appears document (of n words)
def shortest_interval(document, query):
    m1 = query[0]
    M = len(query)  # Assume M > 1 for now
    st = {m1: [(1, -1)]}

    intervals = []
    for k, n in enumerate(document):
        if n in st:
            search_threads = st[n].copy()
            for next, i in search_threads:
                if i != -1:  # if not first query word
                    st[n].remove((next, i))
                    if len(st[n]) == 0:
                        del st[n]
                else:
                    i = k

                # Finished all query words
                if next >= M:
                    intervals.append(k-i+1)
                    continue

                # Add next query word
                next_word = query[next]
                if next_word not in st:
                    st[next_word] = [(next+1, i)]
                else:
                    st[next_word].append((next+1, i))

    if len(intervals) == 0:
        return -1
    return min(intervals)


if __name__ == "__main__":
    document = "aaaaa"
    query = "aa"
    assert shortest_interval(document, query) == 2

    document = "abcbcbcb"
    query = "bbb"
    assert shortest_interval(document, query) == 5
    assert shortest_interval(query, document) == -1

    document = "aaaaaa"
    query = "a"
    assert shortest_interval(document, query) == 1

    document = "abcabcabc"
    query = "abc"
    assert shortest_interval(document, query) == 3
    assert shortest_interval(document, "aca") == 4
    assert shortest_interval(document, "cab") == 3
    assert shortest_interval(document, "caab") == 6

    document = "aaaabc"
    query = "abc"
    assert shortest_interval(document, query) == 3
    assert shortest_interval(document, "aabc") == 4