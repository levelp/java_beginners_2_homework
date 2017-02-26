package org.levelp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Anastasiya Solodkaya.
 */
public class FutureTaskDemo {

    public static class ProductInfo {

    }

    public static class DataLoadException extends Exception {

    }

    // VERY LONG TASK
    private ProductInfo loadProductInfo() {
        return null;
    }

    private final FutureTask<ProductInfo> future =
            new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
                public ProductInfo call() throws DataLoadException {
                    return loadProductInfo();
                }
            });


    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public ProductInfo get()
            throws DataLoadException, InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable t = e.getCause();
            if (t instanceof DataLoadException) {
                throw (DataLoadException) t;
            } else {
                if (t instanceof RuntimeException)
                    throw (RuntimeException) t;
                else if (t instanceof Error)
                    throw (Error) t;
                else
                    throw new IllegalStateException("Not unchecked", t);
            }
        }
    }

    private DataLoadException launderThrowable(Throwable cause) {
        return null;
    }
}
