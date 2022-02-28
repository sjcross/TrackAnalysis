package io.github.sjcross.trackanalysis.Plot3D.Utils;

@FunctionalInterface
public interface UniCallback<T>
{
    void invoke(T object);
}
