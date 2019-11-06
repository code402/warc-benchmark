# wat-benchmark

This repository acts as a Hello World for working with WARC files.

Its subfolders contain implementations that fetch a WARC file and
search all captures from `.com` domains for a regex that detects
YouTube links.

See also [the blog post](https://code402.com/hello-warc-common-crawl-code-samples).

This is not bulletproof, production-ready code - I/O retries, closing
resources and robust character decoding is omitted to focus on the
WARC aspect of the code.
