package info.competitiveprogramming.getfollowers.domain.executor;

public interface PostExecutionThread {
    //Thread abstraction created to change the execution context from any thread to any other thread.
    void post(Runnable runnable);
}
