from warcio.archiveiterator import ArchiveIterator
import re
import requests
import sys

regex = re.compile(
    "(youtu\.be/|youtube\.com/(watch\?(.*\&)?v=|(embed|v)/))([^?&\"'>]+)"
)

entries = 0
matching_entries = 0
hits = 0

file_name = "http://commoncrawl.s3.amazonaws.com/crawl-data/CC-MAIN-2019-30/segments/1563195523840.34/warc/CC-MAIN-20190715175205-20190715200159-00000.warc.gz"

if len(sys.argv) > 1:
    file_name = sys.argv[1]

stream = None
if file_name.startswith("http://") or file_name.startswith(
    "https://"
):
    stream = requests.get(file_name, stream=True).raw
else:
    stream = open(file_name, "rb")

for record in ArchiveIterator(stream):
    if record.rec_type == "warcinfo":
        continue

    if not ".com/" in record.rec_headers.get_header(
        "WARC-Target-URI"
    ):
        continue

    entries = entries + 1
    contents = (
        record.content_stream()
        .read()
        .decode("utf-8", "replace")
    )
    m = regex.search(contents)
    if m:
        matching_entries = matching_entries + 1
        hits = hits + 1
        m = regex.search(contents, m.end())

    while m:
        m = regex.search(contents, m.end())
        hits = hits + 1

print(
    "Python: "
    + str(hits)
    + " matches in "
    + str(matching_entries)
    + "/"
    + str(entries)
)
