/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableSet;
import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import com.google.common.testing.SerializableTester;

import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * Tests for {@code UnsignedLong}.
 *
 * @author Louis Wasserman
 */
@GwtCompatible(emulated = true)
public class UnsignedLongTest extends TestCase {
  private static final ImmutableSet<Long> TEST_LONGS;

  static {
    ImmutableSet.Builder<Long> testLongsBuilder = ImmutableSet.builder();
    for (long i = -3; i <= 3; i++) {
      testLongsBuilder
          .add(i)
          .add(Long.MAX_VALUE + i)
          .add(Long.MIN_VALUE + i)
          .add(Integer.MIN_VALUE + i)
          .add(Integer.MAX_VALUE + i);
    }
    TEST_LONGS = testLongsBuilder.build();
  }

  public void testAsUnsignedAndLongValueAreInverses() {
    for (long value : TEST_LONGS) {
      assertEquals(
          UnsignedLongs.toString(value), value, UnsignedLong.fromLongBits(value).longValue());
    }
  }

  public void testAsUnsignedBigIntegerValue() {
    for (long value : TEST_LONGS) {
      BigInteger expected = (value >= 0)
          ? BigInteger.valueOf(value)
          : BigInteger.valueOf(value).add(BigInteger.ZERO.setBit(64));
      assertEquals(UnsignedLongs.toString(value), expected,
          UnsignedLong.fromLongBits(value).bigIntegerValue());
    }
  }

  public void testToString() {
    for (long value : TEST_LONGS) {
      UnsignedLong unsignedValue = UnsignedLong.fromLongBits(value);
      assertEquals(unsignedValue.bigIntegerValue().toString(), unsignedValue.toString());
    }
  }

  @GwtIncompatible("too slow")
  public void testToStringRadix() {
    for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++) {
      for (long l : TEST_LONGS) {
        UnsignedLong value = UnsignedLong.fromLongBits(l);
        assertEquals(value.bigIntegerValue().toString(radix), value.toString(radix));
      }
    }
  }

  public void testToStringRadixQuick() {
    int[] radices = {2, 3, 5, 7, 10, 12, 16, 21, 31, 36};
    for (int radix : radices) {
      for (long l : TEST_LONGS) {
        UnsignedLong value = UnsignedLong.fromLongBits(l);
        assertEquals(value.bigIntegerValue().toString(radix), value.toString(radix));
      }
    }
  }

  public void testFloatValue() {
    for (long value : TEST_LONGS) {
      UnsignedLong unsignedValue = UnsignedLong.fromLongBits(value);
      assertEquals(unsignedValue.bigIntegerValue().floatValue(), unsignedValue.floatValue());
    }
  }

  public void testDoubleValue() {
    for (long value : TEST_LONGS) {
      UnsignedLong unsignedValue = UnsignedLong.fromLongBits(value);
      assertEquals(unsignedValue.bigIntegerValue().doubleValue(), unsignedValue.doubleValue());
    }
  }

  public void testPlus() {
    for (long a : TEST_LONGS) {
      for (long b : TEST_LONGS) {
        UnsignedLong aUnsigned = UnsignedLong.fromLongBits(a);
        UnsignedLong bUnsigned = UnsignedLong.fromLongBits(b);
        long expected = aUnsigned
            .bigIntegerValue()
            .add(bUnsigned.bigIntegerValue())
            .longValue();
        UnsignedLong unsignedSum = aUnsigned.plus(bUnsigned);
        assertEquals(expected, unsignedSum.longValue());
      }
    }
  }

  public void testMinus() {
    for (long a : TEST_LONGS) {
      for (long b : TEST_LONGS) {
        UnsignedLong aUnsigned = UnsignedLong.fromLongBits(a);
        UnsignedLong bUnsigned = UnsignedLong.fromLongBits(b);
        long expected = aUnsigned
            .bigIntegerValue()
            .subtract(bUnsigned.bigIntegerValue())
            .longValue();
        UnsignedLong unsignedSub = aUnsigned.minus(bUnsigned);
        assertEquals(expected, unsignedSub.longValue());
      }
    }
  }

  public void testTimes() {
    for (long a : TEST_LONGS) {
      for (long b : TEST_LONGS) {
        UnsignedLong aUnsigned = UnsignedLong.fromLongBits(a);
        UnsignedLong bUnsigned = UnsignedLong.fromLongBits(b);
        long expected = aUnsigned
            .bigIntegerValue()
            .multiply(bUnsigned.bigIntegerValue())
            .longValue();
        UnsignedLong unsignedMul = aUnsigned.times(bUnsigned);
        assertEquals(expected, unsignedMul.longValue());
      }
    }
  }

  public void testDividedBy() {
    for (long a : TEST_LONGS) {
      for (long b : TEST_LONGS) {
        if (b != 0) {
          UnsignedLong aUnsigned = UnsignedLong.fromLongBits(a);
          UnsignedLong bUnsigned = UnsignedLong.fromLongBits(b);
          long expected = aUnsigned
              .bigIntegerValue()
              .divide(bUnsigned.bigIntegerValue())
              .longValue();
          UnsignedLong unsignedDiv = aUnsigned.dividedBy(bUnsigned);
          assertEquals(expected, unsignedDiv.longValue());
        }
      }
    }
  }

  public void testDivideByZeroThrows() {
    for (long a : TEST_LONGS) {
      try {
        UnsignedLong.fromLongBits(a).dividedBy(UnsignedLong.ZERO);
        fail("Expected ArithmeticException");
      } catch (ArithmeticException expected) {}
    }
  }

  public void testMod() {
    for (long a : TEST_LONGS) {
      for (long b : TEST_LONGS) {
        if (b != 0) {
          UnsignedLong aUnsigned = UnsignedLong.fromLongBits(a);
          UnsignedLong bUnsigned = UnsignedLong.fromLongBits(b);
          long expected = aUnsigned
              .bigIntegerValue()
              .remainder(bUnsigned.bigIntegerValue())
              .longValue();
          UnsignedLong unsignedRem = aUnsigned.mod(bUnsigned);
          assertEquals(expected, unsignedRem.longValue());
        }
      }
    }
  }

  public void testModByZero() {
    for (long a : TEST_LONGS) {
      try {
        UnsignedLong.fromLongBits(a).mod(UnsignedLong.ZERO);
        fail("Expected ArithmeticException");
      } catch (ArithmeticException expected) {}
    }
  }

  public void testCompare() {
    for (long a : TEST_LONGS) {
      for (long b : TEST_LONGS) {
        UnsignedLong aUnsigned = UnsignedLong.fromLongBits(a);
        UnsignedLong bUnsigned = UnsignedLong.fromLongBits(b);
        assertEquals(aUnsigned.bigIntegerValue().compareTo(bUnsigned.bigIntegerValue()),
            aUnsigned.compareTo(bUnsigned));
      }
    }
  }

  @GwtIncompatible("too slow")
  public void testEquals() {
    EqualsTester equalsTester = new EqualsTester();
    for (long a : TEST_LONGS) {
      BigInteger big =
          (a >= 0) ? BigInteger.valueOf(a) : BigInteger.valueOf(a).add(BigInteger.ZERO.setBit(64));
      equalsTester.addEqualityGroup(UnsignedLong.fromLongBits(a), UnsignedLong.valueOf(big),
          UnsignedLong.valueOf(big.toString()), UnsignedLong.valueOf(big.toString(16), 16));
    }
    equalsTester.testEquals();
  }

  public void testIntValue() {
    for (long a : TEST_LONGS) {
      UnsignedLong aUnsigned = UnsignedLong.fromLongBits(a);
      int intValue = aUnsigned.bigIntegerValue().intValue();
      assertEquals(intValue, aUnsigned.intValue());
    }
  }

  @GwtIncompatible("serialization")
  public void testSerialization() {
    for (long a : TEST_LONGS) {
      SerializableTester.reserializeAndAssert(UnsignedLong.fromLongBits(a));
    }
  }

  @GwtIncompatible("NullPointerTester")
  public void testNulls() {
    new NullPointerTester().testAllPublicStaticMethods(UnsignedLong.class);
  }
}
