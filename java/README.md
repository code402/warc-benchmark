# Java

There are three implementations. Each uses the IIPC's [jwarc](https://github.com/iipc/jwarc)
library for parsing WARC files. They differ only in the regular expression
engine that is used - the default one that ships with the JDK, Google's [re2j](https://github.com/google/re2j),
and Anders Møller's [dk.brics.automaton](https://www.brics.dk/automaton/) library.

## Running

```bash
./go JDK   # Use the JDK's engine

./go Brics # Use the Brics engine

./go Re2j  # Use the Re2j engine
```

You can optionally provide your own WARC file to be searched as a second argument.
