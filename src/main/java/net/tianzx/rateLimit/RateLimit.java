package net.tianzx.rateLimit;

public interface RateLimit {
    /**
     * Sets the rate, from 1 to 1000000 queries per second
     */
    void setQPS(int qps);

    /**
     * accept or reject a request, called when request is received
     */
    boolean allowThisRequest();

}
