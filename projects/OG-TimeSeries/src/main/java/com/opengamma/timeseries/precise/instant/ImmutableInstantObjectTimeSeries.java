/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.timeseries.precise.instant;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;

import org.threeten.bp.Instant;

import com.opengamma.timeseries.FastBackedObjectTimeSeries;
import com.opengamma.timeseries.ObjectTimeSeries;
import com.opengamma.timeseries.ObjectTimeSeriesOperators;
import com.opengamma.timeseries.ObjectTimeSeriesOperators.BinaryOperator;
import com.opengamma.timeseries.ObjectTimeSeriesOperators.UnaryOperator;
import com.opengamma.timeseries.fast.DateTimeNumericEncoding;
import com.opengamma.timeseries.fast.integer.object.FastArrayIntObjectTimeSeries;
import com.opengamma.timeseries.fast.integer.object.FastIntObjectTimeSeries;
import com.opengamma.timeseries.fast.integer.object.FastListIntObjectTimeSeries;
import com.opengamma.timeseries.fast.integer.object.FastMutableIntObjectTimeSeries;
import com.opengamma.timeseries.fast.longint.object.FastArrayLongObjectTimeSeries;
import com.opengamma.timeseries.fast.longint.object.FastListLongObjectTimeSeries;
import com.opengamma.timeseries.fast.longint.object.FastLongObjectTimeSeries;
import com.opengamma.timeseries.fast.longint.object.FastMutableLongObjectTimeSeries;
import com.opengamma.timeseries.precise.AbstractPreciseObjectTimeSeries;
import com.opengamma.timeseries.precise.AbstractPreciseObjectTimeSeriesBuilder;
import com.opengamma.timeseries.precise.PreciseObjectTimeSeries;

/**
 * Standard immutable implementation of {@code InstantObjectTimeSeries}.
 * 
 * @param <V>  the value being viewed over time
 */
public final class ImmutableInstantObjectTimeSeries<V>
    extends AbstractPreciseObjectTimeSeries<Instant, V>
    implements InstantObjectTimeSeries<V> {

  /** Empty instance. */
  private static final ImmutableInstantObjectTimeSeries<?> EMPTY_SERIES = new ImmutableInstantObjectTimeSeries<Object>(new long[0], new Object[0]);

  /** Serialization version. */
  private static final long serialVersionUID = -43654613865187568L;

  /**
   * The times in the series.
   */
  private final long[] _times;
  /**
   * The values in the series.
   */
  private final V[] _values;

  //-------------------------------------------------------------------------
  /**
   * Obtains a time-series from a single date and value.
   * 
   * @param <V>  the value being viewed over time
   * @return the time-series builder, not null
   */
  public static <V> InstantObjectTimeSeriesBuilder<V> builder() {
    return new Builder<V>();
  }

  //-------------------------------------------------------------------------
  /**
   * Obtains a time-series from a single date and value.
   * 
   * @param <V>  the value being viewed over time
   * @return the time-series, not null
   */
  @SuppressWarnings("unchecked")
  public static <V> ImmutableInstantObjectTimeSeries<V> ofEmpty() {
    return (ImmutableInstantObjectTimeSeries<V>) EMPTY_SERIES;
  }

  /**
   * Obtains a time-series from a single instant and value.
   * 
   * @param <V>  the value being viewed over time
   * @param instant  the singleton instant, not null
   * @param value  the singleton value
   * @return the time-series, not null
   */
  public static <V> ImmutableInstantObjectTimeSeries<V> of(Instant instant, V value) {
    Objects.requireNonNull(instant, "date");
    long[] timesArray = new long[] {InstantToLongConverter.convertToLong(instant)};
    @SuppressWarnings("unchecked")
    V[] valuesArray = (V[]) new Object[] {value};
    return new ImmutableInstantObjectTimeSeries<V>(timesArray, valuesArray);
  }

  /**
   * Obtains a time-series from matching arrays of instants and values.
   * 
   * @param <V>  the value being viewed over time
   * @param instants  the instant array, not null
   * @param values  the value array, not null
   * @return the time-series, not null
   */
  public static <V> ImmutableInstantObjectTimeSeries<V> of(Instant[] instants, V[] values) {
    long[] timesArray = convertToLongArray(instants);
    V[] valuesArray = values.clone();
    validate(timesArray, valuesArray);
    return new ImmutableInstantObjectTimeSeries<V>(timesArray, valuesArray);
  }

  /**
   * Obtains a time-series from matching arrays of instants and values.
   * 
   * @param <V>  the value being viewed over time
   * @param instants  the instant array, not null
   * @param values  the value array, not null
   * @return the time-series, not null
   */
  public static <V> ImmutableInstantObjectTimeSeries<V> of(long[] instants, V[] values) {
    validate(instants, values);
    long[] timesArray = instants.clone();
    V[] valuesArray = values.clone();
    return new ImmutableInstantObjectTimeSeries<V>(timesArray, valuesArray);
  }

  /**
   * Obtains a time-series from matching arrays of instants and values.
   * 
   * @param <V>  the value being viewed over time
   * @param instants  the instant list, not null
   * @param values  the value list, not null
   * @return the time-series, not null
   */
  public static <V> ImmutableInstantObjectTimeSeries<V> of(Collection<Instant> instants, Collection<V> values) {
    long[] timesArray = convertToLongArray(instants);
    @SuppressWarnings("unchecked")
    V[] valuesArray = (V[]) values.toArray();
    validate(timesArray, valuesArray);
    return new ImmutableInstantObjectTimeSeries<V>(timesArray, valuesArray);
  }

  /**
   * Obtains a time-series from another time-series.
   * 
   * @param <V>  the value being viewed over time
   * @param timeSeries  the time-series, not null
   * @return the time-series, not null
   */
  @SuppressWarnings("unchecked")
  public static <V> ImmutableInstantObjectTimeSeries<V> of(PreciseObjectTimeSeries<?, V> timeSeries) {
    if (timeSeries instanceof ImmutableInstantObjectTimeSeries) {
      return (ImmutableInstantObjectTimeSeries<V>) timeSeries;
    }
    PreciseObjectTimeSeries<?, V> other = (PreciseObjectTimeSeries<?, V>) timeSeries;
    long[] timesArray = other.timesArrayFast();
    V[] valuesArray = other.valuesArray();
    return new ImmutableInstantObjectTimeSeries<V>(timesArray, valuesArray);
  }

  //-------------------------------------------------------------------------
  /**
   * Obtains a time-series from another time-series.
   * 
   * @param <V>  the value being viewed over time
   * @param timeSeries  the time-series, not null
   * @return the time-series, not null
   */
  public static <V> ImmutableInstantObjectTimeSeries<V> from(ObjectTimeSeries<Instant, V> timeSeries) {
    if (timeSeries instanceof PreciseObjectTimeSeries) {
      return of((PreciseObjectTimeSeries<Instant, V>) timeSeries);
    }
    long[] timesArray = convertToLongArray(timeSeries.timesArray());
    V[] valuesArray = timeSeries.valuesArray();
    return new ImmutableInstantObjectTimeSeries<V>(timesArray, valuesArray);
  }

  //-------------------------------------------------------------------------
  /**
   * Validates the data before creation.
   * 
   * @param <V>  the value being viewed over time
   * @param instants  the times, not null
   * @param values  the values, not null
   */
  private static <V> void validate(long[] instants, V[] values) {
    if (instants == null || values == null) {
      throw new NullPointerException("Array must not be null");
    }
    // check lengths
    if (instants.length != values.length) {
      throw new IllegalArgumentException("Arrays are of different sizes: " + instants.length + ", " + values.length);
    }
    // check dates are ordered
    long maxTime = Long.MIN_VALUE;
    for (long time : instants) {
      if (time < maxTime) {
        throw new IllegalArgumentException("Instants must be ordered");
      }
      maxTime = time;
    }
  }

  /**
   * Creates an instance.
   * 
   * @param instants  the times, not null
   * @param values  the values, not null
   */
  private ImmutableInstantObjectTimeSeries(long[] instants, V[] values) {
    _times = instants;
    _values = values;
  }

  //-------------------------------------------------------------------------
  static long[] convertToLongArray(Collection<Instant> instants) {
    long[] timesArray = new long[instants.size()];
    int i = 0;
    for (Instant instant : instants) {
      timesArray[i++] = InstantToLongConverter.convertToLong(instant);
    }
    return timesArray;
  }

  static long[] convertToLongArray(Instant[] instants) {
    long[] timesArray = new long[instants.length];
    for (int i = 0; i < timesArray.length; i++) {
      timesArray[i] = InstantToLongConverter.convertToLong(instants[i]);
    }
    return timesArray;
  }

  static <V> Entry<Instant, V> makeMapEntry(Instant key, V value) {
    return new SimpleImmutableEntry<Instant, V>(key, value);
  }

  //-------------------------------------------------------------------------
  @Override
  protected long convertToLong(Instant instant) {
    return InstantToLongConverter.convertToLong(instant);
  }

  @Override
  protected Instant convertFromLong(long instant) {
    return InstantToLongConverter.convertToInstant(instant);
  }

  @Override
  protected Instant[] createArray(int size) {
    return new Instant[size];
  }

  //-------------------------------------------------------------------------
  @Override
  public int size() {
    return _times.length;
  }

  //-------------------------------------------------------------------------
  @Override
  public boolean containsTime(long instant) {
    int binarySearch = Arrays.binarySearch(_times, instant);
    return (binarySearch >= 0);
  }

  @Override
  public V getValue(long instant) {
    int binarySearch = Arrays.binarySearch(_times, instant);
    if (binarySearch >= 0) {
      return _values[binarySearch];
    } else {
      return null;
    }
  }

  @Override
  public long getTimeAtIndexFast(int index) {
    return _times[index];
  }

  @Override
  public V getValueAtIndex(int index) {
    return _values[index];
  }

  //-------------------------------------------------------------------------
  @Override
  public long getEarliestTimeFast() {
    try {
      return _times[0];
    } catch (IndexOutOfBoundsException ex) {
      throw new NoSuchElementException("Series is empty");
    }
  }

  @Override
  public V getEarliestValue() {
    try {
      return _values[0];
    } catch (IndexOutOfBoundsException ex) {
      throw new NoSuchElementException("Series is empty");
    }
  }

  @Override
  public long getLatestTimeFast() {
    try {
      return _times[_times.length - 1];
    } catch (IndexOutOfBoundsException ex) {
      throw new NoSuchElementException("Series is empty");
    }
  }

  @Override
  public V getLatestValue() {
    try {
      return _values[_values.length - 1];
    } catch (IndexOutOfBoundsException ex) {
      throw new NoSuchElementException("Series is empty");
    }
  }

  //-------------------------------------------------------------------------
  @Override
  public long[] timesArrayFast() {
    return _times.clone();
  }

  @Override
  public V[] valuesArray() {
    return _values.clone();
  }

  //-------------------------------------------------------------------------
  @Override
  public InstantObjectEntryIterator<V> iterator() {
    return new InstantObjectEntryIterator<V>() {
      private int _index = -1;

      @Override
      public boolean hasNext() {
        return (_index + 1) < size();
      }

      @Override
      public Entry<Instant, V> next() {
        if (hasNext() == false) {
          throw new NoSuchElementException("No more elements in the iteration");
        }
        _index++;
        long date = ImmutableInstantObjectTimeSeries.this.getTimeAtIndexFast(_index);
        V value = ImmutableInstantObjectTimeSeries.this.getValueAtIndex(_index);
        return makeMapEntry(ImmutableInstantObjectTimeSeries.this.convertFromLong(date), value);
      }

      @Override
      public long nextTimeFast() {
        if (hasNext() == false) {
          throw new NoSuchElementException("No more elements in the iteration");
        }
        _index++;
        return ImmutableInstantObjectTimeSeries.this.getTimeAtIndexFast(_index);
      }

      @Override
      public Instant nextTime() {
        return ImmutableInstantObjectTimeSeries.this.convertFromLong(nextTimeFast());
      }

      @Override
      public long currentTimeFast() {
        if (_index < 0) {
          throw new IllegalStateException("Iterator has not yet been started");
        }
        return ImmutableInstantObjectTimeSeries.this.getTimeAtIndexFast(_index);
      }

      @Override
      public Instant currentTime() {
        return ImmutableInstantObjectTimeSeries.this.convertFromLong(currentTimeFast());
      }

      @Override
      public V currentValue() {
        if (_index < 0) {
          throw new IllegalStateException("Iterator has not yet been started");
        }
        return ImmutableInstantObjectTimeSeries.this.getValueAtIndex(_index);
      }

      @Override
      public int currentIndex() {
        return _index;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Immutable iterator");
      }
    };
  }

  //-------------------------------------------------------------------------
  @Override
  public InstantObjectTimeSeries<V> subSeries(Instant startInstant, Instant endInstant) {
    return subSeriesFast(convertToLong(startInstant), convertToLong(endInstant));
  }

  @Override
  public InstantObjectTimeSeries<V> subSeries(Instant startInstant, boolean includeStart, Instant endInstant, boolean includeEnd) {
    return subSeriesFast(convertToLong(startInstant), includeStart, convertToLong(endInstant), includeEnd);
  }

  @Override
  public InstantObjectTimeSeries<V> subSeriesFast(long startInstant, long endInstant) {
    int startPos = Arrays.binarySearch(_times, startInstant);
    int endPos = (endInstant == Integer.MIN_VALUE) ? _times.length : Arrays.binarySearch(_times, endInstant);
    startPos = startPos >= 0 ? startPos : -(startPos + 1);
    endPos = endPos >= 0 ? endPos : -(endPos + 1);
    if (endPos > _times.length) {
      endPos = _times.length;
    }
    long[] timesArray = Arrays.copyOfRange(_times, startPos, endPos);
    V[] valuesArray = Arrays.copyOfRange(_values, startPos, endPos);
    return new ImmutableInstantObjectTimeSeries<V>(timesArray, valuesArray);
  }

  @Override
  public InstantObjectTimeSeries<V> subSeriesFast(long startInstant, boolean includeStart, long endInstant, boolean includeEnd) {
    if (startInstant != endInstant || includeStart || includeEnd) {
      startInstant += (includeStart ? 0 : 1);
      endInstant += (includeEnd ? 1 : 0);
    }
    return subSeriesFast(startInstant, endInstant);
  }

  //-------------------------------------------------------------------------
  @Override
  public InstantObjectTimeSeries<V> head(int numItems) {
    if (numItems == size()) {
      return this;
    }
    long[] timesArray = Arrays.copyOfRange(_times, 0, numItems);
    V[] valuesArray = Arrays.copyOfRange(_values, 0, numItems);
    return new ImmutableInstantObjectTimeSeries<V>(timesArray, valuesArray);
  }

  @Override
  public InstantObjectTimeSeries<V> tail(int numItems) {
    int size = size();
    if (numItems == size) {
      return this;
    }
    long[] timesArray = Arrays.copyOfRange(_times, size - numItems, size);
    V[] valuesArray = Arrays.copyOfRange(_values, size - numItems, size);
    return new ImmutableInstantObjectTimeSeries<V>(timesArray, valuesArray);
  }

  @Override
  @SuppressWarnings("unchecked")
  public InstantObjectTimeSeries<V> lag(int days) {
    long[] times = timesArrayFast();
    V[] values = valuesArray();
    if (days == 0) {
      return new ImmutableInstantObjectTimeSeries<V>(times, values);
    } else if (days < 0) {
      if (-days < times.length) {
        long[] resultTimes = new long[times.length + days]; // remember days is -ve
        System.arraycopy(times, 0, resultTimes, 0, times.length + days);
        V[] resultValues = (V[]) new Object[times.length + days];
        System.arraycopy(values, -days, resultValues, 0, times.length + days);
        return new ImmutableInstantObjectTimeSeries<V>(resultTimes, resultValues);
      } else {
        return ImmutableInstantObjectTimeSeries.ofEmpty();
      }
    } else { // if (days > 0) {
      if (days < times.length) {
        long[] resultTimes = new long[times.length - days]; // remember days is +ve
        System.arraycopy(times, days, resultTimes, 0, times.length - days);
        V[] resultValues = (V[]) new Object[times.length - days];
        System.arraycopy(values, 0, resultValues, 0, times.length - days);
        return new ImmutableInstantObjectTimeSeries<V>(resultTimes, resultValues);
      } else {
        return ImmutableInstantObjectTimeSeries.ofEmpty();
      }
    }
  }

  //-------------------------------------------------------------------------
  @Override
  public ImmutableInstantObjectTimeSeries<V> newInstance(Instant[] dates, V[] values) {
    return of(dates, values);
  }

  //-------------------------------------------------------------------------
  @Override
  public InstantObjectTimeSeries<V> operate(UnaryOperator<V> operator) {
    V[] valuesArray = valuesArray();
    for (int i = 0; i < valuesArray.length; i++) {
      valuesArray[i] = operator.operate(valuesArray[i]);
    }
    return new ImmutableInstantObjectTimeSeries<V>(_times, valuesArray);  // immutable, so can share times
  }

  @Override
  public InstantObjectTimeSeries<V> operate(V other, BinaryOperator<V> operator) {
    V[] valuesArray = valuesArray();
    for (int i = 0; i < valuesArray.length; i++) {
      valuesArray[i] = operator.operate(valuesArray[i], other);
    }
    return new ImmutableInstantObjectTimeSeries<V>(_times, valuesArray);  // immutable, so can share times
  }

  private InstantObjectTimeSeries<V> operate(ObjectTimeSeries<?, V> other, BinaryOperator<V> operator) {
    if (other instanceof PreciseObjectTimeSeries) {
      return operate((PreciseObjectTimeSeries<?, V>) other, operator);
    }
    throw new UnsupportedOperationException("Can only operate on a PreciseObjectTimeSeries");
  }

  @Override
  @SuppressWarnings("unchecked")
  public InstantObjectTimeSeries<V> operate(PreciseObjectTimeSeries<?, V> other, BinaryOperator<V> operator) {
    long[] aTimes = timesArrayFast();
    V[] aValues = valuesArray();
    int aCount = 0;
    long[] bTimes = other.timesArrayFast();
    V[] bValues = other.valuesArray();
    int bCount = 0;
    long[] resTimes = new long[aTimes.length + bTimes.length];
    V[] resValues = (V[]) new Object[resTimes.length];
    int resCount = 0;
    while (aCount < aTimes.length && bCount < bTimes.length) {
      if (aTimes[aCount] == bTimes[bCount]) {
        resTimes[resCount] = aTimes[aCount];
        resValues[resCount] = operator.operate(aValues[aCount], bValues[bCount]);
        resCount++;
        aCount++;
        bCount++;
      } else if (aTimes[aCount] < bTimes[bCount]) {
        aCount++;
      } else { // if (aTimes[aCount] > bTimes[bCount]) {
        bCount++;
      }
    }
    long[] trimmedTimes = new long[resCount];
    V[] trimmedValues = (V[]) new Object[resCount];
    System.arraycopy(resTimes, 0, trimmedTimes, 0, resCount);
    System.arraycopy(resValues, 0, trimmedValues, 0, resCount);
    return new ImmutableInstantObjectTimeSeries<V>(trimmedTimes, trimmedValues);
  }

  @SuppressWarnings("unchecked")
  @Override
  public InstantObjectTimeSeries<V> unionOperate(PreciseObjectTimeSeries<?, V> other, BinaryOperator<V> operator) {
    long[] aTimes = timesArrayFast();
    V[] aValues = valuesArray();
    int aCount = 0;
    long[] bTimes = other.timesArrayFast();
    V[] bValues = other.valuesArray();
    int bCount = 0;
    long[] resTimes = new long[aTimes.length + bTimes.length];
    V[] resValues = (V[]) new Object[resTimes.length];
    int resCount = 0;
    while (aCount < aTimes.length || bCount < bTimes.length) {
      if (aCount >= aTimes.length) {
        int bRemaining = bTimes.length - bCount;
        System.arraycopy(bTimes, bCount, resTimes, resCount, bRemaining);
        System.arraycopy(bValues, bCount, resValues, resCount, bRemaining);
        resCount += bRemaining;
        break;
      } else if (bCount >= bTimes.length) {
        int aRemaining = aTimes.length - aCount;
        System.arraycopy(aTimes, aCount, resTimes, resCount, aRemaining);
        System.arraycopy(aValues, aCount, resValues, resCount, aRemaining);
        resCount += aRemaining;
        break;
      } else if (aTimes[aCount] == bTimes[bCount]) {
        resTimes[resCount] = aTimes[aCount];
        resValues[resCount] = operator.operate(aValues[aCount], bValues[bCount]);
        resCount++;
        aCount++;
        bCount++;
      } else if (aTimes[aCount] < bTimes[bCount]) {
        resTimes[resCount] = aTimes[aCount];
        resValues[resCount] = aValues[aCount];
        resCount++;
        aCount++;
      } else { // if (aTimes[aCount] > bTimes[bCount]) {
        resTimes[resCount] = bTimes[bCount];
        resValues[resCount] = bValues[bCount];
        resCount++;
        bCount++;
      }
    }
    long[] trimmedTimes = new long[resCount];
    V[] trimmedValues = (V[]) new Object[resCount];
    System.arraycopy(resTimes, 0, trimmedTimes, 0, resCount);
    System.arraycopy(resValues, 0, trimmedValues, 0, resCount);
    return new ImmutableInstantObjectTimeSeries<V>(trimmedTimes, trimmedValues);
  }

  //-------------------------------------------------------------------------
  @Override
  public InstantObjectTimeSeries<V> intersectionFirstValue(PreciseObjectTimeSeries<?, V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>firstOperator());
  }

  @Override
  public InstantObjectTimeSeries<V> intersectionSecondValue(PreciseObjectTimeSeries<?, V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>secondOperator());
  }

  @Override
  public InstantObjectTimeSeries<V> noIntersectionOperation(PreciseObjectTimeSeries<?, V> other) {
    return unionOperate(other, ObjectTimeSeriesOperators.<V>noIntersectionOperator());
  }

  //-------------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof ImmutableInstantObjectTimeSeries) {
      ImmutableInstantObjectTimeSeries<?> other = (ImmutableInstantObjectTimeSeries<?>) obj;
      return Arrays.equals(_times, other._times) &&
              Arrays.equals(_values, other._values);
    }
    if (obj instanceof PreciseObjectTimeSeries) {
      PreciseObjectTimeSeries<?, ?> other = (PreciseObjectTimeSeries<?, ?>) obj;
      return Arrays.equals(timesArrayFast(), other.timesArrayFast()) &&
              Arrays.equals(valuesArray(), other.valuesArray());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(timesArrayFast()) ^ Arrays.hashCode(valuesArray());
  }

  //-------------------------------------------------------------------------
  /**
   * The builder implementation.
   */
  private static final class Builder<V>
      extends AbstractPreciseObjectTimeSeriesBuilder<Instant, V>
      implements InstantObjectTimeSeriesBuilder<V> {

    @Override
    public InstantObjectTimeSeriesBuilder<V> put(Instant time, V value) {
      return (InstantObjectTimeSeriesBuilder<V>) super.put(time, value);
    }

    @Override
    public InstantObjectTimeSeriesBuilder<V> put(long time, V value) {
      return (InstantObjectTimeSeriesBuilder<V>) super.put(time, value);
    }

    @Override
    public InstantObjectTimeSeriesBuilder<V> putAll(Instant[] times, V[] values) {
      return (InstantObjectTimeSeriesBuilder<V>) super.putAll(times, values);
    }

    @Override
    public InstantObjectTimeSeriesBuilder<V> putAll(long[] times, V[] values) {
      return (InstantObjectTimeSeriesBuilder<V>) super.putAll(times, values);
    }

    @Override
    public InstantObjectTimeSeriesBuilder<V> putAll(PreciseObjectTimeSeries<?, V> timeSeries) {
      return (InstantObjectTimeSeriesBuilder<V>) super.putAll(timeSeries);
    }

    @Override
    public InstantObjectTimeSeriesBuilder<V> putAll(PreciseObjectTimeSeries<?, V> timeSeries, int startPos, int endPos) {
      return (InstantObjectTimeSeriesBuilder<V>) super.putAll(timeSeries, startPos, endPos);
    }

    @Override
    public InstantObjectTimeSeriesBuilder<V> putAll(Map<Instant, V> timeSeriesMap) {
      return (InstantObjectTimeSeriesBuilder<V>) super.putAll(timeSeriesMap);
    }

    @Override
    public InstantObjectTimeSeriesBuilder<V> clear() {
      return (InstantObjectTimeSeriesBuilder<V>) super.clear();
    }

    @Override
    protected long convertToLong(Instant instant) {
      return InstantToLongConverter.convertToLong(instant);
    }

    @Override
    public InstantObjectTimeSeries<V> build() {
      return (InstantObjectTimeSeries<V>) super.build();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected InstantObjectTimeSeries<V> createTimeSeries(SortedMap<Long, V> series) {
      long[] times = new long[series.size()];
      Object[] values = new Object[series.size()];
      int i = 0;
      for (Entry<Long, V> entry : series.entrySet()) {
        times[i] = entry.getKey();
        values[i++] = entry.getValue();
      }
      return new ImmutableInstantObjectTimeSeries<V>(times, (V[]) values);
    }
  }

  //-------------------------------------------------------------------------
  @Override
  public ObjectTimeSeries<Instant, V> intersectionFirstValue(ObjectTimeSeries<?, V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>firstOperator());
  }

  public ObjectTimeSeries<Instant, V> intersectionFirstValue(FastBackedObjectTimeSeries<?, V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>firstOperator());
  }

  public ObjectTimeSeries<Instant, V> intersectionFirstValue(FastIntObjectTimeSeries<V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>firstOperator());
  }

  public ObjectTimeSeries<Instant, V> intersectionFirstValue(FastLongObjectTimeSeries<V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>firstOperator());
  }

  public ObjectTimeSeries<Instant, V> intersectionSecondValue(ObjectTimeSeries<?, V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>secondOperator());
  }

  public ObjectTimeSeries<Instant, V> intersectionSecondValue(FastBackedObjectTimeSeries<?, V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>secondOperator());
  }

  public ObjectTimeSeries<Instant, V> intersectionSecondValue(FastLongObjectTimeSeries<V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>secondOperator());
  }

  public ObjectTimeSeries<Instant, V> intersectionSecondValue(FastIntObjectTimeSeries<V> other) {
    return operate(other, ObjectTimeSeriesOperators.<V>secondOperator());
  }

  //-------------------------------------------------------------------------
  @Override
  public FastIntObjectTimeSeries<V> toFastIntObjectTimeSeries() {
    throw new UnsupportedOperationException("Instant does not fit into an int");
  }

  @Override
  public FastIntObjectTimeSeries<V> toFastIntObjectTimeSeries(DateTimeNumericEncoding encoding) {
    return new FastArrayIntObjectTimeSeries<V>(encoding, toFastIntObjectTimeSeries());
  }

  @Override
  public FastMutableIntObjectTimeSeries<V> toFastMutableIntObjectTimeSeries() {
    return new FastListIntObjectTimeSeries<V>(toFastIntObjectTimeSeries());
  }

  @Override
  public FastMutableIntObjectTimeSeries<V> toFastMutableIntObjectTimeSeries(DateTimeNumericEncoding encoding) {
    return new FastListIntObjectTimeSeries<V>(encoding, toFastIntObjectTimeSeries());
  }

  //-------------------------------------------------------------------------
  @Override
  public FastLongObjectTimeSeries<V> toFastLongObjectTimeSeries() {
    return toFastLongObjectTimeSeries(DateTimeNumericEncoding.TIME_EPOCH_MILLIS);
  }

  @Override
  public FastLongObjectTimeSeries<V> toFastLongObjectTimeSeries(DateTimeNumericEncoding encoding) {
    return new FastArrayLongObjectTimeSeries<V>(encoding, _times, _values);
  }

  @Override
  public FastMutableLongObjectTimeSeries<V> toFastMutableLongObjectTimeSeries() {
    return new FastListLongObjectTimeSeries<V>(toFastLongObjectTimeSeries());
  }

  @Override
  public FastMutableLongObjectTimeSeries<V> toFastMutableLongObjectTimeSeries(DateTimeNumericEncoding encoding) {
    return new FastListLongObjectTimeSeries<V>(encoding, toFastLongObjectTimeSeries(encoding));
  }

}
