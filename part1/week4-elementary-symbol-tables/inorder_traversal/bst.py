from collections import deque


def compare(key1, key2):
    if key1 == key2:
        return 0
    if key1 < key2:
        return -1
    return 1


class Node:
    def __init__(self, key, val, n):
        self.left = None
        self.right = None
        self.key = key
        self.val = val
        self.n = n


class BST:
    def __init__(self):
        self.root = None

    def size(self):
        return self._size(self.root)

    def _size(self, x):
        if x is None:
            return 0
        return x.n

    def put(self, key, val):
        self.root = self._put(self.root, key, val)

    def _put(self, x, key, val):
        if x is None:
            return Node(key, val, 1)
        cmp_ = compare(key, x.key)
        if cmp_ < 0:
            x.left = self._put(x.left, key, val)
        elif cmp_ > 0:
            x.right = self._put(x.right, key, val)
        else:
            x.val = val
        x.n = self._size(x.left) + self._size(x.right) + 1
        return x

    def __str__(self):
        if self.root is None:
            return ""

        result = []

        # Level order traversal
        queue = deque()
        queue.append((self.root, 0))
        while len(queue) > 0:
            node, level = queue.popleft()
            if level >= len(result):
                result.append([node.val])
            else:
                result[level].append(node.val)
            if node.left is not None:
                queue.append((node.left, level+1))
            if node.right is not None:
                queue.append((node.right, level+1))

        # Put values into a string
        s = ""
        for vals in result:
            for val in vals:
                s += str(val) + " "
            s += "\n"
        return s

    def inorder(self):
        def inorder_helper(node, arr):
            if node is None:
                return
            inorder_helper(node.left, arr)
            arr.append(node.val)
            inorder_helper(node.right, arr)

        result = []
        inorder_helper(self.root, result)
        return result

    def morris_inorder(self):
        result = []

        cur = self.root
        while cur is not None:
            if cur.left is None:
                result.append(cur.val)
                cur = cur.right
            else:
                prev = cur.left
                while prev.right is not None and prev.right != cur:
                    prev = prev.right

                if prev.right is None:
                    prev.right = cur
                    cur = cur.left
                else:
                    prev.right = None
                    result.append(cur.val)
                    cur = cur.right

        return result


if __name__ == "__main__":
    bst = BST()
    bst.put(1, 1)
    bst.put(3, 3)
    bst.put(0, 0)
    bst.put(2, 2)
    bst.put(5, 5)
    bst.put(-5, -5)
    bst.put(-1, -1)
    bst.put(-2, -2)
    print(bst)

    print(bst.inorder())
    print(bst.morris_inorder())
