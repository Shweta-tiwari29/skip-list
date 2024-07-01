import java.util.Random;

class SkipListNode<T> {
    int key;
    T value;
    SkipListNode<T>[] forward;

    @SuppressWarnings("unchecked")
    public SkipListNode(int key, T value, int level) {
        this.key = key;
        this.value = value;
        forward = new SkipListNode[level + 1];
        for (int i = 0; i < forward.length; i++) {
            forward[i] = null;
        }
    }
}

class SkipList<T> {
    private static final int MAX_LEVEL = 16;
    private final Random random = new Random();
    private SkipListNode<T> head;
    private int level;

    public SkipList() {
        head = new SkipListNode<>(Integer.MIN_VALUE, null, MAX_LEVEL);
        level = 0;
    }

    private int randomLevel() {
        int lvl = 0;
        while (lvl < MAX_LEVEL && random.nextInt(2) == 0) {
            lvl++;
        }
        return lvl;
    }

    public void insert(int key, T value) {
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL + 1];
        SkipListNode<T> current = head;

        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].key < key) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        current = current.forward[0];

        if (current == null || current.key != key) {
            int newLevel = randomLevel();

            if (newLevel > level) {
                for (int i = level + 1; i <= newLevel; i++) {
                    update[i] = head;
                }
                level = newLevel;
            }

            current = new SkipListNode<>(key, value, newLevel);

            for (int i = 0; i <= newLevel; i++) {
                current.forward[i] = update[i].forward[i];
                update[i].forward[i] = current;
            }
        }
    }

    public void delete(int key) {
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL + 1];
        SkipListNode<T> current = head;

        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].key < key) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        current = current.forward[0];

        if (current != null && current.key == key) {
            for (int i = 0; i <= level; i++) {
                if (update[i].forward[i] != current) {
                    break;
                }
                update[i].forward[i] = current.forward[i];
            }

            while (level > 0 && head.forward[level] == null) {
                level--;
            }
        }
    }

    public T search(int key) {
        SkipListNode<T> current = head;

        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].key < key) {
                current = current.forward[i];
            }
        }

        current = current.forward[0];

        if (current != null && current.key == key) {
            return current.value;
        }

        return null;
    }

    public void printList() {
        for (int i = 0; i <= level; i++) {
            SkipListNode<T> node = head.forward[i];
            System.out.print("Level " + i + ": ");
            while (node != null) {
                System.out.print(node.key + " ");
                node = node.forward[i];
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SkipList<String> list = new SkipList<>();

        list.insert(3, "Three");
        list.insert(6, "Six");
        list.insert(7, "Seven");
        list.insert(9, "Nine");
        list.insert(12, "Twelve");
        list.insert(19, "Nineteen");
        list.insert(17, "Seventeen");
        list.insert(26, "Twenty Six");
        list.insert(21, "Twenty One");
        list.insert(25, "Twenty Five");

        list.printList();

        System.out.println("Search for 19: " + list.search(19));
        System.out.println("Search for 15: " + list.search(15));

        list.delete(19);
        System.out.println("After deleting 19:");
        list.printList();
    }
}
