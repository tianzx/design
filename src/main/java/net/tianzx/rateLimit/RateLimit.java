package net.tianzx.rateLimit;

public interface RateLimit {

    void setQPS(int qps);

    boolean allowThisRequest();

}
