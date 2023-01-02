package io.github.vampireachao.stream.core.lambda;

/**
 * LambdaInvokeException
 *
 * @author VampireAchao Cizai_

 * @since 2022/9/4
 */
public class LambdaInvokeException extends RuntimeException {

    /**
     * <p>Constructor for LambdaInvokeException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object
     */
    public LambdaInvokeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public LambdaInvokeException(String message) {
        super(message);
    }

}
