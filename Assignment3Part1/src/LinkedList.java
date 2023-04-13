import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
public class LinkedList<T> {
    private class Node{
        T item;
        int key;
        Node next;

        public Node(int key, T item) {
            this.item = item;
            this.key = key;
            this.next = null;
        }
    }

    private Node head;
    private Lock lock = new ReentrantLock();
    public LinkedList() {
        head = new Node(Integer.MIN_VALUE, null);
        head.next = new Node(Integer.MAX_VALUE, null);
    }

    public boolean add(T item){
        Node prev, curr;
        int key = item.hashCode();
        lock.lock();
        try {
            prev = head;
            curr = prev.next;
            while (curr.key < key){
                prev = curr;
                curr = curr.next;
            }
            if (key == curr.key)
                return false;
            else{
                Node node = new Node(key, item);
                node.next = curr;
                prev.next = node;
                return true;
            }
        }
        finally {
            lock.unlock();
        }
    }

    public boolean remove(T item){
        Node prev, curr;
        int key = item.hashCode();
        lock.lock();
        try{
            prev = head;
            curr = head.next;
            while (curr.key < key) {
                prev = curr;
                curr = curr.next;
            }
            if (key == curr.key){
                prev.next = curr.next;
                return true;
            } else {
                return false;
            }
        }
        finally {
            lock.unlock();
        }
    }

    public boolean contains(T item){
        Node prev, curr;
        int key = item.hashCode();
        lock.lock();
        try {
            prev = head;
            curr = prev.next;
            while (curr.key < key){
                prev = curr;
                curr = curr.next;
            }
            return key == curr.key;
        }
        finally {
            lock.unlock();
        }
    }

}


