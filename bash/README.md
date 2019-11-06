# bash

I do not recommend using bash for big data.

But let's do it anyway! This will correctly count all entries
that have at least one hit, but may undercount multiple hits in
a single entry if they occur without an intervening newline.

## Running

```bash
time ./decompress  # Just the time to fetch and decompress a WARC

time ./search      # Fetch, decompress and search the WARC
```

You can optionally provide your own WARC file to be searched as an argument
after the script name.
