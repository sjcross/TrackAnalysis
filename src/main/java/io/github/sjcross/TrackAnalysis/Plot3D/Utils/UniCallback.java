package io.github.sjcross.TrackAnalysis.Plot3D.Utils;

@FunctionalInterface
public interface UniCallback<T>
{
    void invoke(T object);
}
