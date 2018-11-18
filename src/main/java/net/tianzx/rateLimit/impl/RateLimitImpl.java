package net.tianzx.rateLimit.impl;

import net.tianzx.rateLimit.RateLimit;

public class RateLimitImpl implements RateLimit {
    long time = -1;
    int qps;
    long qpsMillis;

    @Override
    public void setQPS(int qps) {
        if(qps <1 || qps >1_000){
            throw new RuntimeException();
        }
        this.qps = qps;
        qpsMillis = qps * 1000;
    }

    @Override
    public boolean allowThisRequest() {
        long currentTime  =  System.currentTimeMillis();
        if (time == -1) {
            time = currentTime;
            return true;
        }else {
            long result = currentTime - time;
            if (result < qpsMillis) {
                return false;
            }else {
                time = currentTime;
                return true;
            }
        }
    }
}
