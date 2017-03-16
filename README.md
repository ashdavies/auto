### Auto
[![Build Status](https://img.shields.io/travis/ashdavies/auto.svg)](https://travis-ci.org/ashdavies/auto)
[![Coverage](https://img.shields.io/codecov/c/github/ashdavies/auto.svg)](https://codecov.io/github/ashdavies/auto)
[![Issues](https://img.shields.io/github/issues/ashdavies/auto.svg)](https://github.com/ashdavies/auto/issues)
[![Pull Requests](https://img.shields.io/github/issues-pr/ashdavies/auto.svg)](https://github.com/ashdavies/auto/pulls)

[![License](https://img.shields.io/badge/license-apache%202.0-blue.svg)](https://github.com/ashdavies/auto/blob/master/LICENSE.txt)
[![Versions](https://img.shields.io/badge/version-0.6.0-yellowgreen.svg)](https://github.com/ashdavies/auto/blob/master/CHANGELOG.md)

### Auto: Decorator
[![Bintray](https://img.shields.io/bintray/v/ashdavies/maven/auto-decorator.svg)](https://bintray.com/ashdavies/maven/auto-decorator)
[![Bintray](https://img.shields.io/bintray/v/ashdavies/maven/auto-decorator-compiler.svg)](https://bintray.com/ashdavies/maven/auto-compiler)

#### Description
Generate decorated implementations for interfaces or abstract classes.

#### Usage
```gradle
dependencies {
  compile 'io.ashdavies.auto:auto-decorator:{current-version}'
  annotationProcessor 'io.ashdavies.auto:auto-decorator-compiler:{current-version}'
}
```

**Normal Usage**
Generates a simple non operation instance of an interface.

```java
@AutoDecorator
interface Listener {

  void onError(Throwable throwable);
}
```

```java
listener = new ListenerDecorator(decorated);
```

**Inner Class Usage**
If you annotate an inner class its name will be prefixed with the outer class.

```java
class Presenter {

  @AutoDecorator
  interface Listener {

    void onError(Throwable throwable);
  }
}
```

```java
listener = new Presenter$ListenerDecorator(decorated);
```

**Iterable Usage**
Optionally you can generate an implementation that accepts multiple decorated classes.
This only works for methods without a return type since the correct return cannot be distinguished.
Return types will be supported with the implementation of merge strategies.

```java
@AutoDecorator(iterable = true)
interface Listener {

  void onError(Throwable throwable)
}
```

```java
listener = new ListenerDecorator(first, second, third);
```

### Auto: No-Op
[![Bintray](https://img.shields.io/bintray/v/ashdavies/maven/auto-no-op.svg)](https://bintray.com/ashdavies/maven/auto-no-op)
[![Bintray](https://img.shields.io/bintray/v/ashdavies/maven/auto-no-op-compiler.svg)](https://bintray.com/ashdavies/maven/auto-no-op-compiler)

#### Description
Generate non operational implementations for interfaces or abstract classes.

**Normal Usage**
```java
@AutoNoOp
interface Listener {

  void onError(Throwable throwable);
}
```

```java
listener = new ListenerNoOp();
```

**Instance Usage**
Optionally, you can instruct the processor to generate a static instance for repeated usage.

```java
@AutoNoOp(instance = true)
interface Listener {

  void onError(Throwable throwable);
}
```

```java
listener = ListenerNoOp.instance();
```
