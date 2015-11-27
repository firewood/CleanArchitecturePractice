package info.competitiveprogramming.getfollowers.executor;

public interface PostExecutionThread {
    //Thread abstraction created to change the execution context from any thread to any other thread.
    void post(Runnable runnable);
}
