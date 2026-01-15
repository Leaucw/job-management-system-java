package adt;
/**
 * Interface for a custom LinkedList ADT
 * Team Component - Custom LinkedList ADT for Internship Application Program
 * 
 * @param <T> The type of elements in the list
 */
public interface ListInterface<T> {

    void clear();
    boolean add(T newEntry);
    boolean add(int newPosition, T newEntry);
    T remove(int givenPosition);
    boolean replace(int givenPosition, T newEntry);
    T getEntry(int givenPosition);
    boolean contains(T anEntry);
    int getNumberOfEntries();
    boolean isEmpty();
    boolean isFull();
    String toString();
    
    LinkedList<T> filter(Predicate<T> predicate);
    void sort(Comparator<T> comparator);
    int binarySearch(T key, Comparator<T> comparator);
    T findFirst(Predicate<T> predicate);
    LinkedListIterator getIterator();
    
    interface Predicate<T> {
        boolean test(T t);
    }
    
    interface Comparator<T> {
        int compare(T a, T b);
    }
    
    interface LinkedListIterator<T>  {
        boolean hasNext();
        T next();
        void remove();
    }
    


}