/^WARC-Target-URI/ { mode = 0 }
/^WARC-Target-URI: .*\.com\// { mode = 1; entries += 1 }
mode > 0 && /(youtu\.be\/|youtube\.com\/(watch\?(.*&)?v=|(embed|v)\/))([^\?&"'>]+)/ {
	if (mode == 1) {
		matching_entries += 1
		mode = 2
	}
	hits += 1
}

END {
	print "bash: " hits " of " matching_entries "/" entries
}
