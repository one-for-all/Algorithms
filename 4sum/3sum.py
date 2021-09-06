# Extension of 4 sum
# Given an array a[], determine if there are distinct i, j, k such that
# a[i] + a[j] + a[k] = 0

def three_sum(a):
    N = len(a)
    for i in range(N):
        target = 0 - a[i]
        exists = set()
        for j in range(i+1, N):
            if target - a[j] in exists:
                return True
            exists.add(a[j])

    return False


if __name__ == "__main__":
    a = [1, 2, -3]
    assert three_sum(a)

    a = [1, 2, 3]
    assert not three_sum(a)

    a = [-1, 2, 1]
    assert not three_sum(a)

    a = [2, 2, -2]
    assert not three_sum(a)
