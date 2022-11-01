# Implementation of "Dutch national flag" question

def three_sort(a):
    """
    :param a: An array containing 1, 2, or 3.
    :return: a sorted array
    """

    def swap(a, i, j):
        """ Swap the values at i and j
        """
        tmp = a[i]
        a[i] = a[j]
        a[j] = tmp

    red = 0
    blue = len(a) - 1
    cur = 0
    while cur <= blue:
        if a[cur] == 1:
            swap(a, red, cur)
            red += 1
            cur += 1
        elif a[cur] == 2:
            cur += 1
        elif a[cur] == 3:
            swap(a, blue, cur)
            blue -= 1

    return a


if __name__ == "__main__":
    a = [1, 1, 3, 3, 2, 2, 1, 1, 3, 1]
    three_sort(a)
    assert sorted(a) == a

    b = []
    three_sort(b)
    assert sorted(b) == b
