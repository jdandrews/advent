package advent.y2017.datastructures;

public class LinkedList {
    public static class Element {
        public Element(int v) {
            value = v;
        }

        public int value;
        public Element next;
        public Element previous;
    }

    private Element head;

    public Element addFirst(int v) {
        Element e = new Element(v);
        if (head != null) {
            head.previous = e;
            e.next = head;
        }
        head = e;
        return e;
    }

    public Element addLast(int v) {
        Element e = new Element(v);
        Element end = findEnd();
        if (end == null) {
            head = e;
        } else {
            end.next = e;
            e.previous = end;
        }
        return e;
    }

    private Element findEnd() {
        if (head == null) {
            return null;
        }
        Element result = head;
        while (result.next != null) {
            result = result.next;
        }
        return result;
    }

    public void clear() {
        head = null;
    }
}
