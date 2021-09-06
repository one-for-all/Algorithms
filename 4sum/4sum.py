# Week 6 - Hash Tables - Interview Question 1
# Given an array a[], determine if there are distinct i, j, k, l such that
# a[i] + a[j] = a[k] + a[l]

def four_sum(a):
    N = len(a)

    sums = {}
    for i in range(N):
        for j in range(i+1, N):
            sum = a[i] + a[j]
            if sum not in sums:
                sums[sum] = [(i, j)]
            else:
                indices = sums[sum]
                for tu in indices:
                    if len(set(tu).intersection((i, j))) == 0:
                        return True
                indices.append((i, j))

    return False


def four_sum_fast(a):
    N = len(a)

    sums = {}
    for i in range(N):
        i_sums = {}  # tmp dict for i row
        for j in range(i+1, N):
            sum = a[i] + a[j]

            # Test with existing indices
            if sum not in i_sums:
                if sum in sums:
                    for tu in sums[sum]:
                        if len(set(tu).intersection((i, j))) == 0:
                            return True

            # Add sum->j to tmp dict for i row
            if sum not in i_sums:
                i_sums[sum] = set()
            i_sums[sum].add((i, j))

        # Add i_sums to sums
        for sum in i_sums:
            if sum not in sums:
                sums[sum] = set()
            sums[sum].update(i_sums[sum])


if __name__ == "__main__":
    def test_sum(a):
        return four_sum_fast(a)

    a = [1, 2, 3]
    assert not test_sum(a)

    a = [1, 2, 3, 4]
    assert test_sum(a)

    a = [1, 2, 2, 1]
    # assert test_sum(a)

    a = [1, 2, 2]
    assert not test_sum(a)

    a = [1, 2, 3, 4, 5]
    assert test_sum(a)

    a = [1, 4, 2, 1]
    assert not test_sum(a)

    a = [1, 2, 1, 2]
    assert test_sum(a)
