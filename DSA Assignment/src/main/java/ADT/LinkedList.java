package adt;
/**
 * LinkedList implementation of LinkedListInterface
 * Team Component - Custom LinkedList ADT for Internship Application Program
 * 
 * @param <T> The type of elements in the list
 */
public class LinkedList<T> implements ListInterface<T> {

    private Node firstNode;
    private int numberOfEntries;

    public LinkedList() {
        clear();
    }

    @Override
    public final void clear() {
        firstNode = null;
        numberOfEntries = 0;
    }

    @Override
    public boolean add(T newEntry) {
        Node newNode = new Node(newEntry);

        if (isEmpty()) {
            firstNode = newNode;
        } else {
            Node currentNode = firstNode;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }

        numberOfEntries++;
        return true;
    }

    @Override
    public boolean add(int newPosition, T newEntry) {
        if (newPosition < 1 || newPosition > numberOfEntries + 1) {
            return false;
        }

        Node newNode = new Node(newEntry);

        if (newPosition == 1) {
            newNode.next = firstNode;
            firstNode = newNode;
        } else {
            Node nodeBefore = getNodeAt(newPosition - 1);
            newNode.next = nodeBefore.next;
            nodeBefore.next = newNode;
        }

        numberOfEntries++;
        return true;
    }

    @Override
    public T remove(int givenPosition) {
        if (givenPosition < 1 || givenPosition > numberOfEntries) {
            return null;
        }

        T result;
        if (givenPosition == 1) {
            result = firstNode.data;
            firstNode = firstNode.next;
        } else {
            Node nodeBefore = getNodeAt(givenPosition - 1);
            result = nodeBefore.next.data;
            nodeBefore.next = nodeBefore.next.next;
        }

        numberOfEntries--;
        return result;
    }

    @Override
    public boolean replace(int givenPosition, T newEntry) {
        if (givenPosition < 1 || givenPosition > numberOfEntries) {
            return false;
        }

        getNodeAt(givenPosition).data = newEntry;
        return true;
    }

    @Override
    public T getEntry(int givenPosition) {
        if (givenPosition < 1 || givenPosition > numberOfEntries) {
            return null;
        }
        return getNodeAt(givenPosition).data;
    }

    @Override
    public boolean contains(T anEntry) {
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.data.equals(anEntry)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node currentNode = firstNode;
        while (currentNode != null) {
            sb.append(currentNode.data);
            if (currentNode.next != null) {
                sb.append("\n");
            }
            currentNode = currentNode.next;
        }
        return sb.toString();
    }

    @Override
    public LinkedList<T> filter(Predicate<T> predicate) {
        LinkedList<T> result = new LinkedList<>();
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (predicate.test(currentNode.data)) {
                result.add(currentNode.data);
            }
            currentNode = currentNode.next;
        }
        return result;
    }

    @Override
    public void sort(Comparator<T> comparator) {
        if (numberOfEntries > 1) {
            firstNode = mergeSort(firstNode, comparator);
        }
    }

    @Override
    public int binarySearch(T key, Comparator<T> comparator) {
        int low = 1;
        int high = numberOfEntries;
        
        while (low <= high) {
            int mid = (low + high) / 2;
            T midVal = getEntry(mid);
            int cmp = comparator.compare(midVal, key);
            
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    @Override
    public T findFirst(Predicate<T> predicate) {
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (predicate.test(currentNode.data)) {
                return currentNode.data;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public LinkedListIterator getIterator() {
        return new LinkedListIterator();
    }

    private Node getNodeAt(int position) {
        Node currentNode = firstNode;
        for (int i = 1; i < position; i++) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private Node mergeSort(Node head, Comparator<T> comparator) {
        if (head == null || head.next == null) {
            return head;
        }

        Node middle = getMiddle(head);
        Node nextOfMiddle = middle.next;
        middle.next = null;

        Node left = mergeSort(head, comparator);
        Node right = mergeSort(nextOfMiddle, comparator);

        return sortedMerge(left, right, comparator);
    }

    private Node sortedMerge(Node a, Node b, Comparator<T> comparator) {
        if (a == null) return b;
        if (b == null) return a;

        Node result;
        if (comparator.compare(a.data, b.data) <= 0) {
            result = a;
            result.next = sortedMerge(a.next, b, comparator);
        } else {
            result = b;
            result.next = sortedMerge(a, b.next, comparator);
        }
        return result;
    }

    private Node getMiddle(Node head) {
        if (head == null) return head;

        Node slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private class Node {
        private T data;
        private Node next;

        private Node(T data) {
            this(data, null);
        }

        private Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
    
    

    private class LinkedListIterator implements ListInterface.LinkedListIterator {
        private Node currentNode;
        private Node previousNode;
        private boolean canRemove;

        public LinkedListIterator() {
            this.currentNode = firstNode;
            this.previousNode = null;
            this.canRemove = false;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new RuntimeException("No more elements");
            }
            
            T data = currentNode.data;
            previousNode = currentNode;
            currentNode = currentNode.next;
            canRemove = true;
            
            return data;
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new RuntimeException("next() must be called before remove()");
            }
            
            if (previousNode == firstNode) {
                firstNode = firstNode.next;
            } else {
                Node beforePrevious = firstNode;
                while (beforePrevious != null && beforePrevious.next != previousNode) {
                    beforePrevious = beforePrevious.next;
                }
                
                if (beforePrevious != null) {
                    beforePrevious.next = previousNode.next;
                }
            }
            
            numberOfEntries--;
            canRemove = false;
        }
        
    }
}