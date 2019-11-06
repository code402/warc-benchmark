const fs = require("fs");
const http = require("http");
const process = require("process");
const { recordIterator } = require("node-warc");
const zlib = require("zlib");

const re = /(youtu\.be\/|youtube\.com\/(watch\?(.*&)?v=|(embed|v)\/))([^\?&"'>]+)/g;

let entries = 0;
let matchingEntries = 0;
let hits = 0;
async function iterateRecords(warcStream) {
  for await (const record of recordIterator(warcStream)) {
    if (
      record.warcHeader["WARC-Target-URI"] &&
      record.warcHeader["WARC-Target-URI"].indexOf(".com/") < 0
    )
      continue;

    entries++;
    const string = record.content.toString("utf-8");
    let found = false;
    while (re.exec(string)) {
      if (!found) {
        found = true;
        matchingEntries++;
      }
      hits++;
    }
  }
}

const fileName =
  process.argv.length > 2
    ? process.argv[2]
    : "http://commoncrawl.s3.amazonaws.com/crawl-data/CC-MAIN-2019-30/segments/1563195523840.34/warc/CC-MAIN-20190715175205-20190715200159-00000.warc.gz";

if (fileName.indexOf("http://") == 0) {
  http.get(fileName, res => {
    iterateRecords(res.pipe(zlib.createGunzip())).then(() => {
      console.log(`node-warc: ${hits} of ${matchingEntries}/${entries}`);
    });
  });
} else {
  iterateRecords(fs.createReadStream(fileName).pipe(zlib.createGunzip())).then(
    () => {
      console.log(`node-warc: ${hits} of ${matchingEntries}/${entries}`);
    }
  );
}
