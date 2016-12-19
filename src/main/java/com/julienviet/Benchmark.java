/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.julienviet;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Threads(2)
@BenchmarkMode(Mode.Throughput)
@Fork(value = 1, jvmArgs = { "-XX:-UseBiasedLocking" })
public class Benchmark {

  @State(Scope.Thread)
  public static class ThreadState {
    private final Object o = new Object();
    private int value;
  }

  @State(Scope.Group)
  public static class GroupState {
    private final Object o = new Object();
    private int value;
  }

  @State(Scope.Group)
  public static class GroupVolatileState {
    private volatile int value;
  }

  @State(Scope.Thread)
  public static class ThreadVolatileState {
    private volatile int value;
  }

  @Group("baseline")
  @org.openjdk.jmh.annotations.Benchmark
  public int baseline1(ThreadState state1) {
    return ++state1.value;
  }

  @Group("baseline")
  @org.openjdk.jmh.annotations.Benchmark
  public int baseline2(ThreadState state1) {
    return ++state1.value;
  }

  @Group("contented_lock")
  @org.openjdk.jmh.annotations.Benchmark
  public int contentedLock1(GroupState state) {
    synchronized (state.o) {
      return ++state.value;
    }
  }

  @Group("contented_lock")
  @org.openjdk.jmh.annotations.Benchmark
  public int contentedLock2(GroupState state) {
    synchronized (state.o) {
      return ++state.value;
    }
  }

  @Group("uncontended_lock")
  @org.openjdk.jmh.annotations.Benchmark
  public int uncontendedLock1(ThreadState state) {
    synchronized (state.o) {
      return ++state.value;
    }
  }

  @Group("uncontended_lock")
  @org.openjdk.jmh.annotations.Benchmark
  public int uncontendedLock2(ThreadState state) {
    synchronized (state.o) {
      return ++state.value;
    }
  }

  @Group("uncontended_volatile")
  @org.openjdk.jmh.annotations.Benchmark
  public int volatile1(ThreadVolatileState state) {
    return ++state.value;
  }

  @Group("uncontended_volatile")
  @org.openjdk.jmh.annotations.Benchmark
  public int volatile2(ThreadVolatileState state) {
    return ++state.value;
  }

  @Group("contented_volatile")
  @org.openjdk.jmh.annotations.Benchmark
  public int sharedVolatile1(GroupVolatileState state) {
    return ++state.value;
  }

  @Group("contended_sharedvolatile")
  @org.openjdk.jmh.annotations.Benchmark
  public int sharedVolatile2(GroupVolatileState state) {
    return ++state.value;
  }
}
