package g54915.model.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private final List<Observer> observers;

    /**
     * Construct an Observable with zero Observers.
     */
    public Observable() {
        observers = new ArrayList<>();
    }

    /**
     * Adds an observer to the set of observers for this object, provided that
     * it is not the same as some observer already in the set. The order in
     * which notifications will be delivered to multiple observers is not
     * specified. See the class comment.
     *
     * @param observer an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    public void addObserver(Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Deletes an observer from the set of observers of this object. Passing
     * {@code null} to this method will have no effect.
     *
     * @param observer the observer to be deleted.
     */
    public synchronized void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        notifyObservers(null);
    }

    public void notifyObservers(Object arg) {
        for (Observer observer : observers) {
            observer.update(this, arg);
        }
    }

    /**
     * Returns the number of observers of this {@code Observable} object.
     *
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return observers.size();
    }
}
