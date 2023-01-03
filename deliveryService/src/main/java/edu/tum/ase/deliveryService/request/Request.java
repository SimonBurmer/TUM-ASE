package edu.tum.ase.deliveryService.request;

public interface Request<T> {

    void apply(T other);
}
