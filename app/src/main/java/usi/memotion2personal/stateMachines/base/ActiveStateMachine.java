package usi.memotion2personal.stateMachines.base;

/**
 * Created by usi on 20/04/17.
 */
public abstract class ActiveStateMachine extends StateMachine implements Runnable {

    public ActiveStateMachine(Enum[][] transitions, Enum startState) {
        super(transitions, startState);
    }

    @Override
    public void run() {
        transition();
    }
}
