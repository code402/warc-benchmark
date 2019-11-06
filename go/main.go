package main

import (
	"fmt"
	"github.com/wolfgangmeyers/go-warc/warc"
	"io"
	"net/http"
	"os"
	"regexp"
	"strings"
)

func main() {
	filename := "http://commoncrawl.s3.amazonaws.com/crawl-data/CC-MAIN-2019-30/segments/1563195523840.34/warc/CC-MAIN-20190715175205-20190715200159-00000.warc.gz"
	if len(os.Args) > 1 {
		filename = os.Args[1]
	}

	var hits, entries, matchingEntries int
	var err error
	var f io.ReadCloser

	if strings.Index(filename, "http://") == 0 || strings.Index(filename, "https://") == 0 {
		resp, err := http.Get(filename)
		if err != nil {
			panic(err)
		}
		f = resp.Body
	} else {
		f, err = os.Open(filename)
		if err != nil {
			panic(err)
		}
	}
	defer f.Close()
	wf, err := warc.NewWARCFile(f)
	if err != nil {
		panic(err)
	}

	re := regexp.MustCompile("(youtu\\.be/|youtube\\.com/(watch\\?(.*&)?v=|(embed|v)/))([^?&\"'>]+)")
	reader := wf.GetReader()
	reader.Iterate(func(wr *warc.WARCRecord, err error) {
		if err != nil && err.Error() == "EOF" {
			return
		}

		if err != nil {
			panic(err)
		}

		if !strings.Contains(wr.GetUrl(), ".com/") {
			return
		}

		entries++

		contents, err := wr.GetPayload().Read(-1)
		if err != nil {
			panic(err)
		}
		matches := re.FindAll(contents, -1)
		if matches != nil {
			matchingEntries++
			hits += len(matches)
		}
	})

	fmt.Printf("go: %d of %d/%d\n", hits, matchingEntries, entries)
}
