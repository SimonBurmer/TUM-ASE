package edu.tum.ase.backendCommon.request;

public interface Request<T> {

    void apply(T other);
}
